/*******************************************************************************
* Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.iclient.uiwidgets;

import org.eclipse.ice.datastructures.form.Form;

/** 
 * <p>Implementations of the IUpdateEventListener are registered with a FormWidget and notified when the Form is updated.</p>
 * @author Jay Jay Billings
 */
public interface IUpdateEventListener {
	/** 
	 * <p>This operation notifies the listener that the included Form has been updated.</p>
	 * @param form <p>The widget that was changed.</p>
	 */
	public void formUpdated(Form form);
}