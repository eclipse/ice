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

import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.geometry.ICEShape;
import org.eclipse.ice.viz.service.geometry.OperatorType;
import org.eclipse.ice.viz.service.geometry.ShapeType;
import org.eclipse.ice.viz.service.geometry.Transformation;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * <p>
 * Stores additional information for generating a JME3 spatial and material from
 * the IShape
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class RenderShape extends ICEShape {

	/**
	 * 
	 */
	private boolean selected = false;
	/**
	 * 
	 */
	private float alpha = 1.0f;

	/**
	 * 
	 * @param shape
	 */
	public RenderShape(ShapeType shapeType){
		super(shapeType);
	}
	
	public RenderShape(OperatorType operatorType){
		super(operatorType);
	}

	/**
	 * <p>
	 * Returns the selected state of the shape
	 * </p>
	 * 
	 * @return <p>
	 *         The selected state
	 *         </p>
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * <p>
	 * Sets the selected state of the shape
	 * </p>
	 * 
	 * @param selected
	 *            <p>
	 *            The selected state
	 *            </p>
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * <p>
	 * Generates the material from the current shape state
	 * </p>
	 * <p>
	 * In order to obtain a Spacial object, assigning it a material from this
	 * operation is not needed. It is automatically assigned a material in this
	 * implementation.
	 * </p>
	 * 
	 * @return <p>
	 *         The new material
	 *         </p>
	 */
	public Material createMaterial() {

		// TODO Render the material from the state of the RenderShape

		Material material = new Material();
		return material;

	}

	private class SpatialGenerator{

		Spatial spatial = null;

		public void addShape(ICEShape newShape){
			if(newShape.isComplex()){
				if (newShape.getOperatorType() == OperatorType.Union) {

					Node node = new Node();

					spatial = node;
				}
			}
			else{
				Mesh mesh = new Box(1.0f, 1.0f, 1.0f);
				spatial = new Geometry(newShape.getName(), mesh);
			}
			
		}
		

	}

	/**
	 * Generates a Spatial object from the current shape state
	 * 
	 * The proper material is automatically assigned to the Spatial when
	 * created.
	 */
	public Spatial createSpatial() {

		// Make a visitor class so the spatial can be created

		SpatialGenerator spatialGenerator = new SpatialGenerator();

		// Call the appropriate visit operation of the above anonymous class

		spatialGenerator.addShape(this);

		return spatialGenerator.spatial;

	}

	/**
	 * <p>
	 * Sets the alpha amount to the given value
	 * </p>
	 * 
	 * @param alpha
	 *            <p>
	 *            The new alpha value (0 is transparent, 1 is opaque)
	 *            </p>
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	/**
	 * <p>
	 * Returns the alpha value, scaled on [0, 1]
	 * </p>
	 * 
	 * @return <p>
	 *         The alpha value (0 is transparent, 1 is opaque)
	 *         </p>
	 */
	public float getAlpha() {
		return alpha;
	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub

	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public boolean equals(Object otherObject) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from Identifiable.
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Converts the IShape's Transformation to a JME3 Transform
	 */
	private Transform getTransform() {

		Transformation transformation = getTransformation();

		// Create the JME3 transform

		Transform transform = new Transform();

		double size = transformation.getSize();
		double[] scale = transformation.getScale();
		double[] rotation = transformation.getRotation();
		double[] translation = transformation.getTranslation();

		// Set the transformation values

		transform.setScale((float) (size * scale[0]),
				(float) (size * scale[1]), (float) (size * scale[2]));

		// Calculation the rotation from a quaternion

		Quaternion rotationQuat = new Quaternion();
		rotationQuat.fromAngles((float) rotation[0], (float) rotation[1],
				(float) rotation[2]);
		transform.setRotation(rotationQuat);

		transform.setTranslation((float) translation[0],
				(float) translation[1], (float) translation[2]);

		return transform;

	}

	/*
	 * Implements a method from IUpdateable.
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// Does nothing yet.
	}

	/*
	 * Implements a method from Component.
	 */
	@Override
	public void register(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}



}
