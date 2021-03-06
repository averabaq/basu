/* 
 * $Id: ProcessModelDAOImpl.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel;

import java.util.HashSet;
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
 * Data access object class implementation for the <strong>processModel</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ProcessModelDAO.COMPONENT_NAME)
public class ProcessModelDAOImpl implements ProcessModelDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ProcessModel> findAll() throws IllegalArgumentException {
		logger.debug("Finding all process models...");
		List<ProcessModel> list = entityManager.createQuery("from event-dw.ProcessModel order by id asc").getResultList();
		logger.debug("All process models found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel#getId()} as primary key.
	 * 
	 * @param id process model's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessModel findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving process model with identifier " + id + "...");
		ProcessModel processModel = entityManager.find(ProcessModel.class, id);	
		if (processModel == null) {			
			logger.debug("Process model with identifier " + id + " not found.");
		} else {		
			logger.debug("Process model " + processModel + " retrieved successfully.");
		}
		return processModel;
	}	  	
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel}
	 * entity object associated from the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessModel processModel) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting process model " + processModel + "...");
		ProcessModel _processModel = findById(processModel.getId());
		if (_processModel == null) {
			logger.warn("Process model [" + processModel.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_processModel);	
		logger.debug("Process model " + processModel + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel}
	 * entity object associated to the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessModel processModel) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving processModel " + processModel + "...");	
		entityManager.persist(processModel);	
		logger.debug("Process model " + processModel + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel#instances} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel} to load the instances on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadProcessInstances(ProcessModel processModel) throws IllegalArgumentException {
		if (processModel == null) {
			logger.warn("Cannot load process instances from a non existing (null) process model.");
			return;
		}
		if (processModel.getId() == null) {
			logger.warn("Process model does not have any identifier. Cannot load process instances from a non existing (null) process model.");
			return;
		}
		logger.debug("Loading process instances from process model " + processModel + " ...");				
		Query query = entityManager.createQuery("select elements(m.instances) from event-dw.ProcessModel as m where m.id = :id");
		query.setParameter("id", processModel.getId());
		processModel.setInstances(new HashSet<ProcessInstance>(query.getResultList()));		
		logger.debug("Process instances from process model '" + processModel + "' loaded successfully.");				
	}	
}