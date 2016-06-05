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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.january.form.ICEResource;

/** 
 * <p>The FakeTextEditor is a realization of ITextEditor that is used for testing. It provides several methods in addition to the ITextEditor interface that are used for testing and introspection.</p>
 * @author Jay Jay Billings
 */
public class FakeTextEditor implements ITextEditor {
	/** 
	 * <p>Boolean to store the display state.</p>
	 */
	private boolean displayed;
	/** 
	 * <p>The ICEResource that is ostensibly managed by the FakeTextEditor.</p>
	 */
	private ICEResource iceResource;

	/** 
	 * <p>This operation returns true if the display operation is called for the FakeTextEditor.</p>
	 * @return
	 */
	public boolean widgetDisplayed() {
		return displayed;
	}

	/** 
	 * (non-Javadoc)
	 * @see ITextEditor#setResource(ICEResource resource)
	 */
	@Override
	public void setResource(ICEResource resource) {

		iceResource = resource;

		return;

	}

	/** 
	 * (non-Javadoc)
	 * @see ITextEditor#getResource()
	 */
	@Override
	public ICEResource getResource() {
		return iceResource;
	}

	/** 
	 * (non-Javadoc)
	 * @see ITextEditor#display()
	 */
	@Override
	public void display() {

		// Display if and only if the ICEResource is not equal to null
		if (iceResource != null) {
			displayed = true;
		}

	}
}