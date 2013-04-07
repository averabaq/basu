/* 
 * $Id: Stats.java,v 1.0 2013-03-30 22:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HEvent;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HModel;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HSequenceGenerator;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>event-publisher.properties</i> file 
 * located under the 'conf' directory. 
 *
 * @author averab
 */
@Scope(value=BeanDefinition.SCOPE_SINGLETON)
@Component(value="stats")
public class Stats {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(Stats.class);  
    /** Operation stats */
    public enum Operation {
    	READ_MAX_ID,
    	READ_BY_ID,
    	READ_BY_SOURCE_DATA,
    	READ,
    	WRITE,
    	DELETE,
    	UPDATE,
    	LOAD
    }
    /** Statistic file */
    private PrintWriter writer;
    /** Configuration object */
    @Autowired private Config config;	

    @PostConstruct
    public void init() {
		try {
			if (config.isStatsActive()) {
				String statsFile = config.getString(StaticResources.BUNDLE_CONFIG_STATS_FILE_KEY);
				this.writer = new PrintWriter(new BufferedWriter(new FileWriter(statsFile, true)));
			} 
		} catch (IOException ex) {			
			logger.fatal(ex.fillInStackTrace());
		}
    }
    /**
     * Write statistical performance entry in the stats result file.
     * @param op performance statistical operation.
     * @param obj performance object.
     * @param start start time.
     * @param end end time.
     */
    synchronized public void writeStat(Operation op, Object obj, long start, long end) {
    	if (config.isStatsActive()) {
    		if (obj instanceof HEvent)
    			writer.printf("%s,%s,%s,%s,%s,%s\n", op, "EVENT", ((HEvent)obj).getEventID(), start, end, (end - start));
    		else if (obj instanceof HProcessInstance)
    			writer.printf("%s,%s,%s,%s,%s,%s\n", op, "PROCESS_INSTANCE", ((HProcessInstance)obj).getInstanceSrcId(), start, end, (end - start));
    		else if (obj instanceof HActivityInstance)	
    			writer.printf("%s,%s,%s,%s,%s,%s\n", op, "ACTIVITY_INSTANCE", ((HActivityInstance)obj).getInstanceSrcId(), start, end, (end - start));
    		else if (obj instanceof HModel)	
    			writer.printf("%s,%s,%s,%s,%s,%s\n", op, "MODEL", ((HModel)obj).getModelSrcId(), start, end, (end - start));
    		else if (obj instanceof HSequenceGenerator)	
    			writer.printf("%s,%s,%s,%s,%s,%s\n", op, "SEQUENCE_GENERATOR", ((HSequenceGenerator)obj), start, end, (end - start));   		
    		else if (obj != null)
    			logger.warn("Unkown statistical performance object object '" + obj.getClass() + "'.");
    		writer.flush();
    	}
    }
}