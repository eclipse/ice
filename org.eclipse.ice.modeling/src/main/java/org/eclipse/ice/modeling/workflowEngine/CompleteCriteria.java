package org.eclipse.ice.modeling.workflowEngine;

public class CompleteCriteria {

	private String compltMsg;

	public CompleteCriteria() {
		System.out.println("CompleteCriteria() constructor");
		
		this.setCompltMsg("Default Completion Criteria");
	}

	public CompleteCriteria(String msg) {
		System.out.println("CompleteCriteria(String msg) constructor");
		
		this.setCompltMsg(msg);
	}

	public String getCompltMsg() {
		System.out.println("CompleteCriteria.getCompltMsg()");
		return this.compltMsg;
	}

	/**
	 * 
	 * @param compltMsg
	 */
	public void setCompltMsg(String compltMsg) {
		System.out.println("CompleteCriteria.setCompltMsg(String compltMsg)");
		this.compltMsg = compltMsg;
	}

}   // end class CompleteCriteria