/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test.moose;

import static org.junit.Assert.*;

import org.junit.Test;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.item.utilities.moose.Parameter;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This operation checks the MOOSE Parameter class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ParameterTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the accessors for the Parameter class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAccessors() {
		// begin-user-code

		// Local Declarations
		String name = "Clara", description = "Companion", defaultValue = "Oswin";
		String group_name = "Oswald", cpp_type = "std::string";
		boolean required = true;
		Parameter param = new Parameter();

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setGroup_name(group_name);
		param.setRequired(required);

		// Check the parameter
		assertEquals(name, param.getName());
		assertEquals(description, param.getDescription());
		assertEquals(group_name, param.getGroup_name());
		assertEquals(required, param.isRequired());
		assertEquals(cpp_type, param.getCpp_type());
		assertEquals(defaultValue, param.getDefault());

		// Check switching the required mode off
		param.setRequired(false);
		assertEquals(false, param.isRequired());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the Parameter to write itself to a
	 * ICE Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkToEntry() {
		// begin-user-code

		// Local Declarations
		String name = "Souffle Girl", description = "Companion", defaultValue = "Oswin";
		String group_name = "Clara", cpp_type = "std::string";
		boolean required = true;
		Parameter param = new Parameter();

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setGroup_name(group_name);
		param.setRequired(required);

		// Get the Entry
		Entry entry = param.toEntry();

		// Check the Entry
		assertEquals(name, entry.getName());
		assertEquals(description, entry.getDescription());
		assertEquals(defaultValue, entry.getDefaultValue());
		assertEquals(required, entry.isRequired());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the parameter to write itself as a
	 * string.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkToString() {
		// begin-user-code

		// Local Declarations
		String name = "Clara", description = "Companion", defaultValue = "Oswin";
		String group_name = "Oswald", cpp_type = "std::string";
		boolean required = true;
		Parameter param = new Parameter();

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setGroup_name(group_name);
		param.setRequired(required);

		// Check the string
		assertEquals(name + " = " + defaultValue, param.toString());

		return;
		// end-user-code
	}

	/**
	 * This operation checks the Parameter class to make sure that it can be
	 * loaded from a string.
	 */
	@Test
	public void checkFromString() {

		// Local Declarations
		String name = "Clara", defaultValue = "Companion";
		String paramString = name + " = " + defaultValue;
		Parameter param = new Parameter();

		// Load the parameter
		param.fromString(paramString);
		// Check the parameter
		assertEquals(name, param.getName());
		assertEquals(defaultValue, param.getDefault());

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Parameter to make sure that it can be loaded
	 * from an Entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFromEntry() {
		// begin-user-code

		// Local Declarations
		String name = "Amy", description = "Companion", defaultValue = "Pond";
		String group_name = "Pond", cpp_type = "std::string";
		boolean required = true;
		Parameter param = new Parameter(), testParam = null;
		Entry paramEntry = null;

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setGroup_name(group_name);
		param.setRequired(required);

		// Create an Entry from the parameter
		paramEntry = param.toEntry();
		assertNotNull(paramEntry);

		// Load the test parameter
		testParam = new Parameter();
		testParam.fromEntry(paramEntry);

		// Check the Parameter. Only the name, description and value are
		// converted.
		assertEquals(param.getName(), testParam.getName());
		assertEquals(param.getDescription(), testParam.getDescription());
		assertEquals(param.getDefault(), testParam.getDefault());
		assertEquals(param.isRequired(), testParam.isRequired());

		return;
		// end-user-code
	}
}