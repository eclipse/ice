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

import org.junit.Test;

import org.eclipse.ice.client.widgets.ExtraInfoDialog;
import org.eclipse.ice.datastructures.form.DataComponent;

/**
 * <p>
 * This class is responsible for testing the ExtraInfoDialog class.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ExtraInfoDialogTester {
	/**
	 * <p>
	 * The dialog to test.
	 * </p>
	 * 
	 */
	private ExtraInfoDialog extraInfoDialog;

	/**
	 * <p>
	 * This operation checks the DataComponent accessors on the ExtraInfoDialog
	 * class.
	 * </p>
	 * 
	 */
	@Test
	public void checkAccessors() {

		// Local Declarations
		DataComponent comp = new DataComponent();

		// Initialize the dialog
		extraInfoDialog = new ExtraInfoDialog(null);

		// Setup the DataComponent
		comp.setId(536);
		comp.setName("Extra Info Dialog Test Data Component");
		comp.setName("A component for the ExtraInfoDialogTester that Jay "
				+ "had to create after being the first person to break Bones' "
				+ "new Maven+Tycho build.");

		// Check the dialog's accessors
		assertNull(extraInfoDialog.getDataComponent());
		extraInfoDialog.setDataComponent(comp);
		assertNotNull(extraInfoDialog.getDataComponent());
		assertEquals(comp, extraInfoDialog.getDataComponent());

		return;
	}
}