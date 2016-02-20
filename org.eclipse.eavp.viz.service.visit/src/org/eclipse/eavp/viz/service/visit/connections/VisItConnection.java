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
package org.eclipse.eavp.viz.service.visit.connections;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtConnectionManager;

/**
 * This class provides an {@link IVizConnection} for connecting to
 * {@link VisItSwtConnection}s.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnection extends VizConnection<VisItSwtConnection> {

	/**
	 * The default constructor.
	 */
	public VisItConnection() {

		// Set read-only properties before their validators are set.
		setProperty("password", "notused");

		// Remove the description, as its property is not supported.
		propertyHandlers.remove("Description");
		// Replace default property names with custom ones.
		propertyHandlers.put("connId", propertyHandlers.remove("Name"));
		propertyHandlers.put("url", propertyHandlers.remove("Host"));
		propertyHandlers.put("port", propertyHandlers.remove("Port"));
		propertyHandlers.put("visDir", propertyHandlers.remove("Path"));
		// Add a property handler for the proxy name and port.
		propertyHandlers.put("gateway", new IPropertyHandler() {
			@Override
			public boolean validateValue(String value) {
				// Accept any non-null strings.
				return value != null;
			}
		});
		propertyHandlers.put("localGatewayPort", propertyHandlers.get("port"));
		// Add a property handler for the VisIt session username.
		propertyHandlers.put("username", new IPropertyHandler() {
			@Override
			public boolean validateValue(String value) {
				// Accept any non-null strings.
				return value != null;
			}
		});
		// Add a property handler for the VisIt session password.
		propertyHandlers.put("password", new IPropertyHandler() {
			@Override
			public boolean validateValue(String value) {
				return false;
			}
		});

		// Set up a property handler that accepts positive, non-zero integers.
		IPropertyHandler positiveIntHandler = new IPropertyHandler() {
			@Override
			public boolean validateValue(String value) {
				boolean valid = false;
				if (value != null) {
					String trimmedValue = value.trim();
					try {
						if (Integer.parseInt(trimmedValue) > 0) {
							valid = true;
						}
					} catch (NumberFormatException e) {
						// Invalid value.
					}
				}
				return valid;
			}
		};
		// Add a property handler for the window width and height.
		propertyHandlers.put("windowWidth", positiveIntHandler);
		propertyHandlers.put("windowHeight", positiveIntHandler);
		// Add a property handler for the window ID.
		propertyHandlers.put("windowId", new IPropertyHandler() {
			@Override
			public boolean validateValue(String value) {
				boolean valid = false;
				if (value != null) {
					String trimmedValue = value.trim();
					try {
						int intValue = Integer.parseInt(trimmedValue);
						// The window ID can be -1 (lets the manager decided) or
						// a number between 1 and 16, inclusive.
						if (intValue == -1
								|| (intValue >= 1 && intValue <= 16)) {
							valid = true;
						}
					} catch (NumberFormatException e) {
						// Invalid value.
					}
				}
				return valid;
			}
		});

		// Set default values for additional properties. Do this before the
		// validators are applied so that read-only properties will be set.
		setProperty("localGatewayPort", "22");
		setProperty("windowWidth", "1340");
		setProperty("windowHeight", "1020");
		setProperty("windowId", "1");
		// Change the default port to 9600.
		setPort(9600);

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#connectToWidget()
	 */
	@Override
	protected VisItSwtConnection connectToWidget() {
		String key = getName();
		Shell shell = createDefaultShell();
		Map<String, String> properties = getProperties();

		// Add fixed properties that are only required for connecting and cannot
		// be changed.
		properties.put("isLaunch", "true");
		properties.put("useTunneling",
				"localhost".equals(getHost()) ? "false" : "true");

		return VisItSwtConnectionManager.createConnection(key, shell,
				properties);
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#disconnectFromWidget(java.lang.Object)
	 */
	@Override
	protected boolean disconnectFromWidget(VisItSwtConnection widget) {
		boolean closed = false;
		if (widget != null) {
			widget.close();
			closed = true;
		}
		return closed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#getDescription()
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#getHost()
	 */
	@Override
	public String getHost() {
		return getProperty("url");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#getName()
	 */
	@Override
	public String getName() {
		return getProperty("connId");
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
//		if (getState() == ConnectionState.Connected) {
//			// The order of the returned list is not guaranteed. Throw it into
//			// an ordered set and get the lowest positive ID not in the set.
//			Set<Integer> ids = new HashSet<Integer>(getWidget().getWindowIds());
//			// Find the first integer not in the set.
//			while (ids.contains(windowId)) {
//				windowId++;
//			}
//		} else {
//			windowId = -1;
//		}
		return windowId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#getPath()
	 */
	@Override
	public String getPath() {
		return getProperty("visDir");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#getPort()
	 */
	@Override
	public int getPort() {
		return Integer.parseInt(getProperty("port"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#setDescription(java.lang.String)
	 */
	@Override
	public boolean setDescription(String description) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#setHost(java.lang.String)
	 */
	@Override
	public boolean setHost(String host) {
		return setProperty("url", host);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#setName(java.lang.String)
	 */
	@Override
	public boolean setName(String name) {
		return setProperty("connId", name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#setPath(java.lang.String)
	 */
	@Override
	public boolean setPath(String path) {
		return setProperty("visDir", path);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#setPort(int)
	 */
	@Override
	public boolean setPort(int port) {
		return setProperty("port", Integer.toString(port));
	}
}
