package org.eclipse.ice.modeling.actors;

import org.eclipse.ice.modeling.workflowEngine.*;

public class ReducerStub {

	public ReducerStub() {
		System.out.println("ReducerStub() constructor");
		
	}   // end ReducerStub() construcor

	/**
	 * 
	 * @param msg
	 * @param msgSrc
	 */
	public void processMessage(Message msg, WorkflowEngine msgSrc) {
		System.out.println("ReducerStub.processMessage()");
		
		// Process the incoming message - Reduction Action
		System.out.println("   msg: " + msg.getExpId() + ", Instrument: " + msg.getSrcInstrument() + ", cmnd: " + msg.getCmnd());
		
		// Send a response to the msgSrc based on processing 
		
		
	}   // end ReducerStub.processMessage()

}   // end class ReducerStub