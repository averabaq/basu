/* 
 * $Id: ActivityInstanceDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.dao;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.Stats;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HModel;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HSequenceGenerator;

import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    /** Model data access object */
    @Autowired private ModelDAO modelDAO;
    /** Statistical performance object */
    @Autowired private Stats stats; 
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ActivityInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all activity instances...");
		Query query = entityManager.createQuery("select i from " + HActivityInstance.class.getName() + " i order by id asc");
		List<HActivityInstance> list = query.getResultList();
		logger.debug("All activity instances found successfully.");
		/* convert to business object */
		List<ActivityInstance> instances = new ArrayList<ActivityInstance>();
		for (HActivityInstance hinstance : list) {
			ActivityInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
			HModel hprocessModel = entityManager.find(HModel.class, hinstance.getModel());
			Model model = BusinessObjectAssembler.getInstance().toBusinessObject(hprocessModel);
			instance.setModel((ActivityModel)model); 
			instances.add(instance);
		}
		return instances;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving activity instance with identifier " + id + "...");
		HActivityInstance hinstance = entityManager.find(HActivityInstance.class, id);	
		if (hinstance != null) {			
			ActivityInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
			Model model = modelDAO.findById(hinstance.getModel());
			instance.setModel((ActivityModel)model);	
			logger.debug("Activity instance " + instance + " retrieved successfully.");
			return instance;
		} else {	
			logger.debug("Activity instance with identifier " + id + " not found.");
		}				
		return null;
	}	  
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel#getSource()} retrieve from 
	 *  {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance#getModel()}) 
	 * as unique keys.
	 * 
	 * @param activityId activity instance identifier given at the original source.
	 * @param model activity instance model.
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityInstance findBySourceData(String activityId, Model model) throws IllegalArgumentException {
		logger.debug("Retrieving activity instance with source data as pairs of (" + activityId + ", " + model.getSource() + ")...");
		Model _model = modelDAO.findBySourceData(model.getModelSrcId(), model.getSource());
		if (_model == null)
			return null;
		Query query = entityManager.createQuery("select p from " + HActivityInstance.class.getName() + " p where p.instanceSrcId = :sourceId and p.model = :modelId");
		query.setParameter("sourceId", activityId);
		query.setParameter("modelId", _model.getId());
		HActivityInstance hinstance = null;
		try {
			long ini = System.nanoTime();
			hinstance = (HActivityInstance)query.getSingleResult();
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_SOURCE_DATA, hinstance, ini, end);
			logger.debug("Activity instance " + hinstance + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Activity instance with source data as pairs of (" + activityId + ", " + model.getSource() + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.error(nurex.fillInStackTrace());
			logger.fatal("This message should never appear. Inconsistence in the database has been found. There exists two or more different local activity instances for a unique pair of source and source activity instances.");			
			throw new IllegalArgumentException("Inconsistence in the database has been found. There exists two or more different local activity instances for a unique pair of source and source activity instances.");
		}
		ActivityInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
		return instance;
	}    	
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity 
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation} objects, 
	 * a determined {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel#getSource()}) property. 
	 * 
	 * @param correlation list of event correlation objects associated to the activity instance that is trying to be found.
	 * @param model activity model associated to the activity instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} entity object associated.
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
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging activity instance " + instance + "...");
		HActivityInstance hinstance = BusinessObjectAssembler.getInstance().toEntity(instance);
		entityManager.merge(hinstance);		
		logger.debug("Activity instance " + instance + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting activity instance " + instance + "...");
		HActivityInstance hinstance = entityManager.find(HActivityInstance.class, instance.getId());
		if (hinstance == null) {
			logger.warn("Activity instance [" + instance.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		/* TODO: remove events */
		entityManager.remove(hinstance);	
		logger.debug("Activity instance " + instance + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving activity instance " + instance + "...");	
		
		if (instance == null) {
			logger.warn("Cannot save activity instance. Activity instance is null.");
			throw new IllegalArgumentException("Cannot save activity instance. Activity instance is null.");
		}
		if (instance.getId() != null) {
			long ini = System.nanoTime();
			/* checks for the activity instance existence */
			HActivityInstance _hactivityInstance = entityManager.find(HActivityInstance.class, instance.getId());			
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_ID, _hactivityInstance, ini, end);			
			if (_hactivityInstance != null) {
				logger.debug("Activity instance " + instance.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		if (instance.getInstanceSrcId() != null) {
			ActivityInstance _instance = findBySourceData(instance.getInstanceSrcId(), instance.getModel());
			if (_instance != null) {
				logger.debug("Activity Instance " + _instance.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		logger.debug("Process instance " + instance.getId() + " does not exists. Saving process instance...");
		/* saves the model */	
		modelDAO.save(instance.getModel());

		/* saves the instance */
		HActivityInstance hactivityInstance = BusinessObjectAssembler.getInstance().toEntity(instance);

		long ini = System.nanoTime();
		HSequenceGenerator hsequenceGenerator = entityManager.find(HSequenceGenerator.class, HSequenceGenerator.Type.ACTIVITY_INSTANCE);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.READ_MAX_ID, hsequenceGenerator, ini, end);
		if (hsequenceGenerator == null) {
			logger.debug("There are not activity instances defined at the datastore.");	
			hsequenceGenerator = new HSequenceGenerator(HSequenceGenerator.Type.ACTIVITY_INSTANCE, 0L);
			ini = System.nanoTime();
			entityManager.persist(hsequenceGenerator);
			end = System.nanoTime();
			stats.writeStat(Stats.Operation.WRITE, hsequenceGenerator, ini, end);
		}

		hactivityInstance.setId(hsequenceGenerator.getNextSeq());
		instance.setId(hactivityInstance.getId());
		ini = System.nanoTime();
		entityManager.persist(hactivityInstance);
		end = System.nanoTime();
		stats.writeStat(Stats.Operation.WRITE, hactivityInstance, ini, end);
		/* increase sequence */
		hsequenceGenerator.increase();
		entityManager.merge(hsequenceGenerator);
		logger.debug("Activity instance " + instance + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} to load the events on.
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