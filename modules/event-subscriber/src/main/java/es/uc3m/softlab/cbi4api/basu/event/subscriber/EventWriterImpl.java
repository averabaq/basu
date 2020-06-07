/*
 * $Id: EventWriterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventFacade;

import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.ActivityParent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component implementation for writing events to the global data store.
 * This interface defines all methods for storing <strong>event</strong>
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 *
 * @author averab
 * @version 1.0.0
 */
@Component(value=EventWriter.COMPONENT_NAME)
public class EventWriterImpl implements EventWriter {
    /** Logger for tracing */
    private Logger logger = Logger.getLogger(EventWriterImpl.class);
    /** Configuration object */
    @Autowired private Config config;
    /** Event session facade */
    @Autowired private EventFacade eventFacade;
    /** JMS template */
    @Produce(property="gbasTemplate", ref="gbas.bpaf.queue.endpoint")
    private ProducerTemplate producer;

    /**
     * Stores the event in the data source and sets the processed event back to be forwarded to the
     * output the channel in xml format.
     * @param exchange exchange data object to route.
     * @param bpafEvent BPAF event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} to store
     * @param basuEvent BASU event {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event}
     *  object to forward in xml format to the GBAS output channel.
     * @throws EventException if any illegal data access or inconsistent event data error occurred.
     */
    @Override
    public void loadEvent(Exchange exchange, Event bpafEvent, es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event basuEvent) throws EventException {
        // store the BPAF event in the data source
        storeEvent(bpafEvent);
        // creates the GBAS event messages with specific BASU instance data (updated after being stored)
        EventMessage msg = createGbasEventMessages(bpafEvent, basuEvent);
        // forwards the events to GBAS channel
        for (es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event evt : msg.getGbasEvents()) {
            // marshall back the processed event
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
            try {
                JAXBContext context = JAXBContext.newInstance(es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                logger.debug("Marshalling event " + evt + "...");
                marshaller.marshal(evt, writer);
                final byte[] xml = baos.toByteArray();
                // sets the objects back to the channel
                producer.send(new Processor() {
                    public void process(Exchange outExchange) {
                        outExchange.getIn().setBody(xml, byte[].class);
                    }
                });
            } catch (JAXBException jaxbex) {
                logger.error(jaxbex.fillInStackTrace());
                throw new EventException(jaxbex);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ioex) {
                        logger.error(ioex.fillInStackTrace());
                    }
                }
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException ioex) {
                        logger.error(ioex.fillInStackTrace());
                    }
                }
            }
        }
    }
    /**
     * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
     * entity object state with the data source.
     *
     * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
     * entity object to save.
     * @throws EventException if any illegal data access or inconsistent event data error occurred.
     */
    private void storeEvent(Event event) throws EventException {
        logger.debug("Saving the event " + event + "...");
        eventFacade.storeEvent(event);
        logger.info("Event " + event + " stored successfully.");
    }
    /**
     * Synchronize the incoming message attributes from external sources (listeners) with
     * the BASU managed data in BPAF format and creates the associated event messages.
     * @param bpafEvent BPAF stored event.
     * @param basuEvent BASU incoming event.
     */
    private EventMessage createGbasEventMessages(Event bpafEvent, es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event basuEvent) {
        EventMessage msg = new EventMessage();
        createGbasProcessMessage(msg, bpafEvent, basuEvent);
        createGbasActivityMessage(msg, bpafEvent, basuEvent);
        msg.setDomainEvent(bpafEvent);
        return msg;
    }
    /**
     *
     * @param msg
     * @param basuEvent
     */
    private void createGbasProcessMessage(EventMessage msg, Event bpafEvent, es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event basuEvent) {
        // the event process must converted into a sub-process (activity) in GBAS.
        es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event activity = EventUtils.cloneEvent(basuEvent);
        activity.setServerID(config.getNodeId());
        activity.setProcessDefinitionID(StaticResources.GBAS_GLOBAL_PROCESS_DEFINITION_ID);
        activity.setActivityDefinitionID(basuEvent.getProcessDefinitionID());
        activity.setActivityInstanceID(bpafEvent.getProcessInstanceID());
        activity.setActivityName(basuEvent.getProcessName());
        activity.getCorrelation().setProcessInstanceID(null);
        // add the GBAS sub-process
        msg.getGbasEvents().add(activity);
    }
    /**
     *
     * @param msg
     * @param basuEvent
     */
    private void createGbasActivityMessage(EventMessage msg, Event bpafEvent, es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event basuEvent) {
        // in case of an activity, the event must converted into a sub-activity in GBAS.
        if (StringUtils.isNotBlank(bpafEvent.getActivityDefinitionID())) {
            es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event subactivity = EventUtils.cloneEvent(basuEvent);
            subactivity.setServerID(config.getNodeId());
            subactivity.setProcessDefinitionID(StaticResources.GBAS_GLOBAL_PROCESS_DEFINITION_ID);
            subactivity.setActivityDefinitionID(basuEvent.getActivityDefinitionID());
            subactivity.setActivityInstanceID(bpafEvent.getActivityInstanceID());
            subactivity.setActivityParent(new es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.ActivityParent());
            subactivity.getActivityParent().setActivityDefinitionID(basuEvent.getProcessDefinitionID());
            subactivity.getActivityParent().setActivityInstanceID(bpafEvent.getProcessInstanceID());
            subactivity.getCorrelation().setProcessInstanceID(null);
            // add the GBAS sub-activity
            msg.getGbasEvents().add(subactivity);

            // multiple nested levels are not yet supported
            /*
             * if (StringUtils.isNotBlank(bpafEvent.getActivityParentID())) {
             *    createGbasActivityMessage(msg, bpafEvent, subactivity);
             * }
             */
        }
    }
}