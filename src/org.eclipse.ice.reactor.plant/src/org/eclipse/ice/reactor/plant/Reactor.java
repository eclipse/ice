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
package org.eclipse.ice.reactor.plant;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Describes reactor parameters.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Reactor extends PlantComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Core channel(s) contained in the reactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<CoreChannel> coreChannels;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Null constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param channels
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Reactor() {
		super();

		coreChannels = new ArrayList<CoreChannel>();

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The core channel(s) contained in the reactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param channels
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Reactor(ArrayList<CoreChannel> channels) {
		// begin-user-code
		super();

		if (channels != null) {
			coreChannels = channels;
		} else {
			coreChannels = new ArrayList<CoreChannel>();
		}
		return;
		// end-user-code
	}

	/**
	 * @return the coreChannels
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<CoreChannel> getCoreChannels() {
		// begin-user-code
		return coreChannels;
		// end-user-code
	}

	/**
	 * @param coreChannels
	 *            the coreChannels to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCoreChannels(ArrayList<CoreChannel> coreChannels) {
		// begin-user-code
		this.coreChannels = coreChannels;

		// Notify listeners of the change.
		notifyListeners();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code
		// Ensure the incoming object is not null
		if (otherObject == null) {
			return false;
		}
		// If same object in memory, then equal
		if (this == otherObject) {
			return true;
		}
		// Make sure this is an actual Junction
		if (!(otherObject instanceof Reactor)) {
			return false;
		}
		// Cast the Object as a Junction
		Reactor other = (Reactor) otherObject;

		// Verify PlantComponent and ICEObject data are equal
		// and the inputs and outputs are the same
		return super.equals(other) && other.coreChannels.equals(coreChannels);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code
		Reactor temp = new Reactor();
		temp.copy(this);
		return temp;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Reactor otherObject) {
		// begin-user-code
		// Make sure other is not null
		if (otherObject == null) {
			return;
		}
		// Copy the PlantComponent and ICEObject data
		super.copy(otherObject);

		// Copy the input and output data
		coreChannels = otherObject.coreChannels;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		int hash = super.hashCode();
		hash = 31 * hash + coreChannels.hashCode();
		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Accepts PlantComponentVisitors to reveal the type of a PlantComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IPlantComponentVisitor visitor) {
		// begin-user-code
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The operation loads the component from the XML stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            The stream containing the SML for this object.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code

		// end-user-code
	}
}