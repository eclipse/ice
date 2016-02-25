/*******************************************************************************
 * Copyright (c) 2012, 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Initial API and implementation and/or initial documentation - Jay Jay Billings, 
 *    Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson, 
 *    Claire Saunders, Matthew Wang, Anna Wojtowicz
 *     
 *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;

/**
 * <p>
 * The Entry class is responsible for collecting and managing the individual
 * values needed by the Item to perform a task. The Entry class is capable of
 * checking bounds and validity.
 * </p>
 * <p>
 * The Entry's content will be maintained by a content provider, or any piece
 * that respectfully inherits from the IEntryContentProvider. All of the getters
 * and setters for the content (AllowedValues, type, default values, etc) will
 * pull from the ContentProvider. If the nullary constructor is used, then the
 * Entry will create the BasicEntryContentProvider. Entry.Value and attributes
 * like changedState, on the other hand, will be managed specifically by Entry
 * (this excludes default value).
 * </p>
 * <p>
 * The "readiness" of an Entry is used map dependencies between Entries and
 * other Entries. An Entry may depend on a parent Entry and passing the correct
 * key-value pair to the update() operation will mark the Entry as ready or not
 * ready depending on the state of the parent. All Entries default to ready. The
 * changed or modified state of the Entry is similar in that it distinguishes
 * whether or not an Entry has been recently modified.
 * </p>
 * <p>
 * The Entry implements IUpdateable to update the state of the Entry based on
 * key-value pairs. The base default implementation of update() will mark the
 * Entry as "ready" (isReady() will return true) if the name of the Entry's
 * parent is passed as the key and one of "ready," "yes," "y," "on," "true" or
 * "enabled" is passed as the value (minus the commas of course). It will mark
 * the Entry as not ready (isReady() will return false) if the value is
 * "not ready," "no," "n," "off," "false" or "disabled" instead. Neither set of
 * values are case sensitive ("Yes" will work as well as "yes"). It also resets
 * the "changed" state of the Entry to false to reflect that review is required
 * because of new information from the update. This operation should be
 * overridden by subclasses to get tailored update behavior, but Entry.update
 * should be called still (by using "super.update()") to handle updating because
 * of parents and setting the changed state.
 * </p>
 * <p>
 * IComponentListeners can also registered with the Entry since it implements
 * IUpdateable. The Entry will notify listeners when its value changes.
 * </p>
 * <p>
 * Managing the readiness of the Entry with update in the base class allows ICE
 * to generically handle the almost trivial case of dependencies where one Entry
 * needs to know about the state of another before revealing itself.
 * </p>
 * <p>
 * The Entry can also be marked as "secret" to indicate that its contents should
 * not be displayed openly. Entries should be marked secret in their setup()
 * operation. There is no public operation to mark them as secret, although
 * there is an isSecret() operation to determine whether or not an Entry is
 * secret.
 * </p>
 * <p>
 * Entry's may be "tagged" with values that act as secondary names or unique
 * identifiers in systems outside of ICE. This is particularly useful for
 * creating files based on key-value pairs of Entry names and values, but the
 * name of the Entry needs to be some condensed or modified form of the human
 * readable name returned by getName().
 * </p>
 * <p>
 * Entry is a subclass of ICEObject. It overrides ICEObject.copy(),
 * ICEObject.clone() as specified and required by the ICEObject class.
 * </p>
 * <p>
 * Giving one Entry priority over another can be done in a couple of different
 * ways. The first way is to set an Entry's required flag to true (c.f.
 * setRequired()). Entries can also be organized based on their identification
 * number. If an Entry is marked as "required" it should be treated as if it is
 * required for computation and prioritized higher than others that are not
 * required.
 * </p>
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 */
@XmlRootElement(name = "Entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class VizEntry extends VizObject implements IVizUpdateable {

	/**
	 * The default value of the Entry.
	 */
	@XmlAttribute()
	protected String defaultValue;

	/**
	 * This list stores either the exact values that the Entry may have or a
	 * range in which the value of the Entry must exist depending on the
	 * AllowedValueType.
	 */
	@XmlElement(name = "AllowedValues")
	protected ArrayList<String> allowedValues;

	/**
	 * The value of the Entry.
	 */
	@XmlAttribute()
	protected String value;

	/**
	 * This attribute describes the types of values that Entry will accept and
	 * is equal to AllowedValueType.Undefined if the Entry is created with the
	 * simple constructor.
	 */
	@XmlElement(name = "AllowedValueType")
	protected VizAllowedValueType allowedValueType;

	/**
	 * A String attribute where a comment about the Entry can be stored.
	 */
	@XmlElement(name = "Comment")
	protected String comment;

	/**
	 * This attribute stores the state of the Entry as either true if the Entry
	 * is ready to be addressed and false if the Entry is not ready. This
	 * attribute is true by default.
	 */
	@XmlAttribute()
	protected boolean ready;
	/**
	 * This attribute describes the "changed" state of the Entry. It has a value
	 * of true if the Entry's value was recently set and false if the Entry has
	 * not changed or was recently updated. It defaults to false.
	 */
	@XmlAttribute()
	protected boolean changeState;

	/**
	 * True if the content of the Entry should be treated as something secret
	 * and needs to be obscured from open view or print. Its default value is
	 * false.
	 */
	@XmlAttribute()
	protected boolean secretFlag;

	/**
	 * The name of the Entry that is the parent of this Entry. Parent Entries
	 * are used, for example, to create dependencies among Entries such that
	 * some subset of the Entries will not become available to update until the
	 * parent notifies them that they should be ready.
	 */
	@XmlAttribute()
	protected String parent;

	/**
	 * The tag of an Entry is a secondary descriptive value that may be used to
	 * "tag" an Entry with a small note or additional value. This information
	 * should not be used in the UI! Another way to think of the tag of an Entry
	 * is to consider it as a second name that could be used, for example, when
	 * writing to a file or stream where human readability is less of a factor
	 * than the ability to parse the stream, (such as key-value pairs).
	 */
	@XmlAttribute()
	protected String tag;

	/**
	 * This attribute stores a human-readable reason for rejecting an invalid
	 * value passed to setValue(). It may be retrieved by calling
	 * getErrorMessage().
	 */
	@XmlAttribute()
	protected String errorMessage = null;

	/**
	 * The template for the error that is returned for set value if the allowed
	 * value type is continuous.
	 */
	@XmlTransient
	protected String continuousErrMsg = "'${incorrectValue}' is an "
			+ "unacceptable value. The value must be between ${lowerBound} "
			+ "and ${upperBound}.";

	/**
	 * The template for the error that is returned for set value if the allowed
	 * value type is discrete.
	 */
	@XmlTransient
	protected String discreteErrMsg = "'${incorrectValue}' is an unacceptable "
			+ "value. The value must be one of ${allowedValues}.";

	/**
	 * The content provider for the Entry.
	 */
	@XmlAnyElement()
	@XmlElementRef(name = "BasicEntryContentProvider", type = BasicVizEntryContentProvider.class)
	protected IVizEntryContentProvider iEntryContentProvider;

	/**
	 * This attribute indicates whether or not the Entry should be considered as
	 * a required quantity.
	 */
	protected boolean required = false;

	/**
	 * A convenience function which calls the constructor configured to run
	 * setup()
	 */
	public VizEntry() {
		this(true);
	}

	/**
	 * A constructor that will create an Entry with only a unique ID and a name.
	 * Default values are set:
	 * <ul>
	 * <li>objectDescription = "Entry" + uniqueId</li>
	 * <li>defaultValue = ""</li>
	 * <li>allowedValues = null</li>
	 * <li>allowedValueType = AllowedValueType.Undefined</li>
	 * </ul>
	 * 
	 * The constructor may optionally call the setup function after setting the
	 * default values. The setup function can be overridden to tailor the
	 * properties of the Entry or otherwise overload the behavior of the Entry.
	 * Not invoking startup is desirable when the user will invoke it themselves
	 * at a later point, such as when another constructor calls this one before
	 * completing the object's initialization itself.
	 * 
	 * @param runSetup
	 *            Whether or not to run the setup() function
	 */
	public VizEntry(boolean runSetup) {

		// Set everything else to the default values
		objectDescription = "Entry " + this.uniqueId;
		defaultValue = "";
		allowedValues = new ArrayList<String>();
		allowedValueType = VizAllowedValueType.Undefined;
		comment = "";
		parent = "orphan";
		ready = true;
		changeState = false;
		secretFlag = false;
		tag = null;

		// BECP defaults should be the same as the above!
		iEntryContentProvider = new BasicVizEntryContentProvider();

		// Call the setup function to tailor the Entry for the developer
		if (runSetup) {
			setup();
		}

		// Set values on BECP
		iEntryContentProvider.setAllowedValues(this.allowedValues);
		iEntryContentProvider.setAllowedValueType(this.allowedValueType);
		iEntryContentProvider.setDefaultValue(this.defaultValue);
		// The parent and tag need to be checked to make sure that they were not
		// already set by the setter. Not checking them creates a very subtle
		// bug by which someone can override an Entry's setup() operation and
		// set all of the values, but then call setParent() or setTag(), which
		// calls the provider directly, instead of setting the member variable.
		// In this case, the value of parent or tag is overwritten with null in
		// the provider because the member variables are still their default
		// values.
		if ("orphan".equals(iEntryContentProvider.getParent())) {
			iEntryContentProvider.setParent(parent);
		}
		if (iEntryContentProvider.getTag() == null) {
			iEntryContentProvider.setTag(tag);
		}

		// Setup the list of Listeners
		listeners = new ArrayList<IVizUpdateableListener>();

		return;
	}

	/**
	 * A constructor that sets the entry to the content provider.
	 * 
	 * @param contentProvider
	 */
	public VizEntry(IVizEntryContentProvider contentProvider) {

		// Call the super constructor
		this();

		// This is not cloned!
		if (contentProvider != null) {
			this.iEntryContentProvider = contentProvider;
		}

		// Call the setup function to tailor the Entry for the developer
		setup();
		
		return;
	}

	/**
	 * This Entry returns the allowed values for the Entry and should only be
	 * used when the AllowedValueType is defined as Discrete, in which case the
	 * value must equal one of the allowed values. These allowed values types
	 * should not be used to check the submitted value because the Entry class
	 * will handle its own error checking upon the submission of a value in
	 * setValue().
	 * 
	 * @return The allowed values for the Entry.
	 */
	public ArrayList<String> getAllowedValues() {

		return this.iEntryContentProvider.getAllowedValues();
	}

	/**
	 * This operation returns the default value of the Entry.
	 * 
	 * @return The default value of the Entry.
	 */
	public String getDefaultValue() {
		return this.iEntryContentProvider.getDefaultValue();
	}

	/**
	 * This operation returns the current value of the Entry or the default
	 * value if no other has been specified.
	 * 
	 * @return The current value of the Entry.
	 */
	public String getValue() {

		// Make sure there is a default value that makes sense - If it wasn't
		// set in construction but allowed values were provided, it should be
		// reset to allowedValues.get(0). The default value should always be
		// equal to something if there are allowed values for the Entry!
		if ("".equals(this.iEntryContentProvider.getDefaultValue())
				&& !this.iEntryContentProvider.getAllowedValues().isEmpty()) {
			this.iEntryContentProvider
					.setDefaultValue(this.iEntryContentProvider
							.getAllowedValues().get(0));
		}
		// Return the proper value. The defaultValue and allowedValues are from
		// BECP, value is from Entry.
		return (value != null) ? value : this.iEntryContentProvider
				.getDefaultValue();
	}

	/**
	 * <p>
	 * This operation returns the type of value that is stored in the Entry.
	 * </p>
	 * <p>
	 * A response type of AllowedValueType.Discrete means that the answer must
	 * be one of the values from getAllowedValues().
	 * </p>
	 * <p>
	 * A response of AllowedValueTypes.Continuous means that the value must
	 * exist within the range of the maximum and minimum of the values from
	 * getAllowedValues().
	 * </p>
	 * <p>
	 * A response of AllowedValueType.Undefined means that the response will not
	 * be checked.
	 * </p>
	 * <p>
	 * A response of AllowedValueType.File means that the response will not be
	 * checked just like an Undefined type, but should only be used for values
	 * that represent actual files. File entries render differently in the UI.
	 * </p>
	 * <p>
	 * Please note that the Entry class will check the validity of the values
	 * submitted to it and the information returned from this method is purely
	 * for information purposes (deciding how to draw the client, etc.).
	 * </p>
	 * 
	 * @return The AllowedValueType for the Entry.
	 */
	public VizAllowedValueType getValueType() {
		return this.iEntryContentProvider.getAllowedValueType();
	}

	/**
	 * Set the comment on the Entry. Empty strings are permitted.
	 * 
	 * @param comment
	 *            The comment to set on the Entry.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Get the value of the Entry's comment.
	 * 
	 * @return The comment.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * This operation returns true if the Entry should be addressed and false if
	 * the Entry is not ready to be addressed (waiting on a parent Entry, etc.).
	 * 
	 * @return The readiness state of the Entry.
	 */
	public boolean isReady() {
		return this.ready;
	}

	/**
	 * This operation returns true if the Entry's value was recently set and
	 * false if the Entry's value has not been changed or if the Entry was
	 * recently updated.
	 * 
	 * @return True if the Entry's value was recently set, false if the Entry's
	 *         value has not been set or if the Entry was recently updated.
	 */
	public boolean isModified() {
		return this.changeState;
	}

	/**
	 * This operation stores the name of an Entry on which this Entry is
	 * dependent. The "parent" Entry must be evaluated and notify this Entry by
	 * calling update() before this Entry will mark itself as ready.
	 * 
	 * @param parentName
	 *            The name of the parent Entry.
	 */
	public void setParent(String parentName) {

		// Take the parent name so long as it is not null
		if (parentName != null) {
			this.iEntryContentProvider.setParent(parentName);
		}

		return;
	}

	/**
	 * This operation retrieves the name of the Entry on which this Entry is
	 * dependent. The "parent" Entry must be evaluated and notify this Entry by
	 * calling update() before this Entry will mark itself as ready.
	 * 
	 * @return The name of the parent Entry.
	 */
	public String getParent() {

		// The default parent value should not be returned. That would open the
		// Entry up to updates without actually registering a parent.
		if ("orphan".equals(iEntryContentProvider.getParent())) {
			return null;
		}

		return iEntryContentProvider.getParent();

	}

	/**
	 * This operation sets the value of the Entry to newValue and returns true
	 * if it is successful.
	 * 
	 * @param newValue
	 *            The new value of the Entry. This value will be checked by the
	 *            Entry for validity.
	 * @return True if setValue worked, false otherwise.
	 */
	public boolean setValue(String newValue) {

		// Local Declarations
		Double lowerBound = null;
		Double upperBound = null;
		Double newValueDouble = null;
		boolean returnCode = false;

		// ---- USE BasicEntryContentProvider VALUES ONLY! ----

		// Check to see if the value actually needs to be changed. If the values
		// are the same, don't do any work. Just return true and do not notify
		// the listeners.
		// FIXME What if value is null and newValue is null?
		if (value != null && value.equals(newValue)) {
			return true;
		}

		// Get the allowed values and allowed value type from the content
		// provider for use below.
		final VizAllowedValueType allowedValueType = iEntryContentProvider
				.getAllowedValueType();
		final List<String> allowedValues = iEntryContentProvider
				.getAllowedValues();

		// Make sure there is a default value that makes sense - If it wasn't
		// set in construction but allowed values were provided, it should be
		// reset to allowedValues.get(0). The default value should always be
		// equal to something if there are allowed values for the Entry!
		if ("".equals(iEntryContentProvider.getDefaultValue())
				&& !allowedValues.isEmpty()) {
			iEntryContentProvider.setDefaultValue(allowedValues.get(0));
		}
		// Check discrete values
		if (allowedValueType == VizAllowedValueType.Discrete) {
			if (allowedValues.contains(newValue)) {
				this.value = newValue;
				returnCode = true;
			} else {
				returnCode = false;
			}
		} else if (allowedValueType == VizAllowedValueType.Continuous
				&& allowedValues != null) {
			// Check continuous value against the bounds. Doing this with
			// doubles is simplest. allowedValues should only have two
			// values for Continuous values.
			if (allowedValues.size() == 2) {
				lowerBound = Double.valueOf(iEntryContentProvider
						.getAllowedValues().get(0));
				upperBound = Double.valueOf(iEntryContentProvider
						.getAllowedValues().get(1));
				// Try to cast to a double, but fail if it is impossible.
				try {
					newValueDouble = Double.valueOf(newValue);
				} catch (NumberFormatException | NullPointerException e) {
					returnCode = false;
				}
				// Set the value if it is within the bounds
				if (newValueDouble != null
						&& (newValueDouble.compareTo(lowerBound) != -1 && newValueDouble
								.compareTo(upperBound) != 1)) {
					this.value = newValue;
					returnCode = true;
				} else {
					returnCode = false;
				}
			}
		} else if (allowedValueType == VizAllowedValueType.Undefined
				|| allowedValueType == VizAllowedValueType.File
				|| allowedValueType == VizAllowedValueType.Executable) {
			this.value = newValue;
			returnCode = true;
		}

		// Set the change state
		if (returnCode) {
			this.changeState = true;
			// set the error message to null if the returncode is true
			this.errorMessage = null;
		}
		// Setup the error messages accordingly because setting the value
		// has failed.
		else if (allowedValueType == VizAllowedValueType.Continuous) {
			if (allowedValues.size() != 2) {
				// We return false here because this is an entirely unexpected
				// error and should never occur due to user
				// input.
				return false;
			}
			String error = this.continuousErrMsg;
			// Replace the default error values with the ones for this Entry
			error = error.replace("${incorrectValue}",
					newValue != null ? newValue : "null");
			error = error.replace("${lowerBound}", iEntryContentProvider
					.getAllowedValues().get(0));
			error = error.replace("${upperBound}", iEntryContentProvider
					.getAllowedValues().get(1));
			this.errorMessage = error;
		}
		// Modify it according if the error message is for discrete allowed
		// values
		else if (allowedValueType == VizAllowedValueType.Discrete) {
			String error = this.discreteErrMsg;

			// loop to get all the values of the allowedValues
			String tempValues = "";
			for (int i = 0; i < allowedValues.size(); i++) {
				// If it is a list and it is the last item, add an "or"
				if (i == allowedValues.size() - 1 && allowedValues.size() > 1) {
					tempValues += " or";
				}
				// Add the value to the message
				tempValues += " " + allowedValues.get(i);
				// Add a comma for the allowedValues
				if (i < allowedValues.size() - 1 && allowedValues.size() > 2) {
					tempValues += ",";
				}

			}

			// Replace with correct errors
			error = error.replace("${incorrectValue}",
					newValue != null ? newValue : "null");
			error = error.replace(" ${allowedValues}", tempValues);
			this.errorMessage = error;
		}

		// Notify the listeners of the change if the value was correctly set
		if (returnCode) {
			notifyListeners();
		}
		return returnCode;
	}

	/**
	 * This operation sets the readiness state of the Entry to the value of
	 * isReady.
	 * 
	 * @param isReady
	 *            The value to which the readiness state of the Entry should be
	 *            set.
	 */
	public void setReady(boolean isReady) {
		this.ready = isReady;
		return;
	}

	/**
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// Local Declarations
		ArrayList<String> readyEquivalentValues = new ArrayList<String>();
		ArrayList<String> notReadyEquivalentValues = new ArrayList<String>();

		// The update should only be processed if the values are not null
		if (updatedKey != null && newValue != null) {

			// Setup the list of values that are equivalent to "ready"
			readyEquivalentValues.add("ready");
			readyEquivalentValues.add("yes");
			readyEquivalentValues.add("y");
			readyEquivalentValues.add("true");
			readyEquivalentValues.add("enabled");
			readyEquivalentValues.add("on");
			// Setup the list of values that are equivalent to "not ready"
			notReadyEquivalentValues.add("not ready");
			notReadyEquivalentValues.add("no");
			notReadyEquivalentValues.add("n");
			notReadyEquivalentValues.add("false");
			notReadyEquivalentValues.add("disabled");
			notReadyEquivalentValues.add("off");

			// Check to see if the parent is notifying the Entry that it should
			// be ready or if it should not be ready. The values are compared
			// against the accepted set of values that will change the readiness
			// state of this Entry in a way that is not case sensitive.
			if (updatedKey.equals(this.iEntryContentProvider.getParent())
					&& readyEquivalentValues.contains(newValue.toLowerCase())) {
				this.ready = true;
				this.changeState = false;
			} else if (updatedKey
					.equals(this.iEntryContentProvider.getParent())
					&& notReadyEquivalentValues
							.contains(newValue.toLowerCase())) {
				this.ready = false;
			}

		}

		return;
	}

	/**
	 * This operation sets up the allowed values and allowed value type of the
	 * Entry. Default values and the "secret" flag are also specified in this
	 * operation. It is called during construction and should be overloaded to
	 * tailor the Entry.
	 */
	protected void setup() {
		// No default implementation
		return;
	}

	/**
	 * This method is used to check equality between the Entry and another
	 * Entry. It returns true if the Entries are equal and false if they are
	 * not.
	 * 
	 * @param otherEntry
	 *            The Entry that should be checked for equality.
	 * @return True if the Entries are equal, false if not
	 */
	@Override
	public boolean equals(Object otherEntry) {

		// Local Declarations
		boolean retVal = false;
		VizEntry other = null;

		// Check the Entry, null and base type check first. Note that the
		// instanceof operator must be used because subclasses of Entry
		// can be anonymous.
		if (otherEntry != null && (otherEntry instanceof VizEntry)) {
			// See if they are the same reference on the heap
			if (this == otherEntry) {
				retVal = true;
			} else {
				other = (VizEntry) otherEntry;
				// Check each member value
				retVal = (this.uniqueId == other.uniqueId)
						&& (this.objectName.equals(other.objectName))
						&& (this.objectDescription
								.equals(other.objectDescription))
						// Check data not available on the provider
						&& (this.comment.equals(other.comment))
						&& (this.ready == other.ready)
						&& (this.changeState == other.changeState)
						&& (this.secretFlag == other.secretFlag)
						&& (this.required == other.required)
						// Allowed Values, type, parent, and tag are checked on
						// iEntryContentProvider
						&& (this.iEntryContentProvider
								.equals(other.iEntryContentProvider));
			}
		}
		return retVal;
	}

	/**
	 * This operation returns the hashcode value of the Entry.
	 * 
	 * @return The hashcode
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = 8;

		// Compute the hashcode
		hash = 31 * super.hashCode();
		hash = 31 * hash + (this.ready ? 1 : 0);
		hash = 31 * hash + (this.changeState ? 1 : 0);
		hash = 31 * hash + (this.secretFlag ? 1 : 0);
		hash = 31 * hash + (null == this.comment ? 0 : this.comment.hashCode());
		hash = 31 * hash + (null == this.value ? 0 : this.value.hashCode());
		hash = 31
				* hash
				+ (null == this.defaultValue ? 0 : this.defaultValue.hashCode());
		hash = 31 * hash + (null == this.parent ? 0 : this.parent.hashCode());
		hash = 31
				* hash
				+ (null == this.allowedValues ? 0 : this.allowedValues
						.hashCode());
		hash = 31
				* hash
				+ (null == this.allowedValueType ? 0 : this.allowedValueType
						.hashCode());
		hash = 31 * hash + (null == this.tag ? 0 : this.tag.hashCode());
		hash = 31 * hash + this.iEntryContentProvider.hashCode();
		hash = 31 * hash + (required ? 1 : 0);

		return hash;
	}

	/**
	 * This operation returns true if the Entry is "secret" and its contents
	 * should be obscured if displayed or printed and false if not.
	 * 
	 * @return True if the entry is "secret", false otherwise.
	 */
	public boolean isSecret() {
		return secretFlag;
	}

	/**
	 * This operation performs a deep copy of the attributes of another Entry
	 * into the current Entry.
	 * 
	 * @param otherEntry
	 *            The Entry from which information should be copied.
	 */
	public void copy(VizEntry otherEntry) {

		// if entry is null, return
		if (otherEntry == null) {
			return;
		}

		// copy from super class
		super.copy(otherEntry);

		// Copy current values
		this.allowedValueType = otherEntry.allowedValueType;
		this.changeState = otherEntry.changeState;
		this.defaultValue = otherEntry.defaultValue;
		this.comment = otherEntry.comment;
		this.ready = otherEntry.ready;
		this.value = otherEntry.value;
		this.secretFlag = otherEntry.secretFlag;
		this.parent = otherEntry.parent;
		this.tag = otherEntry.tag;
		this.required = otherEntry.required;
		this.iEntryContentProvider = (IVizEntryContentProvider) otherEntry.iEntryContentProvider
				.clone();
		// Deep copy allowed Values
		this.allowedValues.clear();
		for (int i = 0; i < otherEntry.allowedValues.size(); i++) {
			this.allowedValues.add(otherEntry.allowedValues.get(i));
		}

		// Notify all of the people watching this Entry
		notifyListeners();

		return;
	}

	/**
	 * This operation provides a deep copy of the Entry.
	 * 
	 * @return A clone of the Entry.
	 */
	@Override
	public Object clone() {

		// Create a new instance of entry and copy the contents
		VizEntry entry = new VizEntry();
		entry.copy(this);

		return entry;
	}

	/**
	 * This operation returns the Entry's tag.
	 * 
	 * @return The Entry's tag.
	 */
	public String getTag() {
		return this.iEntryContentProvider.getTag();
	}

	/**
	 * This operation sets the tag of the Entry.
	 * 
	 * @param tagValue
	 *            The Entry's new tag.
	 */
	public void setTag(String tagValue) {

		this.tag = tagValue;
		this.iEntryContentProvider.setTag(tagValue);

		return;
	}

	/**
	 * This operation returns a human-readable reason for a rejected value
	 * passed to setValue().
	 * 
	 * @return <p>
	 *         The error message. If the AllowedValueType of the Entry is
	 *         Continuous, it will be an error of the form:
	 *         </p>
	 *         <p>
	 *         <b>'${incorrectValue}' in an unacceptable value. The value must
	 *         be between ${lowerBound} and ${upperBound}.</b>
	 *         </p>
	 *         <p>
	 *         If the AllowedValueType is Discrete, it will be an error of the
	 *         form:
	 *         </p>
	 *         <p>
	 *         <b>'${incorrectValue}' in an unacceptable value. The value must
	 *         be one of ${allowedValues}.</b>
	 *         </p>
	 *         <p>
	 *         The variables are:
	 *         </p>
	 *         <ul>
	 *         <li>${incorrectValue} - The incorrect value submitted to
	 *         setValue()</li>
	 *         <li>${lowerBound} - The lower bound of the range</li>
	 *         <li>${upperBound} - The upper bound of the range</li>
	 *         <li>${allowedValue} - The entire set of allowed values for
	 *         AllowedValueType.Discrete.</li>
	 *         </ul>
	 *         <p>
	 *         An error message will never be posted if the type is
	 *         AllowedValueType.Undefined and this value will be null.
	 *         </p>
	 *         <p>
	 *         If the value of setValue() was accepted, this operation will
	 *         return null.
	 *         </p>
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * Sets the content provider. Resets the value to null.
	 * 
	 * @param contentProvider
	 *            The content provider.
	 */
	public void setContentProvider(IVizEntryContentProvider contentProvider) {

		if (contentProvider != null) {
			this.iEntryContentProvider = contentProvider;
		}

		return;
	}

	/**
	 * This operation returns true if the Entry should be treated as a required
	 * quantity.
	 * 
	 * @return True if required, false if not.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * This operation sets the marks the Entry as required or not to indicate
	 * where or not it should be treated as a required quantity.
	 * 
	 * @param entryRequired
	 *            The value to which the required state of the Entry should be
	 *            set; true if required, false if not.
	 */
	public void setRequired(boolean entryRequired) {
		required = entryRequired;
		return;
	}
}
