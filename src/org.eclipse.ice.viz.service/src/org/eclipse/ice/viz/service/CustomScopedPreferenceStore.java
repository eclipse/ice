package org.eclipse.ice.viz.service;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class provides a customized {@link ScopedPreferenceStore} that gives
 * client code the ability to access {@link IEclipsePreferences} nodes stored
 * underneath the store's primary preference node.
 * 
 * @author Jordan Deyton
 *
 */
public class CustomScopedPreferenceStore extends ScopedPreferenceStore {

	/**
	 * The scoped context to which preferences are stored, usually
	 * {@link InstanceScope#INSTANCE}.
	 */
	private final IScopeContext context;
	/**
	 * The associated preference node's qualifier or ID, usually the bundle's
	 * ID.
	 */
	private final String qualifier;

	/**
	 * The default constructor.
	 * 
	 * @param context
	 *            The scope to store to, e.g., {@link InstanceScope#INSTANCE}.
	 * @param qualifier
	 *            The qualifer used to look up the preference node, e.g., the
	 *            bundle's ID.
	 */
	public CustomScopedPreferenceStore(IScopeContext context, String qualifier) {
		super(context, qualifier);

		this.context = context;
		this.qualifier = qualifier;
	}

	/**
	 * Gets the associated preference node from the {@link #context}.
	 * 
	 * @return The primary preference node.
	 */
	private IEclipsePreferences getPreferenceNode() {
		return context.getNode(qualifier);
	}

	/**
	 * Determines whether or not the preference node with the specified relative
	 * path exists underneath this store's associated preference node.
	 * 
	 * @param path
	 *            The relative path. A relative path must not start with "/"
	 *            (meaning it is an absolute path) and must not be an empty
	 *            string (meaning it is the primary node itself).
	 * @return True if the store's primary node contains a child node with the
	 *         specified relative path, false otherwise.
	 */
	public boolean hasNode(String path) {
		boolean exists = false;
		// Don't process a null path.
		if (path != null) {
			// We only want to give the client code access to child nodes. If
			// the path is empty, then the resulting node would be the store's
			// primary node. If the path starts with a "/", it's an absolute
			// path.
			path = path.trim();
			if (!path.isEmpty() && !path.startsWith("/")) {
				try {
					exists = getPreferenceNode().nodeExists(path);
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}
			}
		}
		return exists;
	}

	/**
	 * Gets the child preferences node from the specified relative path. If such
	 * a node did not exist previously, it will be created.
	 * 
	 * @param path
	 *            The relative path. A relative path must not start with "/"
	 *            (meaning it is an absolute path) and must not be an empty
	 *            string (meaning it is the primary node itself).
	 * @return The child preference node, or null if the path was invalid.
	 */
	public IEclipsePreferences getNode(String path) {
		IEclipsePreferences child = null;
		if (path != null) {
			path = path.trim();
			// We only want to give the client code access to child nodes. If
			// the path is empty, then the resulting node would be the store's
			// primary node. If the path starts with a "/", it's an absolute
			// path.
			path = path.trim();
			if (!path.isEmpty() && !path.startsWith("/")) {
				child = (IEclipsePreferences) getPreferenceNode().node(path);
			}
		}
		return child;
	}

	/**
	 * Removes the child preferences node at the specified relative path.
	 * <p>
	 * This operation should be used instead of calling
	 * {@link IEclipsePreferences#removeNode()}, as the parent node needs to be
	 * flushed afterward.
	 * </p>
	 * 
	 * @param path
	 *            The relative path. A relative path must not start with "/"
	 *            (meaning it is an absolute path) and must not be an empty
	 *            string (meaning it is the primary node itself).
	 * @return True if an {@link IEclipsePreferences} node was removed from the
	 *         store, false otherwise.
	 */
	public boolean removeNode(String path) {
		boolean removed = false;
		// Make sure the relative path exists before proceeding.
		if (hasNode(path)) {
			// Remove the child node from the store's primary parent node. We
			// also need to flush the parent node immediately.
			IEclipsePreferences node = getPreferenceNode();
			IEclipsePreferences child = getNode(path);
			try {
				child.removeNode();
				node.flush();
				removed = true;
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
		return removed;
	}

}
