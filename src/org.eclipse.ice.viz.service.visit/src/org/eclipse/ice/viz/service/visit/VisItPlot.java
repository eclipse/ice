/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
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
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionAdapter;
import org.eclipse.ice.viz.service.visit.actions.ImportLocalFileAction;
import org.eclipse.jface.action.IAction;
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
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItPlot implements IPlot, IUpdateableListener {

	// ---- Service and Connection ---- //
	/**
	 * The visualization service responsible for maintaining
	 * {@link VisItSwtConnection}s.
	 */
	private final VisItVizService service;
	/**
	 * The adapter currently assigned to this plot. It provides the required
	 * access to the VisIt connection API.
	 */
	private VisItConnectionAdapter adapter;
	// -------------------------------- //

	// ---- Source and Plot Properties ---- //
	// TODO We should eventually provide access to some plot properties.
	/**
	 * The data source, either a local or remote file.
	 */
	private URI source;
	/**
	 * The source path required by the VisIt widgets.
	 */
	private String sourcePath;

	/**
	 * The category of the currently drawn plot.
	 */
	private String plotCategory = null;
	/**
	 * The type of the currently drawn plot.
	 */
	private String plotType = null;
	// ------------------------------------ //

	// ---- UI Widgets ---- //
	/**
	 * The current parent {@code Composite} in which either the {@link #canvas}
	 * or {@link #infoComopsite} is rendered.
	 */
	private Composite parent;
	/**
	 * The current VisIt widget used to draw VisIt plots. This should only be
	 * visible if the connection is open.
	 */
	private VisItSwtWidget canvas = null;
	/**
	 * This presents the user with helpful information about the status of the
	 * associated VisIt connection. It should only be visible if the connection
	 * is *not* open.
	 */
	private Composite infoComposite = null;

	// -------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param service
	 *            The visualization service responsible for maintaining
	 *            {@link VisItSwtConnection}s.
	 * @param file
	 *            The data source, either a local or remote file.
	 */
	public VisItPlot(VisItVizService service, URI file) {
		this.service = service;
		// TODO Make the adapter configurable, e.g., let the user pick from
		// available stored connections.
		adapter = service.getAdapter();
		// Register the plot as a listener so it can receive connection
		// update events.
		adapter.register(this);

		// Set the data source.
		setDataSource(file);

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

		if (adapter.getState() == ConnectionState.Connected
				&& sourcePath != null) {
			// Determine the VisIt FileInfo for the data source.
			ViewerMethods methods = adapter.getConnection().getViewerMethods();
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
		return (canvas == null || canvas.isDisposed() ? 0 : 3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
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
		// Nothing to do yet.
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
		return adapter.getConnectionProperty("url");
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
		} else if (this.parent != null && this.parent != parent) {
			throw new IllegalArgumentException("VisItPlot error: "
					+ "Cannot draw same plot in multiple Composites.");
		}

		// Update the plot category and type.
		// TODO Use the specified plot category and type.
		this.plotCategory = "Mesh";
		this.plotType = "Mesh";

		// Update the reference to the parent Composite.
		this.parent = parent;

		// Update the drawn plot.
		update(adapter.getState(), adapter.getConnection());

		return;
	}

	/**
	 * Sets the data source (which is currently rendered if the plot is drawn).
	 * If the data source is valid and new, then the plot will be updated
	 * accordingly.
	 * 
	 * @param uri
	 *            The new data source URI.
	 */
	public void setDataSource(URI uri) {
		if (uri != null) {
			// Update the source.
			source = uri;
			// On Windows, the File class inserts standard forward slashes as
			// separators. VisIt, on the other hand, requires the native
			// separator. If the URI uses the standard separator on Windows,
			// update the source path to use the native Windows separator.
			String path = source.getPath();
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				if (path.startsWith("/")) {
					path = path.substring(1);
					path = path.replace("/",
							System.getProperty("file.separator"));
				}
			}
			// Update the source's VisIt-friendly path.
			if (!path.equals(sourcePath)) {
				sourcePath = path;

				// Trigger an update to the UI since the source changed.
				update(adapter);
			}
		}
		return;
	}

	/**
	 * This method informs the plot that its associated connection has been
	 * updated. The plot can then update its contents if it has contributed to
	 * the UI.
	 * 
	 * @param component
	 *            The component that was updated. This is expected to be the
	 *            associated {@link VisItConnectionAdapter}.
	 */
	@Override
	public void update(IUpdateable component) {
		if (component == adapter) {

			final ConnectionState state = adapter.getState();
			final VisItSwtConnection connection = adapter.getConnection();
			// Trigger an update to the UI.
			if (parent != null && !parent.isDisposed()) {
				parent.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						update(state, connection);
					}
				});
			}
		}
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

		// If connected, try to render the plot in the VisItSwtWidget/canvas.
		if (state == ConnectionState.Connected) {

			// If necessary, dispose the infoComposite.
			if (infoComposite != null && !infoComposite.isDisposed()) {
				infoComposite.dispose();
				infoComposite = null;
			}

			// If necessary, create the canvas.
			if (canvas == null) {
				// Create the canvas.
				canvas = createCanvas(parent, SWT.DOUBLE_BUFFERED, connection);
				// Create a mouse manager to handle mouse events inside the
				// canvas.
				new VisItMouseManager(canvas);

				// Refresh the parent since its size likely changed due to the
				// new canvas.
				parent.layout();
			}

			// Make sure the Canvas is activated.
			canvas.activate();

			System.out.println("VisItPlot message: " + "Drawing plot "
					+ plotCategory + " - " + plotType + " for source file \""
					+ sourcePath + "\".");

			// Draw the specified plot on the Canvas.
			ViewerMethods widget = canvas.getViewerMethods();
			widget.openDatabase(sourcePath);
			widget.deleteActivePlots();
			widget.addPlot(plotCategory, plotType);
			widget.drawPlots();
		}
		// Otherwise, there is a problem of some sort. Give the user a link to
		// the VisIt preferences along with an informative message.
		else {

			// Set the message and icon based on the state of the connection.
			final String message;
			final String linkMessage = "Click here to update VisIt connection preferences.";
			final Image image;
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

			// The widgets used in the infoComposite...
			Label iconLabel; // A helpful icon based on severity of the problem.
			Composite msgComposite; // Contains a message and link.
			Label msgLabel; // A helpful message about the problem.
			Hyperlink prefLink; // A link to VisIt's connection preferences.

			// If necessary, dispose the canvas and create the infoComposite.
			if (canvas != null && !canvas.isDisposed()) {
				canvas.dispose();
				canvas = null;
			}

			// If necessary, create the infoComposite.
			if (infoComposite == null) {
				infoComposite = new Composite(parent, SWT.NONE);
				infoComposite.setLayout(new GridLayout(2, false));

				// Create an info label with an image.
				iconLabel = new Label(infoComposite, SWT.NONE);
				iconLabel.setLayoutData(new GridData(SWT.BEGINNING,
						SWT.BEGINNING, false, false));

				// Create a Composite to contain the info message and the
				// hyperlink
				// with the info message above the hyperlink.
				msgComposite = new Composite(infoComposite, SWT.NONE);
				msgComposite.setLayoutData(new GridData(SWT.BEGINNING,
						SWT.BEGINNING, false, false));
				msgComposite.setLayout(new GridLayout(1, false));

				// Create an info label with informative text.
				msgLabel = new Label(msgComposite, SWT.NONE);
				msgLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
						false, false));

				// Create a link to the preference page.
				prefLink = new Hyperlink(msgComposite, SWT.NONE);
				prefLink.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
						false, false));
				prefLink.setUnderlined(true);
				prefLink.setForeground(display
						.getSystemColor(SWT.COLOR_LINK_FOREGROUND));
				// Add the listener to redirect the user to the preferences.
				prefLink.addHyperlinkListener(new IHyperlinkListener() {
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
						PreferencesUtil
								.createPreferenceDialogOn(
										shell,
										"org.eclipse.ice.viz.service.visit.preferences",
										null, null).open();
					}
				});
			}
			// Since the infoComposite exists, we don't need to recreate
			// anything. Just get references to them so we can update them.
			else {
				Control[] children = infoComposite.getChildren();
				iconLabel = (Label) children[0];
				// Since the message and link are children of msgComposite, we
				// need to get them from the msgComposite's array of children.
				msgComposite = (Composite) children[1];
				children = msgComposite.getChildren();
				msgLabel = (Label) children[0];
				prefLink = (Hyperlink) children[1];
			}

			// Update the contents of the infoComposite's widgets.
			iconLabel.setImage(image);
			msgLabel.setText(message);
			prefLink.setText(linkMessage);

			// Refresh the parent in case the widget text has an influence on
			// the layout specifics.
			parent.layout();
			// We also have to update the msgComposite so the labels take up the
			// space they need and are not cut off.
			msgComposite.layout();
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

		// Set the VisIt connection info. It requres a valid VisItSwtConnection
		// and 3 integers (a window ID, width, and height).
		int windowId = Integer.parseInt(adapter
				.getConnectionProperty("windowId"));
		int windowWidth = Integer.parseInt(adapter
				.getConnectionProperty("windowWidth"));
		int windowHeight = Integer.parseInt(adapter
				.getConnectionProperty("windowHeight"));

		try {
			canvas.setVisItSwtConnection(connection, windowId, windowWidth,
					windowHeight);
		} catch (ConnectException e) {
			System.err.println("VisItPlot error: "
					+ "Could not set connection for VisIt Canvas.");
			e.printStackTrace();
		}

		return canvas;
	}

	/**
	 * Disposes of the plot and any resources it consumes. This includes
	 * unregistering it from its associated connection adapter.
	 */
	protected void dispose() {
		// Unregister from the connection adapter.
		adapter.unregister(this);
		adapter = null;

		// If possible, inform the UI to dispose the plot's UI contributions.
		if (parent != null && !parent.isDisposed()) {
			parent.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (canvas != null && !canvas.isDisposed()) {
						canvas.dispose();
					}
					if (infoComposite != null && !infoComposite.isDisposed()) {
						infoComposite.dispose();
					}
					return;
				}
			});
		}

		return;
	}

	// TODO This should be standardized somehow...
	public List<IAction> getActions() {
		List<IAction> actions = new ArrayList<IAction>();

		actions.add(new ImportLocalFileAction(this));

		return actions;
	}
}
