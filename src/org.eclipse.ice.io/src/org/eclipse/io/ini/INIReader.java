/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.io.ini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.io.serializable.IReader;

/**
 * The INIReader provides functionality for parsing files that use the INI structure.
 * 
 * @author Andrew Bennett
 *
 */
public class INIReader implements IReader {

	/**
	 * The character to use as a comment symbol
	 */
	private String comment;
	
	/**
	 * A string that can be used if there is an unusual 
	 * prefix before assignments within sections
	 */
	private String sectionIndent = "";
	
	/**
	 * Regex to match the section headers
	 */
	private Pattern sectionPattern = Pattern.compile("\\[.*\\]");
	
	/**
	 * Regex to match the variable assignments.  The default
	 * matches =, multiple spaces after test, or tabs after
	 * text, in that order of precedence.
	 */
	private String assignmentPattern = "(=|\\b(\\s)+?|\\b\\t)";
	
	/**
	 * The type of template to use.  If null just read a bare ini file
	 */
	private IFile templateFile = null;
	
	/**
	 * Stores the template type to the name of the template file.  Template files
	 * should be organized by:
	 *   [section names]
	 *   variable = default value ; list, of, possible, values ; AllowedValueType
	 */
	private HashMap<String,IFile> templateMap = new HashMap<String, IFile>();
	
