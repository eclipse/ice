package org.eclipse.ice.viz.service.visit.preferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.datastructures.form.TableComponent;

/**
 * A {@code ConnectionManager} manages a list of {@link Connection} preferences
 * for connecting to VisIt instances. It is backed by a {@link LinkedHashMap} to
 * provide fast lookups while keeping the insertion order. Each
 * {@code Connection} is expected to have a unique ID.
 * <p>
 * This class is intended to be internal to the VisItVizService bundle and
 * primarily serves as a convenient way to keep track of VisIt connection
 * preferences, which would otherwise be stored directly in a map of strings.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionManager extends TableComponent implements IdManager {
	
	/**
	 * The backing map of {@link Connection}s.
	 */
	private final Map<String, Connection> connections = new LinkedHashMap<String, Connection>();

	public ConnectionManager() {
		this.setRowTemplate((ArrayList<Entry>) getTemplate());
	}
	
	public boolean idAvailable(String id) {
		return id != null && !(id = id.trim()).isEmpty()
				&& !connections.containsKey(id);
	}
	
	public boolean idAvailable(int id) {
		return (idAvailable(Integer.toString(id)));
	}
	
	public List<Entry> getTemplate() {
		List<Entry> template = new ArrayList<Entry>();
		
		IEntryContentProvider contentProvider;
		
		// TODO descriptions
		
		// ---- id ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("New Connection...");
		Entry idEntry = new IdEntry(contentProvider, this);
		idEntry.setName("Name");
		template.add(idEntry);
		// ---- host ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("localhost");
		Entry hostEntry = new Entry(contentProvider);
		hostEntry.setName("Host");
		template.add(hostEntry);
		// ---- host port ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("9600");
		Entry hostPortEntry = new Entry(contentProvider);
		hostPortEntry.setName("Host Port");
		template.add(hostPortEntry);
		// ---- host OS ---- //
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
		// ---- proxy ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("");
		Entry proxyEntry = new Entry(contentProvider);
		proxyEntry.setName("Proxy");
		template.add(proxyEntry);
		// ---- proxy port ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("9600");
		Entry proxyPortEntry = new Entry(contentProvider);
		proxyPortEntry.setName("Proxy Port");
		template.add(proxyPortEntry);
		// ---- VisIt user ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("");
		Entry userEntry = new Entry(contentProvider);
		userEntry.setName("User");
		template.add(userEntry);
		
		return template;
	}
		
	/**
	 * Gets an insertion-ordered list of all {@link Connection} IDs.
	 * 
	 * @return A list of connection IDs.
	 */
	public List<String> getConnectionIds() {
		return new ArrayList<String>(connections.keySet());
	}

	/**
	 * Gets an insertion-ordered list of all {@link Connection}s.
	 * 
	 * @return A list of {@code Connection}s.
	 */
	public List<Connection> getConnections() {
		return new ArrayList<Connection>(connections.values());
	}

	/**
	 * Gets the {@link Connection} with the specified ID.
	 * 
	 * @param id
	 *            The ID of the connection.
	 * @return A {@code Connection} with the specified ID or {@code null} if a
	 *         matching {@code Connection} could not be found.
	 */
	public Connection getConnection(String id) {
		return connections.get(id);
	}

	/**
	 * Adds the specified {@link Connection} to the manager. {@code null} values
	 * and {@code Connections} with the same ID are not permitted.
	 * 
	 * @param connection
	 *            The {@code Connection} to add.
	 * @return True if the {@code Connection} is not {@code null} and its ID was
	 *         not already in use in this manager, false otherwise.
	 */
	public boolean addConnection(Connection connection) {
		boolean added = false;
		if (connection != null) {
			String key = connection.getId();
			if (!connections.containsKey(key)) {
				connections.put(key, connection);
				added = true;
			}
		}

		return added;
	}

	/**
	 * Removes the specified {@link Connection} from the manager.
	 * 
	 * @param connection
	 *            The {@code Connection} to remove.
	 * @return True if the {@code Connection} was actually removed from the
	 *         manager, false otherwise.
	 */
	public boolean removeConnection(Connection connection) {
		boolean removed = false;
		if (connection != null) {
			String key = connection.getId();
			if (connections.get(key) == connection) {
				connections.remove(key);
				removed = true;
			}
		}
		return removed;
	}

	/**
	 * Removes all {@link Connection}s from the manager.
	 */
	public void clear() {
		connections.clear();
	}

	// private static final String SEPARATOR = ";";
	//
	// public void syncFromPreferences(IPreferenceStore store) {
	//
	// // Reset all known connections.
	// this.connections.clear();
	//
	// String prefix = "connection.";
	//
	// String[] id = store.getString(prefix + "id").split(SEPARATOR);
	// String[] host = store.getString(prefix + "host").split(SEPARATOR);
	// String[] hostPort = store.getString(prefix +
	// "hostPort").split(SEPARATOR);
	// String[] path = store.getString(prefix + "path").split(SEPARATOR);
	// String[] proxy = store.getString(prefix + "proxy").split(SEPARATOR);
	// String[] proxyPort = store.getString(prefix +
	// "proxyPort").split(SEPARATOR);
	// String[] user = store.getString(prefix + "user").split(SEPARATOR);
	//
	// for (int i = 0; i < id.length; i++) {
	// Connection connection = new Connection();
	// connections.put(id[i], connection);
	// connection.setId(id[i]);
	// connection.setHost(host[i]);
	// connection.setHostPort(Integer.parseInt(hostPort[i]));
	// connection.setPath(path[i]);
	// connection.setProxy(proxy[i]);
	// connection.setProxyPort(Integer.parseInt(proxyPort[i]));
	// connection.setUser(user[i]);
	// }
	//
	// return;
	// }
	//
	// public void syncToPreferences(IPreferenceStore store) {
	//
	// String prefix = "connection.";
	//
	// String id = "";
	// String host = "";
	// String hostPort = "";
	// String path = "";
	// String proxy = "";
	// String proxyPort = "";
	// String user = "";
	//
	// List<Connection> connections = getConnections();
	//
	// String delimit = "";
	// for (Connection connection : connections) {
	// id += delimit + connection.getId();
	// host += delimit + connection.getHost();
	// hostPort += delimit + Integer.toString(connection.getHostPort());
	// path += delimit + connection.getPath();
	// proxy += delimit + connection.getProxy();
	// proxyPort += delimit + Integer.toString(connection.getProxyPort());
	// user += delimit + connection.getUser();
	// delimit = SEPARATOR;
	// }
	//
	// store.setValue(prefix + "id", id);
	// store.setValue(prefix + "host", host);
	// store.setValue(prefix + "hostPort", hostPort);
	// store.setValue(prefix + "path", path);
	// store.setValue(prefix + "proxy", proxy);
	// store.setValue(prefix + "proxyPort", proxyPort);
	// store.setValue(prefix + "user", user);
	//
	// return;
	// }

}
