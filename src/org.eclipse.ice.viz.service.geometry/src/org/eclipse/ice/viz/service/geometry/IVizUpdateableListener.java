package org.eclipse.ice.viz.service.geometry;

import java.util.ArrayList;


public interface IVizUpdateableListener {
	/**
	 * <p>
	 * The Component that was updated.
	 * </p>
	 * 
	 */
	ArrayList<IVizUpdateable> component = null;

	/**
	 * <p>
	 * This operation notifies the listener that an update has occurred in the
	 * Component.
	 * </p>
	 * 
	 * @param component The component that was updated in some way.
	 */
	public void update(IVizUpdateable component);
}
