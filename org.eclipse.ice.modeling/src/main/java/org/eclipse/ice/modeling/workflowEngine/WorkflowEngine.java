package org.eclipse.ice.modeling.workflowEngine;

import org.eclipse.ice.modeling.actors.*;
import org.eclipse.ice.modeling.*;

public class WorkflowEngine implements IWorkflow {

	private ReducerStub reducer;
	private Workflow workflow;

	/**
	 * 
	 */
	public WorkflowEngine() {
		System.out.println("WorkflowEngine() constructor");
		
	}   // end WorkflowEngine() constructor

	/**
	 * @param reducer
	 * @param workflow
	 */
	public WorkflowEngine(ReducerStub reducer, Workflow workflow) {
		System.out.println("WorkflowEngine(ReducerStub reducer, Workflow workflow) constructor");
		
		this.reducer  = reducer;
		this.workflow = workflow;
		
	}   // end WorkflowEngine() constructor

	/**
	 * 
	 * @param msg
	 */
	public void handleMsg(Message msg) {
		System.out.println("WorkflowEngine.handleMsg()");
		
		Message msgOut;
		
		// Determine which workflow the incoming msg is associated wtih
		// The incoming message has a refernce to a DataSet which contains the MetaData
		//Workflow wrkfl = this.determineWorkflow(msg.getDataSetRef().getMetaData());
		
		
		// Ask the workflow for the next outgoing message
		// A return of null indicates no follow on message
		msgOut = workflow.handleMsg(msg);
		
		if (msgOut != null) {
			reducer.processMessage(msgOut, this);
		}
		
	}   // end WorkflowEngine.handleMsg(Message msg)

	public ReducerStub getReducer() {
		return this.reducer;
	}   // end WorkflowEngine.getReducer()

	/**
	 * 
	 * @param reducer
	 */
	public void setReducer(ReducerStub reducer) {
		this.reducer = reducer;
	}

	/**
	 * 
	 * @param metaData
	 */
	public Workflow determineWorkflow(MetaData metaData) {
		System.out.println("WorkflowEngine.determineWorkflow(MetaData metaData)");
		return this.workflow;
		
	}   // end WorkflowEngine.setReducer(ReducerStub reducer)


}   // end class WorkflowEngine