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
package org.eclipse.ice.viz.service.visit.connections;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.connections.VizConnection;
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
			public String validateValue(String value) {
				// Accept any non-null strings.
				String newValue = null;
				if (value != null) {
					newValue = value.trim();
				}
				return newValue;
			}
		});
		propertyHandlers.put("localGatewayPort", propertyHandlers.get("port"));
		// Add a property handler for the VisIt session username.
		propertyHandlers.put("username", new IPropertyHandler() {
			@Override
			public String validateValue(String value) {
				// Accept any non-null strings.
				String newValue = null;
				if (value != null) {
					newValue = value.trim();
				}
				return newValue;
			}
		});

		return;
	}

	/*
	 * Implements an abstract method from VizConnection.
	 */
	@Override
	protected VisItSwtConnection connectToWidget() {
		String key = getName();
		Shell shell = createDefaultShell();
		Map<String, String> properties = getProperties();

		// Add fixed properties that cannot yet be changed.
		properties.put("password", "notused");
		properties.put("windowWidth", "1340");
		properties.put("windowHeight", "1020");
		properties.put("windowId", "1");
		properties.put("isLaunch", "true");
		properties.put("useTunneling", "localhost".equals(getHost()) ? "false" : "true");

		return VisItSwtConnectionManager.createConnection(key, shell, properties);
	}

	/*
	 * Implements an abstract method from VizConnection.
	 */
	@Override
	protected boolean disconnectFromWidget(VisItSwtConnection widget) {
		widget.close();
		return true;
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
	 * Overrides a method from VizConnection.
	 */
	@Override
	public String getName() {
		return getProperty("connId");
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public String getHost() {
		return getProperty("url");
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public int getPort() {
		return Integer.parseInt(getProperty("port"));
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public String getPath() {
		return getProperty("visDir");
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public boolean setName(String name) {
		return setProperty("connId", name);
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public boolean setDescription(String description) {
		return false;
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public boolean setHost(String host) {
		return setProperty("url", host);
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public boolean setPort(int port) {
		return setProperty("port", Integer.toString(port));
	}

	/*
	 * Overrides a method from VizConnection.
	 */
	@Override
	public boolean setPath(String path) {
		return setProperty("visDir", path);
	}

}
