/*******************************************************************************
* Copyright (c) 2013, 2014 UT-Battelle, LLC.
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

import org.eclipse.ice.iclient.uiwidgets.IFormWidget;

/** 
 * <p>This interface can be realized by client developers to customize the way that the EclipseUIWidgetFactory renders Forms for the user. Realizations of this interface should be registered as an OSGi Declarative service.</p><p>IFormWidgetBuilders must provide the name of the Form that they are meant to render. This requires that a Form with that name exist for the IFormWidget that the Builder creates to work properly. Thus, if no Form with the target name does not exist, the Builder should never be called.</p>
 * @author Jay Jay Billings
 */
public interface IFormWidgetBuilder {
	/** 
	 * <p>This operation returns the name of the target Form that the Builder will render.</p>
	 * @return <p>The name of the Form.</p>
	 */
	public String getTargetFormName();

	/** 
	 * <p>This operation builds the IFormWidget.</p>
	 * @return <p>The specialized IFormWidget from the service.</p>
	 */
	public IFormWidget build();
}