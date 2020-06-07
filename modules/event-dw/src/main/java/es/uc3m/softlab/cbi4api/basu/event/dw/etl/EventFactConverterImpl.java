/* 
 * $Id: EventFactConverterImpl.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for converting the event store information into facts.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventFactConverter.COMPONENT_NAME)
public class EventFactConverterImpl implements EventFactConverter {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(EventFactConverterImpl.class);
    
    /**
     * 
     * @param processes
     * @param activities
     * @return
     */
    public List<EventFact> transform(List<ProcessInstance> processes, List<ActivityInstance> activities) {
    	List<EventFact> processFacts = transformProcess(processes);
    	List<EventFact> activityFacts = transformActivities(activities);
    	activityFacts.addAll(processFacts);
    	return activityFacts;
    }
    /**
     * 
     * @param instances
     * @return
     */
    private List<EventFact> transformProcess(List<ProcessInstance> instances) {
    	logger.debug("Transforming all process instances defined in event store into facts.");
    	List<EventFact> facts = new ArrayList<EventFact>();
    	for (ProcessInstance instance : instances) {
    		es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance _instance = transform(instance);

    		/* fact construction */
    		EventFact fact = new EventFact();

    		/* order events */
    		if (instance.getEvents() == null)
    			continue;

    		/* set the process instance dimension */
    		fact.setProcess(_instance);
    		/* sets the metrics */
    		fact.setStartTime(instance.getStartTime());
    		fact.setEndTime(instance.getEndTime());
    		fact.setTurnAround(instance.getTurnAround());
    		fact.setChangeOver(instance.getChangeOverTime());
    		fact.setWait(instance.getWaitingTime());
    		fact.setSuspend(instance.getSuspendedTime());
    		fact.setProcessing(instance.getProcessingTime());

    		facts.add(fact);								
    	}
    	return facts;		
    }  
    /**
     * 
     * @param instances
     * @return
     */
    private List<EventFact> transformActivities(List<ActivityInstance> instances) {
    	logger.debug("Transforming all activity instances defined in event store into facts.");
    	List<EventFact> facts = new ArrayList<EventFact>();
    	for (ActivityInstance instance : instances) {
    		es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance _instance = transform(instance);

    		/* fact construction */
    		EventFact fact = new EventFact();

    		/* order events */
    		if (instance.getEvents() == null)
    			continue;
    		/* set the process & activity instance dimension */
    		fact.setActivity(_instance);
    		fact.setProcess(transform(instance.getEventList().get(0).getProcessInstance()));
    		/* sets the metrics */
    		fact.setStartTime(instance.getStartTime());
    		fact.setEndTime(instance.getEndTime());
    		fact.setTurnAround(instance.getTurnAround());
    		fact.setChangeOver(instance.getChangeOverTime());
    		fact.setWait(instance.getWaitingTime());
    		fact.setSuspend(instance.getSuspendedTime());
    		fact.setProcessing(instance.getProcessingTime());

    		facts.add(fact);								
    	}
    	return facts;		
    } 
    /**
     * Transform the event store domain {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} to the
     * data warehouse star dimension domain {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance}
     * 
     * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance} object to transform.
     * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance} object transformed.
     */
    private es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance transform(ProcessInstance instance) {
    	if (instance == null)
    		return null;
    	es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance _instance =
				new es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance();
    	_instance.setId(instance.getId());
    	_instance.setName(instance.getName());    	
    	_instance.setDescription(instance.getDescription());
    	//_instance.setCorrelatorId(instance.getCorrelatorId());
    	_instance.setInstanceSrcId(instance.getInstanceSrcId());
    	_instance.setModel(transform(instance.getModel()));    	    	
    	return _instance;
    }     
    /**
     * Transform the event store domain {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} to the
     * data warehouse star dimension domain {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance}
     * 
     * @param instance {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance} object to transform.
     * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance} object transformed.
     */
    private es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance transform(ActivityInstance instance) {
    	if (instance == null)
    		return null;
    	es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance _instance =
				new es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance();
    	_instance.setId(instance.getId());
    	_instance.setName(instance.getName());
    	_instance.setDescription(instance.getDescription());    	
    	_instance.setInstanceSrcId(instance.getInstanceSrcId());
    	//_instance.setModel(transform(instance.getModel()));
    	_instance.setParent(transform(instance.getParent()));    
    	_instance.setProcess(transform(instance.getEventList().get(0).getProcessInstance()));
    	
    	return _instance;
    }    
    /**
     * Transform the event store domain {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Model} to the
     * data warehouse star dimension domain {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel}
     * 
     * @param model {@link es.uc3m.softlab.cbi4api.basu.event.store.domain.Model} object to transform.
     * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel} object transformed.
     */
    private es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel transform(Model model) {
    	if (model == null)
    		return null;
    	es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel _model =
				new es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessModel();
    	_model.setId(model.getId());
    	_model.setName(model.getName());
    	_model.setDescription(model.getDescription());
    	_model.setSource(model.getSource().getName());
        // TODO: IMPORTANT!!! model.setType( ACTIVITY/PROCESS )
    	return _model;
    }
}