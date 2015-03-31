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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
	 * This composite contains the {@link #canvas} and {@link #infoComposite} in
	 * a stack.
	 */
	private Composite plotComposite = null;
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
	/**
	 * Displays an icon to demonstrate the severity of the message.
	 */
	private Label iconLabel;
	/**
	 * Displays a message explaining the current state of the connection or why
	 * it cannot render anything.
	 */
	private Label msgLabel;
	/**
	 * A link to the visualization connection preferences.
	 */
	private Hyperlink link;

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

		// Set the data source.
		setDataSource(file);

		return;
	}

	/**
	 * Sets the current connection associated with the plot.
	 * 
	 * @param adapter
	 *            The new connection adapter. If {@code null}, the connection
	 *            will be unset and the plot will be cleared.
	 */
	protected void setConnection(VisItConnectionAdapter adapter) {
		if (adapter != this.adapter) {
			if (this.adapter != null) {
				this.adapter.unregister(this);
			}
			this.adapter = adapter;

			// Trigger an update to the UI.
			updateUI();

			// Register for updates from the adapter if possible.
			if (adapter != null) {
				adapter.register(this);
			}
		}
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

		if (adapter != null && adapter.getState() == ConnectionState.Connected
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
		return (adapter != null ? adapter.getConnectionProperty("url") : null);
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

		// Get the current parent control.
		Composite currentParent = (plotComposite != null ? plotComposite
				.getParent() : null);

		// Check the parameters.
		if (category == null || plotType == null || parent == null) {
			throw new NullPointerException("VisItPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED,
					"VisItPlot error: "
							+ "Cannot draw plot inside disposed Composite.");
		} else if (currentParent != null && parent != currentParent) {
			throw new IllegalArgumentException("VisItPlot error: "
					+ "Cannot draw same plot in multiple Composites.");
		}

		// Update the plot category and type.
		// TODO Use the specified plot category and type.
		this.plotCategory = "Mesh";
		this.plotType = "Mesh";

		// Create the plot Composite.
		plotComposite = new Composite(parent, SWT.NONE);
		plotComposite.setFont(parent.getFont());
		plotComposite.setBackground(parent.getBackground());
		plotComposite.setLayout(new StackLayout());

		// Trigger an update to the UI.
		updateUI();

		return;
	}

	/**
	 * Gets the path to the specified file.
	 * 
	 * @param source
	 *            The source URI.
	 * @return A VisIt-friendly file path.
	 */
	protected static String getSourcePath(URI source) {
		String path = null;
		if (source != null) {
			// On Windows, the File class inserts standard forward slashes as
			// separators. VisIt, on the other hand, requires the native
			// separator. If the URI uses the standard separator on Windows,
			// update the source path to use the native Windows separator.
			path = source.getPath();

			// If the host is local and a Windows-based machine, we need to
			// update the path to use Windows separators for VisIt.
			String host = source.getHost();
			// TODO VisIt should just be able to handle a raw URI... The code
			// below can't handle remote Windows machines.
			if ((host == null || "localhost".equals(host))
					&& System.getProperty("os.name").toLowerCase()
							.contains("windows")) {
				if (path.startsWith("/")) {
					path = path.substring(1);
					path = path.replace("/",
							System.getProperty("file.separator"));
				}
			}
		}
		return path;
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

			String path = getSourcePath(uri);

			// Update the source's VisIt-friendly path.
			if (!path.equals(sourcePath)) {
				sourcePath = path;

				// Trigger an update to the UI.
				updateUI();
			}
		} else if (source != null) {
			source = null;
			sourcePath = null;

			// Trigger an update to the UI.
			updateUI();
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
		// If the argument is null, then do nothing. Even if the current adapter
		// is null, the UI should already be up to date!
		if (component != null && component == adapter) {
			// Trigger an update to the UI.
			updateUI();
		}
	}

	/**
	 * This method updates the UI component of the plot based on the current
	 * state of the VizService. This method should be used any time a UI update
	 * needs to be triggered, e.g., when the data source changes or when the
	 * underlying connection is updated.
	 */
	private void updateUI() {

		// Determine the current connection and its state.
		final ConnectionState state;
		final VisItSwtConnection connection;
		if (adapter != null) {
			state = adapter.getState();
			connection = adapter.getConnection();
		} else {
			state = ConnectionState.Disconnected;
			connection = null;
		}

		// Trigger an update to the UI.
		if (plotComposite != null && !plotComposite.isDisposed()) {
			// If we are not on the UI thread, update the UI asynchronously on
			// the UI thread.
			if (Display.getCurrent() == null) {
				plotComposite.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						updateUI(state, connection);
					}
				});
			}
			// If we are on the UI thread, update the UI synchronously.
			else {
				updateUI(state, connection);
			}
		}

		return;
	}

	/**
	 * This method updates the plot based on the current state of the
	 * VizService. This method should <i>not</i> be used. Instead, use
	 * {@link #updateUI()}.
	 * <p>
	 * <b>Note:</b> This method assumes that it is called on the UI thread!
	 * </p>
	 */
	private void updateUI(ConnectionState state, VisItSwtConnection connection) {

		final Display display = plotComposite.getDisplay();

		// Get the StackLayout from the plot Composite.
		final StackLayout stackLayout = (StackLayout) plotComposite.getLayout();

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

			// If necessary, create the canvas.
			if (canvas == null) {
				// Create the canvas.
				canvas = createCanvas(plotComposite, SWT.DOUBLE_BUFFERED,
						connection);
				// Create a mouse manager to handle mouse events inside the
				// canvas.
				new VisItMouseManager(canvas);
			}

			// Make sure the Canvas is activated.
			canvas.activate();

			System.out.println("VisItPlot message: " + "Drawing plot "
					+ plotCategory + " - " + plotType + " for source file \""
					+ sourcePath + "\".");

			// Draw the specified plot on the Canvas.
			ViewerMethods widget = canvas.getViewerMethods();

			// Remove all existing plots.
			widget.deleteActivePlots();

			// If the source is not set, then we need to display a helpful
			// message.
			if (source == null) {
				// Set the message and icon.
				String message = "No file specified or the file could not be found.";
				Image image = display.getSystemImage(SWT.ICON_WARNING);

				// If necessary, create the infoComposite.
				if (infoComposite == null) {
					infoComposite = createInfoComposite(plotComposite, SWT.NONE);
				}
				// Update the contents of the infoComposite's widgets.
				iconLabel.setImage(image);
				msgLabel.setText(message);
				// Hide the link since the problem is not a connection issue.
				link.setVisible(false);
				// Make the infoComposite appear in front.
				stackLayout.topControl = infoComposite;
				plotComposite.layout(true, true);
				// The above layout requires the two boolean flags to make sure
				// the text widgets are also laid out.
			} else {
				widget.openDatabase(sourcePath);
				// TODO How do we handle paths that are invalid?
				widget.addPlot(plotCategory, plotType);
				widget.drawPlots();

				// Make the Canvas appear in front. This only needs to be done
				// if it's not already at the top.
				if (stackLayout.topControl != canvas) {
					stackLayout.topControl = canvas;
					plotComposite.layout();
				}
			}
		}
		// Otherwise, there is a problem of some sort. Give the user a link to
		// the VisIt preferences along with an informative message.
		else {

			// Set the message and icon based on the state of the connection.
			final String message;
			final Image image;
			if (state == ConnectionState.Connecting) {
				message = "The VisIt connection is being established...";
				image = display.getSystemImage(SWT.ICON_WORKING);
			} else if (state == ConnectionState.Disconnected) {
				if (connection == null) {
					message = "The VisIt connection is not configured.";
				} else {
					message = "The VisIt connection is currently disconnected.";
				}
				image = display.getSystemImage(SWT.ICON_WARNING);
			} else {
				message = "The VisIt connection failed!";
				image = display.getSystemImage(SWT.ICON_ERROR);
			}

			// If necessary, dispose the canvas and create the infoComposite.
			if (canvas != null && !canvas.isDisposed()) {
				canvas.dispose();
				canvas = null;
			}

			// If necessary, create the infoComposite.
			if (infoComposite == null) {
				infoComposite = createInfoComposite(plotComposite, SWT.NONE);
			}
			// Update the contents of the infoComposite's widgets.
			iconLabel.setImage(image);
			msgLabel.setText(message);
			// Show the link since the problem is a connection issue.
			link.setVisible(true);
			// Make the infoComposite appear in front.
			stackLayout.topControl = infoComposite;
			plotComposite.layout(true, true);
			// The above layout requires the two boolean flags to make sure the
			// text widgets are also laid out.
		}

		return;
	}

	private Composite createInfoComposite(Composite parent, int style) {

		final Display display = parent.getDisplay();
		final Shell shell = parent.getShell();

		Composite infoComposite = new Composite(parent, style);
		infoComposite.setLayout(new GridLayout(2, false));

		String linkText = "Click here to update VisIt connection preferences.";

		// Create an info label with an image.
		iconLabel = new Label(infoComposite, SWT.NONE);
		iconLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));

		// Create a Composite to contain the info message and the
		// hyperlink
		// with the info message above the hyperlink.
		Composite msgComposite = new Composite(infoComposite, SWT.NONE);
		msgComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));
		msgComposite.setLayout(new GridLayout(1, false));

		// Create an info label with informative text.
		msgLabel = new Label(msgComposite, SWT.NONE);
		msgLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false));

		// Create a link to the preference page.
		link = new Hyperlink(msgComposite, SWT.NONE);
		link.setText(linkText);
		link.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		link.setUnderlined(true);
		link.setForeground(display.getSystemColor(SWT.COLOR_LINK_FOREGROUND));
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
						"org.eclipse.ice.viz.service.visit.preferences", null,
						null).open();
			}
		});

		return infoComposite;
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
		if (adapter != null) {
			adapter.unregister(this);
			adapter = null;
		}

		// If possible, inform the UI to dispose the plot's UI contributions.
		if (plotComposite != null && !plotComposite.isDisposed()) {
			plotComposite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (canvas != null && !canvas.isDisposed()) {
						canvas.dispose();
						canvas = null;
					}
					if (infoComposite != null && !infoComposite.isDisposed()) {
						infoComposite.dispose();
						infoComposite = null;
						msgLabel = null;
						iconLabel = null;
						link = null;
					}
					plotComposite.dispose();
					plotComposite = null;
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

	@Override
	public void setEventListener(int eventType, Listener listener) {
		// TODO Auto-generated method stub
		
	}
}
