/* 
 * $Id: EventPublisherScheduler.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Scheduler class that fires the method which will publish the new events
 * to the jms queue/topic.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=StaticResources.COMPONENT_NAME_EVENT_SCHEDULER)
public class EventPublisherScheduler {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventPublisherScheduler.class);
	/** Event reader service */
	@Autowired private ETLEvent etl;
	
	/**
	 * Scheduled task that is run on a fixed rate basis.
	 */
	public void run() {		
		try {
			logger.debug("Performing ETL process...");
			etl.process();
			logger.debug("ETL process performed.");
		} catch(EventPublisherException evpx) {
			logger.error(evpx.fillInStackTrace());
		} 
	}
}
