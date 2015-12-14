package org.eclipse.ice.item.action;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * The RemoteAction is an abstract extension of the Action abstract 
 * class that provides subclasses with a means to create an 
 * IRemoteConnection to a remote host by providing the String host name. 
 * 
 * @author Alex McCaskey
 *
 */
public abstract class RemoteAction extends Action {

	/**
	 * Reference to the IRemoteConnection to the remote 
	 * host. 
	 */
	protected IRemoteConnection connection;
	
	/**
	 * Return the OSGi service with the given service interface.
	 *
	 * @param service
	 *            service interface
	 * @return the specified service or null if it's not registered
	 */
	private <T> T getService(Class<T> service) {
		BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference<T> ref = context.getServiceReference(service);
		return ref != null ? context.getService(ref) : null;
	}

	/**
	 * This method returns an IRemoteConnection stored in the Remote Preferences
	 * that corresponds to the provided hostname.
	 *
	 * @param host
	 * @return
	 */
	protected IRemoteConnection getRemoteConnection(String host) {
		IRemoteServicesManager remoteManager = getService(IRemoteServicesManager.class);
		IRemoteConnection connection = null;
		IRemoteConnectionType connectionType = remoteManager.getRemoteConnectionTypes().get(0);
		if (connectionType != null) {
			try {

				for (IRemoteConnection c : connectionType.getConnections()) {
					String connectionHost = c.getService(IRemoteConnectionHostService.class).getHostname();
					if (InetAddress.getByName(host).getHostAddress()
							.equals(InetAddress.getByName(connectionHost).getHostAddress())) {
						connection = c;
						break;
					}

				}
			} catch (UnknownHostException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}
		return connection;
	}
}
