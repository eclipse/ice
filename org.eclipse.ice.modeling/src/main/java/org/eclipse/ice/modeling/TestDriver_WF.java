package org.eclipse.ice.modeling;

import org.eclipse.ice.modeling.workflowEngine.*;
import org.eclipse.ice.modeling.actors.*;

public class TestDriver_WF {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("TestDriver_WF.main()");

		//Instantiate all supporting objects and kick off the workflow system
		TransSrvcStub  transSrvc = new TransSrvcStub();
		ReducerStub	   reducer1  = new ReducerStub();
		
		// Creating the Workflow System creates all of its parts includign the engine
		// which is the part that needs to be called byt the Translation Service
		WorkflowSystem wf        = new WorkflowSystem( transSrvc, reducer1 );
		
		transSrvc.setWorkflow(wf.getWfEng());
		transSrvc.generateCmnd("Transition Service Command");
		
		
	}  // end main()
}