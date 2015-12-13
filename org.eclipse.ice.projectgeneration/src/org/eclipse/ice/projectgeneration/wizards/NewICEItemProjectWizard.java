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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.projectgeneration.NewICEItemProjectSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.wizards.WizardElement;
import org.eclipse.pde.internal.ui.wizards.plugin.NewPluginProjectWizard;
import org.eclipse.pde.ui.templates.NewPluginProjectFromTemplateWizard;
import org.eclipse.ui.IWorkbench;

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

	private IStructuredSelection selection;
	private IWorkbench workbench;

	/**
	 * Constructor
	 */
	public NewICEItemProjectWizard() {
		setWindowTitle(WIZARD_TITLE);
	}

	@Override
	protected String getTemplateID() {
		String retval = null;
		// TODO Auto-generated method stub
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
					System.out.println(elements[j].getAttribute(WizardElement.ATT_ID));
					retval = (elements[j].getAttribute(WizardElement.ATT_ID));
				}
			}
		}
		return "org.eclipse.ice.projectgeneration.templates.ICEItemTemplate";
	}
}
