/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.widgets.IFormWidgetBuilder;
import org.eclipse.ice.iclient.uiwidgets.IFormWidget;

/**
 * This class provides the declarative service that registers the MOOSE
 * FormEditor Widget with ICE's service for dynamically extending the widget
 * factory. This lets us tailor the standard ICEFormEditor behavior for MOOSE
 * Model Builders.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class MOOSEFormWidgetBuilder implements IFormWidgetBuilder {

	/**
	 * The name of the items/forms used to create {@link MOOSEFormEditor}s. This
	 * should be the same as the name in the MOOSEModelBuilder item class.
	 */
	public static final String name = "MOOSE Model Builder";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.IFormWidgetBuilder#getTargetFormName
	 * ()
	 */
	public String getTargetFormName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.IFormWidgetBuilder#build()
	 */
	public IFormWidget build() {
		return new MOOSEEclipseFormWidget();
	}

}
