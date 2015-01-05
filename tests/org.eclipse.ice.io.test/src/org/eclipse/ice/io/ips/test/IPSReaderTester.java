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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.io.ips.IPSReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
		
		// Set up where to look

		String separator = System.getProperty("file.separator");
		String filePath = System.getProperty("user.home") + separator + "ICETests" 
				+ separator + "caebatTesterWorkspace" + separator 
				+ "Caebat_Model" + separator + "example_ini.conf";
		IPath fileIPath = new Path(filePath);
		IFile inputFile = ResourcesPlugin.getWorkspace().getRoot().getFile(fileIPath);
		
		BufferedReader testReader = null;
		try {
			testReader = new BufferedReader(new FileReader(new File(filePath)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Create an IPSReader to test
		IPSReader reader = new IPSReader();
		assertNotNull(reader);
		
		// Try to read in invalid INI file
		IFile fakeFile = null;
		Form form = null;
		form = reader.read(fakeFile);
		assertTrue(form == null);
		
		// Load the INI file and parse the contents into Components
		try {
			form = reader.read(inputFile);
			testReader.close();
		} catch (FileNotFoundException e) {
			fail("Failed to find IPS input file: " + filePath);
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from IPS input file: " + filePath);
			e.printStackTrace();
		}
		
		// Make sure we found some components
		ArrayList<Component> components = form.getComponents();
		assertEquals(4,components.size());	
		DataComponent timeLoopData = (DataComponent) components.get(0);
		TableComponent globalConfig = (TableComponent) components.get(1);
		TableComponent portsTable = (TableComponent) components.get(2);
		MasterDetailsComponent portsMaster = (MasterDetailsComponent) components.get(3);


		/* --- Check the GLOBAL CONFIGURATION component --- */
		String configName = "Global Configuration";
		assertEquals(configName, globalConfig.getName());
		assertEquals(20, globalConfig.numberOfRows());
		for (int i = 0; i < 20; i++) {
			assertNotNull(globalConfig.getRow(i));
		}
		
		
		/* --- Check the PORTS TABLE component --- */		
		String portsName = "Ports Table";
		assertEquals(portsName, portsTable.getName());
		assertEquals(5, portsTable.numberOfRows());
		for (int i = 0; i < 5; i++) {
			assertNotNull(portsTable.getRow(i));
		}
		
		/* --- Check the Ports Master component --- */
		String masterName = "Ports Master";
		assertEquals(masterName, portsMaster.getName());
		assertEquals(5, portsMaster.numberOfMasters());
		for (int i = 0; i < 5; i++) {
			assertNotNull(portsMaster.getMasterAtIndex(i));
		}
				
		/* --- Check the TIME LOOP component --- */
		String timeLoopName = "Time Loop Data";
		assertEquals(timeLoopName, timeLoopData.getName());
		assertEquals(5, timeLoopData.retrieveAllEntries().size());
		for (int i = 0; i < 5; i++) {
			assertNotNull(timeLoopData.retrieveAllEntries().get(i));
		}		
		
		return;
	}
}
