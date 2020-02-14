package org.eclipse.ice.modeling.workflowEngine;

public class SuccessCriteria {

	private String successMsg;
	
	public SuccessCriteria() {
		System.out.println("SuccessCriteria() constructor");
		
		this.setSuccessMsg("Default SuccessCriteria");
	}   // end SuccessCriteria() constructor

	/**
	 * 
	 * @param msg
	 */
	public SuccessCriteria(String msg) {
		System.out.println("SuccessCriteria(String msg) constructor");
		
		this.setSuccessMsg(msg);
	}   // end SuccessCriteria(String msg) constructor

	public String getSuccessMsg() {
		System.out.println("SuccessCriteria.getSuiccessMsg()");
		return this.successMsg;
	}

	/**
	 * 
	 * @param msg
	 */
	public void setSuccessMsg(String msg) {
		System.out.println("SuccessCriteriaset.SuccessMsg(String msg)");
		this.successMsg = msg;
	}

}   // end class SuccessCriteria