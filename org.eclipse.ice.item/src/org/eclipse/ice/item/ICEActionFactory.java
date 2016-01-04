/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.item;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ICEActionFactory is the default implementation of the ICE IActionFactory
 * interface. It provides access to any Action implementation that has been
 * exposed to the Eclipse Extension Registry.
 * 
 * @author Alex McCaskey
 *
 */
public class ICEActionFactory implements IActionFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private Logger logger = LoggerFactory.getLogger(ICEActionFactory.class);

	/**
	 * Map holding all available Actions keyed by their String names.
	 */
	private HashMap<String, Action> actions;

	/**
	 * The constructor, grabs all available Actions and adds them to the Action
	 * map.
	 */
	public ICEActionFactory() {
		// Initialize the Map
		actions = new HashMap<String, Action>();

		// Get all available Actions.
		try {
			for (Action action : Action.getAvailableActions()) {
				actions.put(action.getActionName(), action);
			}
		} catch (CoreException e) {
			logger.error("Error in adding an Action to the Action map.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.IActionFactory#getAction(java.lang.String)
	 */
	@Override
	public Action getAction(String actionName) {
		return actions.get(actionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.IActionFactory#getAvailableActions()
	 */
	@Override
	public String[] getAvailableActions() {
		return actions.keySet().toArray(new String[actions.size()]);
	}

}
