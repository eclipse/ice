/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.projectgeneration.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardCopyOption.*;
import java.util.Arrays;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.WizardElement;
import org.eclipse.pde.internal.ui.wizards.plugin.AbstractFieldData;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationFromTemplatePage;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationOperation;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationPage;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginContentPage;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.eclipse.pde.ui.templates.NewPluginProjectFromTemplateWizard;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ice.projectgeneration.ICEItemNature;
import org.eclipse.ice.projectgeneration.templates.ICEItemWizard;

/**
 * This class defines the steps for creating a new New ICE Item project via the
 * wizard that us accessible via: 'File -> New... -> Other -> New ICE Item
 * Project'
 * 
 * @author arbennett
 */
@SuppressWarnings("restriction")
public class NewICEItemProjectWizard extends NewPluginProjectFromTemplateWizard {

	private static final String DESCRIPTION = "Create a new ICE item project.";
	private static final String WIZARD_TITLE = "Create a new ICE item project";
	private static final String TEMPLATE_ID = "org.eclipse.ice.projectgeneration.pluginContent.ICEItem";

	private AbstractFieldData fPluginData;
	private NewProjectCreationPage fProjectPage;
	private PluginContentPage fContentPage;
	private ICEItemWizard fTemplateWizard;
	private IProjectProvider fProjectProvider;
	private IConfigurationElement fConfig;

	/**
	 * Constructor
	 */
	public NewICEItemProjectWizard() {
		setNeedsProgressMonitor(true);
		fPluginData = new PluginFieldData();
		setWindowTitle(WIZARD_TITLE);
	}

