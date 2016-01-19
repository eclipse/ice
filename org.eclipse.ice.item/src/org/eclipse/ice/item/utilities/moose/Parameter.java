/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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
package org.eclipse.ice.item.utilities.moose;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.FileEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * This class represents a MOOSE input Parameter. This class is parsed from and
 * written to YAML using SnakeYAML's Java Bean parser. It can also be loaded
 * from an Entry, although this should be used sparingly since the conversion
 * from an ICE Entry to a MOOSE parameter is not a 1-1 mapping. It is only used
 * in ICE for writing input files, at the moment.
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 */
public class Parameter {
	/**
	 * The name of the parameter
	 */
	private String name = "";

	/**
	 * The flag that indicates whether or not this parameter is required. True
	 * if it is required, false if not.
	 */
	private boolean required;

	/**
	 * The flag that indicates whether or not this parameter is enabled (ie. if
	 * it is commented out or not). False if the parameter is commented out,
	 * otherwise true.
	 */
	private boolean enabled;

	/**
	 * The default value of the parameter.
	 */
	private String _default = "";

	/**
	 * The C++ type of the parameter.
	 */
	private String cpp_type = "";

	/**
	 * The list of options of the parameter (if any). Not all parameters have
	 * options.
	 */
	private ArrayList<String> options = null;

	/**
	 * The name of the group to which the parameter belongs.
	 */
	private String group_name = "";

	/**
	 * A description of the parameter.
	 */
	private String description = "";

	/**
	 * A comment on the parameter.
	 */
	private String comment = "";

	/**
	 * This operation retrieves the name of the parameter.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This operation indicates if the parameter is required. True if required,
	 * false if not.
	 * 
	 * @return True if required, false if not
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Returns if the parameter is enabled or not (ie. commented out).
	 * 
	 * @return True if the parameter is enabled, false if it is commented out.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * This operation retrieves the default value of the parameter.
	 * 
	 * @return The default value
	 */
	public String getDefault() {
		return _default;
	}

	/**
	 * This method returns the parameter's comment, if it has one.
	 * 
	 * @return The parameter's comment, or an empty String if there is none.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * This operation retrieves the C++ type of the parameter.
	 * 
	 * @return The type
	 */
	public String getCpp_type() {
		return cpp_type;
	}

	/**
	 * This operation retrieves the list of options (if any)
	 * 
	 * @return The list of options.
	 */
	public ArrayList<String> getOptions() {
		return options;
	}

	/**
	 * This operation retrieves the group name of the parameter.
	 * 
	 * @return The group name
	 */
	public String getGroup_name() {
		return group_name;
	}

	/**
	 * This operation retrieves the description of the parameter.
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This operation sets the name of the parameter.
	 * 
	 * @param paramName
	 *            The name
	 */
	public void setName(String paramName) {
		name = paramName;
		return;
	}

	/**
	 * This operation sets specifies whether or not the parameter is required.
	 * True for required, false if not.
	 * 
	 * @param flag
	 *            True if required, false if not
	 */
	public void setRequired(boolean flag) {
		required = flag;
		return;
	}

	/**
	 * This method sets if the parameter is currently enabled (ie. not commented
	 * out).
	 * 
	 * @param flag
	 *            True if the parameter is enabled, false if it is commented
	 *            out.
	 */
	public void setEnabled(boolean flag) {
		enabled = flag;
		return;
	}

	/**
	 * This operation sets the default value of the parameter.
	 * 
	 * @param defaultValue
	 *            The default value
	 */
	public void setDefault(String defaultValue) {
		_default = defaultValue;
		return;
	}

	/**
	 * This method sets the parameter's comment.
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
		return;
	}

	/**
	 * This operation sets the C++ type of the parameter.
	 * 
	 * @param type
	 *            The type
	 */
	public void setCpp_type(String type) {
		cpp_type = type;
		return;
	}

	/**
	 * This operation sets options of the parameter (if any).
	 * 
	 * @param optionsStr
	 *            A string of options, with each separated by any amount of
	 *            whitespace.
	 */
	public void setOptions(String optionsStr) {

		// Initiate the ArrayList if it hasn't been already
		if (options == null) {
			options = new ArrayList<String>();
		}

		// Break up the input string of options at each whitespace
		String[] splitOptionsStr = optionsStr.split("\\s+");

		// Add them to the ArrayList
		for (int i = 0; i < splitOptionsStr.length; i++) {
			options.add(splitOptionsStr[i]);
		}

		return;
	}

