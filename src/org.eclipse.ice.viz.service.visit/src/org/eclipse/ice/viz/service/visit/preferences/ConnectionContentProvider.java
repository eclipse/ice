package org.eclipse.ice.viz.service.visit.preferences;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class provides a basic content provider for {@link ConnectionManager}s.
 * Its input is expected to be a single {@code ContentManager}, and the elements
 * provided are its list of {@link Connection}s.
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionContentProvider implements IStructuredContentProvider {

	/**
	 * The current manager for cached {@link Connection}s.
	 */
	private ConnectionManager manager;

	/**
	 * The content provider's target {@code Viewer}.
	 */
	private Viewer viewer;

	/**
	 * The default constructor.
	 */
	public ConnectionContentProvider() {
		// Nothing to do yet.
	}

	// ---- Implements IStructuredContentProvider ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		manager.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer != null && newInput != null) {

			// Update the references to the Viewer and IPreferenceStore.
			this.viewer = viewer;
			if (newInput instanceof ConnectionManager) {
				manager = (ConnectionManager) newInput;
			}

		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {

		// The top level should return the list of Connections.
		// No elements should be returned for anything else.

		Object[] elements = null;
		if (inputElement == manager) {
			List<Connection> connections = manager.getConnections();
			elements = connections.toArray(new Object[connections.size()]);
		} else {
			elements = new Object[] {};
		}

		return elements;
	}
	// ----------------------------------------------- //

}
