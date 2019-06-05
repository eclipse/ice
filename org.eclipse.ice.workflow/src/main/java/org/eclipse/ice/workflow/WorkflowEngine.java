package org.eclipse.ice.workflow;

public class WorkflowEngine {

	public enum States {
		EXECUTING, FAILED, FINISHED, INITIALIZED, READY, REVIEWING, WAITING, WAITING_FOR_INFO
	}

	public enum Events {
		PARAMETERS_RECEIVED, EXCEPTION, EXCEPTIONHANDLED, EXECUTION_COMPLETE
	}
	
}
