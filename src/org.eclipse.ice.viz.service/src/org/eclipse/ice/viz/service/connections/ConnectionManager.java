/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.datastructures.form.TableComponent;

/**
 * This class manages a list of uniquely-keyed VisIt connections within an ICE
 * {@link TableComponent}.
 * <p>
 * Each connection is defined by the following parameters (although it must be
 * noted that the only required unique value is the name/key/ID):
 * <ol>
 * <li><b>Name</b> - A unique ID or key for the connection. This should be a
 * user-friendly value.</li>
 * <li><b>Host</b> - The FQDN or IP address of the remote host. For local
 * launches, this should be "localhost".</li>
 * <li><b>Host Port</b> - The port on which the host serves VisIt. The default
 * is 9600.</li>
 * <li><b>Host OS</b> - The operating system on the host.</li>
 * <li><b>Path</b> - The path to the VisIt binaries on the host.</li>
 * <li><b>Username</b> - The username to use when connecting to a remote host.</li>
 * <li><b>Password</b> - The password to use when connecting to a remote host.</li>
 * <li><b>Proxy</b> - The FQDN or IP address of a proxy. If specified, the
 * connection will route through the proxy to the host.</li>
 * <li><b>Proxy Port</b> - The port to which the client must connect to use the
 * proxy.</li>
 * <li><b>VisIt User</b> - The VisIt user name.</li>
 * <li><b>VisIt Password</b> - The VisIt session password.</li>
 * </ol>
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionManager extends TableComponent implements IKeyManager,
		IUpdateableListener {

	// TODO Remove the debug output from this class.

	/**
	 * A map from the user-friendly name/key of a connection to its row index as
	 * maintained by the underlying {@code TableComponent}.
	 */
	private final Map<String, Integer> keyToIndexMap = new HashMap<String, Integer>();
	/**
	 * A map from the row index as maintained by the underlying
	 * {@code TableComponent} to the user-friendly name/key of a connection.
	 */
	private final Map<Integer, String> indexToKeyMap = new HashMap<Integer, String>();

	/**
	 * This keeps track of the connection name/key column's {@code Entry}s. This
	 * helps us determine the previous key (see {@link #oldKeys}) for a key
	 * {@code Entry} that was changed.
	 */
	private final List<Entry> keyEntries = new ArrayList<Entry>();
	/**
	 * This keeps track of the previous values for the connection name/key
	 * column's {@code Entry}s. This enables us to remove old values from the
	 * key maps (see {@link #keyToIndexMap} and {@link #indexToKeyMap}) when a
	 * key value changes.
	 */
	private final List<String> oldKeys = new ArrayList<String>();

	/*-
	 * There are three situations that will require the above two maps to be
	 * updated:
	 * 
	 * 1 - A row is added (override addRow())
	 * 2 - A row is removed (override deleteRow(int))
	 * 3 - The name/key/ID for a row is updated (listen for updates from each
	 *     name/key/ID Entry)
	 */

	/**
	 * A list of key change listeners.
	 */
	private final List<IKeyChangeListener> keyListeners = new ArrayList<IKeyChangeListener>();

	/**
	 * The default constructor.
	 */
	public ConnectionManager() {
		// Set the default row template. This should never change.
		super.setRowTemplate(getConnectionTemplate());
	}

	/**
	 * Does nothing. The row template cannot be changed for this class.
	 */
	@Override
	public void setRowTemplate(ArrayList<Entry> template) {
		// Do nothing...
	}

	/**
	 * Gets the row template for a connection as defined in the
	 * {@link ConnectionManager documentation for the class}.
	 * 
	 * @return An {@code ArrayList<Entry>} containing the template {@code Entry}
	 *         s for each exposed connection property.
	 */
	protected ArrayList<Entry> getConnectionTemplate() {
		ArrayList<Entry> template = new ArrayList<Entry>();

		// TODO These Entries need descriptions.
		// TODO Add the password Entries.

		IEntryContentProvider contentProvider;

		// ---- name ---- //
		KeyEntryContentProvider keyContentProvider = new KeyEntryContentProvider(
				this);
		Entry keyEntry = new KeyEntry(keyContentProvider, this);
		keyEntry.setName("Name");
		template.add(keyEntry);
		// ---- host ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setDefaultValue("localhost");
		Entry hostEntry = new Entry(contentProvider);
		hostEntry.setName("Host");
		template.add(hostEntry);
		// ---- host port ---- //
		PortEntryContentProvider portContentProvider = new PortEntryContentProvider();
		portContentProvider.setDefaultValue("9600");
		Entry hostPortEntry = new PortEntry(portContentProvider);
		hostPortEntry.setName("Host Port");
		template.add(hostPortEntry);
		// ---- host os ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Discrete);
		ArrayList<String> systems = new ArrayList<String>(3);
		systems.add("Windows");
		systems.add("Linux");
		systems.add("OS X");
		contentProvider.setAllowedValues(systems);
		contentProvider.setDefaultValue("Windows");
		Entry hostOSEntry = new Entry(contentProvider);
		hostOSEntry.setName("Host OS");
		template.add(hostOSEntry);
		// ---- path ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		Entry pathEntry = new Entry(contentProvider);
		pathEntry.setName("Path");
		template.add(pathEntry);
		// ---- username ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("");
		Entry userEntry = new Entry(contentProvider);
		userEntry.setName("User");
		template.add(userEntry);
		// ---- password ---- //
		// contentProvider = new BasicEntryContentProvider();
		// contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		// contentProvider.setDefaultValue("");
		// Entry passwordEntry = new Entry(contentProvider);
		// passwordEntry.setName("Password");
		// template.add(passwordEntry);

		return template;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.TableComponent#addRow()
	 */
	@Override
	public int addRow() {

		// Try to add a new row.
		int index = super.addRow();

		// If a row was actually added, we need to do some bookkeeping. If the
		// return value is -1, then a row was not added.
		if (index >= 0) {
			// Get the name/key/ID of the connection.
			List<Entry> connection = getRow(index);
			Entry keyEntry = connection.get(0);
			String key = keyEntry.getValue();

			// Throw an exception if the new key is already in the bookkeeping.
			// This should never happen as long as we use a KeyEntry and a
			// KeyEntryContentProvider!
			if (keyToIndexMap.containsKey(key)) {
				throw new IllegalArgumentException("ConnectionManager error: "
						+ "A new connection was added with the key \"" + key
						+ "\", but the key is already in use!");
			}

			// Update the key maps.
			keyToIndexMap.put(key, index);
			indexToKeyMap.put(index, key);
			// Update the key lists.
			keyEntries.add(keyEntry);
			oldKeys.add(key);

			// Register with the key Entry for changes to the key.
			keyEntry.register(this);

			// Notify key change listeners that a new key was added.
			notifyKeyChangeListeners(null, key);
		}

		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.TableComponent#deleteRow(int)
	 */
	@Override
	public boolean deleteRow(int index) {
		// Try to delete the row. If it was deleted, we will need to update our
		// bookkeeping.
		boolean deleted = super.deleteRow(index);
		if (deleted) {
			// Unregister from the key Entry.
			keyEntries.get(index).unregister(this);
			// Get the key from the old key list.
			String key = oldKeys.get(index);

			// Update the key maps.
			keyToIndexMap.remove(key);
			indexToKeyMap.remove(index);
			// Update the key lists.
			keyEntries.remove(index);
			oldKeys.remove(index);

			// Notify key change listeners that a key was removed.
			notifyKeyChangeListeners(key, null);
		}
		return deleted;
	}

	/**
	 * Updates the bookkeeping for connection keys. If there is a key conflict,
	 * this method throws an exception.
	 * 
	 * @param component
	 *            The updated component. This should only every be an
	 *            {@code Entry} for a connection's key.
	 */
	@Override
	public void update(IUpdateable component) {
		// See if the updated component is one of the key Entries. We have to
		// test by reference, not by equality.
		int index = -1;
		for (int i = 0; i < keyEntries.size(); i++) {
			if (component == keyEntries.get(i)) {
				index = i;
				i = keyEntries.size();
			}
		}
		System.out.println(index);
		if (index >= 0) {
			// Get the new and old keys from the key Entry.
			Entry keyEntry = keyEntries.get(index);
			String newKey = keyEntry.getValue();
			String oldKey = oldKeys.get(index);

			// If the key actually changed, we need to update our bookkeeping.
			if (!oldKey.equals(newKey)) {
				// Throw an exception if the new key is already in the
				// bookkeeping!
				if (keyToIndexMap.containsKey(newKey)) {
					throw new IllegalArgumentException(
							"ConnectionManager error: "
									+ "A key Entry was updated from \""
									+ oldKey + "\" to \"" + newKey
									+ "\", but the key \"" + newKey
									+ "\" is already in use!");
				}

				System.out.println("Key \"" + oldKey + "\" was changed to \""
						+ newKey + "\"");

				// Update the key maps. Note that we must remove the old key.
				keyToIndexMap.remove(oldKey);
				keyToIndexMap.put(newKey, index);
				indexToKeyMap.put(index, newKey);
				// Update the old key list.
				oldKeys.set(index, newKey);

				// Notify key change listeners that a key was changed.
				notifyKeyChangeListeners(oldKey, newKey);
			}
		}
		return;
	}

	/**
	 * Gets the list of connection names or keys.
	 * 
	 * @return The connection names.
	 */
	public List<String> getConnectionNames() {
		return new ArrayList<String>(oldKeys);
	}

	/**
	 * Gets the row from the underlying {@code TableComponent} corresponding to
	 * the name of the connection.
	 * 
	 * @param name
	 *            The name of the connection.
	 * @return A row from the {@code TableComponent}, or null if the name is
	 *         invalid.
	 */
	public List<Entry> getConnection(String name) {
		Integer id = keyToIndexMap.get(name);
		return id != null ? getRow(id) : null;
	}

	// ---- Implements IKeyManager ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.visit.connections.KeyManager#keyAvailable
	 * (java.lang.String)
	 */
	@Override
	public boolean keyAvailable(String key) {
		return key != null && !keyToIndexMap.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.visit.connections.KeyManager#getAvailableKeys
	 * ()
	 */
	@Override
	public List<String> getAvailableKeys() {
		// Since we do not restrict the key names, return an empty list.
		return new ArrayList<String>(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.visit.connections.KeyManager#getNextKey()
	 */
	@Override
	public String getNextKey() throws IllegalStateException {
		String prefix = "Connection";
		int i = 1;
		String key = prefix + Integer.toString(i);
		while (!keyAvailable(key)) {
			key = prefix + Integer.toString(++i);
		}
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.IKeyManager#addKeyChangeListener
	 * (org.eclipse.ice.viz.service.connections.IKeyChangeListener)
	 */
	@Override
	public void addKeyChangeListener(IKeyChangeListener listener) {
		if (listener != null && !keyListeners.contains(listener)) {
			keyListeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.IKeyManager#removeKeyChangeListener
	 * (org.eclipse.ice.viz.service.connections.IKeyChangeListener)
	 */
	@Override
	public void removeKeyChangeListener(IKeyChangeListener listener) {
		keyListeners.remove(listener);
	}

	// -------------------------------- //

	/**
	 * Notifies the {@link #keyListeners} of key change events on a separate
	 * daemon thread.
	 * 
	 * @param oldKey
	 *            The previous key. Null if the key is added.
	 * @param newKey
	 *            The new key. Null if the key is deleted.
	 */
	private void notifyKeyChangeListeners(final String oldKey,
			final String newKey) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				for (IKeyChangeListener listener : keyListeners) {
					listener.keyChanged(oldKey, newKey);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		return;
	}
}
