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

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
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
	/** Event session facade */
    @Autowired private EventFacade eventFacade;
	
    /**
     * Stores the event in the data source and sets the processed event back to be forwarded to the
     * output the channel in xml format.  
     * @param exchange exchange data object to route. 
     * @param event event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object to forward in xml format to the output channel.
     * @throws EventException if any illegal data access or inconsistent event data error occurred.
     */
    public void loadEvent(Exchange exchange, Event event) throws EventException {
    	// store the event in the data source
    	storeEvent(event);
    	// marshall back the processed event 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  	    
    	try {
    		JAXBContext context = JAXBContext.newInstance(Event.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		logger.debug("Marshalling event " + event + "...");
    		marshaller.marshal(event, writer);
    		final byte[] xml = baos.toByteArray();
    		// sets the objects back to the channel
    		exchange.getOut().setBody(xml, byte[].class);  
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
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object state with the data source.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object to save.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void storeEvent(Event event) throws EventException {
		logger.debug("Saving the event " + event + "...");
		eventFacade.storeEvent(event);
		logger.info("Event " + event + " stored successfully.");
	} 
}