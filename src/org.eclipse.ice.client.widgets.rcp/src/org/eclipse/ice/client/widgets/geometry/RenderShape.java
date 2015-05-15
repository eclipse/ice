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

import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;
import org.eclipse.ice.datastructures.form.geometry.OperatorType;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.form.geometry.Transformation;

/**
 * <p>
 * Stores additional information for generating a JME3 spatial and material from
 * the IShape
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class RenderShape implements IShape {
	/**
	 * The wrapped shape serving as a data structure without storing any
	 * rendering details
	 */
	private IShape shape;

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
	public RenderShape(IShape shape) {

		this.shape = shape;

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

	private class SpatialGenerator implements IShapeVisitor {

		Spatial spatial = null;

		/**
		 * Creates a mesh at the end of the scene graph
		 */
		@Override
		public void visit(PrimitiveShape primitiveShape) {

			Mesh mesh = new Box(Vector3f.ZERO, 1.0f, 1.0f, 1.0f);
			spatial = new Geometry(primitiveShape.getName(), mesh);
		}

		/**
		 * Creates a node and adds child spatials to it
		 */
		@Override
		public void visit(ComplexShape complexShape) {

			// Unions are the only ComplexShape currently supported

			if (complexShape.getType() == OperatorType.Union) {

				Node node = new Node();

				spatial = node;
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

		shape.acceptShapeVisitor(spatialGenerator);

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

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setId(int id)
	 */
	public void setId(int id) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getDescription()
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getId()
	 */
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setName(String name)
	 */
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setDescription(String description)
	 */
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#equals(Object otherObject)
	 */
	public boolean equals(Object otherObject) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#hashCode()
	 */
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Converts the IShape's Transformation to a JME3 Transform
	 */
	private Transform getTransform() {

		Transformation transformation = shape.getTransformation();

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

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	public void update(String updatedKey, String newValue) {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#register(IUpdateableListener listener)
	 */
	public void register(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor visitor)
	 */
	public void accept(IComponentVisitor visitor) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#getTransformation()
	 */
	public Transformation getTransformation() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#setTransformation(Transformation transformation)
	 */
	public boolean setTransformation(Transformation transformation) {
		return shape.setTransformation(transformation);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#getProperty(String key)
	 */
	public String getProperty(String key) {
		return shape.getProperty(key);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#setProperty(String key, String value)
	 */
	public boolean setProperty(String key, String value) {
		return shape.setProperty(key, value);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#removeProperty(String key)
	 */
	public boolean removeProperty(String key) {
		return shape.removeProperty(key);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#getParent()
	 */
	public IShape getParent() {
		// TODO
		return null;
	}

	@Override
	public void acceptShapeVisitor(IShapeVisitor visitor) {
		shape.acceptShapeVisitor(visitor);
	}

	public Object clone() {
		return null;
	}

	@Override
	public void unregister(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}
}
