/* 
 * $Id: XESEventReader.java,v 1.0 2013-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import org.apache.camel.Exchange;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.LogType;

/**
 * Component interface for retrieving the incoming events in XES format 
 * from the message queue. 
 * 
 * @author averab
 * @version 1.0.0
 */
public interface XESEventReader {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_XES_EVENT_READER;        

	/**
	 * Extract the events in XES format from messages retrieved from 
	 * the jms queue.
	 * 
	 * @param exchange event message xml containing events in XES format.
	 * @return XES log events extracted from the message.
	 * @throws XESEventReaderException if any error occurred during event information retrieval.
	 */
	public LogType extractEvents(Exchange exchange) throws XESEventReaderException ;
}