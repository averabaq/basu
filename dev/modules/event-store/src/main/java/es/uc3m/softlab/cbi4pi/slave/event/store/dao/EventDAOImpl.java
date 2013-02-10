/* 
 * $Id: EventDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventData;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventPayload;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data access object class implementation for the <strong>event</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab  
 * @version 1.0.0
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=EventDAO.COMPONENT_NAME)
public class EventDAOImpl implements EventDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<Event> findAll() throws IllegalArgumentException {
		logger.debug("Finding all events...");
		List<Event> list = entityManager.createQuery("select e from event-store.Event e order by eventID asc").getResultList();
		logger.debug("All events found successfully.");
		return list;
	}
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
    @SuppressWarnings("unchecked")
    public List<Event> findAllByProcessDefId(String processDefId) throws IllegalArgumentException {
		logger.debug("Finding all events of process definition with id " + processDefId + "...");
		Query query = entityManager.createQuery("from event-store.Event e where e.processDefinitionID = :processDefId order by eventID asc");
		query.setParameter("processDefId", processDefId);
		List<Event> list = query.getResultList();
		logger.debug("All events of process definition with id " + processDefId + " found successfully.");
		return list;
	}    
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
    @SuppressWarnings("unchecked")
    public List<Event> findAllByProcessInstId(String processInstId) throws IllegalArgumentException {
		logger.debug("Finding all events of process instance with id " + processInstId + "...");
		Query query = entityManager.createQuery("select e from event-store.Event e where e.processInstanceID = :processInstId order by eventID asc");
		query.setParameter("processInstId", processInstId);
		List<Event> list = query.getResultList();
		logger.debug("All events of process instance with id " + processInstId + " found successfully.");
		return list;
	}    
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
    @SuppressWarnings("unchecked")
    public List<Event> findAllByProcessName(String processName) throws IllegalArgumentException {
		logger.debug("Finding all events of process with name " + processName + "...");
		Query query = entityManager.createQuery("select e from event-store.Event e where e.processName = :processName order by eventID asc");
		query.setParameter("processName", processName);
		List<Event> list = query.getResultList();
		logger.debug("All events of process with name " + processName + " found successfully.");
		return list;
	}    
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
	public Event findById(long eventId) throws IllegalArgumentException {
		logger.debug("Retrieving event with identifier " + eventId + "...");
		Event event = entityManager.find(Event.class, eventId);	
		if (event != null) {		
			loadPayload(event); 
			loadCorrelation(event); 
			loadData(event); 
			logger.debug("Event " + event + " retrieved successfully.");
		} else {
			logger.debug("Event with identifier " + eventId + " not found.");
		}		
		return event;
	}	
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging event " + event + "...");	
		entityManager.merge(event);		
		logger.debug("Event " + event + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object associated from the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event " + event + "...");
		Event _event = findById(event.getEventID());
		if (_event == null) {
			logger.warn("Event [" + event.getEventID() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_event);	
		logger.debug("Event " + event + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object associated to the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving event " + event + "...");	
		entityManager.persist(event);	
		logger.debug("Event " + event + " saved successfully.");
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getPayload()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} to load the event payload on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadPayload(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event payload from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event data elements from event " + event + " ...");				
		Query query = entityManager.createQuery("select elements(e.payload) from event-store.Event as e where e.eventID = :eventID");
		query.setParameter("eventID", event.getEventID());
		event.setPayload(new HashSet<EventPayload>(query.getResultList()));		
		logger.debug("Payload from event " + event + " loaded successfully.");				
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getCorrelations()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} to load the event correlation data on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadCorrelation(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event correlation data from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event correlation data from event " + event + " ...");				
		Query query = entityManager.createQuery("select elements(e.correlations) from event-store.Event as e where e.eventID = :eventID");
		query.setParameter("eventID", event.getEventID());
		event.setCorrelations(new HashSet<EventCorrelation>(query.getResultList()));
		logger.debug("Correlation data from event " + event + " loaded successfully.");				
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getData()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} to load the event data elements on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadData(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event data elements from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event data elements from event " + event + " ...");				
		Query query = entityManager.createQuery("select elements(e.data) from event-store.Event as e where e.eventID = :eventID");
		query.setParameter("eventID", event.getEventID());
		event.setData(new HashSet<EventData>(query.getResultList()));
		logger.debug("Data elements from event " + event + " loaded successfully.");				
	}			
}
