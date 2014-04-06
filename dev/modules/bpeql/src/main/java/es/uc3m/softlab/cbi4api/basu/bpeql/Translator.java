/* 
 * $Id: Translator.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.bpeql;

/**
 * Translator service interface for parsing and translating BPEQL
 * queries into HiveQL queries.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface Translator {
 	/** Spring component name */
    public static final String SERVICE_NAME = StaticResources.SERVICE_NAME_TRANSLATOR;        

    /**
     * Translate the BPEQL statement passed by arguments into an HiveQL query.
     * 
     * @param statement BPEQL statement to translate.
     * @return statement translated into HiveQL format.
     * @throws BPEQLException if any generic error associated to the BPEQL module occurred.
     */
	public String translate(String statement) throws BPEQLException;
}