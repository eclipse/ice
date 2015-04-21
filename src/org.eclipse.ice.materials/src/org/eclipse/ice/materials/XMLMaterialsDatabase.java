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
package org.eclipse.ice.materials;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.form.Material;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * This realization of the IMaterialDatabase interface manages the materials in
 * an XML database. It is designed to be used in the Equinox OSGi Service
 * Framework. It requires the Eclipse Resources Plugin. It should be started and
 * stopped with the start() and stop() functions at the beginning and end of its
 * lifecycle.
 * 
 * It stores the default list of Materials in a private folder ("data/") in its
 * bundle. It stores the (possibly) modified user list of Materials in its
 * bundle directory in the workspace.
 * 
 * @author Jay Jay Billings
 * 
 */
public class XMLMaterialsDatabase implements IMaterialsDatabase {

	/**
	 * This file contains the most recent version of the database that the user
	 * has modified.
	 */
	private File userDatabase;

	/**
	 * This file contains the raw, unmodified version of the database that has
	 * not been modified.
	 */
	private File defaultDatabase;

	/**
	 * This context is used to create all of the JAXB related utilities for
	 * marshalling and unmarshalling the file.
	 */
	private JAXBContext jaxbContext;

	/**
	 * The list of materials loaded from the database. They are stored as a
	 * Hashtable for fast, sychronized manipulation.
	 */
	Hashtable<String, Material> materialsMap;

	/**
	 * The service tracker to track the LoggingService.
	 */
	ServiceTracker logTracker;

	/**
	 * The logging service.
	 */
	LogService logger;

	/**
	 * The constructor
	 */
	public XMLMaterialsDatabase() {
		// Nothing TODO
	}

	/**
	 * The test constructor. This should ONLY be used for testing. It overrides
	 * the work performed by the default constructor to locate the database
	 * files with values provided by the caller.
	 * 
	 * @param testXMLDatabase
	 *            The XML file that contains the materials.
	 * @param testDefaultXMLDatabase
	 *            The XML file that contains the materials that should be
	 *            considered the default list.
	 */
	public XMLMaterialsDatabase(File testUserXMLDatabase,
			File testDefaultXMLDatabase) {

		// Assign the database streams
		userDatabase = testUserXMLDatabase;
		defaultDatabase = testDefaultXMLDatabase;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.materials.IMaterialDatabase#getMaterials()
	 */
	@Override
	public List<Material> getMaterials() {
		return new ArrayList<Material>(materialsMap.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialDatabase#addMaterial(org.eclipse.ice
	 * .materials.Material)
	 */
	@Override
	public void addMaterial(Material material) {
		if (material != null) {
			materialsMap.put(material.getName(), material);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialDatabase#deleteMaterial(java.lang.
	 * String)
	 */
	@Override
	public void deleteMaterial(String name) {
		if (name != null) {
			materialsMap.remove(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialDatabase#deleteMaterial(org.eclipse
	 * .ice.materials.Material)
	 */
	@Override
	public void deleteMaterial(Material material) {
		if (material != null) {
			materialsMap.remove(material.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialDatabase#updateMaterial(org.eclipse
	 * .ice.materials.Material)
	 */
	@Override
	public void updateMaterial(Material material) {
		if (material != null) {
			materialsMap.put(material.getName(), material);
		}
	}

	/**
	 * This operation loads the database that is in the provided file.
	 * 
	 * @param streamToLoad
	 *            the file that contains a materials database in XML and which
	 *            should be loaded.
	 */
	private void loadDatabase(File fileToLoad) {
		try {
			// Create the necessary JAXB equipment to load the file
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			// And unmarshall it into the list
			ICEList<Material> rawList = (ICEList<Material>) jaxbUnmarshaller
					.unmarshal(fileToLoad);

			// Load the list into the material map
			materialsMap = new Hashtable<String, Material>();
			for (Material material : rawList.getList()) {
				materialsMap.put(material.getName(), material);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This operation writes the database to disk in the user database file.
	 */
	private void writeDatabase() {

		try {
			// Create the necessary JAXB equipment to dump the file
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// Create the ICEList of Materials
			ICEList<Material> materialsList = new ICEList<Material>();
			materialsList.setList(new ArrayList<Material>(getMaterials()));
			// And dump it into the file
			jaxbMarshaller.marshal(materialsList, userDatabase);
		} catch (JAXBException e) {
			System.err.println("XMLMaterialDatabase: Error writing database!");
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.materials.IMaterialDatabase#restoreDefaults()
	 */
	@Override
	public void restoreDefaults() {
		// Load the default database
		loadDatabase(defaultDatabase);
		// Overwrite the user database
		writeDatabase();
	}

	/**
	 * This operation starts the service.
	 */
	public void start() {

		// Local Declarations
		File fileToLoad;

		// Create the JAXB context to manipulate the files
		try {
			jaxbContext = JAXBContext
					.newInstance(ICEList.class, Material.class);
		} catch (JAXBException e) {
			// Complain
			if (logger != null) {
				logger.log(LogService.LOG_ERROR, "Unable to initialize JAXB!",
						e);
			} else {
				e.printStackTrace();
			}
		}

		// Choose which database to load
		if (userDatabase.exists()) {
			if (logger != null) {
				logger.log(LogService.LOG_INFO,
						"Loading user-modified database.");
			}
			fileToLoad = userDatabase;
		} else {
			fileToLoad = defaultDatabase;
		}

		// Load it up
		loadDatabase(fileToLoad);

		if (logger != null) {
			logger.log(LogService.LOG_INFO, "Started!");
		}

	}

	/**
	 * The OSGi-based start operation that performs framework-specific start
	 * tasks to determine the location of the database files.
	 * 
	 * @param context
	 *            The component context
	 */
	public void start(ComponentContext context) {

		try {
			// Get the file URLs from from the bundle
			BundleContext bundleContext = context.getBundleContext();
			Bundle bundle = bundleContext.getBundle();
			URL userDBURL = bundle.getEntry("data/userMatDB.xml");
			URL defaultDBURL = bundle.getEntry("data/defaultMatDB.xml");
			// Set the file references by converting the bundle:// URLs to
			// file:// URLs.
			userDatabase = new File(FileLocator.toFileURL(userDBURL).getPath());
			defaultDatabase = new File(FileLocator.toFileURL(defaultDBURL)
					.getPath());

			// Grab and open the logging service
			logTracker = new ServiceTracker(bundleContext,
					LogService.class.getName(), null);
			logger = (LogService) logTracker.getService();

			// Once the files are set, just call the other start operation
			start();
		} catch (IOException e) {
			// Complain
			if (logger != null) {
				logger.log(LogService.LOG_ERROR,
						"Unable to start the service!", e);
			} else {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This operation stops the service.
	 */
	public void stop() {

		// Write the database
		writeDatabase();

		if (logger != null) {
			logger.log(LogService.LOG_INFO, "Service stopped!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IElementSource#getElements()
	 */
	@Override
	public EventList<Material> getElements() {
		// Create a new event list and return it using the standard factory
		// method for GlazedLists.
		EventList<Material> list = GlazedLists.eventList(materialsMap.values());
		return list;
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
			Material[] emptyArray = {};
			Map<String, Double> props = materialsMap.values().toArray(
					emptyArray)[0].getProperties();
			ArrayList<String> propNames = new ArrayList<String>(props.keySet());
			// Initialize the table format
			format = new MaterialWritableTableFormat(propNames);
		}

		return format;
	}
}
