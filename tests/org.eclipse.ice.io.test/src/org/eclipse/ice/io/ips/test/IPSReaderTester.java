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
package org.eclipse.ice.io.ips.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.io.ips.IPSReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests the methods of the IPSReader class.  
 * 
 * @author bzq
 *
 */
public class IPSReaderTester {

	/**
	 * Tests the IPSReader
	 */
	@Test
	public void checkIPSReader() {
		
		//Create an IPSReader to test
		IPSReader reader = new IPSReader();
		assertNotNull(reader);
		
		String separator = System.getProperty("file.separator");
		String filePath = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "caebatTesterWorkspace" + separator
				+ "Caebat_Model" + separator + "example_ini.conf";		
		File testFile = new File(filePath);
		
		// Try to read in invalid INI file
		File fakeFile = null;
		ArrayList<Component> components = null;
		try {
			components = reader.loadINIFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake IPS input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake IPS input file");
			e.printStackTrace();
		}
		assertTrue(components == null);
		
		// Load the INI file and parse the contents into Components
		try {
			components = reader.loadINIFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find IPS input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from IPS input file: " + testFile.toString());
			e.printStackTrace();
		}
		
		// Make sure we found some components
		assertNotNull(components);		
		DataComponent globalConfig = (DataComponent) components.get(0);
		DataComponent portsTable = (DataComponent) components.get(1);
		DataComponent firstPort = (DataComponent) components.get(2);
		DataComponent secondPort = (DataComponent) components.get(3);
		DataComponent thirdPort = (DataComponent) components.get(4);
		DataComponent fourthPort = (DataComponent) components.get(5);
		DataComponent fifthPort = (DataComponent) components.get(6);
		DataComponent timeLoopData = (DataComponent) components.get(7);

		/* --- Check the GLOBAL CONFIGURATION component --- */
		String configName = "Global Configuration";
		assertEquals(configName, globalConfig.getName());
		assertEquals(20, globalConfig.retrieveAllEntries().size());
		for (int i = 0; i < 20; i++) {
			assertNotNull(globalConfig.retrieveAllEntries().get(i));
		}
		
		
		/* --- Check the PORTS TABLE component --- */		
		String portsName = "PORTS";
		assertEquals(portsName, portsTable.getName());
		assertEquals(5, portsTable.retrieveAllEntries().size());
		for (int i = 0; i < 5; i++) {
			assertNotNull(portsTable.retrieveAllEntries().get(i));
		}
		
		/* --- Check the PORTS components --- */
		String firstPortName = "CHARTRAN_ELECTRICAL_THERMAL_DRIVER";
		assertEquals(firstPortName, firstPort.getName());
		assertEquals(9, firstPort.retrieveAllEntries().size());
		for (int i = 0; i < 9; i++) {
			assertNotNull(firstPort.retrieveAllEntries().get(i));
		}
		
		String secondPortName = "AMPERES_THERMAL";
		assertEquals(secondPortName, secondPort.getName());
		assertEquals(11, secondPort.retrieveAllEntries().size());
		for (int i = 0; i < 11; i++) {
			assertNotNull(secondPort.retrieveAllEntries().get(i));
		}

		String thirdPortName = "AMPERES_ELECTRICAL";
		assertEquals(thirdPortName, thirdPort.getName());
		assertEquals(11, thirdPort.retrieveAllEntries().size());
		for (int i = 0; i < 11; i++) {
			assertNotNull(thirdPort.retrieveAllEntries().get(i));
		}
		
		String fourthPortName = "NTG";
		assertEquals(fourthPortName, fourthPort.getName());
		assertEquals(11, fourthPort.retrieveAllEntries().size());
		for (int i = 0; i < 11; i++) {
			assertNotNull(fourthPort.retrieveAllEntries().get(i));
		}
		
		String fifthPortName = "INIT_STATE";
		assertEquals(fifthPortName, fifthPort.getName());
		assertEquals(11, fifthPort.retrieveAllEntries().size());
		for (int i = 0; i < 11; i++) {
			assertNotNull(fifthPort.retrieveAllEntries().get(i));
		}
		
		
		/* --- Check the TIME LOOP component --- */
		String timeLoopName = "TIME_LOOP";
		assertEquals(timeLoopName, timeLoopData.getName());
		assertEquals(5, timeLoopData.retrieveAllEntries().size());
		for (int i = 0; i < 5; i++) {
			assertNotNull(timeLoopData.retrieveAllEntries().get(i));
		}
		
		return;
	}
}
