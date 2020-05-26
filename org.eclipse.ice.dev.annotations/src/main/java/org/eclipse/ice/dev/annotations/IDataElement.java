package org.eclipse.ice.dev.annotations;

/**
 * Marker interface for DataElements.
 */
public interface IDataElement {
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
	public String toJSON();

	/**
	 * This operation deserializes a valid JSON string and tries to load it into the
	 * object.
	 *
	 * @param jsonDataElement the contents of this data element as JSON
	 */
	public void fromJSON(final String jsonDataElement);
}
