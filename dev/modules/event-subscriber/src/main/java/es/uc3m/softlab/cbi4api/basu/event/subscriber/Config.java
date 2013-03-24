/* 
 * $Id: Config.java,v 1.0 2013-01-23 22:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>event-subscriber.properties</i> file. 
 *
 * @author averab
 */
public class Config {
    /** Instance of this singleton class */
    private static Config instance = null;
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(Config.class);  
    
    /**
     * Constructor of this singleton class.     
     */
    private Config() {    
    }
    /**
     * Initializes this singleton class and creates a new instance
     * if it's already not.
     * 
     * @return instance of this singleton class.
     */
    public static Config init() {
       	if (instance == null)
    		instance = new Config();
    	return instance;    	
    }
    /**
     * Gets the instance of this singleton class.
     * 
     * @return instance of this singleton class.
     */
    public static Config getInstance() {
       	if (instance == null)
    		instance = init();
    	return instance;
    }
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
		String strLocale = getString("cbi4api.basu.event.subscriber.application.locale");
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
}