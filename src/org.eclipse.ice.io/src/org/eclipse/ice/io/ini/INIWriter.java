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
package org.eclipse.ice.io.ini;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.io.serializable.IWriter;


/**
 * The INIWriter provides functionality for writing a group of TableComponents to a 
 * text file with the INI structure.
 * 
 * @author Andrew Bennett
 *
 */
public class INIWriter implements IWriter {
	
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
	private String sectionPrefix = "[";
	private String sectionPostfix = "]";
	
	/**
	 * Regex to match the variable assignments.  The default
	 * matches =, multiple spaces after test, or tabs after
	 * text, in that order of precedence.
	 */
	private String assignmentPattern = "(=|\\b(\\s)+?|\\b\\t)";
	
	
	/**
	 * Constructor using the = as the way to assign variables
	 * to their values is the default behavior
	 */
	public INIWriter() {
		this("=");
	}
	
	
	/**
	 * Constructor for using a custom variable assignment string
	 */
	public INIWriter(String pattern) {
		assignmentPattern = pattern;
	}
	
	
	/**
	 * Writes out an INI file from the given Form to the given IFile.
	 * 
	 * @param form
	 *            The form containing the data to write.
	 * @param ifile
	 *            The file to write to.
	 */
	@Override
	public void write(Form form, IFile file) {
		// Make sure the input isn't null
		if (form == null || file == null) {
			return;
		}

		// Get the components from the form
		ArrayList<Component> components = form.getComponents();

		// Make sure that the form had data
		if (components != null) {
			try {
				PipedInputStream in = new PipedInputStream(8196);
				PipedOutputStream out = new PipedOutputStream(in);
				DataComponent dataComp;
				String tableContents, compName, currIndent;
				Boolean inSection;
				String newLine = System.getProperty("line.separator");
				ArrayList<Entry> row;
				byte[] byteArray;
				
				// Make sure that we have a file to write to before proceeding
				if (!file.exists()) {
					byte[] blank = "".getBytes();
					InputStream s = new ByteArrayInputStream(blank);
					file.create(s, true, new NullProgressMonitor());
				}
				
				// Each component corresponds to a section in the INI file
				for (Component comp : components) {
					dataComp = (DataComponent) comp;
					compName = dataComp.getName();
					
					// If the section had a name start by adding that
					// Otherwise, just leave it blank
					// Then set the indentation required accordingly
					if (compName != "Default Section") {
						tableContents = sectionPrefix + compName + sectionPostfix + newLine;
						currIndent = sectionIndent;
					} else {
						tableContents = "";
						currIndent = "";
					}
					
					// Now go through the rows and add each variable
					for (Entry ent : dataComp.retrieveAllEntries()){
						tableContents += ent.getName().trim() + assignmentPattern
								+ ent.getValue().trim() + newLine;
					}
					tableContents += newLine;

					// Write it out
					byteArray = tableContents.getBytes();
					out.write(byteArray);
				}
				
				// Close the stream and set the file contents
				out.close();
				file.setContents(in, true, false, new NullProgressMonitor());
			} catch (FileNotFoundException e) {
				System.out.println("INIWriter Message: Could not find "
						+ file.getName() + " for writing.");
			} catch (IOException e) {
				System.out.println("INIWriter Message: Could not write to "
						+ file.getName() + " du to an IO error");
			} catch (CoreException e) {
				System.out.println("INIWriter Message: Could not write to "
						+ file.getName() + " due to an ICE Core error.");
			}
		}
	}

	
	/**
	 * Change the string used to indent on sections
	 */
	public void setSectionIndentation(String indent) {
		sectionIndent = indent;
	}

	/**
	 * Allows the changing of the syntax for section 
	 * headers.
	 * 
	 * @param regex: A regular expression to match to 
	 *               section headers
	 */
	public void setSectionPattern(String prefix, String postfix) {
		sectionPrefix = prefix;
		sectionPostfix = postfix;
	}
	
	/**
	 * Change the string used to separate variables from values
	 */
	public void setAssignmentString(String assign) {
		assignmentPattern = assign;
	}
	
	
	/**
	 * This method is not used.
	 */
	@Override
	public void replace(IFile file, String regex, String value) {
		// Skipped for now
	}

	
	/**
	 * Return that this is an INIWriter.
	 */
	@Override
	public String getWriterType() {
		return "INIWriter";
	}

}
