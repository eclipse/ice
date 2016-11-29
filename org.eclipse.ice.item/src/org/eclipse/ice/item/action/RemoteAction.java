package org.eclipse.ice.item.action;

import java.util.List;

import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.ui.IRemoteUIConnectionService;
import org.eclipse.remote.ui.IRemoteUIConnectionWizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * The RemoteAction is an abstract extension of the Action abstract class that
 * provides subclasses with a means to create an IRemoteConnection to a remote
 * host by providing the String host name.
 * 
 * @author Alex McCaskey
 *
 */
public abstract class RemoteAction extends Action {

	/**
	 * Reference to the IRemoteConnection to the remote host.
	 */
	protected IRemoteConnection connection;

	/**
	 * Return the OSGi service with the given service interface.
	 *
	 * @param service
	 *            service interface
	 * @return the specified service or null if it's not registered
	 */
	protected <T> T getService(Class<T> service) {
		BundleContext context = FrameworkUtil.getBundle(getClass())
				.getBundleContext();
		if (context != null) {
			ServiceReference<T> ref = context.getServiceReference(service);
			return ref != null ? context.getService(ref) : null;
		} else {
			return null;
		}
	}

	/**
	 * This method returns an IRemoteConnection stored in the Remote Preferences
	 * that corresponds to the provided hostname.
	 *
	 * @param host
	 * @return
	 */
	protected IRemoteConnection getRemoteConnection(String host) {
		// Get the IRemoteServicesManager
		IRemoteServicesManager remoteManager = getService(
				IRemoteServicesManager.class);

		// If valid, continue on an get the IRemoteConnection
		if (remoteManager != null) {

			// Get the connection type - basically Jsch is index 0
			// Get the types of available remote connections
			List<IRemoteConnectionType> types = getService(
					IRemoteServicesManager.class).getRemoteConnectionTypes();

			// The type of connection being used, SSH in this case
			IRemoteConnectionType connectionType = null;

			// Search for the connections of type SSH.
			for (IRemoteConnectionType type : types) {
				if ("SSH".equals(type.getName())) {
					connectionType = type;
					break;
				}
			}

			if (connectionType != null) {

				// Loop over existing connections to see if the user already
				// specified
				// a connection to the provided host
				for (IRemoteConnection c : connectionType.getConnections()) {
					// String connectionHost = c
					// .getService(IRemoteConnectionHostService.class)
					// .getHostname();
					//
					// String a = InetAddress.getByName(host).getHostAddress();
					// String b = InetAddress.getByName(connectionHost)
					// .getHostAddress();
					//
					// c.getName()

					if (host.equals(c.getName())) {
						connection = c;

						// Found it, return the connection
						return connection;
					}
				}

				// If no connection found, let's ask the user to define it.
				if (connection == null) {

					// Get a final reference to the connection type
					final IRemoteConnectionType finalType = connectionType;

					// Open the Remote Connection Wizard, syncExec here
					// because we want to wait til its finished.
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {

							// Get the UI Connection Service
							IRemoteUIConnectionService uiConnService = finalType
									.getService(
											IRemoteUIConnectionService.class);

							// Create a UI Connection Wizard
							IRemoteUIConnectionWizard wizard = uiConnService
									.getConnectionWizard(
											PlatformUI.getWorkbench()
													.getActiveWorkbenchWindow()
													.getShell());

							// If valid, open it and save/open the
							// IRemoteConnection
							if (wizard != null) {
								wizard.setConnectionName(host);
								try {
									connection = wizard.open().save();
									if (connection != null) {
										connection.open(null);
									}
								} catch (RemoteConnectionException e) {
									logger.error(getClass().getName()
											+ " Exception!", e);
								}
							}
						}
					});
				}
			}
		}

		return connection;
	}
}
