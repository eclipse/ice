/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections;

/**
 * This is an interface for listeners that can be registered with
 * {@link IVizConnection}s to be notified when the connection's status changes.
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget for the viz
 *            connections that can be listened to.
 */
public interface IVizConnectionListener<T> {

	/**
	 * Notifies the listener that the connection's state has changed.
	 * 
	 * @param connection
	 *            The connection whose state changed.
	 * @param state
	 *            The new state of the connection.
	 * @param message
	 *            A descriptive message to go along with the state change. This
	 *            is particularly useful if the connection failed.
	 */
	public void connectionStateChanged(IVizConnection<T> connection,
			ConnectionState state, String message);

}
