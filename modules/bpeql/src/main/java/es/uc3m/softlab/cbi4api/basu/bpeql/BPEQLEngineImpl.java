/* 
 * $Id: BPEQLEngineImpl.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.bpeql;

import java.util.List;

import es.uc3m.softlab.cbi4api.basu.event.dw.dto.EventFactResultDTO;
import es.uc3m.softlab.cbi4api.basu.event.dw.facade.EventFactException;
import es.uc3m.softlab.cbi4api.basu.event.dw.facade.EventFactFacade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business Process Execution Query Language (BPEQL) service engine implementation 
 * for parsing and translating BPEQL queries into HiveQL queries. This class also
 * provides the functionality for executing the HiveQL queries that retrieve the
 * execution data related a determined business process information.
 * 
 * @author averab
 * @version 1.0.0
 */
@Service(value=BPEQLEngine.SERVICE_NAME)
public class BPEQLEngineImpl implements BPEQLEngine {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(BPEQLEngineImpl.class);  
	/** BPEQL translator */
	@Autowired private Translator translator;
	/** Event fact service facade */
	@Autowired private EventFactFacade eventFactFacade;

    /**
     * Process a BPEQL statement passed by arguments and it is translated it into an HiveQL query,
     * that is later executed and whereby its results are returned.
     * 
     * @param statement BPEQL statement to process.
     * @return statement translated into HiveQL format.
     * @throws BPEQLException if any generic error associated to the BPEQL module occurred.
     */
	public List<EventFactResultDTO> process(String statement) throws BPEQLException {
		logger.info("Trying to process BPEQL query: [ " + statement + " ]...");
		String hiveql = translator.translate(statement);
		logger.info("BPEQL query translated successfully into HiveQL: [" + hiveql + "].");
		try {
			List<EventFactResultDTO> tuples = eventFactFacade.executeQuery(hiveql);
			logger.info("BPEQL query processed successfully.");
			return tuples;
		} catch (EventFactException efex) {
			logger.error("Error executing BPEQL query: " + efex.getLocalizedMessage());
			throw new BPEQLException("Error executing BPEQL query: " + efex.getLocalizedMessage());
		}		
	}
    /**
     * Translate a BPEQL statement passed by arguments into an HiveQL query.
     * 
     * @param statement BPEQL statement to translate.
     * @return statement translated into HiveQL format.
     * @throws BPEQLException if any generic error associated to the BPEQL module occurred.
     */
	public String translate(String statement) throws BPEQLException {
		logger.info("Trying to translate BPEQL query: [ " + statement + " ]...");
		String hiveql = translator.translate(statement);
		logger.info("BPEQL query translated successfully into HiveQL: [" + hiveql + "].");
		return hiveql;
	}	
}