package org.eclipse.ice.viz.service.connections;

/**
 * This class provides a basic implementation of an {@link IConnectionClient}.
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The connection's object type.
 */
public abstract class ConnectionClient<T> implements IConnectionClient<T> {

	/**
	 * The current connection adapter associated with this client.
	 */
	private IConnectionAdapter<T> adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.IConnectionClient#setConnection
	 * (org.eclipse.ice.viz.service.connections.IConnectionAdapter)
	 */
	public void setConnection(IConnectionAdapter<T> adapter) {
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
	 * Gets the current connection adapter associated with this client.
	 * 
	 * @return The connection adapter.
	 */
	protected IConnectionAdapter<T> getConnectionAdapter() {
		return adapter;
	}
}
