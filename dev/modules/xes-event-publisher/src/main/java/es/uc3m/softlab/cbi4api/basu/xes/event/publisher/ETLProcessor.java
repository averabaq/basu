/* 
 * $Id: ETLEventImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service(value=StaticResources.SERVICE_NAME_XES_ETL_PROCESSOR)
public class ETLProcessor implements Processor {
    /** XES Event reader to extract the event data from the jms messages */
	@Autowired private XESEventReader extractor;
    /** Event converter to transform XES events into BPAF extended format */
    @Autowired private XESEventConverter transformer; 
    /** Event writer to publish the converted events into the bpaf publisher queue */
    @Autowired private XESEventWriter loader; 
	
	/**
	 * Passes exchange data object to this processor.
	 * @param exchange data object to process.
	 */
    @Override
	public void process(Exchange exchange) throws Exception {		
		/* extract event */
		LogType xes = extractor.extractEvents(exchange);
		/* transform event */
		List<Event> events = transformer.transform(xes);
		/* publishes the converted xes events into the exchange */
		loader.sendEvents(exchange, events);	
	}
}