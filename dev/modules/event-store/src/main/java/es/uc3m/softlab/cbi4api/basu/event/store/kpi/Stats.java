/* 
 * $Id: Stats.java,v 1.0 2013-03-30 22:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.kpi;

import javax.annotation.PostConstruct;

import es.uc3m.softlab.cbi4api.basu.event.store.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>event-store.properties</i> file. 
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
    	LOAD,
    	MAP_REDUCE_PROCESS_CORRELATION,
        SECONDARY_INDEX_CORRELATION
    }
    /** Configuration object */
    @Autowired
    private Config config;
    /** Stats activation flag */
    private boolean active;
    /** data volume indicator */
    private long dataVolume;
    /** stats batch size */
    private int statsBatchSize;
    /** average operation time */
    private long avgTime;

    /**
     * Post-constructor method to initialize the stats process.
     */
    @PostConstruct
    public void init() {
        this.avgTime = 0;
        this.dataVolume = 0;
        this.statsBatchSize = config.getStatsBatchSize();
        this.active = config.isStatsActive();
    }
    /**
     * Write statistical performance entry in the stats result file.
     * @param op performance statistical operation.
     * @param clazz performance class that the operation has been performed on.
     * @param instance instance operation.
     * @param start start time.
     * @param end end time.
     */
    synchronized public void writeStat(Operation op, Class<?> clazz, Object instance, long start, long end) {
    	if (active) {
            if (avgTime == 0)
                avgTime = toMilliseconds(end - start);
            else
                avgTime = (avgTime + toMilliseconds(end - start)) / 2;
            if (op == Operation.WRITE)
                dataVolume ++;
            if (op == Operation.SECONDARY_INDEX_CORRELATION && (dataVolume % statsBatchSize == 0))
                logger.info(String.format("%s;%s;%s;%s;%s;%s;%s;%s", dataVolume, op, clazz.getName(), String.valueOf(instance), start, end, toMilliseconds(end - start), avgTime));
    	}
    }
    /**
     * Converts from nanoseconds to milliseconds
     * @param nanoseconds number of nanoseconds to convert
     * @return nanoseconds converted
     */
    private static long toMilliseconds(long nanoseconds) {
        return nanoseconds / 1000000L;
    }
}