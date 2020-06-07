// This is a sample lexer generated by the ANTLR3 Maven Archetype
// generator. It shows how to use a superclass to implement
// support methods and variables you may use in the lexer actions
// rather than create a messy lexer that has the Java code embedded
// in it.
//

lexer grammar BPEQLLexer;

options {

   language=Java;  // Default

   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and 
   // variables will be placed.
   //
   superClass = AbstractTLexer;
}

// What package should the generated source exist in?
//
@header {

    package es.uc3m.softlab.cbi4api.basu.bpeql;
    
    import java.util.LinkedList;
}

@members {
    private List<String> errors = new LinkedList<String>();
    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        errors.add(hdr + " " + msg);
    }
    public String getErrors() {
        String msg = "";
        for (String error : errors) {
        	msg += error + "\n";
        }
        return msg;
    }
}

// This is just a simple lexer that matches the usual suspects
//

Left_Paren              
	:	 '(';
Right_Paren             
	:	 ')';
Plus_Sign               
	:	 '+';
Minus_Sign              
	:	 '-';
Equals_Operator         
	:	 '=';
Left_Brace              
	:	 '{';
Right_Brace             
	:	 '}';
Left_Bracket            
	:	 '[';
Right_Bracket           
	:	 ']';
Vertical_Bar            
	:	 '|';
Colon                   
	:	 ':';
Semicolon               
	:	 ';';
Double_Quote            
	:	 '"';
Asterisk                
	:	 '*';	
Quote                   
	:	 '\'';
Less_Than_Operator      
	:	 '<';
Greater_Than_Operator   
	:	 '>';
Comma                   
	:	 ',';
Period                  
	:	 '.';
Question_Mark           
	:	 '?';
Slash                   
	:	 '/';
	
//  Reserved Keyword Tokens

	ACTIVITY                    :   'ACTIVITY'         | 'activity';
	ABORTED                     :   'ABORTED'          | 'aborted';
	AGGREGATE                   :   'AGGREGATE'        | 'aggregate';	
	AND                         :   'AND'              | 'and';	
	CHANGEOVER		            :   'CHANGE_OVER'  	   | 'change_over';
	END_TIME	                :   'END_TIME' 		   | 'end_time';
	FAILED                      :   'FAILED'           | 'failed';
	FALSE                       :   'FALSE'            | 'false';
	FROM                        :   'FROM'             | 'from';
	ID							: 	'ID'			   | 'id';
	INSTANCE					: 	'INSTANCE'		   | 'instance';
	MAP							: 	'MAP'			   | 'map';	
	MODEL						: 	'MODEL'			   | 'model';
	NAME						: 	'NAME'			   | 'name';
    RUNNING                     :   'RUNNING'          | 'running';
	SELECT                      :   'SELECT'           | 'select';
	START_TIME                  :   'START_TIME'       | 'start_time';
	TRUE                        :   'TRUE'             | 'true';
	TURNAROUND                  :   'TURNAROUND'       | 'turnaround';
	PROCESS		                :   'PROCESS' 	       | 'process';
	PROCESSING                  :   'PROCESSING'       | 'processing';
	SUCCESSFUL		            :   'SUCCESSFUL'	   | 'successful';
	SOURCE		                :   'SOURCE'	       | 'source';
	SUSPENDED                   :   'SUSPENDED'        | 'suspended';
	WAIT              		    :   'WAIT'      	   | 'wait';
	WHERE                       :   'WHERE'            | 'where';
	
IDENTIFIER  
	:	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {skip();}
    |   '/*' ( options {greedy=false;} : . )* '*/' {skip();}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {skip();}
    ;

STRING
    :  Quote ( ESC_SEQ | ~('\\'|'"') )* Quote
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;