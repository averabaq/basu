/* 
 * $Id: ProcessMappingDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} 
 * entity object. This interface defines all methods for accessing to the 
 * <strong>ProcessMapping</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessMappingDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_MAPPING_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<ProcessMapping> findAll() throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping#getId()} as primary key.
	 * 
	 * @param id mapping's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessMapping findById(long id) throws IllegalArgumentException; 
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ProcessMapping mapping) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object associated from the data base.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessMapping mapping) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object associated to the data base.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessMapping mapping) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping#mappings} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param mapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} to load the model mappings on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadModelMappings(ProcessMapping mapping) throws IllegalArgumentException;
}