/* 
 * $Id: BpelEventDAOConnectionFactoryImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao;

import org.apache.log4j.Logger;
import org.apache.ode.dao.jpa.JpaTxMgrProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import es.uc3m.softlab.cbi4api.basu.ode.event.publisher.Config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.util.HashMap;

/**
 * Represents the physical resource for creating connections to the bpel event store.
 * This <i>dao</i> implementation provides the essential functionality to access 
 * event data by the means of the 
 * <a href="http://ode.apache.org/bpel-management-api-specification.html">ApacheODE API</a>.
 * 
 * For further information about the usage of this package visit the project web site 
 * at the <a href="http://ode.apache.org">ApacheODE Project</a> from the apache software 
 * foundation. 
 * 
 * This interface defines all methods for accessing to the ApacheODE object data 
 * throughout the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
@Repository(value=BpelEventDAOConnectionFactory.COMPONENT_NAME)
@Scope(value=BeanDefinition.SCOPE_SINGLETON)
public class BpelEventDAOConnectionFactoryImpl implements BpelEventDAOConnectionFactory {
	/** Logger for tracing */
	private transient final Logger logger = Logger.getLogger(BpelEventDAOConnectionFactoryImpl.class);
	/** Entity manager factory bound to this object */
    private EntityManagerFactory entityManagerFactory;
    /** Configuration object */
    @Autowired private Config config;	
    /** Transaction manager bound to this object */
    @Autowired private TransactionManager transactionManager;
    /** Data source bound */
    @Autowired private DataSource dataSource;
    /** Open JPA data base dictionary object */
    private Object dbdictionary;
    /** Bpel Event connections to this data access object */
    private static ThreadLocal<BpelEventDAOConnectionImpl> connections = new ThreadLocal<BpelEventDAOConnectionImpl>();

    /**
     * Constructor of this singleton class.
     */
    public BpelEventDAOConnectionFactoryImpl() {
    	/* 
    	 * The intialization of the factory is carried out from the container.
    	 *    init();
    	 */
    }
    /**
     * Gets a new {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnection} 
     * data access object.
     * @return new {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnection} dao object.
     */
    public BpelEventDAOConnection getConnection() {
        try {
        	logger.debug("Getting connection...");
        	transactionManager.getTransaction().registerSynchronization(new Synchronization() {
                // OpenJPA allows cross-transaction entity managers, which we don't want
                public void afterCompletion(int i) {
                    if (connections.get() != null)
                        connections.get().getEntityManager().close();
                    connections.set(null);
                }
                public void beforeCompletion() { }
            });
        } catch (RollbackException e) {
            throw new RuntimeException("Coulnd't register synchronizer!");
        } catch (SystemException e) {
            throw new RuntimeException("Coulnd't register synchronizer!");
        }
        if (connections.get() != null) {
            return connections.get();
        } else {
            HashMap<String, String> propMap2 = new HashMap<String, String>();
            propMap2.put("openjpa.TransactionMode", "managed");
            EntityManager em = entityManagerFactory.createEntityManager(propMap2);
            BpelEventDAOConnectionImpl conn = createBpelEventDAOConnection(em);
            connections.set(conn);
            return conn;
        }
    }
    /**
     * Creates a new {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnectionImpl} object.
     * @param em entity manager to attach to the dao object.
     * @return new {@link es.uc3m.softlab.cbi4api.basu.ode.event.publisher.dao.BpelEventDAOConnectionImpl} object.
     */
    private BpelEventDAOConnectionImpl createBpelEventDAOConnection(EntityManager em) {
    	BpelEventDAOConnectionImpl dao = null;
    	String maxResultStr = config.getString("f4bpa.ode.event.publisher.jpa.bpel.events.query.max.results");
    	if (maxResultStr != null) {
			try {
				Integer frame = Integer.valueOf(maxResultStr);
				dao = new BpelEventDAOConnectionImpl(em, frame);
			} catch (NumberFormatException nfex) {
				logger.warn("The property 'f4bpa.ode.event.publisher.jpa.bpel.events.query.max.results' is not correctly set an expected number. Please verify this value.");
			}
		} else {
			dao = new BpelEventDAOConnectionImpl(em);
		}    	    	
    	return dao;
    }
    /**
     * Initializes the factory for handling connection to dao objects.
     */
    public void init() {
        HashMap<String, Object> propMap = new HashMap<String,Object>();

        /* propMap.put("openjpa.Log", "DefaultLevel=TRACE"); */
        propMap.put("openjpa.Log", "commons");
        /* propMap.put("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.DerbyDictionary"); */

        propMap.put("openjpa.ManagedRuntime", new JpaTxMgrProvider(transactionManager));
        propMap.put("openjpa.ConnectionFactory", dataSource);
        propMap.put("openjpa.ConnectionFactoryMode", "managed");
        /* propMap.put("openjpa.FlushBeforeQueries", "false"); */
        propMap.put("openjpa.FetchBatchSize", 1000);
        propMap.put("openjpa.jdbc.TransactionIsolation", "read-committed");

        if (dbdictionary != null)
            propMap.put("openjpa.jdbc.DBDictionary", dbdictionary);

        entityManagerFactory = Persistence.createEntityManagerFactory("ode-dao", propMap);
    }
	/**
	 * Sets the {@link #transactionManager} property.
	 * @param transactionManager the {@link #transactionManager} property to set.
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	/**
	 * Gets the {@link #dataSource} property.
	 * @return the {@link #dataSource} property.
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
	/**
	 * Sets the {@link #dataSource} property.
	 * @param dataSource the {@link #dataSource} property to set.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * Sets the {@link #dbdictionary} property.
	 * @param dbdictionary the {@link #dbdictionary} property to set.
	 */
	public void setDBDictionary(String dbdictionary) {
        this.dbdictionary = dbdictionary;
    }
    /**
     * Shutdown data access object factory.
     */
    public void shutdown() {
    	entityManagerFactory.close();
    }
}
