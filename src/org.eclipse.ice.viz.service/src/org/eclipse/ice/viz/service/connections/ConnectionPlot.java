package org.eclipse.ice.viz.service.connections;

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
	 * @param file
	 *            The data source, either a local or remote file.
	 */
	public ConnectionPlot(IVizService vizService, URI file) {
		super(vizService, file);

		// Nothing else to do yet.
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
			// Trigger an update to the UI for all currently rendered plots.
			for (PlotRender plotRender : getPlotRenders()) {
				plotRender.refresh();
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
