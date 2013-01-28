/* 
 * $Id: ProcessMappingFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.facade;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping;

import java.util.List;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
 * entity object. This interface defines all methods for accessing to the <strong>ProcessMapping</strong> entity 
 * data throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessMappingFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_MAPPING_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping#getId()} as primary key.
	 * 
	 * @param id process mapping's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * object associated.
	 */
    public ProcessMapping getProcessMapping(long id);
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} entity 
	 * objects defined at the data base.
	 */
	public List<ProcessMapping> getAll();
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping#mappings} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping} to load the model mappings on.
	 * @throws ProcessMappingException if any illegal data access or inconsistent process mapping data error occurred.
	 */	
    public void loadModelMappings(ProcessMapping processMapping) throws ProcessMappingException;	
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object to save.
	 * @throws ProcessMappingException if any processMapping error occurred.
	 */
	public void saveProcessMapping(ProcessMapping processMapping) throws ProcessMappingException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object to update.
	 * @throws ProcessMappingException if any illegal data access or inconsistent processMapping data error occurred.
	 */
	public void updateProcessMapping(ProcessMapping processMapping) throws ProcessMappingException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object state with the data base.
	 * 
	 * @param processMapping {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping}
	 * entity object to delete.
	 * @throws ProcessMappingException if any illegal data access or inconsistent processMapping data error occurred.
	 */
	public void deleteProcessMapping(ProcessMapping processMapping) throws ProcessMappingException;
}