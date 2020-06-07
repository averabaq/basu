/* 
 * $Id: ETLEventFact.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;

/**
 * Specific ETL (Extract, Transform & Load) service interface for building up
 * the data warehouse.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ETLEventFact {
 	/** Spring component name */
    public static final String SERVICE_NAME = StaticResources.SERVICE_NAME_EVENT_FACT_ETL;        

    /**
     * Process the ETL (Extract, Transform & Load) in full. This method generates the data warehouse
     * by reading the event information from the global event store, transforming the timing execution 
     * data into facts (metrics), and loads the facts into the data warehouse.
     * 
     * @throws EventFactReaderException if any illegal data access or inconsistent event fact reader data error occurred.
     * @throws EventFactWriterException if any illegal data access or inconsistent event fact writer data error occurred.
	 */
	public void processInFull() throws EventFactReaderException, EventFactWriterException;
}