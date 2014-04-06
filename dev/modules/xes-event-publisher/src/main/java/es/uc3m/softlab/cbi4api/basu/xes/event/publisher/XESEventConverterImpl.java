/* 
 * $Id: EventConverterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.XESMap.BPAFStateXesMap;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.TimeStampAdapter;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Correlation;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.CorrelationData;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.CorrelationElement;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.DataElement;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event.EventDetails;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Payload;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.State;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.AttributeBooleanType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.AttributeDateType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.AttributeFloatType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.AttributeIntType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.AttributeStringType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.EventType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.ExtensionType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.GlobalsType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.LogType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.TraceType;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map.BPAFState;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map.XesMap;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * Component implementation for converting the incoming events in XES format 
 * into BPAF extended format.
 * 
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from an extended BPAF format and transforming its content 
 * into an BPAF entity object model.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=XESEventConverter.COMPONENT_NAME)
public class XESEventConverterImpl implements XESEventConverter {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(XESEventConverterImpl.class);
    /** Configuration service object */
	@Autowired private Config config;
    
    /**
     * Convert the input XES event contents into BPAF extension format.
     * 
     * @param event XES event.
     * @return events in BPAF extension format.
     * @throws XESException if any XES map configuration error is found.
     */
    public List<Event> transform(LogType xes) throws XESException {
		logger.debug("Transforming XES log event: " + xes.getXesFeatures());
		// gets the xes map conversion structure
		XESMap xesMap = getMap();
    	// creates a new list of events 
    	List<Event> events = new ArrayList<Event>();
    	// process traces (global processes)
		for (TraceType trace : xes.getTrace()) {
			// process instance level
			Event event = new Event();	
			event.setCorrelation(new Correlation());
			event.setEventID(UUID.randomUUID().toString());
			event.setServerID(config.getSourceId());		
			for (Object object : trace.getStringOrDateOrInt()) {				
				processXES(XESMapType.TRACE, xesMap, object, event);
			}
			// add the event to the returning list
			events.add(event);
			
			// save process data for being set in subprocesses (activities)
			String processName = event.getProcessName();
			String processInstanceID = event.getCorrelation().getProcessInstanceID();
			String processDefinitionID = event.getProcessDefinitionID();			
			// activity instance level
			for (EventType eventType : trace.getEvent()) {	
				event = new Event();	
				event.setCorrelation(new Correlation());
				event.setEventID(UUID.randomUUID().toString());
				event.setServerID(config.getSourceId());
				event.setCorrelation(new Correlation());
				event.getCorrelation().setProcessInstanceID(processInstanceID);
				event.setProcessName(processName);				
				event.setProcessDefinitionID(processDefinitionID);				
				for (Object object : eventType.getStringOrDateOrInt()) {
					processXES(XESMapType.EVENT, xesMap, object, event);			
				}				
				events.add(event);
			}			
		}
		// log xes info
		logXESInfo(xes);
		return events;		
    }
    /**
     * Process XES attributes to convert them into BPAF event object model.
     * @param type XES map type (TRACE/EVENT).
     * @param xesMap XES mapping convertor.
     * @param object XES object attribute.
     * @param event BPAF event model object converted.
     */
    private void processXES(XESMapType type, XESMap xesMap, Object object, Event event) {
		Map<String,Set<BPAFMapType>> attrMap = xesMap.getSemantic().get(type);
		Set<String> correlationSet = xesMap.getCorrelation().get(type);
		Set<String> payloadSet = xesMap.getPayload().get(type);
		Map<String, BPAFStateXesMap> stateMap = xesMap.getState();
		
    	if (object instanceof AttributeStringType) {
			AttributeStringType attr = (AttributeStringType)object;	
			if (attr.isSetKey()) {
				processXESAttribute(xesMap, attrMap, correlationSet, payloadSet, stateMap, event, attr.getKey(), attr.getValue());
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeDateType) {
			AttributeDateType attr = (AttributeDateType)object;	
			if (attr.isSetKey()) {
				processXESAttribute(xesMap, attrMap, correlationSet, payloadSet, stateMap, event, attr.getKey(), attr.getValue());
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeIntType) {
			AttributeIntType attr = (AttributeIntType)object;	
			if (attr.isSetKey()) {
				processXESAttribute(xesMap, attrMap, correlationSet, payloadSet, stateMap, event, attr.getKey(), attr.getValue());
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeFloatType) {
			AttributeFloatType attr = (AttributeFloatType)object;	
			if (attr.isSetKey()) {
				processXESAttribute(xesMap, attrMap, correlationSet, payloadSet, stateMap, event, attr.getKey(), attr.getValue());
			}					
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeBooleanType) {
			AttributeBooleanType attr = (AttributeBooleanType)object;	
			if (attr.isSetKey()) {		
				processXESAttribute(xesMap, attrMap, correlationSet, payloadSet, stateMap, event, attr.getKey(), attr.isValue());
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
		}				
    }
    /**
     * Process the XES attributes received from the XES input data.
     * @param xesMap XES mapping convertor structure.
     * @param attrMap attribute mapping convertor.
     * @param correlationSet correlation mapping set. 
     * @param payloadSet payload mapping set.
     * @param stateMap state transition mapping.
     * @param event BPAF event converted.
     * @param key XES key attribute.
     * @param value XES value attribute.
     */
    private void processXESAttribute(XESMap xesMap, Map<String,Set<BPAFMapType>> attrMap, Set<String> correlationSet, Set<String> payloadSet, Map<String, BPAFStateXesMap> stateMap, Event event, String key, Object value) {
		if (attrMap.containsKey(key)) {
			// converts attributes
			setEventAttribute(event, attrMap.get(key), value);
		} else if (xesMap.getTransitionKey().equals(key)) {					
			if (!event.isSetEventDetails()) {
				event.setEventDetails(new EventDetails());
			}
			State currentState = null;
			State previousState = null;
			if (stateMap.containsKey(String.valueOf(value).toUpperCase())) {
				BPAFState sourceState = stateMap.get((String.valueOf(value)).toUpperCase()).getSource();
				try {					
					if (sourceState != null)
						previousState = State.fromValue(sourceState.value());
				} catch (IllegalArgumentException iaex) {
					logger.error("BPAF State " + sourceState.value() + " does not exist. Please review XES map descriptor file. " + iaex.getMessage());
				}
				BPAFState targetState = stateMap.get((String.valueOf(value)).toUpperCase()).getTarget();
				try {					
					if (targetState != null)
						currentState = State.fromValue(targetState.value());
				} catch (IllegalArgumentException iaex) {
					logger.error("BPAF State " + targetState.value() + " does not exist. Please review XES map descriptor file. " + iaex.getMessage());
				}
			}
			event.getEventDetails().setPreviousState(previousState);
			event.getEventDetails().setCurrentState(currentState);
		} else if (correlationSet != null && correlationSet.contains(key)) { 
	    	// converts correlation
	    	setEventCorrelation(event, key, value);		    	
	    } else if (payloadSet != null && payloadSet.contains(key)) { 
	    	// converts payload
	    	setEventPayload(event, key, value);			    	
	    } else {
	    	// converts data element
			setEventDataElement(event, key, value);
		}
    }    
    /**
     * Sets the event attributes to a particular event
     * @param event event to assign the attributes values to
     * @param type BPAF map event type
     * @param value attribute value
     */
    private void setEventAttribute(Event event, Set<BPAFMapType> types, Object value) {
    	for (BPAFMapType type : types) {
    		switch(type) {
    			case EVENT_ID: event.setEventID((String)value); break;
    			case SERVER_ID: event.setServerID((String)value); break;
    			case PROCESS_DEFINITION_ID: event.setProcessDefinitionID((String)value); break;
    			case PROCESS_INSTANCE_ID: event.getCorrelation().setProcessInstanceID((String)value); break;
    			case PROCESS_NAME: event.setProcessName((String)value); break;
    			case ACTIVITY_DEFINITION_ID: event.setActivityDefinitionID((String)value); break;
    			case ACTIVITY_INSTANCE_ID: event.setActivityInstanceID((String)value); break;
    			case ACTIVITY_NAME: event.setActivityName((String)value); break;
    			case TIMESTAMP: event.setTimestamp((Calendar)value); break;
    			default: logger.warn("Wrong BPAF map type " + type + ". This attribute type does not exist."); break;    			
    		}
    	}
    } 
    /**
     * Sets the event correlation attributes to a particular event
     * @param event event to assign the correlation attributes values to
     * @param key attribute key name
     * @param value attribute value
     */
    private void setEventCorrelation(Event event, String key, Object value) {
		if (!event.isSetCorrelation()) {
			event.setCorrelation(new Correlation());    				
		}
    	if (!event.getCorrelation().isSetCorrelationData()) {
			event.getCorrelation().setCorrelationData(new CorrelationData());    				
		}
		CorrelationElement correlation = new CorrelationElement();
		correlation.setKey(key);
		correlation.setValue(String.valueOf(value));
		event.getCorrelation().getCorrelationData().getCorrelationElement().add(correlation);   	
    } 
    /**
     * Sets the event payload attributes to a particular event
     * @param event event to assign the payload attributes values to
     * @param key attribute key name
     * @param value attribute value
     */
    private void setEventPayload(Event event, String key, Object value) {
    	Payload payload = new Payload();
    	payload.setKey(key);
    	payload.setValue(String.valueOf(value));
    	event.getPayload().add(payload);    	
    } 
    /**
     * Sets the event data elements to a particular event
     * @param event event to assign the data elements values to
     * @param key attribute key name
     * @param value attribute value
     */
    private void setEventDataElement(Event event, String key, Object value) {
    	DataElement element = new DataElement();
		element.setKey(key);
		if (value instanceof Calendar) {
			TimeStampAdapter adapter = new TimeStampAdapter();
			element.setValue(adapter.marshal((Calendar)value));
		} else {
			element.setValue(String.valueOf(value));			
		}
		event.getDataElement().add(element);
    } 
	/**
	 * Get the XES map structure.
	 * @return the XES map structure.
	 * @throws XESException if any XES map configuration error is found.
	 */
	private XESMap getMap() throws XESException {
		ByteArrayInputStream bais = null;
		try {
			byte[] xml = IOUtils.toByteArray(getClass().getResourceAsStream(StaticResources.XES_MAP_XML_CLASSPATH_FILE));
			bais = new ByteArrayInputStream(xml);
			/* create a JAXBContext capable of handling classes */ 
			JAXBContext jc = JAXBContext.newInstance("es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map");	
			/* create an Unmarshaller */
			Unmarshaller u = jc.createUnmarshaller();
			/* Performs an xml validation against the OpenXES schema */
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL xsd = getClass().getResource(StaticResources.XES_MAP_XML_SCHEMA_CLASSPATH_FILE);
			Schema schema = factory.newSchema(xsd);
			u.setSchema(schema);		
			/* unmarshal an instance document into a tree of Java content objects. */			
			XesMap map = (XesMap) u.unmarshal(bais);			
			logger.debug("Unmarshalled XES file sucessfully: " + map);
			XESMap xesmap = XESMapFactory.getInstance().createXesMap(map);
			return xesmap;
		} catch (JAXBException jaxb) {
			logger.error(jaxb.fillInStackTrace());
			throw new XESException(jaxb);
		} catch (SAXException sex) {
			logger.error(sex.fillInStackTrace());
			throw new XESException(sex);
		} catch (IOException ioex) {
			logger.error(ioex.fillInStackTrace());
			throw new XESException(ioex);
		} finally {
			if (bais != null) {			
				try {
					bais.close();
				} catch (IOException ioex) {
					logger.error(ioex.fillInStackTrace());
				}
			}
		}
	}
	/**
	 * Log specific XES info set within the head of the XES xml data file
	 * @param xes XES data
	 */
	private void logXESInfo(LogType xes) {
		// log principal XES attributes
		logger.debug("=================== PRINCIPALS ===================");
		for (Object object : xes.getStringOrDateOrInt()) {
			if (object instanceof AttributeStringType) {
				AttributeStringType attr = (AttributeStringType)object;	
				logger.debug("PRINCIPAL: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeDateType) {
				AttributeDateType attr = (AttributeDateType)object;	
				logger.debug("PRINCIPAL: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeIntType) {
				AttributeIntType attr = (AttributeIntType)object;	
				logger.debug("PRINCIPAL: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeFloatType) {
				AttributeFloatType attr = (AttributeFloatType)object;	
				logger.debug("PRINCIPAL: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeBooleanType) {
				AttributeBooleanType attr = (AttributeBooleanType)object;	
				logger.debug("PRINCIPAL: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
			}				
		}	
		// log global XES declarations
		logger.debug("=================== GLOBALS ===================");
    	for (GlobalsType global : xes.getGlobal()) {
        	for (Object object : global.getStringOrDateOrInt()) {
        		if (object instanceof AttributeStringType) {
    				AttributeStringType attr = (AttributeStringType)object;	
    				logger.debug("GLOBALS: ( " + attr.getKey() + ", " + attr.getValue() + ")");
    			} else if (object instanceof AttributeDateType) {
    				AttributeDateType attr = (AttributeDateType)object;	
    				logger.debug("GLOBALS: ( " + attr.getKey() + ", " + attr.getValue() + ")");
    			} else if (object instanceof AttributeIntType) {
    				AttributeIntType attr = (AttributeIntType)object;	
    				logger.debug("GLOBALS: ( " + attr.getKey() + ", " + attr.getValue() + ")");
    			} else if (object instanceof AttributeFloatType) {
    				AttributeFloatType attr = (AttributeFloatType)object;	
    				logger.debug("GLOBALS: ( " + attr.getKey() + ", " + attr.getValue() + ")");
    			} else if (object instanceof AttributeBooleanType) {
    				AttributeBooleanType attr = (AttributeBooleanType)object;	
    				logger.debug("GLOBALS: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
    			} else {
    				logger.debug("GLOBALS: ( " + object.getClass().getName() + ")");	
    			}
        	}
    	}	
    	// log XES classifiers
    	logger.debug("=================== CLASSIFIERS ===================");
    	for (Object object : xes.getClassifier()) {
    		if (object instanceof AttributeStringType) {
				AttributeStringType attr = (AttributeStringType)object;	
				logger.debug("CLASSIFIER: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeDateType) {
				AttributeDateType attr = (AttributeDateType)object;	
				logger.debug("CLASSIFIER: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeIntType) {
				AttributeIntType attr = (AttributeIntType)object;	
				logger.debug("CLASSIFIER: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeFloatType) {
				AttributeFloatType attr = (AttributeFloatType)object;	
				logger.debug("CLASSIFIER: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeBooleanType) {
				AttributeBooleanType attr = (AttributeBooleanType)object;	
				logger.debug("CLASSIFIER: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
			} else {			
				logger.debug("CLASSIFIER: ( " + object.getClass().getName() + ")");	
			}
    	}
    	// log XES classifiers
    	logger.debug("=================== EXTENSIONS ===================");
    	for (ExtensionType ext : xes.getExtension()) {
    		logger.debug("EXTENSION: ( " + ext.getName() + ", " + ext.getPrefix() + ", " + ext.getUri() + ")");
    	}	    			
	}
}
