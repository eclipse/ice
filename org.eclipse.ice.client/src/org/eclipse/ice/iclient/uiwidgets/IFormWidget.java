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
 * <p>The IFormWidget interface describes the operations that ICE expects from a Widget that can display Forms.</p>
 * @author Jay Jay Billings
 */
public interface IFormWidget extends IObservableWidget {
	/** 
	 * <p>This operation sets the Form that should be displayed by the Widget.</p>
	 * @param form <p>The Form that should be used by the Widget.</p>
	 */
	public void setForm(Form form);

	/** 
	 * <p>This operation retrieves the Form from the IFormWidget and should be used whenever an update is dispatched from the Widget to a Listener.</p>
	 * @return <p>The Form from the Widget.</p>
	 */
	public Form getForm();

	/** 
	 * <p>This operation directs the IFormWidget to display its Form. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 */
	public void display();

	/** 
	 * <p>This operation posts a status message to the IFormWidget that should be displayed to the user or system viewing the widget. It is a simple string.</p>
	 * @param statusMessage <p>The status message.</p>
	 */
	public void updateStatus(String statusMessage);

	/** 
	 * <p>This operation disables the Form widget. Disabled FormWidgets will not make it possible for clients to process the Form. Any buttons or facilities that enable this should be disabled.</p>
	 * @param state <p>True if the widget is disabled, false if not.</p>
	 */
	public void disable(boolean state);
}