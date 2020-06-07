/* 
 * $Id: EventUtils.java,v 1.0 2013-01-23 22:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.*;
import org.springframework.beans.BeanUtils;

/**
 * Utility class for managing event messages
 *
 * @author averab
 */
public class EventUtils {
    /**
     * Clones a new {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event basuEvent} instance.
     * @param basuEvent {@link es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event basuEvent} instance to cloned.
     * @return new instance cloned.
     */
    public static Event cloneEvent(Event basuEvent) {
        Event clonedEvent = new Event();
        BeanUtils.copyProperties(basuEvent, clonedEvent);
        clonedEvent.setCorrelation(new Correlation());
        clonedEvent.setEventDetails(new Event.EventDetails());
        BeanUtils.copyProperties(basuEvent.getEventDetails(), clonedEvent.getEventDetails());
        // clone correlation
        if (basuEvent.isSetCorrelation() && basuEvent.getCorrelation().isSetCorrelationData()) {
            clonedEvent.getCorrelation().setCorrelationData(new CorrelationData());
            clonedEvent.getCorrelation().setProcessInstanceID(basuEvent.getCorrelation().getProcessInstanceID());
            for (CorrelationElement correlation : basuEvent.getCorrelation().getCorrelationData().getCorrelationElement()) {
                CorrelationElement clonedCorrelation = new CorrelationElement();
                BeanUtils.copyProperties(correlation, clonedCorrelation);
                clonedEvent.getCorrelation().getCorrelationData().getCorrelationElement().add(clonedCorrelation);
            }
        }
        // clone payload
        for (Payload payload : basuEvent.getPayload()) {
            Payload clonedPayload = new Payload();
            BeanUtils.copyProperties(payload, clonedPayload);
            clonedEvent.getPayload().add(clonedPayload);
        }
        // clone data elements
        for (DataElement element : basuEvent.getDataElement()) {
            DataElement clonedElement = new DataElement();
            BeanUtils.copyProperties(element, clonedElement);
            clonedEvent.getDataElement().add(clonedElement);
        }
        return clonedEvent;
    }
}