/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.test;

import java.util.List;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.connections.ConnectionAdapter;
import org.eclipse.ice.viz.service.connections.IConnectionClient;

/**
 * This class provides a concrete sub-class of {@link ConnectionAdapter} for
 * testing. It only implements the methods required for such a concrete
 * sub-class.
 * <p>
 * This class also provides public variables for customizing the underlying
 * {@link FakeConnection} and how the connection is established, e.g., to
 * imitate connection/disconnection delays and failures.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnectionAdapter extends ConnectionAdapter<FakeConnection> {

	/**
	 * How long the {@link FakeConnection}'s connect/disconnect process will
	 * take, in ms.
	 */
	public long delay = 1000;

	/**
	 * Whether or not the {@link FakeConnection} will successfully connect or
	 * disconnect.
	 */
	public boolean success = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionAdapter#openConnection
	 * ()
	 */
	@Override
	protected FakeConnection openConnection() {
		FakeConnection client = new FakeConnection();
		client.connect(delay);
		return (success ? client : null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionAdapter#closeConnection
	 * (java.lang.Object)
	 */
	@Override
	protected boolean closeConnection(FakeConnection connection) {
		connection.disconnect(delay);
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionAdapter#
	 * setConnectionProperties(java.util.List)
	 */
	@Override
	public boolean setConnectionProperties(List<Entry> properties) {
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionAdapter#
	 * setConnectionProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setConnectionProperty(String key, String value) {
		return super.setConnectionProperty(key, value);
	}

	/**
	 * Posts an update notification to listeners (registered
	 * {@link IConnectionClient}s).
	 */
	public void postUpdate() {
		notifyListeners();
	}
}
