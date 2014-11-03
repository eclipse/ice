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
package org.eclipse.ice.client.common;

import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Jay Jay Billings This class is a LabelProvider for the
 *         TreeCompositeViewer in ICE.
 */
public class TreeCompositeLabelProvider extends LabelProvider {

	/**
	 * This operation returns the text label for the TreeComposite or Component
	 * represented by element.
	 */
	@Override
	public String getText(Object element) {
		return ((Identifiable) element).getName();
	}

}
