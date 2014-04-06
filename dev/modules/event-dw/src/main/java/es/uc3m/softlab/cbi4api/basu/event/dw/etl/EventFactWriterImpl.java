/* 
 * $Id: EventFactWriterImpl.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.dao.ActivityInstanceDAO;
import es.uc3m.softlab.cbi4api.basu.event.dw.dao.ActivityModelDAO;
import es.uc3m.softlab.cbi4api.basu.event.dw.dao.EventFactDAO;
import es.uc3m.softlab.cbi4api.basu.event.dw.dao.ProcessInstanceDAO;
import es.uc3m.softlab.cbi4api.basu.event.dw.dao.ProcessModelDAO;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityModel;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for writing event facts to the data warehouse. 
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventFactWriter.COMPONENT_NAME)
public class EventFactWriterImpl implements EventFactWriter {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventFactWriterImpl.class);
	/** Process model data access object */
    @Autowired private ProcessModelDAO processModelDAO;
	/** Activity model data access object */
    @Autowired private ActivityModelDAO activityModelDAO;
	/** Process instance data access object */
    @Autowired private ProcessInstanceDAO processInstanceDAO;
	/** Activity instance data access object */
    @Autowired private ActivityInstanceDAO activityInstanceDAO;
    /** Event fact data access object */
    @Autowired private EventFactDAO eventFactDAO;
    
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity objects into the data warehouse.
	 * 
	 * @param facts list of {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity objects to save.
	 * @throws EventFactWriterException if any illegal data access or inconsistent event fact writer data error occurred.
	 */
	public void storeEventFacts(List<EventFact> facts) throws EventFactWriterException {
		logger.debug("Saving a list of event facts...");
		/* save process instance dimension */
		for (EventFact fact : facts) {		
			try {
				ProcessInstance instance = fact.getProcess();
				if (instance != null && processInstanceDAO.findById(instance.getId()) == null) {
					if (instance.getModel() != null) {
						ProcessModel model = processModelDAO.findById(instance.getModel().getId());
						if (model != null)
							instance.setModel(model);
					}
					processInstanceDAO.save(fact.getProcess());	
				}					
			} catch (Exception ex) {
				throw new EventFactWriterException(ex);
			}
		}
		/* save activity instance dimension */
		for (EventFact fact : facts) {		
			try {
				ActivityInstance instance = fact.getActivity();
				if (instance != null && activityInstanceDAO.findById(instance.getId()) == null) {
					/* checks the process */
					if (instance.getProcess() == null) {
						throw new EventFactWriterException("Activity instance '" + instance + "' has not any process instance associated.");
					} else {
						ProcessInstance process = processInstanceDAO.findById(instance.getProcess().getId());
						if (process == null)
							throw new EventFactWriterException("Activity instance " + instance + " has not any persisted process instance associated.");
						instance.setProcess(process);						
					}
					/* checks the mode */
					if (instance.getModel() != null) {
						ActivityModel model = activityModelDAO.findById(instance.getModel().getId());
						if (model != null)
							instance.setModel(model);
					}
					/* checks its parent */
					if (instance.getParent() != null) {
						ActivityInstance parent = activityInstanceDAO.findById(instance.getParent().getId());
						if (parent != null)
							instance.setParent(parent);
					}

					activityInstanceDAO.save(fact.getActivity());	
				}					
			} catch (Exception ex) {
				throw new EventFactWriterException(ex);
			}
		}
		/* save facts table */
		for (EventFact fact : facts) {		
			try {
				eventFactDAO.save(fact);
			} catch (Exception ex) {
				throw new EventFactWriterException(ex);
			}
		}
		logger.info("List of event facts stored successfully.");
	} 
}