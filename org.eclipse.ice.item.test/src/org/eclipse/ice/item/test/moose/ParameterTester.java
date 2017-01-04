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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.item.utilities.moose.Parameter;
import org.eclipse.january.form.DiscreteEntry;
import org.eclipse.january.form.IEntry;
import org.junit.Test;

/**
 * This operation checks the MOOSE Parameter class.
 * 
 * @author Jay Jay Billings
 */
public class ParameterTester {
	
	/**
	 * This operation checks the accessors for the Parameter class.
	 */
	@Test
	public void checkAccessors() {

		// Local Declarations
		String name = "Clara", description = "Companion", defaultValue = "Oswin";
		String comment = "Souffle Girl", group_name = "Oswald", cpp_type = "MooseEnum";
		boolean required = true;
		String options = "option1 option2 option3";
		Parameter param = new Parameter();

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setComment(comment);
		param.setGroup_name(group_name);
		param.setRequired(required);
		param.setOptions(options);

		// Check the parameter
		assertEquals(name, param.getName());
		assertEquals(description, param.getDescription());
		assertEquals(comment, param.getComment());
		assertEquals(group_name, param.getGroup_name());
		assertEquals(required, param.isRequired());
		assertEquals(cpp_type, param.getCpp_type());
		assertEquals(defaultValue, param.getDefault());
		assertEquals(param.getOptions().toString(), "[option1, option2, option3]");

		// Check switching the required mode off
		param.setRequired(false);
		assertEquals(false, param.isRequired());

		return;
	}

	/**
	 * This operation checks the ability of the Parameter to write itself to a
	 * ICE Entry.
	 */
	@Test
	public void checkToEntry() {

		// Local Declarations
		String name = "Souffle Girl", description = "Companion", defaultValue = "option1";
		String group_name = "Clara", cpp_type = "MooseEnum", comment = "Some comment";
		boolean required = true;
		String options = "option1 option2 option3";
		Parameter param = new Parameter();

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setComment(comment);
		param.setGroup_name(group_name);
		param.setRequired(required);
		param.setOptions(options);

		// Get the Entry
		IEntry entry = param.toEntry();

		// Check the Entry
		assertEquals(name, entry.getName());
		assertEquals(description, entry.getDescription());
		assertEquals(comment, entry.getComment());
		assertEquals(defaultValue, entry.getDefaultValue());
		assertEquals(required, entry.isRequired());
		assertTrue(entry instanceof DiscreteEntry);
		assertEquals(entry.getAllowedValues().toString(), 
										"[option1, option2, option3]");

		return;
	}

	/**
	 * This operation checks the ability of the parameter to write itself as a
	 * string.
	 */
	@Test
	public void checkToString() {

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
	 * This operation checks the Parameter to make sure that it can be loaded
	 * from an Entry.
	 */
	@Test
	public void checkFromEntry() {

		// Local Declarations
		String name = "Amy", description = "Companion", defaultValue = "option2";
		String group_name = "Pond", cpp_type = "MooseEnum", comment = "Scottish";
		boolean required = true;
		String options = "option1 option2 option3";
		Parameter param = new Parameter(), testParam = null;
		IEntry paramEntry = null;

		// Setup the parameter
		param.setName(name);
		param.setCpp_type(cpp_type);
		param.setDefault(defaultValue);
		param.setDescription(description);
		param.setComment(comment);
		param.setGroup_name(group_name);
		param.setRequired(required);
		param.setOptions(options);

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
		assertEquals(param.getComment(), testParam.getComment());
		assertEquals(param.getDefault(), testParam.getDefault());
		assertEquals(param.isRequired(), testParam.isRequired());
		assertEquals(param.getOptions(), testParam.getOptions());

		return;
	}
}
