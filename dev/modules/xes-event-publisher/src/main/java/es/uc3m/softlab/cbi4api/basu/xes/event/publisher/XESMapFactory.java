/* 
 * $Id: XESMapFactory.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map.BPAFState;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map.StateTransitions;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map.XesMap;

/**
 * Factory design pattern for creating map conversion objects
 * for handling XES conversion formats.
 * 
 * @author averab
 */
public class XESMapFactory {
    /** Instance of this singleton class */
    private static XESMapFactory instance = null;
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(XESMapFactory.class);  

    /**
     * Constructor of this singleton class.     
     */
    private XESMapFactory() {    
    }
    /**
     * Initializes this singleton class and creates a new instance
     * if it's already not.
     * 
     * @return instance of this singleton class.
     */
    public static XESMapFactory init() {
       	if (instance == null)
    		instance = new XESMapFactory();
    	return instance;    	
    }
    /**
     * Gets the instance of this singleton class.
     * 
     * @return instance of this singleton class.
     */
    public static XESMapFactory getInstance() {
       	if (instance == null)
    		instance = init();
    	return instance;
    }
    /**
     * Creates a XES map structure for format conversion
     * @param xesmap xml map information
     * @return XES map structure
     * @throws XESException if any XES map configuration error is found.
     */
    public XESMap createXesMap(XesMap xesmap) throws XESException {
    	logger.debug("Generating XES map structure...");
    	XESMap _xesmap = new XESMap(); 
    	_xesmap.setSemantic(new HashMap<XESMapType, Map<String,Set<BPAFMapType>>>());
    	_xesmap.setPayload(new HashMap<XESMapType, Set<String>>());
    	_xesmap.setCorrelation(new HashMap<XESMapType, Set<String>>());
    	// maps traces
    	if (xesmap.isSetTrace()) {
    		HashMap<String, Set<BPAFMapType>> traceMap = new HashMap<String, Set<BPAFMapType>>();		
    		if (xesmap.getTrace().isSetEventID()) {    		
    			if (!traceMap.containsKey(xesmap.getTrace().getEventID())) {
    				traceMap.put(xesmap.getTrace().getEventID(), new HashSet<BPAFMapType>());
    			}
    			traceMap.get(xesmap.getTrace().getEventID()).add(BPAFMapType.EVENT_ID);
    		}
    		if (xesmap.getTrace().isSetServerID()) {  
    			if (!traceMap.containsKey(xesmap.getTrace().getServerID())) {
    				traceMap.put(xesmap.getTrace().getServerID(), new HashSet<BPAFMapType>());
    			}
    			traceMap.get(xesmap.getTrace().getServerID()).add(BPAFMapType.SERVER_ID);
    		}
    		if (xesmap.getTrace().isSetProcessDefinitionID()) { 
    			if (!traceMap.containsKey(xesmap.getTrace().getProcessDefinitionID())) {
    				traceMap.put(xesmap.getTrace().getProcessDefinitionID(), new HashSet<BPAFMapType>());
    			}
    			traceMap.get(xesmap.getTrace().getProcessDefinitionID()).add(BPAFMapType.PROCESS_DEFINITION_ID);
    		}
    		if (xesmap.getTrace().isSetProcessInstanceID()) {  
    			if (!traceMap.containsKey(xesmap.getTrace().getProcessInstanceID())) {
    				traceMap.put(xesmap.getTrace().getProcessInstanceID(), new HashSet<BPAFMapType>());
    			}
    			traceMap.get(xesmap.getTrace().getProcessInstanceID()).add(BPAFMapType.PROCESS_INSTANCE_ID);
    		}
    		if (xesmap.getTrace().isSetProcessName()) {   
    			if (!traceMap.containsKey(xesmap.getTrace().getProcessName())) {
    				traceMap.put(xesmap.getTrace().getProcessName(), new HashSet<BPAFMapType>());
    			}
    			traceMap.get(xesmap.getTrace().getProcessName()).add(BPAFMapType.PROCESS_NAME);
    		}
    		if (xesmap.getTrace().isSetTimestamp()) { 
    			if (!traceMap.containsKey(xesmap.getTrace().getTimestamp())) {
    				traceMap.put(xesmap.getTrace().getTimestamp(), new HashSet<BPAFMapType>());
    			}
    			traceMap.get(xesmap.getTrace().getTimestamp()).add(BPAFMapType.TIMESTAMP);
    		}
    		if (xesmap.getTrace().isSetCorrelation()) {      			
    			for (String correlationKey : xesmap.getTrace().getCorrelation()) {
    				if (!_xesmap.getCorrelation().containsKey(XESMapType.TRACE)) {    				
    					_xesmap.getCorrelation().put(XESMapType.TRACE, new HashSet<String>());	
					}    
    				_xesmap.getCorrelation().get(XESMapType.TRACE).add(correlationKey);
    			}    			
    		}
    		if (xesmap.getTrace().isSetPayload()) {      			
    			for (String payloadKey : xesmap.getTrace().getPayload()) {
    				if (!_xesmap.getPayload().containsKey(XESMapType.TRACE)) {    				
    					_xesmap.getPayload().put(XESMapType.TRACE, new HashSet<String>());	
					}    
    				_xesmap.getPayload().get(XESMapType.TRACE).add(payloadKey);
    			}    			
    		}
    		_xesmap.getSemantic().put(XESMapType.TRACE, traceMap);	
    	}
    	// maps events
    	if (xesmap.isSetEvent()) {
    		HashMap<String, Set<BPAFMapType>> eventMap = new HashMap<String, Set<BPAFMapType>>();		
    		if (xesmap.getEvent().isSetEventID()) {    	
    			if (!eventMap.containsKey(xesmap.getEvent().getEventID())) {
    				eventMap.put(xesmap.getEvent().getEventID(), new HashSet<BPAFMapType>());
    			}
    			eventMap.get(xesmap.getEvent().getEventID()).add(BPAFMapType.EVENT_ID);
    		}
    		if (xesmap.getEvent().isSetServerID()) {   
    			if (!eventMap.containsKey(xesmap.getEvent().getServerID())) {
    				eventMap.put(xesmap.getEvent().getServerID(), new HashSet<BPAFMapType>());
    			}
    			eventMap.get(xesmap.getEvent().getServerID()).add(BPAFMapType.SERVER_ID);
    		}
    		if (xesmap.getEvent().isSetActivityDefinitionID()) { 
    			if (!eventMap.containsKey(xesmap.getEvent().getActivityDefinitionID())) {
    				eventMap.put(xesmap.getEvent().getActivityDefinitionID(), new HashSet<BPAFMapType>());
    			}
    			eventMap.get(xesmap.getEvent().getActivityDefinitionID()).add(BPAFMapType.ACTIVITY_DEFINITION_ID);
    		}
    		if (xesmap.getEvent().isSetActivityInstanceID()) {    	
    			if (!eventMap.containsKey(xesmap.getEvent().getActivityInstanceID())) {
    				eventMap.put(xesmap.getEvent().getActivityInstanceID(), new HashSet<BPAFMapType>());
    			}
    			eventMap.get(xesmap.getEvent().getActivityInstanceID()).add(BPAFMapType.ACTIVITY_INSTANCE_ID);
    		}
    		if (xesmap.getEvent().isSetActivityName()) { 
    			if (!eventMap.containsKey(xesmap.getEvent().getActivityName())) {
    				eventMap.put(xesmap.getEvent().getActivityName(), new HashSet<BPAFMapType>());
    			}
    			eventMap.get(xesmap.getEvent().getActivityName()).add(BPAFMapType.ACTIVITY_NAME);
    		}
    		if (xesmap.getEvent().isSetTimestamp()) {   
    			if (!eventMap.containsKey(xesmap.getEvent().getTimestamp())) {
    				eventMap.put(xesmap.getEvent().getTimestamp(), new HashSet<BPAFMapType>());
    			}
    			eventMap.get(xesmap.getEvent().getTimestamp()).add(BPAFMapType.TIMESTAMP);
    		}
    		if (xesmap.getEvent().isSetCorrelation()) {  
    			_xesmap.setCorrelation(new HashMap<XESMapType, Set<String>>());
    			for (String correlationKey : xesmap.getEvent().getCorrelation()) {
    				if (!_xesmap.getCorrelation().containsKey(XESMapType.EVENT)) {    				
    					_xesmap.getCorrelation().put(XESMapType.EVENT, new HashSet<String>());	
					}    
    				_xesmap.getCorrelation().get(XESMapType.EVENT).add(correlationKey);
    			}    			
    		}
    		if (xesmap.getEvent().isSetPayload()) {  
    			_xesmap.setPayload(new HashMap<XESMapType, Set<String>>());
    			for (String payloadKey : xesmap.getEvent().getPayload()) {
    				if (!_xesmap.getPayload().containsKey(XESMapType.EVENT)) {    				
    					_xesmap.getPayload().put(XESMapType.EVENT, new HashSet<String>());	
					}    
    				_xesmap.getPayload().get(XESMapType.EVENT).add(payloadKey);
    			}    			
    		}
    		_xesmap.getSemantic().put(XESMapType.EVENT, eventMap);	
    	}
    	// maps state transitions
    	if (xesmap.isSetStateTransitions()) {
    		_xesmap.setTransitionKey(xesmap.getStateTransitions().getKey());
    		_xesmap.setState(new HashMap<String, BPAFState>());
    		for (StateTransitions.State state : xesmap.getStateTransitions().getState()) {
    			String xesState = null;
    			BPAFState bpafState = null;
    			if (state.isSetXes())
    				xesState = state.getXes();
    			if (state.isSetBpaf())
    				bpafState = state.getBpaf();
     			if (bpafState == null) {
    				logger.warn("BPAF state transition '" + state.getBpaf() + "' is not a valid BPAF state. Please, review the XES xml map configuration.");
    				throw new XESException("BPAF state transition '" + state.getBpaf() + "' is not a valid BPAF state. Please, review the XES xml map configuration."); 
     			}
     			_xesmap.getState().put(xesState, bpafState);
			} 
    	}
    	logger.debug("XES map structure generated successfully.");
    	return _xesmap;   	
    }
}