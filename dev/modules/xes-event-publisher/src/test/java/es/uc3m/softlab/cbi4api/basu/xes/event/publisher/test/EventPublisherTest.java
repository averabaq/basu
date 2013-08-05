/* 
 * $Id: EventPublisherTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.test;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.Config;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.ETLEvent;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.XESEventWriter;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.broker.BrokerService;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class EventPublisherTest extends AbstractShowcaseTest {
	/** ActiveMQ broker service */
	private BrokerService broker = new BrokerService();
	/** Configuration object */
	@Autowired private Config config;
	/** Event reader service */
	@Autowired private ETLEvent etl;
	/** Event sender to publish the data to the jms queue */
	@Autowired private XESEventWriter sender;
	/** JMS template */
	@Autowired private JmsTemplate jmsTemplate;
	/** JMS message XES queue/topic destination */
	@Autowired private Topic xesDestination;
	/** JMS message BPAF queue/topic destination */
	@Autowired private Topic bpafDestination;
	
	@Before
	public void setUp() {
		try {			
			/* configure the broker */
			broker.addConnector(config.getBrokerUrl());
			broker.setBrokerName("Broker1");
			broker.setUseJmx(false);
			broker.setPersistent(false);
			broker.start();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
    
    @Test
    public void testMessagePublisher() throws Throwable {    
		List<Event> events = new ArrayList<Event>();
		for (int i = 0; i < 10 ; i++) {
			Event event = new Event();
			event.setActivityDefinitionID(UUID.randomUUID().toString());
			event.setActivityInstanceID(UUID.randomUUID().toString());
			event.setActivityName(UUID.randomUUID().toString());
			event.setEventID(String.valueOf((long)(Math.random() * 10000)));
			event.setProcessDefinitionID(UUID.randomUUID().toString());
			event.setProcessInstanceID(UUID.randomUUID().toString());
			event.setProcessName(UUID.randomUUID().toString());
			event.setServerID(UUID.randomUUID().toString());
			Calendar tstamp = Calendar.getInstance();
			tstamp.setTime(new Date());
			event.setTimestamp(tstamp);
			events.add(event);
		}
		sender.sendEvents(events);
    }   
    
    @Test
    public void testETLProcess() throws Throwable {
    	final byte[] xes = IOUtils.toByteArray(getClass().getResourceAsStream("/event-logs/Hospital_log.xes"));
		logger.info("Performing ETL process...");
		etl.process(xes);
		logger.info("ETL process performed.");
    }
    
    @Test
    public void testXESMessagePublisher() throws Throwable {     
    	final byte[] xes = IOUtils.toByteArray(getClass().getResourceAsStream("/event-logs/BPI_Challenge_2012.xes"));
     	jmsTemplate.send(xesDestination, new MessageCreator() {
			public Message createMessage(Session session)
			throws JMSException {
				logger.info("Sending XES log event...");
				ObjectMessage message = session.createObjectMessage(xes);
				//message.setJMSReplyTo(arg0);
				return message;
			}
		});
    }
    
	@After
	public void tearDown() {
		try {						
			broker.stop();
		} catch(Exception ex) {
			ex.printStackTrace();			
		}
	}	
}
