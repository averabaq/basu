/* 
 * $Id: ActivityModelDAOImpl.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data access object class implementation for the <strong>activityModel</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ActivityModelDAO.COMPONENT_NAME)
public class ActivityModelDAOImpl implements ActivityModelDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ActivityModel> findAll() throws IllegalArgumentException {
		logger.debug("Finding all activity models...");
		List<ActivityModel> list = entityManager.createQuery("from event-dw.ActivityModel order by id asc").getResultList();
		logger.debug("All activity models found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel#getId()} as primary key.
	 * 
	 * @param id activityModel's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityModel findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving activity model with identifier " + id + "...");
		ActivityModel activityModel = entityManager.find(ActivityModel.class, id);	
		if (activityModel == null) {			
			logger.debug("Activity model with identifier " + id + " not found.");
		} else {		
			logger.debug("Activity model " + activityModel + " retrieved successfully.");
		}
		return activityModel;
	}	  		
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel}
	 * entity object associated from the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting activity model " + activityModel + "...");
		ActivityModel _activityModel = findById(activityModel.getId());
		if (_activityModel == null) {
			logger.warn("Activity model [" + activityModel.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_activityModel);	
		logger.debug("Activity model " + activityModel + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel}
	 * entity object associated to the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving activityModel " + activityModel + "...");	
		entityManager.persist(activityModel);	
		logger.debug("Activity model " + activityModel + " saved successfully.");
	}	
}