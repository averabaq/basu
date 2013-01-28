/* 
 * $Id: Config.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.ode.event.publisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>event-publisher.properties</i> file 
 * located under the 'conf' directory. 
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
    	FileInputStream conf = null;
    	try {
    		Properties propFile = new Properties();
    		String applicationDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); 
    		applicationDir = new File(applicationDir).getParent();
    		String home = new File(applicationDir).getParent();
    		String path = home + File.separator + "conf" + File.separator + StaticResources.CONFIG_RESOURCE_BUNDLE;
    		try {
    			conf = new FileInputStream(path);
       			propFile.load(conf);
       			value = propFile.getProperty(key);   			
        	} catch(FileNotFoundException fnex) {
        		logger.debug("Property file could not be found in " + path + ". Using default properties defined within the jar file.");
        		/* 
        		 * if the property file could not be found on configuration path, 
        		 * it retrieves the data from properties located in default classpath. 
        		 */
        		ResourceBundle props = ResourceBundle.getBundle(StaticResources.CONFIG_RESOURCE_BUNDLE_NAME);     
        		value = props.getString(key);    	
        	}        		
    	} catch (IOException ioex) {
    		logger.fatal("An error occurred accessing to the property file " + StaticResources.CONFIG_RESOURCE_BUNDLE + " could not be found. Please, make sure it is correctly set under the 'conf' directory.");
    		logger.fatal(ioex.fillInStackTrace());         
    	} catch (MissingResourceException mre) {
    		logger.fatal("Not found " + key + " property. Is it defined?");
    		logger.fatal(mre.fillInStackTrace());   
    	} finally {
    		if (conf != null) {
				try {
					conf.close();
				} catch (IOException ioex) {
					logger.error(ioex.fillInStackTrace());
				}
    		}
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
		String strLocale = getString("cbi4pi.slave.event.publisher.application.locale");
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
		String sourceId = getString("cbi4pi.slave.event.publisher.event.source.id");
		if (sourceId == null) 
			logger.error("The property 'cbi4pi.slave.event.publisher.event.source.id' containing the identification of the current source is not found. Please, provide this information.");
		return sourceId;
    }   
    /**
     * Gets the broker url endpoint where to send the event data. 
     * 
     * @return broker url.
     */
    public String getBrokerUrl() {		
		String url = getString("cbi4pi.slave.event.publisher.activemq.broker.url");
		if (url == null) 
			logger.error("The property 'cbi4pi.slave.event.publisher.activemq.broker.url' containing the identification of the current source is not found. Please, provide this information.");
		return url;
    }     
    /**
     * Gets the timer fixed rate for the task scheduler. 
     * 
     * @return task scheduler fixed rate.
     */
    public Long getTimerFixedRate() {		
    	Long timer = null;
		try {
			timer = Long.valueOf(getString("cbi4pi.slave.event.publisher.scheduler.timer.fixed.rate"));
		} catch(NumberFormatException nfex) {
			logger.fatal("Could not parse 'cbi4pi.slave.event.publisher.scheduler.timer.fixed.rate' property. Number was expected.");
			logger.fatal(nfex.fillInStackTrace());
		}		
		return timer;
    }    
    /**
     * Gets the timer cron for the task scheduler.  
     * 
     * @return task scheduler cron setting.
     */
    public String getTimerCron() {		
		String cron = getString("cbi4pi.slave.event.publisher.scheduler.timer.cron");
		if (cron == null) 
			logger.error("The property 'cbi4pi.slave.event.publisher.scheduler.timer.cron' containing the cron timer for the scheduler is not found. Please, provide this information.");
		return cron;
    }       
    /**
     * Retrieves the latest event identifier read. 
     * @throws IOException if any input/output error occurred.
     */
    public long readLastEvent() throws IOException {
    	Long eventId = 0L;
		String fileName = getString("cbi4pi.slave.event.publisher.application.event.file");
		if (fileName == null) {
			throw new IOException("Cannot write last event information. Property \"cbi4pi.slave.event.publisher.application.event.file\" could not be found. Is it defined?");
		}
		String applicationDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); 
		applicationDir = new File(applicationDir).getParent();
		File file = new File(applicationDir + File.separator + fileName);
		if (file.exists()) {	
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);				
			try {
				eventId = ois.readLong() + 1;
			} finally {
				if (ois != null) ois.close();
				if (fis != null) fis.close();							
			}
		}
		return eventId;
    }
    /**
     * Stores locally the latest event identifier written. 
     * @throws IOException if any input/output error occurred.
     */
    public void writeLastEvent(long eventId) throws IOException {
		String fileName = getString("cbi4pi.slave.event.publisher.application.event.file");
		if (fileName == null) {
			throw new IOException("Cannot write last event information. Property \"cbi4pi.slave.event.publisher.application.event.file\" could not be found. Is it defined?");
		}
		String applicationDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); 
		applicationDir = new File(applicationDir).getParent();
		File file = new File(applicationDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);          
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		try {
			oos.writeLong(eventId);
		} finally {
			if (oos != null) oos.close();
			if (fos != null) fos.close();							
		}
    }
}