/* 
 * $Id: ActivityInstanceFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.facade;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.dao.ActivityInstanceDAO;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} 
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab 
 * @version 1.0.0 
 */
@Transactional
@Service(value=ActivityInstanceFacade.COMPONENT_NAME)
public class ActivityInstanceFacadeImpl implements ActivityInstanceFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(ActivityInstanceFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Activity instance data access object */
    @Autowired private ActivityInstanceDAO activityInstanceDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#getId()} as primary key.
	 * 
	 * @param id activity instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated.
	 */
    public ActivityInstance getActivityInstance(long id) {
		logger.debug("Retrieving model with identifier " + id + "...");
		ActivityInstance instance = activityInstanceDAO.findById(id);
		if (instance == null) {
			logger.warn("Cannot get model. Activity instance with identifier: " + id + " does not exist.");
		}
		logger.debug("Activity instance " + instance + " retrieved successfully.");
		return instance;
	}    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated to the  
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getSource()} retrieve from 
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#getModel()}) 
	 * as unique keys.
	 * 
	 * @param activityId activity instance identifier given at the original source.
	 * @param source activity instance source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity object associated.
	 * @throws ActivityInstanceException if any activity instance error occurred.
	 */
    public ActivityInstance getActivityInstance(String activityId, Source source) throws ActivityInstanceException {
		logger.debug("Retrieving activity instance with source data as pairs of (" + activityId + ", " + source + ")...");
		if (activityId == null) 
			throw new ActivityInstanceException(StaticResources.WARN_GET_ACTIVITY_INSTANCE_WITHOUT_INSTANCE_SRC_ID,"Cannot retrieve activity instance if the source activity instance id is not properly provided.");
		if (source == null) 
			throw new ActivityInstanceException(StaticResources.WARN_GET_ACTIVITY_INSTANCE_WITHOUT_SOURCE,"Cannot retrieve activity instance if the source is not properly provided.");
		ActivityInstance instance = activityInstanceDAO.findBySourceData(activityId, source);
		if (instance == null) {
			logger.warn("Cannot get activity instance. Activity instance with source data as pairs of (" + activityId + ", " + source + ") does not exist.");
		}
		logger.debug("Activity instance " + instance + " retrieved successfully.");
		return instance;
	}        
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation} objects, 
	 * a determined {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * and a determined ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} given by
	 * the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the activity instance that is trying to be found.
	 * @param model activity model associated to the activity instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity object associated.
	 * @throws ActivityInstanceException if any activity instance error occurred.
	 */
    public ActivityInstance getActivityInstance(Set<EventCorrelation> correlation, ActivityModel model) throws ActivityInstanceException {
		logger.debug("Retrieving activity instance associted to a determined correlation data from the model " + model + " and source associated...");
		if (correlation == null || correlation.isEmpty()) 
			throw new ActivityInstanceException(StaticResources.WARN_GET_ACTIVITY_INSTANCE_WITHOUT_CORRELATION_DATA,"Cannot retrieve activity instance if the correlation data is not properly provided.");		
		if (model == null) 
			throw new ActivityInstanceException(StaticResources.WARN_GET_ACTIVITY_INSTANCE_WITHOUT_PROCESS_MODEL_ID,"Cannot retrieve activity instance if the activity model is not properly provided.");
		if (model.getSource() == null) 
			throw new ActivityInstanceException(StaticResources.WARN_GET_ACTIVITY_INSTANCE_WITHOUT_SOURCE,"Cannot retrieve activity instance if the source is not properly provided within the model.");
		/* retrieve the activity instance associated with the correlation information provided */
		ActivityInstance instance = activityInstanceDAO.findBySourceData(correlation, model);
		if (instance == null) {
			logger.debug("Cannot get activity instance. Activity instance associted to a determined correlation data from the model " + model + " and associated source " + model.getSource() + " does not exist.");
		}
		logger.debug("Activity instance " + instance + " retrieved successfully.");
		return instance;
	}      
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} to load the events on.
	 * @throws ActivityInstanceException if any illegal data access or inconsistent activity instance data error occurred.
	 */	
    public void loadEvents(ActivityInstance instance) throws ActivityInstanceException {
		logger.debug("Loading events on process instance '" + instance + "'...");
		ActivityInstance _instance = activityInstanceDAO.findById(instance.getId());
		if (_instance == null) {
			logger.debug("Cannot load events. Activity instance with identifier: '" + instance + "' does not exist.");
			throw new ActivityInstanceException(StaticResources.WARN_LOAD_EVENT_ACTIVITY_INSTANCE_NOT_EXIST, "Cannot load events. Activity instance '" + instance + "' does not exist.");
		}
		activityInstanceDAO.loadEvents(instance);
		logger.debug("Events from activity instance " + instance + " loaded successfully.");
	}        
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} entity 
	 * objects defined at the data base.
	 */
	public List<ActivityInstance> getAll() {
		logger.debug("Retrieving all activity instances...");
		List<ActivityInstance> instances = activityInstanceDAO.findAll();
		logger.debug("Activity instances retrieved successfully.");
		return instances;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object to save.
	 * @throws ActivityInstanceException if any activity instance error occurred.
	 */
	public void saveActivityInstance(ActivityInstance instance) throws ActivityInstanceException {
		logger.debug("Saving the activity instance " + instance + "...");		
		if (instance == null) {
			logger.warn("Cannot save an empty activity instance. Trying to save a null activity instance.");
			throw new ActivityInstanceException(StaticResources.WARN_SAVE_NULL_ACTIVITY_INSTANCE, "Cannot save an empty address. Trying to save a null address.");
		}
		activityInstanceDAO.save(instance);		
		logger.info("Activity instance " + instance + " saved successfully.");
	}

	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object to update.
	 * @throws ActivityInstanceException if any illegal data access or inconsistent activity instance data error occurred.
	 */
	public void updateActivityInstance(ActivityInstance instance) throws ActivityInstanceException {
		logger.debug("Updating activity instance " + instance + "...");	
		ActivityInstance _instance = activityInstanceDAO.findById(instance.getId());
		if (_instance == null) {
			logger.warn("Cannot update activity instance. Activity instance with identifier: " + instance.getId() + " does not exist.");
			throw new ActivityInstanceException(StaticResources.WARN_UPDATE_ACTIVITY_INSTANCE_NOT_EXIST, "Cannot update activity instance. Activity instance with identifier: " + instance.getId() + " does not exist.");
		}		
		activityInstanceDAO.merge(instance);		
		logger.info("Activity instance " + instance + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance}
	 * entity object to delete.
	 * @throws ActivityInstanceException if any illegal data access or inconsistent activity instance data error occurred.
	 */
	public void deleteActivityInstance(ActivityInstance instance) throws ActivityInstanceException {
		logger.debug("Removing activity instance " + instance + "...");	
		ActivityInstance _instance = activityInstanceDAO.findById(instance.getId());
		if (_instance == null) {
			logger.warn("Cannot remove activity instance. Activity instance with identifier: " + instance.getId() + " does not exist.");
			throw new ActivityInstanceException(StaticResources.WARN_DELETE_ACTIVITY_INSTANCE_NOT_EXIST, "Cannot remove activity instance. Activity instance with identifier: " + instance.getId() + " does not exist.");
		}
		activityInstanceDAO.delete(_instance);		
		logger.info("ActivityInstance " + _instance + " removed successfully.");
	}		
}