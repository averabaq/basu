/* 
 * $Id: ProcessModelFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.facade;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}  entity 
 * object. This interface defines all methods for accessing to the <strong>model</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessModelFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_MODEL_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getId()} as primary key.
	 * 
	 * @param id process model's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} entity 
	 * object associated.
	 */
    public ProcessModel getProcessModel(long id);
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} entity 
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getSource()}) 
	 * as unique keys.
	 * 
	 * @param processModelId processModel identifier given at the original source.
	 * @param source processModel's source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} entity object associated.
	 * @throws ProcessModelException if any processModel error occurred.
	 */
    public ProcessModel getProcessModel(String processModelId, Source source) throws ProcessModelException;	/**
	 * Gets all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} entity 
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} entity 
	 * objects defined at the data base.
	 */
	public List<ProcessModel> getAll();
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * entity object to save.
	 * @throws ProcessModelException if any processModel error occurred.
	 */
	public void saveProcessModel(ProcessModel processModel) throws ProcessModelException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * entity object to update.
	 * @throws ProcessModelException if any illegal data access or inconsistent processModel data error occurred.
	 */
	public void updateProcessModel(ProcessModel processModel) throws ProcessModelException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * entity object state with the data base.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * entity object to delete.
	 * @throws ProcessModelException if any illegal data access or inconsistent processModel data error occurred.
	 */
	public void deleteProcessModel(ProcessModel processModel) throws ProcessModelException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#instances} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param processModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} to load the instances on.
	 * @throws ProcessModelException if any illegal data access or inconsistent process model data error occurred.
	 */	
    public void loadInstances(ProcessModel processModel) throws ProcessModelException;	
}