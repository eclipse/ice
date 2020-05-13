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

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a basic data container that conveniently weds the data with
 * co-located metadata such as names, descriptions, ids, and other values.
 * 
 * Data contained by DataElements may be of any type, but they must implement
 * the Serializable interface. There is a general expectation, but no
 * requirement, that T be a POJO with a nullary constructor and other methods
 * such as equals(), clone(), and hashCode().
 * 
 * Basic serialization is available using toString() and fromString(). Valid
 * JSON is produced and required by this class for string serialization.
 * 
 * Clients interact with the data objects directly using the getData() and
 * setData() operations.
 * 
 * General metadata properties for data are managed by this class in a property
 * map including: <br>
 * | Property Name (key) | Description | Default value | <br>
 * |---------------|-------------|---------| <br>
 * | name | a simple name for the data name | "name" | <br>
 * | description | a simple description for the data | "description" | <br>
 * | id | a unique identifier | "0" |<br>
 * | comment | a comment that annotates the data in meaningful way | "no
 * comment" | <br>
 * | context | the context (a tag) in which the data should be considered |
 * "default" | <br>
 * 
 * Clients may retrieve or add their own properties/keywords using the
 * 'setProperty' operation. Accessors are available for the default properties
 * as well, as convenience methods. Clients may also designate whether or not
 * data elements are required and secret. Please note that the convenience
 * methods for default properties throw exceptions to enforce non-null values.
 * 
 * Clients are expected to set their own validators and validators are not
 * initialized by default.
 * 
 * Each DataElement is assigned a universally unique identifier (UUID) which can
 * be retrieved but not set by calling DataElement.getUUID().
 * 
 * Strict deep equality for DataElements requires that their UUIDs match as well
 * as all other members. Thus clients who only want to see if the members match
 * need to call DataElement.matches() instead of DataElement.equals() since the
 * latter checks the private UUID as well.
 * 
 * Please note that because of Java Generics rules the type T cannot be a
 * primitive. If primitives need to be stored, boxed types such as Integer,
 * Double, Boolean, etc. must be used.
 * 
 * It is highly recommended that clients use classes of type T that implement
 * equals(), hashCode(), and clone() to avoid unexpected and unspecified
 * behavior.
 * 
 * @author Jay Jay Billings
 *
 */
public class DataElement<T extends Serializable> implements Serializable {

	/**
	 * An id for the Serializable interface implementation.
	 */
	private static final long serialVersionUID = 3710841338767820983L;

	/**
	 * Logging tool
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataElement.class);

	/**
	 * The data stored in the element
	 */
	private T data;

	/**
	 * Metadata entries for the data
	 */
	private Properties dataProps = new Properties();

	/**
	 * A unique private id that identifies the data element
	 */
	private UUID privateId;

	/**
	 * This value is true if the element should be regarded as a client as required.
	 */
	private boolean required;

	/**
	 * This value is true if the element should be regarded as a secret by a client,
	 * such as for passwords.
	 */
	private boolean secret;

	/**
	 * The validator used to check the correctness of the data
	 */
	private JavascriptValidator<T> validator;

	/**
	 * Default constructor
	 */
	public DataElement() {
		dataProps.setProperty("name", "name");
		dataProps.setProperty("description", "description");
		dataProps.setProperty("id", "0");
		dataProps.setProperty("comment", "no comment");
		dataProps.setProperty("context", "default");
		required = false;
		secret = false;
		privateId = UUID.randomUUID();
	}

	/**
	 * Copy constructor
	 * 
	 * @throws Exception thrown if the element to copy is null because who know what
	 *                   to do in that case?
	 */
	public DataElement(DataElement<T> otherElement) throws Exception {
		if (otherElement != null) {
			this.dataProps = otherElement.dataProps;
			this.required = otherElement.required;
			this.secret = otherElement.secret;
			this.privateId = otherElement.privateId;
			this.validator = otherElement.validator;
			this.data = otherElement.data;
		} else {
			// Complain
			throw (new Exception("Element to copy cannot be null."));
		}
	}

	/**
	 * This operation returns a copy of the property map for this data element. Note
	 * that changing the property map will not change the properties on the data
	 * element by design.
	 * 
	 * @return the property map
	 */
	public Properties getProperties() {
		return (Properties) dataProps.clone();
	}

	/**
	 * This operation sets a property of any value on the data. It is useful for
	 * storing additional metadata properties such as tags or keywords.
	 * 
	 * @param key   the key to associate to the value
	 * @param value the value associated with the key
	 * @throws Exception an exception if thrown if the key or value are null
	 */
	public void setProperty(final String key, final String value) throws Exception {
		checkAndSetStringProperty(key, value);
	}

	/**
	 * This operation returns the data held in the container.
	 * 
	 * @return the data a direct reference handle to the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * This operation sets the value of the data in the container to the value of
	 * the input.
	 * 
	 * @param value the new value of the data. The data is copied from value into
	 *              the container's data.
	 */
	public void setData(final T value) {
		data = value;
	}

