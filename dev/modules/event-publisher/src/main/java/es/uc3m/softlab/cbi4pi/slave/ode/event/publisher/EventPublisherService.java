/* 
 * $Id: EventPublisherService.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the main class to launch the event publisher service used for
 * publishing events from a pre-configured BPEL engine (Apache ODE 1.3.5).
 * 
 * @author averab
 * @version 1.0.0
 */
public class EventPublisherService {
    /** Logger for tracing */
    private static Logger logger = Logger.getLogger(EventPublisherService.class);

	/**
	 * Main method for starting up the event publisher service.
	 * 
	 * @param args program arguments
	 */
	public static void main(String[] args) {
        try {
            String version = StaticResources.STRING_EMPTY;
            Package pkg = Package.getPackage("es.uc3m.softlab.cbi4api.basu.ode.event.publisher");
            if (pkg != null) {
                version = ": " + pkg.getImplementationVersion();
            }
            logger.info("Starting Event Publisher (Apache ODE Plugin) " + version);

            final AbstractXmlApplicationContext context;
            logger.info("Loading Event Publisher from spring-event-publisher.xml on the CLASSPATH");
            context = new ClassPathXmlApplicationContext(new String[] {"spring-event-publisher.xml"}, false);
            context.setValidating(false);
            context.refresh();
                        
            EventPublisherContainer container = (EventPublisherContainer) context.getBean(StaticResources.COMPONENT_NAME_EVENT_PUBLISHER_CONTAINER);            
            container.onShutDown(new Runnable() {
                public void run() {
                    if (context instanceof DisposableBean) {
                        try {
                            ((DisposableBean) context).destroy();
                        } catch (Exception ex) {
                            logger.error("Caught: " + ex);
                            ex.printStackTrace();
                        }
                    }
                }
            });
            /* 
             * To avoid System.exit() being invoked during container running, 
             * we need to keep the container main thread alive.
             */ 
            container.block();                        
        } catch (Exception ex) {
            logger.fatal("Caught: " + ex);
            ex.printStackTrace();
        }		
	}
}
