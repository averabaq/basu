/* 
 * $Id: EventFactConverter.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.etl;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.fact.EventFact;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;

/**
 * Component interface for converting the event store information into facts.
 * 
 * @author averab
 */
public interface EventFactConverter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_FACT_CONVERTER;        

    /**
     * 
     * @param processes
     * @param activities
     * @return
     */
    public List<EventFact> transform(List<ProcessInstance> processes, List<ActivityInstance> activities);
}