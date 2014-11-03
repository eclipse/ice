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
package org.eclipse.ice.io.hdf;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides an implementation of {@link IHdfIORegistry}. To support
 * {@link IHdfIOFactory} lookups based on both objects and tag Strings
 * (presumably read from a file), this class uses a map from tags to object
 * classes and a map from object classes to IHdfIOFactories.
 * 
 * @author djg
 * 
 */
public class HdfIORegistry implements IHdfIORegistry {

	/**
	 * The map of tag Strings to object classes.
	 */
	private final Map<String, Class<?>> tagMap = new HashMap<String, Class<?>>();

	/**
	 * The map of object classes to IHdfIOFactories.
	 */
	private final Map<Class<?>, IHdfIOFactory> factoryMap = new HashMap<Class<?>, IHdfIOFactory>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.hdf.IHdfIORegistry#registerHdfIOFactory(org.eclipse
	 * .ice .io.hdf.IHdfIOFactory)
	 */
	public void registerHdfIOFactory(IHdfIOFactory factory) {

		if (factory != null) {
			for (Class<?> supportedClass : factory.getSupportedClasses()) {
				String tag = factory.getTag(supportedClass);

				// Put all tag/class pairs into tagMap.
				// Put all class/factory pairs into factoryMap.
				if (tag != null && supportedClass != null) {
					tagMap.put(tag, supportedClass);
					factoryMap.put(supportedClass, factory);
				}
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.hdf.IHdfIORegistry#getHdfIOFactory(java.lang.Object)
	 */
	public IHdfIOFactory getHdfIOFactory(Object object) {
		return (object != null ? factoryMap.get(object.getClass()) : null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.hdf.IHdfIORegistry#getHdfIOFactory(java.lang.String)
	 */
	public IHdfIOFactory getHdfIOFactory(String tag) {
		return factoryMap.get(tagMap.get(tag));
	}

}
