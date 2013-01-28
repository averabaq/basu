/* 
 * $Id: ControllerManager.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.web.mvc.controller;

import es.uc3m.softlab.cbi4pi.slave.web.Config;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

/**
 * Class that managements the controller common features.
 * 
 * @author averab
 */
public class ControllerManager implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 2104249521387183002L;
	/** Instance of this singleton class */
    private static ControllerManager instance = null;
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(ControllerManager.class);    
    
    /**
     * Constructor of this singleton class.     
     */
    private ControllerManager() {    
    }
    /**
     * Initializes this singleton class and creates a new instance
     * if it's already not.
     * 
     * @return instance of this singleton class.
     */
    public static ControllerManager init() {
       	if (instance == null)
    		instance = new ControllerManager();
    	return instance;    	
    }
    /**
     * Gets the instance of this singleton class.
     * 
     * @return instance of this singleton class.
     */
    public static ControllerManager getInstance() {
       	if (instance == null)
    		instance = init();
    	return instance;
    }
    /**
     * Gets the string message of the key property given from the current locale.
     * @param key key property.
     * @return message of the key property.
     */
    public String getMessage(String key) {    	
    	String value = null;
    	try {
    		ResourceBundle propFile = ResourceBundle.getBundle("message_" + Config.getInstance().getLocale().getLanguage());     
    		value = propFile.getString(key);    		
    	}
    	catch (MissingResourceException mre) {
    		logger.warn("Not found " + key + " message property. Is it defined?");
    		logger.error(mre.getMessage());         
    	}    	
    	return value;    	
    }
    /**
     * Gets the string message of the key property given.
     * @param key key property.
     * @return message of the key property.
     */
    public String getMessage(String key, Locale locale) {
    	String value = null;
    	try {
    		ResourceBundle propFile = ResourceBundle.getBundle("message_" + locale.getLanguage());     
    		value = propFile.getString(key);    		
    	}
    	catch (MissingResourceException mre) {
    		logger.warn("Not found " + key + " message property. Is it defined?");
    		logger.error(mre.getMessage());         
    	}    	
    	return value;    	
    }    
	/**
	 * Gets a controller at the current faces context by the 
     * managed bean name.
     * 
	 * @param <T> 
	 * @param controllerName controller name to find.
	 * @param controllerClass controller class to find.
	 * @return controller found, null if it does not exist.
	 */
	public <T> T lookUpController(String controllerName, Class<T> controllerClass) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return controllerClass.cast(context.getApplication().evaluateExpressionGet(context, "#{" + controllerName + "}", controllerClass));
	}
	/**
	 * Appends an information message to the current faces context throughout the 
	 * configuration of i18n messages.
	 * @param componentId component identifier associated to the error.
	 * @param messageKey message key taken from message bundle
	 * @param messageDetailKey message detail key taken from message bundle
	 * @param args message format arguments
	 */
	public void infoMsg(String componentId, String messageKey, String messageDetailKey, Object... args) {		
		FacesContext facesContext = FacesContext.getCurrentInstance();		 	
		String msg = MessageFormat.format(getMessage(messageKey), args); 
		String det = MessageFormat.format(getMessage(messageDetailKey), args);
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, det); 
		facesContext.addMessage(componentId, facesMessage);	
	}		
	/**
	 * Appends an warning message to the current faces context throughout the 
	 * configuration of i18n messages.
	 * @param componentId component identifier associated to the error.
	 * @param messageKey message key taken from message bundle
	 * @param messageDetailKey message detail key taken from message bundle
	 * @param args message format arguments
	 */
	public void warnMsg(String componentId, String messageKey, String messageDetailKey, Object... args) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		 	
		String msg = MessageFormat.format(getMessage(messageKey), args); 
		String det = MessageFormat.format(getMessage(messageDetailKey), args);
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, det); 
		facesContext.addMessage(componentId, facesMessage);	
	}	
	/**
	 * Appends an error message to the current faces context throughout the 
	 * configuration of i18n messages.
	 * @param componentId component identifier associated to the error.
	 * @param messageKey message key taken from message bundle
	 * @param messageDetailKey message detail key taken from message bundle
	 * @param args message format arguments
	 */
	public void errorMsg(String componentId, String messageKey, String messageDetailKey, Object... args) {		
		FacesContext facesContext = FacesContext.getCurrentInstance();		 	
		String msg = MessageFormat.format(getMessage(messageKey), args); 
		String det = MessageFormat.format(getMessage(messageDetailKey), args);
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, det); 
		facesContext.addMessage(componentId, facesMessage);	
	}	
	/**
	 * Appends an information message to the current faces context.
	 * 
	 * @param componentId component identifier associated to the error.
	 * @param message message to append to the faces message context.
	 * @param messageDetail message to append to the faces message context.
	 */
	public void infoSys(String componentId, String message, String messageDetail) {		
		FacesContext facesContext = FacesContext.getCurrentInstance();		 	
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, messageDetail); 
		facesContext.addMessage(componentId, facesMessage);	
	}		
	/**
	 * Appends an error message to the current faces context.
	 * 
	 * @param componentId component identifier associated to the error.
	 * @param message message to append to the faces message context.
	 * @param messageDetail message to append to the faces message context.
	 */
	public void errorSys(String componentId, String message, String messageDetail) {		
		FacesContext facesContext = FacesContext.getCurrentInstance();		 	
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, messageDetail); 
		facesContext.addMessage(componentId, facesMessage);	
	}	
}