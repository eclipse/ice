package org.eclipse.ice.modeling.workflowEngine;

public class RunSetWF extends Workflow {

	private String runSetID;
	private Workflow childWFs;

	public RunSetWF() {
		super();
		System.out.println("RunSetWF() constructor");
		
		this.setRunSetID("runSetID-10");
		
		// Set the workflows procedure
		this.setProcedure(new Procedure());
		
	}   // end RunSetWF() constructor

	/**
	 * @return the runSetID
	 */
	public String getRunSetID() {
		return runSetID;
	}   // end RunSetWF.getRunSetID()

	/**
	 * @param runSetID the runSetID to set
	 */
	public void setRunSetID(String id) {
		this.runSetID = id;
		this.setWfID(runSetID);
	}   // end RunSetWF.setRunSetID(String id)

	public Workflow getChildWFs() {
		return this.childWFs;
	}   // end RunSetWF.getChildWFs()

	/**
	 * 
	 * @param childWFs
	 */
	public void setChildWFs(Workflow childWFs) {
		this.childWFs = childWFs;
	}   // end RunSetWF.setChildWFs(Workflow childWFs)

	/**
	 * 
	 * @param msgIn
	 */
	public Message handleMsg(Message msgIn) {
		System.out.println("RunSetWF.handleMsg()");
		System.out.println("   msgIn: " + msgIn.getExpId() + ", Instrument: " + msgIn.getSrcInstrument() + ", cmnd: " + msgIn.getCmnd());
		
		// createa an out message.  Keep the same attributes as in except the command
		Message msgOut = new Message();
		msgOut.setExpId( msgIn.getExpId());
		msgOut.setSrcInstrument(msgIn.getSrcInstrument());
		msgOut.setCmnd("msgOut: Message out from RunSetWF: " + this.runSetID);
		
		System.out.println("   msgOut: " + msgOut.getExpId() + ", Instrument: " + msgOut.getSrcInstrument() + ", cmnd: " + msgOut.getCmnd());

		
		// Next create the out message from the child (the run) not from the experiment
		// The experiment should never really generate a message only runs should
		// By doing this we can have the experiment intelligently process the incoming message if
		// there are multiple runs and they need to be processed in a specific order.
		// This is really for batch processing but will/should handle all cases
		
		msgOut = this.childWFs.handleMsg(msgIn);
		
		return msgOut;
	}   // end RunSetWF.handleMsg(Message msgIn)

}   // end class RunSetWF