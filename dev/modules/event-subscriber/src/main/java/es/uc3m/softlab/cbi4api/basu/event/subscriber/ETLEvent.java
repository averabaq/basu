/* 
 * $Id: ETLEvent.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.SourceException;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Specific ETL (Extract, Transform & Load) service interface for F4BPA-BPAF  
 * events as an extension of <a href="http://www.wfmc.org/business-process-analytics-format.html">BPAF</a>.
 * 
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from heterogeneous systems, transforming its F4BPA-BPAF xml content 
 * into the BPAF entity object model, and loading the results into the database.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional
@Service(value=StaticResources.SERVICE_NAME_EVENT_ETL)
public class ETLEvent implements MessageListener {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(ETLEvent.class);  
    /** Event reader to extract the event data from the jms messages */
	@Autowired private EventReader extractor;
    /** Event converter to transform  into the database */
    @Autowired private EventConverter transformer; 
    /** Event writer to store the formatted event into the database */
    @Autowired private EventWriter loader; 

    /**
	 * Passes a message to the listener.
	 * @param msg the message passed to the listener
	 */
	@Override
	public void onMessage(Message msg) {
		try {
			es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event bpafEvent;			
			/* extract event */
			bpafEvent = extractor.extractEvent(msg);
			/* transform event */
			es.uc3m.softlab.cbi4api.basu.event.store.domain.Event event = transformer.transform(bpafEvent);
			/* load the event into the database */
			loader.storeEvent(event);	
		} catch(EventException evex) {	
			if (evex.getCode() == StaticResources.WARN_EVENT_WITH_NO_STATE_TRANSITION)
				logger.warn(evex);
			else
				logger.error(evex.fillInStackTrace());			
		} catch(ProcessInstanceException piex) {			
			logger.error(piex.fillInStackTrace());			
		} catch(ActivityInstanceException aiex) {			
			logger.error(aiex.fillInStackTrace());			
		} catch(EventReaderException erex) {
			logger.error(erex.fillInStackTrace());		
		} catch(SourceException sex) {
			logger.error(sex.fillInStackTrace());		
		} catch(ModelException mex) {
			logger.error(mex.fillInStackTrace());		
		}
	}
}