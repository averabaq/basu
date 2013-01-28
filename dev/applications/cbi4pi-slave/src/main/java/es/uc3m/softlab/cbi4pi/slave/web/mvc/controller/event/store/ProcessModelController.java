/* 
 * $Id: ProcessModelController.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.web.mvc.controller.event.store;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.ProcessModelFacade;
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
@Controller(value=StaticResources.MANAGED_BEAN_PROCESS_MODEL_CONTROLLER)
public class ProcessModelController implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 532158492149587309L;
	/** Logger for tracing. */
	private static final Logger logger = Logger.getLogger(ProcessModelController.class);
	/** Controller manager for common stuff management */
	private ControllerManager manager = ControllerManager.getInstance();
	/** Lazy data model for partial loading of process instances */
	private LazyDataModel<ProcessInstance> lazyModel;
	/** Process model selected */
	private ProcessModel selectedModel;
	/** Process models */
	private List<ProcessModel> models;
	/** Process model facade session bean */
	@Autowired private ProcessModelFacade processModelFacade;

	/**
	 * Creates a new object with a default values for the properties passed by
	 * arguments.
	 */
	public ProcessModelController() {
		lazyModel = new LazyProcessInstancesDataModel(); 
	}
	/**
	 * Initializes and setup the controller.
	 */
	@PostConstruct
	public void init() {
		selectedModel = new ProcessModel();
		refreshModelList();		
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
	 * Gets the {@link #selectedModel} property.
	 * @return the {@link #selectedModel} property.
	 */
	public ProcessModel getSelectedModel() {
		return selectedModel;
	}
	/**
	 * Sets the {@link #selectedModel} property.
	 * @param selectedModel the {@link #selectedModel} property to set.
	 */
	public void setSelectedModel(ProcessModel selectedModel) {
		this.selectedModel = selectedModel;
	}
	/**
	 * Gets the {@link #models} property.
	 * @return the {@link #models} property.
	 */
	public List<ProcessModel> getModels() {
		return models;
	}
	/**
	 * Sets the {@link #models} property.
	 * @param models the {@link #models} property to set.
	 */
	public void setModels(List<ProcessModel> models) {
		this.models = models;
	}
	/**
	 * Refreshes the process model list by retrieving once again 
	 * the data from the data base.
	 */
	private void refreshModelList() {
		logger.debug("Getting all process models defined...");
		models = processModelFacade.getAll();
		logger.debug("All process models retrieved.");		
	}			
	/**
	 * Reloads the process model table information.
	 * @param event action event
	 */
	public void reloadModelsTable(ActionEvent event) {
		try {	
			logger.info("User is trying to reload the process models table...");
			refreshModelList();
			manager.infoMsg("modelsTableForm", "es.uc3m.softlab.cbi4pi.slave.event.store.processModels.reloadModelsTable.action.success", "es.uc3m.softlab.cbi4pi.slave.event.store.processModels.reloadModelsTable.action.success_detail");
			logger.info("User reloaded the process models table successfully");
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			manager.errorSys("modelsTableForm", ex.getLocalizedMessage(), StaticResources.STRING_NULL);
		}
	}
	/**
	 * Loads the process instance table information associated to the {@link #selectedModel}.
	 */
	public String loadInstances() {
		try {	
			logger.info("User is trying to load the process instances of process model " + selectedModel + "...");
			processModelFacade.loadInstances(selectedModel);
			((LazyProcessInstancesDataModel)lazyModel).setDataSource(selectedModel.getInstanceList());
			logger.info("User loaded the process instances successfully");
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			manager.errorSys("modelsTableForm", ex.getLocalizedMessage(), StaticResources.STRING_NULL);
		}
		return null;
	}		
}