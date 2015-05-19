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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

/**
 * <p>
 * Base class for RELAP7 components.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
@XmlRootElement(name = "PlantComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantComponent extends ICEObject implements IReactorComponent {

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public PlantComponent() {

		// Set the name, ID and description.
		this.objectName = "Plant Component 1";
		this.uniqueId = 1;
		this.objectDescription = "Plant-level reactor component";

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            Name of the RELAP7 component.
	 *            </p>
	 */
	public PlantComponent(String name) {

		// Call the nullary constructor and then set the name.
		this();
		this.setName(name);

		return;
	}

	// FIXME This documentation needs to be changed.
	/**
	 * <p>
	 * Accepts IComponentVisitor visitors to reveal the type of a
	 * PlantComponent.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 */
	public void accept(IComponentVisitor visitor) {

		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
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
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

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
	}

	/**
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 */
	public Object clone() {

		// Initialize a new object.
		PlantComponent object = new PlantComponent();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 */
	public void copy(PlantComponent otherObject) {

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Call the super's copy method.
		super.copy(otherObject);

		return;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 */
	public int hashCode() {

		// Call the super's hashCode.
		int hash = super.hashCode();

		return hash;
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
	 */
	public String toString() {

		return objectName;

	}
}