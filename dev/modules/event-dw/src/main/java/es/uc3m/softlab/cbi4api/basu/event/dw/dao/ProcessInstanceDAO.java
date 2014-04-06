/* 
 * $Id: ProcessInstanceDAO.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
 * entity object. This interface defines all methods for accessing to the 
 * <strong>ProcessInstance</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessInstanceDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_INSTANCE_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<ProcessInstance> findAll() throws IllegalArgumentException;
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
	public ProcessInstance findById(long id) throws IllegalArgumentException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException;
}