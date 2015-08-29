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
 * <p>Implementations of the IProcessEventListener are registered with a FormWidget and notified when a process is selected.</p>
 * @author Jay Jay Billings
 */
public interface IProcessEventListener {
	/** 
	 * <p>This operation notifies the listener that the Form has been marked to be processed.</p>
	 * @param form
	 * @param process
	 */
	public void processSelected(Form form, String process);

	/** 
	 * <p>This operation directs the listener to cancel the last process request for the Form. There is no guarantee that it can actually cancel the process (it may finish first!).</p>
	 * @param form <p>The form that was previously processed.</p>
	 * @param process <p>The name of the process that was requested for the previous form.</p>
	 */
	public void cancelRequested(Form form, String process);
}