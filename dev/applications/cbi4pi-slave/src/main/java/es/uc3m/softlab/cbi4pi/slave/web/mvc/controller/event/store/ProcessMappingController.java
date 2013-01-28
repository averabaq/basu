/* 
 * $Id: ProcessMappingController.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.web.mvc.controller.event.store;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessMapping;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.ProcessMappingFacade;
import es.uc3m.softlab.cbi4pi.slave.web.StaticResources;
import es.uc3m.softlab.cbi4pi.slave.web.mvc.controller.ControllerManager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
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
@Controller(value=StaticResources.MANAGED_BEAN_PROCESS_MAPPING_CONTROLLER)
public class ProcessMappingController implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1963011803924790L;
	/** Logger for tracing. */
	private static final Logger logger = Logger.getLogger(ProcessMappingController.class);
	/** Controller manager for common stuff management */
	private ControllerManager manager = ControllerManager.getInstance();
	/** Process mappings selected */
	private ProcessMapping selectedMapping;
	/** Process mappings */
	private List<ProcessMapping> mappings;
	/** Process mapping facade session bean */
	@Autowired private ProcessMappingFacade processMappingFacade;

	/**
	 * Creates a new object with a default values for the properties passed by
	 * arguments.
	 */
	public ProcessMappingController() {	
	}
	/**
	 * Initializes and setup the controller.
	 */
	@PostConstruct
	public void init() {
		selectedMapping = new ProcessMapping();
		refreshMappingList();		
	}
	/**
	 * Refreshes the process mapping list by retrieving once again 
	 * the data from the data base.
	 */
	public void refreshMappingList() {
		logger.debug("Getting all process mappings defined...");
		mappings = processMappingFacade.getAll();
		logger.debug("All process mappings retrieved.");		
	}	
	/**
	 * Gets the {@link #mappings} property.
	 * @return the {@link #mappings} property.
	 */
	public List<ProcessMapping> getMappings() {
		return mappings;
	}
	/**
	 * Sets the {@link #mappings} property.
	 * @param mappings the {@link #mappings} property to set.
	 */
	public void setMappings(List<ProcessMapping> mappings) {
		this.mappings = mappings;
	}
	/**
	 * Gets the {@link #selectedMapping} property.
	 * @return the {@link #selectedMapping} property.
	 */
	public ProcessMapping getSelectedMapping() {
		return selectedMapping;
	}
	/**
	 * Sets the {@link #selectedMapping} property.
	 * @param selectedMapping the {@link #selectedMapping} property to set.
	 */
	public void setSelectedMapping(ProcessMapping selectedMapping) {
		this.selectedMapping = selectedMapping;							
	}	
	/**
	 * Reloads the process mapping table information.
	 * @param event action event
	 */
	public void reloadMappingsTable(ActionEvent event) {
		try {	
			logger.info("User is trying to reload the process mapping table...");
			refreshMappingList();
			manager.infoMsg("mappingsTableForm", "es.uc3m.softlab.cbi4pi.slave.event.store.processMappings.reloadMappingsTable.action.success", "es.uc3m.softlab.cbi4pi.slave.event.store.processMappings.reloadMappingsTable.action.success_detail");
			logger.info("User reloaded the process mapping table successfully");
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			manager.errorSys("mappingsTableForm", ex.getLocalizedMessage(), StaticResources.STRING_NULL);
		}
	}	
}