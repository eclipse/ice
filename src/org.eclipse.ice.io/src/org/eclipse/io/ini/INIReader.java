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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
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
	 * Constructor using the ! as a comment is 
	 * the default behavior
	 */
	public INIReader() {
		this("!");
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
		// Initialize the form & table row template
		Form iniForm = null;
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry variableTemplate = new Entry();
		Entry valueTemplate = new Entry();
		variableTemplate.setName("Name");
		valueTemplate.setName("Value");
		entries.add(variableTemplate);
		entries.add(valueTemplate);		
		
		// Make sure the file exists before trying to read
		if (file != null && file.exists()) {
			ArrayList<TableComponent> sections = null;
			BufferedReader reader = null;
			
			try {
				// Open up the reader and start reading
				iniForm = new Form();
				reader = new BufferedReader(new InputStreamReader(file.getContents()));
				String line;
				String[] splitLine;
				String section = "Default Section";
				TableComponent sectionTable = new TableComponent();
				sectionTable.setName(section);
				sectionTable.setRowTemplate(entries);	
				while((line = reader.readLine()) != null) {
					// Make sure that comments are taken into consideration
					splitLine = line.split(comment);
					line = splitLine[0];
					
					// If at a new section, add the previous section to 
					// the list and create a new TableComponent for the
					// next section
					Matcher sectionMatch = sectionPattern.matcher(line);
					if (sectionMatch.matches()) {
						iniForm.addComponent(sectionTable);
						sectionTable = new TableComponent();
						sectionTable.setName(sectionMatch.group(0).trim());
						sectionTable.setRowTemplate(entries);
					} else if ((splitLine = line.split(assignmentPattern)).length >= 2) {	
						// Get the key and value and put it in the table
						int rowID = sectionTable.addRow();
						ArrayList<Entry> row = sectionTable.getRow(rowID);
						row.get(0).setValue(splitLine[0].trim());
						row.get(1).setValue(splitLine[splitLine.length - 1].trim()); 
					}
				}
				// Add the last section
				iniForm.addComponent(sectionTable);
			} catch (CoreException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}						
		}
		// Return the form
		return iniForm;
	}
	
	
	/**
	 * Allows the changing of the section assignment
	 * indentation string.
	 * 
	 * @param indent
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
	 * @param regex
	 */
	public void setAssignmentPattern(String regex) {
		assignmentPattern = regex;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReaderType() {
		return "INIReader";
	}

}
