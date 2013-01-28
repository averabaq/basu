/* 
 * $Id: ActivityModelDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;

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
 * Data access object class implementation for the <strong>activityModel</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ActivityModelDAO.COMPONENT_NAME)
public class ActivityModelDAOImpl implements ActivityModelDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ActivityModel> findAll() throws IllegalArgumentException {
		logger.debug("Finding all activity models...");
		List<ActivityModel> list = entityManager.createQuery("from event-store.ActivityModel order by id asc").getResultList();
		logger.debug("All activity models found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getId()} as primary key.
	 * 
	 * @param id activityModel's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityModel findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving activity model with identifier " + id + "...");
		ActivityModel activityModel = entityManager.find(ActivityModel.class, id);	
		if (activityModel == null) {			
			logger.debug("Activity model with identifier " + id + " not found.");
		}		
		logger.debug("Activity model " + activityModel + " retrieved successfully.");
		return activityModel;
	}	
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getSource()}) 
	 * as unique keys.
	 * 
	 * @param activityModelId activity model identifier given at the original source.
	 * @param source activityModel's source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ActivityModel findBySourceData(String activityModelId, Source source) throws IllegalArgumentException {
    	logger.debug("Retrieving activity model with source data as pairs of (" + activityModelId + ", " + source + ")...");
		Query query = entityManager.createQuery("from event-store.ActivityModel m where m.modelSrcId = :sourceId and m.source = :source");
		query.setParameter("sourceId", activityModelId);
		query.setParameter("source", source);
		ActivityModel activityModel = null;
		try {
			activityModel = (ActivityModel)query.getSingleResult();
			logger.debug("Activity model " + activityModel + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Activity model with source data as pairs of (" + activityModelId + ", " + source + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.fatal(nurex.fillInStackTrace());
			throw nurex;
		}
		return activityModel;
	}    		
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object state with the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging activity model " + activityModel + "...");	
		entityManager.merge(activityModel);		
		logger.debug("Activity model " + activityModel + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object associated from the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
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
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object associated to the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving activityModel " + activityModel + "...");	
		entityManager.persist(activityModel);	
		logger.debug("Activity model " + activityModel + " saved successfully.");
	}	
}