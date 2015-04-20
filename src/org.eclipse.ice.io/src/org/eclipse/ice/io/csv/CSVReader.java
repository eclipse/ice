/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.io.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.naming.OperationNotSupportedException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.io.serializable.IReader;

/**
 * This class implements the IReader interface to provide a reader for CSV
 * files. It can read any well-formed CSV file. It stores its results in a
 * ListComponent<String []\> on the Form returned from read(). Each String [] in
 * the ListComponent is a line of the file, split and trimmed but uncast.
 * Clients must know the concrete type to which they want to cast.
 * 
 * Comments are ignored and begin with the "#" character.
 * 
 * @author Jay Jay Billings
 *
 */
public class CSVReader implements IReader {

	/**
	 * The lines of text read from the last file.
	 */
	ListComponent<String []> lines;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IReader#read(org.eclipse.core.resources
	 * .IFile)
	 */
	@Override
	public Form read(IFile file) {

		// Configure the form
		Form form = new Form();
		form.setName(file.getName());
		form.setDescription(file.getName());

		// Configure the list
		lines = new ListComponent<String[]>();
		lines.setName(file.getName());
		lines.setDescription(file.getName());

		try {
			// Grab the contents of the file
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					file.getContents()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Skip lines that pure comments
				if (!line.startsWith("#")) {
					// Clip the line if it has a comment symbol in it to be
					// everything before the symbol
					if (line.contains("#")) {
						int index = line.indexOf("#");
						line = line.substring(0, index);
					}
					// Clean up any crap on the line
					String[] lineArray = line.trim().split(",");
					// And clean up any crap on each split piece
					for (String element : lineArray) {
						element = element.trim();
					}
					// Put the lines in the list
					lines.add(lineArray);
				}
			}
			form.addComponent(lines);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IReader#findAll(org.eclipse.core.resources
	 * .IFile, java.lang.String)
	 */
	@Override
	public ArrayList<Entry> findAll(IFile file, String regex) {
		try {
			throw new OperationNotSupportedException("CSVReader Error: "
					+ "IReader.findAll() is not supported... yet.");
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IReader#getReaderType()
	 */
	@Override
	public String getReaderType() {
		return "csv";
	}

}
