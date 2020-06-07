/* 
 * $Id: BpelEventDAOConnection.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao;

import java.util.List;

import org.apache.ode.dao.jpa.EventDAOImpl;
import org.apache.ode.dao.jpa.ProcessDAOImpl;
import org.apache.ode.dao.jpa.ScopeDAOImpl;
import org.apache.ode.dao.jpa.XmlDataDAOImpl;

/**
 * Represents the physical resource for connecting to the bpel event store.
 * This <i>dao</i> interface provides the essential functionality to access 
 * event data by the means of the 
 * <a href="http://ode.apache.org/bpel-management-api-specification.html">ApacheODE API</a>.
 * 
 * For further information about the usage of this package visit the project web site 
 * at the <a href="http://ode.apache.org">ApacheODE Project</a> from the apache software 
 * foundation. 
 * 
 * This interface defines all methods for accessing to the ApacheODE object data 
 * throughout the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface BpelEventDAOConnection {
	/**
	 * Find all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects defined
	 * at the local event store.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects.
	 */
	public List<EventDAOImpl> findAllEvents();
	/**
	 * Find all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects defined
	 * at the local event store.
	 * 
	 * @param id event identifier to retrieve the events by starting from the given value.
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects with a 
	 * greater {@link org.apache.ode.dao.jpa.EventDAOImpl#getId()} that the value passed
	 * by arguments.
	 */
	public List<EventDAOImpl> findAllEventsStartingFrom(long id);
	/**
	 * Find the previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object defined
	 * at the local event store.
	 * 
	 * @param event event to retrieve the previous event from.
	 * @return previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object.
	 */
	public EventDAOImpl findPreviousEvent(EventDAOImpl event);	
	/**
	 * Find all {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity objects associated to the
	 * process {@link org.apache.ode.dao.jpa.ProcessDAOImpl} passed by arguments.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity objects.
	 */
	public List<ScopeDAOImpl> findScopesByProcess(ProcessDAOImpl process);	
	/**
	 * Find the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl#_scopeId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return list of {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity object associated.
	 */
	public List<XmlDataDAOImpl> findXmlDataByScopeId(long id);	
	/**
	 * Find the {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.ScopeDAOImpl#_scopeInstanceId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity object associated.
	 */
	public ScopeDAOImpl findScopeById(long id);
}
