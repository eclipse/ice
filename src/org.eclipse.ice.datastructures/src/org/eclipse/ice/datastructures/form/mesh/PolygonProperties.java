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
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A container for the material ID and group number of every polygon, as defined
 * in the MESH DATA section of a Nek5000 reafile. Example:
 * </p>
 * <code>
 * >            ELEMENT      44 [    1R]  GROUP  0
 * </code>
 * <p>
 * For this Polygon, materialId = "1R", groupNum = 0.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "PolygonProperties")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolygonProperties implements Persistable {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Material ID of the polygon. Must be between 1-4 chars long.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private String materialId;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Group number of the polygon. Must be no more than 5 digits.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private int groupNum;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A nullary constructor that sets default values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PolygonProperties() {
		// begin-user-code
		materialId = "nul1";
		groupNum = 0;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default, parameterized constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            <p>
	 *            Material ID of the polygon. Must be between 1-4 chars long.
	 *            </p>
	 * @param group
	 *            <p>
	 *            Group number of the polygon. Must be no more than 5 digits.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PolygonProperties(String id, int group) {
		// begin-user-code

		this();

		// Check for valid values
		if (id != null && id.length() <= 4 && group <= 99999) {
			materialId = id;
			groupNum = group;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the material ID as a string.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The material ID of the polygon.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getMaterialId() {
		// begin-user-code
		return materialId;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the group number as an int.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The group number of the polygon.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getGroupNum() {
		// begin-user-code
		return groupNum;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this PolygonProperties
	 * and another PolygonProperties. It returns true if they are equal and
	 * false if they are not.
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
	 * 
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
		else if (otherObject != null
				&& otherObject instanceof PolygonProperties) {

			// We can now cast the other object.
			PolygonProperties properties = (PolygonProperties) otherObject;

			// Compare the values between the two objects.
			equals = (materialId.equals(properties.materialId) && groupNum == properties.groupNum);
		}

		return equals;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the PolygonProperties.
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

		// Add local hashes.
		int hash = materialId.hashCode();
		hash = 31 * hash + groupNum;

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the PolygonProperties using a deep
	 * copy.
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
		PolygonProperties object = new PolygonProperties();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a PolygonProperties into the
	 * current object using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param properties
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(PolygonProperties properties) {
		// begin-user-code

		// Check the parameters.
		if (properties == null) {
			return;
		}

		materialId = properties.materialId;
		groupNum = properties.groupNum;

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Persistable#loadFromXML(InputStream inputStream)
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
			this.copy((PolygonProperties) dataObject);

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
	 * (non-Javadoc)
	 * 
	 * @see Persistable#persistToXML(OutputStream outputStream)
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
