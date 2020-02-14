package org.eclipse.ice.modeling.workflowEngine;

/**
 * The ActionMsg class is a type of Action
 */
public class ActionMsg extends Action {

	private Message msg;

	public ActionMsg() {
		System.out.println("ActionMsg() constructor");
		
		this.setMsg(new Message());
		this.msg.setCmnd("Default ActionMsg");
	}

	public Message getMsg() {
		return this.msg;
	}

	/**
	 * 
	 * @param msg
	 */
	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public Message execute() {
		System.out.println("ActionMsg.execute()");
		return msg;
	}

}   // end class ActionMsg