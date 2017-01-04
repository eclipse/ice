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

import org.eclipse.ice.client.widgets.EclipseExtraInfoWidget;
import org.eclipse.january.form.Form;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the EclipseExtraInfoWidget.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseExtraInfoWidgetTester {
	/**
	 * <p>
	 * The EclipseExtraInfoWidget used in the test.
	 * </p>
	 * 
	 */
	private EclipseExtraInfoWidget eclipseExtraInfoWidget;
	/**
	 * <p>
	 * The Form used in the test.
	 * </p>
	 * 
	 */
	private Form form;

	/**
	 * <p>
	 * This operation is responsible for checking the Form accessors for the
	 * EclipseExtraInfoWidget class.
	 * </p>
	 * 
	 */
	@Test
	public void checkFormAccessors() {

		// Setup the Form
		form = new Form();
		form.setId(2);

		// Setup the widget and set the Form
		eclipseExtraInfoWidget = new EclipseExtraInfoWidget();
		eclipseExtraInfoWidget.setForm(form);

		// Check the Form
		assertEquals(2, eclipseExtraInfoWidget.getForm().getId());

		return;

	}
}