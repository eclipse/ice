/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.mesh;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * <!-- begin-UML-doc -->
 * <p >
 * This class provides a basic view for a component of a mesh.
 * </p>
 * <p >
 * The view is the part that the user sees. However, all interactions with the
 * view are processed via the controller. The view should only be updated within
 * the jME3 application's simpleUpdate() method.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class AbstractMeshView {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The jME3 Geometry representing this view in the scene.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Geometry geometry;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor. This creates a jME3 Geometry that can be
	 * attached to the scene.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AbstractMeshView(String name, Material material) {
		// begin-user-code

		// Initialize the Geometry used by this view.
		geometry = new Geometry(name);

		// Set the Geometry's Material.
		geometry.setMaterial(material);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the jME3 Node that contains the view's Geometry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param node
	 *            <p>
	 *            The new parent Node.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setParentNode(Node node) {
		// begin-user-code

		if (node != null) {
			node.attachChild(geometry);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the color of the Geometry's material.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param color
	 *            <p>
	 *            The new color for the view's Geometry.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setColor(ColorRGBA color) {
		// begin-user-code

		geometry.getMaterial().setColor("Color", color);

		return;
		// end-user-code
	}

	// TODO Add to model.
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the size of the view.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            <p>
	 *            The new size of the view.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public abstract void setSize(float size);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Disposes of the AbstractMeshView and its associated jME3 objects.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void dispose() {
		// begin-user-code

		// Remove the Geometry from the parent Node.
		Node parent = geometry.getParent();
		if (parent != null) {
			parent.detachChild(geometry);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this AbstractMeshView
	 * and another AbstractMeshView. It returns true if the objects are equal
	 * and false if they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the objects are equal, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code
		return super.equals(otherObject);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the AbstractMeshView.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		return super.hashCode();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a AbstractMeshView into the current
	 * object using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param view
	 *            <p>
	 *            The object from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(AbstractMeshView view) {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the AbstractMeshView using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code
		return null;
		// end-user-code
	}
}