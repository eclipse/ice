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
package org.eclipse.ice.viz.service.geometry.widgets;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;
import org.eclipse.ice.viz.service.geometry.shapes.IShapeVisitor;
import org.eclipse.ice.viz.service.geometry.shapes.OperatorType;
import org.eclipse.ice.viz.service.geometry.shapes.PrimitiveShape;
import org.eclipse.ice.viz.service.geometry.shapes.Transformation;

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

			Mesh mesh = new Box(1.0f, 1.0f, 1.0f);
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizIdentifiable#setId(int)
	 */
	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizIdentifiable#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizIdentifiable#getId()
	 */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizIdentifiable#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizIdentifiable#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizIdentifiable#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// Does nothing yet.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#register(org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener)
	 */
	@Override
	public void register(IVizUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#getTransformation()
	 */
	@Override
	public Transformation getTransformation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#setTransformation(org.eclipse.ice.viz.service.geometry.shapes.Transformation)
	 */
	@Override
	public boolean setTransformation(Transformation transformation) {
		return shape.setTransformation(transformation);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		return shape.getProperty(key);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setProperty(String key, String value) {
		return shape.setProperty(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#removeProperty(java.lang.String)
	 */
	@Override
	public boolean removeProperty(String key) {
		return shape.removeProperty(key);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#getParent()
	 */
	@Override
	public IShape getParent() {
		// TODO
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.geometry.shapes.IShape#acceptShapeVisitor(org.eclipse.ice.viz.service.geometry.shapes.IShapeVisitor)
	 */
	@Override
	public void acceptShapeVisitor(IShapeVisitor visitor) {
		shape.acceptShapeVisitor(visitor);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#unregister(org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener)
	 */
	@Override
	public void unregister(IVizUpdateableListener listener) {
		// TODO Auto-generated method stub

	}
}
