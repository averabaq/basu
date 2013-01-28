/* 
 * $Id: EventDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
 * entity object. This interface defines all methods for accessing to the 
 * <strong>Event</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<Event> findAll() throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects of a determined process definition 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getProcessDefinitionID()}).
	 * 
	 * @param processDefId process definition identifier. 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getProcessDefinitionID()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects of a determined process definition identifier.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<Event> findAllByProcessDefId(String processDefId) throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects of a determined process instance 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getProcessInstanceID()}).
	 * 
	 * @param processInstId process instance identifier. 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getProcessInstanceID()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects of a determined process instance identifier.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<Event> findAllByProcessInstId(String processInstId) throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects of a determined process name 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getProcessName()}).
	 * 
	 * @param processName process name. 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getProcessName()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects of a determined process name.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<Event> findAllByProcessName(String processName) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getEventID()} as primary key.
	 * 
	 * @param eventId event's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Event findById(long eventId) throws IllegalArgumentException;
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Event event) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object associated from the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Event event) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object associated to the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Event event) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Load the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getPayload()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} to load the event payload on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadPayload(Event event) throws IllegalArgumentException;	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getCorrelations()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} to load the event correlation data on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadCorrelation(Event event) throws IllegalArgumentException;	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getData()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} to load the event data elements on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadData(Event event) throws IllegalArgumentException;
}
