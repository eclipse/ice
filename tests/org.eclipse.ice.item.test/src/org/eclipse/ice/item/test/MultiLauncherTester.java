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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.viz.service.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IGeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.jobLauncher.multiLauncher.MultiLauncher;
import org.eclipse.ice.item.jobLauncher.multiLauncher.MultiLauncherBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the MultiLauncherBuilder and the
 * MultiLauncher classes. It realizes the IComponentVisitor interface so that it
 * can sort and examine the components from the MultiLauncher's Form.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class MultiLauncherTester implements IComponentVisitor {
	/**
	 * 
	 */
	private MultiLauncherBuilder multiLauncherBuilder;

	/**
	 * 
	 */
	private DataComponent dataComponent = null;
	/**
	 * <p>
	 * The MasterDetailsComponent from the MultiLauncher.
	 * </p>
	 * 
	 */
	private MasterDetailsComponent masterDetailsComponent = null;

	/**
	 * <p>
	 * A reference to the project space.
	 * </p>
	 * 
	 */
	private static IProject projectSpace;

	/**
	 * <p>
	 * The ResourceComponent from the MultiLauncher.
	 * </p>
	 * 
	 */
	private ResourceComponent resourceComponent;

	/**
	 * <p>
	 * This operation is called before the test to setup the project space.
	 * </p>
	 * 
	 */
	@BeforeClass
	public static void Before() {

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

	}

	/**
	 * <p>
	 * This operation checks the MultiLauncherBuilder to make sure that it can
	 * create a fully initialized MultiLauncher. It does this by registering
	 * several anonymous subclasses of Item, some with the ItemType.Simulation
	 * type and some with other types, and then calls build() to create the
	 * launcher. It considers the test successful if it can retrieve the Form
	 * from the launcher and find information for each of the Items that it
	 * registered with type ItemType.Simulation.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

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
			builder.setNameForTest("Sally " + i);
			builders.add(builder);
		}
		// Add one more that is not a JobLauncher
		builder = new FakeItemBuilder();
		builder.setNameForTest("Love Hitchiker");
		builder.setTypeForTest(ItemType.ModelOutputPackage);
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

	}

	/**
	 * <p>
	 * This operation modifies the Form retrieved from the MultiLauncher and
	 * submits it back to the launcher to make sure that it can be submitted
	 * successfully.
	 * </p>
	 * 
	 */
	@Test
	public void checkFormSubmission() {

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
			builder.setNameForTest("Sally " + i);
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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(DataComponent component)
	 */
	@Override
	public void visit(DataComponent component) {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(ResourceComponent component)
	 */
	@Override
	public void visit(ResourceComponent component) {

		resourceComponent = component;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TableComponent component)
	 */
	@Override
	public void visit(TableComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MatrixComponent component)
	 */
	@Override
	public void visit(MatrixComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IShape component)
	 */
	@Override
	public void visit(IShape component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(GeometryComponent component)
	 */
	@Override
	public void visit(IGeometryComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 */
	@Override
	public void visit(MasterDetailsComponent component) {

		// Set the MasterDetailsComponent
		masterDetailsComponent = component;
		// Set its header component
		dataComponent = masterDetailsComponent.getGlobalsComponent();

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TreeComposite component)
	 */
	@Override
	public void visit(TreeComposite component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IReactorComponent component)
	 */
	@Override
	public void visit(IReactorComponent component) {
		// TODO Auto-generated method stub

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
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EMFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ListComponent component) {
		// TODO Auto-generated method stub
		
	}
}