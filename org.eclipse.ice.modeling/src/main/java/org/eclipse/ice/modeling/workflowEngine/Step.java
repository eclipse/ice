package org.eclipse.ice.modeling.workflowEngine;

public class Step {

	private Action action;
	private SuccessCriteria success;

	public Step() {
		System.out.println("Step() constructor");
		
		// set an inital default action.
		action = new ActionMsg();
		success = new SuccessCriteria ( "Step Success Criteria" );
		
	}   // end Step() constructor;

	/**
	 * 
	 * @return the msg
	 */
	public Action getAction() {
		return this.action;
	}   // end Step.getAction

	/**
	 * 
	 * @param act the msg to set
	 */
	public void setAction(Action act) {
		this.action = act;
	}   // end Step.setAction

	/**
	 * 
	 * @return the success
	 */
	public SuccessCriteria getSuccess() {
		System.out.println("Step.getSuccess()");
		return success;
	}   // end Step.getSuccess()

	/**
	 * 
	 * @param success the success to set
	 */
	public void setSuccess(SuccessCriteria success) {
		this.success = success;
	}   // end Step.setSuccess(SuccessCriteria success)

	public Message doAction() {
		System.out.println("Step.doAction()");
		return action.execute();
		
	}   // end Step.doAction

}   // end class Step