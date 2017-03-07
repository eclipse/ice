/**
 */
package eclipseapps.impl;

import eclipseapps.EclipseCppProjectProvider;
import eclipseapps.EclipseappsPackage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
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
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.cdt.managedbuilder.ui.wizards.CfgHolder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Eclipse
 * Cpp Project Provider</b></em>'. <!-- end-user-doc -->
 *
 * @generated
 */
public class EclipseCppProjectProviderImpl extends MinimalEObjectImpl.Container implements EclipseCppProjectProvider {
	private IProject project;
	private IConfiguration config;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EclipseCppProjectProviderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipseappsPackage.Literals.ECLIPSE_CPP_PROJECT_PROVIDER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	@SuppressWarnings("restriction")
	public void createProject(String projectName) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		project = workspace.getRoot().getProject(projectName);
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
				} else if (os.contains("Linux") && tool.getName().contains("Linux") && tool.getName().contains("GCC")) {
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
			config = new Configuration(mProj, (org.eclipse.cdt.managedbuilder.internal.core.ToolChain) toolChain,
					ManagedBuildManager.calculateChildId(s, null), cfgHolder.getName());
			IBuilder builder = config.getEditableBuilder();
			builder.setManagedBuildOn(false);
			CConfigurationData data = config.getConfigurationData();
			projDesc.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);
			pdMgr.setProjectDescription(project, projDesc);

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This private method configures the include paths for the CDT project.
	 * 
	 * @param mooseProject
	 * @return
	 */
	private List<ICLanguageSettingEntry> getIncludePaths(IProject proj) {

		// Create a list of ICLanguageSettingEntry to hold the include paths.
		List<ICLanguageSettingEntry> includes = new ArrayList<ICLanguageSettingEntry>();

		try {
			proj.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		includes.add(new CIncludePathEntry("/.env-includes/", ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/.env-includes/c++/6.3.1", ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/.env-includes/c++/6.3.1/backward", ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/.env-includes/c++/6.3.1/x86_64-redhat-linux",
				ICSettingEntry.VALUE_WORKSPACE_PATH));

		return includes;

	}

	public IProject getProject() {
		return project;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EclipseappsPackage.ECLIPSE_CPP_PROJECT_PROVIDER___CREATE_PROJECT__STRING:
				createProject((String)arguments.get(0));
				return null;
			case EclipseappsPackage.ECLIPSE_CPP_PROJECT_PROVIDER___CONFIGURE:
				configure();
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

	@Override
	public void configure() {
		// TODO Auto-generated method stub
		if (!project.getName().equals(".env-includes")) {
			// Set the include and src folders as actual CDT source
			// folders
			try {
				ICProjectDescription cDescription = CoreModel.getDefault().getProjectDescriptionManager()
						.createProjectDescription(project, false);
				ICConfigurationDescription cConfigDescription = cDescription
						.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, config.getConfigurationData());
				cDescription.setActiveConfiguration(cConfigDescription);
				cConfigDescription.setSourceEntries(null);

				// IFolder topLevelFolder =
				// ResourcesPlugin.getWorkspace().getRoot().getFolder(project.getLocation());
				ArrayList<ICSourceEntry> entries = new ArrayList<>();
				for (IResource r : project.members()) {
					if (r.getType() == IResource.FOLDER) {
						IFolder f = (IFolder) r;
						ICSourceEntry e = new CSourceEntry(f, null, ICSettingEntry.RESOLVED);
						entries.add(e);
					}
				}
				cConfigDescription.setSourceEntries(entries.toArray(new ICSourceEntry[entries.size()]));

				// ICSourceEntry topLevelFolderEntry = new
				// CSourceEntry(topLevelFolder, null,
				// ICSettingEntry.SOURCE_PATH);
				// cConfigDescription.setSourceEntries(new ICSourceEntry[] {
				// topLevelFolderEntry });

				ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(project, true);
				ICConfigurationDescription configDecriptions[] = projectDescription.getConfigurations();
				for (ICConfigurationDescription configDescription : configDecriptions) {
					ICFolderDescription projectRoot = configDescription.getRootFolderDescription();
					ICLanguageSetting[] settings = projectRoot.getLanguageSettings();
					for (ICLanguageSetting setting : settings) {
						List<ICLanguageSettingEntry> includes = getIncludePaths(project);
						includes.addAll(setting.getSettingEntriesList(ICSettingEntry.INCLUDE_PATH));
						setting.setSettingEntries(ICSettingEntry.INCLUDE_PATH, includes);
					}
				}
				CoreModel.getDefault().setProjectDescription(project, projectDescription);

				// Update the language xml settings
				IFile languageSettings = project.getFolder(".settings").getFile("language.settings.xml");
				StringWriter writer = new StringWriter();
				try {
					IOUtils.copy(languageSettings.getContents(), writer, languageSettings.getCharset());
				} catch (IOException | CoreException e1) {
					e1.printStackTrace();
				}
				String contents = writer.toString();
				contents = contents.replaceAll("\\$\\{FLAGS\\}", "\\$\\{FLAGS\\} -std=c++14");

				try {
					languageSettings.setContents(new ByteArrayInputStream(contents.getBytes()), true, false, null);
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

} // EclipseCppProjectProviderImpl
