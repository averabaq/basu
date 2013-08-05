/* 
 * $Id: EventWriter.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;


/**
 * Component interface for publishing converted events to the BPAF extension jms queue.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface XESEventWriter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_XES_EVENT_WRITER;        

	/**
	 * Publish a list of BPAF events in extended format throughout the publisher
	 * jms queue.
	 * 
	 * @param events list of {@link es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event}
	 * objects to send in XML representation.
	 * @throws XESEventWriterException if any illegal data access or inconsistent event data error occurred.
	 */
	public void sendEvents(List<Event> events) throws XESEventWriterException;
}