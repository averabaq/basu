/* 
 * $Id: ProcessModelFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.facade;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.dao.ProcessModelDAO;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Source;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} 
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab 
 * @version 1.0.0 
 */
@Transactional
@Service(value=ProcessModelFacade.COMPONENT_NAME)
public class ProcessModelFacadeImpl implements ProcessModelFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(ProcessModelFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** ProcessModel data access object */
    @Autowired private ProcessModelDAO processModelDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getId()} as primary key.
	 * 
	 * @param id process model's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * object associated.
	 */
    public ProcessModel getProcessModel(long id) {
		logger.debug("Retrieving process model with identifier " + id + "...");
		ProcessModel processModel = processModelDAO.findById(id);
		if (processModel == null) {
			logger.debug("Cannot get process model. Process model with identifier: " + id + " does not exist.");
		}
		logger.debug("Process model " + processModel + " retrieved successfully.");
		return processModel;
	}
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#getSource()}) 
	 * as unique keys.
	 * 
	 * @param processModelId processModel identifier given at the original source.
	 * @param source processModel's source.
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity object associated.
	 * @throws ProcessModelException if any processModel error occurred.
	 */
    public ProcessModel getProcessModel(String processModelId, Source source) throws ProcessModelException {
		logger.debug("Retrieving process model with source data as pairs of (" + processModelId + ", " + source + ")...");
		if (processModelId == null) 
			throw new ProcessModelException(StaticResources.WARN_GET_MODEL_WITHOUT_MODEL_SRC_ID,"Cannot retrieve process model if the source process model id is not properly provided.");
		if (source == null) 
			throw new ProcessModelException(StaticResources.WARN_GET_MODEL_WITHOUT_SOURCE,"Cannot retrieve process model if the source is not properly provided.");

		ProcessModel processModel = processModelDAO.findBySourceData(processModelId, source);
		if (processModel == null) {
			logger.debug("Cannot get process model. Process model with source data as pairs of (" + processModelId + ", " + source + ") does not exist.");
		}
		logger.debug("Process model " + processModel + " retrieved successfully.");
		return processModel;
	}  
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel#instances} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} to load the instances on.
	 * @throws ProcessModelException if any illegal data access or inconsistent process model data error occurred.
	 */	
    public void loadInstances(ProcessModel processModel) throws ProcessModelException {
		logger.debug("Loading process instances on process model '" + processModel + "'...");
		ProcessModel _processModel = processModelDAO.findById(processModel.getId());
		if (_processModel == null) {
			logger.debug("Cannot load process instances. Process model with identifier: '" + processModel + "' does not exist.");
			throw new ProcessModelException(StaticResources.WARN_LOAD_PROCESS_INSTANCES_MODEL_NOT_EXIST, "Cannot load process instances. Process model '" + processModel + "' does not exist.");
		}
		processModelDAO.loadProcessInstances(processModel);
		logger.debug("Process instances of process model '" + processModel + "' loaded successfully.");
	}    
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} entity 
	 * objects defined at the data base.
	 */
	public List<ProcessModel> getAll() {
		logger.debug("Retrieving all process models...");
		List<ProcessModel> processModels = processModelDAO.findAll();
		logger.debug("Process models retrieved successfully.");
		return processModels;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object to save.
	 * @throws ProcessModelException if any illegal data access or inconsistent process model data error occurred.
	 */
	public void saveProcessModel(ProcessModel processModel) throws ProcessModelException {
		logger.debug("Saving the processModel " + processModel + "...");		
		if (processModel == null) {
			logger.warn("Cannot save process model. Trying to save a null process model.");
			throw new ProcessModelException(StaticResources.WARN_SAVE_NULL_MODEL, "Cannot save process model. Trying to save a null process model.");
		}
		if (processModel.getName() == null) {
			logger.warn("Cannot save process model. Trying to save a process model without a name.");
			throw new ProcessModelException(StaticResources.WARN_SAVE_MODEL_WITHOUT_NAME, "Cannot save process model. Trying to save a process model without a name.");
		}
		if (processModel.getModelSrcId() == null) {
			logger.warn("Cannot save process model. Trying to save a process model without a source processModel identifier.");
			throw new ProcessModelException(StaticResources.WARN_SAVE_MODEL_WITHOUT_MODEL_SRC_ID, "Cannot save process model. Trying to save a process model without a source process model identifier.");
		}
		if (processModel.getSource() == null) {
			logger.warn("Cannot save process model. Trying to save a process model without a source.");
			throw new ProcessModelException(StaticResources.WARN_SAVE_MODEL_WITHOUT_SOURCE, "Cannot save process model. Trying to save a process model without a source.");
		}
		ProcessModel _processModel = processModelDAO.findBySourceData(processModel.getModelSrcId(), processModel.getSource());
		if (_processModel != null) {
			logger.warn("Cannot save process model. Process model with source data as pairs of (" + processModel.getModelSrcId() + ", " + processModel.getSource() + ") already exists.");
			throw new ProcessModelException(StaticResources.WARN_SAVE_MODEL_WITH_NAME_ALREADY_EXISTS, "Cannot save process model. Process model with source data as pairs of (" + processModel.getModelSrcId() + ", " + processModel.getSource() + ") already exists.");
		}
		processModelDAO.save(processModel);		
		logger.debug("Process model " + processModel + " saved successfully.");
	}

	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object to update.
	 * @throws ProcessModelException if any illegal data access or inconsistent process model data error occurred.
	 */
	public void updateProcessModel(ProcessModel processModel) throws ProcessModelException {
		logger.debug("Updating process model " + processModel + "...");	
		ProcessModel _processModel = processModelDAO.findById(processModel.getId());
		if (_processModel == null) {
			logger.warn("Cannot update process model. Process model with identifier: " + processModel.getId() + " does not exist.");
			throw new ProcessModelException(StaticResources.WARN_UPDATE_MODEL_NOT_EXIST, "Cannot update process model. Process model with identifier: " + processModel.getId() + " does not exist.");
		}		
		processModelDAO.merge(processModel);		
		logger.info("Process model " + processModel + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel}
	 * entity object to delete.
	 * @throws ProcessModelException if any illegal data access or inconsistent process model data error occurred.
	 */
	public void deleteProcessModel(ProcessModel processModel) throws ProcessModelException {
		logger.debug("Removing process model " + processModel + "...");	
		ProcessModel _processModel = processModelDAO.findById(processModel.getId());
		if (_processModel == null) {
			logger.warn("Cannot remove process model. Process model with identifier: " + processModel.getId() + " does not exist.");
			throw new ProcessModelException(StaticResources.WARN_DELETE_MODEL_NOT_EXIST, "Cannot update process model. Process model with identifier: " + processModel.getId() + " does not exist.");
		}
		processModelDAO.delete(_processModel);		
		logger.info("Process model " + _processModel + " removed successfully.");
	}	   	
}