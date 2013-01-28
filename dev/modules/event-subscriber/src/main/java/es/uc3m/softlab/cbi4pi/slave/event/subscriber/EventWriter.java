/* 
 * $Id: EventWriter.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.subscriber;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.EventException;

/**
 * Component interface for writing events to the global data store. 
 * This interface defines all methods for storing <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventWriter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_WRITER;        

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
    public Event getEvent(long id) throws EventException;
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Event}
	 * entity object to save.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void storeEvent(Event event) throws EventException;
}