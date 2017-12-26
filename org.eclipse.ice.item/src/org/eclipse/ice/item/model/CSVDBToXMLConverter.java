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
package org.eclipse.ice.item.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.form.Material;

/**
 * This class converts the materials database from CSV (defaultMatDB.csv) to XML
 * (defaultMatDB.xml).
 * 
 * @author Jay Jay Billings
 *
 */
public class CSVDBToXMLConverter {

	/**
	 * The all-powerful Main!
	 * 
	 * @param args
	 * @throws IOException
	 *             This exception will be thrown if main can't handle the files
	 *             correctly. Mostly the CSV file.
	 * @throws JAXBException
	 *             This exception will be thrown if main can't handle the XML
	 *             file.
	 */
	public static void main(String args[]) throws IOException, JAXBException {
		// Setup the file paths and readers
		String separator = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		File CSVFile = new File(home + separator + "ICETests" + separator
				+ "defaultMatDB.csv");
		File xmlFile = new File(home + separator + "ICETests" + separator
				+ "defaultMatDB.xml");
		FileReader reader = new FileReader(CSVFile);
		BufferedReader bufferedReader = new BufferedReader(reader);

		// Read the materials
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}

		// Close the readers
		bufferedReader.close();
		reader.close();

		// The first line is composed of the names of the properties.
		String[] propertyNames = lines.get(0).split(",");
		// Create a list to store all the materials
		ArrayList<Material> materials = new ArrayList<Material>();
		// Parse and create the materials
		for (int i = 1; i < lines.size(); i++) {
			String[] properties = lines.get(i).split(",");
			Material tmpMaterial = new Material();
			// The first property is the name
			tmpMaterial.setName(properties[0]);
			// All other properties are doubles with names equal to the same
			// index in the properties array.
			for (int j = 1; j < properties.length; j++) {
				tmpMaterial.setProperty(propertyNames[j],
						Double.valueOf(properties[j]));
			}

			// Add the material to the list
			materials.add(tmpMaterial);
		}

		// Note that none of the materials in this database have children. They
		// are all isotopes.

		// Create the list that will be written to XML.
		ICEList<Material> xmlList = new ICEList<Material>();
		xmlList.setList(materials);

		// Create the necessary JAXB equipment to dump the file
		JAXBContext jaxbContext = JAXBContext.newInstance(ICEList.class,
				Material.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Dump the file
		jaxbMarshaller.marshal(xmlList, xmlFile);
		
		return;
	}

}
