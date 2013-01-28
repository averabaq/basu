/* 
 * $Id: ODEEventState.java,v 1.0 2011-10-26 17:32:14 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.ode.event.publisher;

/**
 * Representational event states defined at the ApacheODE API (1.3.5).
 *  
 * @author averab
 * @version 1.0.0 
 */
public enum ODEEventState {
	/** Apache ODE event for the ActivityEnabledEvent */
	ACTIVITY_ENABLED_EVENT("ActivityEnabledEvent"),
	/** Apache ODE event for the ActivityExecEndEvent */
	ACTIVITY_EXEC_END_EVENT("ActivityExecEndEvent"),
	/** Apache ODE event for the ActivityExecStartEvent */
	ACTIVITY_EXEC_START_EVENT("ActivityExecStartEvent"),
	/** Apache ODE event for the ActivityFailureEvent */
	ACTIVITY_FAILURE_EVENT("ActivityFailureEvent"),
	/** Apache ODE event for the ActivityRecoveryEvent */
	ACTIVITY_RECOVERY_EVENT("ActivityRecoveryEvent"),
	/** Apache ODE event for the CompensationHandlerRegistered */
	COMPENSATION_HANDLER_REGISTERED("CompensationHandlerRegistered"),
	/** Apache ODE event for the CorrelationEvent */
	CORRELATION_EVENT("CorrelationEvent"),
	/** Apache ODE event for the CorrelationMatchEvent */
	CORRELATION_MATCH_EVENT("CorrelationMatchEvent"),
	/** Apache ODE event for the CorrelationNoMatchEvent */
	CORRELATION_NO_MATCH_EVENT("CorrelationNoMatchEvent"),
	/** Apache ODE event for the CorrelationSetEvent */
	CORRELATION_SET_EVENT("CorrelationSetEvent"),
	/** Apache ODE event for the CorrelationSetWriteEvent */
	CORRELATION_SET_WRITE_EVENT("CorrelationSetWriteEvent"),
	/** Apache ODE event for the ExpressionEvaluationEvent */
	EXPRESSION_EVALUATION_EVENT("ExpressionEvaluationEvent"),
	/** Apache ODE event for the ExpressionEvaluationFailedEvent */
	EXPRESSION_EVALUATION_FAILED_EVENT("ExpressionEvaluationFailedEvent"),
	/** Apache ODE event for the ExpressionEvaluationSuccessEvent */
	EXPRESSION_EVALUATION_SUCCESS_EVENT("ExpressionEvaluationSuccessEvent"),
	/** Apache ODE event for the NewProcessInstanceEvent */
	NEW_PROCESS_INSTANCE_EVENT("NewProcessInstanceEvent"),
	/** Apache ODE event for the PartnerLinkModificationEvent */
	PARTNER_LINK_MODIFICATION_EVENT("PartnerLinkModificationEvent"),
	/** Apache ODE event for the ProcessCompletionEvent */
	PROCESS_COMPLETION_EVENT("ProcessCompletionEvent"),
	/** Apache ODE event for the ProcessInstanceStartedEvent */
	PROCESS_INSTANCE_STARTED_EVENT("ProcessInstanceStartedEvent"),
	/** Apache ODE event for the ProcessInstanceStateChangeEvent */
	PROCESS_INSTANCE_STATE_CHANGE_EVENT("ProcessInstanceStateChangeEvent"),
	/** Apache ODE event for the ProcessMessageExchangeEvent */
	PROCESS_MESSAGE_EXCHANGE_EVENT("ProcessMessageExchangeEvent"),
	/** Apache ODE event for the ProcessTerminationEvent */
	PROCESS_TERMINATION_EVENT("ProcessTerminationEvent"),
	/** Apache ODE event for the ScopeCompletionEvent */
	SCOPE_COMPLETION_EVENT("ScopeCompletionEvent"),
	/** Apache ODE event for the ScopeFaultEvent */
	SCOPE_FAULT_EVENT("ScopeFaultEvent"),
	/** Apache ODE event for the ScopeStartEvent */
	SCOPE_START_EVENT("ScopeStartEvent"),
	/** Apache ODE event for the VariableModificationEvent */
	VARIABLE_MODIFICATION_EVENT("VariableModificationEvent"),
	/** Apache ODE event for the VariableReadEvent */
	VARIABLE_READ_EVENT("VariableReadEvent");
	
	/** enumerator value */
	private final String value;

	/**
	 * Constructor for this enumerator class.
	 * 
	 * @param value
	 */
	private ODEEventState(String value) {
		this.value = value;
	}
	/**
	 * Gets the enum current constant value.
	 * 
	 * @return enum current constant value.
	 */
	public String value() {
		return this.value;
	}
}