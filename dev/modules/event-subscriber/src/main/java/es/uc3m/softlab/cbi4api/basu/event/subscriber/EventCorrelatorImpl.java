/* 
 * $Id: EventCorrelatorImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ModelFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceFacade;
import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Correlation;
import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.CorrelationElement;
import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for correlating events before being stored into 
 * the global data store. 
 * This interface defines all methods for correlating <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventCorrelator.COMPONENT_NAME)
public class EventCorrelatorImpl implements EventCorrelator {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventCorrelatorImpl.class);
    /** Model session facade */
    @Autowired private ModelFacade modelFacade;
    /** Process instance session facade */
    @Autowired private ProcessInstanceFacade processInstanceFacade;   
    /** Activity instance session facade */
    @Autowired private ActivityInstanceFacade activityInstanceFacade;

    /**
     * Correlates the incoming {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event} 
     * by obtaining an existing process instance or creating a new one if necessary. 
     * @param event {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event} to get the associated
     * process instance if it exists, otherwise it creates a new one.
     * @param processModel {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel} associated to the incoming 
     * {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event}.
     * @return exact {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} associated to properly 
     * correlated the incoming {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event}.
     * @throws ProcessInstanceException if any process instance exception occurred during processing.
     * @throws ModelException if any model exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     */
    public ProcessInstance correlateProcess(Event event, Source source) throws ModelException, ProcessInstanceException, EventException {
		logger.debug("Correlating incoming event with id " + event.getEventID());
    	ProcessInstance processInstance = null;
		/* gets the process model */
		ProcessModel processModel = (ProcessModel)modelFacade.getModel(event.getProcessDefinitionID(), source);
		
		/* If the process instance identifier is provided at source */
		if (event.getProcessInstanceID() != null) {
			logger.debug("Incoming event with id " + event.getEventID() + " comes from a bpel engine or alternative system that provides a source process instance identifier.");
			String processInstanceId = event.getProcessInstanceID();		
			/* gets the process instance from the event store */
			processInstance = processInstanceFacade.getProcessInstance(processInstanceId, processModel);
	    	/* if the process instance has not been defined yet */
	    	if (processInstance == null) {	    		
	    		processInstance = new ProcessInstance();
	    		/*
	    		 * TODO: request process instance identifier to GBAS instance
	    		 * processInstance.setId(new instance from GBAS); 
	    		 */
	    		processInstance.setInstanceSrcId(processInstanceId);
	    		processInstance.setName(event.getProcessName());
	    		processInstance.setModel(processModel);
	    	}		 
		} else {
			logger.debug("Incoming event with id " + event.getEventID() + " does not provide a process instance identifier. Trying to correlate the event attending to the correlation data attached.");
			/* 
			 * If the process instance identifier is not provided at source,
			 * performs the correlation of events attending to the correlation
			 * information supplied. 
			 */
			Correlation correlation = event.getCorrelation();	
			if (correlation == null)
				throw new EventException(StaticResources.ERROR_INCOMING_EVENT_EMPTY_CORRELATION_DATA, "No source process instance identifier nor correlation data was provided. The event cannot be correlated so it will be ignored.");
			/* transform the event correlation data */
			Set<EventCorrelation> eventCorrelations = transformCorrelation(correlation.getCorrelationElement());
			/* gets the process instance from the event store */
			processInstance = processInstanceFacade.getProcessInstance(eventCorrelations, processModel);
	    	/* if the process instance has not been defined yet */
	    	if (processInstance == null) {
	    		processInstance = new ProcessInstance();
	    		/*
	    		 * TODO: request process instance identifier to GBAS instance
	    		 * processInstance.setId(new instance from GBAS); 
	    		 */
	    		processInstance.setInstanceSrcId(null);	    		
	    		processInstance.setName(event.getProcessName());
	    		processInstance.setModel(processModel);
	    	}		 
		}  
		return processInstance;
    }
    /**
     * Correlates the incoming {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event} 
     * by obtaining an existing process instance or creating a new one if necessary. 
     * @param event {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event} to get the associated
     * process instance if it exists, otherwise it creates a new one.
     * @param source {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Source} associated to the 
     * {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel} of the incoming 
     * {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event}.
     * @return right {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} associated to properly 
     * correlated the incoming {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.Event}.
     * @throws ActivityInstanceException if any activity instance exception occurred during processing.
     * @throws ModelException if any model exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     */
    public ActivityInstance correlateActivity(Event event, Source source) throws ModelException, ActivityInstanceException, EventException {
    	logger.debug("Correlating incoming event with id " + event.getEventID());
    	ActivityInstance activityInstance = null;		
		/* if the event refers to an activity */
		if (event.getActivityInstanceID() != null) {
			logger.debug("Incoming event with id " + event.getEventID() + " comes from a bpel engine or alternative system that provides a souce activity instance identifier.");
			/* gets the activity model */
			ActivityModel activityModel = (ActivityModel)modelFacade.getModel(event.getActivityDefinitionID(), source);
			/* if the activity model has not been defined */
			if (activityModel == null) {
				activityModel = new ActivityModel();
				activityModel.setType(ModelType.ACTIVITY);
				activityModel.setName(event.getActivityDefinitionID());	
				activityModel.setModelSrcId(event.getActivityDefinitionID());
				activityModel.setSource(source);
			}							
				
			/* gets the activity instance from the event store */
			String activityInstanceId = event.getActivityInstanceID();
			/* gets the activity instance from the event store */
			activityInstance = activityInstanceFacade.getActivityInstance(activityInstanceId, activityModel);
			
			/* if the activity instance has not been defined yet */
			if (activityInstance == null) {				
				activityInstance = new ActivityInstance();
				activityInstance.setInstanceSrcId(activityInstanceId);
				activityInstance.setName(event.getActivityName());
				activityInstance.setModel(activityModel);
				if (event.getActivityParentID() != null) {
					ActivityInstance parent = activityInstanceFacade.getActivityInstance(event.getActivityParentID(), activityModel);
					activityInstance.setParent(parent);
				}
			} 			
		} else {
			/* the event comes from non-BPEL systems*/
			if (event.getActivityDefinitionID() != null) {	
				logger.debug("Incoming event with id " + event.getEventID() + " does not provide an active instance identifier. Trying to correlate the event attending to the correlation data attached.");
				/* gets the activity model */
				ActivityModel activityModel = (ActivityModel)modelFacade.getModel(event.getActivityDefinitionID(), source);
				/* if the activity model has not been defined */
				if (activityModel == null) {
					activityModel = new ActivityModel();
					activityModel.setType(ModelType.ACTIVITY);
					activityModel.setName(event.getActivityDefinitionID());	
					activityModel.setModelSrcId(event.getActivityDefinitionID());
					activityModel.setSource(source);
				}					
				/* 
				 * If the activity instance identifier is not provided at source,
				 * performs the correlation of events attending to the correlation
				 * information supplied. 
				 */
				Correlation correlation = event.getCorrelation();	
				if (correlation == null)
					throw new EventException(StaticResources.ERROR_INCOMING_EVENT_EMPTY_CORRELATION_DATA, "No source activity instance identifier nor correlation data was provided. The event cannot be correlated so it will be ignored.");
				/* transform the event correlation data */
				Set<EventCorrelation> eventCorrelations = transformCorrelation(correlation.getCorrelationElement());
				/* gets the activity instance from the event store */
				activityInstance = activityInstanceFacade.getActivityInstance(eventCorrelations, activityModel);
		    	/* if the activity instance has not been defined yet */
		    	if (activityInstance == null) {
		    		activityInstance = new ActivityInstance();
		    		activityInstance.setInstanceSrcId(null);	    		
		    		activityInstance.setName(event.getActivityName());
		    		activityInstance.setModel(activityModel);
		    	}			    	
			}
		}
		return activityInstance;
    }
	/**
	 * Transform a list of {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.CorrelationElement} 
	 * objects into a list of {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation} objects.
	 * @param correlations list of {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.bpaf.extension.CorrelationElement} objects.
	 * @return list of {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation} objects.
	 */
	private Set<EventCorrelation> transformCorrelation(List<CorrelationElement> correlations) {
		if (correlations == null)
			return null;
		Set<EventCorrelation> eventCorrelations = new HashSet<EventCorrelation>();
		for (CorrelationElement correlation : correlations) {
			EventCorrelation eventCorrelation = new EventCorrelation();
			eventCorrelation.setKey(correlation.getKey());
			eventCorrelation.setValue(correlation.getValue());
			eventCorrelations.add(eventCorrelation);
		}
		return eventCorrelations;
	}
}