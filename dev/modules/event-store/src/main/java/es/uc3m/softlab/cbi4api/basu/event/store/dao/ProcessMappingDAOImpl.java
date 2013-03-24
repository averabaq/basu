/* 
 * $Id: ProcessMappingDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.dao;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModelMapping;

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
 * Data access object class implementation for the <strong>ProcessMapping</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ProcessMappingDAO.COMPONENT_NAME)
public class ProcessMappingDAOImpl implements ProcessMappingDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<ProcessMapping> findAll() throws IllegalArgumentException {
		logger.debug("Finding all process mappings...");
		List<ProcessMapping> list = entityManager.createQuery("from event-store.ProcessMapping order by id asc").getResultList();
		logger.debug("All process mappings found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping#getId()} as primary key.
	 * 
	 * @param id mapping's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessMapping findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving process mapping with identifier " + id + "...");
		ProcessMapping mapping = entityManager.find(ProcessMapping.class, id);	
		if (mapping != null) {			
			logger.debug("Process mapping with identifier " + id + " not found.");
		}		
		logger.debug("Process mapping " + mapping + " retrieved successfully.");
		return mapping;
	}	           
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ProcessMapping mapping) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging process mapping " + mapping + "...");	
		entityManager.merge(mapping);		
		logger.debug("Process mapping " + mapping + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object associated from the data base.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessMapping mapping) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting process mapping " + mapping + "...");
		ProcessMapping _mapping = findById(mapping.getId());
		if (_mapping == null) {
			logger.warn("Process mapping [" + mapping.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_mapping);	
		logger.debug("Process mapping " + mapping + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object associated to the data base.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessMapping mapping) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving process mapping " + mapping + "...");	
		entityManager.persist(mapping);	
		logger.debug("Process mapping " + mapping + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping#mappings} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} to load the model mappings on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	@SuppressWarnings("unchecked")
	public void loadModelMappings(ProcessMapping mapping) throws IllegalArgumentException {
		if (mapping == null) {
			logger.warn("Cannot load model mappings from a non existing (null) process mapping.");
			return;
		}
		if (mapping.getId() == null) {
			logger.warn("Process mapping does not have any identifier. Cannot load model mappings from a non existing (null) process mapping.");
			return;
		}
		logger.debug("Loading model mappings from process mapping " + mapping + " ...");				
		Query query = entityManager.createQuery("select p from event-store.ProcessModelMapping as p where p.mapping = :mapping order by p.sequence asc");
		query.setParameter("mapping", mapping);
		mapping.setMappings(new HashSet<ProcessModelMapping>(query.getResultList()));		
		logger.debug("Model mappings from process mapping " + mapping + " loaded successfully.");				
	}		
}