/* 
 * $Id: ETLEventImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.mxml.event.publisher;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.basu.event.Payload;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.basu.event.State;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.mxml.WorkflowLog;

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
	/** Event reader to extract the event data from the local store */
	//@Autowired private EventReader extractor;
	/** Event sender to publish the data to the jms queue */
	@Autowired private EventSender loader;
	
	/**
	 * Process all events by executing the ETL engine.
	 * @throws EventPublisherException if any error occurred during 
	 * the ETL execution process.
	 */
	public void process() throws EventPublisherException {
		try {
			/* extract events */
			//List<EventDAOImpl> odeEvents = extractor.readNewEvents();
			/* transform events */
			List<Event> events = transform(new ArrayList<WorkflowLog>());
			/* load events */
			loader.publishEvents(events);
		} catch(EventReaderException evrex) {
			logger.error(evrex.fillInStackTrace());
			throw new EventPublisherException(evrex);
		} catch(JMSException jmsex) {
			logger.error(jmsex.fillInStackTrace());
			throw new EventPublisherException(jmsex);
		}
	}
	/**
	 * Converts a list of {@link org.apache.ode.dao.jpa.EventDAOImpl} event objects
	 * into a list of {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.xsd.bpaf.Event}
	 * event objects.
	 * 
	 * @param events list of <strong>ApacheODE</strong> events to transform.
	 * @return list of events transformed to the <strong>BPAF</strong> format.
	 * @throws EventReaderException if any reader error occurred during the transformation process.
	 */
	private List<Event> transform(List<WorkflowLog> events) throws EventReaderException {
		List<Event> _events = new ArrayList<Event>();
		
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
		return null;
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
		
		return payload;
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
	 * a list of {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.xsd.bpaf.extension.Payload} data:
	 * <code>  
	 * Payload('message.payload.OrderShip.Item.ItemCode', '0587')
	 * Payload('message.payload.OrderShip.Item.Quantity', '3')
	 * Payload('message.payload.OrderShip.Item.ItemCode', '9372')
	 * Payload('message.payload.OrderShip.Item.Quantity', '10')
	 * </code>
	 * </p>
	 * @param node current node of the DOM tree.
	 * @param key partial {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.xsd.bpaf.extension.Payload#getKey()} 
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