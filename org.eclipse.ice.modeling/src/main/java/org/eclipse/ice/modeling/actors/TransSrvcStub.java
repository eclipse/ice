package org.eclipse.ice.modeling.actors;


import org.eclipse.ice.modeling.IWorkflow;
import org.eclipse.ice.modeling.workflowEngine.*;

public class TransSrvcStub {

	private IWorkflow workflow;   // end TransSrvcStub() constructor
	

	public TransSrvcStub() {
		System.out.println("TransSrvcStub() constructor");
		
	}   // endTransSrvcStub() constructor

	public IWorkflow getWorkflow() {
		System.out.println("TransSrvcStub.getWorkflow()");
		return this.workflow;
		
	}   // end TransSrvcStub.getWorkflow()

	/**
	 * 
	 * @param wf
	 */
	public void setWorkflow( IWorkflow wf ) {
		System.out.println("TransSrvcStub.setWorkflow( IWorkflow workflow )");
		this.workflow = wf;
		
	}   // end TransSrvcStub.setWorkflow(WorkflowEngine workflow)

	public void generateCmnd() {
		System.out.println("TransSrvcStub.generateCmnd()");
		
		// Create a Message and send it to the Workflow
		Message msg = new Message();
		msg.setCmnd("Default msg from Transition Service");
		msg.setExpId("ExpID-42");
		msg.setSrcInstrument(3);
		
		System.out.println("   cmnd: " + msg.getExpId() + ", Instrument: " + msg.getSrcInstrument() + ", cmnd: " + msg.getCmnd());

		workflow.handleMsg(msg);
		
	}   // end TransSrvcStub.generateCmnd()

	/**
	 * 
	 * @param cmnd
	 */
	public void generateCmnd(String cmnd) {
		System.out.println("TransSrvcStub.generateCmnd(String cmnd)");
		
		// Create a Message and send it to the Workflow
		Message msg = new Message();
		msg.setCmnd(cmnd);
		msg.setExpId("ExpID-42");
		msg.setSrcInstrument(5);

		System.out.println("   cmnd: " + msg.getExpId() + ", Instrument: " + msg.getSrcInstrument() + ", cmnd: " + msg.getCmnd());

		workflow.handleMsg(msg);
		
	}   // end TransSrvcStub.generateCmnd(String cmnd)


}   // end class TransSrvcStub