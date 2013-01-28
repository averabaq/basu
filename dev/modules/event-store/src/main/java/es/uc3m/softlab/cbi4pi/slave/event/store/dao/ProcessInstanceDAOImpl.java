/* 
 * $Id: ProcessInstanceDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data access object class implementation for the <strong>ProcessInstance</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ProcessInstanceDAO.COMPONENT_NAME)
public class ProcessInstanceDAOImpl implements ProcessInstanceDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ProcessInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all process instances...");
		List<ProcessInstance> list = entityManager.createQuery("from event-store.ProcessInstance order by id asc").getResultList();
		logger.debug("All process instances found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with identifier " + id + "...");
		ProcessInstance instance = entityManager.find(ProcessInstance.class, id);	
		if (instance == null) {			
			logger.debug("Process instance with identifier " + id + " not found.");
		}		
		logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}	    
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getSource()} retrieve from 
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#getModel()}) 
	 * as unique keys.
	 * 
	 * @param processId process instance identifier given at the original source.
	 * @param source process instance source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ProcessInstance findBySourceData(String processId, Source source) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with source data as pairs of (" + processId + ", " + source + ")...");
		Query query = entityManager.createQuery("from event-store.ProcessInstance p where p.instanceSrcId = :sourceId and p.model.source = :source");
		query.setParameter("sourceId", processId);
		query.setParameter("source", source);		
		ProcessInstance instance = null;
		try {
			instance = (ProcessInstance)query.getSingleResult();
			logger.debug("Process instance " + instance + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Process instance with source data as pairs of (" + processId + ", " + source + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.error(nurex.fillInStackTrace());
			logger.fatal("This message should never appear. Inconsistence in the database has been found. There exists two or more different local process instances for a unique pair of source and source process instances.");			
			throw new IllegalArgumentException("Inconsistence in the database has been found. There exists two or more different local process instances for a unique pair of source and source process instances.");
		}
		return instance;
	}   
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation} objects, 
	 * a determined {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * and a determined ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} given by
	 * the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getSource()}) property. 
	 * 
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @param model process model associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public ProcessInstance findBySourceData(Set<EventCorrelation> correlation, ProcessModel model) throws IllegalArgumentException {
    	logger.debug("Retrieving process instance associted to a determined correlation data from the model " + model + " associated to the source " + model.getSource() + "...");
		if (correlation == null || correlation.size() == 0) {
			throw new IllegalArgumentException("Cannot retrieve process instance because no correlation data has been provided.");
		}
    	StringBuilder correlationClause = new StringBuilder("ec.key in (");
		for (EventCorrelation eventCorrelation : correlation) {
			correlationClause.append("'" + eventCorrelation.getKey() + "',");			
		}
		correlationClause.deleteCharAt(correlationClause.lastIndexOf(","));
		correlationClause.append(") and ec.value in (");
		for (EventCorrelation eventCorrelation : correlation) {
			correlationClause.append("'" + eventCorrelation.getValue() + "',");			
		}
		correlationClause.deleteCharAt(correlationClause.lastIndexOf(","));
		correlationClause.append(")");
		Query query = entityManager.createQuery("select object(evt) from event-store.Event evt where evt.id in (select distinct e.id from Event e, EventCorrelation ec where e.id = ec.event and " + correlationClause.toString() + " and e.processInstance.model.name = :modelName and e.processInstance.model.source = :source group by e.id having count(e.id) = :correlationSize) order by evt.id desc");
		query.setParameter("modelName", model.getName());
		query.setParameter("source", model.getSource());
		query.setParameter("correlationSize", Long.valueOf(correlation.size()));		
		List<Event> events = query.getResultList();
		if (events == null || events.size() == 0) {
			logger.debug("No process instance associted to a determined correlation data from the model " + model + " associated to the source " + model.getSource() + " could be found.");
			return null;
		}
		/* gets the process instance from the most recent event to undertake the event correlation */
		ProcessInstance instance = events.get(0).getProcessInstance();
		logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}       
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging process instance " + instance + "...");	
		entityManager.merge(instance);		
		logger.debug("Process instance " + instance + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting process instance " + instance + "...");
		ProcessInstance _instance = findById(instance.getId());
		if (_instance == null) {
			logger.warn("Process instance [" + instance.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_instance);	
		logger.debug("Process instance " + instance + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving process instance " + instance + "...");	
		entityManager.persist(instance);	
		logger.debug("Process instance " + instance + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} to load the events on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadEvents(ProcessInstance instance) throws IllegalArgumentException {
		if (instance == null) {
			logger.warn("Cannot load events from a non existing (null) process instance.");
			return;
		}
		if (instance.getId() == null) {
			logger.warn("Process instance does not have any identifier. Cannot load events from a non existing (null) process instance.");
			return;
		}
		logger.debug("Loading events from the process instance " + instance + " ...");				
		Query query = entityManager.createQuery("select elements(i.events) from event-store.ProcessInstance as i where i.id = :id");
		query.setParameter("id", instance.getId());
		instance.setEvents(query.getResultList());		
		logger.debug("Events from process instance '" + instance + "' loaded successfully.");				
	}		
}