/* 
 * $Id: EventSubscriberTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.subscriber.test;

import es.uc3m.softlab.cbi4pi.slave.event.subscriber.EventReader;
import es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event;
import es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event.EventDetails;
import es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.State;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class EventSubscriberTest extends AbstractShowcaseTest  {
	/** Log for tracing */
    private static final Logger logger = Logger.getLogger(EventSubscriberTest.class);
	/** ActiveMQ broker service */
	private BrokerService broker = new BrokerService();
    /** Event reader to extract the event data from the jms messages */
	@Autowired private EventReader extractor;
	
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
    public void testExtractor() throws Throwable {    
    	for (int i = 0; i < 10 ; i++) {
    		Event bpafEvent = new es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event();
    		bpafEvent.setActivityDefinitionID(UUID.randomUUID().toString());
    		bpafEvent.setActivityInstanceID(UUID.randomUUID().toString());
    		bpafEvent.setActivityName(UUID.randomUUID().toString());
    		bpafEvent.setEventID(String.valueOf((long)(Math.random() * 10000)));
    		bpafEvent.setProcessDefinitionID(UUID.randomUUID().toString());
    		bpafEvent.setProcessInstanceID(UUID.randomUUID().toString());
    		bpafEvent.setProcessName(UUID.randomUUID().toString());
    		bpafEvent.setServerID(UUID.randomUUID().toString());
    		bpafEvent.setEventDetails(new EventDetails());
    		
			State currentState = State.values()[(int)(Math.random() * State.values().length)];
			State previousState = State.values()[(int)(Math.random() * State.values().length)];
			bpafEvent.getEventDetails().setCurrentState(currentState);
			bpafEvent.getEventDetails().setPreviousState(previousState);

    		Calendar tstamp = Calendar.getInstance();
    		tstamp.setTime(new Date());
    		bpafEvent.setTimestamp(tstamp);

    		try {    			
    			ByteArrayOutputStream baos = new ByteArrayOutputStream();
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  	    	
    			JAXBContext context = JAXBContext.newInstance(es.uc3m.softlab.cbi4pi.slave.event.subscriber.xsd.bpaf.extension.Event.class);
    			Marshaller marshaller = context.createMarshaller();
    			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    			logger.debug("Marshalling event " + bpafEvent + "...");
    			marshaller.marshal(bpafEvent, writer);
    			writer.close();
    			baos.close();
    			final byte[] xml = baos.toByteArray();	
    			logger.info("Extracting xml event: " + new String(xml));
    			
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
				Connection connection = connectionFactory.createConnection();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				  
				Message msg = session.createObjectMessage(xml);
				Event bpafExtractedEvent = extractor.extractEvent(msg);
    			Assert.assertNotNull(bpafExtractedEvent);
    			
    		} catch(JAXBException jaxbex) {
    			logger.error("Cannot send event " + bpafEvent + " due to a JAXB marshalling error.");
    			logger.error(jaxbex.fillInStackTrace());
    			throw new Exception(jaxbex.getLocalizedMessage());
    		} catch(IOException ioex) {
    			logger.error("Cannot send event " + bpafEvent + " due to an input/out error marshalling the event.");
    			logger.error(ioex.fillInStackTrace());
    			throw new Exception(ioex.getLocalizedMessage());
    		}
    	}
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