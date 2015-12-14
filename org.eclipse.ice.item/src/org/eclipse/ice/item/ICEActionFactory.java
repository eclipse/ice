package org.eclipse.ice.item;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.action.Action;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class ICEActionFactory implements IActionFactory {

	/**
	 * 
	 */
	private HashMap<String, Action> actions;
	
	/**
	 * 
	 */
	public ICEActionFactory() {
		actions = new HashMap<String, Action>();
		
		try {
			for (Action action : Action.getAvailableActions()) {
				actions.put(action.getActionName(), action);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.IActionFactory#getAction(java.lang.String)
	 */
	@Override
	public Action getAction(String actionName) {
		return actions.get(actionName);
	}

}
