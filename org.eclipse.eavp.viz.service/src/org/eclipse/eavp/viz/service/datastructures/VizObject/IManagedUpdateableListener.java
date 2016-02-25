/*******************************************************************************
 * Copyright (c) 2011, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures.VizObject;

import java.util.ArrayList;

/**
 * An interface for participants in the observer pattern, which receive updates
 * from an IMangedUpdateable's IUpdateableSubscriptionManager.
 * 
 * @author Robert Smith
 *
 */
public interface IManagedUpdateableListener {

	/**
	 * Polls the listener for a list of the types of events it wants to receive
	 * from the given source.
	 * 
	 * @param source
	 *            The object which this listener will listen to
	 * @return An array of the event types the listener will be notified of
	 *         during updates
	 */
	public ArrayList<SubscriptionType> getSubscriptions(
			IManagedUpdateable source);

	/**
	 * Receive an update, including the source component and type of event that
	 * triggered the update.
	 * 
	 * @param component
	 *            The updateable component the update is coming from
	 * @param type
	 *            The event type that of the update
	 */
	public void update(IManagedUpdateable component,
			SubscriptionType[] type);
}
