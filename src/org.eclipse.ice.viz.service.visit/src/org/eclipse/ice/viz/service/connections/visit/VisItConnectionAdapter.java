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
package org.eclipse.ice.viz.service.connections.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtConnectionManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.connections.ConnectionAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class provides a {@link ConnectionAdapter} that wraps a
 * {@link VisItSwtConnection}. It handles connecting and disconnecting as well
 * as updating the required connection properties.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnectionAdapter extends
		ConnectionAdapter<VisItSwtConnection> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.visit.ConnectionAdapter#
	 * openConnection()
	 */
	@Override
	protected VisItSwtConnection openConnection() {

		// Create a VisIt ConnectionJob
		ConnectionJob connectionJob = new ConnectionJob("Connecting to VisIt") {

			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ice.viz.service.connections.ConnectionAdapter.ConnectionJob#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				final int ticks = 100;
				monitor.beginTask("Creating the connection to VisIt...", ticks);
				try {
					connection = VisItSwtConnectionManager.createConnection(
							getKey(), createDefaultShell(),
							getConnectionProperties());//, monitor);

					if (connection == null) {
						String errorMessage = "The VisIt connection was null. Check "
								+ "your VisIt application path, or specified port, in the Visualization > VisIt "
								+ "preferences page.";
						return new Status(
								Status.ERROR,
								"org.eclipse.ice.viz.service.connections.visit",
								1, errorMessage, null);
					}

					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}

				} finally {
					monitor.subTask("VisIt connection established successfully.");
					monitor.worked(100);
					monitor.done();
				}
				return Status.OK_STATUS;
			}

		};

		// Schedule it for execution
		connectionJob.schedule();

		// Let this thread wait til it's been launched
		// and completed.
		try {
			connectionJob.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Return, clients should handle if its null or not
		return connectionJob.getConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.visit.ConnectionAdapter#
	 * closeConnection(java.lang.Object)
	 */
	@Override
	protected boolean closeConnection(VisItSwtConnection connection) {
		connection.close();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.visit.ConnectionAdapter#
	 * setConnectionProperties(java.util.List)
	 */
	@Override
	public boolean setConnectionProperties(List<Entry> row) {

		boolean changed = false;

		if (row != null && row.size() >= 9) {

			String value;

			// Update all of the properties. If one changes, then changed should
			// be set to true.
			changed |= setConnectionProperty("connId", row.get(0).getValue());
			changed |= setConnectionProperty("url", row.get(1).getValue());
			changed |= setConnectionProperty("port", row.get(2).getValue());
			changed |= setConnectionProperty("visDir", row.get(4).getValue());
			changed |= setConnectionProperty("gateway", row.get(7).getValue());
			changed |= setConnectionProperty("localGatewayPort", row.get(8)
					.getValue());
			changed |= setConnectionProperty("username", row.get(9).getValue());
			changed |= setConnectionProperty("password", "notused");
			changed |= setConnectionProperty("windowWidth", "1340");
			changed |= setConnectionProperty("windowHeight", "1020");
			changed |= setConnectionProperty("windowId", "1");
			changed |= setConnectionProperty("isLaunch", "true");
			// useTunneling should be false for local launches and true for
			// remote.
			value = "localhost".equals(getConnectionProperty("url")) ? "false"
					: "true";
			changed |= setConnectionProperty("useTunneling", value);

			// Print out some debug messages about the new input when debugging.
			if (System.getProperty("DebugICE") != null) {
				logger.info("VisItConnectionAdapter message: "
						+ "Connection input properties:");
				for (java.util.Map.Entry<String, String> e : getConnectionProperties()
						.entrySet()) {
					logger.info("  " + e.getKey() + "," + e.getValue());
				}
				logger.info("VisItConnectionAdapter message: "
						+ "End of connection input properties.");
			}

			// Update the reference to the key.
			objectName = getConnectionProperty("connId");
		}

		return changed;
	}

	/**
	 * Creates a new, default {@code Shell} using the UI thread.
	 * 
	 * @return A new, unopened {@code Shell}, or {@code null} if the main
	 *         {@code Display} could not be found.
	 */
	private Shell createDefaultShell() {
		// FIXME The VisItSwtConnection shouldn't need to worry about a Shell,
		// except when creating dialogs. Eventually, the connection dialogs
		// should be handled in ICE. In special cases where VisIt needs to
		// create its own custom dialog, it should get a Shell from the UI
		// Display like the code below.

		// Set the default return value.
		Shell shell = null;

		// We need a Display to create a Shell (since the Shell constructor
		// requires one and the Display controls the UI thread).
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}

		if (display != null) {
			// Create a new Shell on the UI thread.
			final Display displayRef = display;
			final AtomicReference<Shell> shellRef = new AtomicReference<Shell>();
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					shellRef.set(new Shell(displayRef));
				}
			});
			// This sets the return value.
			shell = shellRef.get();
		}

		return shell;
	}

	/**
	 * Gets the next available window ID from the underlying connection. Each
	 * unique, disjoint view powered by VisIt requires a separate "window" in
	 * VisIt. Views that share a window ID will be updated concurrently.
	 * 
	 * @return The next available window ID, or -1 if the connection is not
	 *         open.
	 */
	public int getNextWindowId() {
		// FIXME There is a bug that prevents any window ID besides 1 from
		// working as expected. For now, just return 1. A bug ticket has been
		// filed.
		int windowId = 1;
		// if (getState() == ConnectionState.Connected) {
		// // The order of the returned list is not guaranteed. Throw it into
		// // an ordered set and get the lowest positive ID not in the set.
		// Set<Integer> ids = new HashSet<Integer>(getConnection()
		// .getWindowIds());
		// // Find the first integer not in the set.
		// while (ids.contains(windowId)) {
		// windowId++;
		// }
		// } else {
		// windowId = -1;
		// }
		return windowId;
	}
}
