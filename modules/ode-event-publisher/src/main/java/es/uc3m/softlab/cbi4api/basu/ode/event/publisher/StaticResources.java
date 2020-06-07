/* 
 * $Id: StaticResources.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher;

/**
 * In this class is defined all global and generic static 
 * resources of the application.
 * 
 * @author averab
 */
public class StaticResources {
	/** Internationalization resource bundle name */
	public static final String CONFIG_RESOURCE_BUNDLE_NAME="ode-event-publisher";	
	/** Internationalization resource bundle */
	public static final String CONFIG_RESOURCE_BUNDLE=CONFIG_RESOURCE_BUNDLE_NAME + ".properties";	
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";
    /** Empty string representation */
    public static final String STRING_EMPTY="";

	/** Component name for the event publisher container */
	public static final String COMPONENT_NAME_EVENT_PUBLISHER_CONTAINER = "/cbi4api-basu/event-publisher/component/EventPublisherContainer";
	/** Component name for the bpel event data access object connection factory */
	public static final String COMPONENT_NAME_BPEL_EVENT_DAO_CONNECTION_FACTORY = "/cbi4api-basu/event-publisher/component/BpelEventDAOConnectionFactory";
	/** Component name for the event publisher scheduler */
	public static final String COMPONENT_NAME_CONFIG = "/cbi4api-basu/event-publisher/component/Config";
	/** Component name for the event publisher scheduler */
	public static final String COMPONENT_NAME_EVENT_SCHEDULER = "/cbi4api-basu/event-publisher/component/EventPublisherScheduler";
	/** Component name for the event reader service */
	public static final String COMPONENT_NAME_EVENT_READER = "/cbi4api-basu/event-publisher/component/EventReader";
	/** Component name for the event reader service */
	public static final String COMPONENT_NAME_EVENT_SENDER = "/cbi4api-basu/event-publisher/component/EventSender";
	
	/** Service name for the event publisher ETL (Extract, Transform & Load) module */
	public static final String SERVICE_NAME_EVENT_ETL = "/cbi4api-basu/event-publisher/service/EventETL";	
}