	/**
	 * Keep track of which TableComponent each template variable is in so we can
	 * load the actual file more quickly
	 */
	private HashMap<String, Integer> variableToComponentNumber = new HashMap<String, Integer>();
	
	
	/**
	 * Constructor using the ! as a comment is 
	 * the default behavior
	 */
	public INIReader() {
		this(";");
	}
	
	
	/**
	 * Constructor allowing the specification of 
	 * different comment symbols.
	 * 
	 * @param c: the symbol to use to specify a comment
	 */
	public INIReader(String c) {
		comment = c;
	}
	
	
	/**
	 * Read through an INI file by reading each section.
	 * Declarations that are not in a section (ie before
	 * a section has been declared) will be put into their
	 * own category.
	 * 
	 * @param file: the INI file to read
	 */
	@Override
	public Form read(IFile file) {
		// Initialize the form
		Form iniForm = null;
		
		// Check if we are building a templated form and if so,
		// make the template, then read in the file to the template
		if (templateFile != null) {
			iniForm = buildTemplate();
			iniForm = loadIntoTemplate(iniForm, file);
		} else {
			// Build the default version of the form
			// Make sure the file exists before trying to read
			if (file != null && file.exists()) {
				ArrayList<DataComponent> sections = null;
				BufferedReader reader = null;
				
				try {
					// Open up the reader and start reading
					iniForm = new Form();
					reader = new BufferedReader(new InputStreamReader(file.getContents()));
					String line;
					String[] splitLine;
					String section = "Default Section";
					DataComponent sectionComp = new DataComponent();
					sectionComp.setName(section);	
					while((line = reader.readLine()) != null) {
						// Make sure that comments are taken into consideration
						splitLine = line.split(comment);
						line = splitLine[0].trim();
						// If at a new section, add the previous section to 
						// the list and create a new DataComponent for the
						// next section
						Matcher sectionMatch = sectionPattern.matcher(line);
						if (sectionMatch.matches()) {
							if (sectionComp.retrieveAllEntries().size() > 0) {
								iniForm.addComponent(sectionComp);
							}
							sectionComp = new DataComponent();
							sectionComp.setName(sectionMatch.group(0).trim().replace("[","").replace("]",""));
						} else if ((splitLine = line.split(assignmentPattern)).length >= 2) {	
							// Get the key and value and put it in the Data Component
							String var = splitLine[0].trim();
							String val = splitLine[splitLine.length - 1].trim();
							sectionComp.addEntry(makeTemplateEntry(var, val, new ArrayList<String>(), AllowedValueType.Undefined));
						}
					}
					// Add the last section if needed
					if (sectionComp.retrieveAllEntries().size() > 0) {
						iniForm.addComponent(sectionComp);
					}
				} catch (CoreException e) {
					e.printStackTrace();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}	
			}
		}
		// Return the form
		return iniForm;
	}
	
	
	/**
	 * If a template is used it can be loaded with this method.  
	 * 
	 * @return the default form from the template
	 */
	private Form buildTemplate() {
		Form templateForm = new Form();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry variableTemplate = new Entry();
		Entry valueTemplate = new Entry();
		int componentNumber = 0;
		variableTemplate.setName("Name");
		valueTemplate.setName("Value");
		entries.add(variableTemplate);
		entries.add(valueTemplate);		
		
		// Make sure the file exists before trying to read
		if (templateFile != null && templateFile.exists()) {
			BufferedReader reader = null;
			try {
				// Open up the reader and start reading
				reader = new BufferedReader(new InputStreamReader(templateFile.getContents()));
				String line, varName, defaultVal;
				String[] templateSections;
				String section = "Default Section";
				DataComponent sectionComp = new DataComponent();
				String[] valueArray;
				ArrayList<String> allowedValues;
				AllowedValueType valueType;
				sectionComp.setName(section);
				while((line = reader.readLine()) != null) {
					// If at a new section, add the previous section to 
					// the list and create a new TableComponent for the
					// next section
					Matcher sectionMatch = sectionPattern.matcher(line);
					if (sectionMatch.matches()) {
						if (sectionComp.retrieveAllEntries().size() > 0) {
							templateForm.addComponent(sectionComp);
							++componentNumber;
						}
						// Set up the new data component
						sectionComp = new DataComponent();
						sectionComp.setName(sectionMatch.group(0).trim().replace("[","").replace("]",""));
					} else if ((templateSections = line.split(";")).length >= 2) {	
						// Get the key and value and put it in the table
						varName = templateSections[0].split("=")[0].trim();
						defaultVal = templateSections[0].split("=")[1].trim();
						valueArray = templateSections[1].split(",\\s");
						allowedValues = new ArrayList<String>(Arrays.asList(valueArray));
						
						// If there are allowed value types, get them
						if (templateSections.length > 2) {
							valueType = AllowedValueType.valueOf(templateSections[2].trim());
						} else {
							valueType = AllowedValueType.Undefined;
						}
						
						// Create the new entry & store which component it goes in
						Entry newEntry = makeTemplateEntry(varName, defaultVal, allowedValues, valueType);
						sectionComp.addEntry(newEntry);
						variableToComponentNumber.put(varName, sectionComp.getId());
					}
				}
				// Add the last section if needed
				if (sectionComp.retrieveAllEntries().size() > 0) {
					templateForm.addComponent(sectionComp);
				}
			} catch (CoreException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}						
		}
		
		
		return templateForm;
	}
	
	
	/**
	 * Update the form to have the correct data from the input file
	 * 
	 * @param form : The template form
	 * @param file : The file to use to update the form
	 * @return the updated form
	 */
	private Form loadIntoTemplate(Form form, IFile file) {
		// Initialize the form & table row template
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry variableTemplate = new Entry();
		Entry valueTemplate = new Entry();
		variableTemplate.setName("Name");
		valueTemplate.setName("Value");
		entries.add(variableTemplate);
		entries.add(valueTemplate);		

		if (file != null && file.exists()) {
			ArrayList<TableComponent> sections = null;
			BufferedReader reader = null;
			
			try {
				// Open up the reader and start reading
				reader = new BufferedReader(new InputStreamReader(file.getContents()));
				String line, var, val;
				String[] splitLine;
				String section = "Default Section";
				boolean foundInTemplate;
				int rowNumber;
				DataComponent sectionTable = new DataComponent();
				sectionTable.setName(section);
				while((line = reader.readLine()) != null) {
					
					// Make sure that comments are taken into consideration
					splitLine = line.split(comment)[0].trim().split(assignmentPattern);
					if (splitLine.length >= 2) {	
						
						// Get the variable name and value
						var = splitLine[0].trim();
						val = splitLine[1].trim();
						foundInTemplate = false;
						
						// Try to find the variable in the existing form.  If it exists update the data component
						if (variableToComponentNumber.containsKey(var)) {
							DataComponent comp = (DataComponent) form.getComponent(variableToComponentNumber.get(var));
							for (Entry ent : comp.retrieveAllEntries()) {
								if (ent.getName().equals(var)) {
									ent.setValue(val);
									foundInTemplate = true;
								}
							}
						}
						
						// If the variable didn't exist add it to the first data componenet
						if (!foundInTemplate) {
							DataComponent comp = (DataComponent) form.getComponents().get(0);
							Entry newEntry = makeTemplateEntry(var, val, new ArrayList<String>(), AllowedValueType.Undefined);
							comp.addEntry(newEntry);
						}
					}
				}	
			} catch (CoreException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}	
		}
		// Return the form
		return form;
	}
	
	
	/**
	 * Initialize an entry with the correct allowed values
	 * 
	 * @return the entry
	 */
	private Entry makeTemplateEntry(final String name, final String defaultVal, final ArrayList<String> allowedVals, 
			final AllowedValueType valueType) {
		Entry entry = new Entry() {
			protected void setup() {
				this.setName(name);
				this.tag = "";
				this.ready = true;
				this.setDescription("");
				this.allowedValues = allowedVals;
				this.defaultValue = defaultVal;
				this.value = this.defaultValue;
				this.allowedValueType = valueType;
			}
		};

		return entry;
	}	
	
