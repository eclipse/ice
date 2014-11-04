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
package org.eclipse.ice.item.test;

import org.eclipse.ice.datastructures.form.DataComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The FakeDataComponent class is a subclass of DataComponent that is used for
 * testing.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeDataComponent extends DataComponent {

	// Updated value
	String value;

	@Override
	public void update(String key, String newValue) {
		this.value = newValue;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the value that was passed to the update operation
	 * inherited from DataComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The value submitted to the update operation.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getUpdatedValue() {
		// begin-user-code
		return value;
		// end-user-code
	}

}