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

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import org.eclipse.eavp.viz.service.AbstractPlot;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a basic implementation for an {@link IPlot} whose content
 * depends on a local or remote connection (an {@link IVizConnection} ).
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionPlot<T> extends AbstractPlot
		implements IVizConnectionListener<T> {

	// TODO Rename this class to ConnectionPlot.

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectionPlot.class);

	/**
	 * The current connection associated with this plot.
	 */
	private IVizConnection<T> connection;

	/**
	 * The composite in which this plot is rendered.
	 */
	private ConnectionPlotComposite<T> plotComposite;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionListener#connectionStateChanged(org.eclipse.eavp.viz.service.connections.IVizConnection, org.eclipse.eavp.viz.service.connections.ConnectionState, java.lang.String)
	 */
	@Override
	public void connectionStateChanged(IVizConnection<T> connection,
			ConnectionState state, String message) {
		// Nothing to do.
	}

	/**
	 * Creates a composite that renders plot content using an
	 * {@link IVizConnection}.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @return A new, basic connection plot composite.
	 */
	protected abstract ConnectionPlotComposite<T> createPlotComposite(
			Composite parent);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#draw(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {

		// If necessary, create a new plot composite.
		if (plotComposite == null) {
			// Check the parent Composite.
			if (parent == null) {
				throw new SWTException(SWT.ERROR_NULL_ARGUMENT, "IPlot error: "
						+ "Cannot draw plot in a null Composite.");
			} else if (parent.isDisposed()) {
				throw new SWTException(SWT.ERROR_WIDGET_DISPOSED,
						"IPlot error: "
								+ "Cannot draw plot in a disposed Composite.");
			}

			// Create a plot composite.
			plotComposite = createPlotComposite(parent);
			plotComposite.setConnectionPlot(this);
			plotComposite.setConnection(connection);
			plotComposite.refresh();
			// Its reference should be unset when disposed.
			plotComposite.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					plotComposite = null;
				}
			});
		}
		// Otherwise, ignore the parent argument and trigger a normal refresh.
		else {
			plotComposite.refresh();
		}

		return plotComposite;
	}

	/**
	 * Tries to convert the specified hostname (which may already be an IP) into
	 * an IP address. If the IP cannot be found, it just returns the input
	 * string.
	 * 
	 * @param host
	 *            The hostname string.
	 * @return Either the host's IP as a string or the input string if its IP
	 *         address could not be found.
	 */
	protected final String findIPAddress(String host) {
		String ipAddress;
		try {
			InetAddress address = InetAddress.getByName(host);
			ipAddress = address.getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress = host;
		}
		return ipAddress;
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
	 * Gets the currently drawn plot composite.
	 * 
	 * @return The current plot composite, or {@code null} if it has not been
	 *         drawn.
	 */
	protected ConnectionPlotComposite<T> getPlotComposite() {
		return plotComposite;
	}

	/**
	 * Sets the connection for this plot.
	 * 
	 * @param connection
	 *            The new connection.
	 * @return True if the connection changed, false otherwise.
	 * @throws Exception
	 *             If the data source URI is set and the connection host does
	 *             not match the URI's host.
	 */
	public boolean setConnection(IVizConnection<T> connection)
			throws Exception {
		boolean changed = false;
		if (connection != this.connection) {
			// Check the connection host against the current URI.
			validateURI(getDataSource(), connection);

			// Unregister from the old connection.
			if (this.connection != null) {
				this.connection.removeListener(this);
			}

			// Register with the new connection.
			if (plotComposite != null) {
				plotComposite.setConnection(connection);
			}
			this.connection = connection;
			this.connection.addListener(this);

			changed = true;
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#setDataSource(java.net.URI)
	 */
	@Override
	public boolean setDataSource(URI uri) throws Exception {
		// Validate the connection. Note that we must allow null values for the
		// URI and connection in order to unset them.
		validateURI(uri, connection);
		return super.setDataSource(uri);
	}

	/**
	 * Validates the URI against the viz connection. This method performs a
	 * simple check against the host names for the connection and the file, if
	 * available.
	 * 
	 * @param uri
	 *            The data source URI being validated.
	 * @param connection
	 *            The viz connection being validated.
	 * @throws Exception
	 *             If both the URI and the connection are not null and
	 *             correspond to different host IP addresses.
	 */
	protected void validateURI(URI uri, IVizConnection<T> connection)
			throws Exception {
		if (uri != null && connection != null) {
			// Try to convert the host into an IP address. If not possible, just
			// leave it as is.
			String fileHost = findIPAddress(uri.getHost());
			String connHost = findIPAddress(connection.getHost());

			// Print the file and connection hosts to debug output.
			logger.debug(getClass().getName() + " message: " + "File \"" + uri
					+ "\" maps to host \"" + fileHost + ".");
			logger.debug(getClass().getName() + " message: "
					+ "Viz connection to \"" + connection.getHost()
					+ "\" maps to host \"" + connHost + ".");

			// Check that the hosts are the same.
			if (!fileHost.equals(connHost)) {
				throw new Exception(getClass().getName() + " Exception! "
						+ "The plot file and connections are on different hosts.");
			}
		}
		return;
	}



	// -------- Methods for VisIt Plot --------//

	/**
	 * Leave for later implementation, by visItPlot, etc
	 * 
	 * @param file
	 *            The file that will be searched for plot types
	 * @return A map of plot types specified by the category as the key
	 */
	protected void addAllPlotTypes(URI file) throws IOException, Exception {
		// Nothing TODO yet..we do not know what type of connection plot this
		// is. Let super classes define this behavior
		return;
	}

}
