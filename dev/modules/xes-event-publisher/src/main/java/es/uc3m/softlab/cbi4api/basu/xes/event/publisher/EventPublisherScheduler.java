/* 
 * $Id: EventPublisherScheduler.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * Scheduler class that fires the method which will publish the new events
 * to the jms queue/topic.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=StaticResources.COMPONENT_NAME_EVENT_SCHEDULER)
public class EventPublisherScheduler {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventPublisherScheduler.class);
    /** Configuration service object */
	@Autowired private Config config;
	/** JMS template */
	@Autowired private JmsTemplate jmsTemplate;
	/** JMS message XES queue/topic destination */
	@Autowired private Topic xesDestination;
	/** Event reader service */
	@Autowired private ETLEvent etl;
	
	/**
	 * Scheduled task that is run on a fixed rate basis.
	 */
	public void run() {		
		String path = config.getPullDir();
		if (path == null) {
			logger.error("Cannot find the XES pull import directory");
			throw new RuntimeException("Cannot find the XES pull import directory");	
		}
		File pull = new File(path); 
		if (!pull.exists()) {
			logger.error("The XES pull import path (" + path + ") does not exist.");
			throw new RuntimeException("The XES pull import path (" + path + ") does not exist.");					
		}
		if (!pull.isDirectory()) {
			logger.error("The XES pull import path (" + path + ") is not a valid directory.");
			throw new RuntimeException("The XES pull import path (" + path + ") is not a valid directory.");					
		}
		for (File file : pull.listFiles()) {
			FileInputStream fis = null;
			RandomAccessFile randomAccessfile = null;
			FileLock lock = null;
			try {
				logger.info("File found in the XES pull directory: " + file.getAbsolutePath());
				randomAccessfile = new RandomAccessFile(file, "rw");
				FileChannel fileChannel = randomAccessfile.getChannel();
			    // Try to get an exclusive lock on the file.
				lock = fileChannel.tryLock(0L, Long.MAX_VALUE, false);
			    if (lock == null) {
			    	logger.warn("File " + file.getAbsolutePath() + " is not available for locking. Please, make sure that the input file has writting permissions.");
			    }
				fis = new FileInputStream(file);
				final byte[] xes = IOUtils.toByteArray(fis);
				if (config.useJmsOnImportXESFiles()) {
					logger.info("Pushing file " + file.getAbsolutePath() + " into the XES jms queue...");
					jmsTemplate.send(xesDestination, new MessageCreator() {
						public Message createMessage(Session session) throws JMSException {
							logger.info("Sending XES log event...");
							ObjectMessage message = session.createObjectMessage(xes);
							//message.setJMSReplyTo(arg0);
							return message;
						}
					});
				} else {
					logger.debug("Performing ETL process...");
					etl.process(xes);
					logger.debug("ETL process performed.");					
				}
				// delete the file once it's processed
				file.delete();
			} catch (EventPublisherException epex) {
				logger.error(epex.fillInStackTrace());
			} catch (FileNotFoundException fnfex) {
				logger.error("Unexpected exception: " + fnfex.fillInStackTrace());
			} catch (IOException ioex) {
				logger.error(ioex.fillInStackTrace());
			} finally {
			    // always release the lock and close the file			    
			    if (lock != null && lock.isValid())
			    	try {
			    		lock.release();
			    	} catch (IOException ioex) {
						// ignore exception
						logger.warn(ioex.fillInStackTrace());
					}
			    // close the random access file along its file channel
				if (randomAccessfile != null) {
					try {
						randomAccessfile.close();
					} catch (IOException ioex) {
						// ignore exception
						logger.warn(ioex.fillInStackTrace());
					}					
				}
				// closes the file input stream
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException ioex) {
						// ignore exception
						logger.warn(ioex.fillInStackTrace());
					}	
				}
			}
		}
	}
}
