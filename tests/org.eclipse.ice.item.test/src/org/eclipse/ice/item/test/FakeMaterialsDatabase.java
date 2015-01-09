/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.item.test;

import java.util.List;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.IMaterialsDatabase;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * A fake implementation of the IMaterialsDatabase used for testing.
 * 
 * @author Jay Jay Billings
 * 
 */
public class FakeMaterialsDatabase implements IMaterialsDatabase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.materials.IMaterialsDatabase#getMaterials()
	 */
	@Override
	public List<Material> getMaterials() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialsDatabase#addMaterial(org.eclipse.
	 * ice.datastructures.form.Material)
	 */
	@Override
	public void addMaterial(Material material) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialsDatabase#deleteMaterial(java.lang
	 * .String)
	 */
	@Override
	public void deleteMaterial(String name) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialsDatabase#deleteMaterial(org.eclipse
	 * .ice.datastructures.form.Material)
	 */
	@Override
	public void deleteMaterial(Material material) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.materials.IMaterialsDatabase#updateMaterial(org.eclipse
	 * .ice.datastructures.form.Material)
	 */
	@Override
	public void updateMaterial(Material material) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.materials.IMaterialsDatabase#restoreDefaults()
	 */
	@Override
	public void restoreDefaults() {
		// TODO Auto-generated method stub

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

}
