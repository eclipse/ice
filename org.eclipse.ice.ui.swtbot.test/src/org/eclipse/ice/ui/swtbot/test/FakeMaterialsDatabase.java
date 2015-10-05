/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Robert Smith
 *******************************************************************************/
package org.eclipse.ice.ui.swtbot.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.MaterialWritableTableFormat;
import org.eclipse.ice.materials.XMLMaterialsDatabase;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * A simplified version of XMLMaterialsDatabase for testing purposes. Instead of
 * normal data loading, it simply sets itself up with a few materials with a
 * small number of properties each.
 * 
 * @author Robert Smith
 *
 */
public class FakeMaterialsDatabase extends XMLMaterialsDatabase {

	/**
	 * The default constructor. It simply calls the loadDatabase function.
	 */
	public FakeMaterialsDatabase() {
		restoreDefaults();
	}

	/**
	 * Replaces the superclass's private loading function.
	 * 
	 * @param fileToLoad
	 *            A dummy arguement which is ignored.
	 */
	private void loadDatabase(File fileToLoad) {
		ArrayList<Material> materials = new ArrayList<Material>();

		// Create the default materials
		Material Ag = new Material();
		Ag.setName("Ag");
		Ag.setProperty(Material.ABS_X_SECTION, 63.3);
		Ag.setProperty(Material.COHERENT_SCAT_LENGTH, 5.922);
		Ag.setProperty(Material.ATOMIC_DENSITY, 58.62);

		Material Al = new Material();
		Al.setName("Al");
		Al.setProperty(Material.ABS_X_SECTION, .231);
		Al.setProperty(Material.COHERENT_SCAT_LENGTH, 3.449);
		Al.setProperty(Material.ATOMIC_DENSITY, 60.31);

		Material Am = new Material();
		Am.setName("Am");
		Am.setProperty(Material.ABS_X_SECTION, 75.3);
		Am.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
		Am.setProperty(Material.ATOMIC_DENSITY, 1.04);

		Material Ar = new Material();
		Ar.setName("Ar");
		Ar.setProperty(Material.ABS_X_SECTION, 0.675);
		Ar.setProperty(Material.COHERENT_SCAT_LENGTH, 1.909);
		Ar.setProperty(Material.ATOMIC_DENSITY, 0.03);

		Material As = new Material();
		As.setName("As");
		As.setProperty(Material.ABS_X_SECTION, 4.5);
		As.setProperty(Material.COHERENT_SCAT_LENGTH, 6.58);
		As.setProperty(Material.ATOMIC_DENSITY, 46.03);

		Material Au = new Material();
		Au.setName("Au");
		Au.setProperty(Material.ABS_X_SECTION, 98.65);
		Au.setProperty(Material.COHERENT_SCAT_LENGTH, 7.63);
		Au.setProperty(Material.ATOMIC_DENSITY, 57.72);

		// Add them to the list
		materials.add(Ag);
		materials.add(Al);
		materials.add(Am);
		materials.add(Ar);
		materials.add(As);
		materials.add(Au);

		// Sort them
		Collections.sort(materials);

		// Load the list into the material map
		materialsMap = new Hashtable<String, Material>();
		for (Material material : materials) {
			materialsMap.put(material.getName(), material);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.materials.XMLMaterialsDatabase#restoreDefaults()
	 */
	@Override
	public void restoreDefaults() {
		loadDatabase(new File(""));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.materials.XMLMaterialsDatabase#start()
	 */
	@Override
	public void start() {
		loadDatabase(new File(""));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IElementSource#getTableFormat()
	 */
	@Override
	public TableFormat<Material> getTableFormat() {

		MaterialWritableTableFormat format = null;

		// Build and return a table format if there are materials in the
		// database
		if (!materialsMap.isEmpty()) {
			// Get the properties off the map. Pulling back the array is more
			// efficient than getting an iterator. I think...
			// Material[] emptyArray = {};
			// Map<String, Double> props = materialsMap.values().toArray(
			// emptyArray)[0].getProperties();
			// ArrayList<String> propNames = new
			// ArrayList<String>(props.keySet());
			// // Initialize the table format
			ArrayList<String> propNames = new ArrayList<String>();
			propNames.add(Material.ABS_X_SECTION);
			propNames.add(Material.COHERENT_SCAT_LENGTH);
			propNames.add(Material.ATOMIC_DENSITY);
			format = new MaterialWritableTableFormat(propNames);
		}

		return format;
	}
}
