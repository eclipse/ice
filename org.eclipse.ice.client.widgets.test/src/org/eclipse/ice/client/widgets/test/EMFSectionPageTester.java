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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.client.widgets.EMFSectionPage;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.january.form.emf.EMFComponent;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the EMFSectionPage class.
 * It only tests the accessor operations for the EMFComponent.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EMFSectionPageTester {
	/**
	 * 
	 */
	private EMFSectionPage emfSectionPage;

	/**
	 * <p>
	 * </p>
	 * 
	 */
	@Test
	public void checkEMFComponent() {

		// Local Declarations
		EMFComponent comp1 = new EMFComponent(), comp2 = null;

		// Set some info on the first component
		comp1.setName("Gravy");

		// Setup the EMFSectionPage
		emfSectionPage = new EMFSectionPage(new ICEFormEditor(), "", "");
		emfSectionPage.setEMFComponent(comp1);

		// Get the component and check it
		comp2 = emfSectionPage.getEMFComponent();
		assertNotNull(comp2);
		assertEquals(comp2.getName(), "Gravy");

		// Reset comp2 and 
		comp2 = null;

		// Grab the Component from the 
		comp2 = emfSectionPage.getEMFComponent();
		assertNotNull(comp2);

		return;

	}
}