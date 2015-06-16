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
package org.eclipse.ice.reactor.perspective;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;

import org.eclipse.jface.wizard.Wizard;

/**
 * This class creates wizards to generate and add reactor components to a .h5
 * file in the Reactor Viewer. The content of the wizard is determined by the
 * type of selection input to the constructor.
 * 
 * @author Taylor Patterson
 */
public class AddReactorPartWizard extends Wizard {

	/**
	 * The constructor
	 * 
	 * @param selectedElement
	 */
	public AddReactorPartWizard(Object selectedElement) {

		// Call Wizard's constructor
		super();

		// Get the selection type. If the selection is an ICEResource, it is a
		// .h5 file, and we can simply add the appropriate page to the wizard.
		// If the selection is an IReactorComponent, use the visitor to discover
		// its type and add the appropriate page to the wizard.
		if (selectedElement instanceof ICEResource) {
			// Add page for adding reactors to .h5 files
			addPage(new AddReactorPartWizardPage((ICEResource) selectedElement));
		} else if (selectedElement instanceof IReactorComponent) {
			// Add page for adding to an IReactorComponent
			addPage(new AddReactorPartWizardPage(
					(IReactorComponent) selectedElement));
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
