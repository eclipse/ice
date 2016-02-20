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
package org.eclipse.eavp.viz.service.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a customized {@link ScopedPreferenceStore} that gives
 * client code following options:
 *
 * <ul>
 * <li>Can add and remove child {@link IEclipsePreferences} nodes to the store.</li>
 * <li>Can remove keyed values from the store.</li>
 * <li>Can add and remove values for secure storage.</li>
 * </ul>
 *
 * Changes to the store using any of the aforementioned features are not
 * persisted until {@link #save()} is called by the JFace preference page
 * platform.
 *
 * @author Jordan Deyton
 *
 */
public class CustomScopedPreferenceStore extends ScopedPreferenceStore {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CustomScopedPreferenceStore.class);

	/**
	 * The scoped context to which preferences are stored, usually
	 * {@link InstanceScope#INSTANCE}.
	 * <p>
	 * This variable mirrors a private variable of the name {@code storeContext}
	 * in {@link ScopedPreferenceStore}.
	 * </p>
	 */
	private final IScopeContext context;
	/**
	 * The associated preference node's qualifier or ID, usually the bundle's
	 * ID.
	 * <p>
	 * This variable mirrors a private variable of the name
	 * {@code nodeQualifier} in {@link ScopedPreferenceStore}.
	 * </p>
	 */
	private final String qualifier;

	/**
	 * The default context is the context where getDefault and setDefault
	 * methods will search. This context is also used in the search.
	 * <p>
	 * This variable mirrors a private variable of the same name in
	 * {@link ScopedPreferenceStore}.
	 * </p>
	 */
	private final IScopeContext defaultContext;

	/**
	 * Boolean value indicating whether or not this store has changes to be
	 * saved.
	 * <p>
	 * This variable mirrors a private variable of the same name in
	 * {@link ScopedPreferenceStore}.
	 * </p>
	 */
	private boolean dirty;

	/**
	 * A convenient constructor for getting a default
	 * {@code CustomScopedPreferenceStore} for a class' bundle.
	 *
	 * @param clazz
	 *            The target class.
	 */
	public CustomScopedPreferenceStore(Class<?> clazz) {
		this(InstanceScope.INSTANCE, FrameworkUtil.getBundle(clazz)
				.getSymbolicName());
	}

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

		defaultContext = DefaultScope.INSTANCE;
	}

	/**
	 * Gets the associated default preference node from the
	 * {@link #defaultContext}.
	 *
	 * @return The preference node for default values.
	 */
	private IEclipsePreferences getDefaultPreferenceNode() {
		return defaultContext.getNode(qualifier);
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

		// Convert the path to a valid path string, if possible.
		path = validatePath(path);

		// If the path was valid, get the node corresponding to the path.
		if (path != null) {

			// If the node is new, then we need to mark the store as dirty.
			if (nodeExists(path)) {
				dirty = true;
			}
			// Get or create the node.
			child = (IEclipsePreferences) getPreferenceNode().node(path);
		}
		return child;
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
	 * Gets the associated secure preference node from the
	 * {@link SecurePreferencesFactory}.
	 *
	 * @return The preference node for secure values.
	 */
	private ISecurePreferences getSecurePreferenceNode() {
		ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
		return preferences.node(qualifier);
	}

	/**
	 * Returns the current value of the string-valued, <i>securely stored</i>
	 * preference with the given name.
	 * <p>
	 * Returns the default-default value (the empty string <code>""</code>) if
	 * there is no preference with the given name, or if the current value
	 * cannot be treated as a string.
	 * </p>
	 *
	 * @param name
	 *            The name of the preference.
	 * @return The string-valued preference, which is stored securely.
	 */
	public String getSecureString(String name) {
		String value = STRING_DEFAULT_DEFAULT;
		if (name != null) {
			ISecurePreferences node = getSecurePreferenceNode();
			try {
				value = node.get(name, STRING_DEFAULT_DEFAULT);
			} catch (StorageException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}
		return value;
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

		// Convert the path to a valid path string, if possible.
		path = validatePath(path);

		// If the path was valid, see if its node exists.
		if (path != null) {
			exists = nodeExists(path);
		}

		return exists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.preferences.ScopedPreferenceStore#needsSaving()
	 */
	@Override
	public boolean needsSaving() {
		return dirty || super.needsSaving();
	}

	/**
	 * Determines whether or not a child node exists in the preference store.
	 *
	 * @param validPath
	 *            A path validated by {@link #validatePath(String)}.
	 * @return True if the node existed, false otherwise.
	 */
	private boolean nodeExists(String validPath) {
		boolean exists = false;
		try {
			exists = getPreferenceNode().nodeExists(validPath);
		} catch (BackingStoreException e) {
			logger.error(getClass().getName() + " Exception!", e);
		}
		return exists;
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

		// Try to get the specified node.
		IEclipsePreferences child = getNode(path);
		if (child != null) {
			// Try to remove the node. Do not flush the parent yet. That will
			// need to be done when saving.
			try {
				child.removeNode();
				removed = true;
				dirty = true;
			} catch (BackingStoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

		return removed;
	}

	/**
	 * Removes the specified, <i>securely stored</i> value from the store.
	 *
	 * @param name
	 *            The name of the value to remove.
	 */
	public void removeSecureString(String name) {
		if (name != null) {
			getSecurePreferenceNode().remove(name);
			dirty = true;
		}
	}

	/**
	 * Removes the specified value (including its defaults) from the store.
	 *
	 * @param name
	 *            The name of the value to remove.
	 */
	public void removeValue(String name) {
		getPreferenceNode().remove(name);
		getDefaultPreferenceNode().remove(name);
		dirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.preferences.ScopedPreferenceStore#save()
	 */
	@Override
	public void save() throws IOException {
		// Flush the secure preference node.
		boolean secureFlushed = false;
		getSecurePreferenceNode().flush();
		secureFlushed = true;

		// If the secure store was flushed successfully, we can proceed with the
		// super method's flush. We can't do this within the try because it will
		// catch the super method's thrown IOException.
		if (secureFlushed) {
			// If the super save method throws an exception then neither this
			// class' dirty variable nor the super class' private dirty variable
			// will be set to false.
			super.save();
			dirty = false;
		}

		return;
	}

	/**
	 * Sets the current value of the string-valued, <i>securely stored</i>
	 * preference with the given name.
	 * <p>
	 * A property change event is reported if the current value of the
	 * preference actually changes from its previous value. In the event object,
	 * the property name is the name of the preference, and the old and new
	 * values are wrapped as objects.
	 * </p>
	 *
	 * @param name
	 *            The name of the preference.
	 * @param value
	 *            The value of the preference that must be stored securely.
	 */
	public void setSecureValue(String name, String value) {

		if (name != null && value != null) {
			ISecurePreferences node = getSecurePreferenceNode();
			try {
				node.put(name, value, true);
				dirty = true;
			} catch (StorageException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

		return;
	}

	/**
	 * Validates the path, returning the valid version of the path, or null if
	 * the path is invalid.
	 *
	 * @param path
	 *            The path to validate.
	 * @return The trimmed, validated path, or null if the path is invalid.
	 */
	private String validatePath(String path) {
		if (path != null) {
			path = path.trim();
			// The path must be relative, i.e. it must not be:
			// 1 - The current node (the path is empty)
			// 2 - An absolute path (the path starts with a slash)
			if (path.isEmpty() || path.startsWith("/")) {
				path = null;
			}
		}
		return path;
	}
}
