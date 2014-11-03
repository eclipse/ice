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
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.jface.wizard.IWizard;

/**
 * This interface is used by implementing classes to provide an IWizard
 * instance.
 * 
 * @author tnp
 */
public interface IWizardProvider {

	/**
	 * The operation retrieves an instance of an IWizard for the calling class.
	 * 
	 * @param obj
	 *            The Object that the Wizard will edit
	 * @return An IWizard instance
	 */
	public IWizard getWizard(Object obj);

}
