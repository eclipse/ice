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
package org.eclipse.ice.tests.client.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.ice.client.widgets.EclipseTextEditor;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.junit.Test;

/**
 * <p>
 * This class is responsible checking the EclipseTextEditor.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseTextEditorTester {
	/**
	 * 
	 */
	private EclipseTextEditor eclipseTextEditor;

	/**
	 * <p>
	 * This operation ensures that the EclipseTextEditor can manage ICEResources
	 * with its getter and setter operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkResources() {

		// Local Declarations
		ICEResource res1 = null, retResource = null;
		try {
			res1 = new ICEResource(new File("Nemesis"));
			retResource = null;
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		// There's no way it should have gotten out here like this, but check
		// anyway.
		assertNotNull(res1);

		// Initialize the editor
		eclipseTextEditor = new EclipseTextEditor();

		// Setup the Resource
		res1.setName("Last Star Trek TNG Movie");
		res1.setId(9);
		res1.setDescription("Brent Spiner directed this movie, which "
				+ "featured a Picard clone from Romulus, and killed Data "
				+ "off because he was getting too old for the role.");

		// Set the editor's resource
		eclipseTextEditor.setResource(res1);

		// Retrieve the resource and make sure it is the same one;
		retResource = eclipseTextEditor.getResource();
		assertNotNull(retResource);
		assertEquals(res1.getId(), retResource.getId());
		assertEquals(res1.getName(), retResource.getName());
		assertEquals(res1.getDescription(), retResource.getDescription());

		return;
	}
}