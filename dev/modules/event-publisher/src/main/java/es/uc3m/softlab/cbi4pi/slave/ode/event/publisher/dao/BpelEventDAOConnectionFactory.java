/* 
 * $Id: BpelEventDAOConnectionFactory.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao;

import es.uc3m.softlab.cbi4api.basu.ode.event.publisher.StaticResources;

/**
 * Data access object factory interface for creating <i>dao</i> instances in order 
 * to access event data by the means of the 
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
public interface BpelEventDAOConnectionFactory {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_BPEL_EVENT_DAO_CONNECTION_FACTORY;        

    /**
     * Gets a new {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnection} 
     * data access object.
     * @return new {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnection} dao object.
     */
    public BpelEventDAOConnection getConnection();
}