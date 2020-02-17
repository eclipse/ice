package org.eclipse.ice.modeling.workflowEngine;

public class Action {

	public Action() {
		System.out.println("Action() constructor");
	}

	/*
	 * I think this should be virtual function so it has to be implemented by
	 * the concrete class.
	 */
	public Message execute() {
		System.out.println("Action.execute()");
		return null;
	}

}