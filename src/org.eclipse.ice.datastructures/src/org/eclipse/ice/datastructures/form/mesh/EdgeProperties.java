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
import org.eclipse.ice.datastructures.ICEObject.Persistable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class contains properties of an edge. These properties can be maintained
 * by either a Polygon or the Edge itself. All properties are private fields but
 * can be accessed via getters and setters.
 * </p>
 * <p>
 * </p>
 * <p>
 * Currently, the properties only contain the boundary conditions of edges
 * (faces). The intent is to let the Polygon (element) manage these properties
 * since in Nek the elements have boundary conditions for all their faces.
 * Shared faces can have two fluid, thermal, or nth passive scalar boundary
 * conditions.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "EdgeProperties")
@XmlAccessorType(XmlAccessType.FIELD)
public class EdgeProperties implements Persistable {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fluid boundary condition for this edge in a polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "fluidCondition")
	private BoundaryCondition fluidBoundaryCondition;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The thermal boundary condition for this edge in a polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "thermalCondition")
	private BoundaryCondition thermalBoundaryCondition;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A list of passive scalar boundary condition for this edge in a polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "otherCondition")
	private ArrayList<BoundaryCondition> otherBoundaryConditions;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor for properties of an Edge. This initializes every
	 * property it can to non-null values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public EdgeProperties() {
		// begin-user-code

		// Initialize the boundary condition fields.
		fluidBoundaryCondition = new BoundaryCondition();
		thermalBoundaryCondition = new BoundaryCondition();
		otherBoundaryConditions = new ArrayList<BoundaryCondition>();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the fluid boundary condition for the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param condition
	 *            The new BoundaryCondition.
	 * @return True if the new condition was successfully set, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setFluidBoundaryCondition(BoundaryCondition condition) {
		// begin-user-code

		boolean changed = false;

		// Only set the boundary condition if:
		// The new condition is not null
		// The new condition is not the same one as the old one
		if (condition != null && condition != fluidBoundaryCondition) {
			fluidBoundaryCondition = condition;
			changed = true;
		}

		return changed;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the fluid boundary condition for the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The edge's BoundaryCondition. This should never be null.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition getFluidBoundaryCondition() {
		// begin-user-code
		return fluidBoundaryCondition;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the thermal boundary condition for the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param condition
	 *            The new BoundaryCondition.
	 * @return True if the new condition was successfully set, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setThermalBoundaryCondition(BoundaryCondition condition) {
		// begin-user-code

		boolean changed = false;

		// Only set the boundary condition if:
		// The new condition is not null
		// The new condition is not the same one as the old one
		if (condition != null && condition != thermalBoundaryCondition) {
			thermalBoundaryCondition = condition;
			changed = true;
		}

		return changed;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the thermal boundary condition for the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The edge's BoundaryCondition. This should never be null.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition getThermalBoundaryCondition() {
		// begin-user-code
		return thermalBoundaryCondition;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets a passive scalar boundary condition for the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @param condition
	 *            The new BoundaryCondition.
	 * @return True if the new condition was successfully set, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setOtherBoundaryCondition(int otherId,
			BoundaryCondition condition) {
		// begin-user-code

		boolean changed = false;

		// The new boundary condition will only be added if:
		// The new condition is not null
		// The otherId (index of the passive scalar set) is valid (non-negative)
		if (condition != null && otherId >= 0) {
			// Get the size of the list of passive scalar conditions.
			int size = otherBoundaryConditions.size();

			// We only need to make any changes if:
			// The new passive scalar index is too big (we need to add more
			// default conditions to the list)
			// The new condition is not the same one as the old one

			// If necessary, add new, default BoundaryConditions to the list,
			// then add the new condition to the end of the list.
			if (otherId >= size) {
				for (int i = size; i < otherId; i++) {
					otherBoundaryConditions.add(new BoundaryCondition());
				}
				otherBoundaryConditions.add(condition);
				changed = true;
			}
			// If the index is already valid, compare the new condition with the
			// one in the list and update it if necessary.
			else if (condition != otherBoundaryConditions.get(otherId)) {
				otherBoundaryConditions.set(otherId, condition);
				changed = true;
			}
		}

		return changed;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets a passive scalar boundary condition from the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @return The edge's BoundaryCondition, or null if the passive scalar index
	 *         is invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition getOtherBoundaryCondition(int otherId) {
		// begin-user-code

		// The default return value
		BoundaryCondition condition = null;

		// Check the index before trying to retrieve the boundary condition.
		if (otherId >= 0 && otherId < otherBoundaryConditions.size()) {
			condition = otherBoundaryConditions.get(otherId);
		}

		return condition;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets all passive scalar boundary conditions for the edge.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return An ArrayList of all passive scalar boundary conditions for the
	 *         edge. If no passive scalar boundary conditions exist, this will
	 *         be an empty list.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<BoundaryCondition> getOtherBoundaryConditions() {
		// begin-user-code
		return new ArrayList<BoundaryCondition>(otherBoundaryConditions);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the EdgeProperties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The hash of the Object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Hash based on super's hashCode.
		int hash = 31;

		// Add local hashes.
		hash = 31 * hash + fluidBoundaryCondition.hashCode();
		hash = 31 * hash + thermalBoundaryCondition.hashCode();
		hash = 31 * hash + otherBoundaryConditions.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this EdgeProperties and
	 * another. It returns true if they are equal and false if they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other Object that should be compared with this one.
	 * @return True if the Objects are equal, false otherwise.
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
		else if (otherObject != null && otherObject instanceof EdgeProperties) {

			// We can now cast the other object.
			EdgeProperties conditions = (EdgeProperties) otherObject;

			// Compare the values between the two objects.
			equals = (fluidBoundaryCondition
					.equals(conditions.fluidBoundaryCondition)
					&& thermalBoundaryCondition
							.equals(conditions.thermalBoundaryCondition) && otherBoundaryConditions
					.equals(conditions.otherBoundaryConditions));
		}

		return equals;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a EdgeProperties into the current
	 * object using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param properties
	 *            The Object from which the values should be copied.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(EdgeProperties properties) {
		// begin-user-code

		// Check the parameters.
		if (properties == null) {
			return;
		}

		/* ---- Deep copy the boundary conditions. ---- */
		// Clone the fluid boundary condition.
		fluidBoundaryCondition = (BoundaryCondition) properties.fluidBoundaryCondition
				.clone();

		// Clone the thermal boundary condition.
		thermalBoundaryCondition = (BoundaryCondition) properties.thermalBoundaryCondition
				.clone();

		// Clone and add each of the passive scalar boundary conditions.
		otherBoundaryConditions.clear();
		for (BoundaryCondition condition : properties.otherBoundaryConditions) {
			// Clone non-null boundary conditions.
			if (condition != null) {
				condition = (BoundaryCondition) condition.clone();
			}
			otherBoundaryConditions.add(condition);
		}
		/* -------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the EdgeProperties using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The new clone.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		EdgeProperties object = new EdgeProperties();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Loads--or unmarshals--an EdgeProperties from XML. Required by the
	 * Persistable interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            An input stream from which the persistable should be loaded.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code

		// Initialize JAXBManipulator
		ICEJAXBManipulator jaxbManipulator = new ICEJAXBManipulator();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream
		Object dataObject;

		try {
			dataObject = jaxbManipulator.read(this.getClass(), inputStream);
			// Copy contents of new object into current data structure
			this.copy((EdgeProperties) dataObject);

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
	 * Persists--or marshals--an EdgeProperties to XML. Required by the
	 * Persistable interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param outputStream
	 *            An output stream to which the object should be stored.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void persistToXML(OutputStream outputStream) {
		// begin-user-code

		// Initialize JAXBManipulator
		ICEJAXBManipulator jaxbManipulator = new ICEJAXBManipulator();

		// Call the write() on jaxbManipulator to write to outputStream
		try {
			jaxbManipulator.write(this, outputStream);
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

}
