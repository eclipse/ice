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

/**
 * <p>
 * This interface provides a registry for {@link IHdfIOFactory} instances
 * provided as OSGi Declarative Services. This interface can similarly be
 * referenced via OSGi.
 * </p>
 * <p>
 * The purpose of this class is to enable other bundles to create their own
 * factories for writing to/reading from HDF5 files. These other bundles
 * register their factories with the registry so that UI bundles can request
 * said factories to write or read supported objects.
 * </p>
 * <p>
 * See {@link HdfIORegistry} for a basic implementation of this interface.
 * </p>
 * 
 * @author djg
 * 
 */
public interface IHdfIORegistry {

	/**
	 * Registers an {@link IHdfIOFactory} with the registry. Only non-null
	 * factories can be registered. If two factories share the same supported
	 * classes, only the most recently registered factory will be associated
	 * with those classes.
	 * 
	 * @param factory
	 *            The IHdfIOFactory to add to the registry.
	 */
	public void registerHdfIOFactory(IHdfIOFactory factory);

	/**
	 * <p>
	 * Gets an {@link IHdfIOFactory} from the registry. This factory supports
	 * writing and reading for the specified class.
	 * </p>
	 * <p>
	 * <b>This method is typically used for <i>writing</i> an object.</b>
	 * </p>
	 * 
	 * 
	 * @param object
	 *            The object that needs to be written to or read from a file.
	 * @return An IHdfIOFactory for the object's type, or null if there is no
	 *         factory that supports the specified component.
	 */
	public IHdfIOFactory getHdfIOFactory(Object object);

	/**
	 * <p>
	 * Gets an {@link IHdfIOFactory} from the registry. Instead of using a class
	 * as a key, this uses a String tag read from a file.
	 * </p>
	 * <p>
	 * <b>This method is typically used for <i>reading</i> an object.</b>
	 * </p>
	 * 
	 * @param tag
	 *            The tag used to associated an object from a file with an
	 *            IHdfIOFactory.
	 * @return An IHdfIOFactory for the tag, or null if there is no factory that
	 *         supports the specified tag.
	 */
	public IHdfIOFactory getHdfIOFactory(String tag);

}
