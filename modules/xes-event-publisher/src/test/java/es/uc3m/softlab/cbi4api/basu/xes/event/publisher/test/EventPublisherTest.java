/* 
 * $Id: EventPublisherTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.test;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.ETLProcessor;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.XESEventConverter;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.XESEventReader;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.XESEventWriter;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Correlation;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.LogType;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class EventPublisherTest extends AbstractShowcaseTest {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(EventPublisherTest.class);
	/** Configuration object */
	@Autowired private CamelContext context;
	/** ETL (Extract, Transform & Load) processor */
	@Autowired private ETLProcessor etl;
	/** ETL XES Extractor */
	@Autowired private XESEventReader extractor;
	/** ETL XES Loader */
	@Autowired private XESEventWriter loader;
	/** ETL XES Transformer */
	@Autowired private XESEventConverter transformer;	
	/** Mock source XES etl endpoint */
    @EndpointInject(uri = "mock:xesSource")
    protected MockEndpoint sourceEndpoint;
    /** Mock target XES etl endpoint */
    @EndpointInject(uri = "mock:bpafTarget")
    protected MockEndpoint targetEndpoint;	
    
	@Before
	public void setUp() {
	}	
	
	@Test
	@DirtiesContext      
    public void testETLProcess() throws Throwable {
    	final byte[] xes = IOUtils.toByteArray(getClass().getResourceAsStream("/event-logs/BPI_Challenge_2012-test.xes"));
		logger.info("Performing ETL process...");		
		DefaultExchange exchange = new DefaultExchange(sourceEndpoint);
		exchange.getIn().setBody(xes);
		etl.process(exchange);
		logger.info("ETL process performed.");		
		sourceEndpoint.assertIsSatisfied();
    }
    
	@Test
	@DirtiesContext 
    public void testXESExtractor() throws Throwable { 
    	final byte[] xes = IOUtils.toByteArray(getClass().getResourceAsStream("/event-logs/Hospital_log-test.xes"));
    	DefaultExchange exchange = new DefaultExchange(sourceEndpoint);
    	exchange.getIn().setBody(xes);
    	LogType _xes = extractor.extractEvents(exchange);
    	assert (_xes != null && _xes.isSetTrace());
    }
	
	@Test
	@DirtiesContext 
    public void testXESConverter() throws Throwable {
    	final byte[] xes = IOUtils.toByteArray(getClass().getResourceAsStream("/event-logs/Hospital_log-test.xes"));
    	DefaultExchange exchange = new DefaultExchange(sourceEndpoint);
    	exchange.getIn().setBody(xes);
    	LogType _xes = extractor.extractEvents(exchange);   	
    	assert (_xes == null || _xes.isSetTrace());
    	List<Event> bpafEvents = transformer.transform(_xes);
		assert (bpafEvents != null && !bpafEvents.isEmpty());		
    }
	
	@Test
    public void testXESLoader() throws Throwable {
		List<Event> events = new ArrayList<Event>();
		for (int i = 0; i < 25 ; i++) {
			Event event = new Event();
			event.setCorrelation(new Correlation());
			event.getCorrelation().setProcessInstanceID(UUID.randomUUID().toString());
			event.setActivityDefinitionID(UUID.randomUUID().toString());
			event.setActivityInstanceID(UUID.randomUUID().toString());
			event.setActivityName(UUID.randomUUID().toString());
			event.setEventID(String.valueOf((long)(Math.random() * 10000)));
			event.setProcessDefinitionID(UUID.randomUUID().toString());			
			event.setProcessName(UUID.randomUUID().toString());
			event.setServerID(UUID.randomUUID().toString());
			Calendar tstamp = Calendar.getInstance();
			tstamp.setTime(new Date());
			event.setTimestamp(tstamp);
			events.add(event);
		}
		DefaultExchange exchange = new DefaultExchange(targetEndpoint);
		Assert.assertNotNull(loader);
		loader.sendEvents(exchange, events);
        // assert mock received the body
		targetEndpoint.assertIsSatisfied();
    }
    
	@Test
	public void testBPAFConsumer() throws Throwable {
		Endpoint endpoint = context.getEndpoint("basu-activemq:queue:es.uc3m.softlab.cbi4api.basu.event.bpaf");
		PollingConsumer consumer = endpoint.createPollingConsumer();
		Exchange exchange = consumer.receive(5000);		
		assert (exchange != null && exchange.getIn() != null);
		byte[] xml = (byte[])exchange.getIn().getBody();
		assert (xml != null);
		logger.debug(xml);
		ByteArrayInputStream bios = new ByteArrayInputStream(xml);
		/* create a JAXBContext capable of handling classes */ 
		JAXBContext jc = JAXBContext.newInstance("es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event");			
		/* create an Unmarshaller */
		Unmarshaller u = jc.createUnmarshaller();        					
		/* unmarshal an instance document into a tree of Java content objects. */			
		Event event = (Event) u.unmarshal(bios);
		logger.debug(event);
		assert (event != null);
	}
	  
	@After
	public void tearDown() {
	}	
}
