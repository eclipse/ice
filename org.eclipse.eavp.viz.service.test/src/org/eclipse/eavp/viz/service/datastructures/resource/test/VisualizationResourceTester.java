/*******************************************************************************
 *  * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 *   * All rights reserved. This program and the accompanying materials
 *    * are made available under the terms of the Eclipse Public License v1.0
 *     * which accompanies this distribution, and is available at
 *      * http://www.eclipse.org/legal/epl-v10.html
 *       *
 *        * Contributors:
 *         *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *          *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *           *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *            *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures.resource.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizJAXBHandler;
import org.eclipse.eavp.viz.service.datastructures.resource.IResource;
import org.eclipse.eavp.viz.service.datastructures.resource.IVizResource;
import org.eclipse.eavp.viz.service.datastructures.resource.VisualizationResource;
import org.junit.Test;

/**
 *  * This class is responsible for checking the VizResource class
 *   * 
 *    * @author Taylor Patterson
 *     * 
 *      */
public class VisualizationResourceTester {

	/**
 * 	 * The VizResource to use for testing.
 * 	 	 */
	private IVizResource vizResource;
	
	/**
	 * 
	 */
	private IResource iCEResource;

	/**
	 * <p>
	 * This operation checks the construction of an ICEResource to insure that it
	 * can be created from both a java.io.File.
	 * </p>
	 * 
	 */
	@Test
	public void checkResourceCreation() {

		// Local Declarations
		String filename = "ICEResourceTestFile.testFile";
		File testFile = new File(filename);

		// Create the ICEResource using a java.io.File
		try {
			iCEResource = new VisualizationResource(testFile);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		// Check the default values of the name, id and description of the
		// Resource
		assertEquals(filename, iCEResource.getName());
		assertEquals(1, iCEResource.getId());
		assertEquals(testFile.getAbsolutePath(), iCEResource.getDescription());

	}

	/**
	 * <p>
	 * This operation checks the creation of the non nullary ICEResource
	 * constructor when passed a null parameter. Should throw a
	 * NullPointerException.
	 * </p>
	 * 
	 * @throws Class
	 * @throws Class
	 */
	@Test(expected = java.lang.NullPointerException.class)
	public void checkResourceCreationNullPointerException()
			throws NullPointerException, IOException {
		iCEResource = new VisualizationResource(null);

	}

	/**
	 * <p>
	 * This operation checks the contents of the File managed by an ICEResource.
	 * It also checks the get/setPath operations for URIs to make sure that the
	 * URI can be changed and that changing the URI changes the File.
	 * </p>
	 * 
	 */
	@Test
	public void checkContents() {

		// Local Declarations
		String filename = "ICEResourceTestFile.testFile";
		String filename2 = "ICEResourceTestFile.testFile2";
		File testFile = new File(filename);
		File testFile2 = new File(filename2);

		// Create the ICEResource using a java.io.File
		try {
			iCEResource = new VisualizationResource(testFile);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the File returned by getContents is the same file
		assertEquals(testFile, iCEResource.getContents());

		// Check the path against the File
		assertEquals(testFile.toURI(), iCEResource.getPath());

		// Check null constructor
		iCEResource = new VisualizationResource();

		assertNull(iCEResource.getContents());
		assertNull(iCEResource.getPath());
		assertEquals(iCEResource.getName(), "ICE Object");
		assertEquals(iCEResource.getDescription(), "ICE Object");

		// This is to check the get/setter for Path
		// Create the ICEResource with null constructor
		iCEResource = new VisualizationResource();

		// Set the file with setFile
		try {
			iCEResource.setContents(testFile);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(); // fail if this is hit
		} catch (IOException e) {
			e.printStackTrace();
			fail(); // fail if this is hit
		}

		// Check that the path is the correct URI
		assertTrue(iCEResource.getContents().equals(testFile));

		// Change the path
		iCEResource.setPath(testFile2.toURI());

		// Check that it has changed the path and file
		assertTrue(iCEResource.getPath().equals(testFile2.toURI()));
		assertTrue(iCEResource.getContents().getName()
				.equals(testFile2.getName()));
		assertTrue(iCEResource.getContents().getAbsolutePath()
				.equals(testFile2.getAbsolutePath()));

		// Try to pass null
		iCEResource.setPath(null);

		// Should not change
		assertTrue(iCEResource.getPath().equals(testFile2.toURI()));
		assertTrue(iCEResource.getContents().getName()
				.equals(testFile2.getName()));
		assertTrue(iCEResource.getContents().getAbsolutePath()
				.equals(testFile2.getAbsolutePath()));

		// Try to change the URI to same type
		// Should not change testFile2
		// Try to pass null
		iCEResource.setPath(testFile2.toURI());

		// Should not change
		assertTrue(iCEResource.getPath().equals(testFile2.toURI()));
		assertTrue(iCEResource.getContents().getName()
				.equals(testFile2.getName()));
		assertTrue(iCEResource.getContents().getAbsolutePath()
				.equals(testFile2.getAbsolutePath()));

	}

	/**
	 * <p>
	 * This operation checks the accessors for setting a list of properties on
	 * the ICEResource.
	 * </p>
	 * 
	 */
	@Test
	public void checkProperties() {
		// Local declarations
		VisualizationResource resource = null;
		File file;
		VizEntry prop1, prop2, prop3;
		ArrayList<VizEntry> properties;

		// Create some entries

		// Prop1 is a discrete with true and false
		prop1 = new VizEntry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("true");
				allowedValues.add("false");
				this.defaultValue = "true";
				this.allowedValueType = VizAllowedValueType.Discrete;
			}
		};

		// Prop2 is continuous between 1 and 100. Default 5
		prop2 = new VizEntry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("1");
				allowedValues.add("100");
				this.defaultValue = "5";
				this.allowedValueType = VizAllowedValueType.Continuous;
			}
		};

		// Prop3 is undefined. Value is "Ughn the Barbarian"
		prop3 = new VizEntry() {

			@Override
			protected void setup() {
				this.allowedValueType = VizAllowedValueType.Undefined;
				this.value = "Ughn the Barbarian";
			}
		};

		// Add the props to the properties list
		properties = new ArrayList<VizEntry>();

		properties.add(prop1);
		properties.add(prop2);
		properties.add(prop3);

		// setup an ICEResource
		// Setup the file
		file = new File("An awesome file");

		// Create the ICEResource
		try {
			resource = new VisualizationResource(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// check that the properties is empty
		assertNotNull(resource.getProperties());
		assertEquals(0, resource.getProperties().size());

		// Add properties to the list
		resource.setProperties(properties);

		// Check that the list is set and the properties match
		assertNotNull(resource.getProperties());
		assertEquals(3, resource.getProperties().size());
		assertTrue(properties.equals(resource.getProperties()));

		// Assert that you can not pass null into the setter - no change
		resource.setProperties(null);

		// Check that the list is set and the properties match
		assertNotNull(resource.getProperties());
		assertEquals(3, resource.getProperties().size());
		assertTrue(properties.equals(resource.getProperties()));

		// Show that the list is a shallow copy upon entrance
		// and when the getter is called
		prop1.setName("Billy Bob Jones");
		assertTrue(prop1.equals(properties.get(0))); // Same entry
		assertTrue(properties.equals(resource.getProperties()));

		// Assert you can pass an empty arraylist and it will reset the list
		resource.setProperties(new ArrayList<VizEntry>());

		// check that the properties is empty
		assertNotNull(resource.getProperties());
		assertEquals(0, resource.getProperties().size());

	}

	/**
	 * <p>
	 * An operation that checks the isPictureType and setPictureType operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkIsPicture() {
		// Local declarations
		IResource testNR = null, testNR2 = null;

		// Local Declarations
		String filename = "ICEResourceTestFile.testFile";
		String filename2 = "ICEResourceTestFile2.text";

		File testFile = new File(filename);
		File testFile2 = new File(filename2);

		// testFile is a .testFile extension and should not be a picture
		try {
			iCEResource = new VisualizationResource();
			testNR = new VisualizationResource(testFile);
			testNR2 = new VisualizationResource(testFile2);
			testNR2.setPictureType(true);

		} catch (IOException e) {
			fail(); // Should not happen
		}
		// Default of iCEResource is false with no file type set
		assertFalse(iCEResource.isPictureType());
		// Default of ICEResource is false with a file type set
		assertFalse(testNR.isPictureType());
		// If set to true, picture type is true
		assertTrue(testNR2.isPictureType());

		// Set the testNR2 to false
		testNR2.setPictureType(false);

		// If set to false, picture type is false
		assertFalse(testNR2.isPictureType());

	}

	/**
 * 	 * Check the setFileSet and getFileSet operations.
 * 	 	 */
	@Test
	public void checkFileSet() {

		// Initialize the VizResource
		vizResource = new VisualizationResource();

		// Ensure null initialization
		assertNull(vizResource.getFileSet());

		// Create a file set
		String[] fakeFilenames = { "Benjamin", "Buford", "Blue" };

		// Set the file set
		vizResource.setFileSet(fakeFilenames);

		// Check that the file set is set
		assertNotNull(vizResource.getFileSet());
		assertEquals(3, vizResource.getFileSet().length);
		assertTrue(fakeFilenames.equals(vizResource.getFileSet()));

	}

	/**
	 * Check the setFileSetTitle and getFileSetTitle operations.
	 */
	@Test
	public void checkFileSetTitle() {

		// Initialize the VizResource
		vizResource = new VisualizationResource();

		// Ensure it is not null to start
		assertNotNull(vizResource.getFileSetTitle());

		// Set the file set title
		vizResource.setFileSetTitle("Bubba");

		// Check that the file set title is set
		assertNotNull(vizResource.getFileSetTitle());
		assertTrue("Bubba".equals(vizResource.getFileSetTitle()));

	}

	/**
	 * Check the setRemote and isRemote operations.
	 */
	@Test
	public void checkIsRemote() {

		// Initialize the VizResource
		vizResource = new VisualizationResource();

		// Set the host to 'localhost'
		vizResource.setHost("localhost");

		// Check that the resource is not remote
		assertFalse(vizResource.isRemote());

		// Set the host to something else
		vizResource.setHost("notlocalhost");

		// Not the resource should be remote
		assertTrue(vizResource.isRemote());

	}

	/**
	 * This operation makes sure that the VizResource can be written to and read
	 * from XML properly.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 * @throws NullPointerException
	 */
	@Test
	public void checkXMLPersistence()
			throws NullPointerException, JAXBException, IOException {

		// Local declarations
		VisualizationResource vizResource = null, loadedResource = null;
		VisualizationResource childRes1 = new VisualizationResource(new File("1")),
				childRes2 = new VisualizationResource(new File("2"));
		childRes2.setId(2);
		childRes2.setName("2");
		ArrayList<IVizResource> childResources = new ArrayList<IVizResource>();
		childResources.add(childRes1);
		childResources.add(childRes2);
		String[] fileSet = { "one", "two", "three" };
		String title = "title", host = "localhost";
		String filename = "ICEResourceTestFile.testFile";
		String filename2 = "ICEResourceTestFile2.testFile";
		VizJAXBHandler xmlHandler = new VizJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(VisualizationResource.class);

		// Create the VizResource and load it up with a lot of stuff to persist
		vizResource = new VisualizationResource(new File(filename), childResources);
		vizResource.setFileSet(fileSet);
		vizResource.setFileSetTitle(title);
		vizResource.setHost(host);

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(vizResource, classList, outputStream);

		// Initialize object and pass inputStream to read()
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());
		loadedResource = (VisualizationResource) xmlHandler.read(classList, inputStream);

		// checkContents
		assertTrue(vizResource.equals(loadedResource));

	}

	/**
	 * This operation checks the equals() and hashcode() operations of
	 * VizResource.
	 * 
	 * @throws IOException
	 */
	@Test
	public void checkEquality() throws IOException {

		// Local declarations
		VisualizationResource vizResource = null, equalResource = null;
		VisualizationResource childRes1 = new VisualizationResource(),
				childRes2 = new VisualizationResource();
		ArrayList<IVizResource> childResources = new ArrayList<IVizResource>();
		childResources.add(childRes1);
		childResources.add(childRes2);
		String[] fileSet = { "one", "two", "three" };
		String title = "title", host = "localhost";
		String filename = "ICEResourceTestFile.testFile";

		// Create the VizResource and load it up with a lot of stuff to persist
		vizResource = new VisualizationResource(new File(filename), childResources);
		vizResource.setFileSet(fileSet);
		vizResource.setFileSetTitle(title);
		vizResource.setHost(host);

		// Create the second, equal resource
		equalResource = new VisualizationResource(new File(filename), childResources);
		equalResource.setFileSet(fileSet);
		equalResource.setFileSetTitle(title);
		equalResource.setHost(host);

		// Check them
		assertEquals(vizResource, equalResource);
		assertEquals(vizResource.hashCode(), equalResource.hashCode());
	}

	/**
	 * This operation makes sure that copying and cloning the VizResource works.
	 * 
	 * @throws IOException
	 */
	@Test
	public void checkCopying() throws IOException {

		// Local declarations
		VisualizationResource vizResource = null, equalResource = new VisualizationResource();
		VisualizationResource childRes1 = new VisualizationResource(),
				childRes2 = new VisualizationResource();
		ArrayList<IVizResource> childResources = new ArrayList<IVizResource>();
		childResources.add(childRes1);
		childResources.add(childRes2);
		String[] fileSet = { "one", "two", "three" };
		String title = "title", host = "localhost";
		String filename = "ICEResourceTestFile.testFile";

		// Create the VizResource and load it up with a lot of stuff to persist
		vizResource = new VisualizationResource(new File(filename), childResources);
		vizResource.setFileSet(fileSet);
		vizResource.setFileSetTitle(title);
		vizResource.setHost(host);

		// Create the second, equal resource and check it
		equalResource.copy(vizResource);
		assertEquals(vizResource, equalResource);

		// Now check it as a clone
		equalResource = null;
		equalResource = (VisualizationResource) vizResource.clone();
		assertEquals(vizResource, equalResource);

		return;
	}

}
