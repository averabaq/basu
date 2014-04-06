/* 
 * $Id: EventFactWriter.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;

/**
 * Component interface for writing event facts to the data warehouse. 
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventFactWriter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_FACT_WRITER;        

	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity objects into the data warehouse.
	 * 
	 * @param facts list of {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity objects to save.
	 * @throws EventFactWriterException if any illegal data access or inconsistent event fact writer data error occurred.
	 */
	public void storeEventFacts(List<EventFact> facts) throws EventFactWriterException;
}