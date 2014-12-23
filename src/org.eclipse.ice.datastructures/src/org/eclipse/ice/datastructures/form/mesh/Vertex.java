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
package org.eclipse.ice.datastructures.form.mesh;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class represents a single vertex in a polygon. Multiple Edges can be
 * registered with a single Vertex as listeners, and the Vertex's location can
 * be updated. A Vertex should have its ID set to a unique value before
 * constructing an Edge from it.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton, Taylor Patterson
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "Vertex")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vertex extends ICEObject implements IMeshPart {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current (x, y, z) coordinates of the vertex.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private float[] location;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A nullary constructor. This creates a vertex at the origin and
	 * initializes any fields necessary for the minimal function of a Vertex.
	 * Required for persistence.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Vertex() {
		// begin-user-code

		// Call the super constructor.
		super();

		// Initialize the default location.
		location = new float[] { 0f, 0f, 0f };

		// Set the default name, id, and description.
		setName("Vertex");
		setDescription("");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor. Sets the initial location to the specified
	 * coordinates.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param x
	 *            <p>
	 *            The x component of the location.
	 *            </p>
	 * @param y
	 *            <p>
	 *            The y component of the location.
	 *            </p>
	 * @param z
	 *            <p>
	 *            The z component of the location.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Vertex(float x, float y, float z) {
		// begin-user-code

		// Initialize the defaults.
		this();

		// Set the location.
		location[0] = x;
		location[1] = y;
		location[2] = z;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This constructor takes an array of floats as the location.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            A vector representing the Vertex's location in 3D space.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Vertex(float[] location) {
		// begin-user-code

		// Intialize the defaults.
		this();

		// Set the location if possible.
		if (location == null || location.length != 3) {
			throw new IllegalArgumentException(
					"Vertex error: The (x,y,z) coordinates must be a non-null array of size 3.");
		}
		this.location[0] = location[0];
		this.location[1] = location[1];
		this.location[2] = location[2];

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the current location of the Vertex.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param x
	 *            <p>
	 *            The x component of the location.
	 *            </p>
	 * @param y
	 *            <p>
	 *            The y component of the location.
	 *            </p>
	 * @param z
	 *            <p>
	 *            The z component of the location.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLocation(float x, float y, float z) {
		// begin-user-code

		// Only update if necessary.
		if (location[0] != x || location[1] != y || location[2] != z) {
			// Set the location.
			location[0] = x;
			location[1] = y;
			location[2] = z;

			// Notify the listeners.
			notifyListeners();
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the current location of the Vertex.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            A 3D vector of floats representing the Vertex's current
	 *            location.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLocation(float[] location) {
		// begin-user-code

		// Only update the location on well-formed input
		if (location != null && location.length == 3) {
			setLocation(location[0], location[1], location[2]);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the current location of the Vertex.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A 3D vector of floats representing the Vertex's current location.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public float[] getLocation() {
		// begin-user-code
		return new float[] { location[0], location[1], location[2] };
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the Vertex.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Get initial hash code
		int hash = super.hashCode();

		// Hash each component in the location
		for (Float coordinate : location) {
			hash = 31 * hash + coordinate.hashCode();
		}

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this Vertex and another
	 * Vertex. It returns true if the Vertices are equal and false if they are
	 * not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the Objects are equal, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		boolean equal = false;

		// Check the reference.
		if (this == otherObject) {
			equal = true;
		}
		// Check that the other object is not null and an instance of Vertex
		else if (otherObject != null && otherObject instanceof Vertex) {

			// Cast to Custom2DShape
			Vertex otherVertex = (Vertex) otherObject;

			// Check that these objects have the same ICEObject data, equal
			// locations, and equal edge lists
			equal = (super.equals(otherObject) && Arrays.equals(location,
					otherVertex.getLocation()));
		}

		return equal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a Vertex into the current object
	 * using a deep copy. Listeners are not copied.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param vertex
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Vertex vertex) {
		// begin-user-code

		// Return if object is null
		if (vertex == null) {
			return;
		}

		// Copy the ICEObject data
		super.copy(vertex);

		// Copy the location
		setLocation(vertex.getLocation());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the Vertex using a deep copy. Listeners
	 * are not copied.
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

		// Create a new Vertex
		Vertex vertex = new Vertex();

		// Copy `this` into vertex
		vertex.copy(this);

		return vertex;
		// end-user-code
	}

	/**
	 * Overrides the default behavior of unregister. The problem is that we
	 * often get listeners that satisfy the equals() method but are not actually
	 * the same listener, so the wrong listener gets removed. This unregister
	 * operation instead uses references for comparison rather than the equals()
	 * method.
	 */
	@Override
	public void unregister(IUpdateableListener listener) {
		if (listener != null) {
			for (int i = listeners.size() - 1; i >= 0; i--) {
				if (listeners.get(i) == listener) {
					listeners.remove(i);
				}
			}
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method calls the {@link IMeshPartVisitor}'s visit method.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The {@link IMeshPartVisitor} that is visiting this
	 *            {@link IMeshPart}.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void acceptMeshVisitor(IMeshPartVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}