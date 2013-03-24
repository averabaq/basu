/* 
 * $Id: ProcessModelDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.dao;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Source;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} 
 * entity object. This interface defines all methods for accessing to the 
 * <strong>ProcessModel</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessModelDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_MODEL_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<ProcessModel> findAll() throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getId()} as primary key.
	 * 
	 * @param id process model's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ProcessModel findById(long id) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getSource()}) 
	 * as unique keys.
	 * 
	 * @param processModelId process model identifier given at the original source.
	 * @param source process model's source.
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ProcessModel findBySourceData(String processModelId, Source source) throws IllegalArgumentException;	
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ProcessModel processModel) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object associated from the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessModel processModel) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object associated to the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessModel processModel) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#instances} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} to load the instances on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadProcessInstances(ProcessModel processModel) throws IllegalArgumentException;	
}