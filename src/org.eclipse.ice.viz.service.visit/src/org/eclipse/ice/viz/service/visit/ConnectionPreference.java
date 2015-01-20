package org.eclipse.ice.viz.service.visit;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum defines connection preferences supported by VisIt. Each preference
 * ID maps to a specific VisIt connection property. This enum includes the ID, a
 * developer-friendly name (use {@code toString()}, a user-friendly name and
 * description, and a default string value for each preference.
 * 
 * @author Jordan Deyton
 *
 */
public enum ConnectionPreference {

	// FIXME Currently, this enum maps 1-1 to the VisItSwtConnection's input
	// map/properties. We will want to conceal some of these from the user, and
	// we will also need different preferences in the PreferenceStore that
	// controls these values.

	/**
	 * The ID associated with the current VisIt connection.
	 */
	ConnectionID("connId", "default", "Connection ID",
			"The ID associated with the current VisIt connection."),
	/**
	 * The host machine that will run VisIt.
	 */
	Host("url", "localhost", "Host Name",
			"The host machine that will run VisIt."),
	/**
	 * The port on the host over which VisIt will be served.
	 */
	HostPort("port", "9600", "Host Port",
			"The port on the host over which VisIt will be served."),
	/**
	 * The path to the VisIt installation on the host machine.
	 */
	VisItPath("visDir", "/home/user/visit/install/bin", "VisIt Path",
			"The path to the VisIt installation on the host machine."),
	/**
	 * Whether to launch a new VisIt instance or connect to an existing one.
	 */
	LaunchInstance("isLaunch", "true", "Launch Instance",
			"Whether to launch a new VisIt instance or connect to an existing one."),
	/**
	 * Whether to use a proxy to connect to the VisIt host or not.
	 */
	UseProxy("useTunneling", "false", "Use Proxy",
			"Whether to use a proxy to connect to the VisIt host or not."),
	/**
	 * The machine through which the client will connect to the host.
	 */
	Proxy("gateway", "", "Proxy",
			"The machine through which the client will connect to the host."),
	/**
	 * The port on the proxy to which the client will connect to the host.
	 */
	ProxyPort("localGatewayPort", "", "Proxy Port",
			"The port on the proxy to which the client will connect to the host."),
	/**
	 * The user name for the running VisIt instance.
	 */
	SessionUser("username", "user", "Username",
			"The user name for the running VisIt instance."),
	/**
	 * The password used to connect to the running VisIt instance.
	 */
	SessionPassword("password", "notused", "Password",
			"The password used to connect to the running VisIt instance."),
	/**
	 * The type or data or method used to send VisIt render information over the
	 * network.
	 */
	DataType(
			"dataType",
			"image",
			"Data Type",
			"The type or data or method used to send VisIt render information over the network."),
	/**
	 * The width of the view on the client.
	 */
	WindowWidth("windowWidth", "1340", "Window Width", "(no description)"),
	/**
	 * The height of the view on the client.
	 */
	WindowHeight("windowHeight", "1020", "Window Height",
			"The height of the view on the client."),
	/**
	 * The ID associated with the VisIt view to display.
	 */
	WindowID("windowId", "1", "Window ID",
			"The ID associated with the VisIt view to display.");

	/**
	 * The VisIt-widget-friendly ID for the connection preference.
	 */
	private String id;
	/**
	 * The default value for the connection preference.
	 */
	private String defaultValue;
	/**
	 * The user-friendly name for the connection preference.
	 */
	private String name;
	/**
	 * The user-friendly description for the connection preference.
	 */
	private String description;

	/**
	 * A map used to look up an enum value based on a provided {@link #id}.
	 */
	private static Map<String, ConnectionPreference> idMap;
	static {
		// Initialize the map based on the enum values and their IDs.
		idMap = new HashMap<String, ConnectionPreference>();
		for (ConnectionPreference p : values()) {
			idMap.put(p.id, p);
		}
	}

	/**
	 * A private constructor. The class variables are set based on the
	 * parameters.
	 */
	private ConnectionPreference(String id, String defaultValue, String name,
			String description) {
		this.defaultValue = defaultValue;
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * Gets the default value for the connection preference.
	 * 
	 * @return A string representing the connection preference's default value.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Gets the VisIt-widget-friendly ID for the connection preference.
	 * 
	 * @return A string representing the connection preference's
	 *         VisIt-widget-friendly ID.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the user-friendly name for the connection preference.
	 * 
	 * @return A user-friendly string representing the name of the connection
	 *         preference.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the user-friendly description for the connection preference.
	 * 
	 * @return A user-friendly string describing the connection preference.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Converts a string ID to an enum value. This operation is backed by a
	 * {@code HashMap}.
	 * <p>
	 * To convert the developer-friendly name of a preference to an enum value,
	 * use {@link #valueOf(String)}.
	 * </p>
	 * 
	 * @param id
	 *            The VisIt-widget-friendly ID to convert.
	 * @return The corresponding enum value.
	 * @throws IllegalArgumentException
	 *             An exception is thrown if a matching enum value cannot be
	 *             found.
	 * 
	 * @see #valueOf(String)
	 */
	public static ConnectionPreference fromID(String id)
			throws IllegalArgumentException {
		ConnectionPreference p = idMap.get(id);
		if (p == null) {
			throw new IllegalArgumentException(
					"ConnectionPreference error: "
							+ "Matching connection preference could not be found for id \""
							+ id + "\"");
		}
		return p;
	}

}
