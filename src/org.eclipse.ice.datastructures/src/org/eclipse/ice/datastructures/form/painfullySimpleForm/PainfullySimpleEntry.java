/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.datastructures.form.painfullySimpleForm;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A PainfullySimpleEntry is used by the PainfullySimpleForm in place of a
 * regular Entry because it can be loaded from a string that takes a block of a
 * Painfully Simple Form file as input. Like the Painfully Simple Form, the
 * Painfully Simple Entry does not maintain any memory of the stream from which
 * it created itself.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PainfullySimpleEntry extends Entry {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This group name is used to determine to which DataComponent the
	 * PainfullySimpleEntry should be added.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String group;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PainfullySimpleEntry() {
		// begin-user-code
		// Call the superclass' constructor
		super();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the Entry from a collection of strings each of which
	 * should be an statement key-value pair (name=James T. Kirk) from a
	 * Painfully Simple Form Entry block. It expects only the statements from
	 * the block, not the empty lines that surround the block. It also does not
	 * use the "group=" statement and will ignore it. (The group statement is
	 * used by the PainfullySimpleForm to determine which DataComponent owns the
	 * Entry.) The order of the statements does not matter. It ignores any
	 * end-of-line comments in the statements. It throws an exception if an
	 * invalid key is detected in the set. The message of the exception will
	 * contain the invalid key-value pair.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStrings
	 *            <p>
	 *            The set of PSF statements that pertain to this Entry.
	 *            </p>
	 * @throws IOException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromPSFBlock(ArrayList<String> inputStrings)
			throws IOException {
		// begin-user-code

		// Local Declarations
		ArrayList<String> linesToRemove = new ArrayList<String>();
		String[] splitStrings = { "", "" };
		ArrayList<String> validKeys = new ArrayList<String>();

		// This will only work if the string is not null
		if (inputStrings != null) {
			// Setup the list of valid keys
			validKeys.add("name");
			validKeys.add("description");
			validKeys.add("defaultValue");
			validKeys.add("allowedValueType");
			validKeys.add("allowedValue");
			validKeys.add("tag");
			validKeys.add("parent");
			validKeys.add("group");
			// Loop over all of the strings and parse the data
			for (String currentString : inputStrings) {
				// Search for any lines that are whitespace are comments
				if (currentString.matches("^$|(?m)^\\s+$") || // Whitespace
						currentString.startsWith("#") || // Comments with #
						currentString.startsWith("//")) { // Comments with //
					continue;
				}
				// Search for comments at the end of the line and remove them
				if (currentString.contains("#")) {
					currentString = currentString.replaceAll("\\#.*", "");
				} else if (currentString.contains("//")) {
					currentString = currentString.replaceAll("\\/\\/.*", "");
				}
				// Make sure the string contains an equal sign or throw an
				// exception
				if (!currentString.contains("=")) {
					throw new IOException(
							"String in Entry block does not contain an "
									+ "equals sign! The string was at line "
									+ inputStrings.indexOf(currentString)
									+ ":\n" + currentString);
				}
				// Trim and split the string
				splitStrings = currentString.trim().split("\\=");
				// Get the name
				if ("name".equals(splitStrings[0])) {
					setName(splitStrings[1]);
				}
				// Get the description
				else if ("description".equals(splitStrings[0])) {
					setDescription(splitStrings[1]);
				}
				// Get the default value
				else if ("defaultValue".equals(splitStrings[0])) {
					this.iEntryContentProvider.setDefaultValue(splitStrings[1]);
				}
				// Get the AllowedValueType
				else if ("allowedValueType".equals(splitStrings[0])) {
					this.iEntryContentProvider
							.setAllowedValueType(AllowedValueType
									.valueOf(splitStrings[1]));
				}
				// Get the AllowedValues
				else if ("allowedValue".equals(splitStrings[0])) {
					allowedValues.add(splitStrings[1]);
					this.iEntryContentProvider.setAllowedValues(allowedValues);
				}
				// Get the tag
				else if ("tag".equals(splitStrings[0])) {
					setTag(splitStrings[1]);
				}
				// Get the parent
				else if ("parent".equals(splitStrings[0])) {
					setParent(splitStrings[1]);
				}
				// Get the group name
				else if ("group".equals(splitStrings[0])) {
					group = splitStrings[1];
				}
				// Check for an invalid tag. If it is found, throw an exception.
				else if (!validKeys.contains(splitStrings[0])) {
					throw new IOException("Invalid PSF statement: "
							+ currentString);
				}
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations returns the group name that was assigned to this
	 * PainfullySimpleEntry in the PSF. This group name is used to determine to
	 * which DataComponent the PainfullySimpleEntry should be added.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The group name
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getGroup() {
		// begin-user-code
		return group;
		// end-user-code
	}
}