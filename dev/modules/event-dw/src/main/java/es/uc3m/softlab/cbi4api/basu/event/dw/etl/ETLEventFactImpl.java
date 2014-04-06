/* 
 * $Id: ETLEventFactImpl.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Specific ETL (Extract, Transform & Load) service implementation for building up
 * the data warehouse.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional
@Service(value=ETLEventFact.SERVICE_NAME)
public class ETLEventFactImpl implements ETLEventFact {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(ETLEventFact.class);  
    /** Event fact reader to extract the source data */
	@Autowired private EventFactReader extractor;
    /** Event fact converter to transform the source data into facts */
    @Autowired private EventFactConverter transformer; 
    /** Event fact writer to store the facts into the data warehouse */
    @Autowired private EventFactWriter loader; 

    /**
     * Process the ETL (Extract, Transform & Load) in full. This method generates the data warehouse
     * by reading the event information from the global event store, transforming the timing execution 
     * data into facts (metrics), and loads the facts into the data warehouse.
     * 
     * @throws EventFactReaderException if any illegal data access or inconsistent event fact reader data error occurred.
     * @throws EventFactWriterException if any illegal data access or inconsistent event fact writer data error occurred.
	 */
	@Async
	public void processInFull() throws EventFactReaderException, EventFactWriterException {
		logger.info("Processing ETL module to generate the data warehouse in full...");
		/* extracts all the source information */
		List<ProcessInstance> processes = extractor.getAllProcessInstancesInFull();
		List<ActivityInstance> activities = extractor.getAllActivityInstancesInFull();
		/* transforms the source information by generating all dimensions and facts */
		List<EventFact> facts = transformer.transform(processes, activities);
		/* loads the facts into the data warehouse */
		loader.storeEventFacts(facts);
		logger.info("Data warehouse generated in full successfully.");
	}
}