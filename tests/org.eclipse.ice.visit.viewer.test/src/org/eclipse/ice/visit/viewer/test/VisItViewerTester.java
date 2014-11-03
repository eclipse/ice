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
package org.eclipse.ice.visit.viewer.test;

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
import org.eclipse.ice.visit.viewer.VisItViewer;
import org.eclipse.ice.visit.viewer.VisItViewerBuilder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the VisItViewer.
 * </p>
 * <p>
 * This is a complicated series of tests, but the coverage is still relatively
 * high. The tester creates and accesses a project, checks the VisItViewer's
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
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItViewerTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The instance of VisItViewer that will be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static VisItViewer visItViewer;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fake implementation of IAnalysisTool that is used to trick the
	 * VisItViewer without loading heavyweight components.
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
	 * VisItViewer without loading heavyweight components.
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
	 * VisItViewer without loading heavyweight components.
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
	 * The array for storing the fake analysis tools in the test.
	 */
	private static ArrayList<IAnalysisTool> tools;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations sets up the workspace for an instantiates a VisItViewer
	 * for testing.
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

		// Initialize fake analysis tool
		fakeAnalysisTool = new FakeAnalysisTool();

		// Set null values for the other fakes so that they must be updated
		fakeAnalysisDocument = null;
		fakeAnalysisAsset = null;

		// Set the analysis tools
		tools = new ArrayList<IAnalysisTool>();
		tools.add(fakeAnalysisTool);

		// Setup the project workspace
		try {
			// Get the project handle
			projectSpace = workspaceRoot
					.getProject("VisItViewerTesterWorkspace");
			// If the project does not exist, create it
			if (!projectSpace.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "VisItViewerTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("VisItViewerTesterWorkspace");
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
	 * This operation checks the construction of the VisItViewer to ensure that
	 * its Form was created according to the specification on the class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Make the VisItViewer
		visItViewer = new VisItViewer(projectSpace);
		// Set the name
		visItViewer.setName(VisItViewerBuilder.name);
		// Set the analysis tools
		visItViewer.setAnalysisTools(tools);

		// Get the Form
		Form form = visItViewer.getForm();

		// Check the form's components
		assertNotNull(form);
		assertNotNull(form.getComponents());
		assertEquals(2, form.getComponents().size()); // original expected: 3,
														// erroneous

		// The Components of the VisItViewer's Form are very specifically
		// identified in the VisItViewer documentation. First check for the
		// MasterDetailsComponent.
		assertTrue(form.getComponent(1) instanceof MasterDetailsComponent);
		MasterDetailsComponent masterDetailsComponent = (MasterDetailsComponent) form
				.getComponent(1);
		assertEquals(masterDetailsComponent.getId(), 1);
		assertEquals(masterDetailsComponent.getName(), "Analysis Configuration");

		// At this point, there should be no masters in the component because no
		// data sources has been configured with which to populate the
		// component.
		assertEquals(null, masterDetailsComponent.getAllowedMasterValues());

		// Now check the header component, which is a DataComponent
		DataComponent dataComponent = masterDetailsComponent
				.getGlobalsComponent();
		assertTrue(dataComponent instanceof DataComponent);
		assertEquals(dataComponent.getId(), 3);
		assertEquals(dataComponent.getName(), "Data Sources");

		// Also make sure that there are available data sources to select
		Entry dataSources = dataComponent
				.retrieveEntry("Available Data Sources");
		assertNotNull(dataSources);
		assertEquals("Available Data Sources", dataSources.getName());
		assertEquals(1, dataSources.getId());
		assertTrue(dataSources.getAllowedValues().contains("globe.silo"));

		// Now check the ResourceComponent
		assertTrue(form.getComponent(3) instanceof ResourceComponent);
		ResourceComponent resourceComponent = (ResourceComponent) form
				.getComponent(3);
		assertEquals(resourceComponent.getId(), 3);
		assertEquals(resourceComponent.getName(), "Analysis Artifacts");
		// The resource component should have no resources yet
		assertEquals(resourceComponent.getResources().size(), 0);

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation selects a data source on the Form, submits it and confirms
	 * that the MasterDetailsComponent has been updated with selected asset
	 * types. Finally, it chooses some assets to create, submits the Form and
	 * checks that VisItViewer.getStatus().equals(FormStatus.ReadyToProcess) is
	 * true.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkDataSourceReview() {
		// begin-user-code

		// Make the VisItViewer
		visItViewer = new VisItViewer(projectSpace);
		// Set the name
		visItViewer.setName(VisItViewerBuilder.name);
		// Set the analysis tools
		visItViewer.setAnalysisTools(tools);

		// Configure the form
		Form form = configureAnalyzerForm();

		// Submit the Form and check the status
		FormStatus status = visItViewer.submitForm(form);
		assertEquals(FormStatus.ReadyToProcess, status);

		return;

		// end-user-code
	}

	private Form configureAnalyzerForm() {

		// Make the VisItViewer
		visItViewer = new VisItViewer(projectSpace);
		// Set the name
		visItViewer.setName(VisItViewerBuilder.name);
		// Set the analysis tools
		visItViewer.setAnalysisTools(tools);

		// Get the Form
		Form form = visItViewer.getForm();

		// Check the MasterDetailsComponent to make sure that some selections
		// are available
		MasterDetailsComponent masterDetailsComponent = (MasterDetailsComponent) form
				.getComponent(1);

		// Get the global data component
		DataComponent globalData = masterDetailsComponent.getGlobalsComponent();

		// Make a selection
		globalData.retrieveEntry("Available Data Sources").setValue(
				"globe.silo");

		// Resubmit the Form
		visItViewer.submitForm(form);

		// Retrieve the MasterDetailsComponent since it should have been
		// re-created to initialize the values.
		masterDetailsComponent = (MasterDetailsComponent) form.getComponent(1);

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
	 * This operation directs the VisItViewer to create the selected assets.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkProcessing() {
		// begin-user-code

		// Make the VisItViewer
		visItViewer = new VisItViewer(projectSpace);
		// Set the name
		visItViewer.setName(VisItViewerBuilder.name);
		// Set the analysis tools
		visItViewer.setAnalysisTools(tools);

		// Configure the Form
		Form form = configureAnalyzerForm();

		// Submit it
		visItViewer.submitForm(form);

		// Since the Form has already been submitted, check the status and make
		// sure it is ready to process.
		assertEquals(FormStatus.ReadyToProcess, visItViewer.getStatus());

		// Direct the VisItViewer to process the Form
		FormStatus status = visItViewer.process("Generate Analysis Artifacts");
		assertEquals(FormStatus.Processing, visItViewer.getStatus());

		// Wait until the Item is done and make sure the process is
		// FormStatus.Processed.
		while (visItViewer.getStatus().equals(FormStatus.Processing)) {
			// Wait a little bit
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertEquals(FormStatus.Processed, visItViewer.getStatus());

		// Check the ResourceComponent
		form = visItViewer.getForm();
		ResourceComponent resourceComp = (ResourceComponent) form
				.getComponent(3);
		assertNotNull(resourceComp);

		// Get the MasterDetailsComponent component from the form
		MasterDetailsComponent analysisConfigComp = (MasterDetailsComponent) form
				.getComponent(1);

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
			assertTrue(resource.isPictureType());

		}

		// Try directing the Item to process with an unacceptable action name
		assertEquals(FormStatus.InfoError, visItViewer.process("Beam me up."));

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the VisItViewer to update the
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
	 * This operation checks the ability of the VisItViewer to change data
	 * sources when so directed by a client.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkResubmit() {
		// begin-user-code

		// Make the VisItViewer
		visItViewer = new VisItViewer(projectSpace);
		// Set the name
		visItViewer.setName(VisItViewerBuilder.name);
		// Set the analysis tools
		visItViewer.setAnalysisTools(tools);

		// Get the Form
		Form form = visItViewer.getForm();

		// Check the MasterDetailsComponent to make sure that some selections
		// are available
		MasterDetailsComponent masterDetailsComponent = (MasterDetailsComponent) form
				.getComponent(1);

		// Get the global data component
		DataComponent globalData = masterDetailsComponent.getGlobalsComponent();

		// Make a new selection
		globalData.retrieveEntry("Available Data Sources").setValue(
				"AMPData.silo");

		// Resubmit the Form
		visItViewer.submitForm(form);

		// Retrieve the new form
		form = visItViewer.getForm();

		// Retrieve the MasterDetailsComponent since it should have been
		// re-created to initialize the values.
		masterDetailsComponent = (MasterDetailsComponent) form.getComponent(1);
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
		assertEquals(1, masterDetailsComponent.numberOfMasters());

		// Make a new selection
		masterDetailsComponent.getGlobalsComponent()
				.retrieveEntry("Available Data Sources").setValue("globe.silo");

		// Resubmit the form
		visItViewer.submitForm(form);

		// Retrieve the new form
		form = visItViewer.getForm();

		// Retrieve the MasterDetailsComponent since it should have been
		// re-created to initialize the values.
		masterDetailsComponent = (MasterDetailsComponent) form.getComponent(1);
		masterValues = masterDetailsComponent.getAllowedMasterValues();
		assertTrue(masterValues.size() > 0);

		// Check that the selection was cleared
		assertEquals(0, masterDetailsComponent.numberOfMasters());

		return;

		// end-user-code
	}
}