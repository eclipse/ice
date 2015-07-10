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
package org.eclipse.ice.viz.service.connections;

import org.eclipse.ice.viz.service.PlotRender;
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
 * This class provides a {@link PlotRender} specifically for rendering
 * visualizations from a {@link ConnectionPlot}.
 * <p>
 * In particular, such plots require access to an {@link IVizConnection} in
 * order to function properly.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of widget used for underlying viz connections.
 */
public abstract class ConnectionPlotRender<T> extends PlotRender implements IVizConnectionListener<T> {

	// TODO Make the image/icon in the PlotRender class customizable.

	/**
	 * The rendered {@code ConnectionPlot}. This cannot be changed.
	 */
	public final ConnectionPlot<T> plot;

	/**
	 * The current connection associated with this plot.
	 */
	private IVizConnection<T> connection;

	// ---- UI Widgets ---- //
	/**
	 * A link to the visualization connection preferences.
	 */
	private Hyperlink link;

	/**
	 * Whether or not to show the {@link #link}. This should only be set to true
	 * when there is a connection problem when creating or updating the plot
	 * {@code Composite}.
	 */
	private boolean showLink = false;

	// -------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that contains the plot render.
	 * @param plot
	 *            The rendered {@code ConnectionPlot}. This cannot be changed.
	 */
	public ConnectionPlotRender(Composite parent, ConnectionPlot<T> plot) {
		super(parent, plot);

		// Keep a reference to the plot as a ConnectionPlot with the right type.
		this.plot = plot;
	}

	/**
	 * Sets the viz connection used to render visualizations inside the content
	 * composite.
	 * 
	 * @param connection
	 *            The new connection.
	 */
	public void setConnection(IVizConnection<T> connection) {
		if (connection != this.connection) {
			// Unregister from the old connection.
			if (this.connection != null) {
				this.connection.removeListener(this);
			}

			// Set the new connection.
			this.connection = connection;

			// Trigger a refresh.
			refresh();

			// Register with the new connection if possible.
			if (connection != null) {
				connection.addListener(this);
			}
		}
		return;
	}

