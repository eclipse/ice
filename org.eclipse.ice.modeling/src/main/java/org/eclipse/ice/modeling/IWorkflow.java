package org.eclipse.ice.modeling;

import org.eclipse.ice.modeling.workflowEngine.*;

public interface IWorkflow {

	/**
	 * 
	 * @param msg
	 */
	void handleMsg(Message msg);

}