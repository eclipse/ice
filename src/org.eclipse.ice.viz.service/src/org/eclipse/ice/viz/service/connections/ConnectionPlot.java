package org.eclipse.ice.viz.service.connections;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.viz.service.MultiPlot;
import org.eclipse.ice.viz.service.PlotRender;

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
public abstract class ConnectionPlot<T> extends MultiPlot implements
		IConnectionClient<T> {

	/**
	 * The current connection adapter associated with this client.
	 */
	private IConnectionAdapter<T> adapter;

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public ConnectionPlot(IVizService vizService) {
		super(vizService);

		// Nothing else to do yet.
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
	 * @param file
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
	public void setDataSource(URI file) throws NullPointerException,
			IOException, IllegalArgumentException, Exception {

		// Check that the file's host matches the connection host. Also check
		// that the file exists. We check that the file is not null so that the
		// super method can throw the NPE for null files.
		if (file != null) {
			// Set up a message in case the file cannot be read by this plot.
			final String message;

			// TODO We may need to reintroduce this code if we start putting
			// connection info in the URI.
			// // Check for a mismatched host.
			// String fileHost = file.getHost();
			// String connHost = adapter.getHost();
			// if (fileHost != null && !fileHost.equals(connHost)) {
			// message = "The file host \"" + fileHost
			// + "\" does not match the connection's host \""
			// + connHost + "\".";
			// }
			// // This is necessary in case the file's host is null (localhost).
			// else if (fileHost == null && adapter.isRemote()) {
			// message = "The file host is localhost, while the "
			// + "connection host is \"" + connHost + "\".";
			// }
			// else

			// Check that the local file exists and can be read.
			if (!adapter.isRemote()) {
				File fileRef = new File(file);
				if (!fileRef.isFile()) {
					message = "The path \"" + file
							+ "\" does not exist or is not a file.";
				} else if (!fileRef.canRead()) {
					message = "The file \"" + file + "\" cannot be read.";
				}
				// Otherwise, there is no problem.
				else {
					message = null;
				}
			}
			// Check that the remote file exists and can be read.
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
		super.setDataSource(file);
	}

	// ---- Implements IConnectionClient (and IUpdateableListener) ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.IConnectionClient#setConnection
	 * (org.eclipse.ice.viz.service.connections.IConnectionAdapter)
	 */
	public void setConnectionAdapter(IConnectionAdapter<T> adapter) {
		if (adapter != this.adapter) {
			if (this.adapter != null) {
				this.adapter.unregister(this);
			}
			this.adapter = adapter;

			// Trigger an update.
			update(adapter);

			// Register for updates from the adapter if possible.
			if (adapter != null) {
				adapter.register(this);
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
	 *            associated {@link ConnectionAdapter}.
	 */
	@Override
	public void update(IUpdateable component) {
		// If the argument is null, then do nothing. Even if the current adapter
		// is null, the UI should already be up to date!
		if (component != null && component == adapter) {
			// Clear the cache for this plot, since the new connection may
			// affect the available plot types.
			clearCache();
			// Trigger an update to the UI for all currently rendered plots.
			for (PlotRender plotRender : getPlotRenders()) {
				plotRender.refresh(true);
			}
		}
	}

	// ---------------------------------------------------------------- //

	/**
	 * Gets the adapter for the current connection associated with this plot.
	 * 
	 * @return The {@link #adapter}. This may be null.
	 */
	protected IConnectionAdapter<T> getConnectionAdapter() {
		return adapter;
	}
}
