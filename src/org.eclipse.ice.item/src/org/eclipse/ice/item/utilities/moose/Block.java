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
package org.eclipse.ice.item.utilities.moose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class represents a MOOSE input block. This class is parsed from and
 * written to YAML using SnakeYAML's Java Bean parser. It can also be converted
 * to a GetPot compatible string and loaded from a map and a TreeComposite.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Block {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String name = "";
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The type of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String type = "";
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A description of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String description = "";
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The parameters of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The subblocks that belong to this block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<Block> subblocks = new ArrayList<Block>();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The GetPot prefix for the name of the current, actual section.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static final String actualSectionPrefix = "./";
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The GetPot prefix for the name of parent section.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static final String parentSectionPrefix = "../";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the Block should be considered active, false if not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected boolean active = false;

	/**
	 * The amount that this block should be indented when written to GetPot.
	 */
	protected String indent = "";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the name of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The name
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the type of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The type
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getType() {
		// begin-user-code
		return type;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the description of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The description
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getDescription() {
		// begin-user-code
		return description;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the parameters of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The parameters.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Parameter> getParameters() {
		// begin-user-code
		return parameters;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the subblocks of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The blocks
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Block> getSubblocks() {
		// begin-user-code
		return subblocks;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the parameters of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param params
	 *            <p>
	 *            The parameters
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setParameters(ArrayList<Parameter> params) {
		// begin-user-code
		parameters = params;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the name of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param blockName
	 *            <p>
	 *            The name
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setName(String blockName) {
		// begin-user-code
		name = blockName;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the description of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param desc
	 *            <p>
	 *            The description
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDescription(String desc) {
		// begin-user-code
		description = desc;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the type of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param blockType
	 *            <p>
	 *            The type
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setType(String blockType) {
		// begin-user-code
		type = blockType;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the subblocks of the block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param blocks
	 *            <p>
	 *            The blocks
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSubblocks(ArrayList<Block> blocks) {
		// begin-user-code
		subblocks = blocks;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations returns an ICE TreeComposite for the Block. Any
	 * parameters in the block are contained in a DataComponent as Entries
	 * created by calling Parameter.toEntry() on each Parameter. The id of the
	 * data component is 1. Subblocks are configured as children of the tree
	 * composite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The tree composite.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TreeComposite toTreeComposite() {
		// begin-user-code
		
		// Local Declarations
		TreeComposite treeComp = new TreeComposite();
		DataComponent dataComp = new DataComponent();

		// Setup the tree composite info
		treeComp.setName(name);
		treeComp.setDescription(description);
		treeComp.setActive(active);

		// Setup the data component
		dataComp.setName(name + " Parameters");
		dataComp.setId(1);
		// Add the parameters to the list if they exist
		if (parameters != null) {
			for (Parameter param : parameters) {
				dataComp.addEntry(param.toEntry());
			}
		}
		// Add the data component as a data node
		treeComp.addComponent(dataComp);

		// Add the subblocks to the tree if there are any. i+2 is used for the
		// id because the DataComponent has id = 1.
		if (subblocks != null) {
			for (int i = 0; i < subblocks.size(); i++) {
				TreeComposite childTreeComp = subblocks.get(i)
						.toTreeComposite();
				childTreeComp.setId(i + 2);
				treeComp.setNextChild(childTreeComp);
			}
		}

		return treeComp;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the block from an untyped Map. It should only be
	 * used with a Map created by SnakeYAML.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param map
	 *            <p>
	 *            The map.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromMap(Map map) {
		// begin-user-code

		// Local Declarations
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		ArrayList<Block> subblocks = new ArrayList<Block>();
		String mapName = null;
		int slashIndex = -1;

		// Get the name
		if (map.get("name") != null) {
			// Grab the name
			mapName = (String) map.get("name");
			// Check it to make sure that we do not have the fully hierarchical
			// path name. If we do, set the name to the last name in the path.
			slashIndex = mapName.lastIndexOf("/");
			mapName = mapName.substring(slashIndex + 1);
			// Set the final block name
			setName(mapName);
		}
		// ... the description
		if (map.get("description") != null) {
			setDescription((String) map.get("description"));
		}
		// ... and the type
		if (map.get("type") != null) {
			setType((String) map.get("type"));
		}

		// Load the parameters
		if (map.get("parameters") != null) {
			ArrayList<Map> paramsMap = (ArrayList) map.get("parameters");
			for (Map singleParamMap : paramsMap) {
				Parameter param = new Parameter();
				// Get the strings - name
				if (singleParamMap.get("name") != null) {
					param.setName((String) singleParamMap.get("name"));
				}
				// description
				if (singleParamMap.get("description") != null) {
					param.setDescription((String) singleParamMap
							.get("description"));
				}
				// cpp_type
				if (singleParamMap.get("cpp_type") != null) {
					param.setCpp_type((String) singleParamMap.get("cpp_type"));
				}
				// group_name
				if (singleParamMap.get("group_name") != null) {
					param.setGroup_name((String) singleParamMap
							.get("group_name"));
				}
				// default
				if (singleParamMap.get("default") != null) {
					param.setDefault((String) singleParamMap.get("default"));
				}
				// Get the boolean required flag
				if (singleParamMap.get("required") != null) {
					boolean isRequired = 
							(Boolean) singleParamMap.get("required");
					param.setRequired(isRequired);
					param.setEnabled(isRequired);
				}

				// Add the parameter to the list
				parameters.add(param);
			}
			// Add the parameters to the block
			setParameters(parameters);
		}

		// Load the sub blocks
		if (map.get("subblocks") != null) {
			for (Map submap : (ArrayList<Map>) map.get("subblocks")) {
				// Load the block from the map
				Block subblock = getNewBlock();
				subblock.loadFromMap(submap);
				// Add the block to the set
				subblocks.add(subblock);
			}
			// Add the subblocks to the block
			setSubblocks(subblocks);
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation converts the Block to a standard GetPot-compatible format
	 * and returns it as a string. If the name of the Block starts with "/", the
	 * prefix will be changed so that the name will not start with ".//" or
	 * "..//". Only active blocks (isActive == true) are evaluated for writing.
	 * All active parameters marked as required (isRequired == true) are written
	 * to output. Additionally, parameters that may not be required, but are
	 * enabled, are also written out. All other parameters are written, but
	 * commented out.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param prefix
	 *            <p>
	 *            The prefix for the name of the block. This is most commonly
	 *            null or the actual section prefix (Block.actualSectionPrefix).
	 *            If the prefix is not equal to Block.actualSectionPrefix, it
	 *            will be replaced with an empty string.
	 *            </p>
	 *            <p>
	 *            The prefix is used in GetPot to note the relationship of a
	 *            section with its parent. In MOOSE it is either null or equal
	 *            to the actual prefix. In the latter case the section is always
	 *            closed by the parent section prefix
	 *            (Block.parentSectionPrefix). These are "./" and "../" for
	 *            MOOSE, respectively and without the quotation marks.
	 *            </p>
	 * @return <p>
	 *         The GetPot representation of this Block.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String toGetPot(String prefix) {
		// begin-user-code

		// Local Declarations
		String realPrefix = (prefix != null) ? prefix : "";
		String openingSection = (realPrefix != null && realPrefix
				.equals(actualSectionPrefix)) ? actualSectionPrefix + name
				: name;
		String closingSection = (realPrefix.equals(Block.actualSectionPrefix)) ? parentSectionPrefix
				: "";
		String potString = "", commentString = "", whiteSpaceString = "", hash = "";
		boolean hasComment = false;

		// Fix the indentation
		indent += (realPrefix.equals(actualSectionPrefix)) ? "  " : "";

		// Only write the block if it is active!
		if (isActive()) {

			// Open the section
			potString = indent + "[" + openingSection + "]";
			
			// Check if this section block has a comment, if it does, append it
			hasComment = !description.isEmpty();
			if (hasComment) {
				// Remove newline characters from comment
				commentString = description.replaceAll("[\n\r]", "");
				// Append the line
				whiteSpaceString = makeWhiteSpaceString(potString);
				potString += String.format("%s# %s\n", whiteSpaceString, commentString);
			} else {
				potString += "\n";
			}
			
			// Add the parameters to the string
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					
					// Get the parameter, determine if it has a comment
					Parameter param = parameters.get(i);
					hasComment = !param.getDescription().isEmpty();
					
					// Always write out required parameters
					if (param.isRequired()) {
						
						if (hasComment) {
							// Remove newline characters from comment
							commentString = param.getDescription().
									replaceAll("[\n\r]", "");
							// Append the line
							whiteSpaceString = makeWhiteSpaceString(indent + "  " + param.toString());
							potString += String.format("%s  %s%s# %s\n",
									indent, param.toString(), whiteSpaceString,commentString);
						} else {
							potString += indent + "  " + param.toString() + "\n";
						}
					}
					// If the parameter is not explicitly required, check if it
					// is currently enabled
					else if (param.isEnabled()) {
						if (hasComment) {
							// Remove newline characters from comment
							commentString = param.getDescription().
									replaceAll("[\n\r]", "");
							// Append the line
							whiteSpaceString = makeWhiteSpaceString(indent + "  " + param.toString());
							potString += String.format("%s  %s%s# %s\n",
									indent, param.toString(), whiteSpaceString, commentString);
						} else {
							potString += indent + "  " + param.toString() + "\n";
						}
					} 
					else {
						if (hasComment) {
							// Remove newline characters from comment
							commentString = param.getDescription().
									replaceAll("[\n\r]", "");
							// Append the line
							whiteSpaceString = makeWhiteSpaceString(indent + "  " + param.toString());
							potString += String.format("%s# %s%s# %s\n",
									indent, param.toString(), whiteSpaceString, commentString);
						} else {
							potString += indent + "# " + param.toString() + "\n";
						}
					}
				}
			}

			// Add the subblocks to the string
			if (subblocks != null) {
				for (int i = 0; i < subblocks.size(); i++) {
					Block block = subblocks.get(i);
					potString += block.toGetPot(Block.actualSectionPrefix,
							indent);
				}
			}

			// Close the section
			potString += indent + "[" + closingSection + "]\n";

		}

		// Reset the indent prefix if it was originally null
		indent = "";

		return potString;
		// end-user-code
	}

	/**
	 * This is a utility method that will construct and return a String of
	 * whitespaces, depending on the length of the current line. This is used
	 * by the toGetPot() method when appending comments, to try to make comments
	 * align in columns for neatness' sake.
	 * 
	 * @param getPotLine	Current line that the comment will be appended to
	 * @return				A string of whitespace characters that will separate
	 * 						the current line's text from its comment
	 */
	private String makeWhiteSpaceString(String getPotLine) {
	
		String whiteSpaceString = "";
		int whiteSpaceCount = 0, lineLength = 0;
		
		lineLength = getPotLine.length();
		
		if (lineLength < 30) {
			whiteSpaceCount = 30 - lineLength;
		} else if (lineLength < 45) {
			whiteSpaceCount = 45 - lineLength;
		} else if (lineLength < 60) {
			whiteSpaceCount = 60 - lineLength;
		} else {
			whiteSpaceCount = 15;
		}
		
		for (int i = 0; i < whiteSpaceCount; i++) {
			whiteSpaceString += " ";
		}
		
		return whiteSpaceString;
	}
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation converts the Block to a standard GetPot-compatible format
	 * and returns it as a string. If the name of the Block starts with "/", the
	 * prefix will be changed so that the name will not start with ".//" or
	 * "..//". Only active blocks (isActive == true) are written. This version
	 * also takes an string that can be used to indent the GetPot block by a
	 * certain amount.
	 * </p>
	 * <p>
	 * This version is primarily used by the Block itself when dumping its
	 * children.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param prefix
	 *            <p>
	 *            The prefix for the name of the block. This is most commonly
	 *            null or the actual section prefix (Block.actualSectionPrefix).
	 *            If the prefix is not equal to Block.actualSectionPrefix, it
	 *            will be replaced with an empty string.
	 *            </p>
	 *            <p>
	 *            The prefix is used in GetPot to note the relationship of a
	 *            section with its parent. In MOOSE it is either null or equal
	 *            to the actual prefix. In the latter case the section is always
	 *            closed by the parent section prefix
	 *            (Block.parentSectionPrefix). These are "./" and "../" for
	 *            MOOSE, respectively and without the quotation marks.
	 *            </p>
	 * @param depthIndent
	 *            The indentation of the block
	 * @return <p>
	 *         The GetPot representation of this Block.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String toGetPot(String prefix, String depthIndent) {
		// begin-user-code

		indent += depthIndent;

		return toGetPot(prefix);
		// end-user-code
	}

	/**
	 * An alternative version of fromGetPot that uses a properly assembled array
	 * list of the lines. Each line of the block is a separate element of the
	 * array. This call otherwise functions as described by the other fromGetPot
	 * operation.
	 * 
	 * All blocks loaded from GetPot will be marked as active so that they can
	 * be rewritten by calling Block.toGetPot().
	 * 
	 * @param potLines
	 *            The array list of lines created from fromGetPot().
	 */
	public void fromGetPot(ArrayList<String> potLines) {

		// Local declarations
		int subBlockCounter = 0, subBlockLineId = 0;
		String line = "", subBlockLine = "";
		String parameterName = "", parameterValue = "", parameterComment = "";

		// Get the name of the block, set it as active
		name = potLines.get(0).trim();
		active = true;
			
		// Check if this block has a comment, separate it if it does
		if (name.contains("#")) {
			
			// Determine if the whole line is commented out, or there's an
			// in-line comment
			int firstHash = name.indexOf("#");
			int lastHash = name.lastIndexOf("#");
			String comment = "";
			
			// If the whole line is commented out w/o an in-line comment
			if (firstHash == 0 && firstHash == lastHash) {
				// Set the name (without the hash), mark as inactive
				name = name.substring(1).trim();
				active = false;
			}
			// If there's only an in-line comment
			else if (firstHash > 0 && firstHash == lastHash) {
				// Split the name and comment up
				comment = name.substring(firstHash + 1).trim();
		
				// Set the name and comment separately
				description = comment;
				name = name.substring(0, firstHash).trim();
			}
			// If the whole line is commented out with an in-line comment too
			else {
				// Split the name and comment up
				comment = name.substring(lastHash + 1).trim();
		
				// Set the name and comment separately, set as inactive
				description = comment;
				name = name.substring(firstHash + 1, lastHash).trim();
				active = false;
			}
			
		}
		
		
		// The name has to be checked to determine whether or not it is a
		// subblock and the opening characters should be skipped.
		name = (name.contains("./")) ? name.substring(3, name.length() - 1)
				: name.substring(1, name.length() - 1);

		// What is this?! ~JJB 20140701 12:16
		boolean debug = false;
		if ("Variables".equals(name)) {
			debug = false;
		}

		boolean parameterEnabled = true;
		// Loop over the remaining lines and load the parameters and sub blocks.
		for (int i = 1; i < potLines.size(); i++) {
			// Get the line and trim it
			line = potLines.get(i).trim();
			
			// Reset the enabled flag if it's been used
			parameterComment = "";
			parameterEnabled = true;
			
			// Load the line as a parameter if it doesn't start with # or [ and
			// contains =.
			if (!line.startsWith("[") && line.contains("=")) {

				// Split the line at the "="
				String[] parameterPieces = line.split("=");
				
				// A basic 'name = value' parameter would have 2 elements in
				// parameterPieces, but if for whatever reason there are more
				// than 2 (a '=' in the comment), merge elements 1 to n together
				if (parameterPieces.length > 2) {
					for (int j = 1; j < parameterPieces.length; j++) {
						parameterPieces[1] += "=" + parameterPieces[j];
					}
				}

				// Check if this parameter has a comment
				if (parameterPieces[0].contains("#") || 
						(parameterPieces.length > 1 
								&& parameterPieces[1].contains("#"))) {
					
					// Check if the whole line is commented out
					if (parameterPieces[0].startsWith("#")) {
						parameterPieces[0] = 
								parameterPieces[0].substring(1).trim();
						parameterEnabled = false;
						
					}
					
					// Check if there's an inline comment as well
					if (parameterPieces.length > 1 
							&& parameterPieces[1].contains("#")) {
						// Split the parameter value and comment up
						int commentIndex = parameterPieces[1].lastIndexOf("#");
						parameterComment = parameterPieces[1]
								.substring(commentIndex + 1).trim();
						
						// Chop the comment off the parameter value
						parameterPieces[1] = parameterPieces[1]
								.substring(0, commentIndex).trim();
					}
				}

				// Check if the next line contains a "=" (new parameter) or
				// is the end of the block; if yes to either, then proceed
				// as usual
				if (((i + 1) < potLines.size() && (potLines.get(i + 1)
						.contains("=") || potLines.get(i + 1).trim()
						.startsWith("[")))
						|| i == (potLines.size() - 1)) {
					// Set the parameter name and value
					parameterName = parameterPieces[0].trim();
					parameterValue = (parameterPieces.length > 1) ? parameterPieces[1]
							.trim() : "";
				}
				// Otherwise, if it doesn't contain a "=" (new parameter) or
				// is the end of a block, then the value for this parameter
				// spans multiple lines, and we must continue reading until
				// we find its end
				else if ((i + 1) < potLines.size()
						&& !potLines.get(i + 1).trim().startsWith("[")) {

					// Search for how many lines this parameter spans
					int numValueLines = 0;
					for (int j = 1; j <= (potLines.size() - i); j++) {
						// Check if the next line has a new parameter or is the
						// start/end of a block
						if ((i + j) < potLines.size()
								&& (potLines.get(i + j).contains("=") || potLines
										.get(i + j).trim().startsWith("["))) {
							// Set the number of lines and stop here
							numValueLines = j;
							break;
						}
						// Otherwise, if this is the last line, set the number
						// of lines
						else if (j == (potLines.size() - i)) {
							numValueLines = j;
						}
					}
					// Begin constructing the value from the multiple lines
					String multiLineValue = (parameterPieces.length > 1) ? parameterPieces[1]
							.trim() : "";
					for (int j = 1; j < numValueLines; j++) {
						multiLineValue += " " + potLines.get(i + j).trim();
					}

					// Set the parameter name and value
					parameterName = parameterPieces[0].trim();
					parameterValue = multiLineValue;

					// Update i to skip over the lines we just read
					i += (numValueLines - 1);
				}
				
				// Create the new parameter
				Parameter tmpParam = new Parameter();
				tmpParam.setName(parameterName);
				tmpParam.setDefault(parameterValue);
				tmpParam.setDescription(parameterComment);
				// Mark the parameter as required since it will be reused
//				tmpParam.setRequired(true);
				tmpParam.setEnabled(parameterEnabled);

				if (debug) {
					System.out.println("[Block] Adding Parameter "
							+ tmpParam.getName());
				}
				// Load it into the list
				parameters.add(tmpParam);

			} else if (line.contains("[./")) {
				// Increment the subblock counter so that we can correctly count
				// through the blocks. We have to count them so that we know
				// when to break out of the loop because blocks can be nested.
				if (debug) {
					System.out.println("[Block] SubBlock is " + i + " " + line);
				}
				++subBlockCounter;
				// Count over the rest of the lines of the block until the
				// correct number of nested blocks is read.
				while (subBlockCounter > 0) {
					// Go to the next line and read it
					++subBlockLineId;
					subBlockLine = potLines.get(i + subBlockLineId).trim();
					if (debug) {
						System.out.println("[Block] " + subBlockLine + " "
								+ subBlockLineId + " " + subBlockCounter);
					}
					// Note that another block has been encountered if the [
					// character is found.
					if (subBlockLine.contains("[./")) {
						++subBlockCounter;
					} else if (subBlockLine.contains("[../]")) {
						--subBlockCounter;
					}
				}
				if (debug) {
					System.out.println("[Block] Indices: " + i + " "
							+ subBlockLineId + " " + potLines.size());
				}
				// Create the array list of lines for the sub block
				ArrayList<String> subBlockLines = new ArrayList<String>(
						potLines.subList(i, i + subBlockLineId));
				// Create the new block and load it
				Block subBlock = getNewBlock();
				subBlock.fromGetPot(subBlockLines);
				// Add the subblock to the list
				subblocks.add(subBlock);
				// Update the counters so that the next line is properly read

				i += subBlockLineId;

				// AQW Added code here to fix bug
				subBlockCounter = 0;
				subBlockLineId = 0;
			}
		}

		return;
	}

	/**
	 * This operation loads the Block based on the content of a
	 * GetPot-compatible String.
	 * 
	 * GetPot data has much less information than YAML. The only things that
	 * will be loaded are the name of the block, its parameters and its
	 * subblocks.
	 * 
	 * This operation expects that it will be given a single, complete block
	 * that starts with the name and ends with the closing characters. It will
	 * not handle multiple blocks.
	 * 
	 * @param getPotString
	 *            The string that contains the Block information in GetPot
	 *            format.
	 */
	public void fromGetPot(String getPotString) {
		// TODO Auto-generated method stub

		// Create the array list from the string by splitting and converting to
		// a List.
		ArrayList<String> potLines = new ArrayList<String>(
				Arrays.asList(getPotString.split("\n")));

		// Delegate to the private version
		fromGetPot(potLines);

		return;
	}

	/**
	 * This operation overrides toString() to write this class as a String in
	 * GetPot format.
	 */
	@Override
	public String toString() {
		return toGetPot(null);
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a fully-initialized Block from a TreeComposite. It
	 * expects the same format as that produced from toTreeComposite() and will
	 * create a malformed Block if it receives something else.
	 * </p>
	 * <p>
	 * This operation should ONLY be used to create a Block that will be written
	 * to a MOOSE input file because it depends on the creation of Parameters
	 * from Entries and there is no 1-1 mapping between those entities.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param comp
	 *            <p>
	 *            The TreeComposite that should be loaded.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void fromTreeComposite(TreeComposite comp) {
		// begin-user-code

		// Only load up if it is possible
		if (comp != null) {
			// Set the name, description and active state
			name = comp.getName();
			description = comp.getDescription();
			active = comp.isActive();
			// Clear the subblock list and parameters
			subblocks = new ArrayList<Block>();
			parameters = new ArrayList<Parameter>();
			// Convert the parameters
			try {
				// Get the DataComponent of Entries that are Parameters
				ArrayList<Component> dataComps = comp.getDataNodes();
				if (!(dataComps.isEmpty())) {
					DataComponent compParameters = (DataComponent) dataComps
							.get(0);
					if (compParameters != null) {
						// Loop over all the Entries and convert them to
						// Parameters
						for (Entry paramEntry : compParameters
								.retrieveAllEntries()) {
							Parameter param = new Parameter();
							param.fromEntry(paramEntry);
							// Add the parameter to this block's subset
							parameters.add(param);
						}
					}
				}
			} catch (ClassCastException e) {
				System.out.println("Block.fromTreeComposite() Message: "
						+ "Unexpected component in TreeComposite. Aborting.");
			}
			// Convert the children to subblocks
			if (comp.getNumberOfChildren() > 0) {
				for (int i = 0; i < comp.getNumberOfChildren(); i++) {
					TreeComposite child = comp.getChildAtIndex(i);
					Block subBlock = getNewBlock();
					subBlock.fromTreeComposite(child);
					// Add the subblock to this block's subset
					subblocks.add(subBlock);
				}
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a new instance of Block. It can be overridden by
	 * subclasses to create the proper instance of Block subclasses (e.g. -
	 * YAMLBlock) during the recursive walking operations on Block.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new Block, possibly of a subclass.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Block getNewBlock() {
		// begin-user-code
		return new Block();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation indicates if the block is Active. True if Active, false if
	 * not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if Active, false if not
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isActive() {
		// begin-user-code
		return active;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets specifies whether or not the block is Active. True
	 * for Active, false if not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param flag
	 *            <p>
	 *            True if Active, false if not
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setActive(boolean flag) {
		// begin-user-code
		active = flag;
		return;
		// end-user-code
	}

}