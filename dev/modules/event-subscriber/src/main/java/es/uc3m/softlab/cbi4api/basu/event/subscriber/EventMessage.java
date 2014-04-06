/*
 * $Id: EventMesssage.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.subscriber;

import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author averab
 */
public class EventMessage implements Serializable {
    /** Serial Version UID */
    private static final long serialVersionUID = 42046826486280640L;
    /** Event source identifier. */
    private Set<es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event> gbasEvents;
    /** Event previous state. */
    private es.uc3m.softlab.cbi4api.basu.event.store.domain.Event domainEvent;

    /**
     * Creates a new object with null property values.
     */
    public EventMessage() {
    }
    /**
     * Gets the {@link #gbasEvents} property.
     * @return the gbasEvents property.
     */
    public Set<es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event> getGbasEvents() {
        if (gbasEvents == null)
            this.gbasEvents = Sets.newHashSet();
        return gbasEvents;
    }
    /**
     * Sets the {@link #gbasEvents} property.
     * @param gbasEvents the {@link #gbasEvents} to set
     */
    public void setGbasEvents(Set<es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event> gbasEvents) {
        this.gbasEvents = gbasEvents;
    }
    /**
     * Gets the {@link #domainEvent} property.
     * @return the domainEvent property.
     */
    public es.uc3m.softlab.cbi4api.basu.event.store.domain.Event getDomainEvent() {
        return domainEvent;
    }
    /**
     * Sets the {@link #domainEvent} property.
     * @param domainEvent the {@link #domainEvent} to set
     */
    public void setDomainEvent(es.uc3m.softlab.cbi4api.basu.event.store.domain.Event domainEvent) {
        this.domainEvent = domainEvent;
    }
    /**
     * Returns a string representation of the object.
     * @return string representation of the object.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventMesssage (GBAS");
        for (es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event.Event event : gbasEvents)
            builder.append(String.format("[%s]", event));
        builder.append(") DOMAIN [");
        builder.append(domainEvent);
        builder.append("]");
        return builder.toString();
    }
    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = gbasEvents.hashCode();
        result = 31 * result + domainEvent.hashCode();
        return result;
    }
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        EventMessage that = (EventMessage) obj;

        if (!domainEvent.equals(that.domainEvent)) return false;
        if (!gbasEvents.equals(that.gbasEvents)) return false;

        return true;
    }
}
