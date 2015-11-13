/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Andrew P. Belt, Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.geometry;

import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;

import com.jme3.material.Material;

/**
 * An extension of Shape which exposes JME3 specific capabilities of the
 * JME3ShapeView.
 * 
 * @author Andrew P. Belt, Robert Smith
 *
 */
public class JME3Shape extends Shape {

	/**
	 * The nullary constructor
	 */
	public JME3Shape() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public JME3Shape(ShapeComponent model, AbstractView view) {
		super(model, view);
	}

	/**
	 * Getter for the part's JME3 material.
	 * 
	 * @return
	 */
	public Material getMaterial() {
		return ((JME3ShapeView) view).getMaterial();
	}

	/**
	 * This operation sets the material that should be used for this shape when
	 * it is not selected.
	 * 
	 * @param mat
	 *            The JME3 material.
	 */
	public void setMaterial(Material mat) {
		((JME3ShapeView) view).setMaterial(mat);
	}

	/**
	 * This operation sets the material that should be used for the shape when
	 * it is selected.
	 * 
	 * @param mat
	 *            The JME3 material used when this shape is selected.
	 */
	public void setHighlightedMaterial(Material mat) {
		((JME3ShapeView) view).setHighlightedMaterial(mat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.Shape#clone()
	 */
	@Override
	public Object clone() {

		// Create a new shape from clones of the model and view
		ShapeComponent cloneComponent = (ShapeComponent) model.clone();
		JME3ShapeView cloneView = (JME3ShapeView) view.clone();
		JME3Shape clone = new JME3Shape(cloneComponent, cloneView);

		return clone;
	}
}
