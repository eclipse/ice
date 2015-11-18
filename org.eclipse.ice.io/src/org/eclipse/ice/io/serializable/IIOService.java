/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Alexander J. McCaskey
 *******************************************************************************/
package org.eclipse.ice.io.serializable;

/**
 * This is a service container for all IReaders and IWriters exposed to the
 * underlying OSGi framework as a Declarative Service. For each interface
 * realization it receives, it loads it into an appropriate mapping that can be
 * queried later by clients of those IO services.
 * 
 * @author Alex McCaskey, Jay Jay Billings
 *
 */
public interface IIOService {

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * IReaders exposed as a Declarative Service.
	 * 
	 * @param reader
	 *            The IReader realization exposed as a Declarative Service.
	 */
	void addReader(IReader reader);

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * ITemplatedReaders exposed as a Declarative Service.
	 * 
	 * @param templatedReader
	 *            The ITemplatedReader realization exposed as a Declarative
	 *            Service.
	 */
	void addTemplatedReader(ITemplatedReader templatedReader);

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * IWriters exposed as a Declarative Service.
	 * 
	 * @param writer
	 *            The IWriter realization exposed as a Declarative Service.
	 */
	void addWriter(IWriter writer);

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * ITemplatedWriters exposed as a Declarative Service.
	 * 
	 * @param writer
	 *            The ITemplatedWriter realization exposed as a Declarative
	 *            Service.
	 */
	void addTemplatedWriter(ITemplatedWriter writer);

	/**
	 * Return the IReader realization of type readerType.
	 * 
	 * @param readerType
	 *            The IReader type to return.
	 * @return IReader realization
	 */
	IReader getReader(String readerType);

	/**
	 * Return the ITemplatedReader realization of type readerType.
	 * 
	 * @param readerType
	 *            The ITemplatedReader type to return.
	 * @return ITemplatedReader realization
	 */
	ITemplatedReader getTemplatedReader(String readerType);

	/**
	 * Return the IWriter realization of type writerType.
	 * 
	 * @param writerType
	 *            The IWriter type to return.
	 * @return IWriter realization
	 */
	IWriter getWriter(String writerType);

	/**
	 * Return the ITemplatedWriter realization of type writerType.
	 * 
	 * @param writerType
	 *            The ITemplatedWriter type to return.
	 * @return ITemplatedWriter realization
	 */
	ITemplatedWriter getTemplatedWriter(String writerType);

	/**
	 * Return the total number of stored IReader realizations.
	 * 
	 * @return The number of IReaders.
	 */
	int getNumberOfReaders();

	/**
	 * Return the total number of stored IWriter realizations.
	 * 
	 * @return The number of IWriters.
	 */
	int getNumberOfWriters();

}