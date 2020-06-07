/* 
 * $Id: StaticResources.java,v 1.0 2011-09-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.bpeql;

/**
 * In this class is defined all global and generic static 
 * resources of the application.
 * 
 * @author averab
 */
public class StaticResources {
	/** Internationalization resource bundle */
	public static final String CONFIG_RESOURCE_BUNDLE="";	
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";
    /** Empty string representation */
    public static final String STRING_EMPTY="";

	/** Service name for the data BPEQL translator module */
	public static final String SERVICE_NAME_BPEQL_ENGINE = "/f4bpa/bpeql/service/bpeqlEngine";
	/** Service name for the data BPEQL translator module */
	public static final String SERVICE_NAME_TRANSLATOR = "/f4bpa/bpeql/service/translator";
}
