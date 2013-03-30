/* 
 * $Id: ProcessInstanceDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.dao;

import es.uc3m.softlab.cbi4api.basu.event.store.Config;
import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.Stats;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HModel;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HProcessInstance;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    /** Model data access object */
    @Autowired private ModelDAO modelDAO;
    /** Statistical performance object */
    @Autowired private Stats stats; 

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ProcessInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all process instances...");
		List<HProcessInstance> list = entityManager.createQuery("select i from " + HProcessInstance.class.getName() + " i order by id asc").getResultList();
		logger.debug("All process instances found successfully.");
		/* convert to business object */
		List<ProcessInstance> instances = new ArrayList<ProcessInstance>();
		for (HProcessInstance hinstance : list) {
			ProcessInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
			HModel hprocessModel = entityManager.find(HModel.class, hinstance.getModel());
			Model model = BusinessObjectAssembler.getInstance().toBusinessObject(hprocessModel);
			instance.setModel((ProcessModel)model); 
			instances.add(instance);
		}
		return instances;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with identifier " + id + "...");
		HProcessInstance hinstance = entityManager.find(HProcessInstance.class, id);	
		if (hinstance != null) {			
			ProcessInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
			Model model = modelDAO.findById(hinstance.getModel());
			instance.setModel((ProcessModel)model);	
			logger.debug("Process instance " + instance + " retrieved successfully.");
			return instance;
		} else {	
			logger.debug("Process instance with identifier " + id + " not found.");
		}				
		return null;
	}	    
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getSource()} retrieve from 
	 *  {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance#getModel()}) 
	 * as unique keys.
	 * 
	 * @param processId process instance identifier given at the original source.
	 * @param model process instance model.
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ProcessInstance findBySourceData(String processId, Model model) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with source data as pairs of (" + processId + ", " + model.getSource() + ")...");
		Model _model = modelDAO.findBySourceData(model.getModelSrcId(), model.getSource());
		if (_model == null)
			return null;
		Query query = entityManager.createQuery("select p from " + HProcessInstance.class.getName() + " p where p.instanceSrcId = :sourceId and p.model = :modelId");
		query.setParameter("sourceId", processId);
		query.setParameter("modelId", _model.getId());		
		ProcessInstance instance = null;
		try {
			long ini = System.nanoTime();
			instance = (ProcessInstance)query.getSingleResult();
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_SOURCE_DATA, instance, ini, end);		
			logger.debug("Process instance " + instance + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Process instance with source data as pairs of (" + processId + ", " + model.getSource() + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.error(nurex.fillInStackTrace());
			logger.fatal("This message should never appear. Inconsistence in the database has been found. There exists two or more different local process instances for a unique pair of source and source process instances.");			
			throw new IllegalArgumentException("Inconsistence in the database has been found. There exists two or more different local process instances for a unique pair of source and source process instances.");
		}
		return instance;
	}   
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity 
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation} objects, 
	 * a determined {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getSource()}) property. 
	 * 
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @param model process model associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} entity object associated.
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
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging process instance " + instance + "...");
		HProcessInstance hinstance = BusinessObjectAssembler.getInstance().toEntity(instance);
		entityManager.merge(hinstance);		
		logger.debug("Process instance " + instance + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting process instance " + instance + "...");
		HProcessInstance hinstance = entityManager.find(HProcessInstance.class, instance.getId());
		if (hinstance == null) {
			logger.warn("Process instance [" + instance.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		/* TODO: remove events */
		entityManager.remove(hinstance);	
		logger.debug("Process instance " + instance + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving process instance " + instance + "...");			
		
		if (instance == null) {
			logger.warn("Cannot save process instance. Process instance is null.");
			throw new IllegalArgumentException("Cannot save process instance. Process instance is null.");
		}
		if (instance.getId() != null) {
			long ini = System.nanoTime();
			/* checks for the process instance existence */
			HProcessInstance _hprocessInstance = entityManager.find(HProcessInstance.class, instance.getId());		
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_ID, instance, ini, end);
			if (_hprocessInstance != null) {
				logger.debug("Process instance " + instance.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		if (instance.getInstanceSrcId() != null) {
			ProcessInstance _instance = findBySourceData(instance.getInstanceSrcId(), instance.getModel());
			if (_instance != null) {
				logger.debug("Process Instance " + _instance.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		logger.debug("Process instance " + instance.getId() + " does not exists. Saving process instance...");
		/* saves the model */	
		modelDAO.save(instance.getModel());
		/* saves the instance */
		HProcessInstance hprocessInstance = BusinessObjectAssembler.getInstance().toEntity(instance);
		Query query = entityManager.createQuery("select max(i.id) from " + HProcessInstance.class.getName() + " i ");
		Long maxInstance = null;
		
		long ini = System.nanoTime();
		/* checks for the process instance existence */
		maxInstance = (Long)query.getSingleResult();
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.READ_MAX_ID, instance, ini, end);
		
		if (maxInstance == null) {
			logger.warn("There are not process instances defined at the datastore.");	
			maxInstance = 0L;
		}
		
		hprocessInstance.setId(maxInstance + 1);
		instance.setId(hprocessInstance.getId());
		ini = System.nanoTime();
		entityManager.persist(hprocessInstance);
		end = System.nanoTime();
		stats.writeStat(Stats.Operation.WRITE, instance, ini, end);
		logger.debug("Process instance " + instance + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} to load the events on.
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