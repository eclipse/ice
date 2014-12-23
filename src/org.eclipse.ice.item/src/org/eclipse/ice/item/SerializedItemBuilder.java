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
package org.eclipse.ice.item;

import static org.eclipse.ice.item.Item.*;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The SerializedItemBuilder is responsible for building SerializedItems and
 * should be registered with the Core. It will store the name of a Painfully
 * Simple Form file or an XML file and initialize a SerializedItem when the
 * build() operation is called. It parses the InputStream to determine the Item
 * name and type and resets that stream before creating the Item. These
 * properties are determined when the builder is constructed so the reset is
 * only performed once. The SerializedItem loads a SerializedItem from the
 * stream to use as a template and will copy all new Items from this template.
 * It attempts to close the input stream after loading the SerializedItem.
 * </p>
 * <p>
 * The Item Builder name of the Item is set to the name of the Item in file and
 * the SerializedItemBuilder will return the same name by calling getItemName().
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SerializedItemBuilder implements ItemBuilder {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the Item that this builder can create.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ItemType of the Item that this builder can create.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ItemType type;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An instance of the SerializedItem that this Builder can create that is
	 * instantiated upon construction. All subsequent Items are created as
	 * copies of this Item so that the InputStream can be released.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Item originalItem;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. SerializedItemBuilders must be initialized with an
	 * InputStream. The stream can contain either a Painfully Simple Form file
	 * or an XML file that was created according to the JAXB-generated schemas
	 * for ICE Forms. If it is unable to load the InputStream or determines that
	 * the contents of the stream are not consistent with either the XML or the
	 * PSF formats, then it will throw an IOException.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            The InputStream from which the SerializedItemBuilder should
	 *            build the SerializedItem.
	 *            </p>
	 * @throws IOException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SerializedItemBuilder(InputStream inputStream) throws IOException {
		// begin-user-code

		// Local Declarations
		int numChars = 0;
		ByteArrayInputStream itemReadStream = null;
		char[] buffer = null;
		Writer writer = null;
		Reader reader = null;
		ICEJAXBHandler xmlMarshaller = new ICEJAXBHandler();
		boolean tryAgain = false;

		// The input stream should be legit
		if (inputStream != null) {

			// Instantiate the template SerializedItem
			originalItem = new Item(null);

			// Get the bytes from the InputStream, put it into a new stream and
			// close the original. We're doing this in case we have to read it
			// twice.
			writer = new StringWriter();
			buffer = new char[1024];
			try {
				// Setup the reader
				reader = new BufferedReader(new InputStreamReader(inputStream,
						"UTF-8"));
				// Load the bytes into the writer
				while ((numChars = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, numChars);
				}
			} finally {
				// Close the original stream
				inputStream.close();
			}

			// Read an input stream from the writer
			itemReadStream = new ByteArrayInputStream(writer.toString()
					.getBytes());

			// Start by trying load the Item as a JobLauncher. Catch any
			// exceptions so that we can try again.
			try {
				originalItem = (Item) xmlMarshaller.read(JobLauncher.class,
						itemReadStream);
			} catch (NullPointerException e) {
				e.printStackTrace();
				tryAgain = true;
			} catch (JAXBException e) {
				e.printStackTrace();
				tryAgain = true;
			}

			// If that fails, try loading the Item as an instance of the base
			// class. Catch any exceptions so that we can try loading it again
			// as a PSF.
			if (tryAgain) {
				// Reset the input stream
				itemReadStream.reset();
				// Reset the flag
				tryAgain = false;
				try {
					originalItem = (Item) xmlMarshaller.read(Item.class,
							itemReadStream);
				} catch (NullPointerException e) {
					e.printStackTrace();
					tryAgain = true;
				} catch (JAXBException e) {
					e.printStackTrace();
					tryAgain = true;
				}
			}

			// If that failed, try loading it as a PSF. Don't catch the
			// exception because if it fails this time, we want it to fail
			// permanently because it is a real error.
			if (tryAgain) {
				// Reset the stream
				itemReadStream.reset();
				try {
					originalItem.loadFromPSF(itemReadStream);
				} catch (IOException e) {
					throw new IOException("SerializedBuilder Error:"
							+ " Unable to load the serialized "
							+ "Item. It is not of the proper "
							+ "form for a serialized XML Item "
							+ "or a serialized PSF Item.");
				}
			}

			// Close the second stream
			itemReadStream.close();

			// Set the name and type
			name = originalItem.getName();
			type = originalItem.getItemType();

		} else {
			throw new IOException("SerializedBuilder Error: Items can not be "
					+ "created from null input streams! Make sure the file "
					+ "exists in the data directory and re-load ICE.");
		}
		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getItemName() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ItemType getItemType() {
		// begin-user-code
		return type;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(IProject projectSpace)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item build(IProject projectSpace) {
		// begin-user-code

		// Local Declarations
		Item copiedItem = (Item) this.originalItem.clone();

		// Setup the project space
		copiedItem.setProject(projectSpace);

		return copiedItem;
		// end-user-code
	}
}