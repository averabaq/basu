/* 
 * $Id: TranslatorImpl.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.bpeql;

import es.uc3m.softlab.cbi4api.basu.bpeql.BPEQLParser.stmt_return;

import javax.annotation.PostConstruct;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Translator service implementation for parsing and translating BPEQL
 * queries into HiveQL queries.
 * 
 * @author averab
 * @version 1.0.0
 */
@Service(value=Translator.SERVICE_NAME)
public class TranslatorImpl implements Translator {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(TranslatorImpl.class);  
	/** BPEQL lexer */
	private BPEQLLexer lexer;

	/**
	 * Initializes the translator parsing resources.
	 */
	@PostConstruct
	public void init() {
		/* instance a new lexer */
		lexer = new BPEQLLexer();
	}
    /**
     * Translate the BPEQL statement passed by arguments into an HiveQL query.
     * 
     * @param statement BPEQL statement to translate.
     * @return statement translated into HiveQL format.
     * @throws BPEQLException if any generic error associated to the BPEQL module occurred.
     */
	public String translate(String statement) throws BPEQLException {
		try {			
			// First create a file stream using the supplied string source
			// and tell the lexer that that is the character source.
			lexer.setCharStream(new ANTLRStringStream(statement));

			// Using the lexer as the token source, we create a token
			// stream to be consumed by the parser
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			// Now we need an instance of our parser
			BPEQLParser parser = new BPEQLParser(tokens);

			logger.debug("BPEQL statement: " + statement);

			// Provide some user feedback
			logger.debug("    Lexer Start");
			long start = System.currentTimeMillis();

			// Force token load and lex (don't do this normally,
			// it is just for timing the lexer)
			tokens.LT(1);
			long lexerStop = System.currentTimeMillis();
			logger.debug("      lexed in " + (lexerStop - start) + "ms.");

			// And now we merely invoke the start rule for the parser
			logger.debug("    Parser Start");
			long pStart = System.currentTimeMillis();
			stmt_return psrReturn = parser.stmt();			
			long stop = System.currentTimeMillis();
			logger.debug("      Parsed in " + (stop - pStart) + "ms.");

			// If we got a valid a tree (the syntactic validity of the source
			// code was found to be solid), then let's print the tree to show we
			// did something; our testing public wants to know!
			// Pick up the generic tree
			Tree t = (Tree) psrReturn.getTree();

			// Now walk it with the generic tree walker, which does nothing but
			// verify the tree really.
			if (parser.getNumberOfSyntaxErrors() == 0) {
				BPEQLTree walker = new BPEQLTree(new CommonTreeNodeStream(t));
				logger.debug("    AST Walk Start\n");
				pStart = System.currentTimeMillis();
				walker.stmt();
				stop = System.currentTimeMillis();
				logger.debug("\n      AST Walked in " + (stop - pStart) + "ms.");
				logger.info("Query [ " + statement + " ] parsed and translated successfully.");
				return walker.getHiveQL().toString();
			} else {			
				throw new BPEQLException("Error: " + parser.getErrors());
			}			
		} catch (Exception ex) {
			// Something went wrong in the parser, report this
			logger.error("Parser threw an exception: \n\n" + ex.getLocalizedMessage());
			logger.error(ex.fillInStackTrace());
			throw new BPEQLException(ex.getLocalizedMessage());
		}
	}
}