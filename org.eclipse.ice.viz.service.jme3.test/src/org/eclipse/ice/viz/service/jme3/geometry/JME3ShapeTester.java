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

import org.eclipse.ice.viz.service.modeling.ShapeController;
import org.eclipse.ice.viz.service.modeling.ShapeMesh;
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
		ShapeMesh cubeComp = new ShapeMesh();
		cubeComp.setProperty("Type", "Cube");
		JME3ShapeView cubeView = new JME3ShapeView(cubeComp);
		ShapeController cube = new ShapeController(cubeComp, cubeView);
		assertTrue(cube.getRepresentation() instanceof Box);

		// Create a cylinder shape and check that the view returns a Cylinder
		ShapeMesh cylinderComp = new ShapeMesh();
		cylinderComp.setProperty("Type", "Cylinder");
		JME3ShapeView cylinderView = new JME3ShapeView(cylinderComp);
		ShapeController cylinder = new ShapeController(cylinderComp, cylinderView);
		assertTrue(cylinder.getRepresentation() instanceof Cylinder);

		// Create a sphere shape and check that the view returns a Sphere
		ShapeMesh sphereComp = new ShapeMesh();
		sphereComp.setProperty("Type", "Sphere");
		JME3ShapeView sphereView = new JME3ShapeView(sphereComp);
		ShapeController sphere = new ShapeController(sphereComp, sphereView);
		assertTrue(sphere.getRepresentation() instanceof Sphere);

		// Create a cube tube and check that the view returns a Tube
		ShapeMesh tubeComp = new ShapeMesh();
		tubeComp.setProperty("Type", "Tube");
		JME3ShapeView tubeView = new JME3ShapeView(tubeComp);
		ShapeController tube = new ShapeController(tubeComp, tubeView);
		assertTrue(tube.getRepresentation() instanceof Tube);

		// Create a non-rendered shape and check that the view returns null
		ShapeMesh noneComp = new ShapeMesh();
		noneComp.setProperty("Type", "None");
		JME3ShapeView noneView = new JME3ShapeView(noneComp);
		ShapeController none = new ShapeController(noneComp, noneView);
		assertNull(none.getRepresentation());

		// Create a shape with an invalid type and check that the view returns
		// null
		ShapeMesh badComp = new ShapeMesh();
		badComp.setProperty("Type", "Invalid string");
		JME3ShapeView badView = new JME3ShapeView(badComp);
		ShapeController bad = new ShapeController(badComp, badView);
		assertNull(bad.getRepresentation());

		// Create a shape with with no "Type" property and check that the view
		// returns null
		ShapeMesh unsetComp = new ShapeMesh();
		JME3ShapeView unsetView = new JME3ShapeView(unsetComp);
		ShapeController unset = new ShapeController(unsetComp, unsetView);
		assertNull(unset.getRepresentation());
	}

	/**
	 * Check that the shape returns the correct material based on its state
	 */
	@Test
	public void checkMaterial() {

		// Create a shape
		ShapeMesh model = new ShapeMesh();
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
