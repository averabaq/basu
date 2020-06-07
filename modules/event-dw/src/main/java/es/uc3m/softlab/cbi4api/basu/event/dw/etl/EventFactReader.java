/* 
 * $Id: EventFactReader.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;

/**
 * Component interface for retrieving the data warehouse information 
 * located at the event store.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventFactReader {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_FACT_READER;        

	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} defined
	 * at the event store in order to build up the hierarchies, dimensions and facts from
	 * such information. 
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} defined.
	 * @throws EventFactReaderException if any error occurred during event information retrieval.
	 */	
	public List<ProcessInstance> getAllProcessInstancesInFull() throws EventFactReaderException;
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} defined
	 * at the event store in order to build up the hierarchies, dimensions and facts from
	 * such information. 
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} defined.
	 * @throws EventFactReaderException if any error occurred during event information retrieval.
	 */	
	public List<ActivityInstance> getAllActivityInstancesInFull() throws EventFactReaderException;
}