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
package org.eclipse.ice.datastructures.test;

import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tool for testing whether an IShapeVisitor is visited by an IShape
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestShapeVisitor implements IShapeVisitor {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The number of visits from an element to an instance of this class
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int visits;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the number of visits to 0
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestShapeVisitor() {
		// begin-user-code

		// Reset the visit count
		resetVisits();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the number of visits to an instance of this class
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The number of visits from an element to an instance of this class
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getVisits() {
		// begin-user-code
		return visits;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Resets the number of visits to 0
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void resetVisits() {
		// begin-user-code
		visits = 0;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShapeVisitor#visit(ComplexShape complexShape)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ComplexShape complexShape) {
		// begin-user-code
		visits++;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShapeVisitor#visit(PrimitiveShape primitiveShape)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PrimitiveShape primitiveShape) {
		// begin-user-code
		visits++;
		// end-user-code
	}
}