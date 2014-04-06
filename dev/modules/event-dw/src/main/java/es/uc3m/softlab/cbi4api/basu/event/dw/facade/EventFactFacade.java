/* 
 * $Id: EventFactFacade.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.facade;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.dto.EventFactResultDTO;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;

import java.util.List;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
 * entity object. This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventFactFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_FACT_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getId()} as primary key.
	 * 
	 * @param id event's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated.
	 * @throws EventFactException if any event error occurred.
	 */
    public EventFact getEventFact(long id) throws EventFactException;
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getId()} as primary key.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects defined at the data base.
	 */
	public List<EventFact> getAll();
    /**
     * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
     * objects of a determined process or activity model.
     *
     * @param model process or activity model identifier.
     * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
     * objects of a determined process model identifier.
     */
    public List<EventFact> getAllFromModel(long model);
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process or activity instance.
	 * 
	 * @param instance process or activity instance identifier.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process instance identifier.
	 */
    public List<EventFact> getAllFromInstance(long instance);
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object state with the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to save.
	 * @throws EventFactException if any illegal data access or inconsistent event data error occurred.
	 */
	public void storeEventFact(EventFact fact) throws EventFactException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object state with the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to delete.
	 * @throws EventFactException if any illegal data access or inconsistent event data error occurred.
	 */
	public void deleteEventFact(EventFact fact) throws EventFactException;
	/**
	 * Executes a dynamic query.
	 * 
	 * @param query dynamic query.
	 * @return all tuples retrieved from the execution of the dynamic query.
	 * @throws EventFactException if any illegal data access or inconsistent query error occurred.
	 */
    public List<EventFactResultDTO> executeQuery(String query) throws EventFactException;
}