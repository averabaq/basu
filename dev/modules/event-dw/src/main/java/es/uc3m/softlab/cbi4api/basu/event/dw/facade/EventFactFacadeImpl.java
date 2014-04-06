/* 
 * $Id: EventFactFactFacadeImpl.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.facade;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.dao.EventFactDAO;
import es.uc3m.softlab.cbi4api.basu.event.dw.dto.EventFactResultDTO;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the 
 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
 * entity object. This class manages all data access throughout the 
 * Spring framework.
 * 
 * @author averab  
 * @version 1.0.0
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE,
        unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
@Transactional
@Service(value=EventFactFacade.COMPONENT_NAME)
public class EventFactFacadeImpl implements EventFactFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(EventFactFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
    protected EntityManager entityManager;
    /** Event fact data access object */
    @Autowired private EventFactDAO eventFactDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getId()} as primary key.
	 * 
	 * @param id event's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated.
	 * @throws EventFactException if any event error occurred.
	 */
    @Override
    public EventFact getEventFact(long id) throws EventFactException {
		logger.debug("Retrieving event fact with identifier " + id + "...");
		EventFact event = eventFactDAO.findById(id);
		if (event == null) {
			logger.warn("Cannot get event fact. Event fact with identifier: " + id + " does not exist.");
			throw new EventFactException(StaticResources.WARN_GET_EVENT_FACT_NOT_EXIST, "Event fact with id: " + id + " does not exist.");
		}
		logger.debug("Event fact " + event + " retrieved successfully.");
		return event;
	}
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact#getId()} as primary key.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects defined at the data base.
	 */
    @Override
	public List<EventFact> getAll() {
		logger.debug("Retrieving all event facts...");
		List<EventFact> facts = eventFactDAO.findAll();
		logger.debug("Event facts retrieved successfully.");
		return facts;
	}
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process or activity model.
	 * 
	 * @param model process or activity model identifier.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process model identifier.
	 */
    @Override
    public List<EventFact> getAllFromModel(long model) {
		logger.debug("Getting all event facts from process model with identifier " + model + "...");
		List<EventFact> list = eventFactDAO.findAllByProcessModel(model);
		logger.debug("All event facts from process model with identifier " + model + " found successfully.");
		return list;
	}  
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process or activity instance.
	 * 
	 * @param instance process or activity instance identifier.
	 * @return all {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact} entity
	 * objects of a determined process instance identifier.
	 */
    @Override
    public List<EventFact> getAllFromInstance(long instance) {
		logger.debug("Getting all event facts from process instance with identifier " + instance + "...");
		List<EventFact> list = eventFactDAO.findAllByProcessInstance(instance);
		logger.debug("All event facts from process instance with identifier " + instance + " found successfully.");
		return list;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object state with the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to save.
	 * @throws EventFactException if any illegal data access or inconsistent event data error occurred.
	 */
    @Override
	public void storeEventFact(EventFact fact) throws EventFactException {
		logger.debug("Saving the event fact " + fact + "...");
		if (fact == null) {
			logger.warn("Cannot save event fact. Event fact is null.");
			throw new EventFactException(StaticResources.WARN_SAVE_EVENT_FACT_NULL, "Cannot save event. EventFact is null.");
		}
		EventFact _fact = eventFactDAO.findById(fact.getId());
		if (_fact != null) {
			logger.warn("Cannot save event. Event fact already exists.");
			throw new EventFactException(StaticResources.WARN_SAVE_EVENT_FACT_ALREADY_EXISTS, "Cannot save event fact. Event fact already exists.");			
		}		
		entityManager.detach(fact);
		eventFactDAO.save(fact);
		logger.debug("Event fact " + fact + " saved successfully.");
	}    
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object state with the data base.
	 * 
	 * @param fact {@link es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact}
	 * entity object to delete.
	 * @throws EventFactException if any illegal data access or inconsistent event data error occurred.
	 */
    @Override
	public void deleteEventFact(EventFact fact) throws EventFactException {
		logger.debug("Removing event fact " + fact + "...");	
		EventFact _fact = eventFactDAO.findById(fact.getId());
		if (_fact == null) {
			logger.warn("Cannot remove event fact. Event fact with identifier: " + fact.getId() + " does not exist.");
			throw new EventFactException(StaticResources.WARN_DELETE_EVENT_FACT_NOT_EXIST, "Cannot remove event fact. Event fact with identifier: " + fact.getId() + " does not exist.");
		}
		eventFactDAO.delete(_fact);		
		logger.info("Event fact " + _fact + " removed successfully.");
	}	
	/**
	 * Executes a dynamic query.
	 * 
	 * @param query dynamic query.
	 * @return all tuples retrieved from the execution of the dynamic query.
	 * @throws EventFactException if any illegal data access or inconsistent query error occurred.
	 */
    @Override
	@SuppressWarnings("unchecked")
    public List<EventFactResultDTO> executeQuery(String query) throws EventFactException {
		try {
			logger.debug("Executing dynamic query " + query + "...");	
			Query _query = entityManager.createQuery(query);
			//org.hibernate.Query hib = _query.unwrap(org.hibernate.Query.class);
			//hib.setResultTransformer(Transformers.aliasToBean(EventFactResultDTO.class));
			List<EventFactResultDTO> tuples = _query.getResultList();
			logger.debug("Dynamic query executed successfully.");
			return tuples;
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			throw new EventFactException(ex);
		}		
	}  
}