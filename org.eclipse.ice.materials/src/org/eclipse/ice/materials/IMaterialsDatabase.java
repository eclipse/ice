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
package org.eclipse.ice.materials;

import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.ice.datastructures.form.Material;

/**
 * This is a simple interface for managing and dealing with a Materials
 * database. The "database" is a persistent collection of Materials data
 * collected from a number of sources.
 * 
 * Realizations of IMaterialDatabase are meant to be used as services but their
 * lifecycle details are not dictated by this interface.
 * 
 * FIXME! - Cite sources for Material information.
 * 
 * @author Jay Jay Billings
 * 
 */
public interface IMaterialsDatabase extends IElementSource<Material> {

	/**
	 * This operation returns all of the materials in the database.
	 * 
	 * @return A list of all the Materials in the database.
	 */
	public List<Material> getMaterials();

	/**
	 * This operation creates a new material in the database. If the new
	 * Material is already in the database, it updates the existing record.
	 * 
	 * @param material
	 *            the new Material
	 */
	public void addMaterial(Material material);

	/**
	 * This operation deletes a material from the database. If the material with
	 * the name is not found, it does nothing.
	 * 
	 * @param name
	 *            the name of the material
	 */
	public void deleteMaterial(String name);

	/**
	 * This operation deletes a material from the database.
	 * 
	 * @param material
	 *            the Material to remove
	 */
	public void deleteMaterial(Material material);

	/**
	 * This operation overwrites the information for a material in the database
	 * with information from another. If the new material does not exist in the
	 * database, it adds it.
	 * 
	 * @param material
	 *            The new Material information.
	 */
	public void updateMaterial(Material material);

	/**
	 * This operation restores the database to its initial configuration,
	 * destroying all new and updated material information in the process. It is
	 * irreversible.
	 */
	public void restoreDefaults();
}
