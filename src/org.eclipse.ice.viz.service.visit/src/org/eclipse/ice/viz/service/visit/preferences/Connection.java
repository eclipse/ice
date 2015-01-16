package org.eclipse.ice.viz.service.visit.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.form.Entry;

/**
 * An instance of this class contains preferences required to connect to a VisIt
 * service.
 * <p>
 * This class is intended to be internal to the VisItVizService bundle and
 * primarily serves as a convenient way to keep track of VisIt connection
 * preferences, which would otherwise be stored directly in a map of strings.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class Connection {

	/**
	 * The ID associated with the current VisIt connection. Usually, the ID is
	 * unique per host/port combination.
	 */
	private String id;
	/**
	 * The host machine that will run VisIt. For the local machine, use
	 * "localhost".
	 */
	private String host;
	/**
	 * The port on the host over which VisIt will be served.
	 */
	private int hostPort;
	/**
	 * The path to the VisIt installation on the host machine. The format should
	 * be specific to the host's operating system.
	 */
	private String path;
	/**
	 * The machine through which the client will connect to the host. If no
	 * proxy is used, this should be empty.
	 */
	private String proxy;
	/**
	 * The port on the proxy to which the client will connect to the host.
	 */
	private int proxyPort;
	/**
	 * The user name for the running VisIt instance.
	 */
	private String user;

	/**
	 * The minimum allowed port number, inclusive.
	 */
	private static final int minPort = 1024;
	/**
	 * The maximum allowed port number, inclusive.
	 */
	private static final int maxPort = 65535;

	// TODO We may want to add equals/hashCode.
	// TODO Test this class.

	/**
	 * The default constructor. Creates a {@code Connection} with the default
	 * values.
	 */
	public Connection() {
		this(null);
	}

	/**
	 * A copy constructor. Creates a {@code Connection} with the same values as
	 * the specified {@code Connection}, or default values if the specified
	 * {@code Connection} is {@code null}.
	 * 
	 * @param connection
	 *            The {@code Connection} to copy.
	 */
	public Connection(Connection connection) {
		if (connection == null) {
			id = "default";
			host = "localhost";
			hostPort = 9600;
			path = "";
			proxy = "";
			proxyPort = 9600;
			user = "";
		} else {
			id = connection.id;
			host = connection.host;
			hostPort = connection.hostPort;
			path = connection.path;
			proxy = connection.proxy;
			proxyPort = connection.proxyPort;
			user = connection.user;
		}

		return;
	}

	/**
	 * Sets the connection ID. This is the ID associated with the current VisIt
	 * connection. Usually, the ID is unique per host/port combination.
	 * 
	 * @param id
	 *            The new ID. If {@code null}, nothing changes.
	 */
	public void setId(String id) {
		if (id != null) {
			this.id = id;
		}
	}

	/**
	 * Gets the ID associated with the current VisIt connection. Usually, the ID
	 * is unique per host/port combination.
	 * 
	 * @return The connection ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the connection host. This is the host machine that will run VisIt.
	 * For the local machine, use "localhost".
	 * 
	 * @param host
	 *            The new host. If {@code null}, nothing changes.
	 */
	public void setHost(String host) {
		if (host != null) {
			this.host = host;
		}
	}

	/**
	 * Gets the host machine that will run VisIt. For the local machine, use
	 * "localhost".
	 * 
	 * @return The connection host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the connection host port. This is the port on the host over which
	 * VisIt will be served.
	 * 
	 * @param port
	 *            The new host port. If the value lies outside the range of
	 *            {@link #minPort} and {@link #maxPort}, inclusive, nothing
	 *            changes.
	 */
	public void setHostPort(int port) {
		if (port >= minPort && port <= maxPort) {
			this.hostPort = port;
		}
	}

	/**
	 * Gets the port on the host over which VisIt will be served.
	 * 
	 * @return The connection host port.
	 */
	public int getHostPort() {
		return hostPort;
	}

	/**
	 * Sets the connection host machine's VisIt installation path. This is the
	 * path to the VisIt installation on the host machine. The format should be
	 * specific to the host's operating system.
	 * 
	 * @param path
	 *            The new path. If {@code null}, nothing changes.
	 */
	public void setPath(String path) {
		if (path != null) {
			this.path = path;
		}
	}

	/**
	 * Gets the path to the VisIt installation on the host machine. The format
	 * should be specific to the host's operating system.
	 * 
	 * @return The connection host machine's VisIt installation path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the connection proxy. This is the machine through which the client
	 * will connect to the host. If no proxy is used, this should be empty.
	 * 
	 * @param proxy
	 *            The new proxy. If {@code null}, nothing changes.
	 */
	public void setProxy(String proxy) {
		if (proxy != null) {
			this.proxy = proxy;
		}
	}

	/**
	 * Gets the machine through which the client will connect to the host. If no
	 * proxy is used, this should be empty.
	 * 
	 * @return The connection proxy.
	 */
	public String getProxy() {
		return proxy;
	}

	/**
	 * Sets the connection proxy port. This is the port on the proxy to which
	 * the client will connect to the host
	 * 
	 * @param port
	 *            The new proxy port. If the value lies outside the range of
	 *            {@link #minPort} and {@link #maxPort}, inclusive, nothing
	 *            changes.
	 */
	public void setProxyPort(int port) {
		if (port >= minPort && port <= maxPort) {
			this.proxyPort = port;
		}
	}

	/**
	 * Gets the port on the proxy to which the client will connect to the host
	 * 
	 * @return The connection's proxy port.
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets the connection user. This is the user name for the running VisIt
	 * instance.
	 * 
	 * @param user
	 *            The new user. If {@code null}, nothing changes.
	 */
	public void setUser(String user) {
		if (user != null) {
			this.user = user;
		}
	}

	/**
	 * Gets the user name for the running VisIt instance.
	 * 
	 * @return The connection user.
	 */
	public String getUser() {
		return user;
	}

}
