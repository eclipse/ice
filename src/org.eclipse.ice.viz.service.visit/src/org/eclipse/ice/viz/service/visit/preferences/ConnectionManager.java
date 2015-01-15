package org.eclipse.ice.viz.service.visit.preferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class ConnectionManager {

	// TODO Test this class. We may want to add equals/hashcode methods.
	
	/**
	 * The backing map of {@link Connection}s.
	 */
	private final Map<String, Connection> connections = new LinkedHashMap<String, Connection>();

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
