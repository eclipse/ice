/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.geometry.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.jme3.geometry.JME3ControllerFactory;
import org.eclipse.ice.viz.service.jme3.geometry.JME3Shape;
import org.eclipse.ice.viz.service.jme3.geometry.Tube;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;
import org.junit.Test;

import com.jme3.material.Material;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 * A class to test the functionality of a JME3Shape
 * 
 * @author Robert Smith
 *
 */
public class JME3ShapeTester {

	/**
	 * Test that the View creates the correct kind of mesh
	 */
	@Test
	public void checkRepresentation() {

		// Create a cube shape
		ShapeComponent component = new ShapeComponent();
		component.setProperty("Type", ShapeType.Cube.toString());
		JME3ControllerFactory factory = new JME3ControllerFactory();
		JME3Shape shape = (JME3Shape) factory.createController(component);

		// Check that its representation is a JME3 box
		assertTrue(shape.getRepresentation() instanceof Box);

		// Change the shape to a Cylinder and check that its new representation
		// is correct
		component.setProperty("Type", ShapeType.Cylinder.toString());

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(shape.getRepresentation() instanceof Cylinder);

		// Change the shape to a type which cannot be rendered and ensure it
		// returns a null representation
		component.setProperty("Type", ShapeType.None.toString());

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertNull(shape.getRepresentation());

		// Check the representations for spheres and tubes
		component.setProperty("Type", ShapeType.Sphere.toString());

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(shape.getRepresentation() instanceof Sphere);
		component.setProperty("Type", ShapeType.Tube.toString());

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(shape.getRepresentation() instanceof Tube);

	}

	/**
	 * Test that the Shape is correctly managing the material for displaying its
	 * selected status
	 */
	@Test
	public void checkMaterial() {

		// Create a shape
		ShapeComponent component = new ShapeComponent();
		component.setProperty("Type", ShapeType.Cube.toString());
		JME3ControllerFactory factory = new JME3ControllerFactory();
		JME3Shape shape = (JME3Shape) factory.createController(component);

		// Create some materials and assign them to the shape
		Material base = new Material();
		Material highlight = new Material();
		shape.setMaterial(base);
		shape.setHighlightedMaterial(highlight);

		// The shape should return the base material by default

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(base == shape.getMaterial());

		// When selected, it should return the highlighted material
		shape.setProperty("Selected", "True");

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(highlight == shape.getMaterial());

		// Deselecting it should return it to the base material
		shape.setProperty("Selected", "False");

		// // Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(base == shape.getMaterial());

		// Add the shape to a union
		ShapeComponent unionComponent = new ShapeComponent();
		component.setProperty("Operator", "Union");
		JME3Shape union = (JME3Shape) factory.createController(unionComponent);
		union.addEntity(shape);

		// When the union is selected, all of its children should be too.
		union.setProperty("Selected", "True");

		// // Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue("True".equals(shape.getProperty("Selected")));
		assertTrue(highlight == shape.getMaterial());

		// When deselected, its children should again be deselected
		union.setProperty("Selected", "False");

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue("False".equals(shape.getProperty("Selected")));
		assertTrue(base == shape.getMaterial());

		// The shape should still be able to be selected independently of its
		// parent
		shape.setProperty("Selected", "True");

		// Give the thread time to update the shape
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		assertTrue(highlight == shape.getMaterial());

	}
}
