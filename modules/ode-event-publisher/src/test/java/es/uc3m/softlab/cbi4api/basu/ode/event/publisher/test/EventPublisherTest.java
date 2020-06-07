/* 
 * $Id: EventPublisherTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher.test;

import es.uc3m.softlab.cbi4api.basu.ode.event.publisher.EventSender;
import es.uc3m.softlab.cbi4api.basu.ode.event.publisher.xsd.bpaf.extension.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class EventPublisherTest extends AbstractShowcaseTest {
	/** ActiveMQ broker service */
	private BrokerService broker = new BrokerService();
	/** Event sender to publish the data to the jms queue */
	@Autowired private EventSender sender;	
	
	@Before
	public void setUp() {
		try {			
			/* configure the broker */
			broker.addConnector("tcp://localhost:61616");
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
		sender.publishEvents(events);
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
