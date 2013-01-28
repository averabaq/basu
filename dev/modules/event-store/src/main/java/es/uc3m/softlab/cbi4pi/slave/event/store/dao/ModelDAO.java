/* 
 * $Id: ModelDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} 
 * entity object. This interface defines all methods for accessing to the 
 * <strong>Model</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ModelDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_MODEL_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<Model> findAll() throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model#getId()} as primary key.
	 * 
	 * @param id model's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Model findById(long id) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model#getSource()}) 
	 * as unique keys.
	 * 
	 * @param modelId model identifier given at the original source.
	 * @param source model's source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public Model findBySourceData(String modelId, Source source) throws IllegalArgumentException;
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Model model) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object associated from the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Model model) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object associated to the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Model model) throws IllegalArgumentException, TransactionRequiredException;
}