parser grammar BPEQLParser;

options {

    // Default language but name it anyway
    //
    language  = Java;

    // Produce an AST
    //
    output    = AST;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractTParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the 
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = BPEQLLexer;
}

// Import a grammar file, even though it does not really need it in this
// simle demo parser. We do the import to show where imported grammars should be
// stored for maven builds.
//
// import Ruleb;

// Some imaginary tokens for tree rewrites
//
tokens {
    SCRIPT;
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

stmt  
	: query_specification EOF
   	;

query_specification 
	:  SELECT select_instance_list  instance_expression 
	|  SELECT select_model_list model_expression
	|  SELECT AGGREGATE select_aggregate_instance_list  instance_aggregate_expression 
	|  SELECT AGGREGATE select_aggregate_model_list model_aggregate_expression
	;

select_instance_list   	
	:	Asterisk 				
	|	select_instance_sublist	( Comma select_instance_sublist  )* 
	;

select_instance_sublist   
	:	value_expression 
	;

select_model_list   	
	:	Asterisk 				
	|	select_model_sublist ( Comma select_model_sublist  )* 
	;

select_model_sublist   
	:	value_expression 
	;
	
value_expression  
	:	behavioural_value_expression ( structural_value_expression )?
	|	structural_value_expression 
	;
	
structural_value_expression 
	:	ID			
	| 	NAME		
	|	SOURCE		
	|	MODEL		
	| 	START_TIME	
	|	END_TIME	
	;
	
behavioural_value_expression  
	:	SUCCESSFUL
	|   RUNNING
	|   FAILED
	|   ABORTED
	|   TURNAROUND
	|	WAIT 		
	|	PROCESSING 	
	|	SUSPENDED 	
	|	CHANGEOVER 		
	;

select_aggregate_instance_list   	
	:	Asterisk 				
	|	select_aggregate_instance_sublist	( Comma select_aggregate_instance_sublist  )* 
	;

select_aggregate_instance_sublist   
	:	aggregate_value_expression 
	;

select_aggregate_model_list   	
	:	Asterisk 				
	|	select_aggregate_model_sublist ( Comma select_aggregate_model_sublist  )* 
	;

select_aggregate_model_sublist   
	:	aggregate_value_expression 
	;
	
aggregate_value_expression  
	:	behavioural_aggregate_value_expression ( structural_aggregate_value_expression )?
	|	structural_aggregate_value_expression 
	;
	
structural_aggregate_value_expression 
	: 	NAME		
	|	SOURCE		
	|	MODEL		
	;
	
behavioural_aggregate_value_expression  
	:	SUCCESSFUL
    |   RUNNING
    |   FAILED
    |   ABORTED
    |   TURNAROUND
	|	WAIT 		
	|	PROCESSING 	
	|	SUSPENDED 	
	|	CHANGEOVER 		
	;
					
set_quantifier  
	: 	AGGREGATE
	;

instance_expression  
	:	FROM instance_from_clause  
	;

model_expression  
	:	FROM model_from_clause  
	;
	
instance_from_clause   
	:	activity_reference ( activity_where_clause  )?
	|	process_reference ( process_where_clause  )?
	;

model_from_clause	
	:	model_reference ( model_where_clause  )?
	;

instance_aggregate_expression  
	:	FROM instance_aggregate_from_clause  
	;

model_aggregate_expression  
	:	FROM model_aggregate_from_clause  
	;
	
instance_aggregate_from_clause   
	:	activity_reference ( activity_where_clause  )?
	|	process_reference ( process_where_clause  )?
	;

model_aggregate_from_clause	
	:	model_reference ( model_where_clause  )?
	;
	
activity_reference
	:	ACTIVITY   	
	;

process_reference   
	:	PROCESS    	
	;	

model_reference   
	:	MODEL  		
	;
			
activity_where_clause   
	: 	WHERE activity_search_condition 
	;

process_where_clause   
	: 	WHERE process_search_condition 
	;
				
model_where_clause   
	: 	WHERE model_search_condition 
	;
				
activity_search_condition   
	:  	ID Equals_Operator INT   		
	|   NAME Equals_Operator STRING		 
	;

process_search_condition   
	:  	ID Equals_Operator INT 			
	|   NAME Equals_Operator STRING		 
	;

model_search_condition   
	:  	ID Equals_Operator INT 			
	|   NAME Equals_Operator STRING	
	;

id_search_expression
	: 	ID Equals_Operator INT  		 
	;
	
name_search_expression
	: 	NAME Equals_Operator STRING		
	;