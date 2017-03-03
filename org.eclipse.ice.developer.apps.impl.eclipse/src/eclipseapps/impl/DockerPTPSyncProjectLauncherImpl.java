/**
 */
package eclipseapps.impl;

import apps.AppsPackage;
import apps.ProjectLauncher;
import apps.SourcePackage;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerPackage;
import apps.docker.DockerProjectLauncher;

import apps.docker.impl.DockerProjectLauncherImpl;
import eclipseapps.DockerPTPSyncProjectLauncher;
import eclipseapps.EclipseappsFactory;
import eclipseapps.EclipseappsPackage;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.ptp.internal.rdt.sync.git.ui.GitParticipant;
import org.eclipse.ptp.internal.rdt.sync.ui.SynchronizeParticipantRegistry;
import org.eclipse.ptp.internal.rdt.sync.ui.handlers.CommonSyncExceptionHandler;
import org.eclipse.ptp.rdt.sync.core.SyncFlag;
import org.eclipse.ptp.rdt.sync.core.SyncManager;
import org.eclipse.ptp.rdt.sync.core.SyncManager.SyncMode;
import org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipantDescriptor;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Docker PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerPTPSyncProjectLauncherImpl extends DockerProjectLauncherImpl implements DockerPTPSyncProjectLauncher {
	@SuppressWarnings("restriction")
	private class DockerSyncParticipant extends GitParticipant {

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
		
		protected IRemoteConnection connection;
		protected ContainerConfiguration config;
		protected SourcePackage project;
		
		@SuppressWarnings("restriction")
		public DockerSyncParticipant(ISynchronizeParticipantDescriptor descriptor, ContainerConfiguration c, SourcePackage p)
				throws RemoteConnectionException {
			super(descriptor);
			// TODO Auto-generated constructor stub
			// Create the connection to the container
			config = c;
			project = p;
			
			int port = config.getRemoteSSHPort();
			IRemoteServicesManager remoteManager = getService(IRemoteServicesManager.class);

			// If valid, continue on an get the IRemoteConnection
			if (remoteManager != null) {

				// Get the connection type - basically Jsch is index 0
				IRemoteConnectionType connectionType = remoteManager.getRemoteConnectionTypes().get(0);
				IRemoteConnectionWorkingCopy workingCopy = null;

				workingCopy = connectionType.newConnection("Docker Host - localhost:" + port);

				// Set up the Host Service
				IRemoteConnectionHostService service = workingCopy.getService(IRemoteConnectionHostService.class);
				service.setUsePassword(true);
				service.setHostname("localhost");
				service.setPassword("password");
				service.setUsername("root");
				service.setPort(port);

				// Create the new IRemoteConnection
				connection = workingCopy.save();
				connection.open(new NullProgressMonitor());
			}
		}
		
		@Override
		public IRemoteConnection getConnection() {
			return connection;
		}
		
		@Override
		public String getSyncConfigName() {
			return connection.getName();
		}
		
		@Override
		public String getLocation() {
			return "/projects/"+project.getName();
		}
		
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerPTPSyncProjectLauncherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipseappsPackage.Literals.DOCKER_PTP_SYNC_PROJECT_LAUNCHER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean launchProject(SourcePackage proj) {
		// Create the IProject in a language appropriate manner
		// FIXME add more than C++
		languageprojectprovider = EclipseappsFactory.eINSTANCE.createEclipseCppProjectProvider();
		languageprojectprovider.createProject(proj.getName());
		IProject project = ((EclipseCppProjectProviderImpl)languageprojectprovider).getProject();

		// Create the DockerSyncParticipant which keeps track 
		// of metadata and the IRemoteConnection
		ISynchronizeParticipantDescriptor[] providers = SynchronizeParticipantRegistry.getDescriptors();
		DockerSyncParticipant syncPart = null;
		try {
			syncPart = new DockerSyncParticipant(providers[0], containerconfiguration, proj);
		} catch (RemoteConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		// Create the SyncProject
		try {
			SyncManager.makeSyncProject(project, syncPart.getSyncConfigName(), syncPart.getServiceId(),
					syncPart.getConnection(), syncPart.getLocation(), null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		
		// Enable syncing
		SyncManager.setSyncMode(project, SyncMode.ACTIVE);

		// Force an initial sync
		try {
			SyncManager.sync(null, project, SyncFlag.BOTH, new CommonSyncExceptionHandler(false, true));
		} catch (CoreException e) {
			// This should never happen because only a blocking sync can throw a
			// core exception.
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ProjectLauncher.class) {
			switch (baseOperationID) {
				case AppsPackage.PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE: return EclipseappsPackage.DOCKER_PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE;
				default: return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		if (baseClass == DockerProjectLauncher.class) {
			switch (baseOperationID) {
				case DockerPackage.DOCKER_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE: return EclipseappsPackage.DOCKER_PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE;
				default: return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EclipseappsPackage.DOCKER_PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE:
				launchProject((SourcePackage)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //DockerPTPSyncProjectLauncherImpl
