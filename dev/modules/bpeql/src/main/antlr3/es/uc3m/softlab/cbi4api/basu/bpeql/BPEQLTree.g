tree grammar BPEQLTree;

options {

    // Default but name it anyway
    //
    language   = Java;

    // Use the vocab from the parser (not the lexer)
    // The ANTLR Maven plugin knows how to work out the
    // relationships between the .g files and it will build
    // the tree parser after the parser. It will also rebuild
    // the tree parser if the parser is rebuilt.
    //
    tokenVocab = BPEQLParser;

    // Use ANTLR built-in CommonToken for tree nodes
    //
    ASTLabelType = CommonTree;
}

// What package should the generated source exist in?
//
@header {

    package es.uc3m.softlab.cbi4api.basu.bpeql;
}

@members { 
	private StringBuffer hiveql;
	
	public StringBuffer getHiveQL() {
		return hiveql;
	}
}

stmt 
@init { hiveql = new StringBuffer(); }
	: query_specification EOF 		
   	;

query_specification 
	:  SELECT 							{ hiveql.append("select "); }
		select_instance_list  
		instance_expression 
	|  SELECT 							{ hiveql.append("select "); }
		select_model_list  
		model_expression
	|  SELECT AGGREGATE 				{ hiveql.append("select "); }
		select_aggregate_instance_list  
		instance_aggregate_expression 
	|  SELECT AGGREGATE 				{ hiveql.append("select "); }
		select_aggregate_model_list 
		model_aggregate_expression
	;

select_instance_list   	
	:	Asterisk 				{ hiveql.append("i.id as id, i.name as name, m.source as source, m.name as model, f.startTime as startTime, f.endTime as endTime, cast(f.successful as double) as successful, cast(f.failed as double) as failed, cast(f.aborted as double) as aborted, cast(f.running as double) as running, cast(f.turnAround as double) as turnAround, cast(f.wait as double) as wait, cast(f.processing as double) as processing, cast(f.suspend as double) as suspend, cast(f.changeOver as double) as changeOver"); }
	|	select_instance_sublist  
		( Comma 				{ hiveql.append(", "); } 
		  select_instance_sublist  )* 
	;

select_instance_sublist   
	:	value_expression 
	;

select_model_list   	
	:	Asterisk 				{ hiveql.append("i.id as id, i.name as name, m.source as source, m.name as model, f.startTime as startTime, f.endTime as endTime, cast(f.successful as double) as successful, cast(f.failed as double) as failed, cast(f.aborted as double) as aborted, cast(f.running as double) as running, cast(f.turnAround as double) as turnAround, cast(f.wait as double) as wait, cast(f.processing as double) as processing, cast(f.suspend as double) as suspend, cast(f.changeOver as double) as changeOver"); }
	|	select_model_sublist  
		( Comma 				{ hiveql.append(", "); } 
		  select_model_sublist  )* 
	;

select_model_sublist   
	:	value_expression 	
	;
	
value_expression  
	:	behavioural_value_expression ( structural_value_expression )?
	|	structural_value_expression 
	;
	
structural_value_expression 
	:	ID			{ hiveql.append("i.id as id");				}
	| 	NAME		{ hiveql.append("i.name as name");			}
	|	SOURCE		{ hiveql.append("m.source as source");		}
	|	MODEL		{ hiveql.append("m.name as model");			}
	| 	START_TIME	{ hiveql.append("f.startTime as startTime"); }
	|	END_TIME	{ hiveql.append("f.endTime as endTime"); 	}
	;
	
behavioural_value_expression  
	:	SUCCESSFUL  { hiveql.append("cast(f.successful as double) as successful"); 	}
    |   RUNNING     { hiveql.append("cast(f.running as double) as running"); 	}
    |   FAILED      { hiveql.append("cast(f.failed as double) as failed"); 	}
    |   ABORTED     { hiveql.append("cast(f.aborted as double) as aborted"); 	}
    |   TURNAROUND  { hiveql.append("cast(f.turnAround as double) as turnAround"); 	}
	|	WAIT 		{ hiveql.append("cast(f.wait as double) as wait"); 				}
	|	PROCESSING 	{ hiveql.append("cast(f.processing as double) as processing"); 	}
	|	SUSPENDED 	{ hiveql.append("cast(f.suspend as double) as suspend"); 		}
	|	CHANGEOVER	{ hiveql.append("cast(f.changeOver as double) as changeOver"); 	}
	;
	
select_aggregate_instance_list   	
	:	Asterisk 				{ hiveql.append("i.name as name, m.source as source, m.name as model, sum(f.successful as double) as successful, sum(f.failed as double) as failed, sum(f.aborted as double) as aborted, sum(f.running as double) as running, avg(f.turnAround) as turnAround, avg(f.wait) as wait, avg(f.processing) as processing, avg(f.suspend) as suspend, avg(f.changeOver) as changeOver"); }
	|	select_aggregate_instance_sublist  
		( Comma 				{ hiveql.append(", "); } 
		  select_aggregate_instance_sublist  )* 
	;

select_aggregate_instance_sublist   
	:	aggregate_value_expression 
	;

