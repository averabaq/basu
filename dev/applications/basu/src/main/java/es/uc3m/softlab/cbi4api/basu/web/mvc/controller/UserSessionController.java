/* 
 * $Id: ProcessMappingController.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.web.mvc.controller;

import es.uc3m.softlab.cbi4api.basu.web.Config;
import es.uc3m.softlab.cbi4api.basu.web.StaticResources;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
@Scope("session")
@Controller(value=StaticResources.MANAGED_BEAN_USER_SESSION_CONTROLLER)
public class UserSessionController implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 29132301038462891L;
	/** Logger for tracing. */
	private static final Logger logger = Logger.getLogger(UserSessionController.class);	
	/** Controller manager for common stuff management */
	@SuppressWarnings("unused")
	private ControllerManager manager = ControllerManager.getInstance();
	/** User welcome message after login */
	private String welcomeMessage;
	
	/**
	 * Creates a new object with a default values for the properties passed by
	 * arguments.
	 */
	public UserSessionController() {		
	}
	/**
	 * Gets the {@link #welcomeMessage} property.
	 * @return the {@link #welcomeMessage} property.
	 */
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	/**
	 * Sets the {@link #welcomeMessage} property.
	 * @param welcomeMessage the {@link #welcomeMessage} property to set.
	 */
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
    /**
     * Gets the bundle package version defined at the manifest file.     
     * @return the bundle package version defined at the manifest file.   
     */
	public String getBundleVersion() {
		return Config.getInstance().getBundleVersion();
	}
	/**
	 * Gets the current sysdate.
	 * @return the current sysdate.
	 */
	public String getSysDate() {
		Locale locale = Config.getInstance().getLocale();
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale);
		return format.format(new Date());
	}
	/**
	 * Logout action. It finalizes the current user session. 
	 */
	public void logout(ActionEvent event) {
		logger.info("User is trying to log out...");
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext external = facesContext.getExternalContext();
		external.getSessionMap().put(StaticResources.SESSION_MAP_USER, null);
		facesContext.getViewRoot().setViewId(StaticResources.VIEW_ID_DESKTOP);
		HttpSession session = (HttpSession)external.getSession(false);
		session.invalidate();	
				
		try {
			external.redirect(external.getRequestContextPath() + StaticResources.ROOT_PAGE);
			facesContext.responseComplete(); 
		} catch (IOException ioex) {
			logger.warn(ioex.fillInStackTrace());
		}
		
		logger.info("User logged out successfully.");
	}	
}