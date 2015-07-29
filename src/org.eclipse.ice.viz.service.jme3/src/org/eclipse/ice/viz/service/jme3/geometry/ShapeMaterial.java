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
package org.eclipse.ice.viz.service.jme3.geometry;

import org.eclipse.ice.viz.service.jme3.shapes.IShape;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

/**
 * <p>
 * Converts and stores an IShape's key/value properties list in format of JME3
 * data
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ShapeMaterial {
	/**
	 * <p>
	 * The selected state of the shape
	 * </p>
	 * 
	 */
	private boolean selected;
	/**
	 * 
	 */
	private float alpha;

	/**
	 * <p>
	 * The current AssetManager
	 * </p>
	 * 
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
	 * <p>
	 * Initializes the instance with a given IShape
	 * </p>
	 * 
	 * @param assetManager
	 *            <p>
	 *            The AssetManager to load the Material definition
	 *            </p>
	 * @param shape
	 *            <p>
	 *            The shape associated with this ShapeRenderProperties instance
	 *            </p>
	 */
	public ShapeMaterial(AssetManager assetManager, IShape shape) {

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
	}

	/**
	 * <p>
	 * Returns the material associated with the IShape properties
	 * </p>
	 * 
	 * @return <p>
	 *         The material to render the shape
	 *         </p>
	 */
	public Material getMaterial() {

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
	 * <p>
	 * Applies the properties of the given shape to the state of the
	 * ShapeRenderProperties
	 * </p>
	 * 
	 * @param shape
	 *            <p>
	 *            The shape to apply (either a child or its ancestors)
	 *            </p>
	 */
	private void applyShape(IShape shape) {

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

	}

}
