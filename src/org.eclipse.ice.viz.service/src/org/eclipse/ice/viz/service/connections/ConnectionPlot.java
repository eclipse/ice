package org.eclipse.ice.viz.service.connections;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * This class provides the basic implementation for an {@link IPlot} whose
 * content depends on a local or remote connection (a {@link ConnectionAdapter}
 * ).
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionPlot<T> extends ConnectionClient<T> implements
		IPlot {

	// TODO We should support drawing the same plot in multiple locations!
	// TODO Provide access to plot properties.

	/**
	 * The visualization service responsible for this plot.
	 */
	private final IVizService vizService;

	// ---- Source and Plot Properties ---- //
	/**
	 * The data source, either a local or remote file.
	 */
	private URI source;

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
	 * The current widget used to draw the plot. This should only be visible if
	 * the connection is open.
	 */
	private Composite canvas = null;
	/**
	 * This presents the user with helpful information about the status of the
	 * associated connection. It should only be visible if the connection is
	 * *not* open.
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
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 * @param file
	 *            The data source, either a local or remote file.
	 */
	public ConnectionPlot(IVizService vizService, URI file) {
		// Check the parameters.
		if (vizService == null) {
			throw new NullPointerException("IPlot error: "
					+ "Null viz service not allowed.");
		}

		this.vizService = vizService;

		// Set the data source now. This should build any required meta data.
		setDataSource(file);
	}

	/**
	 * This method informs the plot that its associated connection has been
	 * updated. The plot can then update its contents if it has contributed to
	 * the UI.
	 * 
	 * @param component
	 *            The component that was updated. This is expected to be the
	 *            associated {@link ConnectionAdapter}.
	 */
	@Override
	public void update(IUpdateable component) {
		// If the argument is null, then do nothing. Even if the current adapter
		// is null, the UI should already be up to date!
		if (component != null && component == getConnectionAdapter()) {
			// Trigger an update to the UI.
			updateUI();
		}
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
			throw new NullPointerException("IPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED, "IPlot error: "
					+ "Cannot draw plot inside disposed Composite.");
		} else if (currentParent != null && parent != currentParent) {
			throw new IllegalArgumentException("IPlot error: "
					+ "Cannot draw same plot in multiple Composites.");
		}

		// Check the plot category and type.
		String[] plotTypes = getPlotTypes().get(category);
		if (plotTypes == null) {
			throw new IllegalArgumentException("IPlot error: "
					+ "Invalid plot category \"" + category + "\".");
		} else {
			boolean found = false;
			for (int i = 0; !found && i < plotTypes.length; i++) {
				found = plotType.equals(plotTypes[i]);
			}
			if (!found) {
				throw new IllegalArgumentException("IPlot error: "
						+ "Invalid plot type \"" + plotType + "\".");
			}
		}

		// Update the plot category and type.
		this.plotCategory = category;
		this.plotType = plotType;

		// Create the plot Composite.
		plotComposite = new Composite(parent, SWT.NONE);
		plotComposite.setFont(parent.getFont());
		plotComposite.setBackground(parent.getBackground());
		plotComposite.setLayout(new StackLayout());

		// Trigger an update to the UI.
		updateUI();

		return;
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
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return "localhost".equals(getSourceHost());
	}

	/**
	 * Sets the data source (which is currently rendered if the plot is drawn).
	 * If the data source is valid and new, then the plot will be updated
	 * accordingly.
	 * 
	 * @param uri
	 *            The new data source URI.
	 */
	protected void setDataSource(URI file) {
		if (file != null) {
			source = file;
		}
	}

	/**
	 * This method updates the UI component of the plot based on the current
	 * state of the VizService. This method should be used any time a UI update
	 * needs to be triggered, e.g., when the data source changes or when the
	 * underlying connection is updated.
	 */
	private void updateUI() {

		// Get the current connection adapter.
		final IConnectionAdapter<T> adapter = getConnectionAdapter();

		// Determine the current connection and its state.
		final ConnectionState state;
		final T connection;
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
	private void updateUI(ConnectionState state, T connection) {

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
			}

			// Try to update the canvas. If an error occurs, show the info
			// Composite with a useful message.
			try {
				updateCanvas(canvas, connection);

				// Make the Canvas appear in front. This only needs to be done
				// if it's not already at the top.
				if (stackLayout.topControl != canvas) {
					stackLayout.topControl = canvas;
					plotComposite.layout();
				}
			} catch (Exception e) {
				// Set the message and icon.
				String message = e.getMessage();
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

	/**
	 * This creates the useful information {@code Composite} that appears when
	 * the plot could not be rendered either due to an invalid or disconnected
	 * connection adapter or an invalid file.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the info
	 *            {@code Composite}.
	 * @param style
	 *            The SWT style to use for the info {@code Composite}.
	 * @return The new info {@code Composite}.
	 */
	private Composite createInfoComposite(Composite parent, int style) {

		final Display display = parent.getDisplay();
		final Shell shell = parent.getShell();

		Composite infoComposite = new Composite(parent, style);
		infoComposite.setLayout(new GridLayout(2, false));

		String linkText = "Click here to update the " + vizService.getName()
				+ " connection preferences.";

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
						getPreferenceNodeID(), null, null).open();
			}
		});

		return infoComposite;
	}

	/**
	 * Gets the ID of the associated viz service's preferences node. This is
	 * used in the {@link #infoComposite}'s {@link #link} to the preferences.
	 * 
	 * @return The preference page ID.
	 */
	protected abstract String getPreferenceNodeID();

	/**
	 * Creates a new canvas that can render components via the associated
	 * connection.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the canvas.
	 * @param style
	 *            The SWT style to use for the canvas widget.
	 * @param connection
	 *            The connection used to populate the canvas graphics.
	 * @return The new canvas.
	 */
	protected abstract Composite createCanvas(Composite parent, int style,
			T connection);

	/**
	 * Updates what is currently displayed in the canvas if necessary.
	 * 
	 * @param connection
	 *            The connection used to render the plot.
	 * @throws Exception
	 *             If the canvas could not be updated, then an exception should
	 *             be thrown <i>with an informative message</i>.
	 */
	protected abstract void updateCanvas(Composite canvas, T connection)
			throws Exception;

	/**
	 * Gets the category of the currently drawn plot.
	 * 
	 * @return The category of the currently drawn plot.
	 */
	protected String getPlotCategory() {
		return plotCategory;
	}

	/**
	 * Gets the type of the currently drawn plot.
	 * 
	 * @return The type of the currently drawn plot.
	 */
	protected String getPlotType() {
		return plotType;
	}

	/**
	 * Gets the visualization service responsible for this plot.
	 * 
	 * @return The visualization service responsible for this plot.
	 */
	protected IVizService getVizService() {
		return vizService;
	}
}
