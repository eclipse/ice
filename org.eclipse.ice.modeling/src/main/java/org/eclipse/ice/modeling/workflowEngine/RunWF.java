package org.eclipse.ice.modeling.workflowEngine;

public class RunWF extends Workflow {

	private String runID;

	public RunWF() {
		super();
		System.out.println("RunWF() constructor");
		
		this.setRunID("Run-ID");
		
		this.setCurrentStep(null);
		
	}   // end RunWF() constructor

	public String getRunID() {
		return this.runID;
	}   // end RunWF.getRunID()

	/**
	 * 
	 * @param id
	 */
	public void setRunID(String id) {
		this.runID = id;
		this.setWfID(runID);
	}   // end RunWF.setRunID(String id)

	/**
	 * 
	 * @param msgIn
	 */
	public Message handleMsg(Message msgIn) {
		System.out.println("RunWF.nextMsg()");
		System.out.println("   msgIn: " + msgIn.getExpId() + ", Instrument: " + msgIn.getSrcInstrument() + ", cmnd: " + msgIn.getCmnd());
		
		// createa an out message.  Keep the same attributes as in except the command
		Message msgOut = new Message();
		msgOut.setExpId( msgIn.getExpId());
		msgOut.setSrcInstrument(msgIn.getSrcInstrument());
		msgOut.setCmnd("msgOut: Message out from RunWF");
		
		System.out.println("   msgOut: " + msgOut.getExpId() + ", Instrument: " + msgOut.getSrcInstrument() + ", cmnd: " + msgOut.getCmnd());
		
		// Get the next step from the procedure
		// If it is the first run we should get step 0
		this.setCurrentStep(this.getProcedure().nextStep(this.getCurrentStep()));
		
		return this.getCurrentStep().doAction();
		
	}   // end RunWF.nextMsg

}   // end class RunWF