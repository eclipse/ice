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

import org.eclipse.january.form.ICEResource;

/** 
 * <p>The IErrorBox interface describes the operations that ICE expects from a Widget that can display a text editor to a user.</p>
 * @author Jay Jay Billings
 */
public interface ITextEditor {
	/** 
	 * <p>This operation sets the ICEResource that editor should display.</p>
	 * @param resource <p>The ICEResource</p>
	 */
	public void setResource(ICEResource resource);

	/** 
	 * <p>This operation retrieves the ICEResource that editor is displaying or null if it has not been set.</p>
	 * @return <p>The ICEResource</p>
	 */
	public ICEResource getResource();

	/** 
	 * <p>This operation directs the ITextEditor to display its text. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 */
	public void display();
}