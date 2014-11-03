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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Base class for RELAP7 components.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */

@Entity()
@Table(name = "PlantComponent")
@XmlRootElement(name = "PlantComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantComponent extends ICEObject implements IReactorComponent {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PlantComponent() {
		// begin-user-code

		// Set the name, ID and description.
		this.objectName = "Plant Component 1";
		this.uniqueId = 1;
		this.objectDescription = "Plant-level reactor component";

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            Name of the RELAP7 component.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PlantComponent(String name) {
		// begin-user-code

		// Call the nullary constructor and then set the name.
		this();
		this.setName(name);

		return;
		// end-user-code
	}

	// FIXME This documentation needs to be changed.
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Accepts IComponentVisitor visitors to reveal the type of a
	 * PlantComponent.
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
	public void accept(IComponentVisitor visitor) {
		// begin-user-code

		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}

	/**
	 * Sub-classes will need to override this method so that the visit operation
	 * will work correctly.
	 * 
	 * @param visitor
	 *            The IPlantComponentVisitor that is visiting this
	 *            PlantComponent.
	 */
	public void accept(IPlantComponentVisitor visitor) {
		// Sub-classes need to override and implement this method.
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

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}

		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof PlantComponent) {

			// Cast the other object to an PlantComponent.
			PlantComponent component = (PlantComponent) otherObject;

			// Call the super's equality method.
			equals = super.equals(component);
		}

		return equals;
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

		// Initialize a new object.
		PlantComponent object = new PlantComponent();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
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
	public void copy(PlantComponent otherObject) {
		// begin-user-code

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Call the super's copy method.
		super.copy(otherObject);

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

		// Call the super's hashCode.
		int hash = super.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Gets a String representation of the
	 * PlantComponent. <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The name of the PlantComponent as a String.
	 *         </p>
	 * @see <p>
	 *      IReactorComponent#toString()
	 *      </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String toString() {
		// begin-user-code

		return objectName;

		// end-user-code
	}
}