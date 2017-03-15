/**
 */
package eclipseapps.impl;

import apps.SourcePackage;
import apps.docker.impl.DockerProjectLauncherImpl;
import eclipseapps.DockerPTPSyncProjectLauncher;
import eclipseapps.EclipseappsFactory;
import eclipseapps.EclipseappsPackage;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.ptp.internal.rdt.sync.ui.handlers.CommonSyncExceptionHandler;
import org.eclipse.ptp.rdt.sync.core.SyncConfig;
import org.eclipse.ptp.rdt.sync.core.SyncConfigManager;
import org.eclipse.ptp.rdt.sync.core.SyncFlag;
import org.eclipse.ptp.rdt.sync.core.SyncManager;
import org.eclipse.ptp.rdt.sync.core.SyncManager.SyncMode;
import org.eclipse.ptp.rdt.sync.core.exceptions.MissingConnectionException;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.IRemoteProcessService;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Docker
 * PTP Sync Project Launcher</b></em>'. <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerPTPSyncProjectLauncherImpl extends DockerProjectLauncherImpl
		implements DockerPTPSyncProjectLauncher {

	private static final String syncServiceId = "org.eclipse.ptp.rdt.sync.git.core.synchronizeService";

	/**
	 * Return the OSGi service with the given service interface.
	 *
	 * @param service
	 *            service interface
	 * @return the specified service or null if it's not registered
	 */
	protected <T> T getService(Class<T> service) {
		BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
		if (context != null) {
			ServiceReference<T> ref = context.getServiceReference(service);
			return ref != null ? context.getService(ref) : null;
		} else {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerPTPSyncProjectLauncherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipseappsPackage.Literals.DOCKER_PTP_SYNC_PROJECT_LAUNCHER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	@SuppressWarnings("restriction")
	public boolean launchProject(SourcePackage proj) {
		
		// Create the IProject in a language appropriate manner
		// FIXME add more than C++
		EclipseCppProjectProviderImpl provider = (EclipseCppProjectProviderImpl) EclipseappsFactory.eINSTANCE
				.createEclipseCppProjectProvider();
		IRemoteConnection connection = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		int port = containerconfiguration.getRemoteSSHPort();
		IRemoteServicesManager remoteManager = getService(IRemoteServicesManager.class);

		this.projectName = proj.getName();
		
		// If valid, continue on an get the IRemoteConnection
		if (remoteManager != null) {

			// Get the connection type - basically Jsch is index 0
			IRemoteConnectionType connectionType = remoteManager.getRemoteConnectionTypes().get(0);
			IRemoteConnectionWorkingCopy workingCopy = null;
			
			try {
				workingCopy = connectionType.newConnection("Docker Host - localhost:" + port);
			} catch (RemoteConnectionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}

			// Set up the Host Service
			IRemoteConnectionHostService service = workingCopy.getService(IRemoteConnectionHostService.class);
			service.setUsePassword(false);
			service.setHostname("localhost");
//			service.setPassword("password");
			service.setUsername(System.getProperty("user.name"));
			service.setPort(port);

			// Create the new IRemoteConnection
			try {
				connection = workingCopy.save();
				connection.open(new NullProgressMonitor());
			} catch (RemoteConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		// Need to mount the containers /usr/include directory
		IProject includesProject = workspace.getRoot().getProject(".env-includes");// provider.getProject();
		IProjectDescription description = workspace.newProjectDescription(".env-includes");
		try {
			if (!includesProject.exists()) {
				includesProject.create(description, null);
			}
			if (!includesProject.isOpen()) {
				includesProject.open(null);
			}
			SyncManager.makeSyncProject(includesProject, connection.getName() + "-env-includes", syncServiceId,
					connection, "/usr/include", null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}

		// Enable syncing
		SyncManager.setSyncMode(includesProject, SyncMode.ACTIVE);

		// Force an initial sync
		try {
			SyncManager.sync(null, includesProject, SyncFlag.BOTH, new CommonSyncExceptionHandler(false, true));
		} catch (CoreException e) {
			// This should never happen because only a blocking sync can throw a
			// core exception.
			e.printStackTrace();
			return false;
		}

		// Create the Project reference for the primary app
		provider.createProject(proj.getName());
		IProject project = provider.getProject();

		// Create the SyncProject
		try {
			SyncManager.makeSyncProject(project, connection.getName(), syncServiceId, connection,
					"/projects/" + proj.getName(), null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}

		// Enable syncing
		SyncManager.setSyncMode(project, SyncMode.ACTIVE);

		// This is a temporary fix for the PTP sync project 
		// bug with git submodules. 
		removeGitSubmoduleGitDirs_TemporaryPTPBugFix(connection);

		Job j = null;
		// Force an initial sync
		try {
			j = SyncManager.sync(null, project, SyncFlag.BOTH, new CommonSyncExceptionHandler(false, true));
		} catch (CoreException e) {
			// This should never happen because only a blocking sync can throw a
			// core exception.
			e.printStackTrace();
			return false;
		}

		// Let the job run before we run the 
		// language provider configure method. 
		while (j.getState() == Job.RUNNING) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		provider.configure();
		
		return true;
	}

	private void removeGitSubmoduleGitDirs_TemporaryPTPBugFix(IRemoteConnection connection) {
		IRemoteProcessService processService = connection.getService(IRemoteProcessService.class);

		String[] cmd = new String[] { "/bin/bash", "-c", "find /projects/" + projectName + " -name '.git'" };
		// Create the process builder for the remote job
		IRemoteProcessBuilder processBuilder = processService.getProcessBuilder(cmd);

		// Do not redirect the streams
		processBuilder.redirectErrorStream(false);

		IRemoteProcess job = null;
		try {
			job = processBuilder.start(IRemoteProcessBuilder.NONE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream stdOutStream = job.getInputStream();
		InputStream stdErrStream = job.getErrorStream();
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(stdOutStream, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] splitNewLine = writer.toString().split("\n");
		for (String s : splitNewLine) {
			if (!s.equals("/projects/"+projectName+"/.git")) {
				String[] rmcmd = new String[] { "/bin/bash", "-c", "rm -rf " + s };
				// Create the process builder for the remote job
				IRemoteProcessBuilder rmProcessBuilder = processService.getProcessBuilder(rmcmd);

				// Do not redirect the streams
				rmProcessBuilder.redirectErrorStream(false);

				IRemoteProcess rmJob = null;
				try {
					rmJob = rmProcessBuilder.start(IRemoteProcessBuilder.NONE);
				} catch (IOException e) {
					e.printStackTrace();
				}

				InputStream rmStdOutStream = rmJob.getInputStream();
				InputStream rmStdErrStream = rmJob.getErrorStream();
				StringWriter rmWriter = new StringWriter();
				try {
					IOUtils.copy(rmStdErrStream, rmWriter);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void updateConnection(int port) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		SyncConfig config = SyncConfigManager
				.getActive(project);
		try {
			IRemoteConnection conn = config.getRemoteConnection();
			IRemoteConnectionWorkingCopy copy = conn.getWorkingCopy();
			IRemoteConnectionHostService hostService = copy.getService(IRemoteConnectionHostService.class);
			hostService.setPort(port);
			hostService.setUsePassword(false);
			hostService.setHostname("localhost");
//			hostService.setPassword("password");
			hostService.setUsername(System.getProperty("user.name"));
			copy.setName("Docker Host - localhost:" + port);
			conn = copy.save();
			config.setConnection(conn);
			config.setConfigName(conn.getName());
			config.setConnectionName(conn.getName());
			SyncConfigManager.setActive(project, config);
		} catch (MissingConnectionException | RemoteConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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

} // DockerPTPSyncProjectLauncherImpl
