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

/** 
 * <!-- begin-UML-doc -->
 * <p>Simulates the fluid flow associated with a solid heat structure part.</p>
 * <!-- end-UML-doc -->
 * @author w5q
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CoreChannel extends Pipe {

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Nullary constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public CoreChannel() {
		// begin-user-code

		// Call super constructor.
		super();
		
		// Set the name, description and ID.
		setName("Core Channel 1");
		setDescription("Core channel component for reactors");
		setId(1);	
		
		return;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Performs an equality check between two Objects.</p>
	 * <!-- end-UML-doc -->
	 * @param otherObject <p>The other Object to compare against.</p>
	 * @return <p>Returns true if the two objects are equal, otherwise false.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Super's equality check takes care of this.
		return super.equals(otherObject);
		
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Performs a deep copy and returns a newly instantiated Object.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The newly instantiated Object.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code
		
		// Initialize a new object.
		CoreChannel object = new CoreChannel();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Deep copies the contents of otherObject.</p>
	 * <!-- end-UML-doc -->
	 * @param otherObject <p>The other object to copy the contents from.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(CoreChannel otherObject) {
		// begin-user-code
		
		// Super's copy takes care of this.

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the hashCode of the object.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The hashCode of the Object.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		return super.hashCode();
		
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Accepts PlantComponentVisitors to reveal the type of a PlantComponent.</p>
	 * <!-- end-UML-doc -->
	 * @param visitor <p>The PlantComponent's visitor.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IPlantComponentVisitor visitor) {
		// begin-user-code
		
		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The operation loads the component from the XML stream.</p>
	 * <!-- end-UML-doc -->
	 * @param inputStream <p>The stream containing the SML for this object.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}