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

import org.eclipse.january.form.Form;

/**
 * <p>
 * The IExtraInfoWidget interface describes the operations that ICE expects to
 * use when it need to request additional information from a user that was
 * originally requested in an IFormWidget. The content in an IExtraInfoWidget is
 * dynamic and unexpected during runtime (thus the reason it wasn't originally
 * in the IFormWidget), but it will always be of a form consist with the output
 * created during ICore.process() if it needs. @see IClient
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IExtraInfoWidget {
	/**
	 * <p>
	 * This operation directs the IExtraInfoWidget to display. It must be
	 * implemented by subclasses that code to a specific UI API (SWT, Swing).
	 * </p>
	 */
	public void display();

	/**
	 * <p>
	 * This operation sets the Form that should be displayed by the Widget.
	 * </p>
	 * 
	 * @param form
	 *            <p>
	 *            The Form.
	 *            </p>
	 */
	public void setForm(Form form);

	/**
	 * <p>
	 * This operation retrieves the Form from the Widget.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The Form.
	 *         </p>
	 */
	public Form getForm();

	/**
	 * <p>
	 * This operation sets a listener, an IWidgetClosedListener, that is waiting
	 * to be notified with the widget is closed and whether it was closed OK or
	 * if it was cancelled.
	 * </p>
	 * 
	 * @param listener
	 */
	public void setCloseListener(IWidgetClosedListener listener);
}