/* 
 * $Id: EventReader.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event;

import javax.jms.Message;

/**
 * Component interface for retrieving the incoming events in F4BPA-BPAF format 
 * from the message queue. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventReader {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_READER;        

	/**
	 * Extract the events in f4bpa-bpaf extension format from messages retrieved from 
	 * the jms queue.
	 * 
	 * @param msg event message object containing events in bpaf-f4bpa format.
	 * @return all bpaf-f4bpa events extracted from the message.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	public Event extractEvent(Message msg) throws EventReaderException;
}