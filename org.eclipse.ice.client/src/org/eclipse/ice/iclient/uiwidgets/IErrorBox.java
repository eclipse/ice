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

/** 
 * <p>The IErrorBox interface describes the operations that ICE expects from a Widget that can report errors.</p>
 * @author Jay Jay Billings
 */
public interface IErrorBox {
	/** 
	 * <p>This operation sets the error string that will be displayed by the ErrorBoxWidget.</p>
	 * @param error <p>A string containing the error message that should be displayed.</p>
	 */
	public void setErrorString(String error);

	/** 
	 * <p>This operation retrieves the error string that is currently stored in the ErrorBoxWidget.</p>
	 * @return <p>The error string.</p>
	 */
	public String getErrorString();

	/** 
	 * <p>This operation directs the IErrorBox to display its message. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 */
	public void display();
}