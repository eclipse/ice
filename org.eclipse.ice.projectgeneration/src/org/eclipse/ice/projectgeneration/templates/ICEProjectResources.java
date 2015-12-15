/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.projectgeneration.templates;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ICEProjectResources extends ResourceBundle {

	@Override
	protected Object handleGetObject(String key) {
		if (key.equals("okKey")) return "Okay";
		if (key.equals("cancelKey")) return "Cancel";
		return null;
	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(keySet());
	}
	
	protected Set<String> handleKeySet() {
		return new HashSet<String>(Arrays.asList("okKey","cancelKey"));
	}
}
