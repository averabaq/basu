/* 
 * $Id: ETLEvent.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.mxml.event.publisher;

/**
 * Specific ETL (Extract, Transform & Load) service interface for ApacheODE events.
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from BPEL engines, transforming its contents into
 * this format, and publish the results into a JMS queue.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ETLEvent {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.SERVICE_NAME_EVENT_ETL;        

	/**
	 * Process all events by executing the ETL engine.
	 * @throws EventPublisherException if any error occurred during 
	 * the ETL execution process.
	 */
	public void process() throws EventPublisherException;
}