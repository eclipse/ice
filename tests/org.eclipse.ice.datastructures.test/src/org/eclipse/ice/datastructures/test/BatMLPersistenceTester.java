/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.ice.datastructures.batteryml.BatteryMLDoc;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

/**
 * This class tests persistence for BatML files using JAXB.
 * 
 * @author bkj
 * 
 */
public class BatMLPersistenceTester {

	@Test
	public void checkXMLReadAndWrite() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.dir");
		String filePath = userDir + separator + "data" + separator
				+ "electrical.xml";
		String outputFilePath = userDir + separator + "data" + separator
				+ "electrical_out.xml";

		try {
			// Setup the JAXB context
			JAXBContext context = JAXBContext.newInstance(BatteryMLDoc.class);

			// Try to load the file
			Unmarshaller unMarshaller = context.createUnmarshaller();
			Object battery = unMarshaller.unmarshal(new File(filePath));

			// Write it
			File outputFile = new File(outputFilePath);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(battery, outputFile);

			// If that worked without exception, then persistence for the BatML
			// classes works fine.

			// Delete the output file
			outputFile.delete();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		return;
	}

}
