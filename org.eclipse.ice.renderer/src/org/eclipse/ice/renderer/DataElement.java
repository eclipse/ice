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

import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

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
	private UUID private_id;
	private String public_id;
	private String comment;
	private ArrayList<String> keywords;
	private String context;
	private boolean required;
	private boolean secret;
	private Validator validator;

	public Properties getProperties() {
		Properties props = new Properties();
		return (Properties) props.clone();
	}
	
	public void setProperty(final String key, final String value) {
		
	}
	
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

	/**
	 * 
	 * @return
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param desc
	 */
	public void setDescription(final String desc) {
		description = desc;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param elemName
	 */
	public void setName(final String elemName) {
		name = elemName;
	}

	/**
	 * @return the public_id
	 */
	public String getPublicId() {
		return public_id;
	}

	/**
	 * @param public_id the public_id to set
	 */
	public void setId(final String public_id) {
		this.public_id = public_id;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * @return the keywords
	 */
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(final ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(final String context) {
		this.context = context;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(final boolean required) {
		this.required = required;
	}

	/**
	 * @return the secret
	 */
	public boolean isSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(final boolean secret) {
		this.secret = secret;
	}

	/**
	 * 
	 * @return
	 */
	public Validator getValidator() {
		return null;
	}

	/**
	 * 
	 * @param validator
	 */
	public void setValidator(Validator validator) {

	}
	
}
