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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEMatrixComponentSectionPart;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the ICEMatrixComponentSectionPart
 * class. It only tests the accessor operations for the MatrixComponent and the
 * update routine from IComponentListener.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEMatrixComponentSectionPartTester {
	/**
	 * 
	 */
	private ICEMatrixComponentSectionPart ICEMatrixComponentSectionPart;

	/**
	 * <p>
	 * This operation checks the ICESectionPart to make sure that it can store a
	 * MatrixComponent. This operation also tests the update operation of
	 * ICESectionPart by modifying a MatrixComponent and insuring that the
	 * ICESectionPart gets the update.
	 * </p>
	 * 
	 */
	@Test
	@Ignore
	public void checkMatrixComponent() {
		MatrixComponent matrixComponent = new MatrixComponent();
		Display display;
		FormToolkit formTk;
		ManagedForm eclipseTestForm;

		// Setup the display, form toolkit and test form. The display must be
		// retrieve from the Eclipse PlatformUI if it is running or created
		// separately if not.
		if (!PlatformUI.isWorkbenchRunning()) {
			display = new Display();
		} else {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		formTk = new FormToolkit(display);
		eclipseTestForm = new ManagedForm(new Shell(display));

		// Set up the matrix component
		matrixComponent.addColumn();
		matrixComponent.addRow();
		// Should now be a 2x2
		matrixComponent.setName("Matrix 1");
		matrixComponent.setId(1);
		matrixComponent.setDescription("Description");

		// Verify it is 2x2
		assertEquals(matrixComponent.numberOfColumns(), 2);
		assertEquals(matrixComponent.numberOfRows(), 2);

		// Create the Section Part
		ICEMatrixComponentSectionPart = new ICEMatrixComponentSectionPart(
				formTk.createSection(eclipseTestForm.getForm().getBody(),
						Section.TITLE_BAR | Section.DESCRIPTION
								| Section.TWISTIE | Section.EXPANDED),
				new ICEFormEditor(), eclipseTestForm);

		// Assert the SectionParts reference to the MatrixComponent is initially
		// null
		assertNull(ICEMatrixComponentSectionPart.getMatrixComponent());

		// Set the MatrixComponent on the SectionPart
		ICEMatrixComponentSectionPart.setMatrixComponent(matrixComponent);

		// Now verify it is not null and is the same as the one we just gave it
		assertNotNull(ICEMatrixComponentSectionPart.getMatrixComponent());
		assertTrue(ICEMatrixComponentSectionPart.getMatrixComponent().equals(
				matrixComponent));

		// Assert that when passing a null MatrixComponent to the SectionPart,
		// it
		// does not get set as the SectionPart's MatrixComponent reference
		ICEMatrixComponentSectionPart.setMatrixComponent(null);
		MatrixComponent temp = ICEMatrixComponentSectionPart
				.getMatrixComponent();
		assertNotNull(temp);
		assertTrue(temp.equals(matrixComponent));

		// Clean up the Display
		display.dispose();

	}
}