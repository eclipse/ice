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

import org.eclipse.ice.client.widgets.ICEDataComponentDetailsPage;
import org.eclipse.ice.client.widgets.ICEDetailsPageProvider;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.client.widgets.ICEMasterDetailsPage;
import org.eclipse.ice.client.widgets.ICEScrolledPropertiesBlock;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the ICEMasterDetailsPage and its
 * associates. It is one of the only classes in ICE that tests multiple other
 * classes in the same test suite, but this makes sense because of the specific
 * nature of the additional classes.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEMasterDetailsPageTester {

	/**
	 * <p>
	 * This operation checks the component getter and setter on the
	 * ICEMasterDetailsPage class.
	 * </p>
	 * 
	 */
	@Test
	public void checkMasterDetailsPageAccessors() {

		// Get two MasterDetailsComponent to check equality
		MasterDetailsComponent masterDetailsComponent = new MasterDetailsComponent();
		MasterDetailsComponent otherMasterDetailsComponent = new MasterDetailsComponent();

		// Get ICEMasterDetailsPage to check accessor
		ICEFormEditor ICEFormEditor = new ICEFormEditor();
		ICEMasterDetailsPage masterDetailsPage = new ICEMasterDetailsPage(
				ICEFormEditor, "id", "title");

		// Set, then get back another MasterDetailsComponent
		masterDetailsPage.setMasterDetailsComponent(masterDetailsComponent);
		otherMasterDetailsComponent = masterDetailsPage
				.getMasterDetailsComponent();

		// Check the equality
		assertTrue(masterDetailsComponent.equals(otherMasterDetailsComponent));

	}

	/**
	 * <p>
	 * This operation checks the component getter on the ICEDetailsPageProvider
	 * class.
	 * </p>
	 * 
	 */
	@Test
	public void checkDetailsPageProvider() {

		// Get two MasterDetailsComponent to check equality
		MasterDetailsComponent masterDetailsComponent = new MasterDetailsComponent();
		MasterDetailsComponent otherMasterDetailsComponent = new MasterDetailsComponent();

		// Get ICEMasterDetailsPage to check accessor
		ICEDetailsPageProvider ICEDetailsPageProvider = new ICEDetailsPageProvider(
				masterDetailsComponent, null);

		// Get back another MasterDetailsComponent
		otherMasterDetailsComponent = ICEDetailsPageProvider.getComponent();

		// Check the equality
		assertTrue(masterDetailsComponent.equals(otherMasterDetailsComponent));

	}

	/**
	 * <p>
	 * This operation checks the providers getters and setters on the
	 * ICEScrolledPropertiesBlock class.
	 * </p>
	 * 
	 */
	@Test
	public void checkScrolledPropertiesBlock() {

		// Get two ICEDetailsPageProvider to check equality
		MasterDetailsComponent masterDetailsComponent = new MasterDetailsComponent();
		ICEDetailsPageProvider pageProvider = new ICEDetailsPageProvider(
				masterDetailsComponent, null);
		ICEDetailsPageProvider otherPageProvider = new ICEDetailsPageProvider(
				masterDetailsComponent, null);

		// Get ICEScrolledPropertiesBlock to check accessor
		ICEScrolledPropertiesBlock ICEScrolledPropertiesBlock = new ICEScrolledPropertiesBlock(
				null, null, pageProvider);

		// Get back another MasterDetailsComponent
		otherPageProvider = (ICEDetailsPageProvider) ICEScrolledPropertiesBlock
				.getDetailsPageProvider();

		// Check the equality
		assertTrue(pageProvider.equals(otherPageProvider));

	}

	/**
	 * <p>
	 * This operation checks the component getter on the
	 * ICEDataComponentDetailsPage class.
	 * </p>
	 * 
	 */
	@Test
	public void checkDataComponentDetailsPage() {

		// Get two MasterDetailsComponent to check equality
		DataComponent dataComponent = new DataComponent();
		DataComponent otherDataComponent = new DataComponent();

		// Get ICEMasterDetailsPage to check accessor
		ICEDataComponentDetailsPage ICEDataComponentDetailsPage = new ICEDataComponentDetailsPage(
				dataComponent, null);

		// Get back another MasterDetailsComponent
		otherDataComponent = ICEDataComponentDetailsPage.getComponent();

		// Check the equality
		assertTrue(dataComponent.equals(otherDataComponent));

	}
}