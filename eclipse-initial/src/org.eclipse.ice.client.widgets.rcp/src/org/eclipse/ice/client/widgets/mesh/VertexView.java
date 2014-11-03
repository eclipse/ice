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
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Sphere;

/**
 * <!-- begin-UML-doc -->
 * <p >
 * This class provides a basic view for a Vertex, i.e., a single sphere on the
 * grid.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VertexView extends AbstractMeshView {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VertexView(String name, Material material) {
		// begin-user-code
		super(name, material);

		// Create a Sphere mesh for the geometry.
		Sphere sphere = new Sphere(30, 30, 0.2f);
		geometry.setMesh(sphere);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the location of the view's Geometry in the jME3 scene.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            The new location of the Vertex's Geometry in the jME3 scene.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLocation(Vector3f location) {
		// begin-user-code

		geometry.setLocalTranslation(location);

		return;
		// end-user-code
	}

	// TODO Add to model.
	/**
	 * Sets the current size of the vertex view's sphere.
	 * 
	 * @param size
	 *            The new radius of the vertex view's sphere.
	 */
	public void setSize(float size) {
		((Sphere) geometry.getMesh()).updateGeometry(30, 30, size);
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this VertexController
	 * and another VertexController. It returns true if the objects are equal
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
	 * This operation returns the hash value of the VertexController.
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
	 * This operation copies the contents of a VertexController into the current
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
	public void copy(VertexView view) {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the VertexController using a deep copy.
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