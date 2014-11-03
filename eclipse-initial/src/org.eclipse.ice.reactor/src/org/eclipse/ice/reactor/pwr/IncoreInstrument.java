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
package org.eclipse.ice.reactor.pwr;

import java.util.ArrayList;

import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.Ring;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The IncoreInstrument class represents instruments (or detectors) that are
 * used for power distribution monitoring inside of a PWReactor. This class
 * contains a ring of data designed to be the "thimble" for material composition
 * on this class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class IncoreInstrument extends LWRComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An empty thimble tube used a boundary between the detector and the
	 * reactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Ring thimble;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IncoreInstrument() {
		// begin-user-code
		// set default values
		this.setName("Instrument 1");
		this.setDescription("Default Instrument");
		this.setId(1);

		thimble = new Ring("thimble");

		this.HDF5LWRTag = HDF5LWRTagType.INCORE_INSTRUMENT;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name of this IncoreInstrument.
	 *            </p>
	 * @param thimble
	 *            <p>
	 *            An empty thimble tube used a boundary between the detector and
	 *            the reactor.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IncoreInstrument(String name, Ring thimble) {
		// begin-user-code
		// call nullary constructor
		this();
		this.setName(name);
		this.setThimble(thimble);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets an empty thimble tube used a boundary between the detector and the
	 * reactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param thimble
	 *            <p>
	 *            An empty thimble tube used a boundary between the detector and
	 *            the reactor.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setThimble(Ring thimble) {
		// begin-user-code
		// if thimble is not Null set value
		if (thimble != null) {
			this.thimble = thimble;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an empty thimble tube used a boundary between the detector and
	 * the reactor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         An empty thimble tube used a boundary between the detector and
	 *         the reactor.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring getThimble() {
		// begin-user-code
		return this.thimble;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren() {
		// begin-user-code

		// Get the children in super
		ArrayList<IHdfWriteable> children = super.getWriteableChildren();

		// If super had no children
		if (children == null) {

			// Initialize to new array list
			children = new ArrayList<IHdfWriteable>();
		}

		// Add the thimble Ring to children
		children.add(this.thimble);

		return children;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iHdfReadable
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readChild(IHdfReadable iHdfReadable) {
		// begin-user-code

		// If the child is null or not an instance of LWRComponent, then return
		// false.
		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// If this is a Ring
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.RING) {

			// Assign to correct object
			this.thimble = (Ring) childComponent;

		}

		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code
		IncoreInstrument instrument;
		boolean retVal = false;

		// If the same ref on heap, return
		if (otherObject == this) {
			return true;
		}

		// Make sure the otherObject is not null and of the same type as this
		// object
		if (otherObject != null && otherObject instanceof IncoreInstrument) {

			// Cast it
			instrument = (IncoreInstrument) otherObject;

			// check values
			retVal = (super.equals(otherObject) && this.thimble
					.equals(instrument.thimble));

		}

		// Return retVal
		return retVal;

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
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = super.hashCode();

		// Local contents of the hash
		hash += 31 * this.thimble.hashCode();

		// Return the hash
		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(IncoreInstrument otherObject) {
		// begin-user-code

		// If null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.thimble = (Ring) otherObject.thimble.clone();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		IncoreInstrument instrument = new IncoreInstrument();

		// Copy contents
		instrument.copy(this);

		// Return newly instantiated object
		return instrument;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(ILWRComponentVisitor visitor) {
		// begin-user-code
		visitor.visit(this);
		// end-user-code
	}
}