	/**
	 * Set what the reader should consider to be the comment string
	 * in the ini file
	 * 
	 * @param comm: The comment string to look for
	 */
	public void setCommentString(String comm) {
		comment = comm;
	}
	
	/**
	 * Allows the changing of the section assignment
	 * indentation string.
	 * 
	 * @param indent: Set how to indent on sections
	 */
	public void setIndentString(String indent) {
		sectionIndent = indent;
	}
	
	
	/**
	 * Allows the changing of the syntax for section 
	 * headers.
	 * 
	 * @param regex: A regular expression to match to 
	 *               section headers
	 */
	public void setSectionPattern(String regex) {
		sectionPattern = Pattern.compile(regex);
	}
	
	
	/**
	 * Allows the changing of the syntax for variable
	 * assignment
	 * 
	 * @param regex: How to search for variable assignments
	 */
	public void setAssignmentPattern(String regex) {
		assignmentPattern = regex;
	}
	
	
	/**
	 * Set the template file used to validate that the form is correctly
	 * set up.
	 * 
	 * @param template: the type of template that should be used to validate 
	 *                  the form after calling read()
	 */
	public void setTemplateType(String template) {
		if (templateMap.containsKey(template)) {
			templateFile = templateMap.get(template);
		} else {
			templateFile = null;
		}
	}
	
	
	/**
	 * Add a new template to the INIReader that can be used to validate forms.
	 * 
	 * @param name: The name to reference the template file with
	 * @param file: The template file
	 */
	public void addTemplateType(String name, IFile file) {
		// We are going remove the template if it exists
		if (templateMap.containsKey(name)) {
			templateMap.remove(name);
		}
		
		// Put in the new version of the template
		templateMap.put(name, file);
	}
	
	
	/**
	 * Searches a given IFile for content that matches a given regular
	 * expression. Returns all instances that match.
	 * 
	 * @param ifile
	 *            The file to search in
	 * @param regex
	 *            A string representing a regular expression containing the
	 *            specification of what to search for
	 * @return an ArrayList<Entry> with the description set to the regex and the
	 *         value set to the results.
	 */
	@Override
	public ArrayList<Entry> findAll(IFile file, String regex) {
		System.err.println("INIReader Message: The findAll method contains no implementation!");
		return null;
	}

	/**
	 * Return that this is an INIReader
	 * 
	 * @return INIReader
	 */
	@Override
	public String getReaderType() {
		return "INIReader";
	}

}
