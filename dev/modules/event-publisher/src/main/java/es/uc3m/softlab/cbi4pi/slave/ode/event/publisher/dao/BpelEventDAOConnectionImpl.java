/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package es.uc3m.softlab.cbi4pi.slave.ode.event.publisher.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.apache.ode.dao.jpa.EventDAOImpl;
import org.apache.ode.dao.jpa.ProcessDAOImpl;
import org.apache.ode.dao.jpa.ProcessInstanceDAOImpl;
import org.apache.ode.dao.jpa.ScopeDAOImpl;
import org.apache.ode.dao.jpa.XmlDataDAOImpl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents the physical resource for connecting to the bpel event store.
 * This <i>dao</i> implementation provides the essential functionality to access 
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
@Transactional(propagation=Propagation.MANDATORY)
public class BpelEventDAOConnectionImpl implements BpelEventDAOConnection {
	/** Logger for tracing */
    private transient final Logger logger = Logger.getLogger(BpelEventDAOConnectionImpl.class);
    /** Entity manager bound to this bean */
    private EntityManager entityManager;
    /** Data set frame size */
    private Integer frameSize;

    /**
     * Creates a instance of the data access object by setting the entity manager
     * bounds to this bean by the Java Persistence API (JPA) with a null {@link #frameSize}.
     * 
     * @param entityManager entity manager bound to this object.
     */
    public BpelEventDAOConnectionImpl(EntityManager entityManager) {
    	this.entityManager = entityManager;
    	this.frameSize = null;
    }
    /**
     * Creates a instance of the data access object by setting the entity manager
     * bounds to this bean by the Java Persistence API (JPA).
     * 
     * @param entityManager entity manager bound to this object.
     */
    public BpelEventDAOConnectionImpl(EntityManager entityManager, Integer frameSize) {
    	this.entityManager = entityManager;
    	this.frameSize = frameSize;
    }

    /**
	 * Find all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects defined
	 * at the local event store.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EventDAOImpl> findAllEvents() {		
		logger.debug("Finding all events...");
		Query query = entityManager.createQuery("select e from EventDAOImpl e where e._scopeId is null or exists (select s from ScopeDAOImpl s where (s._scopeInstanceId = e._scopeId and s._parentScope is not null)) order by e._id asc");
		//Query query = entityManager.createQuery("select e from EventDAOImpl e order by e._id asc");
		/* if the property is defined, it limits the query result size */
		if (frameSize != null) {
			query.setMaxResults(frameSize);
		}
		List<EventDAOImpl> list = query.getResultList();
		logger.debug("All events found successfully.");
		return list;
	}
	/**
	 * Find all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects defined
	 * at the local event store.
	 * 
	 * @param id event identifier to retrieve the events by starting from the given value.
	 * @return all {@link org.apache.ode.dao.jpa.EventDAOImpl} entity objects with a 
	 * greater {@link org.apache.ode.dao.jpa.EventDAOImpl#getId()} that the value passed
	 * by arguments.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EventDAOImpl> findAllEventsStartingFrom(long id) {		
		logger.debug("Finding all events starting from event identifier " + id + "..."); 
		Query query = entityManager.createQuery("select e from EventDAOImpl e where e._id >= :id order by e._id asc");
		query.setParameter("id", id);
		/* if the property is defined limits the query result size */
		if (frameSize != null) {
			query.setMaxResults(frameSize);
		}
		List<EventDAOImpl> list = query.getResultList();		
		logger.debug("All events starting from event identifier " + id + " found successfully.");
		return list;
	}	
	/**
	 * Find the previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object defined
	 * at the local event store.
	 * 
	 * @param event event to retrieve the previous event from.
	 * @return previous {@link org.apache.ode.dao.jpa.EventDAOImpl} entity object.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EventDAOImpl findPreviousEvent(EventDAOImpl event) {		
		logger.debug("Finding previous event of event with identifier " + event.getId() + "..."); 
		Query query = entityManager.createQuery("select e from EventDAOImpl e where e._tstamp <= :tstamp and e._id < :id and e._instance = :instance and e._scopeId = :scope order by e._id desc");
		query.setParameter("instance", event.getInstance());
		query.setParameter("tstamp", event.getTstamp());
		query.setParameter("id", event.getId());
		query.setParameter("instance", event.getInstance());
		query.setParameter("scope", event.getScopeId());
		List<EventDAOImpl> events = query.getResultList();
		if (events != null && events.size() > 0) {
			logger.debug("Previous event of event with identifier " + event.getId() + " found successfully.");
			return events.get(0);
		}
		logger.debug("No previous event found.");
		return null;
	}	
	/**
	 * Find all {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity objects of a determined
	 * {@link org.apache.ode.dao.jpa.ProcessDAOImpl}.
	 * 
	 * @return all {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity objects of a determined 
	 * {@link org.apache.ode.dao.jpa.ProcessDAOImpl}.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ScopeDAOImpl> findScopesByProcess(ProcessDAOImpl process) {		
		logger.debug("Finding all scopes of process instance " + process.getId() + "..."); 
		Query query = entityManager.createNamedQuery(ProcessInstanceDAOImpl.SELECT_INSTANCE_IDS_BY_PROCESS);
		query.setParameter("process", process);
		List<ScopeDAOImpl> list = query.getResultList();
		logger.debug("All scopes of process instance " + process.getId() + " found successfully.");
		return list;
	}
	/**
	 * Find the {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.ScopeDAOImpl#_scopeInstanceId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return {@link org.apache.ode.dao.jpa.ScopeDAOImpl} entity object associated.
	 */
	@Override
	public ScopeDAOImpl findScopeById(long id) {		
		logger.debug("Finding scope with identifier " + id + "...");
		ScopeDAOImpl scope = null;		
		Query query = entityManager.createQuery("select s from ScopeDAOImpl s where s._scopeInstanceId = :id");
		query.setParameter("id", id);		
		try {
			scope = (ScopeDAOImpl)query.getSingleResult();
		} catch(NoResultException nrex) {
			logger.debug("No scope found with identifier " + id + ".");			
		}
		logger.debug("Scope with identifier " + id + " found successfully.");
		return scope;
	}
	/**
	 * Find the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity 
	 * object associated to the {@link org.apache.ode.dao.jpa.XmlDataDAOImpl#_scopeId} as primary key.
	 * 
	 * @param id scope identifier
	 * @return list of {@link org.apache.ode.dao.jpa.XmlDataDAOImpl} entity object associated.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<XmlDataDAOImpl> findXmlDataByScopeId(long id) {		
		logger.debug("Finding xml data associated to the scope with identifier " + id + "...");
		Query query = entityManager.createQuery("select distinct x from XmlDataDAOImpl as x where x._scopeId = :scope");
		query.setParameter("scope", id);				
		List<XmlDataDAOImpl> list = query.getResultList();		
		logger.debug("Xml data associated to the scope with identifier " + id + " found successfully.");
		return list;
	}		
	/**
	 * Gets the {@link #entityManager} property.
	 * @return the {@link #entityManager} property.
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}
	/**
	 * Sets the {@link #entityManager} property.
	 * @param entityManager the {@link #entityManager} property to set.
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
