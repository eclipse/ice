package org.eclipse.ice.modeling.workflowEngine;

public class ExpWF extends Workflow {

	private String   expID;
	private Workflow childWFs;

	public ExpWF() {
		super();
		System.out.println("ExpWF() constructor");
		
		this.setExpID("ExpID-42");
		
	}   // end ExpWF() constructor

	public String getExpID() {
		return this.expID;
	}   // end ExpWF.getExpID()

	/**
	 * 
	 * @param id
	 */
	public void setExpID(String id) {
		this.expID = id;
		this.setWfID(expID);
	}   // end ExpWF.setExpID(String id)

	public Workflow getChildWFs() {
		return this.childWFs;
	}   // end ExpWF.getChildWFs()

	/**
	 * 
	 * @param childWFs
	 */
	public void setChildWFs(Workflow childWFs) {
		this.childWFs = childWFs;
	}   // end ExpWF.setChildWFs(Workflow childWFs)

	/**
	 * 
	 * @param msgIn
	 */
	public Message handleMsg(Message msgIn) {
		System.out.println("ExpWF.nextMsg()");
		System.out.println("   msgIn: " + msgIn.getExpId() + ", Instrument: " + msgIn.getSrcInstrument() + ", cmnd: " + msgIn.getCmnd());
		
		// createa an out message.  Keep the same attributes as in except the command
		Message msgOut = new Message();
		msgOut.setExpId( msgIn.getExpId());
		msgOut.setSrcInstrument(msgIn.getSrcInstrument());
		msgOut.setCmnd("msgOut: Message out from ExpWF");
		
		System.out.println("   msgOut: " + msgOut.getExpId() + ", Instrument: " + msgOut.getSrcInstrument() + ", cmnd: " + msgOut.getCmnd());

		
		// Next create the out message from the child (the run) not from the experiment
		// The experiment should never really generate a message only runs should
		// By doing this we can have the experiment intelligently process the incoming message if
		// there are multiple runs and they need to be processed in a specific order.
		// This is really for batch processing but will/should handle all cases
		
		msgOut = this.childWFs.handleMsg(msgIn);
		
		return msgOut;
	}   // end ExpWF.nextMsg()

}