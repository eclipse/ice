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
package org.eclipse.ice.projectgeneration.templates;

import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

/**
 * Helper class to create the new ICE item project wizard pages
 * 
 * @author arbennett 
 */
public class ICEItemWizard extends NewPluginTemplateWizard {

	private String projectName;
	
	public ICEItemWizard() {
		super();
	}
	
	@Override
	public void init(IFieldData data) {
		super.init(data);
		projectName = data.getName();
	}
	
	@Override
	public ITemplateSection[] createTemplateSections() {
		return new ITemplateSection[] {new ICEItemTemplate(projectName)};
	}

}
