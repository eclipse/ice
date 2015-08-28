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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.utilities.trilinos.Parameter;
import org.eclipse.ice.item.utilities.trilinos.ParameterList;
import org.junit.Test;

/**
 * This class checks the Parameter and ParameterList classes to make sure that
 * their toEntry() and toTreeComposite() operations work as described.
 */
public class ParameterListTester {
	/**
	 * <p>
	 * This operation checks the parameter classes toEntry() operation.
	 * </p>
	 * 
	 */
	@Test
	public void checkParameter() {

		// Local Declarations
		Parameter param = null, retParam = null;

		// Create a parameter
		param = new Parameter();
		param.name = "MyParameter";
		param.value = "0.1";
		param.type = "double";

		// Create an Entry from the parameter - start with double
		Entry parameterEntry = param.toEntry();
		// Check the name
		assertEquals(param.name, parameterEntry.getName());
		// Check the value
		assertEquals(param.value, parameterEntry.getDefaultValue());
		assertEquals(param.value, parameterEntry.getValue());
		// Check the description
		assertEquals(param.name, parameterEntry.getDescription());
		// Check the AllowedValueType
		assertEquals(AllowedValueType.Continuous, parameterEntry.getValueType());
		// Check the bounds
		assertTrue(parameterEntry.getAllowedValues().contains(
				String.valueOf(Double.POSITIVE_INFINITY)));
		assertTrue(parameterEntry.getAllowedValues().contains(
				String.valueOf(Double.NEGATIVE_INFINITY)));
		// Create parameter from this Entry
		retParam = new Parameter();
		retParam.fromEntry(parameterEntry);
		// And check it
		assertEquals(param.name, retParam.name);
		assertEquals(param.type, retParam.type);
		assertEquals(param.value, retParam.value);

		// Change the value and type to check integers
		param.type = "int";
		param.value = "1";
		parameterEntry = param.toEntry();
		// Check the AllowedValueType
		assertEquals(AllowedValueType.Continuous, parameterEntry.getValueType());
		// Check the bounds
		assertTrue(parameterEntry.getAllowedValues().contains(
				String.valueOf(Double.POSITIVE_INFINITY)));
		assertTrue(parameterEntry.getAllowedValues().contains(
				String.valueOf(Double.NEGATIVE_INFINITY)));
		// Create parameter from this Entry
		retParam = new Parameter();
		retParam.fromEntry(parameterEntry);
		// And check it
		assertEquals(param.name, retParam.name);
		assertEquals(param.type, retParam.type);
		assertEquals(param.value, retParam.value);

		// Change the value and type to check booleans
		param.type = "bool";
		param.value = "true";
		parameterEntry = param.toEntry();
		// Check the AllowedValueType
		assertEquals(AllowedValueType.Discrete, parameterEntry.getValueType());
		// Check the bounds
		assertTrue(parameterEntry.getAllowedValues().contains("true"));
		assertTrue(parameterEntry.getAllowedValues().contains("false"));
		// Create parameter from this Entry
		retParam = new Parameter();
		retParam.fromEntry(parameterEntry);
		// And check it
		assertEquals(param.name, retParam.name);
		assertEquals(param.type, retParam.type);
		assertEquals(param.value, retParam.value);

		// Change the value and type to check strings
		param.type = "string";
		param.value = "myString";
		parameterEntry = param.toEntry();
		// Check the AllowedValueType
		assertEquals(AllowedValueType.Undefined, parameterEntry.getValueType());
		// Create parameter from this Entry
		retParam = new Parameter();
		retParam.fromEntry(parameterEntry);
		// And check it
		assertEquals(param.name, retParam.name);
		assertEquals(param.type, retParam.type);
		assertEquals(param.value, retParam.value);

		// Change the value and type to check arrays
		param.type = "Array(string)";
		param.value = "{1,1,1,1}";
		parameterEntry = param.toEntry();
		// Check the AllowedValueType
		assertEquals(AllowedValueType.Undefined, parameterEntry.getValueType());
		// Create parameter from this Entry
		retParam = new Parameter();
		retParam.fromEntry(parameterEntry);
		// And check it
		assertEquals(param.name, retParam.name);
		assertEquals(param.type, retParam.type);
		assertEquals(param.value, retParam.value);

		return;
	}

