/* 
 * $Id: Config.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
     * Gets the pull directory to import and process event data in XES format. 
     * 
     * @return XES pull directory.
     */
    public String getPullDir() {		
		String sourceId = getString("cbi4api.basu.xes.event.publisher.pull.import.dir");
		if (sourceId == null) 
			logger.error("The property 'cbi4api.basu.xes.event.publisher.pull.import.dir' containing the path to XES pull directory could not be found. Please, provide this information.");
		return sourceId;
    }  
    /**
     * If the module is configured to enqueue incoming XES xml files. Otherwise they will be sent 
     * directly to the ETL process. 
     * 
     * @return whether the XES xml imported file will is enqueued or directly processed by the ETL 
     * module.
     */
    public boolean useJmsOnImportXESFiles() {
    	boolean jms = false;
		String useJms = getString("cbi4api.basu.xes.event.publisher.pull.import.enqueue.xml");
		if (useJms == null) 
			logger.warn("The property 'cbi4api.basu.xes.event.publisher.pull.import.enqueue.xml' could not be found. Is it defined?");
		else
			jms = Boolean.valueOf(useJms);
		return jms;
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
    /**
     * Gets the timer fixed rate for the task scheduler. 
     * 
     * @return task scheduler fixed rate.
     */
    public Long getTimerFixedRate() {		
    	Long timer = null;
		try {
			timer = Long.valueOf(getString("cbi4api.basu.xes.event.publisher.scheduler.timer.fixed.rate"));
		} catch(NumberFormatException nfex) {
			logger.fatal("Could not parse 'cbi4api.basu.xes.event.publisher.scheduler.timer.fixed.rate' property. Number was expected.");
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
		String cron = getString("cbi4api.basu.xes.event.publisher.scheduler.timer.cron");
		if (cron == null) 
			logger.error("The property 'cbi4api.basu.xes.event.publisher.scheduler.timer.cron' containing the cron timer for the scheduler could not be found. Please, provide this information.");
		return cron;
    }       
    /**
     * Retrieves the latest event identifier read. 
     * @throws IOException if any input/output error occurred.
     */
    public long readLastEvent() throws IOException {
    	Long eventId = 0L;
		String fileName = getString("cbi4api.basu.xes.event.publisher.application.event.file");
		if (fileName == null) {
			throw new IOException("Cannot write last event information. Property \"cbi4api.basu.xes.event.publisher.application.event.file\" could not be found. Is it defined?");
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
		String fileName = getString("cbi4api.basu.xes.event.publisher.application.event.file");
		if (fileName == null) {
			throw new IOException("Cannot write last event information. Property \"cbi4api.basu.xes.event.publisher.application.event.file\" could not be found. Is it defined?");
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