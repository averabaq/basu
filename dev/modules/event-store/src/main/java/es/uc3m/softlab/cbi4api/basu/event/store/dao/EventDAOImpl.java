/* 
 * $Id: EventDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.dao;

import es.uc3m.softlab.cbi4api.basu.event.store.Config;
import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.Stats;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    /** Process instance data access object */
    @Autowired private ProcessInstanceDAO processInstanceDAO;
    /** Activity instance data access object */
    @Autowired private ActivityInstanceDAO activityInstanceDAO;    
    /** Statistical performance object */
    @Autowired private Stats stats; 
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<Event> findAll() throws IllegalArgumentException {
		logger.debug("Finding all events...");
		List<HEvent> list = entityManager.createQuery("select e from " + HEvent.class.getName() + " e order by eventID asc").getResultList();
		logger.debug("All events found successfully.");
		/* convert to business object */
		List<Event> events = new ArrayList<Event>();
		for (HEvent hevent : list) {
			Event event = BusinessObjectAssembler.getInstance().toBusinessObject(hevent);			
			ProcessInstance processInstance = processInstanceDAO.findById(hevent.getProcessInstance());
			event.setProcessInstance(processInstance);
			/* TODO: load payload, data, correlation */
			logger.debug(hevent);
			events.add(event);
		}
		return events;
	}  
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} entity 
	 * objects of a determined process instance 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event#getProcessInstanceID()}).
	 * 
	 * @param processInstId process instance identifier. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event#getProcessInstanceID()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} entity 
	 * objects of a determined process instance identifier.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public List<Event> findAllByProcessInstId(String processInstId) throws IllegalArgumentException {
		logger.debug("Finding all events of process instance with id " + processInstId + "...");
		Query query = entityManager.createQuery("select e from " + HEvent.class.getName() + " e where e.processInstance = :processInstId order by eventID asc");
		query.setParameter("processInstId", processInstId);
		List<HEvent> list = query.getResultList();
		logger.debug("All events of process instance with id " + processInstId + " found successfully.");
		/* convert to business object */
		List<Event> events = new ArrayList<Event>();
		for (HEvent hevent : list) {
			Event event = BusinessObjectAssembler.getInstance().toBusinessObject(hevent);
			/* TODO: load process, instance, payload, data, correlation */
			events.add(event);
		}
		return events;
	}     
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event#getEventID()} as primary key.
	 * 
	 * @param eventId event's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Event findById(long eventId) throws IllegalArgumentException {
		logger.debug("Retrieving event with identifier " + eventId + "...");
		long ini = System.nanoTime();
		HEvent hevent = entityManager.find(HEvent.class, eventId);	
		long end = System.nanoTime();	
		if (hevent != null) {
			Event event = BusinessObjectAssembler.getInstance().toBusinessObject(hevent);
			stats.writeStat(Stats.Operation.READ_BY_ID, event, ini, end);	
			ProcessInstance processInstance = processInstanceDAO.findById(hevent.getProcessInstance());
			event.setProcessInstance(processInstance);
			/* TODO: load payload, data, correlation */
			//loadPayload(event); 
			//loadCorrelation(event); 
			//loadData(event); 
			logger.debug("Event " + event + " retrieved successfully.");
			return event;
		} else {
			logger.debug("Event with identifier " + eventId + " not found.");
		}		
		return null;
	}	
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging event " + event + "...");	
		HEvent hevent = BusinessObjectAssembler.getInstance().toEntity(event);
		long ini = System.nanoTime();
		entityManager.merge(hevent);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.UPDATE, event, ini, end);	
		logger.debug("Event " + event + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object associated from the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event " + event + "...");
		HEvent hevent = entityManager.find(HEvent.class, event.getEventID());
		if (hevent == null) {
			logger.warn("Event [" + event.getEventID() + "] could not be deleted. It does not exists.");
			return;
		}	
		long ini = System.nanoTime();
		entityManager.remove(hevent);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.DELETE, event, ini, end);
		logger.debug("Event " + event + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object associated to the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving event " + event + "...");	
		/* save process instance */
		processInstanceDAO.save(event.getProcessInstance());
		/* save activity instance */
		if (event.getActivityInstance() != null)
			activityInstanceDAO.save(event.getActivityInstance());	
		HEvent hevent = BusinessObjectAssembler.getInstance().toEntity(event);
		Query query = entityManager.createQuery("select max(e.eventID) from " + HEvent.class.getName() + " e ");
		Long maxEvent = null;
		long ini = System.nanoTime();
		maxEvent = (Long)query.getSingleResult();	
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.READ_MAX_ID, event, ini, end);
		
		if (maxEvent == null) {
			logger.warn("There are not events defined at the datastore.");	
			maxEvent = 0L;
		}

		hevent.setEventID(maxEvent + 1);
		ini = System.nanoTime();
		entityManager.persist(hevent);	
		end = System.nanoTime();
		stats.writeStat(Stats.Operation.WRITE, event, ini, end);
		logger.debug("Event " + event + " saved successfully.");
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event#getPayload()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} to load the event payload on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadPayload(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event payload from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event data elements from event " + event + " ...");				
		Query query = entityManager.createQuery("select elements(e.payload) from " + Event.class.getName() + " as e where e.eventID = :eventID");
		query.setParameter("eventID", event.getEventID());
		event.setPayload(new HashSet<EventPayload>(query.getResultList()));		
		logger.debug("Payload from event " + event + " loaded successfully.");				
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event#getCorrelations()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} to load the event correlation data on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadCorrelation(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event correlation data from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event correlation data from event " + event + " ...");				
		Query query = entityManager.createQuery("select elements(e.correlations) from " + Event.class.getName() + " as e where e.eventID = :eventID");
		query.setParameter("eventID", event.getEventID());
		event.setCorrelations(new HashSet<EventCorrelation>(query.getResultList()));
		logger.debug("Correlation data from event " + event + " loaded successfully.");				
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event#getData()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} 
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Event} to load the event data elements on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadData(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event data elements from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event data elements from event " + event + " ...");				
		Query query = entityManager.createQuery("select elements(e.data) from " + Event.class.getName() + " as e where e.eventID = :eventID");
		query.setParameter("eventID", event.getEventID());
		event.setData(new HashSet<EventData>(query.getResultList()));
		logger.debug("Data elements from event " + event + " loaded successfully.");				
	}			
}