	/**
	 * Creates and adds the pages to the wizard. Three pages are created.
	 */
	@Override
	public void addPages() {
		WizardElement templateWizardElement = getTemplateWizard();

		fProjectPage = new NewProjectCreationFromTemplatePage("main", fPluginData, getSelection(), //$NON-NLS-1$
				templateWizardElement);
		fProjectPage.setTitle(WIZARD_TITLE);
		fProjectPage.setDescription(DESCRIPTION);

		String projectName = getDefaultValue(DEF_PROJECT_NAME);
		if (projectName != null)
			fProjectPage.setInitialProjectName(projectName);
		addPage(fProjectPage);

		fProjectProvider = new IProjectProvider() {
			public String getProjectName() {
				return fProjectPage.getProjectName();
			}

			public IProject getProject() {
				return fProjectPage.getProjectHandle();
			}

			public IPath getLocationPath() {
				return fProjectPage.getLocationPath();
			}
		};

		fContentPage = new PluginContentPage("page2", fProjectProvider, fProjectPage, fPluginData); //$NON-NLS-1$
		addPage(fContentPage);
		try {
			fTemplateWizard = (ICEItemWizard) templateWizardElement.createExecutableExtension();
			fTemplateWizard.init(fPluginData);
			fTemplateWizard.addPages();
			IWizardPage[] pages = fTemplateWizard.getPages();
			for (int i = 0; i < pages.length; i++) {
				addPage(pages[i]);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			System.out.println("adding wizard pages fails!");
		}
	}

	/**
	 * Takes all of the information from the wizard pages and uses it to create
	 * the plugin and java classes.
	 * 
	 * @return whether the project generation was successful
	 */
	@Override
	public boolean performFinish() {
		boolean successful = false;
		try {
			fProjectPage.updateData();
			fContentPage.updateData();
			IDialogSettings settings = getDialogSettings();
			if (settings != null) {
				fProjectPage.saveSettings(settings);
				fContentPage.saveSettings(settings);
			}
			fTemplateWizard.setTemplateExtension(fProjectPage.getProjectName());
			BasicNewProjectResourceWizard.updatePerspective(fConfig);

			// If the PDE models are not initialized, initialize with option to
			// cancel
			if (!PDECore.getDefault().areModelsInitialized()) {
				try {
					getContainer().run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {
							PDECore.getDefault().getModelManager().targetReloaded(monitor);
							if (monitor.isCanceled()) {
								throw new InterruptedException();
							}
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			getContainer().run(false, true,
					new NewProjectCreationOperation(fPluginData, fProjectProvider, fTemplateWizard));

			IWorkingSet[] workingSets = fProjectPage.getSelectedWorkingSets();
			if (workingSets.length > 0)
				getWorkbench().getWorkingSetManager().addToWorkingSets(fProjectProvider.getProject(), workingSets);
			setNature(fProjectProvider.getProject());
			setPackageLayout();
			updateManifest();
			fProjectProvider.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			successful = true;
		} catch (InvocationTargetException e) {
			PDEPlugin.logException(e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return successful;
	}

	/**
	 * Make sure that the project has the ICEItemNature associated with it.
	 * 
	 * @param project
	 */
	public static void setNature(IProject project) throws CoreException {
		if (!project.hasNature(ICEItemNature.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] projNatures = description.getNatureIds();
			projNatures = Arrays.copyOf(projNatures, projNatures.length + 1);
			projNatures[projNatures.length - 1] = ICEItemNature.NATURE_ID;
			description.setNatureIds(projNatures);
			project.setDescription(description, new NullProgressMonitor());
		}
	}

	/**
	 * Creates a new wizard element for the definition of the java code
	 * templates
	 * 
	 * @return the wizard element corresponding to the project's template
	 */
	private WizardElement getTemplateWizard() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("org.eclipse.ice.projectgeneration", PLUGIN_POINT);
		if (point == null)
			return null;
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				if (elements[j].getName().equals(TAG_WIZARD)) {
					if (TEMPLATE_ID.equals(elements[j].getAttribute(WizardElement.ATT_ID))) {
						return WizardElement.create(elements[j]);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Sets the package layout to be as specified in the wizard.
	 */
	private void setPackageLayout() {
		String sep = System.getProperty("file.separator");
		String[] packageHierarchy = fProjectProvider.getProjectName().split("\\.");
		String projectSrcPath = fProjectProvider.getLocationPath().makeAbsolute().toOSString() + sep
				+ fProjectProvider.getProjectName() + sep + fPluginData.getSourceFolderName();
		File[] projectSrcs = new File(projectSrcPath).listFiles();
		File modelDir = new File(projectSrcPath + sep + String.join(sep, packageHierarchy) + sep + "model");
		File launcherDir = new File(projectSrcPath + sep + String.join(sep, packageHierarchy) + sep + "launcher");
		try {
			modelDir.mkdirs();
			launcherDir.mkdirs();
			for (File f : projectSrcs) {
				if (f.getName().endsWith("Launcher.java") || f.getName().endsWith("LauncherBuilder.java")) {
					Files.move(f.toPath(), (new File(launcherDir.getAbsolutePath() + sep + f.getName())).toPath(),
							REPLACE_EXISTING);
				} else if (f.getName().endsWith("Model.java") || f.getName().endsWith("ModelBuilder.java")) {
					Files.move(f.toPath(), (new File(modelDir.getAbsolutePath() + sep + f.getName())).toPath(),
							REPLACE_EXISTING);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * TODO: Description
	 */
	private void updateManifest() {
		String packageBase = fProjectProvider.getProjectName();
		String sep = System.getProperty("file.separator");
		String manifestFile = fProjectProvider.getLocationPath().makeAbsolute().toOSString() + sep
				+ fProjectProvider.getProjectName() + sep + "META-INF" + sep + "MANIFEST.MF";
		StringBuilder manifestLines = new StringBuilder();
		manifestLines.append("Import-Package: org.eclipse.ice.datastructures.form,\n");
		manifestLines.append(" org.eclipse.ice.io.serializable,\n");
		manifestLines.append(" org.eclipse.ice.item,\n");
		manifestLines.append(" org.eclipse.ice.item.jobLauncher,\n");
		manifestLines.append(" org.eclipse.ice.item.model,\n");
		manifestLines.append(" org.eclipse.ice.datastructures.ICEObject,\n");
		manifestLines.append(" org.eclipse.core.resources,\n");
		manifestLines.append(" org.eclipse.core.runtime,\n");
		manifestLines.append(" org.slf4j\n");
		manifestLines.append("Export-Package: " + packageBase + ".model,\n");
		manifestLines.append(" " + packageBase + ".launcher\n");
		
		try {
			Files.write(Paths.get(manifestFile), manifestLines.toString().getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getTemplateID() {
		return TEMPLATE_ID;
	}
}
