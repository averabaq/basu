/* 
 * $Id: EventCorrelator.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.subscriber;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel;
import es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.EventException;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4pi.slave.event.store.facade.ProcessInstanceException;

import es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event;

/**
 * Component interface for correlating events before being stored into 
 * the global data store. 
 * This interface defines all methods for correlating <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventCorrelator {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_CORRELATOR;        

    /**
     * Correlates the incoming {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event} 
     * by obtaining an existing process instance or creating a new one if necessary. 
     * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event} to get the associated
     * process instance if it exists, otherwise it creates a new one.
     * @param processModel {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} associated to the incoming 
     * {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event}.
     * @param source {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} associated to the 
     * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessModel} of the incoming 
     * {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event}.
     * @return right {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance} associated to properly 
     * correlated the incoming {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event}.
     * @throws ProcessInstanceException if any process instance exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     */
    public ProcessInstance correlate(Event event, ProcessModel processModel, Source source) throws ProcessInstanceException, EventException;
    /**
     * Correlates the incoming {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event} 
     * by obtaining an existing process instance or creating a new one if necessary. 
     * @param event {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event} to get the associated
     * process instance if it exists, otherwise it creates a new one.
     * @param source {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.Source} associated to the 
     * {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityModel} of the incoming 
     * {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event}.
     * @return right {@link es.uc3m.softlab.cbi4pi.slave.event.store.entity.ActivityInstance} associated to properly 
     * correlated the incoming {@link es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event}.
     * @throws ActivityInstanceException if any activity instance exception occurred during processing.
     * @throws ModelException if any model exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     */
    public ActivityInstance correlate(Event event, Source source) throws ModelException, ActivityInstanceException, EventException;
}