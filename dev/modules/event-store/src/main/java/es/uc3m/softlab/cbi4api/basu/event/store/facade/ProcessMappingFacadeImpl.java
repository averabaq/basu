/* 
 * $Id: ProcessMappingFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.facade;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.dao.ProcessMappingDAO;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} 
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab 
 * @version 1.0.0 
 */
@Transactional
@Service(value=ProcessMappingFacade.COMPONENT_NAME)
public class ProcessMappingFacadeImpl implements ProcessMappingFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(ProcessMappingFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Process mapping  data access object */
    @Autowired private ProcessMappingDAO processMappingDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping#getId()} as primary key.
	 * 
	 * @param id process mapping's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * object associated.
	 */
    public ProcessMapping getProcessMapping(long id) {
		logger.debug("Retrieving process mapping with identifier " + id + "...");
		ProcessMapping processMapping = processMappingDAO.findById(id);
		if (processMapping == null) {
			logger.debug("Cannot get process mapping. Process mapping with identifier: " + id + " does not exist.");
		} else {
			/* loads the mappings associated */
			processMappingDAO.loadModelMappings(processMapping);
		}
		logger.debug("Process mapping " + processMapping + " retrieved successfully.");
		return processMapping;
	}
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} entity 
	 * objects defined at the data base.
	 */
	public List<ProcessMapping> getAll() {
		logger.debug("Retrieving all process mappings...");
		List<ProcessMapping> processMappings = processMappingDAO.findAll();
		for (ProcessMapping processMapping : processMappings) {
			/* loads the mappings associated */
			processMappingDAO.loadModelMappings(processMapping);
		}
		logger.debug("All process mappings retrieved successfully.");
		return processMappings;
	}   
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping#mappings} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping} to load the model mappings on.
	 * @throws ProcessMappingException if any illegal data access or inconsistent process mapping data error occurred.
	 */	
    public void loadModelMappings(ProcessMapping processMapping) throws ProcessMappingException {
		logger.debug("Loading model mappings on process mapping '" + processMapping + "'...");
		ProcessMapping _processMapping = processMappingDAO.findById(processMapping.getId());
		if (_processMapping == null) {
			logger.debug("Cannot load model mappings. Process mappings with identifier: '" + processMapping + "' does not exist.");
			throw new ProcessMappingException(StaticResources.WARN_LOAD_MODEL_MAPPINGS_PROCESS_MAPPING_NOT_EXIST, "Cannot load model mappings. Process mappings with identifier: '" + processMapping + "' does not exist.");
		}
		processMappingDAO.loadModelMappings(processMapping);
		logger.debug("Model mappings of process mapping '" + processMapping + "' loaded successfully.");
	}  	
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object to save.
	 * @throws ProcessMappingException if any illegal data access or inconsistent process mapping data error occurred.
	 */
	public void saveProcessMapping(ProcessMapping processMapping) throws ProcessMappingException {
		logger.debug("Saving the process mapping " + processMapping + "...");		
		if (processMapping == null) {
			logger.warn("Cannot save process mapping. Trying to save a null process mapping.");
			throw new ProcessMappingException(StaticResources.WARN_SAVE_NULL_PROCESS_MAPPING, "Cannot save process mapping. Trying to save a null process mapping.");
		}
		if (processMapping.getName() == null) {
			logger.warn("Cannot save process mapping. Trying to save a processMapping without a name.");
			throw new ProcessMappingException(StaticResources.WARN_SAVE_PROCESS_MAPPING_WITHOUT_NAME, "Cannot save process mapping. Trying to save a process mapping without a name.");
		}
		if (processMapping.getDescription() == null) {
			logger.warn("Cannot save process mapping. Trying to save a process mapping without a description.");
			throw new ProcessMappingException(StaticResources.WARN_SAVE_PROCESS_MAPPING_WITHOUT_DESCRIPTION, "Cannot save process mapping. Trying to save a process mapping without a description.");
		}
		processMappingDAO.save(processMapping);		
		logger.debug("Process mapping " + processMapping + " saved successfully.");
	}

	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object to update.
	 * @throws ProcessMappingException if any illegal data access or inconsistent process mapping data error occurred.
	 */
	public void updateProcessMapping(ProcessMapping processMapping) throws ProcessMappingException {
		logger.debug("Updating process mapping " + processMapping + "...");	
		ProcessMapping _processMapping = processMappingDAO.findById(processMapping.getId());
		if (_processMapping == null) {
			logger.warn("Cannot update process mapping. Process mapping with identifier: " + processMapping.getId() + " does not exist.");
			throw new ProcessMappingException(StaticResources.WARN_UPDATE_PROCESS_MAPPING_NOT_EXIST, "Cannot update process mapping. Process mapping with identifier: " + processMapping.getId() + " does not exist.");
		}		
		processMappingDAO.merge(processMapping);		
		logger.info("Process mapping " + processMapping + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessMapping}
	 * entity object to delete.
	 * @throws ProcessMappingException if any illegal data access or inconsistent process mapping data error occurred.
	 */
	public void deleteProcessMapping(ProcessMapping processMapping) throws ProcessMappingException {
		logger.debug("Removing process mapping " + processMapping + "...");	
		ProcessMapping _processMapping = processMappingDAO.findById(processMapping.getId());
		if (_processMapping == null) {
			logger.warn("Cannot remove process mapping. Process mapping with identifier: " + processMapping.getId() + " does not exist.");
			throw new ProcessMappingException(StaticResources.WARN_DELETE_PROCESS_MAPPING_NOT_EXIST, "Cannot update process mapping. Process mapping with identifier: " + processMapping.getId() + " does not exist.");
		}
		processMappingDAO.delete(_processMapping);		
		logger.info("Process mapping " + _processMapping + " removed successfully.");
	}	   	
}