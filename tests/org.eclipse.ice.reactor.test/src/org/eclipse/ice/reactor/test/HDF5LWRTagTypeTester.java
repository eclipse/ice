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
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.*;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.junit.Test;

/**
 * <p>
 * A class that tests the HDF5LWRTagType's methods.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class HDF5LWRTagTypeTester {
	/**
	 * <p>
	 * Checks the toString and toType operations on HDF5LWRTagType.
	 * </p>
	 * 
	 */
	@Test
	public void checkTyping() {

		// Local Declarations
		HDF5LWRTagType type;

		// Check the toString implementations of the HDF5 enum
		assertEquals("BWReactor", HDF5LWRTagType.BWREACTOR.toString());
		assertEquals("Control Bank", HDF5LWRTagType.CONTROL_BANK.toString());
		assertEquals("Fuel Assembly", HDF5LWRTagType.FUEL_ASSEMBLY.toString());
		assertEquals("Incore Instrument",
				HDF5LWRTagType.INCORE_INSTRUMENT.toString());
		assertEquals("LWRComponent", HDF5LWRTagType.LWRCOMPONENT.toString());
		assertEquals("LWRComposite", HDF5LWRTagType.LWRCOMPOSITE.toString());
		assertEquals("LWReactor", HDF5LWRTagType.LWREACTOR.toString());
		assertEquals("LWRRod", HDF5LWRTagType.LWRROD.toString());
		assertEquals("Material", HDF5LWRTagType.MATERIAL.toString());
		assertEquals("PWRAssembly", HDF5LWRTagType.PWRASSEMBLY.toString());
		assertEquals("PWReactor", HDF5LWRTagType.PWREACTOR.toString());
		assertEquals("Ring", HDF5LWRTagType.RING.toString());
		assertEquals("Rod Cluster Assembly",
				HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY.toString());
		assertEquals("Tube", HDF5LWRTagType.TUBE.toString());
		assertEquals("Grid Label Provider",
				HDF5LWRTagType.GRID_LABEL_PROVIDER.toString());
		assertEquals("LWRGridManager", HDF5LWRTagType.LWRGRIDMANAGER.toString());
		assertEquals("MaterialBlock", HDF5LWRTagType.MATERIALBLOCK.toString());

		// Check the toType implementations of the HDf5 enum

		// Specify the type
		type = HDF5LWRTagType.BWREACTOR;
		// Check the type
		assertEquals(type.toType("BWReactor"), HDF5LWRTagType.BWREACTOR);

		// Specify the type
		type = HDF5LWRTagType.CONTROL_BANK;
		// Check the type
		assertEquals(type.toType("Control Bank"), HDF5LWRTagType.CONTROL_BANK);

		// Specify the type
		type = HDF5LWRTagType.FUEL_ASSEMBLY;
		// Check the type
		assertEquals(type.toType("Fuel Assembly"), HDF5LWRTagType.FUEL_ASSEMBLY);

		// Specify the type
		type = HDF5LWRTagType.INCORE_INSTRUMENT;
		// Check the type
		assertEquals(type.toType("Incore Instrument"),
				HDF5LWRTagType.INCORE_INSTRUMENT);

		// Specify the type
		type = HDF5LWRTagType.LWRCOMPONENT;
		// Check the type
		assertEquals(type.toType("LWRComponent"), HDF5LWRTagType.LWRCOMPONENT);

		// Specify the type
		type = HDF5LWRTagType.LWRCOMPOSITE;
		// Check the type
		assertEquals(type.toType("LWRComposite"), HDF5LWRTagType.LWRCOMPOSITE);

		// Specify the type
		type = HDF5LWRTagType.LWREACTOR;
		// Check the type
		assertEquals(type.toType("LWReactor"), HDF5LWRTagType.LWREACTOR);

		// Specify the type
		type = HDF5LWRTagType.LWRROD;
		// Check the type
		assertEquals(type.toType("LWRRod"), HDF5LWRTagType.LWRROD);

		// Specify the type
		type = HDF5LWRTagType.MATERIAL;
		// Check the type
		assertEquals(type.toType("Material"), HDF5LWRTagType.MATERIAL);

		// Specify the type
		type = HDF5LWRTagType.PWRASSEMBLY;
		// Check the type
		assertEquals(type.toType("PWRAssembly"), HDF5LWRTagType.PWRASSEMBLY);

		// Specify the type
		type = HDF5LWRTagType.PWREACTOR;
		// Check the type
		assertEquals(type.toType("PWReactor"), HDF5LWRTagType.PWREACTOR);

		// Specify the type
		type = HDF5LWRTagType.RING;
		// Check the type
		assertEquals(type.toType("Ring"), HDF5LWRTagType.RING);

		// Specify the type
		type = HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY;
		// Check the type
		assertEquals(type.toType("Rod Cluster Assembly"),
				HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY);

		// Specify the type
		type = HDF5LWRTagType.TUBE;
		// Check the type
		assertEquals(type.toType("Tube"), HDF5LWRTagType.TUBE);

		// Specify the type
		type = HDF5LWRTagType.GRID_LABEL_PROVIDER;
		// Check the type
		assertEquals(type.toType("Grid Label Provider"),
				HDF5LWRTagType.GRID_LABEL_PROVIDER);

		// Specify the type
		type = HDF5LWRTagType.LWRGRIDMANAGER;
		// Check the type
		assertEquals(type.toType("LWRGridManager"),
				HDF5LWRTagType.LWRGRIDMANAGER);

		// Specify the type
		type = HDF5LWRTagType.MATERIALBLOCK;
		// Check the type
		assertEquals(type.toType("MaterialBlock"), HDF5LWRTagType.MATERIALBLOCK);

		// Try to return a type that does not exist
		assertEquals(type.toType("asdasd1"), null);

	}
}