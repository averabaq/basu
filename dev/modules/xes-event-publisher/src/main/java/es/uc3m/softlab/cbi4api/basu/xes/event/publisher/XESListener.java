/* 
 * $Id: XESListener.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Specific ETL (Extract, Transform & Load) service interface for XES events  
 * events <a href="http://www.openxes.org">OpenXES</a>.
 * 
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from heterogeneous systems, transforming its XES events 
 * into the BPAF extended format, and publishing the converted events throughout
 * a jms messaging queue.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
@Service(value=StaticResources.SERVICE_NAME_XES_LISTENER)
public class XESListener implements MessageListener {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(XESListener.class);  
	/** Event reader service */
	@Autowired private ETLEvent etl;

    /**
	 * Passes a message to the listener.
	 * @param msg the message passed to the listener
	 */
	@Override
	public void onMessage(Message msg) {
		try {			
			logger.debug("JMS message ID: " + msg.getJMSMessageID());
			Object xmlEvent = ((ObjectMessage)msg).getObject();				
			byte[] xes = (byte[])xmlEvent;	
			logger.debug("Performing ETL process...");
			etl.process(xes);
			logger.debug("ETL process performed.");
		} catch(JMSException jmsex) {	
			logger.error(jmsex.fillInStackTrace());				
		} catch(Exception ex) {	
			logger.error(ex.fillInStackTrace());	
		}
	}
}