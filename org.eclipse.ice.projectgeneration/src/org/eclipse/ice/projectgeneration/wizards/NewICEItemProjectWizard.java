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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.projectgeneration.NewICEItemProjectSupport;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.WizardElement;
import org.eclipse.pde.internal.ui.wizards.plugin.AbstractFieldData;
import org.eclipse.pde.internal.ui.wizards.plugin.NewPluginProjectWizard;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationFromTemplatePage;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationOperation;
import org.eclipse.pde.internal.ui.wizards.plugin.NewProjectCreationPage;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginContentPage;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.eclipse.pde.ui.IPluginContentWizard;
import org.eclipse.pde.ui.templates.NewPluginProjectFromTemplateWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

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
	private static final String WIZARD_NAME = "New ICE Item Project";
	private static final String WIZARD_TITLE = "Create a new ICE item project";

	private AbstractFieldData fPluginData;
	private NewProjectCreationPage fProjectPage;
	private PluginContentPage fContentPage;
	private IPluginContentWizard fTemplateWizard;
	private IProjectProvider fProjectProvider;
	private IConfigurationElement fConfig;
	
	private IStructuredSelection selection;
	private IWorkbench workbench;

	/**
	 * Constructor
	 */
	public NewICEItemProjectWizard() {
		setNeedsProgressMonitor(true);
		fPluginData = new PluginFieldData();
		setWindowTitle(WIZARD_TITLE);
	}

		@Override
	public void addPages() {
		WizardElement templateWizardElement = getTemplateWizard();

		System.out.println(fPluginData.toString());
		fProjectPage = new NewProjectCreationFromTemplatePage("main", fPluginData, getSelection(), templateWizardElement); //$NON-NLS-1$
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
			fTemplateWizard = (IPluginContentWizard) templateWizardElement.createExecutableExtension();
			fTemplateWizard.init(fPluginData);
			fTemplateWizard.addPages();
			IWizardPage[] pages = fTemplateWizard.getPages();
			for (int i = 0; i < pages.length; i++) {
				addPage(pages[i]);
			}
		} catch (CoreException e) {
			System.out.println("adding wizard pages fails!");
		}
	}

	@Override
	public boolean performFinish() {
		try {
			fProjectPage.updateData();
			fContentPage.updateData();
			IDialogSettings settings = getDialogSettings();
			if (settings != null) {
				fProjectPage.saveSettings(settings);
				fContentPage.saveSettings(settings);
			}
			BasicNewProjectResourceWizard.updatePerspective(fConfig);

			// If the PDE models are not initialized, initialize with option to cancel
			if (!PDECore.getDefault().areModelsInitialized()) {
				try {
					getContainer().run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
							// Target reloaded method clears existing models (which don't exist currently) and inits them with a progress monitor
							PDECore.getDefault().getModelManager().targetReloaded(monitor);
							if (monitor.isCanceled()) {
								throw new InterruptedException();
							}
						}
					});
				} catch (InterruptedException e) {
					// Model initialization cancelled
					return false;
				}
			}

			getContainer().run(false, true, new NewProjectCreationOperation(fPluginData, fProjectProvider, fTemplateWizard));

			IWorkingSet[] workingSets = fProjectPage.getSelectedWorkingSets();
			if (workingSets.length > 0)
				getWorkbench().getWorkingSetManager().addToWorkingSets(fProjectProvider.getProject(), workingSets);

			return true;
		} catch (InvocationTargetException e) {
			PDEPlugin.logException(e);
		} catch (InterruptedException e) {
		}
		return false;
	}

	
	@Override
	protected String getTemplateID() {
		return "org.eclipse.ice.projectgeneration.pluginContent.ICEItem";
	}
	
	private WizardElement getTemplateWizard() {
		String templateID = getTemplateID();
		if (templateID == null) {
			return null;
		}

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("org.eclipse.ice.projectgeneration", PLUGIN_POINT);
		if (point == null) {
			return null;
		
		}
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				if (elements[j].getName().equals(TAG_WIZARD)) {
					if (templateID.equals(elements[j].getAttribute(WizardElement.ATT_ID))) {
						return WizardElement.create(elements[j]);
					}
				}
			}
		}
		return null;
	}
}