	/**
	 * This operation serializes the data element to a string in verified JSON.
	 * 
	 * @return a JSON string describing the element
	 */
	@Override
	public String toString() {

		String value = null;
		// Convert to json using Jackson
		ObjectMapper mapper = new ObjectMapper();
		// Set visibility so that only methods are serialized. This removes duplication
		// otherwise produced due to the convenience methods.
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
		try {
			value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Unable to write DataElement to string!", e);
		}

		return value;
	}

	/**
	 * This operation deserializes a valid JSON string and tries to load it into the
	 * object.
	 * 
	 * @param jsonDataElement the contents of this data element as JSON
	 */
	public void fromString(final String jsonDataElement) {

		// Load the data from the string with Jackson.
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(jsonDataElement);
			// Read each node from the root and write it into the structure. Data...
			JsonNode dataNode = rootNode.get("data");
			data = (T) mapper.treeToValue(dataNode, data.getClass());
			// Properties
			JsonNode propsNode = rootNode.get("dataProps");
			dataProps = mapper.treeToValue(propsNode, Properties.class);
			// Required and secret booleans
			JsonNode requiredNode = rootNode.get("required");
			required = mapper.treeToValue(requiredNode, Boolean.class);
			JsonNode secretNode = rootNode.get("secret");
			secret = mapper.treeToValue(secretNode, Boolean.class);
			// Private ids
			JsonNode idNode = rootNode.get("privateId");
			privateId = mapper.treeToValue(idNode, UUID.class);
			// Validators
			JsonNode validatorNode = rootNode.get("validator");
			validator = mapper.treeToValue(validatorNode, validator.getClass());

		} catch (JsonProcessingException e) {
			logger.error("Unable to read DataElement from string!", e);
		}

