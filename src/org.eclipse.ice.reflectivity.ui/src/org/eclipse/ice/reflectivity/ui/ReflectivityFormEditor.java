/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.reflectivity.ui;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.viz.service.IVizServiceFactory;

/**
 * The custom form editor for the reflectivity model. Should be used instead of
 * {@link ICEFormEditor} to display reflectivity models.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityFormEditor extends ICEFormEditor {

	/**
	 * ID for Eclipse, used for the bundle's editor extension point.
	 */
	public static final String ID = "org.eclipse.ice.reflectivity.ui.ReflectivityFormEditor";

	@Override
	protected void addPages() {
		// TODO implement this to customize our reflectivity view!!!
	}
}
