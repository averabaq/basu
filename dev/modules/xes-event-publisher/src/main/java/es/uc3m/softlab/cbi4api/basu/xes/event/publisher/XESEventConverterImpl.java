/* 
 * $Id: EventConverterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.TimeStampAdapter;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Correlation;
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
		for (Object object : xes.getStringOrDateOrInt()) {
			if (object instanceof AttributeStringType) {
				AttributeStringType attr = (AttributeStringType)object;	
				logger.debug("ROOT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeDateType) {
				AttributeDateType attr = (AttributeDateType)object;	
				logger.debug("ROOT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeIntType) {
				AttributeIntType attr = (AttributeIntType)object;	
				logger.debug("ROOT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeFloatType) {
				AttributeFloatType attr = (AttributeFloatType)object;	
				logger.debug("ROOT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
			} else if (object instanceof AttributeBooleanType) {
				AttributeBooleanType attr = (AttributeBooleanType)object;	
				logger.debug("ROOT: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
			}				
		}	
		for (TraceType trace : xes.getTrace()) {
			// process instance level
			Event event = new Event();	
			event.setEventID(UUID.randomUUID().toString());
			event.setServerID(config.getSourceId());
			for (Object object : trace.getStringOrDateOrInt()) {				
				processXESTraceAttribute(xesMap, object, event);
			}
			events.add(event);
			String processName = event.getProcessName();
			String processInstanceID = event.getProcessInstanceID();
			String processDefinitionID = event.getProcessDefinitionID();
			// activity instance level
			event = new Event();
			event.setEventID(UUID.randomUUID().toString());
			event.setServerID(config.getSourceId());
			event.setProcessName(processName);
			event.setProcessInstanceID(processInstanceID);
			event.setProcessDefinitionID(processDefinitionID);
			for (EventType eventType : trace.getEvent()) {
				for (Object object : eventType.getStringOrDateOrInt()) {
					processXESEventAttribute(xesMap, object, event);			
				}
			}
			events.add(event);
		}
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
    	for (ExtensionType ext : xes.getExtension()) {
    		logger.debug("EXTENSION ( " + ext.getName() + ", " + ext.getPrefix() + ", " + ext.getUri() + ")");
    	}	
    	
    	
    	
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Event.class);
		
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	for (Event _event : events) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  
    		marshaller.marshal(_event, writer);
    		writer.close();
    		baos.close();
    		final byte[] xml = baos.toByteArray();
			logger.error(new String(xml));
		}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return events;		
    }  
    /**
     * Process XES trace attributes to convert them into BPAF event object model.
     * @param object XES object attribute.
     * @param event BPAF event model object converted.
     */
    private void processXESTraceAttribute(XESMap xesMap, Object object, Event event) {
		Map<String,Set<BPAFMapType>> attrMap = xesMap.getSemantic().get(XESMapType.TRACE);
		Set<String> correlationSet = xesMap.getCorrelation().get(XESMapType.TRACE);
		Set<String> payloadSet = xesMap.getPayload().get(XESMapType.TRACE);
		Map<String, BPAFState> stateMap = xesMap.getState();
		
    	if (object instanceof AttributeStringType) {
			AttributeStringType attr = (AttributeStringType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (xesMap.getTransitionKey().equals(attr.getKey())) {					
					if (!event.isSetEventDetails()) {
						event.setEventDetails(new EventDetails());
					}
					State currentState = null;
					if (stateMap.containsKey(attr.getValue().toUpperCase())) {
						try {
							currentState = State.fromValue(stateMap.get(attr.getValue().toUpperCase()).value());
						} catch (IllegalArgumentException iaex) {
							logger.error("BPAF State " + stateMap.get(attr.getValue().toUpperCase()).value() + " does not exist. Please review XES map descriptor file. " + iaex.getMessage());
						}
					}
					event.getEventDetails().setCurrentState(currentState);
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.getValue());
				}
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeDateType) {
			AttributeDateType attr = (AttributeDateType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
					DataElement element = new DataElement();
					element.setKey(attr.getKey());
					TimeStampAdapter adapter = new TimeStampAdapter();
					element.setValue(adapter.marshal(attr.getValue()));
					event.getDataElement().add(element);
				}
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeIntType) {
			AttributeIntType attr = (AttributeIntType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				}  else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.getValue());
				}
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeFloatType) {
			AttributeFloatType attr = (AttributeFloatType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.getValue());
				}
			}					
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeBooleanType) {
			AttributeBooleanType attr = (AttributeBooleanType)object;	
			if (attr.isSetKey()) {		
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.isValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.isValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.isValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.isValue());
				}
			}
			logger.debug("TRACE: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
		}				
    }
    /**
     * Process XES event attributes to convert them into BPAF event object model.
     * @param object XES object attribute.
     * @param event BPAF event model object converted.
     */
    private void processXESEventAttribute(XESMap xesMap, Object object, Event event) {
		Map<String,Set<BPAFMapType>> attrMap = xesMap.getSemantic().get(XESMapType.EVENT);
		Set<String> correlationSet = xesMap.getCorrelation().get(XESMapType.EVENT);
		Set<String> payloadSet = xesMap.getPayload().get(XESMapType.EVENT);
		Map<String, BPAFState> stateMap = xesMap.getState();
		if (object instanceof AttributeStringType) {
			AttributeStringType attr = (AttributeStringType)object;	
			if (attr.isSetKey()) {				
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (xesMap.getTransitionKey().equals(attr.getKey())) {					
					if (!event.isSetEventDetails()) {
						event.setEventDetails(new EventDetails());
					}
					State currentState = null;
					if (stateMap.containsKey(attr.getValue().toUpperCase())) {
						try {
							currentState = State.fromValue(stateMap.get(attr.getValue().toUpperCase()).value());
						} catch (IllegalArgumentException iaex) {
							logger.error("BPAF State " + stateMap.get(attr.getValue().toUpperCase()).value() + " does not exist. Please review XES map descriptor file. " + iaex.getMessage());
						}
					}
					event.getEventDetails().setCurrentState(currentState);
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.getValue());
				}
			}
			logger.debug("EVENT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeDateType) {
			AttributeDateType attr = (AttributeDateType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
					DataElement element = new DataElement();
					element.setKey(attr.getKey());
					TimeStampAdapter adapter = new TimeStampAdapter();
					element.setValue(adapter.marshal(attr.getValue()));
					event.getDataElement().add(element);
				}
			}
			logger.debug("EVENT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeIntType) {
			AttributeIntType attr = (AttributeIntType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.getValue());
				}
			}
			logger.debug("EVENT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeFloatType) {
			AttributeFloatType attr = (AttributeFloatType)object;	
			if (attr.isSetKey()) {
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.getValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.getValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.getValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.getValue());
				}
			}					
			logger.debug("EVENT: ( " + attr.getKey() + ", " + attr.getValue() + ")");
		} else if (object instanceof AttributeBooleanType) {
			AttributeBooleanType attr = (AttributeBooleanType)object;
			if (attr.isSetKey()) {						
				if (attrMap.containsKey(attr.getKey())) {
					// converts attributes
					setEventAttribute(event, attrMap.get(attr.getKey()), attr.isValue());
				} else if (correlationSet != null && correlationSet.contains(attr.getKey())) { 
			    	// converts correlation
			    	setEventCorrelation(event, attr.getKey(), attr.isValue());		    	
			    } else if (payloadSet != null && payloadSet.contains(attr.getKey())) { 
			    	// converts payload
			    	setEventPayload(event, attr.getKey(), attr.isValue());			    	
			    } else {
			    	// converts data element
					setEventDataElement(event, attr.getKey(), attr.isValue());
				}
			}					
			logger.debug("EVENT: ( " + attr.getKey() + ", " + attr.isValue() + ")");											
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
    			case PROCESS_INSTANCE_ID: event.setProcessInstanceID((String)value); break;
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
		if (event.getCorrelation() == null) {
			event.setCorrelation(new Correlation());    				
		}
		CorrelationElement correlation = new CorrelationElement();
		correlation.setKey(key);
		correlation.setValue(String.valueOf(value));
		event.getCorrelation().getCorrelationElement().add(correlation);   	
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
		element.setValue(String.valueOf(value));
		event.getDataElement().add(element);  	
    } 
	/**
	 * Get the XES map structure.
	 * @return the XES map structure.
	 * @throws XESException if any XES map configuration error is found.
	 */
	private XESMap getMap() throws XESException {
		try {
			byte[] xml = IOUtils.toByteArray(getClass().getResourceAsStream(StaticResources.XES_MAP_XML_CLASSPATH_FILE));
			ByteArrayInputStream bais = new ByteArrayInputStream(xml);
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
		} catch(IOException ioex) {
			logger.error(ioex.fillInStackTrace());
			throw new XESException(ioex);
		}
	}
}
