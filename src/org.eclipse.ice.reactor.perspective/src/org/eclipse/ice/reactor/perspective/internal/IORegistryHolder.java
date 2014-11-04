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
package org.eclipse.ice.reactor.perspective.internal;

import org.eclipse.ice.io.hdf.IHdfIORegistry;

/**
 * This class grabs the currently available {@link IHdfIORegistry}
 * implementation from the OSGi Declarative Services. This enables classes in
 * this bundle to fetch IO factories for writing and reading reactor files.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class IORegistryHolder {

	/**
	 * The currently available {@link IHdfIORegistry} implementation, which runs
	 * via OSGi DS.
	 */
	private static IHdfIORegistry registry;

	/**
	 * Gets the currently available {@link IHdfIORegistry} implementation, which
	 * runs via OSGi DS.
	 * 
	 * @return ICE's current IHdfIORegistry implementation, or null if the
	 *         service is not running.
	 */
	public static IHdfIORegistry getRegistry() {
		return registry;
	}

	/**
	 * Sets the currently available {@link IHdfIORegistry} implementation, which
	 * runs via OSGi DS. <b>This method should only be called via the OSGi
	 * framework.</b>
	 * 
	 * @param registry
	 *            The registry implementation.
	 */
	public static void setHdfIORegistry(IHdfIORegistry registry) {

		if (registry != null) {
			IORegistryHolder.registry = registry;
			System.out.println("ICE Reactor Perspective Message: "
					+ "IO registry set successfully!");
		} else {
			System.out.println("ICE Reactor Perspective Message:"
					+ "Framework attempted to set IO registry, but the "
					+ "reference was null.");
		}

		return;
	}

	/**
	 * Unsets the currently available {@link IHdfIORegistry} implementation,
	 * which runs via OSGi DS. <b>This method should only be called via the OSGi
	 * framework.</b>
	 * 
	 * @param registry
	 *            The registry implementation.
	 */
	public static void unsetHdfIORegistry(IHdfIORegistry registry) {
		if (registry == IORegistryHolder.registry) {
			IORegistryHolder.registry = null;
		}
	}
}
