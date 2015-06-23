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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEGeometryPage;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.junit.Test;

/**
 * <p>
 * This class tests the accessors of the ICEGeometryPage.
 * </p>
 * 
 * @author Gregory M. Lyon
 */
public class ICEGeometryPageTester {
	/**
	 * <p>
	 * Checks the accessors for getting and setting a geometryComponent in
	 * geometryPage.
	 * </p>
	 * 
	 */
	@Test
	public void checkGeometryPageAccessors() {

		// Get two GeometryComponent to check equality
		GeometryComponent geometryComponent = new GeometryComponent();
		GeometryComponent otherGeometryComponent = new GeometryComponent();

		// Get ICEGeometryPage to check accessor
		ICEFormEditor ICEFormEditor = new ICEFormEditor();
		ICEGeometryPage GeometryPage = new ICEGeometryPage(ICEFormEditor, "id",
				"title");

		// Set, then get back another GeometryComponent
		GeometryPage.setGeometry(geometryComponent);
		otherGeometryComponent = GeometryPage.getGeometry();

		// Check the equality
		assertTrue(geometryComponent.equals(otherGeometryComponent));

	}
}