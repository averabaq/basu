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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
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
	@Produce(property="basuTemplate", ref="basu.bpaf.queue.endpoint")
	private ProducerTemplate producer; 

	/**
	 * Send a list of BPAF events in extended format throughout a camel route.
	 * 
	 * @param events list of {@link es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event}
	 * objects to send in XML representation.
	 * @param exchange exchange data object to route.
	 * @throws XESEventWriterException if any illegal data access or inconsistent event data error occurred.
	 */
	public void sendEvents(Exchange exchange, List<Event> events) throws XESEventWriterException {
		for (Event event : events) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  
			try {									    	
				JAXBContext context = JAXBContext.newInstance(Event.class);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				logger.debug("Marshalling event " + event + "...");
				marshaller.marshal(event, writer);
				final byte[] xml = baos.toByteArray();								
				producer.send(new Processor() {
					public void process(Exchange outExchange) {
						outExchange.getIn().setBody(xml, String.class);
					}
				});
			} catch(JAXBException jaxbex) {
				logger.error("Cannot send event " + event + " due to a JAXB marshalling error.");
				logger.error(jaxbex.fillInStackTrace());
				throw new XESEventWriterException(jaxbex.getLocalizedMessage());
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch(IOException ioex) {
						logger.error(ioex.fillInStackTrace());
					}
				}
				if (baos != null) {
					try {
						baos.close();
					} catch(IOException ioex) {
						logger.error(ioex.fillInStackTrace());
					}
				}
			}
		}
	} 
}