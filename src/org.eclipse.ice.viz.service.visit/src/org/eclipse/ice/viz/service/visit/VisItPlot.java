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
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
	 * The visualization service responsible for maintaining
	 * {@link VisItSwtConnection}s.
	 */
	private final VisItVizService service;

	/**
	 * The VisIt connection (either local or remote) that powers any VisIt
	 * widgets required for this plot.
	 */
	private VisItSwtConnection connection;

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

		// FIXME For now, assume that the service is connected...
		connection = service.getDefaultConnection();
		if (connection == null) {
			System.err.println("THIS SHOULD NOT BE NULL!!!");
		}
		connection.getViewerMethods().openDatabase(sourcePath);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {

		// // Determine the VisIt FileInfo for the data source.
		// ViewerMethods methods = connection.getViewerMethods();
		// methods.openDatabase(sourcePath);
		// FileInfo info = methods.getDatabaseInfo();
		//
		// // Get all of the plot types and plots in the file.
		List<String> plots;
		Map<String, String[]> plotTypes = new TreeMap<String, String[]>();
		// plots = info.getMeshes();
		// plotTypes.put("Mesh", plots.toArray(new String[plots.size()]));
		// plots = info.getMaterials();
		// plotTypes.put("Material", plots.toArray(new String[plots.size()]));
		// plots = info.getScalars();
		// plotTypes.put("Scalar", plots.toArray(new String[plots.size()]));
		// plots = info.getVectors();
		// plotTypes.put("Vector", plots.toArray(new String[plots.size()]));

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
		return (canvas == null ? 0 : 3);
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
		return preferences.get(ConnectionPreference.Host.getID());
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
		// FIXME Add these null checks back...
		if (/* category == null || plotType == null || */parent == null) {
			throw new NullPointerException("VisItPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED,
					"VisItPlot error: "
							+ "Cannot draw plot inside disposed Composite.");
		}

		// Update the reference to the parent Composite.
		this.parent = parent;
		
		update();

		// Display display = parent.getDisplay();
		//
		// // Draw a temporary Composite/Label that informs the user of progress
		// // drawing the VisIt plot.
		// Composite infoComposite = new Composite(parent, SWT.BORDER);
		// infoComposite.setLayout(new GridLayout(1, false));
		// Label infoLabel = new Label(infoComposite, SWT.NONE);
		// infoLabel.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		// infoLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
		// false, false));
		// infoLabel.setText("Getting VisIt connection...");
		//
		// // Create the VisIt Canvas if necessary.
		// if (canvas == null) {
		// canvas = createCanvas(parent, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		// // Create a mouse manager to handle mouse events inside the canvas.
		// new VisItMouseManager(canvas);
		// }
		// // Make sure the Canvas is activated.
		// canvas.activate();
		//
		// // Draw the specified plot on the Canvas.
		// ViewerMethods widget = canvas.getViewerMethods();
		// widget.deleteActivePlots();
		// widget.addPlot(category, plotType);
		// widget.drawPlots();

		return;
	}

	/**
	 * This method updates the plot based on the current state of the
	 * VizService.
	 * <p>
	 * <b>Note:</b> This method assumes that it is called on the UI thread!
	 * </p>
	 */
	private void update() {

		final Display display = parent.getDisplay();
		final Shell shell = parent.getShell();

		if (connection != null) {
			// If the canvas was not previously available, dispose all
			// contributed controls and create the canvas.
			if (canvas == null) {
				// Dispose all previous controls.
				for (Control control : contributedControls) {
					control.dispose();
				}
				contributedControls.clear();

				// Create the canvas.
				canvas = createCanvas(parent, SWT.BORDER | SWT.DOUBLE_BUFFERED);
				// Create a mouse manager to handle mouse events inside the
				// canvas.
				new VisItMouseManager(canvas);

				// Add the canvas as a contributed control. It might need to be
				// disposed later.
				contributedControls.add(canvas);
			}

			// Make sure the Canvas is activated.
			canvas.activate();

			// Draw the specified plot on the Canvas.
			ViewerMethods widget = canvas.getViewerMethods();
			widget.deleteActivePlots();
			// FIXME This will need to be changed to whatever the client wants.
			widget.addPlot("Mesh", "Mesh");
			widget.drawPlots();
		} else {

			// Dispose all previous controls.
			for (Control control : contributedControls) {
				control.dispose();
			}
			contributedControls.clear();

			// The service is not connected. Notify the user and give them a
			// link to the preferences.
			Composite infoComposite = new Composite(parent, SWT.NONE);
			infoComposite.setLayout(new GridLayout(2, false));
			// Create an info label with an image.
			Label iconLabel = new Label(infoComposite, SWT.NONE);
			iconLabel.setImage(Display.getCurrent().getSystemImage(
					SWT.ICON_INFORMATION));
			iconLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false));
			// Create a Composite to contain the info message and the hyperlink
			// with the info message above the hyperlink.
			Composite msgComposite = new Composite(infoComposite, SWT.NONE);
			msgComposite.setLayoutData(new GridData(SWT.BEGINNING,
					SWT.BEGINNING, false, false));
			msgComposite.setLayout(new GridLayout(1, false));
			// Create an info label with text and a hyperlink.
			String message = "There is currently no connection to VisIt";
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

			// TODO We will need to devise some way to handle this...
			// // In case the service is just trying to connect, add a hook so
			// the
			// // plot will update when the connection is established.
			// // Get the Display from the info Composite.
			// final Display display = infoComposite.getDisplay();
			// vizService.addClient(new Runnable() {
			// @Override
			// public void run() {
			// // The service has updated (connected). Refresh!
			// // Note: This has to be done on the UI thread! We use async
			// // exec because this thread won't need to do anything
			// // afterward.
			// display.asyncExec(new Runnable() {
			// @Override
			// public void run() {
			// vizServiceUpdated();
			// }
			// });
			// return;
			// }
			// });
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
	 * @return The new VisIt {@code Canvas}
	 */
	private VisItSwtWidget createCanvas(Composite parent, int style) {

		// Create the canvas.
		VisItSwtWidget canvas = new VisItSwtWidget(parent, style);

		Map<String, String> connectionPreferences = service.getConnectionProperties();
		
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

}
