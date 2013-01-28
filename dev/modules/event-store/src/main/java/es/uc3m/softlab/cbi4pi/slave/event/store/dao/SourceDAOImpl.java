/* 
 * $Id: SourceDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

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
 * Data access object class implementation for the <strong>source</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=SourceDAO.COMPONENT_NAME)
public class SourceDAOImpl implements SourceDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<Source> findAll() throws IllegalArgumentException {
		logger.debug("Finding all sources...");
		List<Source> list = entityManager.createQuery("from event-store.Source order by id asc").getResultList();
		logger.debug("All sources found successfully.");
		return list;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source#getId()} as primary key.
	 * 
	 * @param id source's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Source findById(String id) throws IllegalArgumentException {
		logger.debug("Retrieving source with identifier " + id + "...");
		Source source = entityManager.find(Source.class, id);	
		if (source == null) {			
			logger.debug("Source with identifier " + id + " not found.");
		}		
		logger.debug("Source " + source + " retrieved successfully.");
		return source;
	}	      
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Source source) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging source " + source + "...");	
		entityManager.merge(source);		
		logger.debug("Source " + source + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source}
	 * entity object associated from the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Source source) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting source " + source + "...");
		Source _source = findById(source.getId());
		if (_source == null) {
			logger.warn("Source [" + source.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_source);	
		logger.debug("Source " + source + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source}
	 * entity object associated to the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Source source) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving source " + source + "...");	
		entityManager.persist(source);	
		logger.debug("Source " + source + " saved successfully.");
	}	
}
