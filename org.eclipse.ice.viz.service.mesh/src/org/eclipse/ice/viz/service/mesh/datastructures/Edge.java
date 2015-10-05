/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.datastructures.VizObject.VizObject;

/**
 * <p>
 * This class represents a single edge in a polygon. Each edge must be
 * registered with two Vertices. The default behavior of an Edge is that of a
 * straight line between the two vertices.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@XmlRootElement(name = "Edge")
@XmlSeeAlso({ PolynomialEdge.class, BezierEdge.class })
@XmlAccessorType(XmlAccessType.FIELD)
public class Edge extends VizObject implements IVizUpdateableListener, IMeshPart {

	/**
	 * <p>
	 * An array holding the start and end vertices for this edge.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Vertex")
	private final Vertex[] vertices;

	/**
	 * <p>
	 * The length of the edge.
	 * </p>
	 * 
	 */
	@XmlAttribute
	private float length;

	/**
	 * <p>
	 * A lock for updating the edge. It is a read/write lock, meaning multiple
	 * reads can occur simultaneously, whereas writes block the lock.
	 * </p>
	 * 
	 */
	@XmlTransient
	private final ReentrantReadWriteLock updateLock;

	/**
	 * <p>
	 * A nullary constructor. This creates an Edge with default (invalid)
	 * vertices and initializes any fields necessary for the minimal function of
	 * an Edge. Required for persistence.
	 * </p>
	 * 
	 */
	public Edge() {

		// Call the super constructor.
		super();

		// Initialize the default vertices.
		vertices = new Vertex[] { new Vertex(), new Vertex() };

		// Set the default length.
		length = 0f;

		// Set the default name, id, and description.
		setName("Edge");
		setDescription("");

		// Initialize the read/write lock.
		updateLock = new ReentrantReadWriteLock(true);

		return;
	}

	/**
	 * <p>
	 * The default constructor. It sets the edge's vertices based on the
	 * supplied starting and ending vertices. The vertices should be different
	 * and must have their IDs set to a non-negative integer. It registers the
	 * edge as a listener with the provided vertices.
	 * </p>
	 * 
	 * @param start
	 *            <p>
	 *            The first Vertex in this Edge.
	 *            </p>
	 * @param end
	 *            <p>
	 *            The second Vertex in this Edge.
	 *            </p>
	 */
	public Edge(Vertex start, Vertex end) {

		// The expected behavior is that if the supplied Vertices are invalid,
		// then the edge's default vertices become an empty list and its length
		// becomes -1.

		// The vertices are valid if:
		// they are non-null Vertex instances
		// they are not the same reference
		// there are exactly 2 Vertex instances

		// Initialize the defaults.
		this();

		// The supplied Vertices must not be null and must not be the same.
		if (start == null || end == null || start.getId() == end.getId()) {
			throw new IllegalArgumentException(
					"Edge error: start and end vertices must not be null and must not have the same ID.");
		}
		// Set the vertex IDs.
		vertices[0] = start;
		vertices[1] = end;

		// Update the length
		updateLength();

		// Register with the vertices.
		start.register(this);
		end.register(this);

		return;
	}

	/**
	 * <p>
	 * This constructor takes a collection of vertices. The collection should
	 * contain only two different vertices. They must have their IDs set to a
	 * non-negative integer. It registers the edge as a listener with the
	 * provided vertices.
	 * </p>
	 * 
	 * @param vertices
	 *            <p>
	 *            The two Vertices this Edge connects.
	 *            </p>
	 */
	public Edge(ArrayList<Vertex> vertices) {

		// The expected behavior is that if the supplied ArrayList of Vertices
		// is invalid, then the edge's default vertices become an empty list and
		// its length becomes -1.

		// The vertices are valid if:
		// they are non-null Vertex instances
		// they are not the same reference
		// there are exactly 2 Vertex instances

		// Initialize the defaults.
		this();

		// The supplied ArrayList must not be null and must have two elements.
		if (vertices == null || vertices.size() != 2) {
			throw new IllegalArgumentException(
					"Edge error: The vertex ArrayList must not be null and must have 2 elements.");
		}
		// Make sure the vertices are not null and are not the same
		// reference.
		Vertex start = vertices.get(0);
		Vertex end = vertices.get(1);

		// The supplied Vertices must not be null and must not be the same.
		if (start == null || end == null || start.getId() == end.getId()) {
			throw new IllegalArgumentException(
					"Edge error: start and end vertices must not be null and must not have the same ID.");
		}
		// Set the vertex IDs.
		this.vertices[0] = start;
		this.vertices[1] = end;

		// Update the length
		updateLength();

		// Register with the vertices.
		start.register(this);
		end.register(this);

		return;
	}

	/**
	 * <p>
	 * Gets the IDs of the vertices this edge connects.
	 * </p>
	 * 
	 * @return <p>
	 *         The IDs of the vertices this edge connects.
	 *         </p>
	 */
	public int[] getVertexIds() {

		// Return a copy of the vertex IDs.
		return new int[] { vertices[0].getId(), vertices[1].getId() };
	}

	/**
	 * <p>
	 * Gets the location of the first vertex for this edge.
	 * </p>
	 * 
	 * @return <p>
	 *         The location of the edge's starting vertex.
	 *         </p>
	 */
	public float[] getStartLocation() {
		return vertices[0].getLocation();
	}

	/**
	 * <p>
	 * Gets the location of the last vertex for this edge.
	 * </p>
	 * 
	 * @return <p>
	 *         The location of the edge's ending vertex.
	 *         </p>
	 */
	public float[] getEndLocation() {
		return vertices[1].getLocation();
	}

	/**
	 * <p>
	 * Gets the length of the Edge. The default value is the Euclidean distance
	 * between the Edge's vertices.
	 * </p>
	 * 
	 * @return <p>
	 *         A float representing the length of the Edge.
	 *         </p>
	 */
	public float getLength() {

		// Set a default return value in case the length cannot be read.
		float length = 0f;

		// Acquire the read lock before fetching the length.
		updateLock.readLock().lock();
		try {
			length = this.length;
		} finally {
			updateLock.readLock().unlock();
		}

		return length;
	}

	/**
	 * Updates the Edge if the Vertex's ID matches one of its stored Vertices.
	 */
	@Override
	public void update(IVizUpdateable component) {

		// Check for null.
		if (component != null) {
			Vertex vertex = (Vertex) component;

			// Make sure the ID is valid.
			int id = vertex.getId();

			// See which vertex the component matches. For whichever one
			// matches, we need to update the reference, reset the length, and
			// notify edge listeners.
			for (int i = 0; i < 2; i++) {
				if (id == vertices[i].getId()) {

					updateLock.writeLock().lock();
					try {
						// Update the stored reference to the Vertex if its
						// ID matches.
						vertices[i] = vertex;
					} finally {
						updateLock.writeLock().unlock();
					}

					// Recompute the length.
					updateLength();

					// Notify listeners of the change.
					notifyListeners();

					// Break from the loop.
					i = 1;
				}
			}
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the Edge.
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

		// Compute the hashes of the vertices lowest ID first.
		int i = 0;
		if (vertices[0].getId() > vertices[1].getId()) {
			i = 1;
		}
		for (int j = 0; j < 2; j++) {
			hash = 31 * hash + vertices[(i + j) % 2].hashCode();
		}
		return hash;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this Edge and another
	 * Edge. It returns true if the Edges are equal and false if they are not.
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
		else if (otherObject != null && otherObject instanceof Edge) {

			// We can now cast the other object.
			Edge edge = (Edge) otherObject;

			// Compare the values between the two objects.
			// The order of the vertices does not matter.
			equals = (super.equals(otherObject) && ((vertices[0]
					.equals(edge.vertices[0]) && vertices[1]
					.equals(edge.vertices[1])) || (vertices[1]
					.equals(edge.vertices[0]) && vertices[0]
					.equals(edge.vertices[1]))));

		}

		return equals;
	}

	/**
	 * <p>
	 * This operation copies the contents of a Edge into the current object
	 * using a deep copy. The copy will not be registered with its vertices.
	 * </p>
	 * 
	 * @param edge
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 */
	public void copy(Edge edge) {

		// Check the parameters.
		if (edge == null) {
			return;
		}

		// Copy the super's data.
		super.copy(edge);

		// Deep copy the vertices.
		int size = edge.vertices.length;
		for (int i = 0; i < size; i++) {
			Vertex clone = (Vertex) edge.vertices[i].clone();
			vertices[i] = clone;
			clone.register(this);
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the Edge using a deep copy. The copy
	 * will not be registered with its vertices.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		Edge object = new Edge();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * Overrides the default behavior of unregister. The problem is that we
	 * often get listeners that satisfy the equals() method but are not actually
	 * the same listener, so the wrong listener gets removed. This unregister
	 * operation instead uses references for comparison rather than the equals()
	 * method.
	 */
	@Override
	public void unregister(IVizUpdateableListener listener) {
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
	}

	/**
	 * <p>
	 * Recomputes the length of the edge.
	 * </p>
	 * 
	 */
	private void updateLength() {
		updateLock.writeLock().lock();
		try {
			// Recompute the length of the edge.
			// Get the vertex coordinates.
			float[] v1 = vertices[0].getLocation();
			float[] v2 = vertices[1].getLocation();

			// Compute the distance between them.
			float d1 = v2[0] - v1[0];
			float d2 = v2[1] - v1[1];
			float d3 = v2[2] - v1[2];
			length = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
		} finally {
			updateLock.writeLock().unlock();
		}

		return;
	}
}