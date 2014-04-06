/* 
 * $Id: ProcessInstanceDAOImpl.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance;

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
 * Data access object class implementation for the <strong>ProcessInstance</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ProcessInstanceDAO.COMPONENT_NAME)
public class ProcessInstanceDAOImpl implements ProcessInstanceDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ProcessInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all process instances...");
		List<ProcessInstance> list = entityManager.createQuery("from event-dw.ProcessInstance order by id asc").getResultList();
		logger.debug("All process instances found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with identifier " + id + "...");
		ProcessInstance instance = entityManager.find(ProcessInstance.class, id);	
		if (instance == null) {			
			logger.debug("Process instance with identifier " + id + " not found.");
		} else {		
			logger.debug("Process instance " + instance + " retrieved successfully.");
		}
		return instance;
	}	          
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
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
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving process instance " + instance + "...");			
		entityManager.persist(instance);	
		logger.debug("Process instance " + instance + " saved successfully.");
	}			
}