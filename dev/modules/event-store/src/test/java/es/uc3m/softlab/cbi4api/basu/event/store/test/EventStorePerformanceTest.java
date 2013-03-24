/* 
 * $Id: EventStoreTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ActivityModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.ProcessModel;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.basu.event.store.domain.State;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.EventFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ModelFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.ProcessInstanceFacade;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.SourceException;
import es.uc3m.softlab.cbi4api.basu.event.store.facade.SourceFacade;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class EventStorePerformanceTest extends AbstractShowcaseTest {
	/** Log for tracing */
    private static final Logger logger = Logger.getLogger(EventStoreTest.class);
    /** concurrency degree */
	private static final int CONCURRENCY_DEGREE = 3;
    /** subprocess span level */
	private static final int SUBPROCESS_SPAN_LEVEL = 5;	
	/** number of events per instance */
	private static final int EVENTS_PER_INSTANCE = 8;
	/** Log for tracing */
    private static final String STATS_FILE = "/var/cbi4api/basu/tmp/stats-" + CONCURRENCY_DEGREE + "-degree.csv";
    /** Statistic file */
    protected static PrintWriter writer;
	/** Number of event records to store in this test */
	private static final int TEST_CASE_EVENT_NUM = 1;
	/** Event session facade */
	@Autowired private EventFacade eventFacade;
	/** Source session facade */
	@Autowired private SourceFacade sourceFacade;
	/** Model session facade */
	@Autowired private ModelFacade modelFacade;
	/** Process instance session facade */
	@Autowired private ProcessInstanceFacade processInstanceFacade;
	/** Activity instance session facade */
	@Autowired private ActivityInstanceFacade activityInstanceFacade;
	
	/**
	 * Tests the creation and storing of events.
	 * @throws SourceException if any source access error occurred.
	 * @throws ModelException if any model access error occurred.
	 * @throws ProcessInstanceException if any process instance access error occurred.
	 * @throws EventException if any event access error occurred.
	 */
	@Test
	@Rollback(false)
	public void testPerformance() throws SourceException, ModelException, ProcessInstanceException, EventException {
		ArrayList<Source> sources = new ArrayList<Source>();
		for (int i = 0; i < CONCURRENCY_DEGREE; i++) {
			Source source = new Source();
			source.setId("ApacheODE(" + i + ")-192.168.1.1" + i);
			source.setName("ApacheODE");
			source.setDescription("BPEL engine");
			source.setInetAddress("192.168.1.1" + i);
			source.setPort(8080);
			sources.add(source);
			sourceFacade.saveSource(source);			
		}
		long modelId = (long)(Math.random() * 1000);
		//logger.fatal("Model " + modelId);
		
		/* Simulation of one global process per source */
		ArrayList<ProcessModel> processModels = new ArrayList<ProcessModel>();
		for (int i = 0; i < CONCURRENCY_DEGREE; i++) {
			ProcessModel processModel = new ProcessModel();	
			processModel.setModelSrcId(String.valueOf(++modelId));
			processModel.setName("Process model [" + String.format("%03d", Long.valueOf(processModel.getModelSrcId())) + "]");		
			processModel.setType(ModelType.PROCESS);
			processModel.setSource(sources.get(i));
			processModels.add(processModel);
			//logger.fatal("Creating process model " + processModel.getModelSrcId());
			modelFacade.saveModel(processModel);
		}
		//logger.fatal("model for activity " + modelId);
		/* Simulation of one global process per source */
		ArrayList<ActivityModel> activityModels = new ArrayList<ActivityModel>();
		for (int i = 0; i < CONCURRENCY_DEGREE; i++) {
			for (int j = 0; j < SUBPROCESS_SPAN_LEVEL; j++) {
				ActivityModel activityModel = new ActivityModel();
				activityModel.setModelSrcId(String.valueOf(++modelId));
				activityModel.setName("Activity model [" + String.format("%03d", Long.valueOf(activityModel.getModelSrcId())) + "]");				
				activityModel.setType(ModelType.ACTIVITY);
				activityModel.setSource(sources.get(i));
				activityModels.add(activityModel);
				//logger.fatal("Creating activity model " + activityModel.getModelSrcId());
				modelFacade.saveModel(activityModel);
			}
		}
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(STATS_FILE)));
		} catch (IOException ex) {			
			logger.fatal(ex.fillInStackTrace());
		}

		long pInstanceId = (long)(Math.random() * 10000);
		long aInstanceId = (long)(Math.random() * 10000);
		for (int i = 0; i < TEST_CASE_EVENT_NUM; i++) {				
			for (int j = 0; j < CONCURRENCY_DEGREE; j++) {
				ProcessInstance processInstance = new ProcessInstance();
				processInstance.setInstanceSrcId(String.valueOf(pInstanceId++));
				processInstance.setModel(processModels.get(j));
				//logger.fatal("Assignin process model: " + processInstance.getModel().getModelSrcId());
				processInstance.setCorrelatorId((long)(Math.random() * 10000));
				//logger.fatal("Assignin process instance: " + String.format("%05d", Long.valueOf(processInstance.getInstanceSrcId())));
				processInstance.setName("Process instance [" + String.format("%05d", Long.valueOf(processInstance.getInstanceSrcId())) + "]");
				for (int k = 0; k < SUBPROCESS_SPAN_LEVEL; k++) {
					ActivityInstance activityInstance = new ActivityInstance();
					activityInstance.setInstanceSrcId(String.valueOf(aInstanceId++));
					activityInstance.setModel(activityModels.get((j * SUBPROCESS_SPAN_LEVEL) + k));	
					//logger.fatal("Assignin activity instance: " + String.format("%05d", Long.valueOf(activityInstance.getInstanceSrcId())));
					activityInstance.setName("Activity instance [" + String.format("%05d", Long.valueOf(activityInstance.getInstanceSrcId())) + "]");
					for (int l = 0; l < EVENTS_PER_INSTANCE; l++) {			
						(new EventGenerator(eventFacade, processInstance, activityInstance)).run();		
					}
				}
			}

		}
	}
	/**
	 * Test events retrieval methods.
	 * @throws EventException if any event access error occurred.
	 */
	@Test
	@Rollback(false)
	public void testFindEvents() throws EventException {	
		logger.fatal("=======================================================");
		/*try {
		List<Source> sources = sourceFacade.getAll();
		for (Source source : sources) {
			logger.fatal(source);
		}			
		List<Event> events = eventFacade.getAll();
		for (Event event : events) {
			logger.fatal(event);
		}
		List<ProcessInstance> processInstances = processInstanceFacade.getAll();
		for (ProcessInstance instance : processInstances) {
			logger.fatal(instance);
		}
		List<ActivityInstance> activityInstances = activityInstanceFacade.getAll();
		for (ActivityInstance instance : activityInstances) {
			logger.fatal(instance);
		}				
		List<Model> models = modelFacade.getAll();
		for (Model model : models) {
			logger.fatal(model);
		}		
		} catch(Exception ex) {
			logger.error(ex.fillInStackTrace());
			ex.printStackTrace();
		} finally {
			logger.fatal("=======================================================");
		}*/
		
	}
}