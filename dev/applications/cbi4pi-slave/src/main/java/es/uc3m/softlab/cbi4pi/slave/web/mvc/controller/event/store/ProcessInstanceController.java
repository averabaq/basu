/* 
 * $Id: ProcessInstanceController.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.web.mvc.controller.event.store;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.ProcessInstanceFacade;
import es.uc3m.softlab.cbi4pi.slave.web.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.web.mvc.controller.ControllerManager;
import es.uc3m.softlab.cbi4pi.slave.web.mvc.view.event.store.LazyProcessInstancesDataModel;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.spring.scope.ViewScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Class user bean controller under the <i>Mojarra Project</i> for JSF 2.0 implementation.
 * 
 * This class uses the <b>JSF 2.0</b> package implementation developed by the Mojarra Project. 
 * <p> It implements the Java Specification Request
 * <a href="http://www.jcp.org/en/jsr/detail?id=314">JSR 314 Java Server Faces 2.0.</a>
 * </p><p>Mojarra is Sun's high performance, battle-tested implementation of JSF, 
 * and is used in IBM WebSphereTM, Oracle WebLogicTM, Oracle 10g Application Server, 
 * SpringSource dm ServerTM, and other popular enterprise platforms such as JBoss, 
 * so important to be mentioned.<p>
 * 
 * For further information about the usage of this package visit the Project web site 
 * at the <a href="https://javaserverfaces.dev.java.net">Mojarra Project</a> from sun
 * open source projects web site.
 * 
 * @author averab
 * @version 1.0
 */
@Scope(ViewScope.SCOPE_NAME)
@Controller(value=StaticResources.MANAGED_BEAN_PROCESS_INSTANCE_CONTROLLER)
public class ProcessInstanceController implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 2810265607896878527L;
	/** Logger for tracing. */
	private static final Logger logger = Logger.getLogger(ProcessInstanceController.class);
	/** Controller manager for common stuff management */
	private ControllerManager manager = ControllerManager.getInstance();
	/** Lazy data model for partial loading of events */
	private LazyDataModel<ProcessInstance> lazyModel;
	/** Process instance selected */
	private ProcessInstance selectedInstance;
	/** Process instances */
	private List<ProcessInstance> instances;
	/** Process instance facade session bean */
	@Autowired private ProcessInstanceFacade processInstanceFacade;

	/**
	 * Creates a new object with a default values for the properties passed by
	 * arguments.
	 */
	public ProcessInstanceController() {
		lazyModel = new LazyProcessInstancesDataModel(); 
	}
	/**
	 * Initializes and setup the controller.
	 */
	@PostConstruct
	public void init() {
		selectedInstance = new ProcessInstance();
		refreshInstanceList();		
	}
	/**
	 * Gets the {@link #lazyModel} property.
	 * @return the {@link #lazyModel} property.
	 */
	public LazyDataModel<ProcessInstance> getLazyModel() {
		return lazyModel;
	}
	/**
	 * Sets the {@link #lazyModel} property.
	 * @param lazyModel the {@link #lazyModel} property to set.
	 */
	public void setLazyModel(LazyDataModel<ProcessInstance> lazyModel) {
		this.lazyModel = lazyModel;
	}
	/**
	 * Gets the {@link #selectedInstance} property.
	 * @return the {@link #selectedInstance} property.
	 */
	public ProcessInstance getSelectedInstance() {
		return selectedInstance;
	}
	/**
	 * Sets the {@link #selectedInstance} property.
	 * @param selectedInstance the {@link #selectedInstance} property to set.
	 */
	public void setSelectedInstance(ProcessInstance selectedInstance) {
		this.selectedInstance = selectedInstance;
	}
	/**
	 * Gets the {@link #instances} property.
	 * @return the {@link #instances} property.
	 */
	public List<ProcessInstance> getInstances() {
		return instances;
	}
	/**
	 * Sets the {@link #instances} property.
	 * @param instances the {@link #instances} property to set.
	 */
	public void setInstances(List<ProcessInstance> instances) {
		this.instances = instances;
	}
	/**
	 * Refreshes the process instance list by retrieving once again 
	 * the data from the data base.
	 */
	private void refreshInstanceList() {
		logger.debug("Getting all process instances defined...");
		instances = processInstanceFacade.getAll();
		((LazyProcessInstancesDataModel)lazyModel).setDataSource(instances);
		logger.debug("All process instances retrieved.");		
	}			
	/**
	 * Reloads the process instance table information.
	 * @param event action event
	 */
	public void reloadInstanceTable(ActionEvent event) {
		try {	
			logger.info("User is trying to reload the process instance table...");
			refreshInstanceList();
			manager.infoMsg("instanceTableForm", "es.uc3m.softlab.cbi4pi.slave.event.store.processInstance.reloadInstanceTable.action.success", "es.uc3m.softlab.cbi4pi.slave.event.store.processInstance.reloadInstanceTable.action.success_detail");
			logger.info("User reloaded the process instance table successfully");
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			manager.errorSys("instanceTableForm", ex.getLocalizedMessage(), StaticResources.STRING_NULL);
		}		
	}
	/**
	 * Loads the events table information associated to the {@link #selectedInstance}.
	 */
	public String loadEvents() {
		try {	
			logger.info("User is trying to load the events of process instance '" + selectedInstance + "'...");
			processInstanceFacade.loadEvents(selectedInstance);			
			logger.info("User loaded the process instances successfully");
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			manager.errorSys("instanceTableForm", ex.getLocalizedMessage(), StaticResources.STRING_NULL);
		}
		return null;
	}		
}