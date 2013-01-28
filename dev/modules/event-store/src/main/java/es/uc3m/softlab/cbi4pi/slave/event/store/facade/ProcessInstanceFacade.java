/* 
 * $Id: ProcessInstanceFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.facade;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;

import java.util.List;
import java.util.Set;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}  entity 
 * object. This interface defines all methods for accessing to the <strong>ProcessInstance</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessInstanceFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_INSTANCE_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id process instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated.
	 */
    public ProcessInstance getProcessInstance(long id);
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated to the  
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getSource()} retrieve from 
	 *  {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#getModel()}) 
	 * as unique keys.
	 * 
	 * @param processId process instance identifier given at the original source.
	 * @param source process instance source.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity object associated.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
    public ProcessInstance getProcessInstance(String processId, Source source) throws ProcessInstanceException;
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.EventCorrelation} objects, 
	 * a determined {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel}
	 * and a determined ({@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} given by
	 * the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @param model process model associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity object associated.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
    public ProcessInstance getProcessInstance(Set<EventCorrelation> correlation, ProcessModel model) throws ProcessInstanceException;    
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} entity 
	 * objects defined at the data base.
	 */
	public List<ProcessInstance> getAll();
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object to save.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
	public void saveProcessInstance(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object to update.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void updateProcessInstance(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity object to delete.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void deleteProcessInstance(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} 
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} to load the events on.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */	
    public void loadEvents(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Correlates all {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance}
	 * entity objects defined by attending to the information stored and represented by
	 * the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModelMapping} objects.
	 * 
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process 
	 * instance data error occurred during the correlation process.
	 */
	public void correlateAllProcessInstances() throws ProcessInstanceException;
}