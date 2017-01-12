/*******************************************************************************
* Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.tests.client;

import org.eclipse.ice.iclient.uiwidgets.IErrorBox;

/** 
 * <p>The FakeErrorBoxWidget is a realization of IErrorBox that is used for testing. It provides several methods in addition to the IErrorBox interface that are used for testing and inspection.</p>
 * @author Jay Jay Billings
 */
public class FakeErrorBoxWidget implements IErrorBox {
	/** 
	 * <p>Boolean to signify if a listener was registered.</p>
	 */
	private boolean observed;
	/** 
	 * <p>Boolean to store the display state.</p>
	 */
	private boolean displayed;

	/** 
	 * <p>The error message.</p>
	 */
	private String errorMsg = null;

	/** 
	 * <p>This operation returns true if the display operation is called for the FakeErrorBoxWidget.</p>
	 * @return <p>True if the widget was displayed, false if not.</p>
	 */
	public boolean widgetDisplayed() {
		return this.displayed;
	}

	/** 
	 * <p>This operation implements display() from UIWidget with a simple pass through that makes whether or not the method was called. Nothing is drawn on the screen.</p>
	 */
	@Override
	public void display() {

		this.displayed = true;

		return;

	}

	/** 
	 * (non-Javadoc)
	 * @see IErrorBox#setErrorString(String error)
	 */
	@Override
	public void setErrorString(String error) {

		// Set the error message
		errorMsg = error;

		return;

	}

	/** 
	 * (non-Javadoc)
	 * @see IErrorBox#getErrorString()
	 */
	@Override
	public String getErrorString() {

		// Return the error message
		return errorMsg;

	}

}