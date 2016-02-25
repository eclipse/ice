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
package org.eclipse.eavp.viz.service.connections;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.widgets.PlotComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * This class provides a {@link PlotComposite} specifically for rendering
 * visualizations over an {@link IVizConnection}.
 * 
 * @author Jordan
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionPlotComposite<T> extends PlotComposite
		implements IVizConnectionListener<T> {

	/**
	 * The current connection associated with this plot.
	 */
	private IVizConnection<T> connection;

	/**
	 * A link to the visualization connection preferences.
	 */
	private Hyperlink link;

	/**
	 * A reference to the associated connection plot.
	 */
	private ConnectionPlot<T> plot;

	/**
	 * Whether or not to show the {@link #link}. This should only be set to true
	 * when there is a connection problem when creating or updating the plot
	 * {@code Composite}.
	 */
	private boolean showLink = false;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public ConnectionPlotComposite(Composite parent, int style) {
		super(parent, style);
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionListener#connectionStateChanged(org.eclipse.eavp.viz.service.connections.IVizConnection, org.eclipse.eavp.viz.service.connections.ConnectionState, java.lang.String)
	 */
	@Override
	public void connectionStateChanged(IVizConnection<T> connection,
			ConnectionState state, String message) {
		// Trigger a refresh.
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#createInfoContent(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected Composite createInfoContent(Composite parent, int style) {
		// Get the info Composite and its child with the message label.
		final Composite infoComposite = super.createInfoContent(parent, style);
		final Composite msgComposite = (Composite) infoComposite
				.getChildren()[1];

		// Get a Display and Shell used to create the hyperlink.
		final Display display = infoComposite.getDisplay();
		final Shell shell = infoComposite.getShell();

		// Set the text to display in the hyperlink.
		final String linkText = "Click here to update the connection "
				+ "preferences.";

		// Create a link to the preference page.
		link = new Hyperlink(msgComposite, SWT.NONE);
		link.setText(linkText);
		link.setLayoutData(
				new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		link.setUnderlined(true);
		link.setForeground(display.getSystemColor(SWT.COLOR_LINK_FOREGROUND));
		// Add the listener to redirect the user to the preferences.
		link.addHyperlinkListener(new IHyperlinkListener() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// Open up the viz service connection preferences.
				PreferencesUtil
						.createPreferenceDialogOn(shell,
								getConnectionPreferencePageID(), null, null)
						.open();
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {
				// Nothing to do yet.
			}

			@Override
			public void linkExited(HyperlinkEvent e) {
				// Nothing to do yet.
			}
		});

		return infoComposite;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#createPlotContent(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected final Composite createPlotContent(Composite parent, int style)
			throws Exception {
		// Redirect to the more specific method with the current viz connection.
		return createPlotContent(parent, style, connection);
	}

	/**
	 * This method creates customized plot content.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to and <i>should</i> be
	 * overridden.
	 * </p>
	 * 
	 * @param parent
	 *            The parent of the outermost content {@code Composite}.
	 * @param style
	 *            The style for the outermost content {@code Composite}.
	 * @param connection
	 *            The associated viz connection.
	 * @return The <i>outermost</i> plot content {@code Composite}.
	 * @throws Exception
	 *             If there was an error preventing plot content from being
	 *             created.
	 */
	protected Composite createPlotContent(Composite parent, int style,
			IVizConnection<T> connection) throws Exception {
		// Validate the current state of the connection. This throws an
		// exception if there's a connection problem.
		validateConnection(connection);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#disposeInfoContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void disposeInfoContent(Composite infoContent) {
		super.disposeInfoContent(infoContent);

		// We need to unset the link.
		link = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#disposePlotContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected final void disposePlotContent(Composite plotContent) {
		// Redirect to the more specific method with the current viz connection.
		disposePlotContent(plotContent, connection);
	}

	/**
	 * This method disposes customized plot content. This is triggered every
	 * time the content is successfully created in
	 * {@link #createPlotContent(Composite, int)} but failed to update in
	 * {@link #updatePlotContent(Composite)}. Sub-classes should override this
	 * method to handle any additional disposals, but they <b><i>must call the
	 * super method last</i></b>.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @param plotContent
	 *            The main plot composite that is being disposed.
	 * @param connection
	 *            The associated viz connection.
	 */
	protected void disposePlotContent(Composite plotContent,
			IVizConnection<T> connection) {
		// Nothing to do by default.
	}

	/**
	 * Gets the current connection associated with this plot.
	 *
	 * @return The {@link #connection}. This may be {@code null} if it has not
	 *         been set.
	 */
	protected IVizConnection<T> getConnection() {
		return connection;
	}

	/**
	 * Gets the current plot associated with this composite.
	 * 
	 * @return The current plot, or {@code null} if it is not set.
	 */
	protected ConnectionPlot<T> getConnectionPlot() {
		return plot;
	}

	/**
	 * Gets the ID of the associated viz service's preferences page for
	 * connections. This is used in the info {@code Composite}'s {@link #link}
	 * to the preferences.
	 * 
	 * @return The connection preference page ID.
	 */
	protected abstract String getConnectionPreferencePageID();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#hideSeries(org.eclipse.eavp.viz.service.ISeries)
	 */
	@Override
	protected final void hideSeries(ISeries series) throws Exception {
		// Redirect to the more specific method with the current viz connection.
		hideSeries(series, connection);
	}

	/**
	 * Hides the specified series. Sub-classes should override this method to
	 * handle actually removing the series, but they <b><i>must call the super
	 * method last after any exceptions are thrown</i></b>.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @param series
	 *            The series to hide.
	 * @param connection
	 *            The associated viz connection.
	 * @throws Exception
	 *             If the series could not be hidden.
	 */
	protected void hideSeries(ISeries series, IVizConnection<T> connection)
			throws Exception {
		// Validate the current state of the connection. This throws an
		// exception if there's a connection problem.
		validateConnection(connection);
		super.hideSeries(series);
	}

	/**
	 * Sets the connection for this plot.
	 * 
	 * @param connection
	 *            The new connection.
	 * @return True if the connection changed, false otherwise.
	 * @throws Exception
	 *             If the connection cannot be set.
	 */
	protected boolean setConnection(IVizConnection<T> connection)
			throws Exception {
		boolean changed = false;
		if (connection != this.connection) {

			// Unregister from the old connection.
			if (this.connection != null) {
				connection.removeListener(this);
			}

			// Register with the new connection.
			this.connection = connection;
			changed = true;

			// Trigger a refresh.
			refresh();

			// Register as a listener if possible.
			if (connection != null) {
				connection.addListener(this);
			}
		}
		return changed;
	}

	/**
	 * Sets the current plot associated with this plot composite.
	 * <p>
	 * <b>Note:</b> This method will <i>not</i> refresh the composite
	 * automatically. If the plot changes, the calling code should call
	 * {@link #refresh()} afterward.
	 * </p>
	 * 
	 * @param plot
	 *            The new plot shown in this composite, or {@code null} to clear
	 *            it.
	 * @return True if the plot changed, false otherwise.
	 */
	public boolean setConnectionPlot(ConnectionPlot<T> plot) {
		boolean changed = false;
		if (super.setPlot(plot)) {
			this.plot = plot;
			changed = true;
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#setPlot(org.eclipse.eavp.viz.service.IPlot)
	 */
	@Override
	public boolean setPlot(IPlot plot) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#showSeries(org.eclipse.eavp.viz.service.ISeries)
	 */
	@Override
	protected final void showSeries(ISeries series) throws Exception {
		// Redirect to the more specific method with the current viz connection.
		showSeries(series, connection);
	}

	/**
	 * Shows the specified series. Sub-classes should override this method to
	 * handle actually rendering the series, but they <b><i>must call the super
	 * method last after any exceptions are thrown</i></b>.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @param series
	 *            The series to add.
	 * @param connection
	 *            The associated viz connection.
	 * @throws Exception
	 *             If the series could not be added.
	 */
	protected void showSeries(ISeries series, IVizConnection<T> connection)
			throws Exception {
		// Validate the current state of the connection. This throws an
		// exception if there's a connection problem.
		validateConnection(connection);
		super.showSeries(series);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#updateInfoContent(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
	@Override
	protected void updateInfoContent(Composite infoContent, String message) {

		// Set the link's visibility.
		link.setVisible(showLink);

		super.updateInfoContent(infoContent, message);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#updatePlotContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected final void updatePlotContent(Composite plotContent)
			throws Exception {
		// Redirect to the more specific method with the current viz connection.
		updatePlotContent(plotContent, connection);
	}

	/**
	 * This method updates the customized plot content. The default behavior
	 * finds all series whose enabled/disabled state has been changed since the
	 * last time this method was called and calls either
	 * {@link #showSeries(ISeries)} or {@link #hideSeries(ISeries)} to apply the
	 * change. Alternatively, series can be individually updated by setting
	 * their enabled flags and calling show/hide series.
	 * <p>
	 * Sub-classes should override this method <i>if additional code is required
	 * to refresh the custom widgets</i>.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @param plotContent
	 *            The composite previously created for the customized plot
	 *            content.
	 * @param connection
	 *            The associated viz connection.
	 * @throws Exception
	 *             If there is an error preventing the customized plot content
	 *             from being updated.
	 */
	protected void updatePlotContent(Composite plotContent,
			IVizConnection<T> connection) throws Exception {
		// Validate the current state of the connection. This throws an
		// exception if there's a connection problem.
		validateConnection(connection);

		// Perform the default update process without the connection.
		super.updatePlotContent(plotContent);
	}

	/**
	 * Validates the connection, returning if the connection is valid or
	 * throwing an exception otherwise.
	 * 
	 * @param connection
	 *            The {@link #plot}'s connection.
	 * @throws Exception
	 *             An exception with an informative message is thrown if there
	 *             is a problem with the connection.
	 */
	protected void validateConnection(IVizConnection<T> connection)
			throws Exception {
		// Get the widget and its state from the connection.
		final T widget = connection.getWidget();
		final ConnectionState state = connection.getState();

		// Set the message and icon based on the state of the connection.
		final String message;
		final Image image;
		final String name = connection.getName();

		// Get the display from the parent Composite. This is required to set
		// the error icon appropriately.
		final Display display = getDisplay();

		// If the connection is valid, we should immediately break and return.
		// This is expected to be the most common situation.
		if (widget != null && state == ConnectionState.Connected) {
			// There does not appear to be an issue related to the connection
			// preferences, so hide the link.
			showLink = false;
			return;
		} else if (state == ConnectionState.Connecting) {
			message = "The connection \"" + name + "\" is being established...";
			image = display.getSystemImage(SWT.ICON_WORKING);
		} else if (state == ConnectionState.Disconnected) {
			if (widget == null) {
				message = "The connection \"" + name + "\" is not configured.";
			} else {
				message = "The connection \"" + name
						+ "\" is currently disconnected.";
			}
			image = display.getSystemImage(SWT.ICON_WARNING);
		} else if (state == ConnectionState.Failed) {
			message = "The connection \"" + name + "\" failed!";
			image = display.getSystemImage(SWT.ICON_ERROR);
		} else { // (connection == null)
			message = "The connection \"" + name + "\" is not available!";
			image = display.getSystemImage(SWT.ICON_ERROR);
		}

		// Set the image and then throw an exception.
		showLink = true;
		infoIcon = image;
		throw new Exception(message);
	}
}
