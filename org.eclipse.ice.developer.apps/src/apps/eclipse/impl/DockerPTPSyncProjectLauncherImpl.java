/**
 */
package apps.eclipse.impl;

import apps.SourcePackage;
import apps.docker.ContainerConfiguration;
import apps.docker.impl.DockerProjectLauncherImpl;

import apps.eclipse.DockerPTPSyncProjectLauncher;
import apps.eclipse.EclipsePackage;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CSourceEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICFolderDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.ICSourceEntry;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.make.core.IMakeCommonBuildInfo;
import org.eclipse.cdt.make.core.IMakeTarget;
import org.eclipse.cdt.make.core.IMakeTargetManager;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.cdt.managedbuilder.ui.wizards.CfgHolder;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ptp.internal.rdt.sync.git.ui.GitParticipant;
import org.eclipse.ptp.internal.rdt.sync.ui.SynchronizeParticipantRegistry;
import org.eclipse.ptp.internal.rdt.sync.ui.handlers.CommonSyncExceptionHandler;
import org.eclipse.ptp.rdt.sync.core.SyncFlag;
import org.eclipse.ptp.rdt.sync.core.SyncManager;
import org.eclipse.ptp.rdt.sync.core.SyncManager.SyncMode;
import org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant;
import org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipantDescriptor;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
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
		return EclipsePackage.Literals.DOCKER_PTP_SYNC_PROJECT_LAUNCHER;
	}

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
	 */
	@SuppressWarnings("restriction")
	@Override
	public void launchProject(SourcePackage proj) {
		System.out.println("Launching PTP Project for " + proj.getName());

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(proj.getName());
		IProjectDescription description = workspace.newProjectDescription(proj.getName());
		String os = System.getProperty("os.name");

		try {
			// Create the CDT Project
			CCorePlugin.getDefault().createCDTProject(description, project, new NullProgressMonitor());

			// Add the CPP nature
			CCProjectNature.addCCNature(project, new NullProgressMonitor());

			// Set up build information
			ICProjectDescriptionManager pdMgr = CoreModel.getDefault().getProjectDescriptionManager();
			ICProjectDescription projDesc = pdMgr.createProjectDescription(project, false);
			ManagedBuildInfo info = ManagedBuildManager.createBuildInfo(project);
			ManagedProject mProj = new ManagedProject(projDesc);
			info.setManagedProject(mProj);

			// Grab the correct toolchain
			// FIXME this should be better...
			IToolChain toolChain = null;
			for (IToolChain tool : ManagedBuildManager.getRealToolChains()) {
				if (os.contains("Mac") && tool.getName().contains("Mac") && tool.getName().contains("GCC")) {
					toolChain = tool;
					break;
				} else if (os.contains("Linux") && tool.getName().contains("Linux")
						&& tool.getName().contains("GCC")) {
					toolChain = tool;
					break;
				} else if (os.contains("Windows") && tool.getName().contains("Cygwin")) {
					toolChain = tool;
					break;
				} else {
					toolChain = null;
				}
			}

			// Set up the Build configuratino
			CfgHolder cfgHolder = new CfgHolder(toolChain, null);
			String s = toolChain == null ? "0" : toolChain.getId(); //$NON-NLS-1$
			IConfiguration config = new Configuration(mProj,
					(org.eclipse.cdt.managedbuilder.internal.core.ToolChain) toolChain,
					ManagedBuildManager.calculateChildId(s, null), cfgHolder.getName());
			IBuilder builder = config.getEditableBuilder();
			builder.setManagedBuildOn(false);
			CConfigurationData data = config.getConfigurationData();
			projDesc.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);
			pdMgr.setProjectDescription(project, projDesc);

			// Set the include and src folders as actual CDT source
			// folders
//			ICProjectDescription cDescription = CoreModel.getDefault().getProjectDescriptionManager()
//					.createProjectDescription(cProject, false);
//			ICConfigurationDescription cConfigDescription = cDescription
//					.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, config.getConfigurationData());
//			cDescription.setActiveConfiguration(cConfigDescription);
//			cConfigDescription.setSourceEntries(null);
//			IFolder frameworkFolder = cProject.getFolder("framework");
//			IFolder srcFolder = frameworkFolder.getFolder("src");
//			IFolder includeFolder = frameworkFolder.getFolder("include");
//			ICSourceEntry srcFolderEntry = new CSourceEntry(srcFolder, null, ICSettingEntry.RESOLVED);
//			ICSourceEntry includeFolderEntry = new CSourceEntry(includeFolder, null, ICSettingEntry.RESOLVED);
//			cConfigDescription.setSourceEntries(new ICSourceEntry[] { srcFolderEntry, includeFolderEntry });
//
//			ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(cProject, true);
//			ICConfigurationDescription configDecriptions[] = projectDescription.getConfigurations();
//			for (ICConfigurationDescription configDescription : configDecriptions) {
//				ICFolderDescription projectRoot = configDescription.getRootFolderDescription();
//				ICLanguageSetting[] settings = projectRoot.getLanguageSettings();
//				for (ICLanguageSetting setting : settings) {
//					List<ICLanguageSettingEntry> includes = getIncludePaths(project);
//					includes.addAll(setting.getSettingEntriesList(ICSettingEntry.INCLUDE_PATH));
//					setting.setSettingEntries(ICSettingEntry.INCLUDE_PATH, includes);
//				}
//			}
//			CoreModel.getDefault().setProjectDescription(cProject, projectDescription);
			
		} catch (CoreException e) {
			e.printStackTrace();
		}

		ISynchronizeParticipantDescriptor[] providers = SynchronizeParticipantRegistry.getDescriptors();
		DockerSyncParticipant syncPart = null;
		try {
			syncPart = new DockerSyncParticipant(providers[0], containerconfiguration, proj);
		} catch (RemoteConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			SyncManager.makeSyncProject(project, syncPart.getSyncConfigName(), syncPart.getServiceId(),
					syncPart.getConnection(), syncPart.getLocation(), null);
		} catch (CoreException e) {
			e.printStackTrace();
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
		}
	}
	
} //DockerPTPSyncProjectLauncherImpl
