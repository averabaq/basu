/* 
 * $Id: EventFactImplDAO.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;

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
 * Data access object class implementation for the <strong>EventFact</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab  
 * @version 1.0.0
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=EventFactDAO.COMPONENT_NAME)
public class EventFactDAOImpl implements EventFactDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */     
    @SuppressWarnings("unchecked")
    public List<EventFact> findAll() throws IllegalArgumentException {
		logger.debug("Finding all event facts...");
		List<EventFact> list = entityManager.createQuery("from event-dw.EventFact order by id asc").getResultList();
		logger.debug("All event facts found successfully.");
		return list;
	}
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process mapping 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getMapping()}).
	 * 
	 * @param mapping process mapping. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getProcessDefinitionID()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process mapping.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public List<EventFact> findAllByProcessMap(long mapping) throws IllegalArgumentException {
		logger.debug("Finding all event facts of determined process mapping with id " + mapping + "...");
		Query query = entityManager.createQuery("from event-dw.EventFact f where f.mapping = :mapping order by id asc");
		query.setParameter("mapping", mapping);
		List<EventFact> list = query.getResultList();
		logger.debug("All events facts of process mapping with id " + mapping + " found successfully.");
		return list;
	}    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process model 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getModel()}).
	 * 
	 * @param model process model. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getModel()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process model.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public List<EventFact> findAllByProcessModel(long model) throws IllegalArgumentException {
		logger.debug("Finding all event facts of determined process model with id " + model + "...");
		Query query = entityManager.createQuery("from event-dw.EventFact f where f.model = :model order by id asc");
		query.setParameter("model", model);
		List<EventFact> list = query.getResultList();
		logger.debug("All events facts of process model with id " + model + " found successfully.");
		return list;
	}  
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process instance 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getInstance()}).
	 * 
	 * @param instance process instance. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getInstance()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process instance.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public List<EventFact> findAllByProcessInstance(long instance) throws IllegalArgumentException {
		logger.debug("Finding all event facts of determined process instance with id " + instance + "...");
		Query query = entityManager.createQuery("from event-dw.EventFact f where f.instance = :instance order by id asc");
		query.setParameter("instance", instance);
		List<EventFact> list = query.getResultList();
		logger.debug("All events facts of process instance with id " + instance + " found successfully.");
		return list;
	}      
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getId()} as primary key.
	 * 
	 * @param id event fact's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public EventFact findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving event fact with identifier " + id + "...");
		EventFact fact = entityManager.find(EventFact.class, id);	
		if (fact != null) {		
			logger.debug("Event fact " + fact + " retrieved successfully.");
		} else {
			logger.debug("Event fact with identifier " + id + " not found.");
		}		
		return fact;
	}	
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object associated from the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(EventFact fact) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event fact " + fact + "...");
		EventFact _fact = findById(fact.getId());
		if (_fact == null) {
			logger.warn("Event fact [" + fact.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_fact);	
		logger.debug("Event fact " + fact + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object associated to the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(EventFact fact) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving event fact " + fact + "...");			
		entityManager.persist(fact); 	
		logger.debug("Event fact " + fact + " saved successfully.");
	}	
}