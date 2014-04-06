/* 
 * $Id: ActivityInstanceDAOImpl.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance;

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
 * Data access object class implementation for the <strong>ActivityInstance</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ActivityInstanceDAO.COMPONENT_NAME)
public class ActivityInstanceDAOImpl implements ActivityInstanceDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ActivityInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all activity instances...");
		Query query = entityManager.createQuery("from event-dw.ActivityInstance order by id asc");
		query.setMaxResults(10);
		List<ActivityInstance> list = query.getResultList();
		logger.debug("All activity instances found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving activity instance with identifier " + id + "...");
		ActivityInstance instance = entityManager.find(ActivityInstance.class, id);	
		if (instance == null) {			
			logger.debug("Activity instance with identifier " + id + " not found.");
		} else {		
			logger.debug("Activity instance " + instance + " retrieved successfully.");
		}
		return instance;
	}	  
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
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
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving activity instance " + instance + "...");			
		entityManager.persist(instance);	
		logger.debug("Activity instance " + instance + " saved successfully.");
	}		
}