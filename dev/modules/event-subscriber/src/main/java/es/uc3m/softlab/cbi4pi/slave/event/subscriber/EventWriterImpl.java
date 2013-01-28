/* 
 * $Id: EventWriterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.subscriber;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.EventException;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.EventFacade;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component implementation for writing events to the global data store. 
 * This interface defines all methods for storing <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Transactional(propagation=Propagation.MANDATORY)
@Component(value=EventWriter.COMPONENT_NAME)
public class EventWriterImpl implements EventWriter {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventWriterImpl.class);
	/** Event session facade */
    @Autowired private EventFacade eventFacade;
	
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event#getEventID()} as primary key.
	 * 
	 * @param id event's identifier
	 * @return {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event} entity 
	 * object associated.
	 * @throws EventException if any event error occurred.
	 */
    public Event getEvent(long id) throws EventException {
		logger.debug("Retrieving event with identifier " + id + "...");
		Event event = eventFacade.getEvent(id);
		logger.debug("Event " + event + " retrieved successfully.");
		return event;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to save.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void storeEvent(Event event) throws EventException {
		logger.debug("Saving the event " + event + "...");
		eventFacade.storeEvent(event);
		logger.info("Event " + event + " stored successfully.");
	} 
}