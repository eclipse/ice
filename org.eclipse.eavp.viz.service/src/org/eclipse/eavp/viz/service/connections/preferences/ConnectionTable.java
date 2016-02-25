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
package org.eclipse.eavp.viz.service.connections.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizTableComponent;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;

/**
 * This class manages a list of uniquely-keyed connections within an ICE
 * {@link TableComponent}.
 * <p>
 * Each connection is defined by the following parameters (although it must be
 * noted that the only required unique value is the name/key/ID):
 * </p>
 * <ol>
 * <li><b>Name</b> - A unique ID or key for the connection. This should be a
 * user-friendly value.</li>
 * <li><b>Host</b> - The FQDN or IP address of the remote host. For local
 * launches, this should be "localhost".</li>
 * <li><b>Port</b> - The port on which the host serves the connection. The
 * default is 9600.</li>
 * <li><s><b>Host OS</b> - The operating system on the host.</s></li>
 * <li><b>Path</b> - The path to the VisIt binaries on the host.</li>
 * <li><s><b>Username</b> - The username to use when connecting to a remote
 * host.</s></li>
 * <li><s><b>Password</b> - The password to use when connecting to a remote
 * host.</s></li>
 * <li><s><b>Proxy</b> - The FQDN or IP address of a proxy. If specified, the
 * connection will route through the proxy to the host.</s></li>
 * <li><s><b>Proxy Port</b> - The port to which the client must connect to use
 * the proxy.</s></li>
 * </ol>
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionTable extends VizTableComponent {

	// TODO Pull from PTP preferences if possible.

	/**
	 * Manages the keys or names of the connections. Each row's first Entry
	 * corresponds to its name and must be unique, although there is no
	 * restriction on allowed names.
	 */
	private final IKeyManager keyManager;

	/**
	 * A map from the user-friendly name/key of a connection to its row index as
	 * maintained by the underlying {@code TableComponent}.
	 */
	private final Map<String, Integer> keyToIndexMap = new HashMap<String, Integer>();

	/**
	 * A list of listeners added to the row Entry containing the connection
	 * key/name. These will be used to update the key/name bookkeeping when the
	 * Entry changes.
	 */
	private final List<IVizUpdateableListener> keyListeners = new ArrayList<IVizUpdateableListener>();

	/**
	 * The default constructor.
	 */
	public ConnectionTable() {
		// Create the connection key (name) manager and the key listener.
		keyManager = createKeyManager();
		// keyEntryListener = createKeyEntryListener();

		// Set the default row template.
		super.setRowTemplate(createConnectionTemplate());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizTableComponent#addRow()
	 */
	@Override
	public int addRow() {

		// Try to add a new row.
		int index = super.addRow();

		// If a row was actually added, we need to do some bookkeeping. If the
		// return value is -1, then a row was not added.
		if (index >= 0) {
			// Get the new row.
			List<VizEntry> connection = getRow(index);

			// Update its key to the next available one.
			VizEntry keyEntry = connection.get(0);
			final String key = keyManager.getNextKey();
			keyEntry.setValue(key);

			// Update the key bookkeeping.
			keyToIndexMap.put(key, index);

			// Register with the key Entry for changes to the key.
			IVizUpdateableListener keyListener = new IVizUpdateableListener() {
				/**
				 * The previous key. It starts off as the new key above.
				 */
				private String oldKey = key;

				/**
				 * Updates the bookkeeping if the key/name changes.
				 */
				@Override
				public void update(IVizUpdateable component) {
					// Convert it to an Entry and get its key.
					VizEntry entry = (VizEntry) component;
					String newKey = entry.getValue();

					// If the name changes, update the bookkeeping.
					if (!oldKey.equals(newKey)) {
						int i = keyToIndexMap.remove(oldKey);
						keyToIndexMap.put(newKey, i);
						oldKey = newKey;
					}

					return;
				}
			};
			keyListeners.add(keyListener);
			keyEntry.register(keyListener);
		}

		return index;
	}

	/**
	 * Gets the row template for a connection as defined in the
	 * {@link ConnectionTable documentation for the class}.
	 * 
	 * @return An {@code ArrayList<Entry>} containing the template {@code Entry}
	 *         s for each exposed connection property.
	 */
	protected ArrayList<VizEntry> createConnectionTemplate() {
		ArrayList<VizEntry> template = new ArrayList<VizEntry>();

		IVizEntryContentProvider contentProvider;

		// ---- name ---- //
		KeyEntryContentProvider keyContentProvider = new KeyEntryContentProvider(keyManager);
		VizEntry keyEntry = new KeyEntry(keyContentProvider);
		keyEntry.setName("Name");
		keyEntry.setDescription("The name of the connection. This must be unique.");
		template.add(keyEntry);
		// ---- host ---- //
		contentProvider = new BasicVizEntryContentProvider();
		contentProvider.setDefaultValue("localhost");
		VizEntry hostEntry = new VizEntry(contentProvider);
		hostEntry.setName("Host");
		hostEntry.setDescription(
				"The FQDN or IP address of the remote host.%n" + "For local launches, this should be \"localhost\".");
		template.add(hostEntry);
		// ---- host port ---- //
		PortEntryContentProvider portContentProvider = new PortEntryContentProvider();
		portContentProvider.setDefaultValue("9600");
		VizEntry hostPortEntry = new PortEntry(portContentProvider);
		hostPortEntry.setName("Port");
		hostPortEntry.setDescription("The port on which the host serves the connection.%n"
				+ "Should be a positive integer within the port range "
				+ Integer.toString(PortEntryContentProvider.MIN_PORT) + "-"
				+ Integer.toString(PortEntryContentProvider.MAX_PORT) + ".");
		template.add(hostPortEntry);
		// ---- path ---- //
		contentProvider = new BasicVizEntryContentProvider();
		contentProvider.setAllowedValueType(VizAllowedValueType.Undefined);
		VizEntry pathEntry = new VizEntry(contentProvider);
		pathEntry.setName("Path");
		pathEntry.setDescription("The full path to the local or remote installation.");
		template.add(pathEntry);

		return template;
	}

	/**
	 * Creates a default key manager for managing connection key names.
	 * 
	 * @return The key manager for connection names.
	 */
	private IKeyManager createKeyManager() {
		return new IKeyManager() {
			@Override
			public void addKeyChangeListener(IKeyChangeListener listener) {
				// if (listener != null && !keyListeners.contains(listener)) {
				// keyListeners.add(listener);
				// }
			}

			@Override
			public List<String> getAvailableKeys() {
				// Since we do not restrict the key names, return an empty list.
				return new ArrayList<String>(0);
			}

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

			@Override
			public boolean keyAvailable(String key) {
				return key != null && !keyToIndexMap.containsKey(key);
			}

			@Override
			public void removeKeyChangeListener(IKeyChangeListener listener) {
				// keyListeners.remove(listener);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizTableComponent#deleteRow(int)
	 */
	@Override
	public boolean deleteRow(int index) {
		// Try to delete the row. If it was deleted, we will need to update our
		// bookkeeping.

		boolean deleted = false;

		if (index >= 0 && index < numberOfRows()) {
			// Get the row.
			List<VizEntry> row = getRow(index);

			deleted = super.deleteRow(index);
			if (deleted) {
				// Unregister the key listener from the KeyEntry and dispose the
				// listener.
				row.get(0).unregister(keyListeners.remove(index));

				// Get the key from the deleted row.
				String key = row.get(0).getValue();

				// Update the key map.
				keyToIndexMap.remove(key);

				// Update all other values in the key map.
				for (int i = index; i < numberOfRows(); i++) {
					key = getRow(i).get(0).getValue();
					keyToIndexMap.put(key, i);
				}
			}
		}
		return deleted;
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
	public List<VizEntry> getConnection(String name) {
		Integer id = keyToIndexMap.get(name);
		return id != null ? getRow(id) : null;
	}

	/**
	 * Gets the set of connection names or keys.
	 * 
	 * @return The connection names.
	 */
	public Set<String> getConnectionNames() {
		return new TreeSet<String>(keyToIndexMap.keySet());
	}

	/**
	 * Does nothing. The row template cannot be changed for this class.
	 */
	@Override
	public void setRowTemplate(ArrayList<VizEntry> template) {
		// Do nothing...
	}

}
