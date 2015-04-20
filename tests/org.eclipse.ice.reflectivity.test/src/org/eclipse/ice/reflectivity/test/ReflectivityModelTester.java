/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reflectivity.test;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ice.reflectivity.ReflectivityModel;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * This class is responsible for testing the ReflectivityModel. It implements
 * the IMaterialsDatabase interface so that it can make sure the reflectivity
 * model initializes properly when setupFormWithServices() is called.
 * 
 * @author Jay Jay Billings
 *
 */
public class ReflectivityModelTester implements IMaterialsDatabase {

	/**
	 * This operation checks the ReflectivityModel and makes sure that it can
	 * properly construct its Form.
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Local Declarations
		int listID = 1;
		ListComponent<Material> list;

		// Just create one with the nullary constructor
		// No need to check the Item with a IProject instance
		ReflectivityModel model = new ReflectivityModel();
		model.setMaterialsDatabase(this);
		model.setupFormWithServices();

		// Make sure we have a form and some components
		assertNotNull(model.getForm());
		assertEquals(1, model.getForm().getComponents().size());

		// Get the table component
		list = (ListComponent<Material>) model.getForm().getComponent(listID);

		// Make sure it's not null and the name is correct
		assertNotNull(list);
		assertEquals("Reflectivity Input Data", list.getName());

		// Make sure that the element source of the list is set to insure that
		// setupFormWithServices() worked.
		assertNotNull(list.getElementSource());
		assertEquals(list.getElementSource(),this);

		return;
	}

	@Override
	public EventList<Material> getElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableFormat<Material> getTableFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Material> getMaterials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMaterial(Material material) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMaterial(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMaterial(Material material) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateMaterial(Material material) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreDefaults() {
		// TODO Auto-generated method stub

	}

}