	/**
	 * This operation sets the group name of the parameter.
	 * 
	 * @param gName
	 *            The group name
	 */
	public void setGroup_name(String gName) {
		group_name = gName;
		return;
	}

	/**
	 * This operation sets the description of the parameter.
	 * 
	 * @param desc
	 *            The description
	 */
	public void setDescription(String desc) {
		description = desc;
		return;
	}

	/**
	 * This operation returns this parameter as an ICE Entry with
	 * AllowedValueType.Undefined.
	 * 
	 * @return The data components
	 */
	public IEntry toEntry() {

		// Local Declarations
		IEntry entry = null;
		
		// If the type is discrete (MooseEnum) and the options list
		// isn't empty
		if ((("MooseEnum").equals(Parameter.this.cpp_type) || ("MultiMooseEnum").equals(Parameter.this.cpp_type))
				&& options != null && !options.isEmpty()) {
			entry = new DiscreteEntry();
			entry.setAllowedValues(options);
			// Set the default value, descri
			String value = Parameter.this.getDefault();
			entry.setDefaultValue((options.contains(value) ? value : options.get(0)));
		}
		// If the value type is boolean
		else if (("bool").equals(Parameter.this.cpp_type)) {
			entry = new DiscreteEntry("true","false");
			// Set the default value and description
			entry.setDefaultValue((Parameter.this.getDefault().equals("false")) ? "false" : "true");
		} else if ("FileName".equals(Parameter.this.cpp_type) || "MeshFileName".equals(Parameter.this.cpp_type)) {
			entry = new FileEntry();
			if (options != null && !options.isEmpty()) {
				entry.setAllowedValues(options);
				// Set the default value, descri
				String value = Parameter.this.getDefault();
				entry.setDefaultValue((options.contains(value) ? value : options.get(0)));
			}

			// Otherwise, for all other parameters
		} else if ("VariableName".equals(cpp_type) || "AuxVariableName".equals(cpp_type) || "variable".equals(this.name)) {
			entry = new DiscreteEntry();
		} else {
			entry = new StringEntry();
			entry.setDefaultValue(Parameter.this.getDefault());
		}
		
		// Set the rest of the attributes
		entry.setValue(entry.getDefaultValue());
		entry.setName(getName());
		entry.setDescription(getDescription());
		entry.setComment(getComment());
		entry.setRequired(required);
		entry.setTag(enabled ? "true" : "false");

		if ("type".equals(getName())) {
			entry.setRequired(true);
			entry.setTag("true");
		}

		return entry;
	}

	/**
	 * This operation writes the name and value of the parameter to a string as
	 * "name = value" (minus the quotations of course).
	 * 
	 * @return The parameter as a string.
	 */
	@Override
	public String toString() {
		return name + " = " + _default;
	}

	/**
	 * <p>
	 * This operation will load a Parameter from an Entry following the same
	 * rules as in toEntry(). This operation should ONLY be used to convert an
	 * Entry to a Parameter for writing a MOOSE input file.
	 * </p>
	 * <p>
	 * The parameter can not reconstruct the cpp_type from the Entry, so only
	 * the name, description and value (stored as default) are converted.
	 * </p>
	 * 
	 * @param entry
	 *            The Entry to load into the Parameter.
	 */
	public void fromEntry(IEntry entry) {

		// Only do this with a real Entry
		if (entry != null) {
			name = entry.getName();
			description = entry.getDescription();
			_default = entry.getDefaultValue();
			comment = entry.getComment();
			required = entry.isRequired();
			enabled = !"false".equalsIgnoreCase(entry.getTag());
			options = entry instanceof DiscreteEntry ? (ArrayList<String>) entry.getAllowedValues() : null;
		}

		return;
	}

	/**
	 * <p>
	 * This operation reads the parameter from a string of the form name =
	 * defaultValue and loads the name and default value.
	 * </p>
	 * <p>
	 * If the string is invalid then the parameter will remain unchanged.
	 * </p>
	 * 
	 * @param paramString
	 *            The parameter as a string.
	 */
	public void fromString(String paramString) {

		// Get the quantities if the string is formatted correctly.
		if (paramString != null && paramString.contains("=")) {
			// Split the string
			String[] pieces = paramString.split("=");
			// Get the pieces
			name = pieces[0].trim();
			_default = pieces[1].trim();
		}

		return;
	}
}
