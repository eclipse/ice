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
package org.eclipse.eavp.viz.service.geometry.widgets;

import org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeContentProvider.BlankShape;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * <p>
 * Provides the name and icon for shape items in the shape TreeViewer
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ShapeTreeLabelProvider extends LabelProvider {
	/**
	 * <p>
	 * Returns the image associated with the given element object
	 * </p>
	 * 
	 * @param element
	 *            <p>
	 *            An IShape to produce its image
	 *            </p>
	 * @return
	 * 		<p>
	 *         The icon associated with the given IShape element
	 *         </p>
	 */
	@Override
	public Image getImage(Object element) {

		// Don't display an image beside the shape

		return null;

	}

	/**
	 * <p>
	 * Returns the name associated with the given element object
	 * </p>
	 * 
	 * @param element
	 *            <p>
	 *            The ICEObject or AbstractShape to produce its text
	 *            </p>
	 * @return
	 * 		<p>
	 *         The name associated with the element's ICEObject properties
	 *         </p>
	 */
	@Override
	public String getText(Object element) {

		// Check that the element is an ICEObject and is not null

		if (element instanceof BasicController) {

			// Return the ICEObject's name property with its ICEObject ID
			// appended with a space separator

			IController iceElement = (IController) element;
			return iceElement.getProperty(MeshProperty.NAME) + " "
					+ iceElement.getProperty(MeshProperty.ID);
		}

		else if (element instanceof ShapeTreeContentProvider.BlankShape) {

			// Return the BlankShape default label text

			return BlankShape.TEXT;
		}

		else {
			return null;
		}

	}
}