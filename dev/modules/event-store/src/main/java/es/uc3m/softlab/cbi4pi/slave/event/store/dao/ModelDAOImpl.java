/* 
 * $Id: ModelDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.dao;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data access object class implementation for the <strong>model</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ModelDAO.COMPONENT_NAME)
public class ModelDAOImpl implements ModelDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model} entity 
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<Model> findAll() throws IllegalArgumentException {
		logger.debug("Finding all models...");
		List<Model> list = entityManager.createQuery("from event-store.Model order by id asc").getResultList();
		logger.debug("All models found successfully.");
		return list;
	}
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
	public Model findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving model with identifier " + id + "...");
		Model model = entityManager.find(Model.class, id);	
		if (model == null) {			
			logger.debug("Model with identifier " + id + " not found.");
		}		
		logger.debug("Model " + model + " retrieved successfully.");
		return model;
	}	    
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
    public Model findBySourceData(String modelId, Source source) throws IllegalArgumentException {
    	logger.debug("Retrieving model with source data as pairs of (" + modelId + ", " + source + ")...");
		Query query = entityManager.createQuery("from event-store.Model m where m.modelSrcId = :sourceId and m.source = :source");
		query.setParameter("sourceId", modelId);
		query.setParameter("source", source);
		Model model = null;
		try {
			model = (Model)query.getSingleResult();
			logger.debug("Model " + model + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Model with source data as pairs of (" + modelId + ", " + source + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.fatal(nurex.fillInStackTrace());
			throw nurex;
		}
		return model;
	}    
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Model model) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging model " + model + "...");	
		entityManager.merge(model);		
		logger.debug("Model " + model + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object associated from the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Model model) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting model " + model + "...");
		Model _model = findById(model.getId());
		if (_model == null) {
			logger.warn("Model [" + model.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		entityManager.remove(_model);	
		logger.debug("Model " + model + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object associated to the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Model}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Model model) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving model " + model + "...");	
		entityManager.persist(model);	
		logger.debug("Model " + model + " saved successfully.");
	}	
}