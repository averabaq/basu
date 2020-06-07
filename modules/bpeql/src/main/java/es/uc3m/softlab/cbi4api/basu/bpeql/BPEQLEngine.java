/* 
 * $Id: BPEQLEngine.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.bpeql;

import es.uc3m.softlab.cbi4api.basu.event.dw.dto.EventFactResultDTO;

import java.util.List;

/**
 * Business Process Execution Query Language (BPEQL) service engine implementation 
 * for parsing and translating BPEQL queries into HiveQL queries. This class also
 * provides the functionality for executing the HiveQL queries that retrieve the
 * execution data related a determined business process information.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface BPEQLEngine {
 	/** Spring component name */
    public static final String SERVICE_NAME = StaticResources.SERVICE_NAME_BPEQL_ENGINE;        

    /**
     * Process a BPEQL statement passed by arguments and it is translated it into an HiveQL query,
     * that is later executed and whereby its result are returned.
     * 
     * @param statement BPEQL statement to process.
     * @return statement translated into HiveQL format.
     * @throws BPEQLException if any generic error associated to the BPEQL module occurred.
     */
	public List<EventFactResultDTO> process(String statement) throws BPEQLException;
    /**
     * Translate a BPEQL statement passed by arguments into an HiveQL query.
     * 
     * @param statement BPEQL statement to translate.
     * @return statement translated into HiveQL format.
     * @throws BPEQLException if any generic error associated to the BPEQL module occurred.
     */
	public String translate(String statement) throws BPEQLException;
}