/* 
 * $Id: XESEventReaderImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.LogType;

/**
 * Component implementation for retrieving the incoming events in F4BPA-BPAF format 
 * from the message queue. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=XESEventReader.COMPONENT_NAME)
public class XESEventReaderImpl implements XESEventReader {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(XESEventReaderImpl.class);
	
	/**
	 * Extract the events in XES format from messages retrieved from 
	 * the jms queue.
	 * 
	 * @param xml event message xml containing events in XES format.
	 * @return XES log events extracted from the message.
	 * @throws XESEventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public LogType extractEvents(byte[] xml) throws XESEventReaderException {
		LogType logType = null;
		try {		
			//logger.info(new String(xml));
			ByteArrayInputStream bais = new ByteArrayInputStream(xml);
			/* create a JAXBContext capable of handling classes */ 
			JAXBContext jc = JAXBContext.newInstance("es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes");			
			/* create an Unmarshaller */
			Unmarshaller u = jc.createUnmarshaller();        			
			/* Performs an xml validation against the OpenXES schema */ 
			/*SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL xsd = getClass().getResource(getClass().getResource(StaticResources.BASU_XES_XML_SCHEMA_CLASSPATH_FILE));		
			Schema schema = factory.newSchema(xsd);
			u.setSchema(schema);*/			
			/* unmarshal an instance document into a tree of Java content objects. */			
			JAXBElement<LogType> log = (JAXBElement<LogType>) u.unmarshal(bais);			
			logger.debug("Unmarshalled XES file sucessfully: " + log);
			logType = log.getValue();			
			logger.debug("XES event message version " + logType.getOpenxesVersion() + ".");
		} catch (JAXBException jaxb) {
			logger.error(jaxb.fillInStackTrace());
			throw new XESEventReaderException(jaxb);
		} 
		return logType;
	}
}