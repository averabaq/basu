/* 
 * $Id: EventFactDAO.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dao;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
 * entity object. This interface defines all methods for accessing to the 
 * <strong>EventFact</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventFactDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_FACT_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<EventFact> findAll() throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process mapping 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getMapping()}).
	 * 
	 * @param mapping process mapping. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getMapping()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process mapping.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<EventFact> findAllByProcessMap(long mapping) throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process model 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getModel()}).
	 * 
	 * @param model process model. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getModel()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process model.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<EventFact> findAllByProcessModel(long model) throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process instance 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getInstance()}).
	 * 
	 * @param instance process instance. 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getInstance()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process instance.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<EventFact> findAllByProcessInstance(long instance) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getId()} as primary key.
	 * 
	 * @param id event fact's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public EventFact findById(long id) throws IllegalArgumentException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object associated from the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(EventFact fact) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object associated to the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws javax.persistence.TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(EventFact fact) throws IllegalArgumentException, TransactionRequiredException;
}