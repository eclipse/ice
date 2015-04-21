package org.eclipse.ice.viz.service.connections;

import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;

/**
 * A connection client is a class that can be associated with a single
 * {@link IConnectionAdapter}. It registers with the adapter as an
 * {@link IUpdateableListener} and is notified when the connection changes via
 * its {@link #update(IUpdateable)} method.
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The connection object's type.
 */
public interface IConnectionClient<T> extends IUpdateableListener {

	/**
	 * Sets the current connection associated with the client.
	 * <p>
	 * <b>Note:</b> Implementations should at least unregister from the
	 * previously associated connection and register with the new one. It may
	 * also trigger an update to the client.
	 * </p>
	 * 
	 * @param adapter
	 *            The new connection adapter. If {@code null}, the connection
	 *            will be unset and the plot will be cleared.
	 */
	void setConnectionAdapter(IConnectionAdapter<T> adapter);

}
