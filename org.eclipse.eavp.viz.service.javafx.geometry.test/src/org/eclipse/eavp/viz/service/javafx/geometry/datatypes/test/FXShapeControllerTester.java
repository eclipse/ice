/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.datatypes.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeController;
import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeView;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.eclipse.eavp.viz.service.modeling.Transformation;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestController;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestMesh;
import org.junit.Test;

import javafx.scene.Group;

/**
 * A class for testing the functionality of the FXShapeController.
 * 
 * @author Robert Smith
 *
 */
public class FXShapeControllerTester {

	/**
	 * Check that FXShapeControllers can be properly cloned.
	 */
	@Test
	public void checkClone() {

		// Create a cloned FXShape and check that it is identical to the
		// original
		ShapeMesh mesh = new ShapeMesh();
		FXShapeController shape = new FXShapeController(mesh,
				new FXShapeView(mesh));
		shape.setProperty("Test", "Property");
		FXShapeController clone = (FXShapeController) shape.clone();
		assertTrue(shape.equals(clone));
	}

	/**
	 * Test the FXShapeController's handling of the JavaFX Nodes' associations
	 * with ICE datastructures.
	 */
	@Test
	public void checkNodes() {

		// Create a cube
		ShapeMesh mesh = new ShapeMesh();
		mesh.setProperty("Type", "Cube");
		FXShapeView view = new FXShapeView(mesh);
		FXShapeController shape = new FXShapeController(mesh, view);

		// The JavaFX node should have a reference to the FXShapeController
		assertTrue(shape == ((Group) shape.getRepresentation()).getProperties()
				.get(ShapeController.class));

		// Create a sphere
		ShapeMesh mesh2 = new ShapeMesh();
		mesh2.setProperty("Type", "Sphere");
		FXShapeView view2 = new FXShapeView(mesh2);
		FXShapeController shape2 = new FXShapeController(mesh2, view2);

		// Add the sphere shape as a child to the cube shape. The cube JavaFX
		// node should now have the sphere JavaFX node as a child
		shape.addEntity(shape2);
		assertTrue(((Group) shape.getRepresentation()).getChildren()
				.contains(shape2.getRepresentation()));

		// Create a cylinder
		ShapeMesh mesh3 = new ShapeMesh();
		mesh2.setProperty("Type", "Cylinder");
		FXShapeView view3 = new FXShapeView(mesh3);
		FXShapeController shape3 = new FXShapeController(mesh3, view3);

		// Add the cylinder shape as a child to the cube shape. The cube JavaFX
		// node should now have both JavaFX nodes as children.
		shape.addEntity(shape3);
		assertTrue(((Group) shape.getRepresentation()).getChildren()
				.contains(shape2.getRepresentation()));
		assertTrue(((Group) shape.getRepresentation()).getChildren()
				.contains(shape3.getRepresentation()));

		// Remove the cube from being the sphere's parent. This should remove
		// its JavaFX node from the cube's JavaFX node's children.
		shape2.removeEntity(shape);
		assertFalse(((Group) shape.getRepresentation()).getChildren()
				.contains(shape2.getRepresentation()));

		// Remove the cylinder from the sphere's children. This should also
		// remove its JavaFX node from the cube's JavaFX node's children
		shape.removeEntity(shape3);
		assertFalse(((Group) shape.getRepresentation()).getChildren()
				.contains(shape3.getRepresentation()));

		// Create a union
		ShapeMesh unionMesh = new ShapeMesh();
		unionMesh.setProperty("Operator", "Union");
		FXShapeView unionView = new FXShapeView(unionMesh);
		FXShapeController unionShape = new FXShapeController(unionMesh,
				unionView);

		// Set the shape's parent as the union. This should add its JavaFX node
		// to the union's java fxnode's children.
		shape.setParent(unionShape);
		assertTrue(((Group) unionShape.getRepresentation()).getChildren()
				.contains(shape.getRepresentation()));
	}

	/**
	 * Test that the FXShape controller receives and handles updates correctly.
	 */
	@Test
	public void checkUpdates() {

		// Create a cube
		ShapeMesh mesh = new ShapeMesh();
		mesh.setProperty("Type", "Cube");
		FXShapeView view = new FXShapeView(mesh);
		FXShapeController shape = new FXShapeController(mesh, view);

		// Create a test controller to monitor when the shape is updated
		TestController controller = new TestController(new TestMesh(),
				new AbstractView());
		controller.addEntity(shape);

		// Clear the controller's updated state
		controller.isUpdated();

		// Add a child to the mesh
		mesh.addEntityByCategory(
				new AbstractController(new AbstractMesh(), new AbstractView()),
				"Test");

		// This should have sent an update to the controller
		assertTrue(controller.isUpdated());

		// Select the mesh
		mesh.setProperty("Selected", "True");

		// The controller should have been updated
		assertTrue(controller.isUpdated());

		// Change one of the mesh's properties
		mesh.setProperty("Test", "Property");

		// The controller should not be subscribed to general property messages
		// from the mesh
		assertFalse(controller.isUpdated());

		// Add a second child to the controller.
		FXShapeController child = new FXShapeController(new ShapeMesh(),
				new FXTestView());
		shape.addEntity(child);

		// Update the view's transformation
		view.setTransformation(new Transformation());

		// The controller should have been updated
		assertTrue(controller.isUpdated());

		// The controller should have recursively refreshed all childrens' views
		assertTrue(((FXTestView) child.getView()).isRefreshed());
	}

	private class FXTestView extends FXShapeView {

		/**
		 * Whether or not this view has been refreshed since the last time it
		 * was checked.
		 */
		boolean refreshed = false;

		/**
		 * Checks if the view was refreshed and resets it to its initial,
		 * unrefreshed state.
		 * 
		 * @return True if the view has been refreshed since the last time that
		 *         isRefreshed() was invoked. False otherwise.
		 */
		public boolean isRefreshed() {
			boolean temp = refreshed;
			refreshed = false;
			return temp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeView#
		 * refresh(org.eclipse.eavp.viz.service.modeling.AbstractMesh)
		 */
		@Override
		public void refresh(AbstractMesh model) {
			refreshed = true;
			super.refresh(model);
		}

	}
}
