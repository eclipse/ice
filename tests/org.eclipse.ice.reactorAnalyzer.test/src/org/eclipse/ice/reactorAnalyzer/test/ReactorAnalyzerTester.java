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
package org.eclipse.ice.reactorAnalyzer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisTool;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzerBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the ReactorAnalyzer.
 * </p>
 * <p>
 * This is a complicated series of tests, but the coverage is still relatively
 * high. The tester creates and accesses a project, checks the ReactorAnalyzer's
 * Form, tries to invoke a review, processing and resubmits for a second review.
 * </p>
 * <p>
 * In general it is a very bad idea to string together separate tests. However,
 * because of the amount of work involved in arriving at any particular point in
 * the Item workflow, the tests in this class build on the results of the
 * previous tests.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ReactorAnalyzerTester {
	// FIXME These tests need to be updated. However, the ReactorAnalyzer will
	// be undergoing a LOT of changes soon.

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The instance of ReactorAnalyzer that will be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static ReactorAnalyzer reactorAnalyzer;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fake implementation of IAnalysisTool that is used to trick the
	 * ReactorAnalyzer without loading heavyweight components.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static FakeAnalysisTool fakeAnalysisTool;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fake implementation of IAnalysisDocument that is used to trick the
	 * ReactorAnalyzer without loading heavyweight components.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static FakeAnalysisDocument fakeAnalysisDocument;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fake implementation of IAnalysisAsset that is used to trick the
	 * ReactorAnalyzer without loading heavyweight components.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static FakeAnalysisAsset fakeAnalysisAsset;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Eclipse Resources Project Space used for the tests.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static IProject projectSpace;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations sets up the workspace for an instantiates a
	 * ReactorAnalyzer for testing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void BeforeClass() {
		// begin-user-code

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorAnalyzerTesterWorkspace";

		// Initialize fake analysis tool
		fakeAnalysisTool = new FakeAnalysisTool();

		// Set null values for the other fakes so that they must be updated
		fakeAnalysisDocument = null;
		fakeAnalysisAsset = null;

		// Setup the project workspace
		try {
			// Get the project handle
			projectSpace = workspaceRoot
					.getProject("reactorAnalyzerTesterWorkspace");
			// If the project does not exist, create it
			if (!projectSpace.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(userDir)).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin
						.getWorkspace()
						.newProjectDescription("reactorAnalyzerTesterWorkspace");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				projectSpace.create(desc, null);
			}
			// Open the project if it is not already open
			if (projectSpace.exists() && !projectSpace.isOpen()) {
				projectSpace.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the construction of the ReactorAnalyzer to ensure
	 * that its Form was created according to the specification on the class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Make the ReactorAnalyzer
		reactorAnalyzer = new ReactorAnalyzer(projectSpace);
		// Set the name
		reactorAnalyzer.setName(ReactorAnalyzerBuilder.name);
		// Set the analysis tools
		ArrayList<IAnalysisTool> tools = new ArrayList<IAnalysisTool>();
		tools.add(fakeAnalysisTool);
		reactorAnalyzer.setAnalysisTools(tools);

		// Get the Form
		Form form = reactorAnalyzer.getForm();

		// Check the form's components
		assertNotNull(form);
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		// The Components of the ReactorAnalyzer's Form are very specifically
		// identified in the ReactorAnalyzer documentation. Check the
		// DataComponent that identifies the data files.
		assertTrue(form.getComponent(1) instanceof DataComponent);
		DataComponent dataComponent = (DataComponent) form.getComponent(1);
		assertEquals(dataComponent.getId(), 1);
		assertEquals(dataComponent.getName(), "Data Sources");

		// Also make sure that there are available data sources to select
		Entry dataSources = dataComponent.retrieveEntry("Input Data");
		assertNotNull(dataSources);
		assertEquals("Input Data", dataSources.getName());
		assertEquals(1, dataSources.getId());
		assertTrue(dataSources.getAllowedValues().contains("globe.silo"));

		// Check the reference data sources
		Entry referenceSources = dataComponent.retrieveEntry("Reference Data");
		assertNotNull(referenceSources);
		assertEquals("Reference Data", referenceSources.getName());
		assertEquals(2, referenceSources.getId());
		assertTrue(referenceSources.getAllowedValues().contains("globe.silo"));

		// Now check the ResourceComponent
		assertTrue(form.getComponent(2) instanceof ResourceComponent);
		ResourceComponent resourceComponent = (ResourceComponent) form
				.getComponent(2);
		assertEquals(resourceComponent.getId(), 2);
		assertEquals(resourceComponent.getName(), "Analysis Artifacts");
		// The resource component should have no resources yet
		assertEquals(resourceComponent.getResources().size(), 0);

		// Check for the MasterDetailsComponent.
		assertTrue(form.getComponent(3) instanceof MasterDetailsComponent);
		MasterDetailsComponent masterDetailsComponent = (MasterDetailsComponent) form
				.getComponent(3);
		assertEquals(masterDetailsComponent.getId(), 3);
		assertEquals(masterDetailsComponent.getName(), "Analysis Configuration");

		// At this point, there should be no masters in the component because no
		// data sources has been configured with which to populate the
		// component.
		assertEquals(null, masterDetailsComponent.getAllowedMasterValues());

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation selects a data source on the Form, submits it and confirms
	 * that the MasterDetailsComponent has been updated with selected asset
	 * types. Finally, it chooses some assets to create, submits the Form and
	 * checks that ReactorAnalyzer.getStatus().equals(FormStatus.ReadyToProcess)
	 * is true.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	@Test
	public void checkDataSourceReview() {
		// begin-user-code

		// Make the ReactorAnalyzer
		reactorAnalyzer = new ReactorAnalyzer(projectSpace);
		// Set the name
		reactorAnalyzer.setName(ReactorAnalyzerBuilder.name);
		// Set the analysis tools
		ArrayList<IAnalysisTool> tools = new ArrayList<IAnalysisTool>();
		tools.add(fakeAnalysisTool);
		reactorAnalyzer.setAnalysisTools(tools);

		// Configure the form
		Form form = configureAnalyzerForm();

		// Submit the Form and check the status
		FormStatus status = reactorAnalyzer.submitForm(form);
		assertEquals(FormStatus.ReadyToProcess, status);

		return;

		// end-user-code
	}

	private Form configureAnalyzerForm() {

		// Get the Form
		Form form = reactorAnalyzer.getForm();

		// Check the MasterDetailsComponent to make sure that some selections
		// are available
		MasterDetailsComponent masterDetailsComponent = (MasterDetailsComponent) form
				.getComponent(3);

		// Get the global data component
		DataComponent sourceData = (DataComponent) form.getComponent(1);

		// Make a selection
		sourceData.retrieveEntry("Input Data").setValue("globe.silo");

		// Resubmit the Form
		reactorAnalyzer.submitForm(form);

		// Retrieve the MasterDetailsComponent since it should have been
		// re-created to initialize the values.
		masterDetailsComponent = (MasterDetailsComponent) form.getComponent(3);

		// Add a master to the MasterDetailsComponent.
		int masterId = masterDetailsComponent.addMaster();
		ArrayList<String> masterValues = masterDetailsComponent
				.getAllowedMasterValues();
		assertNotNull(masterValues);
		assertTrue(masterValues.size() > 0);

		// Edit the details of the MasterDetailsComponent to make a selection
		masterDetailsComponent.setMasterInstanceValue(masterId,
				masterValues.get(0));

		return form;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the ReactorAnalyzer to create the selected assets.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	@Test
	public void checkProcessing() {
		// begin-user-code

		// Make the ReactorAnalyzer
		reactorAnalyzer = new ReactorAnalyzer(projectSpace);
		// Set the name
		reactorAnalyzer.setName(ReactorAnalyzerBuilder.name);
		// Set the analysis tools
		ArrayList<IAnalysisTool> tools = new ArrayList<IAnalysisTool>();
		tools.add(fakeAnalysisTool);
		reactorAnalyzer.setAnalysisTools(tools);

		// Configure the Form
		Form form = configureAnalyzerForm();

		// Submit it
		reactorAnalyzer.submitForm(form);

		// Since the Form has already been submitted, check the status and make
		// sure it is ready to process.
		assertEquals(FormStatus.ReadyToProcess, reactorAnalyzer.getStatus());

		// Direct the ReactorAnalyzer to process the Form
		FormStatus status = reactorAnalyzer
				.process("Generate Analysis Artifacts");
		assertEquals(FormStatus.Processing, reactorAnalyzer.getStatus());

		// Wait until the Item is done and make sure the process is
		// FormStatus.Processed.
		while (reactorAnalyzer.getStatus().equals(FormStatus.Processing)) {
			// Wait a little bit
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertEquals(FormStatus.Processed, reactorAnalyzer.getStatus());

		// Check the ResourceComponent
		form = reactorAnalyzer.getForm();
		ResourceComponent resourceComp = (ResourceComponent) form
				.getComponent(2);
		assertNotNull(resourceComp);

		// Get the MasterDetailsComponent component from the form
		MasterDetailsComponent analysisConfigComp = (MasterDetailsComponent) form
				.getComponent(3);

		// Get the list of resources from the ResourceComponent
		ArrayList<ICEResource> resources = resourceComp.getResources();

		// Check the resource list
		assertNotNull(resources);
		assertTrue(resources.size() > 0);
		assertEquals(analysisConfigComp.numberOfMasters(), resources.size());

		// Loop over each resource
		for (int i = 0; i < resources.size(); i++) {

			// Get a resource
			ICEResource resource = resources.get(i);

			// Test its properties
			assertEquals(resource.getId(), i);
		}

		// Try directing the Item to process with an unacceptable action name
		assertEquals(FormStatus.InfoError,
				reactorAnalyzer.process("Beam me up."));

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the ReactorAnalyzer to update the
	 * properties of an asset when its properties are changed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkProperties() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the ReactorAnalyzer to change data
	 * sources when so directed by a client.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	@Test
	public void checkResubmit() {
		// begin-user-code

		// Make the ReactorAnalyzer
		reactorAnalyzer = new ReactorAnalyzer(projectSpace);
		// Set the name
		reactorAnalyzer.setName(ReactorAnalyzerBuilder.name);
		// Set the analysis tools
		ArrayList<IAnalysisTool> tools = new ArrayList<IAnalysisTool>();
		tools.add(fakeAnalysisTool);
		reactorAnalyzer.setAnalysisTools(tools);

		// Get the Form
		Form form = reactorAnalyzer.getForm();

		// Check the MasterDetailsComponent to make sure that some selections
		// are available
		MasterDetailsComponent masterDetailsComponent = (MasterDetailsComponent) form
				.getComponent(3);

		// Get the global data component
		DataComponent sourceData = (DataComponent) form.getComponent(1);

		// Make a selection
		sourceData.retrieveEntry("Input Data").setValue("globe.silo");

		// Resubmit the Form
		reactorAnalyzer.submitForm(form);

		// Retrieve the new form
		form = reactorAnalyzer.getForm();

		// Retrieve the MasterDetailsComponent since it should have been
		// re-created to initialize the values.
		masterDetailsComponent = (MasterDetailsComponent) form.getComponent(3);
		ArrayList<String> masterValues = masterDetailsComponent
				.getAllowedMasterValues();
		assertNotNull(masterValues);
		assertTrue(masterValues.size() > 0);

		// Add a master to the MasterDetailsComponent.
		int masterId = masterDetailsComponent.addMaster();

		// Edit the details of the MasterDetailsComponent to make a selection
		masterDetailsComponent.setMasterInstanceValue(masterId,
				masterValues.get(0));

		// Check that a selection was made
		assertEquals(masterDetailsComponent.numberOfMasters(), 1);

		// Make a selection
		sourceData = (DataComponent) form.getComponent(1);
		sourceData.retrieveEntry("Input Data").setValue("AMPData.silo");

		// Resubmit the form
		reactorAnalyzer.submitForm(form);

		// Retrieve the new form
		form = reactorAnalyzer.getForm();

		// Retrieve the MasterDetailsComponent since it should have been
		// re-created to initialize the values.
		masterDetailsComponent = (MasterDetailsComponent) form.getComponent(3);
		masterValues = masterDetailsComponent.getAllowedMasterValues();
		assertTrue(masterValues.size() > 0);

		// Check that the selection was cleared
		assertEquals(0, masterDetailsComponent.numberOfMasters());

		return;

		// end-user-code
	}
}