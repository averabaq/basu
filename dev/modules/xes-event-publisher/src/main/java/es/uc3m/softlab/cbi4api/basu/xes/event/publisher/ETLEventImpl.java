/* 
 * $Id: ETLEventImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.LogType;

/**
 * Specific ETL (Extract, Transform & Load) service implementation for handling
 * the XES events.
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition)</a> by
 * extracting the event data from XES sources, transforming its contents into
 * this format, and publish the results into a JMS queue.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional
@Service(value=ETLEvent.COMPONENT_NAME)
public class ETLEventImpl implements ETLEvent {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(ETLEventImpl.class);
    /** XES Event reader to extract the event data from the jms messages */
	@Autowired private XESEventReader extractor;
    /** Event converter to transform XES events into BPAF extended format */
    @Autowired private XESEventConverter transformer; 
    /** Event writer to publish the converted events into the bpaf publisher queue */
    @Autowired private XESEventWriter loader; 
	
	/**
	 * Process all events by executing the ETL engine.
	 * @param xml xml message to process.
	 * @throws EventPublisherException if any error occurred during 
	 * the ETL execution process.
	 */
	public void process(byte[] xml) throws EventPublisherException {
		try {			
			/* extract event */
			LogType xes = extractor.extractEvents(xml);
			/* transform event */
			List<Event> events = transformer.transform(xes);
			/* publishes the converted xes events into the bpaf listener queue */
			loader.sendEvents(events);	
		} catch(XESEventReaderException xerex) {
			logger.error(xerex.fillInStackTrace());
			throw new EventPublisherException(xerex);
		} catch(XESEventWriterException xewex) {
			logger.error(xewex.fillInStackTrace());
			throw new EventPublisherException(xewex);
		} catch(XESException xesex) {
			logger.error(xesex.fillInStackTrace());
			throw new EventPublisherException(xesex);
		}
	}
}