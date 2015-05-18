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
package org.eclipse.ice.reactor;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfReader;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.*;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * <p>
 * The LWRComponentReader class creates and populates an LWRComponent or
 * LWRComposite tree from an HDF5 file. This class implements the IHdfReadable
 * interface. This takes any type of LWRComponent or LWRComposite and iterates
 * from the top of that particular tree down, so a user could read from HDF5 and
 * populate that list accordingly to any part of a Reactor or its delegated
 * classes that inherit from LWRComponent.
 * </p>
 * 
 * @author s4h
 */
public class LWRComponentReader implements IHdfReader {

	/**
	 * <p>
	 * A HashMap keyed on HDF5LWRTagType name storing unique LWRComponent
	 * instances.
	 * </p>
	 */
	private HashMap<HDF5LWRTagType, LWRComponent> lWRComponentInstanceMap;

	/**
	 * The Constructor.
	 */
	public LWRComponentReader() {

		// Create a new HashMap<HDF5LWRTagType, LWRComponent> for the
		// LWRComponent instances
		this.lWRComponentInstanceMap = new HashMap<HDF5LWRTagType, LWRComponent>();

		// Create and assign default instances to the map
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.CONTROL_BANK,
				new ControlBank());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.FUEL_ASSEMBLY,
				new FuelAssembly(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.GRID_LABEL_PROVIDER,
				new GridLabelProvider(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.INCORE_INSTRUMENT,
				new IncoreInstrument());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.LWRROD, new LWRRod());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.MATERIAL,
				new Material());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.PWREACTOR,
				new PressurizedWaterReactor(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.RING, new Ring());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY,
				new RodClusterAssembly(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.MATERIALBLOCK,
				new MaterialBlock());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.TUBE, new Tube());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.LWRCOMPONENT,
				new LWRComponent());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.LWRCOMPOSITE,
				new LWRComposite());
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.LWREACTOR,
				new LWReactor(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.BWREACTOR,
				new BWReactor(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.PWRASSEMBLY,
				new PWRAssembly(-1));
		this.lWRComponentInstanceMap.put(HDF5LWRTagType.LWRGRIDMANAGER,
				new LWRGridManager(-1));

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfReader#read(URI uri)
	 */
	public IHdfReadable read(URI uri) {

		// Open the file at the provided uri
		H5File h5File = HdfFileFactory.openH5File(uri);

		// If the file is null, then return false
		if (h5File == null) {
			return null;
		}
		// Get the root group from the file
		H5Group rootH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// If the root group is null, then return null
		if (rootH5Group == null) {
			return null;
		}
		// Get the first child group from the root group
		H5Group h5Group = HdfReaderFactory.getChildH5Group(rootH5Group, 0);

		// Read the group into the iHdfReadable
		IHdfReadable iHdfReadable = this.read(h5Group);

		// Close the file
		HdfFileFactory.closeH5File(h5File);

		// Return the iHdfReadable
		return iHdfReadable;

	}

	/**
	 * <p>
	 * Returns a clone of the LWRComponent instance corresponding to the
	 * provided HDF5LWRTagType enumeration literal.
	 * </p>
	 * 
	 * @param HDF5LWRTag
	 *            <p>
	 *            A HDF5LWRTagType enumeration literal.
	 *            </p>
	 * @return <p>
	 *         A clone of the LWRComponent instance corresponding to the
	 *         provided HDF5LWRTagType enumeration literal.
	 *         </p>
	 */
	private LWRComponent getLWRComponentInstance(HDF5LWRTagType HDF5LWRTag) {

		// Get the LWRComponent instance corresponding to the provided tag
		LWRComponent lWRComponent = (LWRComponent) lWRComponentInstanceMap.get(
				HDF5LWRTag).clone();

		// Return the instance
		return lWRComponent;
	}

	/**
	 * <p>
	 * Recursively reads an H5Group and its child H5Groups and Datasets into an
	 * IHdfReadable.
	 * </p>
	 * 
	 * @param h5Group
	 *            <p>
	 *            The H5Group to be read.
	 *            </p>
	 * @return <p>
	 *         An IHdfReadable that has been populated by the provided H5Group
	 *         and its child H5Groups and Datasets.
	 *         </p>
	 */
	private IHdfReadable read(H5Group h5Group) {

		IHdfReadable iHdfReadable = null;

		// Check for a null h5Group
		if (h5Group == null) {
			return null;
		}
		// Get the tag from the group
		HDF5LWRTagType HDF5LWRTag = HDF5LWRTagType.toType(HdfReaderFactory
				.readStringAttribute(h5Group, "HDF5LWRTag"));

		// If the tag does not exist, skip it
		if (HDF5LWRTag != null) {

			// Get an instance from the tag
			iHdfReadable = this.getLWRComponentInstance(HDF5LWRTag);

			// Read in the attributes
			iHdfReadable.readAttributes(h5Group);

			// Read in the datasets
			iHdfReadable.readDatasets(h5Group);

			// Get a list of child groups
			ArrayList<H5Group> childGroupList = HdfReaderFactory
					.getChildH5Groups(h5Group);

			// If the group has entries
			if (!(childGroupList.isEmpty())) {

				// Cycle over these entries
				for (H5Group group : childGroupList) {

					// Create and populate a child from the child group
					IHdfReadable child = this.read(group);

					// Read the child into the readable
					iHdfReadable.readChild(child);
				}

			}
		}
		// Return the top level readable
		return iHdfReadable;

	}
}