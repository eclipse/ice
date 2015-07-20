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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.MultiPlot;
import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides the basic implementation for an {@link IPlot} whose
 * content depends on a local or remote connection (an {@link IVizConnection} ).
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionPlot<T> extends MultiPlot {

	/**
	 * The current connection associated with this plot.
	 */
	private IVizConnection<T> connection;

	/**
	 * A list of the plot renders cast as {@link ConnectionPlotRender}s. Their
	 * connection should be synchronized with this plot's current connection.
	 */
	private final List<ConnectionPlotRender<T>> plotRenders;

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public ConnectionPlot(IVizService vizService) {
		super(vizService);

		// Nothing else to do yet.
		plotRenders = new ArrayList<ConnectionPlotRender<T>>();
	}

	/*
	 * Implements an abstract method from MultiPlot#createPlotRender.
	 */
	protected PlotRender createPlotRender(Composite parent) {
		// Create a ConnectionPlotRender.
		ConnectionPlotRender<T> plotRender = createConnectionPlotRender(parent);
		plotRenders.add(plotRender);
		// Set its connection.
		plotRender.setConnection(connection);
		return plotRender;
	}

	/**
	 * Creates a {@link ConnectionPlotRender} inside the specified parent
	 * Composite. The PlotRender's content should not be created yet.
	 * 
	 * @param parent
	 *            The parent Composite that will contain the new PlotRender.
	 * @return The new PlotRender.
	 */
	protected abstract ConnectionPlotRender<T> createConnectionPlotRender(Composite parent);

	/**
	 * Gets the current connection associated with this plot.
	 * 
	 * @return The {@link #connection}. This may be {@code null}.
	 */
	protected IVizConnection<T> getConnection() {
		return connection;
	}

	/**
	 * Gets a list of all current rendered plots.
	 * 
	 * @return A list containing each current {@link ConnectionPlotRender} in
	 *         this {@code ConnectionPlot}.
	 */
	protected List<ConnectionPlotRender<T>> getConnectionPlotRenders() {
		return new ArrayList<ConnectionPlotRender<T>>(plotRenders);
	}

	/**
	 * Sets the viz connection used by this plot.
	 * 
	 * @param connection
	 *            The new connection to use for this plot.
	 */
	public void setConnection(IVizConnection<T> connection) {
		if (connection != this.connection) {
			this.connection = connection;

			// Set the connection for all of the plot renders.
			for (ConnectionPlotRender<T> plotRender : plotRenders) {
				plotRender.setConnection(connection);
			}
		}
		return;
	}

	/**
	 * Sets the data source (which is currently rendered if the plot is drawn).
	 * If the data source is valid and new, then the plot will be updated
	 * accordingly.
	 * <p>
	 * <b>Note:</b> {@link ConnectionPlot} additionally performs basic checks on
	 * the files. For instance, it will throw an exception if the file does not
	 * exist or if there are read permission issues.
	 * </p>
	 * 
	 * @param uri
	 *            The new data source URI.
	 * @throws NullPointerException
	 *             if the specified file is null
	 * @throws IOException
	 *             if there was an error while reading the file's contents
	 * @throws IllegalArgumentException
	 *             if there are no plots available
	 * @throws Exception
	 *             if there is some other unspecified problem with the file
	 */
	@Override
	public void setDataSource(URI uri) throws NullPointerException, IOException, IllegalArgumentException, Exception {

		// Check that the file's host matches the connection host. Also check
		// that the file exists. We check that the file is not null so that the
		// super method can throw the NPE for null files.
		if (uri != null) {
			// Check if the connection exists. If not, we need to throw an
			// exception.
			if (connection == null) {
				throw new NullPointerException("IPlot error: " + "The plot's connection is not set.");
			}

			// Set up a message in case the file cannot be read by this plot.
			final String message;

			// Get the hosts from the connection and the URI.
			String connHost = connection.getHost();
			String fileHost = uri.getHost();
			if (fileHost == null) {
				fileHost = "localhost";
			}

			// If they do not match, throw an exception.
			if (!fileHost.equals(connHost)) {
				message = "The file host \"" + fileHost + "\" does not match the connection's host \"" + connHost
						+ "\".";
			}
			// If they do match and the file is local, check that the file
			// exists and can be read.
			else if ("localhost".equals(connHost)) {
				File fileRef = new File(uri.getPath());
				if (!fileRef.isFile()) {
					message = "The path \"" + uri + "\" does not exist or is not a file.";
				} else if (!fileRef.canRead()) {
					message = "The file \"" + uri + "\" cannot be read.";
				}
				// Otherwise, there is no problem.
				else {
					message = null;
				}
			}
			// Do the same for remote files.
			else {
				// TODO We need to find a way to check remote files...
				message = null;
			}

			// Throw an exception if necessary.
			if (message != null) {
				throw new IllegalArgumentException("IPlot error: " + message);
			}
		}

		// Proceed with the super class' methods for error checking and setting
		// the data source.
		super.setDataSource(uri);
	}

}
