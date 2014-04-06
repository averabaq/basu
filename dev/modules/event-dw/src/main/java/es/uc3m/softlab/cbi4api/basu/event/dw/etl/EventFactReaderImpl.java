/* 
 * $Id: EventFactReaderImpl.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceFacade;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for retrieving the data warehouse information 
 * located at the event store.
 *  
 * @author averab
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventFactReader.COMPONENT_NAME)
public class EventFactReaderImpl implements EventFactReader {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventFactReaderImpl.class);
    /** Process instance service session facade */
    @Autowired private ProcessInstanceFacade processInstanceFacade;
    /** Activity instance service session facade */
    @Autowired private ActivityInstanceFacade activityInstanceFacade;
	
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} defined
	 * at the event store in order to build up the hierarchies, dimensions and facts from
	 * such information. 
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} defined.
	 * @throws EventFactReaderException if any error occurred during event information retrieval.
	 */	
	public List<ProcessInstance> getAllProcessInstancesInFull() throws EventFactReaderException {
		logger.debug("Retrieving all process instances defined at the event store...");
		List<ProcessInstance> instances = processInstanceFacade.getAll();		
		for (ProcessInstance instance : instances) {
			try {
				processInstanceFacade.loadEvents(instance);				
			} catch (ProcessInstanceException piex) {
				logger.error(piex.fillInStackTrace());
			}
		}
		return instances;
	}
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} defined
	 * at the event store in order to build up the hierarchies, dimensions and facts from
	 * such information. 
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} defined.
	 * @throws EventFactReaderException if any error occurred during event information retrieval.
	 */	
	public List<ActivityInstance> getAllActivityInstancesInFull() throws EventFactReaderException {
		logger.debug("Retrieving all activity instances defined at the event store...");
		List<ActivityInstance> instances = activityInstanceFacade.getAll();							
		for (ActivityInstance instance : instances) {
			try {
				activityInstanceFacade.loadEvents(instance);				
			} catch (ActivityInstanceException piex) {
				logger.error(piex.fillInStackTrace());
			}
		}
		return instances;
	}
}