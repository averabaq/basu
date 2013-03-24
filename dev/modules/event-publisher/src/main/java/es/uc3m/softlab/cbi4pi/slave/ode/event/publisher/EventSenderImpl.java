/* 
 * $Id: EventSenderImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher;

import es.uc3m.softlab.cbi4api.basu.ode.event.publisher.xsd.bpaf.extension.Event;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for sending the bpel events to a messaging queue. 
 * This interface defines all methods for sending the <strong>event</strong> 
 * entity data managed through the ApacheODE API (1.3.5) and based upon 
 * the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventSender.COMPONENT_NAME)
public class EventSenderImpl implements EventSender {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventPublisherContainer.class);
	/** JMS template */
	@Autowired private JmsTemplate template;
	/** JMS message queue/topic destination */
	@Autowired private Destination destination;
    /** Configuration object */
    @Autowired private Config config;
	
	/**
	 * Sends a list of events to the messaging queue defined.
	 * @param events list of events to publish. 
	 * @throws JMSException if any error occurred during messaging publication.
	 */
	public void publishEvents(List<Event> events) throws JMSException {
		for (Event event : events) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  	    	
				JAXBContext context = JAXBContext.newInstance(Event.class);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				logger.debug("Marshalling event " + event + "...");
				marshaller.marshal(event, writer);
				writer.close();
				baos.close();
				final byte[] xml = baos.toByteArray();					
				template.send(destination, new MessageCreator() {
					public Message createMessage(Session session)
					throws JMSException {
						logger.info("Sending xml event: " + new String(xml));
						ObjectMessage message = session.createObjectMessage(xml);
						//message.setJMSReplyTo(arg0);
						return message;
					}
				});
				/* 
				 * makes sure to persist on disk the last event data just
				 * when the event has been successfully published into the
				 * messaging queue.  
				 */
				synchronized (event) {
					config.writeLastEvent(Long.valueOf(event.getEventID()));
				}
			} catch(JAXBException jaxbex) {
				logger.error("Cannot send event " + event + " due to a JAXB marshalling error.");
				logger.error(jaxbex.fillInStackTrace());
				throw new JMSException(jaxbex.getLocalizedMessage());
			} catch(IOException ioex) {
				logger.error("Cannot send event " + event + " due to an input/out error marshalling the event.");
				logger.error(ioex.fillInStackTrace());
				throw new JMSException(ioex.getLocalizedMessage());
			}
		}
	}
	/**
	 * 
	 * @throws JMSException
	 */
	public void publishProcessModels() throws JMSException {
			/*
			DbConfStoreConnectionFactory confStoreFactory = new DbConfStoreConnectionFactory(bpelDAO.getDataSource(), false, OdeConfigProperties.DEFAULT_TX_FACTORY_CLASS_NAME);
			Collection<DeploymentUnitDAO> dunits = confStoreFactory.getConnection().getDeploymentUnits();
			for (DeploymentUnitDAO dudao : dunits) {								
				String deployDir = dudao.getDeploymentUnitDir();	
				String[] extensions = {".bpel"};
				Collection<File> bpelFiles = FileUtils.listFiles(new File(deployDir), extensions, false);
				for (File bpelFile : bpelFiles) {
					logger.info("Process model: " + dudao.getName() + " - " + bpelFile.getName());
				}
				for (ProcessConfDAO pconf : dudao.getProcesses()) {
					logger.info("Process: " + pconf.getPID() + ":" + pconf.getType() + " [" + pconf.getState().toString() + "].");
					
					ProcessDAO process = bpelDAO.getConnection().getProcess(pconf.getPID());
					Collection<ProcessInstanceDAO> instances = bpelDAO.getConnection().instanceQuery("pid = " + process.getProcessId());
					for (ProcessInstanceDAO instance : instances) {
						logger.info("Process instance: " + instance.getInstanceId() + ":" + " [" + ProcessState.stateToString(instance.getPreviousState()) + " -> " + ProcessState.stateToString(instance.getState()) + "].");
						
					}
				}
			}*/
	}				

	public void stop() throws JMSException {
	}
	/**
	 * Gets the {@link #template} property.
	 * @return the {@link #template} property.
	 */
	public JmsTemplate getTemplate() {
		return template;
	}
	/**
	 * Sets the {@link #template} property.
	 * @param template the {@link #template} property to set.
	 */
	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}
	/**
	 * Gets the {@link #destination} property.
	 * @return the {@link #destination} property.
	 */
	public Destination getDestination() {
		return destination;
	}
	/**
	 * Sets the {@link #destination} property.
	 * @param destination the {@link #destination} property to set.
	 */
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
