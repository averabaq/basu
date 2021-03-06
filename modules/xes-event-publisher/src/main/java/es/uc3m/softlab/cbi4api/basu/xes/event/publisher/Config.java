/* 
 * $Id: Config.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>xes-event-publisher.properties</i> file. 
 *
 * @author averab
 */
@Scope(value=BeanDefinition.SCOPE_SINGLETON)
@Component(value=StaticResources.COMPONENT_NAME_CONFIG)
public class Config {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(Config.class);  
    
    /**
     * Gets the string value of the key property given.
     * @param key key property.
     * @return value of the key property.
     */
    public String getString(String key) {
    	String value = null;
    	try {
    		ResourceBundle propFile = ResourceBundle.getBundle(StaticResources.CONFIG_RESOURCE_BUNDLE);     
    		value = propFile.getString(key);    		
    	}
    	catch (MissingResourceException mre) {
    		logger.warn("Not found " + key + " property. Is it defined?");
    		logger.error(mre.getMessage());         
    	}    	
    	return value;  
    }   
    /**
     * Gets the local application configuration from business logic layer. It may differ from
     * the presentation layer. 
     * 
     * @return locale locale application configuration.
     */
    public Locale getLocale() {
		Locale locale;
		String strLocale = getString("cbi4api.basu.xes.event.publisher.application.locale");
		if (strLocale == null) 
			strLocale = StaticResources.DEFAULT_LOCALE;				
		String locales[] = strLocale.split("_");
		try {
			locale = new Locale(locales[0], locales[1]);
		} catch(ArrayIndexOutOfBoundsException auobex) {
			logger.warn("Locale from properties file bad formed. Setting default locale...");
			locales = StaticResources.DEFAULT_LOCALE.split("_");		
			locale = new Locale(locales[0], locales[1]);
		}
    	return locale;    	
    }       
    /**
     * Gets the source id which represents the origin of the event data. 
     * 
     * @return source identifier.
     */
    public String getSourceId() {		
		String sourceId = getString("cbi4api.basu.xes.event.publisher.event.source.id");
		if (sourceId == null) 
			logger.error("The property 'cbi4api.basu.xes.event.publisher.event.source.id' containing the identification of the current source could not be found. Please, provide this information.");
		return sourceId;
    }   
    /**
     * Gets the broker url endpoint where to send the event data. 
     * 
     * @return broker url.
     */
    public String getBrokerUrl() {		
		String url = getString("cbi4api.basu.xes.event.publisher.activemq.broker.url");
		if (url == null) 
			logger.error("The property 'cbi4api.basu.xes.event.publisher.activemq.broker.url' containing the identification of the broker url could not be found. Please, provide this information.");
		return url;
    }     
}