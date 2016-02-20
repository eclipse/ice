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

/**
 * An interface for participants in the observer pattern who have an
 * UpdateableSubscriptionManager, which allows the listener to subscribe for
 * updates only for specified kinds of events and for the observed object to
 * collect messages to be sent in bulk, rather than one at a time.
 * 
 * @author Robert Smith
 *
 */
public interface IManagedUpdateable {

	/**
	 * Register a listener to receive managed updates.
	 * 
	 * @param listener
	 *            The object which will receive future updates
	 * @param types
	 *            The list of event types that the listener is subscribed to
	 *            listen to
	 */
	public void register(IManagedUpdateableListener listener);

	/**
	 * Remove a registered listener.
	 * 
	 * @param listener
	 *            The object which will no longer receive notifications from
	 *            this
	 */
	public void unregister(IManagedUpdateableListener listener);
}
