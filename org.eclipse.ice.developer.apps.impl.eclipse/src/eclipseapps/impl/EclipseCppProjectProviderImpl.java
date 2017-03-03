/**
 */
package eclipseapps.impl;

import eclipseapps.EclipseCppProjectProvider;
import eclipseapps.EclipseappsPackage;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.cdt.managedbuilder.ui.wizards.CfgHolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Cpp Project Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EclipseCppProjectProviderImpl extends MinimalEObjectImpl.Container implements EclipseCppProjectProvider {
	private IProject project;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EclipseCppProjectProviderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipseappsPackage.Literals.ECLIPSE_CPP_PROJECT_PROVIDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void createProject(String projectName) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IProjectDescription description = workspace.newProjectDescription(projectName);
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
	}

	public IProject getProject() {
		return project;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EclipseappsPackage.ECLIPSE_CPP_PROJECT_PROVIDER___CREATE_PROJECT__STRING:
				createProject((String)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //EclipseCppProjectProviderImpl
