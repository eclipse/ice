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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ice.datastructures.ICEObject.ICEList;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.MaterialStack;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class XMLMaterialsDatabase
		implements IMaterialsDatabase, BundleActivator {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(XMLMaterialsDatabase.class);

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
	protected Hashtable<String, Material> materialsMap;

	/**
	 * The service registration to publish and unpublish this class as an
	 * IMaterialsDatabase service.
	 */
	private ServiceRegistration<IMaterialsDatabase> registration;

	/**
	 * The default path of the database XML file in the bundle.
	 */
	private String defaultDBPath = "data/defaultMatDB.xml";

	/**
	 * The default path of the database as a URL.
	 */
	private URL defaultDBURL;

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
	 * @param testUserXMLDatabase
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

			// Create the input stream
			FileInputStream stream = new FileInputStream(fileToLoad);
			// Create the necessary JAXB equipment to load the file
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			// And unmarshall it into the list
			ICEList<Material> rawList = (ICEList<Material>) jaxbUnmarshaller
					.unmarshal(stream);

			// Load the list into the material map
			materialsMap = new Hashtable<String, Material>();
			for (Material material : rawList.getList()) {
				materialsMap.put(material.getName(), material);
			}
		} catch (JAXBException | FileNotFoundException e) {
			logger.error("Unable to load " + fileToLoad.getAbsolutePath() + ".",
					e);
		}
		return;
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
			logger.error(getClass().getName() + " Exception!", e);
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
	 * This operation starts the service. unlike the activator
	 * (start(BundleContext)), this operation actually manipulates the files.
	 */
	public void start() {

		// Create the JAXB context to manipulate the files
		try {
			jaxbContext = JAXBContext.newInstance(ICEList.class, Material.class,
					MaterialStack.class);
		} catch (JAXBException e) {
			// Complain to the logger service
			logger.error("Unable to initialize JAXB!", e);
		}

		// Find the default user database if it exists in the workspace.
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IPath userDBPath = workspaceRoot.getLocation().append("userMatDB.xml");

		// Set the default file references
		try {
			// The default database and the user database are both injected
			// during tested, but found programmatically in production. So, only
			// pull their locations if they are not set already.
			if (defaultDatabase == null && userDatabase == null) {
				// This file is in a bundle, so we need to convert its URL.
				defaultDatabase = new File(
						FileLocator.toFileURL(defaultDBURL).getPath());
				// This file is in the workspace, so we need to convert its
				// path.
				userDatabase = userDBPath.toFile();
			}
			// Choose which database to load and do so
			if (userDatabase.exists()) {
				logger.info("Loading user-modified database.");
				loadDatabase(userDatabase);
			} else {
				loadDatabase(defaultDatabase);
				// Also create the user database since it doesn't exist.
				userDatabase.createNewFile();
			}
		} catch (IOException | NullPointerException e1) {
			logger.error("Unable to load database.", e1);
		}

		// Throw some info in the log
		logger.info("Started!");

		return;
	}

	/**
	 * The OSGi-based start operation that performs framework-specific start
	 * tasks to determine the location of the database files.
	 *
	 * @param context
	 *            The component context
	 */
	@Override
	public void start(BundleContext context) {

		if (context != null) {
			// Find the default database that ships with ICE.
			Bundle bundle = context.getBundle();
			defaultDBURL = bundle.getEntry(defaultDBPath);

			// Once the files are set, just call the other start operation
			start();

			// Register the service
			registration = context.registerService(IMaterialsDatabase.class,
					this, null);
		}

		return;
	}

	/**
	 * This operation stops the service.
	 */
	public void stop() {

		// Write the database
		writeDatabase();

		logger.info("Service stopped!");

		return;
	}

	/**
	 * This operation stops the service and unregisters it.
	 *
	 * @param context
	 *            the OSGi bundle context
	 */
	@Override
	public void stop(BundleContext context) {

		// Stop the service
		stop();

		// Unregister this service from the framework
		registration.unregister();

		return;
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
			Map<String, Double> props = materialsMap.values()
					.toArray(emptyArray)[0].getProperties();
			ArrayList<String> propNames = new ArrayList<String>(props.keySet());
			// Initialize the table format
			format = new MaterialWritableTableFormat(propNames);
		}

		return format;
	}
}
