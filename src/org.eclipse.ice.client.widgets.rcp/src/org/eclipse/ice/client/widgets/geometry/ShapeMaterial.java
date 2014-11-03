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

import org.eclipse.ice.datastructures.form.geometry.IShape;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Converts and stores an IShape's key/value properties list in format of JME3
 * data
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeMaterial {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The selected state of the shape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean selected;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private float alpha;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current AssetManager
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AssetManager assetManager;

	/**
	 * The Material used for this shape.
	 */
	private Material material;

	/**
	 * The Material used for this shape when it is selected/highlighted.
	 */
	private Material highlightedMaterial;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the instance with a given IShape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param assetManager
	 *            <p>
	 *            The AssetManager to load the Material definition
	 *            </p>
	 * @param shape
	 *            <p>
	 *            The shape associated with this ShapeRenderProperties instance
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ShapeMaterial(AssetManager assetManager, IShape shape) {
		// begin-user-code

		// Set the assetManager used for this shape and material
		this.assetManager = assetManager;

		// Initialize variables
		selected = false;
		alpha = 1.0f;

		// Loop through each of the shape's parents
		while (shape != null) {
			applyShape(shape);
			// Get the shape's parent
			shape = shape.getParent();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the material associated with the IShape properties
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The material to render the shape
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material getMaterial() {
		// begin-user-code

		// There's no material if there is no assetManager!
		if (assetManager == null) {
			return null;
		}
		// Highlight the shape if it is selected. The if statement is ordered
		// backwards because it is most likely that the shape is not selected
		// and in this case it will execute this function quicker because it
		// only has to do the first check.
		if (!selected) {
			return material;
		} else {
			return highlightedMaterial;
		}
		// end-user-code
	}

	/**
	 * This operation sets the material that should be used for this shape.
	 * 
	 * @param mat
	 *            The JME3 material.
	 */
	public void setMaterial(Material mat) {

		// Set the material that should be used for this shape
		material = mat;

		return;
	}

	/**
	 * This operation sets the material that should be used for the shape when
	 * it is selected.
	 * 
	 * @param mat
	 *            The JME3 material used when this shape is selected.
	 */
	public void setHighlightedMaterial(Material mat) {

		// Set the material
		highlightedMaterial = mat;

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Applies the properties of the given shape to the state of the
	 * ShapeRenderProperties
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param shape
	 *            <p>
	 *            The shape to apply (either a child or its ancestors)
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void applyShape(IShape shape) {
		// begin-user-code

		// Extract the shape properties

		String selectedValue = shape.getProperty("selected");
		String alphaValue = shape.getProperty("alpha");

		// Selected

		if (selectedValue != null && "true".equals(selectedValue)) {
			selected = true;
		}
		// Alpha

		if (alphaValue != null) {
			try {
				float alphaFloat = Float.valueOf(alphaValue);

				alpha *= alphaFloat;
			} catch (NumberFormatException e) {
			}
		}

		// Scale alpha value with a constant factor

		// end-user-code
	}

}
