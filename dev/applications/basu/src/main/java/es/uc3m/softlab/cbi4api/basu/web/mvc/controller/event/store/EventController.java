/* 
 * $Id: EventController.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.web.mvc.controller.event.store;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventFacade;
import es.uc3m.softlab.cbi4api.basu.web.StaticResources;
import es.uc3m.softlab.cbi4api.basu.web.mvc.controller.ControllerManager;

import java.io.Serializable;

import javax.annotation.PostConstruct;

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
@Controller(value=StaticResources.MANAGED_BEAN_EVENT_CONTROLLER)
public class EventController implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 7264755235525616819L;
	/** Logger for tracing. */
	private static final Logger logger = Logger.getLogger(EventController.class);
	/** Controller manager for common stuff management */
	private ControllerManager manager = ControllerManager.getInstance();
	/** Event selected */	
	private Event selectedEvent;
	/** Event facade session bean */
	@Autowired private EventFacade eventFacade;

	/**
	 * Creates a new object with a default values for the properties passed by
	 * arguments.
	 */
	public EventController() {
	}
	/**
	 * Initializes and setup the controller.
	 */
	@PostConstruct
	public void init() {
		selectedEvent = new Event();
	}
	/**
	 * Gets the {@link #selectedEvent} property.
	 * @return the {@link #selectedEvent} property.
	 */
	public Event getSelectedEvent() {
		return selectedEvent;
	}
	/**
	 * Sets the {@link #selectedEvent} property.
	 * @param selectedEvent the {@link #selectedEvent} property to set.
	 */
	public void setSelectedEvent(Event selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
	/**
	 * Loads the event lazy data information associated to the {@link #selectedEvent}.
	 */
	public void loadLazyData() {
		try {	
			logger.info("User is trying to load the lazy data of event '" + selectedEvent + "'...");			
			eventFacade.loadEventData(selectedEvent);
			eventFacade.loadEventPayload(selectedEvent);
			eventFacade.loadEventCorrelation(selectedEvent);			
			logger.info("User loaded the events successfully");
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			manager.errorSys("eventsTableForm", ex.getLocalizedMessage(), StaticResources.STRING_NULL);
		}
	}	
}