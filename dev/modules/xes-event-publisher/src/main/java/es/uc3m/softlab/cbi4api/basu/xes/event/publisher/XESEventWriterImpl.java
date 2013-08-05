/* 
 * $Id: XESEventWriterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;

/**
 * Component implementation for writing events to the global data store. 
 * This interface defines all methods for storing <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=XESEventWriter.COMPONENT_NAME)
public class XESEventWriterImpl implements XESEventWriter {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(XESEventWriterImpl.class);
	/** JMS template */
	@Autowired private JmsTemplate jmsTemplate;
	/** JMS message queue/topic destination */
	@Autowired private Topic bpafDestination;

	/**
	 * Publish a list of BPAF events in extended format throughout the publisher
	 * jms queue.
	 * 
	 * @param events list of {@link es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event}
	 * objects to send in XML representation.
	 * @throws XESEventWriterException if any illegal data access or inconsistent event data error occurred.
	 */
	@Override
	public void sendEvents(List<Event> events) throws XESEventWriterException {
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
				jmsTemplate.send(bpafDestination, new MessageCreator() {
					public Message createMessage(Session session)
					throws JMSException {
						logger.info("Sending xml event: " + new String(xml) + " to " + bpafDestination.getTopicName());
						ObjectMessage message = session.createObjectMessage(xml);						
						//message.setJMSReplyTo(arg0);
						return message;
					}
				});
			} catch(JAXBException jaxbex) {
				logger.error("Cannot send event " + event + " due to a JAXB marshalling error.");
				logger.error(jaxbex.fillInStackTrace());
				throw new XESEventWriterException(jaxbex.getLocalizedMessage());
			} catch(IOException ioex) {
				logger.error("Cannot send event " + event + " due to an input/out error marshalling the event.");
				logger.error(ioex.fillInStackTrace());
				throw new XESEventWriterException(ioex.getLocalizedMessage());
			}
		}
	} 
}