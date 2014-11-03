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
package org.eclipse.ice.item.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.ice.datastructures.form.FormStatus;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The TaggedOutputWriterAction will write a collection of key-value pairs to a
 * file. It requires that each key-value pair be listed in the dictionary and it
 * will write them as "key=value" in the file. The name of the file should be
 * passed as a value in the dictionary with "iceTaggedOutputFileName" as the
 * key. (Presumably no one else would ever use such a key name...) It assumes
 * that the size of the file is relatively small and that it can therefore be
 * written quickly enough that a separate thread is not needed.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TaggedOutputWriterAction extends Action {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TaggedOutputWriterAction() {
		// begin-user-code
		super();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#execute(Dictionary<Object> dictionary)
	 */
	public FormStatus execute(Dictionary<String, String> dictionary) {
		// begin-user-code

		// Local Declarations
		String fileName = null, key = null, value = null;
		File outputFile = null;
		FileWriter writer = null;
		FormStatus status = FormStatus.InfoError;
		Enumeration<String> keys = null;

		// Only run if the dictionary is not null and has a file name in it
		if (dictionary != null
				&& dictionary.get("iceTaggedOutputFileName") != null) {
			outputFile = new File(dictionary.get("iceTaggedOutputFileName"));
			try {
				// Create a writer
				writer = new FileWriter(outputFile);
				// Get the set of keys
				keys = dictionary.keys();
				// Write the file
				while (keys.hasMoreElements()) {
					key = keys.nextElement();
					value = dictionary.get(key);
					if (!("iceTaggedOutputFileName").equals(key)) {
						writer.write(key.replaceAll("\\s+", "") + "=" + value
								+ "\n");
					}
				}
				// Close the writer
				writer.close();
				// Update the status
				status = FormStatus.Processed;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		return status;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#cancel()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancel() {
		// begin-user-code
		// TODO Auto-generated method stub
		return FormStatus.InfoError;
		// end-user-code
	}
}