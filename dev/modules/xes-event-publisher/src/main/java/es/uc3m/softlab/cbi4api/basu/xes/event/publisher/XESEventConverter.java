/* 
 * $Id: XESEventConverter.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.LogType;

/**
 * Component interface for converting the incoming XES events into BPAF extended format.
 * 
 * <p> This class follows the  
 * <a href="http://www.openxes.org"> XES
 * (XML Event)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition)</a> by
 * extracting the event data from an extended bpaf format. 
 * 
 * @author averab
 * @version 1.0.0
 */
public interface XESEventConverter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_XES_EVENT_CONVERTER;        

    /**
     * Convert the input XES event contents into BPAF extension format.
     * 
     * @param event XES event.
     * @return events in BPAF extension format.
     * @throws XESException if any XES map configuration error is found.
     */
    public List<Event> transform(LogType xes) throws XESException;
}