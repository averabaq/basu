/* 
 * $Id: BusinessObjectAssembler.java,v 1.0 2013-02-16 16:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.dao;

import java.util.Date;
import java.util.HashSet;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventDetail;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.State;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HEvent;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HModel;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HSource;

/**
 * Business object assembler class for parsing and converter between business 
 * domain and entity objects. 
 * 
 * @author averab
 * @version 1.0.0  
 */
public class BusinessObjectAssembler {
    /** Instance of this singleton class */
    private static BusinessObjectAssembler instance = null;
    
    /**
     * Constructor of this singleton class.     
     */
    private BusinessObjectAssembler() {    
    }
    /**
     * Initializes this singleton class and creates a new instance
     * if it's already not.
     * 
     * @return instance of this singleton class.
     */
    public static BusinessObjectAssembler init() {
       	if (instance == null)
    		instance = new BusinessObjectAssembler();
    	return instance;    	
    }
    /**
     * Gets the instance of this singleton class.
     * 
     * @return instance of this singleton class.
     */
    public static BusinessObjectAssembler getInstance() {
       	if (instance == null)
    		instance = init();
    	return instance;
    }
    /**
     * Convert source entity object to business domain object
     * @param hsource entity object to convert.
     * @return domain object.
     */
    public Source toBusinessObject(HSource hsource) {
		Source source = new Source();
		source.setId(hsource.getId());
		source.setName(hsource.getName());
		source.setDescription(hsource.getDescription());
		source.setInetAddress(hsource.getInetAddress());
		source.setPort(hsource.getPort());
		return source;
    }    
    /**
     * Convert source business domain object to entity object
     * @param source business domain object to convert.
     * @return entity object.
     */
    public HSource toEntity(Source source) {
		HSource hsource = new HSource();
		hsource.setId(source.getId());
		hsource.setName(source.getName());
		hsource.setDescription(source.getDescription());
		hsource.setInetAddress(source.getInetAddress());
		hsource.setPort(source.getPort());
		return hsource;
    }      
    /**
     * Convert event entity object to business domain object
     * @param hevent entity object to convert.
     * @return domain object.
     */
    public Event toBusinessObject(HEvent hevent) {
    	Event event = new Event();
    	event.setEventID(hevent.getEventID());
    	event.setTimestamp(new Date(hevent.getTimestamp()));
    	event.setEventDetails(new EventDetail());
    	event.getEventDetails().setCurrentState(State.valueOf(hevent.getCurrentState()));
    	event.getEventDetails().setPreviousState(State.valueOf(hevent.getPreviousState()));
    	event.setDataElement(new HashSet<EventData>());
    	event.setCorrelations(new HashSet<EventCorrelation>());
    	event.setPayload(new HashSet<EventPayload>());
    	return event;
    }
    /**
     * Convert event business domain object to entity object
     * @param event business domain object to convert.
     * @return entity object.
     */
    public HEvent toEntity(Event event) {
    	HEvent hevent = new HEvent();
    	hevent.setEventID(event.getEventID());
    	hevent.setTimestamp(event.getTimestamp().getTime());
    	hevent.setProcessInstance(event.getProcessInstance().getId());
    	hevent.setCurrentState(event.getEventDetails().getCurrentState().name());  	
    	if (event.getActivityInstance() != null)    
    		hevent.setActivityInstance(event.getActivityInstance().getId());
    	if (event.getEventDetails().getPreviousState() != null)
    		hevent.setPreviousState(event.getEventDetails().getPreviousState().name());    	
    	return hevent;
    }
    /**
     * Convert process instance entity object to business domain object
     * @param hprocessInstance entity object to convert.
     * @return domain object.
     */
    public ProcessInstance toBusinessObject(HProcessInstance hprocessInstance) {
    	ProcessInstance processInstance = new ProcessInstance();
    	processInstance.setId(hprocessInstance.getId());
    	processInstance.setName(hprocessInstance.getName());
    	processInstance.setDescription(hprocessInstance.getDescription());
    	processInstance.setInstanceSrcId(hprocessInstance.getInstanceSrcId());
    	processInstance.setCorrelatorId(hprocessInstance.getCorrelatorId());
    	return processInstance;
    }
    /**
     * Convert process instance business domain object to entity object
     * @param processInstance business domain object to convert.
     * @return entity object.
     */
    public HProcessInstance toEntity(ProcessInstance processInstance) {
    	HProcessInstance hprocessInstance = new HProcessInstance();
    	hprocessInstance.setId((processInstance.getId() == null)? 0 : processInstance.getId());
    	hprocessInstance.setName(processInstance.getName());
    	hprocessInstance.setDescription(processInstance.getDescription());
    	hprocessInstance.setModel(processInstance.getModel().getId());
    	hprocessInstance.setInstanceSrcId(processInstance.getInstanceSrcId());
    	hprocessInstance.setCorrelatorId((processInstance.getCorrelatorId() == null)? 0 : processInstance.getCorrelatorId());
    	return hprocessInstance;
    }    
    /**
     * Convert activity instance entity object to business domain object
     * @param hactivityInstance entity object to convert.
     * @return domain object.
     */
    public ActivityInstance toBusinessObject(HActivityInstance hactivityInstance) {
    	ActivityInstance activityInstance = new ActivityInstance();
    	activityInstance.setId(hactivityInstance.getId());
    	activityInstance.setName(hactivityInstance.getName());
    	activityInstance.setDescription(hactivityInstance.getDescription());
    	activityInstance.setInstanceSrcId(hactivityInstance.getInstanceSrcId());
    	return activityInstance;
    }
    /**
     * Convert activity instance business domain object to entity object
     * @param activityInstance business domain object to convert.
     * @return entity object.
     */
    public HActivityInstance toEntity(ActivityInstance activityInstance) {
    	HActivityInstance hactivityInstance = new HActivityInstance();
    	hactivityInstance.setId((activityInstance.getId() == null)? 0 : activityInstance.getId());
    	hactivityInstance.setName(activityInstance.getName());
    	hactivityInstance.setDescription(activityInstance.getDescription());
    	hactivityInstance.setModel(activityInstance.getModel().getId());
    	hactivityInstance.setInstanceSrcId(activityInstance.getInstanceSrcId());
    	if (activityInstance.getParent() != null)
    		hactivityInstance.setParent((activityInstance.getParent().getId() == null)? 0 : activityInstance.getParent().getId());
    	return hactivityInstance;
    }  
    /**
     * Convert model entity object to business domain object
     * @param hmodel entity object to convert.
     * @return domain object.
     */
    public Model toBusinessObject(HModel hmodel) {
    	ModelType type = ModelType.valueOf(hmodel.getType());
    	Model model = null;
    	if (type == ModelType.PROCESS) {
    		model = new ProcessModel();
    	} else if (type == ModelType.ACTIVITY) {
    		model = new ActivityModel();
    	} else {
    		throw new IllegalArgumentException("Model type " + type + " not suported.");
    	}    	
    	model.setId(hmodel.getId());
    	model.setName(hmodel.getName());
    	model.setDescription(hmodel.getDescription());
    	model.setModelSrcId(hmodel.getModelSrcId());
    	model.setType(type);
    	return model;
    }
    /**
     * Convert model business domain object to entity object
     * @param model business domain object to convert.
     * @return entity object.
     */
    public HModel toEntity(Model model) {
    	HModel hmodel = new HModel();
    	hmodel.setId((model.getId() == null)? 0 : model.getId());
    	hmodel.setName(model.getName());
    	hmodel.setDescription(model.getDescription());
    	hmodel.setType(model.getType().name());
    	hmodel.setModelSrcId(model.getModelSrcId());
    	hmodel.setSource(model.getSource().getId());
    	return hmodel;
    }  
}