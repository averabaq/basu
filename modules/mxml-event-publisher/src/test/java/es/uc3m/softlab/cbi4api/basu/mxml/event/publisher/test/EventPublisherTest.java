/* 
 * $Id: EventPublisherTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.test;

import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.EventSender;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.mxml.AuditTrailEntry;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.mxml.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.mxml.WorkflowLog;
import es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.mxml.Process;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.broker.BrokerService;
import org.apache.commons.io.IOUtils;
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
    
    @Test
    public void testMXMLMessagePublisher() throws Throwable {      	
    	byte[] mxml = IOUtils.toByteArray(getClass().getResourceAsStream("/event-logs/hospital_log.mxml"));
		ByteArrayInputStream bais = new ByteArrayInputStream(mxml);
		/* create a JAXBContext capable of handling classes */ 
		JAXBContext jc = JAXBContext.newInstance("es.uc3m.softlab.cbi4api.basu.mxml.event.publisher.xsd.mxml");			
		/* create an Unmarshaller */
		Unmarshaller u = jc.createUnmarshaller();        			
		/* Performs an xml validation against the MXML schema 
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(getClass().getResource("/xsd/mxml.xsd"));
		u.setSchema(schema);*/			
		/* unmarshal an instance document into a tree of Java content objects. */			
		WorkflowLog log = (WorkflowLog) u.unmarshal(bais);
		
		System.out.println("Unmarshalled MXML sucessfully: " + log.getSource().getProgram());
		logger.debug("Unmarshalled MXML file sucessfully: " + log);
		for (Process process : log.getProcess()) {
			System.out.println("Process: " + process.getId() + " [" + process.getDescription() + "]");
			for (ProcessInstance instance : process.getProcessInstance()) {
				System.out.println("     Instance: " + instance.getId() + " [" + process.getDescription() + "]");	
				for (AuditTrailEntry auditEntry : instance.getAuditTrailEntry()) {
					System.out.println("          Audit: " + auditEntry.getEventType().getValue() + " [" + auditEntry.getWorkflowModelElement() + "] (" + auditEntry.getOriginator() + ")");
				}
			}
		}
		bais.close();
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
