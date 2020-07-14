package org.eclipse.ice.dev.annotations;

import java.util.Map;
import java.util.UUID;

/**
 * Marker interface for DataElements.
 */
public interface IDataElement<T> {

	/**
	 * Get the public identifier of the data element. This is a common id that may
	 * or may not be unique to this data element.
	 *
	 * @return the public id
	 */
	public long getId();

	/**
	 * Set the public identifier of the data element. This is a common id that may
	 * or may not be unique to this data element.
	 *
	 * @param public_id the public id
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setId(final long public_id) throws Exception;

	/**
	 * This operation returns the simple name of the data
	 *
	 * @return the name of the data, i.e. - "CORD-19" or "Steve"
	 */
	public String getName();

	/**
	 * Set the simple name of the element
	 *
	 * @param name a simple name
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setName(final String name) throws Exception;

	/**
	 * Get the simple description of the data, i.e. - "Machine readable data for
	 * COVID-19 research."
	 *
	 * @return the description of the data
	 */
	public String getDescription();

	/**
	 * Set the description of the data
	 *
	 * @param desc the description
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setDescription(final String desc) throws Exception;

	/**
	 * Get the comment/tag that annotates this data. This value is different than
	 * the description in that it provides commentary or a secondary designation in
	 * the form of an annotation for the data. For example, where the description
	 * should generally be useful, this value could simply be "2020Data" or any
	 * other tag of convenience used during processing.
	 *
	 * @return the comment
	 */
	public String getComment();

	/**
	 * Return the comment or tag that annotates the data
	 *
	 * @param comment the comment to set
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setComment(final String comment) throws Exception;

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
	public String getContext();

	/**
	 * Return the context in which the data exists.
	 *
	 * @param context the context to set
	 * @throws Exception An exception is thrown if the value is null, which is
	 *                   unallowable.
	 */
	public void setContext(final String context) throws Exception;

	/**
	 * True if the element is required by the client, false otherwise. This is only
	 * for client tracking and may make no sense for different clients.
	 *
	 * @return true if required, false if not
	 */
	public boolean isRequired();

	/**
	 * True if the element is required by the client, false otherwise. This is only
	 * for client side tracking and may make no sense for different clients.
	 *
	 * @param required true if required, false if not
	 */
	public void setRequired(final boolean required);

	/**
	 * True if the element is something that should be secret by the client, false
	 * otherwise. This is only for client tracking and may make no sense for
	 * different clients.
	 *
	 * @return true if secret, false if not
	 */
	public boolean isSecret();

	/**
	 * True if the element is something that should be secret by the client, false
	 * otherwise. This is only for client tracking and may make no sense for
	 * different clients.
	 *
	 * @param secret true if the element should be treated as a secret, false
	 *               otherwise
	 */
	public void setSecret(final boolean secret);

	/**
	 * This operation returns the UUID of the data element. The UUID is a private
	 * unique identifier assigned to all data elements.
	 *
	 * @return the UUID for this element
	 */
	public UUID getUUID();

	/**
	 * Format the DataElement as an output friendly string.
	 * @return String representation of Data
	 */
	public String toString();

	/**
	 * This operation clones the object. Note that it differs from the base class
	 * implementation in that it will return null if it cannot create the clone to
	 * promote fast failure. See {@link java.lang.Object#clone()};
	 * @return the cloned object
	 */
	public Object clone();

	/**
	 * This function checks deep equality of DataElements to see if all members are
	 * equal ("match") with the exception of fields with match set to false (such
	 * as an automatically generated UUID). This is important for checking if two
	 * objects were generated separately but are otherwise equal.
	 *
	 * @param o the other element to compare
	 * @return true if all members of the element except excluded fields match
	 *         this element.
	 */
	public boolean matches(Object o);

	/**
	 * This operation serializes the data element to a string in verified JSON.
	 *
	 * @return a JSON string describing the element
	 */
	public String toJson();

	/**
	 * This operation deserializes a valid JSON string and tries to load it into the
	 * object.
	 *
	 * @param jsonDataElement the contents of this data element as JSON
	 * @return the deserialized DataElement
	 */
	public T fromJson(final String jsonDataElement);

	/**
	 * Load from a String-Object Map, skipping the String parsing step. Structures
	 * such as {@link org.bson.Document} implement {@code Map<String, Object>} and
	 * therefore do not need to be processed from raw String form.
	 *
	 * @param <S> Object extending {@code Map<String, Object>}
	 * @param jsonDataElement the contents of this data element as a
	 *        {@code Map<String, Object>}
	 * @return the deserialized DataElement
	 */
	public <S extends Map<String, Object>> T fromJSON(final S jsonDataElement);

	/**
	 * This operation returns the validator that has been configured for this
	 * element.
	 *
	 * @return the validator or null if it has not been set
	 */
	public JavascriptValidator<T> getValidator();

	/**
	 * This operation sets the validator associated with this element.
	 *
	 * @param validator the validator or null to reset the reference
	 */
	public void setValidator(JavascriptValidator<T> validator);
}
