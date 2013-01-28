/* 
 * $Id: EventReader.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.ode.event.publisher;

import java.util.List;

import org.apache.ode.dao.jpa.EventDAOImpl;
import org.apache.ode.dao.jpa.ScopeDAOImpl;
import org.apache.ode.dao.jpa.XmlDataDAOImpl;

/**
 * Component interface for retrieving the bpel events from the local data store. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data throughout the ApacheODE API (1.3.5) and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventReader {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_READER;        

	/**
	 * Read all {@link org.apache.ode.dao.jpa.EventDAOImpl} new entity objects defined
	 * at the local event store which were never retrieved and published before.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects with a 
	 * greater {@link org.apache.ode.dao.jpa.EventDAOImpl#getId()} that the value passed
	 * by arguments.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	public List<EventDAOImpl> readNewEvents() throws EventReaderException;
	/**
	 * Reads all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects defined
	 * at the local event store.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	public List<EventDAOImpl> readAllEvents() throws EventReaderException;
	/**
	 * Reads the previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object defined
	 * at the local event store.
	 * 
	 * @param event event to retrieve the previous event from.
	 * @return previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object.
	 */
	public EventDAOImpl readPreviousEvent(EventDAOImpl event) throws EventReaderException;	
	/**
	 * Reads the {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.ScopeDAOImpl#_scopeInstanceId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity object associated.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	public ScopeDAOImpl readScope(Long id) throws EventReaderException;
	/**
	 * Reads the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl#_scopeId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return list of {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity object associated.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	public List<XmlDataDAOImpl> readXmlData(Long id) throws EventReaderException;
}