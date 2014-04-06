/* 
 * $Id: EventSender.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.mxml.event.publisher;

import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.basu.event.Event;

import java.util.List;

import javax.jms.JMSException;

/**
 * Component interface for sending the bpel events to a messaging queue. 
 * This interface defines all methods for sending the <strong>event</strong> 
 * entity data managed through the ApacheODE API (1.3.5) and based upon 
 * the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventSender {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_SENDER;        

	/**
	 * Sends a list of events to the messaging queue defined.
	 * @param events list of events to publish. 
	 * @throws JMSException if any error occurred during messaging publication.
	 */
	public void publishEvents(List<Event> events) throws JMSException;
}