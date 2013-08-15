/* 
 * $Id: StaticResources.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

/**
 * In this class is defined all global and generic static 
 * resources of the project system module.
 * 
 * @author averab
 */
public class StaticResources {
	/** Internationalization resource bundle */
	public static final String CONFIG_RESOURCE_BUNDLE="event-subscriber";		
	/** basu-event BPAF xml schema classpath location */
    public static final String BASU_EVENT_BPAF_XML_SCHEMA_CLASSPATH_FILE="/xsd/basu-event.xsd";
	/** basu-process BPAF xml schema classpath location */
    public static final String BASU_PROCESS_BPAF_XML_SCHEMA_CLASSPATH_FILE="/xsd/basu-process.xsd";
    /** Charset encoding */
    public static final String CHARSET_ENCODING = "UTF-8";
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";
    
	/** Component name for the event subscriber configuration */
	public static final String COMPONENT_NAME_CONFIG = "eventSubscriberConfig";
	/** Component name for the event writer */
	public static final String COMPONENT_NAME_EVENT_WRITER = "/cbi4api-basu/event-subscriber/component/EventWriter";
	/** Component name for the event reader service */
	public static final String COMPONENT_NAME_EVENT_READER = "/cbi4api-basu/event-subscriber/component/EventReader";
	/** Component name for the event converter service */
	public static final String COMPONENT_NAME_EVENT_CONVERTER = "/cbi4api-basu/event-subscriber/component/EventConverter";
	/** Component name for the event writer */
	public static final String COMPONENT_NAME_EVENT_CORRELATOR = "/cbi4api-basu/event-subscriber/component/EventCorrelator";
	
	/** Service name for the event subscriber ETL (Extract, Transform & Load) module */
	public static final String SERVICE_NAME_ETL_PROCESSOR = "/cbi4api-basu/event-subscriber/service/etl-event-processor";
	
	/** Facade warn login code for generic spring components exceptions */ 
	public static final int ERROR_GENERIC_SPRING_COMPONENTS = 1;
	/** Facade warn login code for generic event subscriber unexpected exceptions */ 
	public static final int ERROR_GENERIC_EVENT_SUBSCRIBER_UNEXPECTED_EXCEPTION = 10;
	
	/** Facade warn code for processing an incoming event which does not supply enough information to correlate the event */ 
	public static final int ERROR_INCOMING_EVENT_EMPTY_CORRELATION_DATA = 10001;	
	/** Facade warn code for processing an incoming event which has not state transition */ 
	public static final int WARN_EVENT_WITH_NO_STATE_TRANSITION = 10002;
}
