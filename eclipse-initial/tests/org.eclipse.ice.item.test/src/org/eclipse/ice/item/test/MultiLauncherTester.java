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
package org.eclipse.ice.item.test;

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

import static org.eclipse.ice.item.jobLauncher.multiLauncher.MultiLauncher.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.BatteryComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.jobLauncher.multiLauncher.MultiLauncher;
import org.eclipse.ice.item.jobLauncher.multiLauncher.MultiLauncherBuilder;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the MultiLauncherBuilder and the
 * MultiLauncher classes. It realizes the IComponentVisitor interface so that it
 * can sort and examine the components from the MultiLauncher's Form.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MultiLauncherTester implements IComponentVisitor {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MultiLauncherBuilder multiLauncherBuilder;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private DataComponent dataComponent = null;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The MasterDetailsComponent from the MultiLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MasterDetailsComponent masterDetailsComponent = null;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to the project space.
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
	 * The ResourceComponent from the MultiLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ResourceComponent resourceComponent;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is called before the test to setup the project space.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void Before() {
		// begin-user-code

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		String separator = System.getProperty("file.separator");
		String filename = null;

		// Setup the project
		try {
			// Get the project handle
			projectSpace = workspaceRoot.getProject("itemTesterWorkspace");
			// If the project does not exist, create it
			if (!projectSpace.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemTesterWorkspace");
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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the MultiLauncherBuilder to make sure that it can
	 * create a fully initialized MultiLauncher. It does this by registering
	 * several anonymous subclasses of Item, some with the ItemType.Simulation
	 * type and some with other types, and then calls build() to create the
	 * launcher. It considers the test successful if it can retrieve the Form
	 * from the launcher and find information for each of the Items that it
	 * registered with type ItemType.Simulation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Create the builder
		int numBuilders = 4;
		FakeItemBuilder builder = null;
		ArrayList<ItemBuilder> builders = new ArrayList<ItemBuilder>();
		multiLauncherBuilder = new MultiLauncherBuilder();

		// Check the details of the builder
		assertEquals("MultiLauncher", multiLauncherBuilder.getItemName());
		assertEquals(ItemType.Simulation, multiLauncherBuilder.getItemType());

		// Make sure calling build returns null since no builders have been
		// added
		assertNull(multiLauncherBuilder.build(projectSpace));

		// Create some ItemBuilders and register them with the
		// MultiLauncherBuilder
		for (int i = 0; i < numBuilders; i++) {
			builder = new FakeItemBuilder();
			builder.setName("Sally " + i);
			builders.add(builder);
		}
		// Add one more that is not a JobLauncher
		builder = new FakeItemBuilder();
		builder.setName("Love Hitchiker");
		builder.setType(ItemType.ModelOutputPackage);
		builders.add(builder);

		// Set the list of builders
		multiLauncherBuilder.addBuilders(builders);

		// Build a MultiLauncher
		Item launcher = multiLauncherBuilder.build(projectSpace);

		// Check the launcher
		assertNotNull(launcher);
		assertTrue(launcher instanceof MultiLauncher);
		assertTrue(launcher.getName().contains("MultiLauncher"));

		// Grab the Form and take a look at it. It should have one component.
		Form form = launcher.getForm();
		assertEquals(2, form.getNumberOfComponents());

		// Get the Component from the Form and visit it to get the data and
		// master-details components
		ArrayList<Component> components = form.getComponents();
		assertNotNull(components);
		assertEquals(2, components.size());
		for (Component component : components) {
			component.accept(this);
		}
		// Check the DataComponent. This component is the header or "globals"
		// component from the MasterDetailsComponent of the launcher.
		assertNotNull(dataComponent);
		assertEquals("Execution Mode", dataComponent.getName());
		assertNotNull(dataComponent.retrieveEntry("Enable Parallel Execution"));
		// The DataComponent should have id = 3
		assertEquals(3, dataComponent.getId());

		// Check the MasterDetailsComponent
		assertNotNull(masterDetailsComponent);
		assertEquals(numBuilders, masterDetailsComponent
				.getAllowedMasterValues().size());
		// The MasterDetailsComponent should have id = 1
		assertEquals(1, masterDetailsComponent.getId());
		ArrayList<String> allowedMasterValues = masterDetailsComponent
				.getAllowedMasterValues();
		// Loop over the builders and check them as they were constructed above.
		for (int i = 0; i < numBuilders; i++) {
			assertTrue(allowedMasterValues.contains("Sally " + i));
		}
		// The launcher should not contain an Item named "Love Hitchiker"
		// because it was not a JobLauncher.
		assertTrue(!allowedMasterValues.contains("Love Hitchiker"));

		// Check the ResourceComponent
		assertNotNull(resourceComponent);
		// The ResourceComponent should have id = 2
		assertEquals(2, resourceComponent.getId());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation modifies the Form retrieved from the MultiLauncher and
	 * submits it back to the launcher to make sure that it can be submitted
	 * successfully.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFormSubmission() {
		// begin-user-code

		// Create the builder
		int numBuilders = 4;
		FakeItemBuilder builder = null;
		ArrayList<ItemBuilder> builders = new ArrayList<ItemBuilder>();
		multiLauncherBuilder = new MultiLauncherBuilder();

		// Check the details of the builder
		assertEquals("MultiLauncher", multiLauncherBuilder.getItemName());
		assertEquals(ItemType.Simulation, multiLauncherBuilder.getItemType());

		// Create some ItemBuilders and register them with the
		// MultiLauncherBuilder
		for (int i = 0; i < numBuilders; i++) {
			builder = new FakeItemBuilder();
			builder.setName("Sally " + i);
			builders.add(builder);
		}
		// Set the list of builders
		multiLauncherBuilder.addBuilders(builders);

		// Build a MultiLauncher
		Item launcher = multiLauncherBuilder.build(projectSpace);
		assertNotNull(launcher);

		// Grab the Form and get the component
		Form form = launcher.getForm();
		ArrayList<Component> components = form.getComponents();
		components.get(0).accept(this);

		// Add a few "Sally" launchers
		int id = masterDetailsComponent.addMaster();
		masterDetailsComponent.setMasterInstanceValue(id, "Sally 1");
		masterDetailsComponent.addMaster();
		masterDetailsComponent.setMasterInstanceValue(id, "Sally 1");
		masterDetailsComponent.addMaster();
		masterDetailsComponent.setMasterInstanceValue(id, "Sally 2");
		masterDetailsComponent.addMaster();
		masterDetailsComponent.setMasterInstanceValue(id, "Sally 0");

		// Submit the Form and make sure it is accepted
		assertEquals(FormStatus.ReadyToProcess, launcher.submitForm(form));

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(DataComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(DataComponent component) {
		// begin-user-code

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(ResourceComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ResourceComponent component) {
		// begin-user-code

		resourceComponent = component;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TableComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TableComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MatrixComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MatrixComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IShape component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IShape component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(GeometryComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(GeometryComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MasterDetailsComponent component) {
		// begin-user-code

		// Set the MasterDetailsComponent
		masterDetailsComponent = component;
		// Set its header component
		dataComponent = masterDetailsComponent.getGlobalsComponent();

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TreeComposite component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TreeComposite component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IReactorComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IReactorComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	@Override
	public void visit(TimeDataComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MeshComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BatteryComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}
}