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
package org.eclipse.ice.io.serializable;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class serves as a container for all IReaders and IWriters exposed to the
 * underlying OSGi framework as a Declarative Service. For each interface
 * realization it receives, it loads it into an appropriate mapping that can be
 * queried later by clients of those IO services.
 * 
 * @author Alex McCaskey
 *
 */
public class IOService {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(IOService.class);

	/**
	 * Reference to the mapping between IReader type Strings and the
	 * corresponding IReader.
	 */
	private HashMap<String, IReader> readerMap;

	/**
	 * Reference to the mapping between IReader type Strings and the
	 * corresponding IReader.
	 */
	private HashMap<String, ITemplatedReader> templatedReaderMap;

	/**
	 * Reference to the mapping between IWriter type Strings and the
	 * corresponding IWriter.
	 */
	private HashMap<String, IWriter> writerMap;

	/**
	 * Reference to the mapping between ITemplatedWriter type Strings and the
	 * corresponding ITemplatedWriter.
	 */
	private HashMap<String, ITemplatedWriter> templatedWriterMap;

	/**
	 * The constructor
	 */
	public IOService() {
		// Initialize our containers
		readerMap = new HashMap<String, IReader>();
		templatedReaderMap = new HashMap<String, ITemplatedReader>();
		writerMap = new HashMap<String, IWriter>();
		templatedWriterMap = new HashMap<String, ITemplatedWriter>();
		
		// Load all services available via extension points. 
		try {
			for (IReader r : IReader.getIReaders()) {
				addReader(r);
			}
			for (IWriter w : IWriter.getIWriters()) {
				addWriter(w);
			}
		} catch (CoreException e) {
			logger.error("Error adding IReaders and IWriters to Item's IOService.", e);
		}
	}

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * IReaders exposed as a Declarative Service.
	 * 
	 * @param reader
	 *            The IReader realization exposed as a Declarative Service.
	 */
	public void addReader(IReader reader) {

		if (reader != null) {
			logger.info("[IOService Message] Adding " + reader.getReaderType()
					+ " reader to the IOService Mapping.");
			readerMap.put(reader.getReaderType(), reader);
		}

		return;
	}

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * ITemplatedReaders exposed as a Declarative Service.
	 * 
	 * @param templatedReader
	 *            The ITemplatedReader realization exposed as a Declarative
	 *            Service.
	 */
	public void addTemplatedReader(ITemplatedReader templatedReader) {

		if (templatedReader != null) {
			logger.info("[IOService Message] Adding "
					+ templatedReader.getReaderType()
					+ " templated reader to the IOService Mapping.");
			templatedReaderMap.put(templatedReader.getReaderType(),
					templatedReader);
		}

		return;
	}

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * IWriters exposed as a Declarative Service.
	 * 
	 * @param writer
	 *            The IWriter realization exposed as a Declarative Service.
	 */
	public void addWriter(IWriter writer) {

		if (writer != null) {
			logger.info("[IOService Message] Adding " + writer.getWriterType()
					+ " writer to the IOService Mapping.");
			writerMap.put(writer.getWriterType(), writer);
		}
	}

	/**
	 * This method is used by the underlying OSGi framework to add available
	 * ITemplatedWriters exposed as a Declarative Service.
	 * 
	 * @param writer
	 *            The ITemplatedWriter realization exposed as a Declarative
	 *            Service.
	 */
	public void addTemplatedWriter(ITemplatedWriter writer) {

		if (writer != null) {
			logger.info("[IOService Message] Adding " + writer.getWriterType()
					+ " templated writer to the IOService Mapping.");
			templatedWriterMap.put(writer.getWriterType(), writer);
		}
	}

	/**
	 * Return the IReader realization of type readerType.
	 * 
	 * @param readerType
	 *            The IReader type to return.
	 * @return IReader realization
	 */
	public IReader getReader(String readerType) {

		// Make sure we have this Reader and if so return it
		if (readerMap.containsKey(readerType)) {
			return readerMap.get(readerType);
		}

		return null;
	}

	/**
	 * Return the ITemplatedReader realization of type readerType.
	 * 
	 * @param readerType
	 *            The ITemplatedReader type to return.
	 * @return ITemplatedReader realization
	 */
	public ITemplatedReader getTemplatedReader(String readerType) {

		// Make sure we have this Reader and if so return it
		if (templatedReaderMap.containsKey(readerType)) {
			return templatedReaderMap.get(readerType);
		}

		return null;
	}

	/**
	 * Return the IWriter realization of type writerType.
	 * 
	 * @param writerType
	 *            The IWriter type to return.
	 * @return IWriter realization
	 */
	public IWriter getWriter(String writerType) {

		// Make sure we have this Writer and if so return it
		if (writerMap.containsKey(writerType)) {
			return writerMap.get(writerType);
		}

		return null;
	}

	/**
	 * Return the ITemplatedWriter realization of type writerType.
	 * 
	 * @param writerType
	 *            The ITemplatedWriter type to return.
	 * @return ITemplatedWriter realization
	 */
	public ITemplatedWriter getTemplatedWriter(String writerType) {

		// Make sure we have this Writer and if so return it
		if (templatedWriterMap.containsKey(writerType)) {
			return templatedWriterMap.get(writerType);
		}

		return null;
	}

	/**
	 * Return the total number of stored IReader realizations.
	 * 
	 * @return The number of IReaders.
	 */
	public int getNumberOfReaders() {
		return readerMap.size();
	}

	/**
	 * Return the total number of stored IWriter realizations.
	 * 
	 * @return The number of IWriters.
	 */
	public int getNumberOfWriters() {
		return writerMap.size();
	}

}
