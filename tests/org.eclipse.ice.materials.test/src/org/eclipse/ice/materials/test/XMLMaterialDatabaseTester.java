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
package org.eclipse.ice.materials.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.MaterialWritableTableFormat;
import org.eclipse.ice.materials.XMLMaterialsDatabase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.odell.glazedlists.gui.TableFormat;

/**
 * This class is responsible for testing the XMLMaterialsDatabase class. Since
 * it is generally hard to test a service that needs to be stopped and started
 * to perform various functions with order-independent tesst, this class only
 * has one large operation that tests everything in a way that should be
 * consistent with the intended use of the service.
 * 
 * @author Jay Jay Billings
 * 
 */
public class XMLMaterialDatabaseTester {

	/**
	 * The test file used for the database modified by the user.
	 */
	private static File userTestFile;

	/**
	 * The test file used as a default database that isn't modified by the user.
	 */
	private static File defaultTestFile;

	/**
	 * This operation sets up the test. It creates a file in the $HOME/ICETests
	 * directory if it doesn't already exist. The ICETests directory must exist
	 * in advance.
	 */
	@BeforeClass
	public static void before() {

		// Get the file handle for the test files
		String separator = System.getProperty("file.separator");
		String path = System.getProperty("user.home") + separator + "ICETests"
				+ separator;
		userTestFile = new File(path + "userMaterialDatabase.xml");
		defaultTestFile = new File(path + "defaultMaterialDatabase.xml");
		// Create them if they don't exist
		try {
			userTestFile.createNewFile();
			defaultTestFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This operation removes the test file.
	 */
	@AfterClass
	public static void after() {
		// Delete the user test file
		if (userTestFile.exists()) {
			userTestFile.delete();
		}
		// Delete the default test file
		if (defaultTestFile.exists()) {
			defaultTestFile.delete();
		}
	}

	/**
	 * This is the massive test operation that does everything.
	 */
	@Test
	public void checkDatabase() {

		// Create a couple of materials...
		Material co2 = TestMaterialFactory.createCO2();
		Material h2o = TestMaterialFactory.createH2O();
		ArrayList<Material> materials = new ArrayList<Material>();
		ArrayList<Material> defaultMaterials = (ArrayList<Material>) materials
				.clone();
		ICEList<Material> jaxbMaterialsList = new ICEList<Material>();
		materials.add(h2o);
		materials.add(co2);
		jaxbMaterialsList.setList(materials);

		try {
			// ...and a JAXBContext to dump them to the user test file and the
			// default test file
			JAXBContext jaxbContext = JAXBContext.newInstance(ICEList.class,
					Material.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(jaxbMaterialsList, userTestFile);
			jaxbMarshaller.marshal(jaxbMaterialsList, defaultTestFile);

			// Initialize and start the service with the test constructor
			XMLMaterialsDatabase database = new XMLMaterialsDatabase(
					userTestFile, defaultTestFile);
			database.start();

			// Make sure it loaded some materials
			List<Material> databaseMaterials = database.getMaterials();
			assertNotNull(databaseMaterials);
			// Make sure it loaded the correct number of materials
			assertEquals(materials.size(), databaseMaterials.size());
			// Make sure all of the materials are in the list
			for (Material material : materials) {
				assertTrue(databaseMaterials.contains(material));
			}

			// Add a material to the database and make sure it took
			Material co2_2 = TestMaterialFactory.createCO2();
			co2_2.setName("CO2_2");
			database.addMaterial(co2_2);
			databaseMaterials = database.getMaterials();
			assertNotNull(databaseMaterials);
			assertEquals(materials.size() + 1, databaseMaterials.size());
			assertTrue(databaseMaterials.contains(co2_2));

			// Delete it and make sure the database reverted correctly
			database.deleteMaterial(co2_2);
			databaseMaterials = database.getMaterials();
			assertNotNull(databaseMaterials);
			assertEquals(materials.size(), databaseMaterials.size());
			for (Material material : materials) {
				assertTrue(databaseMaterials.contains(material));
			}

			// Put it back and then try deleting it by name.
			database.addMaterial(co2_2);
			database.deleteMaterial("CO2_2");
			databaseMaterials = database.getMaterials();
			assertNotNull(databaseMaterials);
			assertEquals(materials.size(), databaseMaterials.size());
			for (Material material : materials) {
				assertTrue(databaseMaterials.contains(material));
			}

			// Update the properties of CO2 and commit it.
			co2.setProperty("molar mass (g/mol)", 20.0);
			database.updateMaterial(co2);
			// Check it.
			databaseMaterials = database.getMaterials();
			assertNotNull(databaseMaterials);
			assertEquals(materials.size(), databaseMaterials.size());
			assertTrue(databaseMaterials.contains(co2));
			// Fix it back
			co2.setProperty("molar mass (g/mol)", 18.01);
			database.updateMaterial(co2);

			// Try to update something that isn't there and make sure it worked.
			Material lastMaterial = new Material();
			lastMaterial.setName("Phutureprimitive");
			database.addMaterial(lastMaterial);
			databaseMaterials = database.getMaterials();
			assertTrue(databaseMaterials.contains(lastMaterial));

			// Restart the service and make sure that it loads the modified list
			// of materials, not the default list.
			database.stop();
			database.start();
			List<Material> reloadedMaterials = database.getMaterials();
			assertTrue(reloadedMaterials.equals(databaseMaterials));

			// Restore the defaults and reload the service, again making sure it
			// now has the default list.
			database.restoreDefaults();
			reloadedMaterials = database.getMaterials();
			for (Material material : defaultMaterials) {
				assertTrue(reloadedMaterials.contains(material));
			}

			// Stop the service and restart it to make sure the defaults
			// persist.
			database.stop();
			database.start();
			reloadedMaterials = database.getMaterials();
			for (Material material : defaultMaterials) {
				assertTrue(reloadedMaterials.contains(material));
			}

			// Check the implementation of IElementSource provided by
			// the database. Make sure the EventList is correct.
			assertEquals(reloadedMaterials.size(), database.getElements()
					.size());
			assertEquals(reloadedMaterials.get(0), database.getElements()
					.get(0));
			// The table format is checked in detail in its own test, so just
			// make sure it isn't null and that it is the right type.
			TableFormat format = database.getTableFormat();
			assertNotNull(format);
			assertTrue(format instanceof MaterialWritableTableFormat);

			// Kill the service for completeness I suppose.
			database.stop();
		} catch (NullPointerException | JAXBException e) {
			// Complain
			e.printStackTrace();
			fail();
		}
	}
}
