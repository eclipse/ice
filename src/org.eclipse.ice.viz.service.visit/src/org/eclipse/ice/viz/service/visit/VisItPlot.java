/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtWidget;

import java.net.ConnectException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.service.visit.VisItVizService.ConnectionState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;

import visit.java.client.FileInfo;
import visit.java.client.ViewerMethods;

/**
 * This class provides the VisIt implementation for an IPlot.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItPlot implements IPlot {

	/**
	 * The map of preferences. Currently, this stores connection preferences.
	 */
	private final Map<String, String> preferences = new HashMap<String, String>();

	/**
	 * The data source, either a local or remote file.
	 */
	private final URI source;
	/**
	 * The source path required by the VisIt widgets.
	 */
	private final String sourcePath;

	/**
	 * The category of the currently drawn plot.
	 */
	private String plotCategory = null;
	/**
	 * The type of the currently drawn plot.
	 */
	private String plotType = null;

	/**
	 * The visualization service responsible for maintaining
	 * {@link VisItSwtConnection}s.
	 */
	private final VisItVizService service;

	/**
	 * The VisIt connection (either local or remote) that powers any VisIt
	 * widgets required for this plot.
	 */
	private VisItSwtConnection connection = null;

	/**
	 * The current VisIt widget used to draw VisIt plots.
	 */
	private VisItSwtWidget canvas = null;

	private Composite parent;

	private final List<Control> contributedControls = new ArrayList<Control>();

	/**
	 * The default constructor.
	 * 
	 * @param source
	 *            The data source, either a local or remote file.
	 * @param service
	 *            The visualization service responsible for maintaining
	 *            {@link VisItSwtConnection}s..
	 */
	public VisItPlot(URI source, VisItVizService service) {
		this.source = source;
		this.service = service;

		// On Windows, the File class inserts standard forward slashes as
		// separators. VisIt, on the other hand, requires the native separator.
		// If the URI uses the standard separator on Windows, update the source
		// path to use the native Windows separator.
		String path = source.getPath();
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			if (path.startsWith("/")) {
				path = path.substring(1);
				path = path.replace("/", System.getProperty("file.separator"));
			}
		}
		sourcePath = path;

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {

		Map<String, String[]> plotTypes = new TreeMap<String, String[]>();

		VisItSwtConnection connection = getConnection();
		if (connection != null) {

			// Determine the VisIt FileInfo for the data source.
			ViewerMethods methods = connection.getViewerMethods();
			methods.openDatabase(sourcePath);
			FileInfo info = methods.getDatabaseInfo();

			// Get all of the plot types and plots in the file.
			List<String> plots;
			plots = info.getMeshes();
			plotTypes.put("Mesh", plots.toArray(new String[plots.size()]));
			plots = info.getMaterials();
			plotTypes.put("Material", plots.toArray(new String[plots.size()]));
			plots = info.getScalars();
			plotTypes.put("Scalar", plots.toArray(new String[plots.size()]));
			plots = info.getVectors();
			plotTypes.put("Vector", plots.toArray(new String[plots.size()]));
		}

		return plotTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// TODO Should we query the plot somehow?
		return (canvas == null || canvas.isDisposed() ? 0 : 3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return preferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#setProperties(java.util
	 * .Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		if (props != null) {
			preferences.putAll(props);
			// TODO Update the drawn plots.
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		// For now, we only support local launches...
		return "localhost";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return "localhost".equals(getSourceHost());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void draw(String category, String plotType, Composite parent)
			throws Exception {

		// Check the parameters.
		if (category == null || plotType == null || parent == null) {
			throw new NullPointerException("VisItPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED,
					"VisItPlot error: "
							+ "Cannot draw plot inside disposed Composite.");
		}

		// FIXME What should we do when the plot is drawn in two places?

		// Update the plot category and type.
		this.plotCategory = category;
		this.plotType = plotType;

		// Update the reference to the parent Composite.
		this.parent = parent;

		// Update the drawn plot.
		update(service.getConnectionState(), getConnection());

		return;
	}

	/**
	 * This method updates the plot based on the current state of the
	 * VizService.
	 * <p>
	 * <b>Note:</b> This method assumes that it is called on the UI thread!
	 * </p>
	 */
	private void update(ConnectionState state, VisItSwtConnection connection) {

		final Display display = parent.getDisplay();
		final Shell shell = parent.getShell();

		// This should never happen, but if it does, we should report an error
		// and not break!
		if (connection == null && state == ConnectionState.Connected) {
			System.err
					.println("VisItPlot error: "
							+ "The connection is not available, although it was reported as connected.");
			state = ConnectionState.Failed;
		}

		System.out.println("VisItPlot message: "
				+ "Updating plot. The current connection state is \"" + state
				+ "\".");

		// If connected, try to render the plot in the VisItSwtWidget/canvas.
		if (state == ConnectionState.Connected) {
			// If the canvas was not previously available, dispose all
			// contributed controls and create the canvas.
			if (canvas == null) {
				// Dispose all previous controls.
				for (Control control : contributedControls) {
					control.dispose();
				}
				contributedControls.clear();

				// Create the canvas.
				canvas = createCanvas(parent, SWT.DOUBLE_BUFFERED, connection);
				// Create a mouse manager to handle mouse events inside the
				// canvas.
				new VisItMouseManager(canvas);

				// Add the canvas as a contributed control. It might need to be
				// disposed later.
				contributedControls.add(canvas);
//				Composite c = new Composite(parent, SWT.NONE);
//				c.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
				parent.layout();
			}

			System.out.println("VisItPlot message: " + "Drawing the plots...");
			
			// Make sure the Canvas is activated.
			canvas.activate();

			// Draw the specified plot on the Canvas.
			ViewerMethods widget = canvas.getViewerMethods();
			widget.deleteActivePlots();
			// FIXME This will need to be changed to whatever the client wants.
			widget.addPlot("Mesh", "Mesh");
			widget.drawPlots();
		}
		// Otherwise, there is a problem of some sort. Give the user a link to
		// the VisIt preferences along with an informative message.
		else {

			// Dispose all previous controls.
			for (Control control : contributedControls) {
				control.dispose();
			}
			contributedControls.clear();

			// Set the message and icon based on the state of the connection.
			String message;
			Image image;
			if (state == ConnectionState.Connecting) {
				message = "The VisIt connection is being established...";
				image = display.getSystemImage(SWT.ICON_WORKING);
			} else if (state == ConnectionState.Disconnected) {
				message = "The VisIt connection is currently unavailable.";
				image = display.getSystemImage(SWT.ICON_WARNING);
			} else {
				message = "The VisIt connection failed!";
				image = display.getSystemImage(SWT.ICON_ERROR);
			}

			// The service is not connected. Notify the user and give them a
			// link to the preferences.
			Composite infoComposite = new Composite(parent, SWT.NONE);
			infoComposite.setLayout(new GridLayout(2, false));
			// Create an info label with an image.
			Label iconLabel = new Label(infoComposite, SWT.NONE);
			iconLabel.setImage(image);
			iconLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false));
			// Create a Composite to contain the info message and the hyperlink
			// with the info message above the hyperlink.
			Composite msgComposite = new Composite(infoComposite, SWT.NONE);
			msgComposite.setLayoutData(new GridData(SWT.BEGINNING,
					SWT.BEGINNING, false, false));
			msgComposite.setLayout(new GridLayout(1, false));
			// Create an info label with text and a hyperlink.
			Label msgLabel = new Label(msgComposite, SWT.NONE);
			msgLabel.setText(message);
			msgLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
					false, false));
			// Create a link to the preference page.
			message = "Click here to update VisIt connection preferences.";
			Hyperlink link = new Hyperlink(msgComposite, SWT.NONE);
			link.setText(message);
			link.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
					false));
			link.setUnderlined(true);
			link.setForeground(display
					.getSystemColor(SWT.COLOR_LINK_FOREGROUND));
			// Add the listener to redirect the user to the preferences.
			link.addHyperlinkListener(new IHyperlinkListener() {
				@Override
				public void linkEntered(HyperlinkEvent e) {
					// Nothing to do yet.
				}

				@Override
				public void linkExited(HyperlinkEvent e) {
					// Nothing to do yet.
				}

				@Override
				public void linkActivated(HyperlinkEvent e) {
					// Open up the VisIt preferences.
					PreferencesUtil.createPreferenceDialogOn(shell,
							"org.eclipse.ice.viz.service.visit.preferences",
							null, null).open();
				}
			});

			// Add the infoComposite to the contributed controls.
			contributedControls.add(infoComposite);

			parent.layout();
		}
		return;
	}

	/**
	 * Creates a new VisIt {@code Canvas} that can render components via VisIt.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the canvas.
	 * @param style
	 *            The SWT style to use for the VisIt {@code Canvas}.
	 * @param connection
	 *            The connection used to populate the {@code Canvas} graphics.
	 * @return The new VisIt {@code Canvas}
	 */
	private VisItSwtWidget createCanvas(Composite parent, int style,
			VisItSwtConnection connection) {

		// Create the canvas.
		VisItSwtWidget canvas = new VisItSwtWidget(parent, style);

		Map<String, String> connectionPreferences = service
				.getConnectionProperties();

		// Set the VisIt connection info. It requres a valid VisItSwtConnection
		// and 3 integers (a window ID, width, and height).
		int windowId = Integer.parseInt(connectionPreferences
				.get(ConnectionPreference.WindowID.toString()));
		int windowWidth = Integer.parseInt(connectionPreferences
				.get(ConnectionPreference.WindowWidth.toString()));
		int windowHeight = Integer.parseInt(connectionPreferences
				.get(ConnectionPreference.WindowHeight.toString()));
		try {
			canvas.setVisItSwtConnection(connection, windowId, windowWidth,
					windowHeight);
		} catch (ConnectException e) {
			System.out.println("VisItPlot error: "
					+ "Could not set connection for VisIt Canvas.");
			e.printStackTrace();
		}

		return canvas;
	}

	protected void updateConnection(String connectionId, ConnectionState state) {
		final String connId = connectionId;
		final ConnectionState connState = state;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				update(connState, service.getConnection(connId));
			}
		});
		return;
	}

	private VisItSwtConnection getConnection() {
		if (connection == null) {
			connection = service.getDefaultConnection();
		}
		return connection;
	}
}
