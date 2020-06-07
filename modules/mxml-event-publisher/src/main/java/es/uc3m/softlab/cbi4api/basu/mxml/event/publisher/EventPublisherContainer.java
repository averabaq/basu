/* 
 * $Id: EventPublisherContainer.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.mxml.event.publisher;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=StaticResources.COMPONENT_NAME_EVENT_PUBLISHER_CONTAINER)
public class EventPublisherContainer implements InitializingBean, DisposableBean, BeanFactoryAware, ApplicationContextAware {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventPublisherContainer.class);
	/** Spring bean factory */
	private BeanFactory beanFactory;
	/** Spring application context */
	private ApplicationContext applicationContext;
	/** Runnable object to fire to shut down */
	private Runnable onShutDown;
	/** Container block flag semaphore */
	private Semaphore block = new Semaphore(0);
	/** States if the container is initialized */
	private AtomicBoolean containerInitialized = new AtomicBoolean(false);
	/** States if the container is started */
	private AtomicBoolean started = new AtomicBoolean(false);
	/** States if the container is flagged to use shutdown hook */
	private boolean useShutdownHook = true;
	/** Shutdown hook thread */
	protected transient Thread shutdownHook;

    /**
     * Gets the container description.
     * @return container description.
     */
    public String getDescription() {
        return "Event Publisher (Apache ODE) Container";
    }
	/**
     * @throws Exception 
     */
	public void afterPropertiesSet() throws Exception {
		init();
		start();
	}
    /**
     * light weight initialization - default values are null.
     * @throws EventPublisherException if any event publisher error occurred.
     */
    public void init() throws EventPublisherException {
        if (containerInitialized.compareAndSet(false, true)) {
            logger.info("F4BPA Event Publisher Container is starting...");
            addShutdownHook();
        }
    }		
	/**
     * Stop event processing.
     * @throws EventPublisherException if any event publisher error occurred.
     */
    public void start() throws EventPublisherException {
        checkInitialized();
        if (started.compareAndSet(false, true)) {
        	
        }
        logger.info("Event Publisher (Apache ODE) Container started.");
    }
    /**
     * Stop event processing.
     * @throws EventPublisherException if any event publisher error occurred.
     */
	public void stop() throws EventPublisherException {
		if (beanFactory instanceof DisposableBean) {
			DisposableBean disposable = (DisposableBean) beanFactory;
			try {
				disposable.destroy();
			} catch (Exception ex) {
				throw new EventPublisherException("Failed to dispose of the Spring BeanFactory due to: " + ex, ex);
			}
		}
        checkInitialized();
        if (started.compareAndSet(true, false)) {
        	
        }
	}	
	/**
	 * Destroys the container instance and releases the block.
	 * @throws Exception if any error arises during container destruction.
	 */
	public void destroy() throws Exception {
		shutDown();
		block.release();
	}
	/**
	 * Shutdowns the container.
	 * @throws EventPublisherException if any error occurred during shutting down.
	 */
	public void shutDown() throws EventPublisherException {
		if (onShutDown != null) {
			onShutDown.run();
		} else {
			// no shutdown handler has been set
			// shutting down the container ourselves
	        if (containerInitialized.compareAndSet(true, false)) {
	            logger.info("Shutting down Event Publisher (Apache ODE) Container ({})...");
	            removeShutdownHook();
	            
	        }
		}
	}
	/**
	 * Checks of the container is initialized.
	 * @throws EventPublisherException if the container is not initialized.
	 */
	protected void checkInitialized() throws EventPublisherException {
		if (!containerInitialized.get()) {
			throw new EventPublisherException("The Event Publisher Container is not initialized - please call init(...)");
		}
	}
	/**
     * Adds a shutdown hook.
     */
    protected void addShutdownHook() {
        if (useShutdownHook) {
            shutdownHook = new Thread("Event Publisher Container (Apache ODE) ShutdownHook") {
                public void run() {
                    containerShutdown();
                }
            };
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        }
    }
    /**
     * Removes the shutdown hook.
     */
    protected void removeShutdownHook() {
        if (shutdownHook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            } catch (Exception e) {
                logger.debug("Caught exception, must be shutting down: ", e);
            }
        }
    }
    /**
     * Causes a clean shutdown of the container when the VM 
     * is being shut down.
     */
    private void containerShutdown() {
        try {
            shutDown();
        } catch (Throwable e) {
            logger.error("Failed to shut down: " + e);
        }
    }
	/**
	 * Gets the {@link #applicationContext} property.
	 * @return the {@link #applicationContext} property.
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	/**
	 * Sets the {@link #applicationContext} property.
	 * @param applicationContext the {@link #applicationContext} property to set.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	/**
	 * Gets the {@link #beanFactory} property.
	 * @return the {@link #beanFactory} property.
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}
	/**
	 * Sets the {@link #beanFactory} property.
	 * @param beanFactory the {@link #beanFactory} property to set.
	 * @throws BeansException if any bean factory error occurred.
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void block() throws InterruptedException {
		block.acquire();
	}
	/**
	 * Set a {@link Runnable} which can handle the shutdown of the container.
	 * 
	 * @param runnable the shutdown handler
	 */
	public void onShutDown(Runnable runnable) {
		this.onShutDown = runnable;
	}
}