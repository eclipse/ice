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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import org.eclipse.ice.client.widgets.EclipseTextEditor;
import org.eclipse.ice.datastructures.resource.ICEResource;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible checking the EclipseTextEditor.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseTextEditorTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EclipseTextEditor eclipseTextEditor;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation ensures that the EclipseTextEditor can manage ICEResources
	 * with its getter and setter operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkResources() {
		// begin-user-code

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
		// end-user-code
	}
}