	/**
	 * Adds a {@link #link} to the visualization service's connection
	 * preferences after the message label.
	 */
	@Override
	protected Composite createInfoComposite(Composite parent, int style) {

		// Get the info Composite and its child with the message label.
		final Composite infoComposite = super.createInfoComposite(parent, style);
		final Composite msgComposite = (Composite) infoComposite.getChildren()[1];

		// Get a Display and Shell used to create the hyperlink.
		final Display display = infoComposite.getDisplay();
		final Shell shell = infoComposite.getShell();

		// Set the text to display in the hyperlink.
		final String linkText = "Click here to update the " + plot.getVizService().getName()
				+ " connection preferences.";

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
				// Open up the viz service connection preferences.
				PreferencesUtil.createPreferenceDialogOn(shell, getPreferenceNodeID(), null, null).open();
			}
		});

		return infoComposite;
	}

	/**
	 * Updates the visibility of the {@link #link} in addition to the default
	 * update behavior.
	 */
	@Override
	protected void updateInfoComposite(Composite infoComposite, String message) {

		// Set the link's visibility.
		link.setVisible(showLink);

		super.updateInfoComposite(infoComposite, message);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.PlotRender#disposeInfoComposite(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected void disposeInfoComposite(Composite infoComposite) {
		super.disposeInfoComposite(infoComposite);

		// We need to unset the link.
		link = null;
	}

	/**
	 * Gets the ID of the associated viz service's preferences node. This is
	 * used in the info {@code Composite}'s {@link #link} to the preferences.
	 * 
	 * @return The preference page ID.
	 */
	protected abstract String getPreferenceNodeID();

	/**
	 * Checks the current connection's status before re-directing to
	 * {@link #createPlotComposite(Composite, int, Object)}.
	 * <p>
	 * <b>Note:</b> Sub-classes should <i>not</i> override this method.
	 * </p>
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style) throws Exception {

		// The default return value.
		Composite plotComposite = null;

		// Validate the current state of the connection. This throws an
		// exception if there's a connection problem.
		validateConnection(connection);

		// Try to render the plot. This may also throw an exception depending on
		// the sub-class' implementation.
		plotComposite = createPlotComposite(parent, style, connection.getWidget());

		return plotComposite;
	}

	/**
	 * Validates the connection, returning if the connection is valid or
	 * throwing an exception otherwise.
	 * 
	 * @param adapter
	 *            The {@link #plot}'s connection adapter.
	 * @throws Exception
	 *             An exception with an informative message is thrown if there
	 *             is a problem with the connection.
	 */
	protected void validateConnection(IVizConnection<T> adapter) throws Exception {
		// Get the connection and its state from the connection adapter.
		final T connection = adapter.getWidget();
		final ConnectionState state = adapter.getState();

		// Set the message and icon based on the state of the connection.
		final String message;
		final Image image;
		final String serviceName = plot.getVizService().getName();

		// Get the display from the parent Composite. This is required to set
		// the error icon appropriately.
		final Display display = parent.getDisplay();

		// If the connection is valid, we should immediately break and return.
		// This is expected to be the most common situation.
		if (connection != null && state == ConnectionState.Connected) {
			// There does not appear to be an issue related to the connection
			// preferences, so hide the link.
			showLink = false;
			return;
		} else if (state == ConnectionState.Connecting) {
			message = "The " + serviceName + " connection is being established...";
			image = display.getSystemImage(SWT.ICON_WORKING);
		} else if (state == ConnectionState.Disconnected) {
			if (connection == null) {
				message = "The " + serviceName + " connection is not configured.";
			} else {
				message = "The " + serviceName + " connection is currently disconnected.";
			}
			image = display.getSystemImage(SWT.ICON_WARNING);
		} else if (state == ConnectionState.Failed) {
			message = "The " + serviceName + " connection failed!";
			image = display.getSystemImage(SWT.ICON_ERROR);
		} else { // (connection == null)
			message = "The " + serviceName + " connection is not available!";
			image = display.getSystemImage(SWT.ICON_ERROR);
		}

		// Set the image and then throw an exception.
		showLink = true;
		infoIcon = image;
		throw new Exception(message);
	}

	/**
	 * Creates the plot {@code Composite} that is shown when the associated
	 * {@link #plot}, {@link #category}, and {@link #type} are all valid.
	 * 
	 * @param parent
	 *            The parent in which the plot {@code Composite} should be
	 *            created.
	 * @param style
	 *            The style to use for the plot {@code Composite}.
	 * @param connection
	 *            The current connection used to render the plot.
	 * @return The new plot {@code Composite}.
	 * @throws Exception
	 *             If the plot is in an invalid state or otherwise cannot be
	 *             rendered, this throws an exception with an informative
	 *             message.
	 */
	protected abstract Composite createPlotComposite(Composite parent, int style, T connection) throws Exception;

	/**
	 * Checks the current connection's status before re-directing to
	 * {@link #updatePlotComposite(Composite, Object)}.
	 * <p>
	 * <b>Note:</b> Sub-classes should <i>not</i> override this method.
	 * </p>
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite) throws Exception {

		// Validate the current state of the connection. This throws an
		// exception if there's a connection problem.
		validateConnection(connection);

		// Try to update the plot. This may also throw an exception depending on
		// the sub-class' implementation.
		updatePlotComposite(plotComposite, connection.getWidget());

		return;
	}

	/**
	 * Updates the plot rendering contained in the specified plot
	 * {@code Composite}.
	 * 
	 * @param plotComposite
	 *            The plot {@code Composite} to update.
	 * @param connection
	 *            The current connection used to render the plot.
	 * @throws Exception
	 *             If the plot is in an invalid state or otherwise cannot be
	 *             rendered, this throws an exception with an informative
	 *             message.
	 */
	protected abstract void updatePlotComposite(Composite plotComposite, T connection) throws Exception;

	/*
	 * Implements a method from IVizConnectionListener.
	 */
	@Override
	public void connectionStateChanged(IVizConnection<T> connection, ConnectionState state, String message) {
		// Trigger a refresh.
		refresh();

		// TODO We can do more with this.
	}

}
