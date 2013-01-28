/* 
 * $Id: EventReaderImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.subscriber;

import es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event;

import java.io.ByteArrayInputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

/**
 * Component implementation for retrieving the incoming events in F4BPA-BPAF format 
 * from the message queue. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=EventReader.COMPONENT_NAME)
public class EventReaderImpl implements EventReader {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventReaderImpl.class);
	
	/**
	 * Extract the events in F4BPA-BPAF extension format from messages retrieved from 
	 * the jms queue.
	 * 
	 * @param msg event message object containing events in F4BPA-BPAF format.
	 * @return all F4BPA-BPAF events extracted from the message.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	public Event extractEvent(Message msg) throws EventReaderException {
		Event event = null;
		try {
			logger.debug("JMS message ID: " + msg.getJMSMessageID());
			Object xmlEvent = ((ObjectMessage)msg).getObject();				
			byte[] xml = (byte[])xmlEvent;			
			logger.info(new String(xml));

			ByteArrayInputStream bios = new ByteArrayInputStream(xml);
			/* create a JAXBContext capable of handling classes */ 
			JAXBContext jc = JAXBContext.newInstance("es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension");			
			/* create an Unmarshaller */
			Unmarshaller u = jc.createUnmarshaller();        			
			/* Performs an xml validation against the F4BPA-BPAF schema */ 
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(getClass().getResource(StaticResources.CBI4PI_SLAVE_BPAF_XML_SCHEMA_CLASSPATH_FILE));
			u.setSchema(schema);			
			/* unmarshal an instance document into a tree of Java content objects. */			
			event = (Event) u.unmarshal(bios);
			logger.debug("Processing event message: " + event.getEventID());
		} catch(JMSException jmsex) {
			logger.error(jmsex.fillInStackTrace());	
			throw new EventReaderException(jmsex);
		} catch (JAXBException jaxb) {
			logger.error(jaxb.fillInStackTrace());
			throw new EventReaderException(jaxb);
		} catch(Exception ioex) {
			logger.error(ioex.fillInStackTrace());
			throw new EventReaderException(ioex);
		}
		return event;
	}
}