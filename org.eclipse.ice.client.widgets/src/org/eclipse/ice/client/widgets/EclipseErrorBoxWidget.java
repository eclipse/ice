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
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.iclient.uiwidgets.IErrorBox;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * This class implements IErrorBox interface to display an error box using
 * elements of SWT/JFace and the Eclipse Rich Client Platform.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseErrorBoxWidget implements IErrorBox {

	/**
	 * <p>
	 * The error string.
	 * </p>
	 */
	protected String errorString;

	/**
	 * (non-Javadoc)
	 * 
	 * @see IErrorBox#setErrorString(String error)
	 */
	@Override
	public void setErrorString(String error) {
		errorString = error;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IErrorBox#getErrorString()
	 */
	@Override
	public String getErrorString() {
		return errorString;
	}

	/**
	 * This operation displays an error message on the screen. It launches a
	 * JFace message dialog.
	 */
	@Override
	public void display() {

		IWorkbench workbench = PlatformUI.getWorkbench();

		// If the error has been set, throw up an error box with the message
		if (errorString != null) {
			MessageDialog.openError(
					workbench.getActiveWorkbenchWindow().getShell(),
					"ICE has encountered an error!", errorString);
		} else {
			// Otherwise throw up a generic error box
			MessageDialog.openError(
					workbench.getActiveWorkbenchWindow().getShell(),
					"ICE has encountered an error!",
					"Unable to process your request!");
		}

		return;
	}

}