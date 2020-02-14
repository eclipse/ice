package org.eclipse.ice.modeling;

import org.eclipse.ice.modeling.workflowEngine.*;
import org.eclipse.ice.modeling.actors.*;

public class WorkflowSystem implements IWorkflow {
	
	private Workflow wrkflow;
	private WorkflowEngine wfEng;
	private TransSrvcStub transSrvc;
	private ReducerStub reducer;   // end initSystem*()

	public WorkflowSystem() {
		
		System.out.println("WorkflowSystem.main()");
		
		// Initialize the system
		initSystem();
		
	}   // end WorkflowSystem() constructor
	
	/**
	 * 
	 * @param trans
	 * @param reducers
	 */
	public WorkflowSystem(TransSrvcStub trans, ReducerStub reducers) {
		
		System.out.println("WorkflowSystem(TransSrvcStub trans, ReducerStub reducers) constructor");
		
		// Set the Transition service and the reducer.
		// FUTURE: There will be a set of reducers
		this.setTransSrvc(trans);
		this.setReducer(reducers);
		
		// Initialize the system
		initSystem();
	}   // end WorkflowSystem(TransSrvcStub trans, ReducerStub reducers) constructor

	public void initSystem () {
		
		System.out.println("WorkflowSystem.initSystem()");
		
		// Create an Experiment workflow and a Run workflow.  Make the run a child
		// of the Experiment workflow
		// Note: The Composite patter is used for workflow.  Both WxpWF and RunWF 
		// inherit from Workflow so they share the same interface.
		RunSetWF runSet = new RunSetWF();
		RunWF    run    = new RunWF();
		
		runSet.setChildWFs(run);
		
		// Init the major objects
		wrkflow = runSet;
		wfEng   = new WorkflowEngine( reducer, wrkflow);

	}   // end WorkflowSystem.initSystem()

	public TransSrvcStub getTransSrvc() {
		return this.transSrvc;
	}   // end WorkflowSystem.getTransSrvc()

	/**
	 * 
	 * @param transSrvc
	 */
	public void setTransSrvc(TransSrvcStub transSrvc) {
		this.transSrvc = transSrvc;
	}   // end WorkflowSystem.setTransSrvc(TransSrvcStub transSrvc)

	public ReducerStub getReducer() {
		return this.reducer;
	}   // end WorkflowSystem.getReducer()

	/**
	 * 
	 * @param reducer
	 */
	public void setReducer(ReducerStub reducer) {
		this.reducer = reducer;
	}   // end WorkflowSystem.setReducer(ReducerStub reducer)

	/**
	 * This method is to satisfy the interface but in a real system it should not resolve to this class.
	 * 
	 * @param reducer
	 */
	public void handleMsg(Message msg) {
		System.out.println("WorkflowSystem.handleMsg(Message msg)");
		
	}   // end WorkflowSystem.handleMsg(Message msg)

	/**
	 * This method returns a class with the IWorkflow interface that the Translation Service uses
	 * 
	 * @return WorkflowEngine
	 */
	public IWorkflow getWfEng() {
		System.out.println("WorkflowSystem.getWfEng()");
		return wfEng;
				
	}   // end WorkflowSystem.getWfEng()

	
}   // end class WorkflowSystem