		return;
	}

	/**
	 * This is a private convenience function for manipulating the property map
	 * 
	 * @param key
	 * @param value
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	private void checkAndSetStringProperty(String key, String value) throws Exception {
		if (value != null) {
			dataProps.setProperty(key, value);
		} else {
			String err = "Data Element property values for key =" + key + " cannot be null";
			throw new Exception(err);
		}
	}

	/**
	 * Get the simple description of the data, i.e. - "Machine readable data for
	 * COVID-19 research."
	 * 
	 * @return the description of the data
	 */
	public final String getDescription() {
		return dataProps.getProperty("description");
	}

	/**
	 * Set the description of the data
	 * 
	 * @param desc the description
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setDescription(final String desc) throws Exception {
		checkAndSetStringProperty("description", desc);
	}

	/**
	 * This operation returns the simple name of the data
	 * 
	 * @return the name of the data, i.e. - "CORD-19" or "Steve"
	 */
	public String getName() {
		return dataProps.getProperty("name");
	}

	/**
	 * Set the simple name of the element
	 * 
	 * @param elemName a simple name
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setName(final String name) throws Exception {
		checkAndSetStringProperty("name", name);
	}

	/**
	 * Get the public identifier of the data element. This is a common id that may
	 * or may not be unique to this data element.
	 * 
	 * @return the public id
	 */
	public long getId() {
		return Long.valueOf(dataProps.getProperty("id")).longValue();
	}

	/**
	 * Set the public identifier of the data element. This is a common id that may
	 * or may not be unique to this data element.
	 * 
	 * @param public_id the public id
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setId(final long public_id) throws Exception {
		checkAndSetStringProperty("id", String.valueOf(public_id));
	}

	/**
	 * Get the comment/tag that annotates this data. This value is different than
	 * the description in that it provides commentary or a secondary designation in
	 * the form of an annotation for the data. For example, where the description
	 * should generally be useful, this value could simple be "2020Data" or any
	 * other tag of convenience used during processing.
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return dataProps.getProperty("comment");
	}

	/**
	 * Return the comment or tag that annotates the data
	 * 
	 * @param comment the comment to set
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setComment(final String comment) throws Exception {
		checkAndSetStringProperty("comment", comment);
	}

	/**
	 * Get the context of the data in its present state. For example, a single
	 * physical sample may be used across multiple experiments and the context in
	 * one case may be "x-ray scattering" whereas in another it could be "neutron
	 * scattering." Another example is when the same data is being used by two
	 * clients and this value changes from "ornl.gov" to "lbnl.gov" to indicate that
	 * a client should dynamically adapt without changing the data otherwise.
	 * 
	 * @return the context
	 */
	public String getContext() {
		return dataProps.getProperty("context");
	}

	/**
	 * Return the context in which the data exists.
	 * 
	 * @param context the context to set
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setContext(final String context) throws Exception {
		checkAndSetStringProperty("context", context);
	}

	/**
	 * True if the element is required by the client, false otherwise. This is only
	 * for client tracking and may make no sense for different clients.
	 * 
	 * @return true if required, false if not
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * True if the element is required by the client, false otherwise. This is only
	 * for client side tracking and may make no sense for different clients.
	 * 
	 * @param required true if required, false if not
	 */
	public void setRequired(final boolean required) {
		this.required = required;
	}

	/**
	 * True if the element is something that should be secret by the client, false
	 * otherwise. This is only for client tracking and may make no sense for
	 * different clients.
	 * 
	 * @return true if secret, false if not
	 */
	public boolean isSecret() {
		return secret;
	}

	/**
	 * True if the element is something that should be secret by the client, false
	 * otherwise. This is only for client tracking and may make no sense for
	 * different clients.
	 * 
	 * @param secret true if the element should be treated as a secret, false
	 *               otherwise
	 */
	public void setSecret(final boolean secret) {
		this.secret = secret;
	}

	/**
	 * This operation returns the UUID of the data element. The UUID is a private
	 * unique identifier assigned to all data elements.
	 * 
	 * @return the UUID for this element
	 */
	public UUID getUUID() {
		return privateId;
	}

	/**
	 * This function checks deep equality of DataElements to see if all members are
	 * equal ("match") with the exception of the private UUID. This is important for
	 * checking if two objects were generated separately but are otherwise equal.
	 * 
	 * @param otherElement the other element to compare
	 * @return true if all members of the element except the UUID match this
	 *         element.
	 */
	public boolean matches(DataElement<T> otherElement) {

		boolean retValue = false;

		// Outer check for null comparisons
		if (otherElement != null) {
			// Next check for shallow comparison
			if (this != otherElement) {
				// Do deep comparison for properties that are always allocated
				boolean propsMatch = this.dataProps.equals(otherElement.dataProps);
				boolean requiredMatch = this.required == otherElement.required;
				boolean secretsMatch = this.secret == otherElement.secret;

				// Since the validator can be null, it needs to be checked for shallow and deep
				// inequality
				boolean validatorsMatch = (this.validator != null) ? this.validator.equals(otherElement.validator)
						: this.validator == otherElement.validator;
				// Data can also be null - but generally shouldn't - so we can accept the NPE
				// here.
				boolean dataMatch = this.data.equals(otherElement.data);
				retValue = propsMatch && secretsMatch && validatorsMatch && dataMatch && requiredMatch;
			} else {
				// This should be true if they are the same because the deep comparison is
				// performed otherwise.
				retValue = true;
			}
		}

		return retValue;
	}

	/**
	 * This performs a deep comparison of the two objects in addition to the shallow
	 * comparison of java.lang.Object.
	 * 
	 * See {@link java.lang.Object#equals(Object)}.
	 */
	@Override
	public boolean equals(Object otherObject) {

		boolean retValue = false;

		// Check type before moving on
		if (otherObject instanceof DataElement<?>) {
			DataElement<T> otherElement = (DataElement<T>) otherObject;
			// Check the private UUIDs, which can be null and have to be checked as such.
			boolean privateIdsMatch = (this.privateId != null) ? this.privateId.equals(otherElement.privateId)
					: this.privateId == otherElement.privateId;
			// Check for matching and then check the UUIDs.
			retValue = this.matches(otherElement) && privateIdsMatch;
		}

		return retValue;
	}

	/**
	 * See {@link java.lang.Object#hashCode()}.
	 */
	@Override
	public int hashCode() {
		// Using a somewhat generic and common technique for computing the hash code
		// here. It matches the version in the old ICE 2.x product line, but I
		// incremented the initial hash seed to 31 from 11 since this is for version 3.
		int hash = 31;
		// The 31 below is just coincidental and part of the original source where I
		// read
		// about hash codes.
		hash = 31 * hash + privateId.hashCode();
		hash = 31 * hash + dataProps.hashCode();
		hash = 31 * hash + validator.hashCode();
		hash = 31 * hash + Boolean.valueOf(required).hashCode();
		hash = 31 * hash + Boolean.valueOf(secret).hashCode();
		hash = 31 * hash + data.hashCode();

		return hash;
	}

	/**
	 * This operation clones the object. Note that it differs from the base class
	 * implementation in that it will return null if it cannot create the clone to
	 * promote fast failure. See {@link java.lang.Object#clone()};
	 */
	@Override
	public Object clone() {
		try {
			// Call the copy constructor to create the clone.
			return new DataElement<T>(this);
		} catch (Exception e) {
			logger.error("Unable to clone DataElement!", e);
			return null;
		}
	}

	/**
	 * This operation returns the validator that has been configured for this
	 * element.
	 * 
	 * @return the validator or null if it has not been set
	 */
	public JavascriptValidator<T> getValidator() {
		return validator;
	}

	/**
	 * This operation sets the validator associated with this element.
	 * 
	 * @param validator the validator or null to reset the reference
	 */
	public void setValidator(JavascriptValidator<T> validator) {
		this.validator = validator;
	}

}