select_aggregate_model_list   	
	:	Asterisk 				{ hiveql.append("i.name as name, m.source as source, m.name as model, sum(f.successful as double) as successful, sum(f.failed as double) as failed, sum(f.aborted as double) as aborted, sum(f.running as double) as running, avg(f.turnAround) as turnAround, avg(f.wait) as wait, avg(f.processing) as processing, avg(f.suspend) as suspend, avg(f.changeOver) as changeOver"); }
	|	select_aggregate_model_sublist  
		( Comma 				{ hiveql.append(", "); } 
		  select_aggregate_model_sublist  )* 
	;

select_aggregate_model_sublist   
	:	aggregate_value_expression 	
	;

aggregate_value_expression  
	:	behavioural_aggregate_value_expression ( structural_aggregate_value_expression )?
	|	structural_aggregate_value_expression 
	;
	
structural_aggregate_value_expression 
	: 	NAME		{ hiveql.append("i.name as name");	}
	|	SOURCE		{ hiveql.append("m.source as source");			}
	|	MODEL		{ hiveql.append("m.name as model");	}
	;
	
behavioural_aggregate_value_expression  
	:	SUCCESSFUL  { hiveql.append("avg(f.successful) as successful"); 	}
    |   RUNNING     { hiveql.append("avg(f.running) as running"); 	}
    |   FAILED      { hiveql.append("avg(f.failed) as failed"); 	}
    |   ABORTED     { hiveql.append("avg(f.aborted) as aborted"); 	}
    |   TURNAROUND  { hiveql.append("avg(f.turnAround) as turnAround"); 	}
	|	WAIT 		{ hiveql.append("avg(f.wait) as wait"); 				}
	|	PROCESSING 	{ hiveql.append("avg(f.processing) as processing"); 	}
	|	SUSPENDED 	{ hiveql.append("avg(f.suspend) as suspend");		}
	|	CHANGEOVER	{ hiveql.append("avg(f.changeOver) as changeOver");	}
	;		
				
set_quantifier  
	:	AGGREGATE 
	;

instance_expression  
	:	FROM 				{ hiveql.append(" from "); 	} 
		instance_from_clause  
	;

model_expression  
	:	FROM 				{ hiveql.append(" from "); 	} 
		model_from_clause  
	;
	
instance_from_clause   
	:	activity_reference ( activity_where_clause  )?
	|	process_reference ( process_where_clause  )?
	;

model_from_clause	
	:	model_reference ( model_where_clause  )?
	;

instance_aggregate_expression  
	:	FROM 			{ hiveql.append(" from "); 	} 
		instance_aggregate_from_clause  
	;

model_aggregate_expression  
	:	FROM 			{ hiveql.append(" from "); 	} 
		model_aggregate_from_clause  
	;
	
instance_aggregate_from_clause   
	:	activity_reference ( activity_where_clause  )?	{ hiveql.append(" group by i.name, m.source, m.name "); }
	|	process_reference ( process_where_clause  )?	{ hiveql.append(" group by i.name, m.source, m.name "); }
	;

model_aggregate_from_clause	
	:	model_reference ( model_where_clause  )?		{ hiveql.append(" group by i.name, m.source, m.name "); }
	;
	
activity_reference
	:	ACTIVITY   	{ hiveql.append("event_fact f "); 	}
					{ hiveql.append("JOIN activity_instance i on (i.id = f.activity) "); }
					{ hiveql.append("JOIN model m on (m.id = i.model) "); }
					{ hiveql.append("WHERE i.id = f.activity "); }
	;

process_reference   
	:	PROCESS    	{ hiveql.append("event_fact f "); 	}
                    { hiveql.append("JOIN process_instance i on (i.id = f.process) "); }
                    { hiveql.append("JOIN model m on (m.id = i.model) "); }
                    { hiveql.append("WHERE f.activity is null "); }
	;	

model_reference   
	:	MODEL  		{ hiveql.append("event_fact f "); 	}
                    { hiveql.append("JOIN process_instance i on (i.id = f.process) "); }
                    { hiveql.append("JOIN model m on (m.id = i.model) "); }
                    { hiveql.append("WHERE f.activity is null "); }
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
	:  	ID Equals_Operator INT   		{ hiveql.append("and f.activity = " + $INT.text); }
	|   NAME Equals_Operator STRING		{ hiveql.append("and i.name = " + $STRING.text); } 
	;

process_search_condition   
	:  	ID Equals_Operator INT 			{ hiveql.append("and f.process = " + $INT.text); }
	|   NAME Equals_Operator STRING		{ hiveql.append("and i.name = " + $STRING.text); } 
	;

model_search_condition   
	:  	ID Equals_Operator INT			{ hiveql.append("and m.id = " + $INT.text); }
	|   NAME Equals_Operator STRING		{ hiveql.append("and m.name = " + $STRING.text); } 
	;

id_search_expression
	: 	ID Equals_Operator INT  		{ hiveql.append("id = " + $INT.text); } 
	;
	
name_search_expression
	: 	NAME Equals_Operator STRING		{ hiveql.append("name = " + $STRING.text); }
	;