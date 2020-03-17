/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.renderer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * TODO: Add tests, documentation, validation, Validator exclusion from JSON, also tags and other features from ICE's Entry, and logging.
 * 
 * Move the Offset and DataFile test classes to the test bundle.
 * 
 * Rename everything to org.eclipse.ice.*
 * 
 * Add license headers, etc.
 * 
 * @author Jay Jay Billings
 *
 */
public class DataElement<T> {

	T data;
	private String description;
	private String name;

	public T getData() {
		return data;
	}

	public void setData(final T value) {
		data = value;
	}

	@Override
	public String toString() {

		String value = null;
		// Convert to json using Jackson
		ObjectMapper mapper = new ObjectMapper();
		try {
			value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return value;
//				.convertValue(this, new TypeReference<Map<String, Object>>() {
//		}).toString();
	}

	public void fromString(final String value) {
		
		// The question here is can we load the data value from json?
		
		// 1.) Jackson needs to know the class type of "data", but otherwise it can load everything else fine.
		// 2.) Probably need to do a two step process:
		//     a.) Pull DataElement properties from json and set directly - strings are strings after all
		//     b.) Use Jackson ObjectMapper.convertValue() to convert data member of type T from json - How to get .class?
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("value = " + value);
			JsonNode rootNode = mapper.readTree(value);
			System.out.println(rootNode);
			System.out.println("data = " + rootNode.get("data"));
			JsonNode dataNode = rootNode.get("data");
			
		    data = (T) mapper.treeToValue(dataNode, data.getClass());
		
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Validator getValidator() {
		return null;
	}

	public void setValidator(Validator validator) {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String desc) {
		description = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(final String elemName) {
		name = elemName;
	}

}
