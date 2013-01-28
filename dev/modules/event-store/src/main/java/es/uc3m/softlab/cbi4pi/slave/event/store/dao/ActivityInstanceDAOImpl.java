/* 
 * $Id: ActivityInstanceDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation;
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
 * Data access object class implementation for the <strong>ActivityInstance</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ActivityInstanceDAO.COMPONENT_NAME)
public class ActivityInstanceDAOImpl implements ActivityInstanceDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ActivityInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all activity instances...");
		Query query = entityManager.createQuery("from event-store.ActivityInstance order by id asc");
		List<ActivityInstance> list = query.getResultList();
		logger.debug("All activity instances found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving activity instance with identifier " + id + "...");
		ActivityInstance instance = entityManager.find(ActivityInstance.class, id);	
		if (instance == null) {			
			logger.debug("Activity instance with identifier " + id + " not found.");
		}		
		logger.debug("Activity instance " + instance + " retrieved successfully.");
		return instance;
	}	  
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getSource()} retrieve from 
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#getModel()}) 
	 * as unique keys.
	 * 
	 * @param activityId activity instance identifier given at the original source.
	 * @param source activity instance source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ActivityInstance findBySourceData(String activityId, Source source) throws IllegalArgumentException {
		logger.debug("Retrieving activity instance with source data as pairs of (" + activityId + ", " + source + ")...");
		Query query = entityManager.createQuery("from event-store.ActivityInstance a where a.instanceSrcId = :sourceId and a.model.source = :source");
		query.setParameter("sourceId", activityId);
		query.setParameter("source", source);
		ActivityInstance instance = null;
		try {
			instance = (ActivityInstance)query.getSingleResult();
			logger.debug("Activity instance " + instance + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Activity instance with source data as pairs of (" + activityId + ", " + source + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.error(nurex.fillInStackTrace());
			logger.fatal("This message should never appear. Inconsistence in the database has been found. There exists two or more different local activity instances for a unique pair of source and source activity instances.");			
			throw new IllegalArgumentException("Inconsistence in the database has been found. There exists two or more different local activity instances for a unique pair of source and source activity instances.");
		}
		return instance;
	}    	
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation} objects, 
	 * a determined {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * and a determined ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} given by
	 * the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getSource()}) property. 
	 * 
	 * @param correlation list of event correlation objects associated to the activity instance that is trying to be found.
	 * @param model activity model associated to the activity instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public ActivityInstance findBySourceData(Set<EventCorrelation> correlation, ActivityModel model) throws IllegalArgumentException {
    	logger.debug("Retrieving activity instance associted to a determined correlation data from the model " + model + " associated to the source " + model.getSource() + " ...");
		if (correlation == null || correlation.size() == 0) {
			throw new IllegalArgumentException("Cannot retrieve activity instance because no correlation data has been provided.");
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
		Query query = entityManager.createQuery("select object(evt) from event-store.Event evt where evt.id in (select distinct e.id from Event e, EventCorrelation ec where e.id = ec.event and " + correlationClause.toString() + " and e.activityInstance.model.name = :modelName and e.activityInstance.model.source = :source group by e.id having count(e.id) = :correlationSize) order by evt.id desc");
		query.setParameter("modelName", model.getName());
		query.setParameter("source", model.getSource());
		query.setParameter("correlationSize", Long.valueOf(correlation.size()));		
		List<Event> events = query.getResultList();
		if (events == null || events.size() == 0) {
			logger.debug("No activity instance associted to a determined correlation data from the model " + model + " associated to the source " + model.getSource() + " could be found.");
			return null;
		}
		/* gets the activity instance from the most recent event to undertake the event correlation */
		ActivityInstance instance = events.get(0).getActivityInstance();
		logger.debug("Activity instance " + instance + " retrieved successfully.");
		return instance;
	}           
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging activity instance " + instance + "...");	
		entityManager.merge(instance);		
		logger.debug("Activity instance " + instance + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting activity instance " + instance + "...");
		ActivityInstance _instance = findById(instance.getId());
		if (_instance == null) {
			logger.warn("Activity instance [" + instance.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_instance);	
		logger.debug("Activity instance " + instance + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving activity instance " + instance + "...");	
		entityManager.persist(instance);	
		logger.debug("Activity instance " + instance + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} to load the events on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadEvents(ActivityInstance instance) throws IllegalArgumentException {
		if (instance == null) {
			logger.warn("Cannot load events from a non existing (null) activity instance.");
			return;
		}
		if (instance.getId() == null) {
			logger.warn("Activity instance does not have any identifier. Cannot load events from a non existing (null) activity instance.");
			return;
		}
		logger.debug("Loading events from the activity instance " + instance + " ...");				
		Query query = entityManager.createQuery("select elements(a.events) from event-store.ActivityInstance as a where a.id = :id");
		query.setParameter("id", instance.getId());
		instance.setEvents(query.getResultList());		
		logger.debug("Events from activity instance '" + instance + "' loaded successfully.");				
	}			
}