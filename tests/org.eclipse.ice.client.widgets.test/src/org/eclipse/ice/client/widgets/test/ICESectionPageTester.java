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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICESectionPage;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.updateableComposite.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the ICESectionPage.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICESectionPageTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ICESectionPage that will be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICESectionPage page;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Component accessor operations on ICESectionPage
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkComponents() {
		// begin-user-code

		// Local Declarations
		DataComponent comp1 = new DataComponent(), comp2 = new DataComponent();
		ArrayList<DataComponent> compList = null;
		String compName = null;
		Form testForm = new Form();

		// Set some info on the components
		comp1.setName("Gravy");
		comp2.setName("Train");
		testForm.addComponent(comp1);
		testForm.addComponent(comp2);

		// Setup the ICESectionPage
		page = new ICESectionPage(new ICEFormEditor(), "1", "2");
		page.addComponent(comp1);
		page.addComponent(comp2);

		// Get the list of components and check them
		assertNotNull(page.getComponents());
		if (page.getComponents().size() > 1) {
			compList = new ArrayList<DataComponent>();
		} else {
			fail(); // Fail if there are no components in the list
		}
		for (Component i : page.getComponents()) {
			compList.add((DataComponent) i);
		}
		assertEquals(compList.size(), 2);
		compName = compList.get(0).getName();
		assertTrue("Gravy".equals(compName) || "Train".equals(compName));
		compName = compList.get(1).getName();
		assertTrue("Gravy".equals(compName) || "Train".equals(compName));

		return;

		// end-user-code
	}
}