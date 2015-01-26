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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IReader#read(org.eclipse.core.resources
	 * .IFile)
	 */
	@Override
	public Form read(IFile file) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
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
