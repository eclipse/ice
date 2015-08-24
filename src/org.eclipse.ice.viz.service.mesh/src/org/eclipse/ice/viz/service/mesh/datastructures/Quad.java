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
package org.eclipse.ice.viz.service.mesh.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This is a 4-sided polygon with 4 edges and 4 vertices.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@XmlRootElement(name = "Quad")
@XmlAccessorType(XmlAccessType.FIELD)
public class Quad extends Polygon {
	/**
	 * <p>
	 * A nullary constructor. This creates a Polygon with no vertices or edges
	 * and initializes any fields necessary. Required for persistence.
	 * </p>
	 * 
	 */
	public Quad() {
		super();

		return;
	}

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 * @param edges
	 *            <p>
	 *            A collection of 4 edges connecting 4 vertices.
	 *            </p>
	 * @param vertices
	 *            <p>
	 *            A collection of 4 vertices connecting 4 edges.
	 *            </p>
	 */
	public Quad(ArrayList<Edge> edges, ArrayList<Vertex> vertices) {
		super(edges, vertices);

		// If the quad is otherwise a valid polygon but does not have the proper
		// number of edges and vertices, it is invalid. Reset the lists.
		int size = this.edges.size();
		if (size != 4) {
			this.vertices = new ArrayList<Vertex>();
			this.edges = new ArrayList<Edge>();

			throw new IllegalArgumentException(
					"Quad error: Cannot construct a quad from " + size
							+ " edges.");
		}

		// Set the name
		setName("Quad");

		return;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the Quad.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the Object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		// None to add yet.

		return hash;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this Quad and another
	 * Quad. It returns true if the Quads are equal and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the Objects are equal, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null
				&& (otherObject instanceof Quad || otherObject instanceof Polygon)) {

			// We can now cast the other object.

			// Compare the values between the two objects.
			equals = super.equals(otherObject);
			// Nothing additional to test for now.
		}

		return equals;
	}

	/**
	 * <p>
	 * This operation copies the contents of a Quad into the current object
	 * using a deep copy.
	 * </p>
	 * 
	 * @param quad
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 */
	public void copy(Quad quad) {

		// Check the parameters.
		if (quad == null) {
			return;
		}

		// Copy the super's data.
		super.copy(quad);

		// Nothing additional to copy for now.

		return;
	}

	/**
	 * <p>
	 * This operation copies the contents of a 4-sided Polygon into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param polygon
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 */
	@Override
	public void copy(Polygon polygon) {

		// Check the parameters.
		if (polygon == null || polygon.getEdges().size() != 4) {
			return;
		}

		// Copy the super's data.
		super.copy(polygon);

		// Nothing additional to copy for now.

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the Quad using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		Quad object = new Quad();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * <p>
	 * This method calls the {@link IMeshPartVisitor}'s visit method.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The {@link IMeshPartVisitor} that is visiting this
	 *            {@link IMeshPart}.
	 *            </p>
	 */
	@Override
	public void acceptMeshVisitor(IMeshPartVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}