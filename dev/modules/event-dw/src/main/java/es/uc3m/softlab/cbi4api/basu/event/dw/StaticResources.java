/* 
 * $Id: StaticResources.java,v 1.0 2014-04-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw;

/**
 * In this class is defined all global and generic static 
 * resources of the application.
 * 
 * @author averab
 */
public class StaticResources {
	/** Sigav persistence name */
	public static final String PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE="cbi4api-basu-event-dw";
	/** Sigav persistence unit name */
	public static final String PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE="cbi4api-basu-event-dw";
	/** Internationalization resource bundle */
	public static final String CONFIG_RESOURCE_BUNDLE="event-dw";	
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";

	/** Component name for event fact data access object */
	public static final String COMPONENT_NAME_EVENT_FACT_DAO = "/cbi4api/basu/event-dw/dao/EventFactDAO";
	/** Component name for process model data access object */
	public static final String COMPONENT_NAME_PROCESS_MODEL_DAO = "/cbi4api/basu/event-dw/dao/ProcessModelDAO";
	/** Component name for activity model data access object */
	public static final String COMPONENT_NAME_ACTIVITY_MODEL_DAO = "/cbi4api/basu/event-dw/dao/ActivityModelDAO";
	/** Component name for process instance data access object */
	public static final String COMPONENT_NAME_PROCESS_INSTANCE_DAO = "/cbi4api/basu/event-dw/dao/ProcessInstanceDAO";
	/** Component name for activity instance data access object */
	public static final String COMPONENT_NAME_ACTIVITY_INSTANCE_DAO = "/cbi4api/basu/event-dw/dao/ActivityInstanceDAO";
	
	/** Component name for event fact service facade */
	public static final String COMPONENT_NAME_EVENT_FACT_FACADE = "/cbi4api/basu/event-dw/facade/EventFactFacade";

	/** Component name for event fact reader ETL module */
	public static final String COMPONENT_NAME_EVENT_FACT_READER = "/cbi4api/basu/event-dw/component/EventFactReader";
	/** Component name for event fact writer ETL module */
	public static final String COMPONENT_NAME_EVENT_FACT_WRITER = "/cbi4api/basu/event-dw/component/EventFactWriter";
	/** Component name for event fact converter ETL module */
	public static final String COMPONENT_NAME_EVENT_FACT_CONVERTER = "/cbi4api/basu/event-dw/component/EventFactConverter";
	
	/** Service name for the data warehouse ETL (Extract, Transform & Load) module */
	public static final String SERVICE_NAME_EVENT_FACT_ETL = "/cbi4api/basu/event-dw/service/etlEventFact";
	
	/** Facade warn code for getting an event fact which does not exist */ 
	public static final int WARN_GET_EVENT_FACT_NOT_EXIST = 10001;	
	/** Facade warn code for saving an event fact which is a null object */ 
	public static final int WARN_SAVE_EVENT_FACT_NULL = 10002;
	/** Facade warn code for saving an event fact which already exists at the database */ 
	public static final int WARN_SAVE_EVENT_FACT_ALREADY_EXISTS = 10003;
	/** Facade warn code for deleting an event fact which does not exist */
	public static final int WARN_DELETE_EVENT_FACT_NOT_EXIST = 10004;
}
