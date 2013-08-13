/* 
 * $Id: StaticResources.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

/**
 * In this class is defined all global and generic static 
 * resources of the application.
 * 
 * @author averab
 */
public class StaticResources {
	/** Internationalization resource bundle name */
	public static final String CONFIG_RESOURCE_BUNDLE="xes-event-publisher";	
	/** basu-event BPAF xml schema classpath location */
    public static final String BASU_EVENT_BPAF_XML_SCHEMA_CLASSPATH_FILE="/xsd/basu-event.xsd";
	/** XES xml schema classpath location */
    public static final String BASU_XES_XML_SCHEMA_CLASSPATH_FILE="/xsd/xes.xsd";
	/** XES map xml schema classpath location */
    public static final String XES_MAP_XML_SCHEMA_CLASSPATH_FILE="/xsd/xesmap.xsd";	
	/** XES map xml classpath location */
    public static final String XES_MAP_XML_CLASSPATH_FILE="/xes-bpaf.xml";	
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";

	/** Component name for the XES event publisher configuration */
	public static final String COMPONENT_NAME_CONFIG = "xesEventPublisherConfig";
	/** Component name for the event publisher scheduler */
	public static final String COMPONENT_NAME_EVENT_SCHEDULER = "/cbi4api-basu/xes-event-publisher/component/EventPublisherScheduler";
	/** Component name for the event reader service */
	public static final String COMPONENT_NAME_EVENT_READER = "/cbi4api-basu/xes-event-publisher/component/EventReader";
	/** Component name for the event reader service */
	public static final String COMPONENT_NAME_EVENT_SENDER = "/cbi4api-basu/xes-event-publisher/component/EventSender";
	
	/** Service name for the XES ETL (Extract, Transform & Load) event processor */
	public static final String SERVICE_NAME_XES_ETL_PROCESSOR = "/cbi4api-basu/xes-event-publisher/service/xes-etl-processor";	
	/** Component name for the XES event writer */
	public static final String COMPONENT_NAME_XES_EVENT_WRITER = "/cbi4api-basu/event-publisher/component/XESEventWriter";
	/** Component name for the XES event reader service */
	public static final String COMPONENT_NAME_XES_EVENT_READER = "/cbi4api-basu/event-publisher/component/XESEventReader";
	/** Component name for the XES event converter service */
	public static final String COMPONENT_NAME_XES_EVENT_CONVERTER = "/cbi4api-basu/event-publisher/component/XESEventConverter";
}