	/**
	 * <p>
	 * This operation checks the ParameterLists toTreeComposite() operation.
	 * </p>
	 * 
	 */
	@Test
	public void checkParameterList() {

		// Create a couple of parameter lists
		ParameterList pList = new ParameterList();
		ParameterList secondPList = new ParameterList();
		ParameterList thirdPList = new ParameterList();
		ParameterList retParamList = new ParameterList();
		ArrayList<Parameter> retParams = new ArrayList<Parameter>();
		int numParameters = 10;

		// Set the names
		pList.name = "MyParameterList";
		secondPList.name = "MySecondParameterList";
		thirdPList.name = "MyThirdParameterList";

		// Create parameters
		pList.parameters = new ArrayList<Parameter>();
		for (int i = 0; i < numParameters; i++) {
			Parameter param = new Parameter();
			param.name = (new Integer(i)).toString();
			param.value = param.name;
			param.type = "string";
			pList.parameters.add(param);
		}

		// Add the second parameter list as a child to the first
		pList.parameterLists = new ArrayList<ParameterList>();
		pList.parameterLists.add(secondPList);

		// Add the third parameter list as a child to the second
		secondPList.parameterLists = new ArrayList<ParameterList>();
		secondPList.parameterLists.add(thirdPList);

		// Get the TreeComposite
		TreeComposite pListTreeComp = pList.toTreeComposite();

		// Check the name and description
		assertEquals(pList.name, pListTreeComp.getName());
		assertEquals(pList.name, pListTreeComp.getDescription());
		// Make sure there is a component available
		assertNotNull(pListTreeComp.getDataNodes());
		assertEquals(1, pListTreeComp.getDataNodes().size());
		assertTrue(pListTreeComp.getDataNodes().get(0) instanceof DataComponent);
		// Get the data component and check out its info
		DataComponent dataComp = (DataComponent) pListTreeComp.getDataNodes()
				.get(0);
		assertEquals(pList.name + " Parameters", dataComp.getName());
		assertEquals(pList.name + " Parameters", dataComp.getDescription());
		// Check out the entries on the DataComponent
		assertEquals(10, dataComp.retrieveAllEntries().size());
		for (int i = 0; i < numParameters; i++) {
			assertEquals((new Integer(i)).toString(), dataComp
					.retrieveAllEntries().get(i).getName());
			assertEquals(i + 1, dataComp.retrieveAllEntries().get(i).getId());
		}
		// Make sure the sub-tree is available
		assertNotNull(pListTreeComp.getChildAtIndex(0));
		assertTrue(pListTreeComp.getChildAtIndex(0) instanceof TreeComposite);
		TreeComposite retSecondPListTreeComp = pListTreeComp.getChildAtIndex(0);
		// Check the name
		assertEquals(secondPList.name, retSecondPListTreeComp.getName());
		// Make sure the sub-tree of the sub-tree is available
		assertNotNull(retSecondPListTreeComp.getChildAtIndex(0));
		assertTrue(retSecondPListTreeComp.getChildAtIndex(0) instanceof TreeComposite);
		TreeComposite retThirdPListTreeComp = retSecondPListTreeComp
				.getChildAtIndex(0);
		// Check the name
		assertEquals(thirdPList.name, retThirdPListTreeComp.getName());

		// Convert the TreeComposite back to a parameter list
		retParamList.fromTreeComposite(pListTreeComp);

		// Check the name
		assertEquals(pList.name, retParamList.name);
		// Check the parameters
		retParams = retParamList.parameters;
		assertEquals(pList.parameters.size(), retParamList.parameters.size());
		for (int i = 0; i < numParameters; i++) {
			assertEquals(pList.parameters.get(i).name, retParams.get(i).name);
			assertEquals(pList.parameters.get(i).value, retParams.get(i).value);
			assertEquals(pList.parameters.get(i).type, retParams.get(i).type);
		}
		// Check the children
		assertEquals(pList.parameterLists.size(),
				retParamList.parameterLists.size());
		assertEquals(pList.parameterLists.get(0).name,
				retParamList.parameterLists.get(0).name);
		assertEquals(secondPList.parameterLists.size(),
				retParamList.parameterLists.get(0).parameterLists.size());
		assertEquals(secondPList.parameterLists.get(0).name,
				retParamList.parameterLists.get(0).parameterLists.get(0).name);

		return;
	}

