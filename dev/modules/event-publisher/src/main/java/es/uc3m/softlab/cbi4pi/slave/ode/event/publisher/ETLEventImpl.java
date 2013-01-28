/* 
 * $Id: ETLEventImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.ode.event.publisher;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jms.JMSException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.ode.dao.jpa.EventDAOImpl;
import org.apache.ode.dao.jpa.ScopeDAOImpl;
import org.apache.ode.dao.jpa.XmlDataDAOImpl;
import org.apache.ode.utils.DOMUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.Config;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.EventPublisherException;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.EventReader;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.EventReaderException;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.EventSender;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.ODEEventState;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.extension.DataElement;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.extension.Event;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.extension.Payload;
import es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.extension.State;

/**
 * Specific ETL (Extract, Transform & Load) service implementation for handling
 * the ApacheODE events.
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition)</a> by
 * extracting the event data from BPEL engines, transforming its contents into
 * this format, and publish the results into a JMS queue.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional
@Service(value=ETLEvent.COMPONENT_NAME)
public class ETLEventImpl implements ETLEvent {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(ETLEventImpl.class);
    /** Configuration object */
    @Autowired private Config config;	
	/** BPEL JPA transaction manager */
	@Autowired private TransactionManager transactionManager;
	/** Event reader to extract the event data from the local store */
	@Autowired private EventReader extractor;
	/** Event sender to publish the data to the jms queue */
	@Autowired private EventSender loader;
	
	/**
	 * Process all events by executing the ETL engine.
	 * @throws EventPublisherException if any error occurred during 
	 * the ETL execution process.
	 */
	public void process() throws EventPublisherException {
		try {
			transactionManager.begin();
			/* extract events */
			List<EventDAOImpl> odeEvents = extractor.readNewEvents();
			/* transform events */
			List<Event> events = transform(odeEvents);
			/* load events */
			loader.publishEvents(events);
		} catch(SystemException sysex) {
			logger.error(sysex.fillInStackTrace());
			throw new EventPublisherException(sysex);			
		} catch(NotSupportedException nsex) {
			logger.error(nsex.fillInStackTrace());
			throw new EventPublisherException(nsex);
		} catch(EventReaderException evrex) {
			logger.error(evrex.fillInStackTrace());
			throw new EventPublisherException(evrex);
		} catch(JMSException jmsex) {
			logger.error(jmsex.fillInStackTrace());
			throw new EventPublisherException(jmsex);
		} finally {
			try {
				transactionManager.rollback();
			} catch(SystemException sysex) {
				logger.error(sysex.fillInStackTrace());
				throw new EventPublisherException(sysex);
			} 
		}
	}
	/**
	 * Converts a list of {@link org.apache.ode.dao.jpa.EventDAOImpl} event objects
	 * into a list of {@link es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.Event}
	 * event objects.
	 * 
	 * @param events list of <strong>ApacheODE</strong> events to transform.
	 * @return list of events transformed to the <strong>BPAF</strong> format.
	 * @throws EventReaderException if any reader error occurred during the transformation process.
	 */
	private List<Event> transform(List<EventDAOImpl> events) throws EventReaderException {
		List<Event> _events = new ArrayList<Event>();
		for (EventDAOImpl _event : events) {
			logger.debug("Reading Event (" + _event.getId() + ") [" + _event.getTstamp() + "]: (" + _event.getType() + ") - from process: " + _event.getInstance().getInstanceId() + ".");
			Event event = new Event(); 	
			Calendar tstamp = Calendar.getInstance(config.getLocale());
			tstamp.setTime(_event.getTstamp());
			event.setEventID(String.valueOf(_event.getId()));
			/* gets the server identification from a setting located at the property file */
			event.setServerID(config.getString("f4bpa.ode.event.publisher.event.source.id"));
			event.setProcessDefinitionID(String.valueOf(_event.getInstance().getProcess().getProcessId().getLocalPart()));
			event.setProcessInstanceID(String.valueOf(_event.getInstance().getInstanceId()));			
			event.setProcessName(_event.getInstance().getProcess().getType().getLocalPart());			 				 				
			event.setTimestamp(tstamp);		
			ScopeDAOImpl scope = extractor.readScope(_event.getScopeId());
			
			/* if the event refers to an activity */
			if (scope != null) {
				event.setActivityName(scope.getName());
				event.setActivityInstanceID(String.valueOf(scope.getScopeInstanceId()));				
				event.setActivityDefinitionID(scope.getName());
				if (scope.getParentScope() != null)
					event.setActivityParentID(String.valueOf(scope.getParentScope().getScopeInstanceId()));
			} 
			/* sets the previous states */
			event.getEventDetails().setCurrentState(transformState(_event.getType()));
			EventDAOImpl previousEvent = extractor.readPreviousEvent(_event);
			if (previousEvent != null) {
				event.getEventDetails().setPreviousState(transformState(previousEvent.getType()));
			}
			
			/* gets the payload xml data */
			if (scope != null && scope.getParentScope() != null) {
				for (Payload payload : transformPayload(scope.getParentScope().getScopeInstanceId())) {
					if (!event.getPayload().contains(payload)) {
						event.getPayload().add(payload);
					}
				} 
			}
			
			/* gets the extra event information */
			DataElement eventDetail = new DataElement();
			eventDetail.setKey("event.detail");
			eventDetail.setValue(_event.getDetail());
			event.getDataElement().add(eventDetail);
			
			_events.add(event);
			logger.debug("Transforming Event (" + _event.getId() + ") [" + _event.getTstamp() + "]: (" + _event.getType() + ") - from process: " + _event.getInstance().getProcess().getType() + ".");				
		}
		return _events;
	}	
	/**
	 * Transform the ApacheODE state to BPAF state format.
	 * @param state ApacheODE scope state in string format.
	 * @return BPAF state format associated.
	 */
	private State transformState(String state) {
		if (state == null)
			return null;
		/* converts to enum type value */
		StringBuffer buffer = new StringBuffer();
		for (Character character : state.toCharArray()) {
			if (Character.isUpperCase(character))
				buffer.append("_");
			buffer.append(Character.toUpperCase(character));
		}
		ODEEventState odeState = ODEEventState.valueOf(buffer.toString().substring(1));
		if (odeState == null)
			return null;
		switch (odeState) {
			case ACTIVITY_ENABLED_EVENT: return State.OPEN_NOT_RUNNING; 
			case ACTIVITY_EXEC_END_EVENT: return State.CLOSED_COMPLETED; 
			case ACTIVITY_EXEC_START_EVENT: return State.OPEN_RUNNING; 
			case ACTIVITY_FAILURE_EVENT: return State.CLOSED_COMPLETED_FAILED; 
			case ACTIVITY_RECOVERY_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case COMPENSATION_HANDLER_REGISTERED: return State.OPEN_RUNNING_IN_PROGRESS;
			case CORRELATION_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case CORRELATION_MATCH_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case CORRELATION_NO_MATCH_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case CORRELATION_SET_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case CORRELATION_SET_WRITE_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case EXPRESSION_EVALUATION_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case EXPRESSION_EVALUATION_FAILED_EVENT: return State.CLOSED_COMPLETED_FAILED;
			case EXPRESSION_EVALUATION_SUCCESS_EVENT: return State.CLOSED_COMPLETED_SUCCESS;
			case NEW_PROCESS_INSTANCE_EVENT: return State.OPEN_NOT_RUNNING;
			case PARTNER_LINK_MODIFICATION_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case PROCESS_COMPLETION_EVENT: return State.CLOSED_COMPLETED;
			case PROCESS_INSTANCE_STARTED_EVENT: return State.OPEN_RUNNING;
			case PROCESS_INSTANCE_STATE_CHANGE_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case PROCESS_MESSAGE_EXCHANGE_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case PROCESS_TERMINATION_EVENT: return State.CLOSED_CANCELLED_TERMINATED;
			case SCOPE_COMPLETION_EVENT: return State.CLOSED_COMPLETED;
			case SCOPE_FAULT_EVENT: return State.CLOSED_COMPLETED_FAILED;
			case SCOPE_START_EVENT: return State.OPEN_RUNNING;
			case VARIABLE_MODIFICATION_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			case VARIABLE_READ_EVENT: return State.OPEN_RUNNING_IN_PROGRESS;
			default: return null;
		}
	}		
	/**
	 * Transform the payload ApacheODE business information to a BPAF 
	 * extension format.
	 * 
	 * @see #processPayload(List, String, Node)
	 * @param scopeId scope identifier which payload is associated to.
	 * @return list of payload business data associated to the scope given by parameters.
	 * @throws EventReaderException if any reader error occurred during the transformation process.
	 */
	private List<Payload> transformPayload(Long scopeId) throws EventReaderException {		
		List<Payload> payload = new ArrayList<Payload>();
		if (scopeId == null)
			return payload;
		
		/* payload xml data */
		List<XmlDataDAOImpl> xmlData = extractor.readXmlData(scopeId);
		for (XmlDataDAOImpl xml : xmlData) {
			if (xml.get() == null)
				continue;
			if (xml.get().getNodeType() == Node.ELEMENT_NODE) {
				Node root = plainXmlDataFromNode(xml.get());
				NodeList list = root.getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {						
					Node _node = list.item(i);
					NodeList childs = _node.getChildNodes();
					for (int j = 0; j < childs.getLength(); j++) {						
						Node childNode = list.item(j);
						processPayload(payload, null, childNode);
					}
				}
			}
		}
		return payload;
	}	
	/**
	 * Removes the xml indentation blank spaces and new lines from the xml
	 * document included inside the node passed by arguments. 
	 * @param node node to get a plain format of its xml document associated.
	 * @return a plain format of the xml document associated to the node.
	 * @throws EventReaderException if any error occurred during xml node processing.
	 */
	private Node plainXmlDataFromNode(Node node) throws EventReaderException {
		try {
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "no");
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(node);
			trans.transform(source, result);
			String xmlString = sw.toString().replaceAll(">[ ]*[\t \n]*[ ]*<", "><");
			Node formattedNode = DOMUtils.stringToDOM(xmlString);
			return formattedNode;
		} catch (TransformerConfigurationException tcex) {
			logger.error(tcex.fillInStackTrace());
			throw new EventReaderException(tcex);
		} catch (TransformerException tex) {
			logger.error(tex.fillInStackTrace());
			throw new EventReaderException(tex);
		} catch (IOException ioex) {
			logger.error(ioex.fillInStackTrace());
			throw new EventReaderException(ioex);
		} catch (SAXException saxex) {
			logger.error(saxex.fillInStackTrace());
			throw new EventReaderException(saxex);
		}
	}
	/**
	 * Process recursively the payload included in the DOM tree of the Xml node
	 * passed by arguments. 
	 * <p>e.g. a business information with a XML payload as following: 
	 * <code>
	 * <message>
	 *   <payload>
	 *     <OrderShip>
	 *       <Item>
	 *         <ItemCode>0587</ItemCode>
	 *         <Quantity>3</ItemCode>
	 *       </Item>
	 *       <Item>
	 *         <ItemCode>9372</ItemCode>
	 *         <Quantity>10</ItemCode>
	 *       </Item>
	 *     </OrderShip>
	 *   </payload>
	 * <message>  
	 * </code>
	 * It will be process by this method returning the following information as
	 * a list of {@link es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.extension.Payload} data:
	 * <code>  
	 * Payload('message.payload.OrderShip.Item.ItemCode', '0587')
	 * Payload('message.payload.OrderShip.Item.Quantity', '3')
	 * Payload('message.payload.OrderShip.Item.ItemCode', '9372')
	 * Payload('message.payload.OrderShip.Item.Quantity', '10')
	 * </code>
	 * </p>
	 * @param node current node of the DOM tree.
	 * @param key partial {@link es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.xsd.bpaf.extension.Payload#getKey()} 
	 * associated to the current level node.
	 * @param data list of payload business data associated to a given level
	 * of the DOM tree at the current node.
	 * @return key name of the current level node. 
	 */
	private String processPayload(List<Payload> data, String key, Node node) {
		NodeList list = node.getChildNodes();
		String dataName = null;
		for (int i = 0; i < list.getLength(); i++) {		
			Node _node = list.item(i);
			if (_node.getNodeType() == Node.ELEMENT_NODE) {	
				if (key == null)
					dataName = node.getNodeName() + processPayload(data, node.getNodeName(), _node);
				else
					dataName = node.getNodeName() + processPayload(data, key + "." + node.getNodeName(), _node);
			} else {
				if (data == null)
					data = new ArrayList<Payload>();
				Payload payload = new Payload();
				payload.setKey(key + "." + node.getNodeName());
				payload.setValue(node.getTextContent());
				data.add(payload);				
			}
		}		
		return dataName;
	}
}