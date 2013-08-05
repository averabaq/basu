/* 
 * $Id: EventReaderImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher;

import es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.ode.dao.jpa.EventDAOImpl;
import org.apache.ode.dao.jpa.ScopeDAOImpl;
import org.apache.ode.dao.jpa.XmlDataDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for retrieving the bpel events from the local data store. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data throughout the ApacheODE API (1.3.5) and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventReader.COMPONENT_NAME)
public class EventReaderImpl implements EventReader {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventReaderImpl.class);
    /** Configuration object */
    @Autowired private Config config;
	/** Bpel Event Data Access Object connection factory implementation */
	@Autowired private BpelEventDAOConnectionFactory bpelEventDAOFactory;	
	
	/**
	 * Read all {@link org.apache.ode.dao.jpa.EventDAOImpl} new entity objects defined
	 * at the local event store which were never retrieved and published before.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects with a 
	 * greater {@link org.apache.ode.dao.jpa.EventDAOImpl#getId()} that the value passed
	 * by arguments.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	public List<EventDAOImpl> readNewEvents() throws EventReaderException {
		List<EventDAOImpl> events = new ArrayList<EventDAOImpl>(); 
		logger.debug("Retrieving all new events from the local store...");
		long eventId = 0;
		
		try {
			eventId = config.readLastEvent();
			events = bpelEventDAOFactory.getConnection().findAllEventsStartingFrom(eventId);
			logger.debug("All new events retrieved successfully.");
			if (events.isEmpty()) 							
				logger.debug("No new events found.");
		} catch(IOException ioex) {
			logger.error(ioex.fillInStackTrace());
			throw new EventReaderException(ioex);
		}
		return events;
	}
	/**
	 * Reads all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects defined
	 * at the local event store.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	public List<EventDAOImpl> readAllEvents() throws EventReaderException {
		List<EventDAOImpl> events = new ArrayList<EventDAOImpl>(); 
		logger.debug("Retrieving all events from the local store...");
		events = bpelEventDAOFactory.getConnection().findAllEvents();
		logger.debug("All events retrieved successfully.");
		return events;
	}
	/**
	 * Reads the previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object defined
	 * at the local event store.
	 * 
	 * @param event event to retrieve the previous event from.
	 * @return previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object.
	 */
	@Override
	public EventDAOImpl readPreviousEvent(EventDAOImpl event) throws EventReaderException {
		EventDAOImpl _event = null; 
		logger.debug("Retrieving previous event from the local store...");
		_event = bpelEventDAOFactory.getConnection().findPreviousEvent(event);
		logger.debug("Previous event retrieved successfully.");
		return _event;
	}		
	/**
	 * Reads the {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.ScopeDAOImpl#_scopeInstanceId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity object associated.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	public ScopeDAOImpl readScope(Long id) throws EventReaderException {
		if (id == null) 
			return null;		
		ScopeDAOImpl scope = null; 
		logger.debug("Retrieving scope with id " + id + " from the local store...");
		scope = bpelEventDAOFactory.getConnection().findScopeById(id);
		logger.debug("Scope with id " + id + " retrieved successfully.");
		return scope;
	}
	/**
	 * Reads the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl#_scopeId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return list of {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity object associated.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	public List<XmlDataDAOImpl> readXmlData(Long id) throws EventReaderException {
		if (id == null) 
			return new ArrayList<XmlDataDAOImpl>();
		logger.debug("Retrieving xml data associated to the scope with id " + id + " from the local store...");
		List<XmlDataDAOImpl> list = bpelEventDAOFactory.getConnection().findXmlDataByScopeId(id);
		logger.debug("Xml data associated to the scope with id " + id + " retrieved successfully.");
		return list;
	}	
}
