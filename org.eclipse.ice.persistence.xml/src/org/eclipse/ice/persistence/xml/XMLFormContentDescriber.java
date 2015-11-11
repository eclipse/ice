/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.persistence.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.ice.datastructures.form.FormTextContentDescriber;

/**
 * This is a content describer for the XML files that are persisted by ICE's
 * default XMLPersistenceProvider.
 *
 * @author Jay Jay Billings
 *
 */
public class XMLFormContentDescriber implements FormTextContentDescriber { 

	/**
	 * Reference to the Item's ID
	 */
	private int itemID = -1;

	/**
	 * Constructor
	 */
	public XMLFormContentDescriber() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.content.IContentDescriber#describe(java.io.
	 * InputStream, org.eclipse.core.runtime.content.IContentDescription)
	 */
	@Override
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		// Just pass the information on to the other operation.
		InputStreamReader reader = new InputStreamReader(contents);
		return describe(reader, description);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.runtime.content.IContentDescriber#getSupportedOptions()
	 */
	@Override
	public QualifiedName[] getSupportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.runtime.content.ITextContentDescriber#describe(java.io.
	 * Reader, org.eclipse.core.runtime.content.IContentDescription)
	 */
	@Override
	public int describe(Reader contents, IContentDescription description) throws IOException {

		int retCode = INVALID;
		BufferedReader bufferedReader = new BufferedReader(contents);

		// The ICE XML files have a very well defined first four lines, although
		// the attribute values change. Read the first four lines of the text in
		// and then check for some common flags.
		String firstLines = "", nextLine;
		int counter = 0;
		while (((nextLine = bufferedReader.readLine()) != null) && counter < 3) {
			firstLines += nextLine;
			counter++;
		}

		// Check the lines
		if (firstLines.contains("<?xml version=")) {
			if (firstLines.contains("itemType=") && firstLines.contains("builderName=")
					&& firstLines.contains("<Form")) {
				retCode = VALID;
				// Now we know this is an ICE XML Item, so
				// get a reference to its ID 
				int index = firstLines.indexOf("itemID=\"");
				int endIndex = firstLines.indexOf("\"", index + 8);
				String itemIdString = firstLines.substring(index, endIndex + 1).replace("\"", "");
				itemID = Integer.valueOf(itemIdString.split("=")[1]);
				
			} else {
				retCode = INDETERMINATE;
			}
		}

		return retCode;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.persistence.ICETextContentDescriber#getItemID()
	 */
	@Override
	public int getItemID() {
		return itemID;
	}

}
