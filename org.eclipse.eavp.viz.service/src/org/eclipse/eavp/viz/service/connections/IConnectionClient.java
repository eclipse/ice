/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;


/**
 * A connection client is a class that can be associated with a single
 * {@link IConnectionAdapter}. It registers with the adapter as an
 * {@link IUpdateableListener} and is notified when the connection changes via
 * its {@link #update(IUpdateable)} method.
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The connection object's type.
 */
public interface IConnectionClient<T> extends IVizUpdateableListener {

	/**
	 * Sets the current connection associated with the client.
	 * <p>
	 * <b>Note:</b> Implementations should at least unregister from the
	 * previously associated connection and register with the new one. It may
	 * also trigger an update to the client.
	 * </p>
	 * 
	 * @param adapter
	 *            The new connection adapter. If {@code null}, the connection
	 *            will be unset and the plot will be cleared.
	 */
	void setConnectionAdapter(IConnectionAdapter<T> adapter);

}
