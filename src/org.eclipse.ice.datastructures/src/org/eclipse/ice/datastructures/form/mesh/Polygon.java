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

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.geometry.AbstractShape;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class represents a polygon composed of a certain number of vertices
 * connected by edges.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "Polygon")
@XmlSeeAlso({ Quad.class, Hex.class })
@XmlAccessorType(XmlAccessType.FIELD)
public class Polygon extends AbstractShape implements IUpdateableListener,
		IMeshPart {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The collection of Vertices composing the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Vertex")
	protected ArrayList<Vertex> vertices;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The collection of Edges composing the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Edge")
	protected ArrayList<Edge> edges;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The properties for each polygon defined in the MESH DATA section of a Nek
	 * reafile. Contains a String representing the material ID, and an integer
	 * representing the group number of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "PolygonProperties")
	protected PolygonProperties polygonProperties;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A map of edge properties for each edge, keyed on the edge IDs.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElementWrapper(name = "EdgeProperties")
	private HashMap<Integer, EdgeProperties> edgeProperties;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A nullary constructor. This creates a Polygon with no vertices or edges
	 * and initializes any fields necessary. Required for persistence.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Polygon() {
		// begin-user-code
		super();

		// Initialize the lists of edges and vertices.
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// Initialize the boundary condition containers.
		edgeProperties = new HashMap<Integer, EdgeProperties>();

		// Initialize the polygon properties
		polygonProperties = new PolygonProperties();

		// Set the default name, id, and description.
		setName("Polygon");
		setDescription("");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edges
	 *            <p>
	 *            A collection of edges connecting a collection of vertices.
	 *            </p>
	 * @param vertices
	 *            <p>
	 *            A collection of vertices connected by a collection of edges.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Polygon(ArrayList<Edge> edges, ArrayList<Vertex> vertices) {
		// begin-user-code

		// Initialize the defaults.
		this();

		// A Polygon has the following requirements:
		// Edge i must have as its start and end points vertices i and i+1.
		// (for the last edge, these are vertices i and 0).
		// The Vertex references stored in the Edge should be the same as stored
		// in the Polygon's list of vertices.

		// This does not check for simple/complex polygons, only that the outer
		// edges form a cycle.

		// Check the parameters for null values.
		if (vertices == null || vertices.contains(null) || vertices.size() < 3) {
			throw new IllegalArgumentException(
					"Polygon error: The vertex list must be a non-null list with at least 3 vertices.");
		} else if (edges == null || edges.contains(null) || edges.size() < 3) {
			throw new IllegalArgumentException(
					"Polygon error: The edge list must be a non-null list with at least 3 edges.");
		}
		// Get the sizes of the two lists.
		int size = vertices.size();

		// Use a Set of vertex IDs to make sure no vertex connects more than two
		// edges.
		TreeSet<Integer> vertexIds = new TreeSet<Integer>();
		TreeSet<Integer> edgeIds = new TreeSet<Integer>();

		// Make sure the edges form a cycle.
		for (int i = 0, j = 1; i < size; i++, j = (i + 1) % size) {
			int v1 = vertices.get(i).getId();
			int v2 = vertices.get(j).getId();
			int[] ids = edges.get(i).getVertexIds();

			// Add the current vertex ID to the Set.
			vertexIds.add(v1);

			// Make sure the two adjacent vertex IDs are not the same.
			if (v1 == v2) {
				throw new IllegalArgumentException(
						"Polygon error: Same ID for adjacent vertices.");
			}
			// Make sure the IDs v1 and v2 are both in the edge's vertex ID
			// list.
			else if ((v1 != ids[0] && v1 != ids[1])
					|| (v2 != ids[0] && v2 != ids[1])) {
				throw new IllegalArgumentException("Polygon error: Edge " + i
						+ " uses unknown vertex.");
			}
			edgeIds.add(edges.get(i).getId());
		}

		// Check the size of the Set of vertex IDs. Every ID should be unique!
		if (vertexIds.size() < size) {
			throw new IllegalArgumentException(
					"Polygon error: A vertex connects more than two edges.");
		} else if (edgeIds.size() < size) {
			throw new IllegalArgumentException(
					"Polygon error: Same edge ID used more than once.");
		}
		// If the vertices/edges supplied are valid, duplicate them. We also
		// must ensure the edges are registered with the correct vertex
		// instances.
		for (int i = 0; i < size; i++) {
			// Get the edge from the supplied list.
			Edge edge = edges.get(i);

			// Get the new start and end vertices for this edge.
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % size);

			// Register the clone with the start and end.
			start.register(edge);
			end.register(edge);

			// Send the start and end vertices to the clone.
			edge.update(start);
			edge.update(end);

			// Add the edge and vertex to the Polygon's lists.
			this.edges.add(edge);
			this.vertices.add(start);

			// Create an entry for the edge in the map of edge properties.
			EdgeProperties properties = new EdgeProperties();
			edgeProperties.put(edge.getId(), properties);
			// Register with all of the boundary conditions in the properties.
			properties.getFluidBoundaryCondition().register(this);
			properties.getThermalBoundaryCondition().register(this);
			for (BoundaryCondition condition : properties
					.getOtherBoundaryConditions()) {
				condition.register(this);
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the collection of edges composing the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The collection of Edges composing the 2D polygon.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Edge> getEdges() {
		// begin-user-code

		// Return a copy of the edge list.
		return new ArrayList<Edge>(edges);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the collection of vertices composing the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The collection of Vertices composing the 2D polygon.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Vertex> getVertices() {
		// begin-user-code

		// Return a copy of the vertex list.
		return new ArrayList<Vertex>(vertices);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets a fluid boundary condition for an edge of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edgeId
	 *            The ID of the edge that will have the new BoundaryCondition.
	 * @param condition
	 *            The new BoundaryCondition.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setFluidBoundaryCondition(int edgeId,
			BoundaryCondition condition) {
		// begin-user-code

		// First, check that the edgeId is valid by performing a map lookup.
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (condition != null && properties != null) {
			// If the edgeId is valid, try to set the new condition. If the new
			// condition is set, we need to register with the new condition and
			// notify listeners of the change.
			BoundaryCondition oldCondition = properties
					.getFluidBoundaryCondition();
			if (properties.setFluidBoundaryCondition(condition)) {
				// Unregister from the old condition and register with the new.
				oldCondition.unregister(this);
				condition.register(this);

				// Notify listeners of the change.
				notifyListeners();
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the fluid boundary condition for an edge of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edgeId
	 *            The ID of the edge that has a BoundaryCondition.
	 * @return The edge's BoundaryCondition for this polygon, or null if the
	 *         edge ID is invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition getFluidBoundaryCondition(int edgeId) {
		// begin-user-code

		// If the edgeId is valid, we can pull the property from the
		// EdgeProperty instance.
		BoundaryCondition condition = null;
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (properties != null) {
			condition = properties.getFluidBoundaryCondition();
		}
		return condition;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets a thermal boundary condition for an edge of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edgeId
	 *            The ID of the edge that will have the new BoundaryCondition.
	 * @param condition
	 *            The new BoundaryCondition.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setThermalBoundaryCondition(int edgeId,
			BoundaryCondition condition) {
		// begin-user-code

		// First, check that the edgeId is valid by performing a map lookup.
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (condition != null && properties != null) {
			// If the edgeId is valid, try to set the new condition. If the new
			// condition is set, we need to register with the new condition and
			// notify listeners of the change.
			BoundaryCondition oldCondition = properties
					.getThermalBoundaryCondition();
			if (properties.setThermalBoundaryCondition(condition)) {
				// Unregister from the old condition and register with the new.
				oldCondition.unregister(this);
				condition.register(this);

				// Notify listeners of the change.
				notifyListeners();
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the thermal boundary condition for an edge of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edgeId
	 *            The ID of the edge that has a BoundaryCondition.
	 * @return The edge's BoundaryCondition for this polygon, or null if the
	 *         edge ID is invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition getThermalBoundaryCondition(int edgeId) {
		// begin-user-code

		// If the edgeId is valid, we can pull the property from the
		// EdgeProperty instance.
		BoundaryCondition condition = null;
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (properties != null) {
			condition = properties.getThermalBoundaryCondition();
		}
		return condition;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets a passive scalar boundary condition for an edge of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edgeId
	 *            The ID of the edge that will have the new BoundaryCondition.
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @param condition
	 *            The new BoundaryCondition.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setOtherBoundaryCondition(int edgeId, int otherId,
			BoundaryCondition condition) {
		// begin-user-code

		// First, check that the edgeId is valid by performing a map lookup.
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (condition != null && properties != null) {
			// If the edgeId is valid, try to set the new condition. If the new
			// condition is set, we need to register with the new condition and
			// notify listeners of the change.
			BoundaryCondition oldCondition = properties
					.getOtherBoundaryCondition(otherId);
			if (properties.setOtherBoundaryCondition(otherId, condition)) {
				// Unregister from the old condition and register with the new.
				// We need a null check because the scalar index ID may be new.
				if (oldCondition != null) {
					oldCondition.unregister(this);
				}
				condition.register(this);

				// Notify listeners of the change.
				notifyListeners();
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets a passive scalar boundary condition for an edge of the polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edgeId
	 *            The ID of the edge that has a BoundaryCondition.
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @return The edge's BoundaryCondition for this polygon, or null if the
	 *         edge ID is invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition getOtherBoundaryCondition(int edgeId, int otherId) {
		// begin-user-code

		// If the edgeId is valid, we can pull the property from the
		// EdgeProperty instance.
		BoundaryCondition condition = null;
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (properties != null) {
			condition = properties.getOtherBoundaryCondition(otherId);
		}
		return condition;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the properties for the current polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param materialId
	 *            The material ID of the current polygon.
	 * @param group
	 *            The group number of the current polygon.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPolygonProperties(String materialId, int group) {
		// begin-user-code
		polygonProperties = new PolygonProperties(materialId, group);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the properties for the current polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The properties for the current polygon.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PolygonProperties getPolygonProperties() {
		// begin-user-code
		return polygonProperties;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the Polygon from persistent storage as XML. This
	 * operation will throw an IOException if it fails.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            An input stream from which the ICEObject should be loaded.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code

		// Initialize a JAXBManipulator.
		jaxbManipulator = new ICEJAXBManipulator();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream.
		try {
			Object dataObject = jaxbManipulator.read(this.getClass(),
					inputStream);

			// Copy the contents of the loaded object into the current one.
			this.copy((Polygon) dataObject);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the Polygon.
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

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash = 31 * hash + vertices.hashCode();
		hash = 31 * hash + edges.hashCode();
		hash = 31 * hash + edgeProperties.hashCode();
		hash = 31 * hash + polygonProperties.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this Polygon and another
	 * Polygon. It returns true if the Polygons are equal and false if they are
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

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof Polygon) {

			// We can now cast the other object.
			Polygon polygon = (Polygon) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject)
					&& vertices.equals(polygon.vertices)
					&& edges.equals(polygon.edges)
					&& edgeProperties.equals(polygon.edgeProperties) && polygonProperties
					.equals(polygon.polygonProperties));
		}

		return equals;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a Polygon into the current object
	 * using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param polygon
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Polygon polygon) {
		// begin-user-code

		// Check the parameters.
		if (polygon == null) {
			return;
		}
		// Copy the super's data.
		super.copy(polygon);

		// Deep copy the vertices.
		vertices.clear();
		for (Vertex vertex : polygon.getVertices()) {
			vertices.add((Vertex) vertex.clone());
		}

		// Deep copy the edges.
		edges.clear();
		ArrayList<Edge> otherEdges = polygon.getEdges();
		int size = otherEdges.size();
		for (int i = 0; i < size; i++) {
			// Clone the edge. This deep copies the other edge's vertices, so we
			// must hook the clone up to our vertex instances.
			Edge clone = (Edge) otherEdges.get(i).clone();

			// Add the clone to the list of edges.
			edges.add(clone);

			// Get the new start and end vertices for this edge.
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % size);

			// Register the clone with the start and end.
			start.register(clone);
			end.register(clone);

			// Send the start and end vertices to the clone.
			clone.update(start);
			clone.update(end);
		}

		/* ---- Deep copy the edge properties. ---- */
		// Unregister from any of the current boundary conditions.
		for (Entry<Integer, EdgeProperties> entry : edgeProperties.entrySet()) {
			EdgeProperties properties = entry.getValue();
			// Unregister from the fluid and thermal boundary conditions.
			properties.getFluidBoundaryCondition().unregister(this);
			properties.getThermalBoundaryCondition().unregister(this);
			// Unregister from all passive scalar boundary conditions.
			for (BoundaryCondition condition : properties
					.getOtherBoundaryConditions()) {
				condition.unregister(this);
			}
		}

		// Clone all of the edge properties from the other polygon and register
		// with their boundary conditions.
		for (Entry<Integer, EdgeProperties> entry : polygon.edgeProperties
				.entrySet()) {
			// Clone and add the edge's properties.
			EdgeProperties properties = (EdgeProperties) entry.getValue()
					.clone();
			edgeProperties.put(entry.getKey(), properties);

			// Unregister from the fluid and thermal boundary conditions.
			properties.getFluidBoundaryCondition().register(this);
			properties.getThermalBoundaryCondition().register(this);
			// Unregister from all passive scalar boundary conditions.
			for (BoundaryCondition condition : properties
					.getOtherBoundaryConditions()) {
				condition.register(this);
			}
		}
		/* ---------------------------------------- */

		// Copy the PolygonProperties
		polygonProperties = (PolygonProperties) polygon.getPolygonProperties()
				.clone();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the Polygon using a deep copy.
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

		// Initialize a new object.
		Polygon object = new Polygon();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#acceptShapeVisitor(IShapeVisitor visitor)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void acceptShapeVisitor(IShapeVisitor visitor) {
		// begin-user-code

		// Nothing to do here. We are currently only extending AbstractShape for
		// the transformation tools.
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Implements the update operation. Currently, the Polygon registers with
	 * its current set of BoundaryConditions.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            The updated component. This should always be a
	 *            BoundaryCondition.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(IUpdateable component) {
		// begin-user-code

		// TODO Currently, this code has been disabled. We still need to decide
		// if the polygon should pass on an update notification when a boundary
		// condition has changed, or if interested parties should register
		// directly with the boundary condition.

		// This method is only called if one of the boundary conditions was
		// updated.

		// Notify listeners when one of the boundary conditions was changed.
		notifyListeners();

		return;
		// end-user-code
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