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
 * <!-- begin-UML-doc -->
 * <p>
 * Stores additional information for generating a JME3 spatial and material from
 * the IShape
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RenderShape implements IShape {
	/**
	 * The wrapped shape serving as a data structure without storing any
	 * rendering details
	 */
	private IShape shape;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean selected = false;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private float alpha = 1.0f;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param shape
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public RenderShape(IShape shape) {
		// begin-user-code

		this.shape = shape;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the selected state of the shape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The selected state
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isSelected() {
		// begin-user-code
		return selected;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the selected state of the shape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param selected
	 *            <p>
	 *            The selected state
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSelected(boolean selected) {
		// begin-user-code
		this.selected = selected;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Generates the material from the current shape state
	 * </p>
	 * <p>
	 * In order to obtain a Spacial object, assigning it a material from this
	 * operation is not needed. It is automatically assigned a material in this
	 * implementation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new material
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material createMaterial() {
		// begin-user-code

		// TODO Render the material from the state of the RenderShape

		Material material = new Material();
		return material;

		// end-user-code
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
		// begin-user-code

		// Make a visitor class so the spatial can be created

		SpatialGenerator spatialGenerator = new SpatialGenerator();

		// Call the appropriate visit operation of the above anonymous class

		shape.acceptShapeVisitor(spatialGenerator);

		return spatialGenerator.spatial;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the alpha amount to the given value
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param alpha
	 *            <p>
	 *            The new alpha value (0 is transparent, 1 is opaque)
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setAlpha(float alpha) {
		// begin-user-code
		this.alpha = alpha;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the alpha value, scaled on [0, 1]
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The alpha value (0 is transparent, 1 is opaque)
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public float getAlpha() {
		// begin-user-code
		return alpha;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setId(int id)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setId(int id) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getDescription()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getDescription() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getId()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getId() {
		// begin-user-code
		// TODO Auto-generated method stub
		return 0;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setName(String name)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setName(String name) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setDescription(String description)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDescription(String description) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#equals(Object otherObject)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#hashCode()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		// TODO Auto-generated method stub
		return 0;
		// end-user-code
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(String updatedKey, String newValue) {
		// begin-user-code
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#register(IUpdateableListener listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void register(IUpdateableListener listener) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor visitor)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IComponentVisitor visitor) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#getTransformation()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Transformation getTransformation() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#setTransformation(Transformation transformation)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setTransformation(Transformation transformation) {
		// begin-user-code
		return shape.setTransformation(transformation);
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#getProperty(String key)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key) {
		// begin-user-code
		return shape.getProperty(key);
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#setProperty(String key, String value)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setProperty(String key, String value) {
		// begin-user-code
		return shape.setProperty(key, value);
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#removeProperty(String key)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeProperty(String key) {
		// begin-user-code
		return shape.removeProperty(key);
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#getParent()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IShape getParent() {
		// begin-user-code
		// TODO
		return null;
		// end-user-code
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
