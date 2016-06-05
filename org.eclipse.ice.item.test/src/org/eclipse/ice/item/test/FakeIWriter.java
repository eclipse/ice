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
package org.eclipse.ice.item.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.january.form.Form;

/**
 * This class is a fake writer for use when testing the Item. The name of the
 * file format is passed into the constructor and used to lie to clients.
 * 
 * @author Jay Jay Billings
 *
 */
public class FakeIWriter implements IWriter {

	// True if write was called
	public boolean written = false;

	// The format to fake
	private String name;

	/**
	 * The constructomr
	 * 
	 * @param format
	 *            the name of the file format that should be faked.
	 */
	public FakeIWriter(String format) {
		name = format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IWriter#write(org.eclipse.ice.
	 * datastructures.form.Form, org.eclipse.core.resources.IFile)
	 */
	@Override
	public void write(Form formToWrite, IFile file) {
		written = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IWriter#replace(org.eclipse.core.
	 * resources.IFile, java.lang.String, java.lang.String)
	 */
	@Override
	public void replace(IFile file, String regex, String value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IWriter#getWriterType()
	 */
	@Override
	public String getWriterType() {
		return name;
	}

}
