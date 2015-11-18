/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;
import org.junit.Test;

import com.jme3.material.Material;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 * A class to test JME3Shape
 * 
 * @author Robert Smith
 *
 */
public class JME3ShapeTester {

	/**
	 * Test that the shape creates the correct JME3 object for each type of
	 * model
	 */
	@Test
	public void checkRepresentation() {

		// Create a cube shape and check that the view returns a Box
		ShapeComponent cubeComp = new ShapeComponent();
		cubeComp.setProperty("Type", "Cube");
		JME3ShapeView cubeView = new JME3ShapeView(cubeComp);
		Shape cube = new Shape(cubeComp, cubeView);
		assertTrue(cube.getRepresentation() instanceof Box);

		// Create a cylinder shape and check that the view returns a Cylinder
		ShapeComponent cylinderComp = new ShapeComponent();
		cylinderComp.setProperty("Type", "Cylinder");
		JME3ShapeView cylinderView = new JME3ShapeView(cylinderComp);
		Shape cylinder = new Shape(cylinderComp, cylinderView);
		assertTrue(cylinder.getRepresentation() instanceof Cylinder);

		// Create a sphere shape and check that the view returns a Sphere
		ShapeComponent sphereComp = new ShapeComponent();
		sphereComp.setProperty("Type", "Sphere");
		JME3ShapeView sphereView = new JME3ShapeView(sphereComp);
		Shape sphere = new Shape(sphereComp, sphereView);
		assertTrue(sphere.getRepresentation() instanceof Sphere);

		// Create a cube tube and check that the view returns a Tube
		ShapeComponent tubeComp = new ShapeComponent();
		tubeComp.setProperty("Type", "Tube");
		JME3ShapeView tubeView = new JME3ShapeView(tubeComp);
		Shape tube = new Shape(tubeComp, tubeView);
		assertTrue(tube.getRepresentation() instanceof Tube);

		// Create a non-rendered shape and check that the view returns null
		ShapeComponent noneComp = new ShapeComponent();
		noneComp.setProperty("Type", "None");
		JME3ShapeView noneView = new JME3ShapeView(noneComp);
		Shape none = new Shape(noneComp, noneView);
		assertNull(none.getRepresentation());

		// Create a shape with an invalid type and check that the view returns
		// null
		ShapeComponent badComp = new ShapeComponent();
		badComp.setProperty("Type", "Invalid string");
		JME3ShapeView badView = new JME3ShapeView(badComp);
		Shape bad = new Shape(badComp, badView);
		assertNull(bad.getRepresentation());

		// Create a shape with with no "Type" property and check that the view
		// returns null
		ShapeComponent unsetComp = new ShapeComponent();
		JME3ShapeView unsetView = new JME3ShapeView(unsetComp);
		Shape unset = new Shape(unsetComp, unsetView);
		assertNull(unset.getRepresentation());
	}

	/**
	 * Check that the shape returns the correct material based on its state
	 */
	@Test
	public void checkMaterial() {

		// Create a shape
		ShapeComponent model = new ShapeComponent();
		JME3ShapeView view = new JME3ShapeView(model);
		JME3Shape shape = new JME3Shape(model, view);

		// Create base and highlight materials
		Material base = new Material();
		Material highlight = new Material();

		// Set the shape's materials
		shape.setMaterial(base);
		shape.setHighlightedMaterial(highlight);

		// By default, the shape should have the base material
		assertEquals(base, shape.getMaterial());

		// When selected, the shape should have the highlighted material
		shape.setProperty("Selected", "True");
		assertEquals(highlight, shape.getMaterial());

		// When not selected, the shape should return to the base material
		shape.setProperty("Selected", "False");
		assertEquals(base, shape.getMaterial());

	}
}
