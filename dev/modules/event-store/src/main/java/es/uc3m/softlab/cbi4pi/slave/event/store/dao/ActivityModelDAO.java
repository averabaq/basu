/* 
 * $Id: ActivityModelDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} 
 * entity object. This interface defines all methods for accessing to the 
 * <strong>ActivityModel</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ActivityModelDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_ACTIVITY_MODEL_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<ActivityModel> findAll() throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getId()} as primary key.
	 * 
	 * @param id activityModel's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityModel findById(long id) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel#getSource()}) 
	 * as unique keys.
	 * 
	 * @param activityModelId activity model identifier given at the original source.
	 * @param source activityModel's source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ActivityModel findBySourceData(String activityModelId, Source source) throws IllegalArgumentException;	
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object state with the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object associated from the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object associated to the data base.
	 * 
	 * @param activityModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityModel activityModel) throws IllegalArgumentException, TransactionRequiredException;
}