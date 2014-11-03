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
package org.eclipse.ice.client.widgets.geometry;

import org.eclipse.ice.client.widgets.geometry.ShapeTreeContentProvider.BlankShape;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Provides the name and icon for shape items in the shape TreeViewer
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeTreeLabelProvider extends LabelProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the image associated with the given element object
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param element
	 *            <p>
	 *            An IShape to produce its image
	 *            </p>
	 * @return <p>
	 *         The icon associated with the given IShape element
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Image getImage(Object element) {
		// begin-user-code

		// Don't display an image beside the shape

		return null;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the name associated with the given element object
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param element
	 *            <p>
	 *            The ICEObject or AbstractShape to produce its text
	 *            </p>
	 * @return <p>
	 *         The name associated with the element's ICEObject properties
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getText(Object element) {
		// begin-user-code

		// Check that the element is an ICEObject and is not null

		if (element instanceof ICEObject) {

			// Return the ICEObject's name property with its ICEObject ID
			// appended with a space separator

			ICEObject iceElement = (ICEObject) element;
			return iceElement.getName() + " " + iceElement.getId();
		}

		else if (element instanceof ShapeTreeContentProvider.BlankShape) {

			// Return the BlankShape default label text

			return BlankShape.TEXT;
		}

		else {
			return null;
		}

		// end-user-code
	}
}