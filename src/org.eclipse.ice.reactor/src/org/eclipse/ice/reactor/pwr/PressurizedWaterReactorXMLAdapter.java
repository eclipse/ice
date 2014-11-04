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
package org.eclipse.ice.reactor.pwr;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This is a custom XMLAdapter that marshalls PressurizedWaterReactors to a
 * string that indicates they are unsupported and unmarshalls them to an empty
 * object.
 * 
 * PressurizedWaterReactors are very large classes that represent information
 * already stored in an HDF5 file and shouldn't be duplicated.
 * 
 * @author Jay Jay Billings
 * 
 */
public class PressurizedWaterReactorXMLAdapter extends
		XmlAdapter<String, PressurizedWaterReactor> {

	@Override
	public PressurizedWaterReactor unmarshal(String v) throws Exception {
		// Just return a PWR with 15x15 assemblies
		return new PressurizedWaterReactor(15);
	}

	@Override
	public String marshal(PressurizedWaterReactor v) throws Exception {
		// Just return a string noting the lack of support.
		return "PWRs are not stored in XML. Please check the HDF5 file.";
	}

}