	/**
	 * This operation checks that a parameter lists can be loaded to and from
	 * XML properly.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkXMLPersistence() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = null;
		Parameter param = new Parameter(), loadedParam = null, secondParam = new Parameter();
		ParameterList pList = new ParameterList(), secondPList = new ParameterList();
		ParameterList loadedList = null, secondLoadedList = null;
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(ParameterList.class);

		System.out.println("----- Testing Parameter XML Persistence -----");

		// Create a parameter
		param.name = "A parameter";
		param.value = "only simple bits and bites";
		param.type = "string";

		// Write it to XML
		xmlHandler.write(param, classList, outputStream);

		// Read it from XML
		inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		loadedParam = new Parameter();
		loadedParam = (Parameter) xmlHandler.read(classList, inputStream);

		// Check the name
		assertEquals(param.name, loadedParam.name);
		// Check the value
		assertEquals(param.value, loadedParam.value);
		// Check the type
		assertEquals(param.type, loadedParam.type);

		// Clear the streams
		try {
			outputStream.flush();
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("----- Testing ParameterList XML Persistence -----");

		// Setup the second parameter
		secondParam.name = "that took Jay all night";
		secondParam.value = "-----";
		secondParam.type = "string";

		// Setup the parameter lists
		pList.name = "ICE's Parameter List";
		pList.parameters = new ArrayList<Parameter>();
		pList.parameters.add(param);
		secondPList.name = "ICE's Second Parameter List";
		secondPList.parameters = new ArrayList<Parameter>();
		secondPList.parameters.add(secondParam);
		pList.parameterLists = new ArrayList<ParameterList>();
		pList.parameterLists.add(secondPList);

		// Write the tree
		outputStream = new ByteArrayOutputStream();
		xmlHandler.write(pList, classList, outputStream);

		// Load the tree
		inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		loadedList = new ParameterList();
		loadedList = (ParameterList) xmlHandler.read(classList, inputStream);

		// Check the name
		assertEquals(pList.name, loadedList.name);
		// Check the parameter for the top level tree
		assertNotNull(loadedList.parameters.size());
		assertEquals(1, loadedList.parameters.size());
		assertEquals(param.name, loadedList.parameters.get(0).name);
		assertEquals(param.value, loadedList.parameters.get(0).value);
		assertEquals(param.type, loadedList.parameters.get(0).type);
		// Check the second level parameter list in the tree
		assertNotNull(loadedList.parameterLists);
		assertEquals(1, loadedList.parameterLists.size());
		secondLoadedList = loadedList.parameterLists.get(0);
		// Check the parameter for the second level tree
		assertNotNull(secondLoadedList.parameters.size());
		assertEquals(1, secondLoadedList.parameters.size());
		assertEquals(secondParam.name, secondLoadedList.parameters.get(0).name);
		assertEquals(secondParam.value,
				secondLoadedList.parameters.get(0).value);
		assertEquals(secondParam.type, secondLoadedList.parameters.get(0).type);

		return;
	}
}