/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.item.test;

import java.util.HashMap;

import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.ITemplatedReader;
import org.eclipse.ice.io.serializable.ITemplatedWriter;
import org.eclipse.ice.io.serializable.IWriter;

/**
 * This is a FakeIOService used for testing. It is a copy of the IOService class
 * that does not pull readers and writers from the service interfaces and does
 * not use the logger.
 * 
 * @author Jay Jay Billings
 *
 */
public class FakeIOService implements IIOService {
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
	public FakeIOService() {
		// Initialize our containers
		readerMap = new HashMap<String, IReader>();
		templatedReaderMap = new HashMap<String, ITemplatedReader>();
		writerMap = new HashMap<String, IWriter>();
		templatedWriterMap = new HashMap<String, ITemplatedWriter>();

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#addReader(org.eclipse.ice.io.
	 * serializable.IReader)
	 */
	@Override
	public void addReader(IReader reader) {

		if (reader != null) {
			readerMap.put(reader.getReaderType(), reader);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#addTemplatedReader(org.eclipse
	 * .ice.io.serializable.ITemplatedReader)
	 */
	@Override
	public void addTemplatedReader(ITemplatedReader templatedReader) {

		if (templatedReader != null) {
			templatedReaderMap.put(templatedReader.getReaderType(),
					templatedReader);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#addWriter(org.eclipse.ice.io.
	 * serializable.IWriter)
	 */
	@Override
	public void addWriter(IWriter writer) {

		if (writer != null) {
			writerMap.put(writer.getWriterType(), writer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#addTemplatedWriter(org.eclipse
	 * .ice.io.serializable.ITemplatedWriter)
	 */
	@Override
	public void addTemplatedWriter(ITemplatedWriter writer) {

		if (writer != null) {
			templatedWriterMap.put(writer.getWriterType(), writer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#getReader(java.lang.String)
	 */
	@Override
	public IReader getReader(String readerType) {

		// Make sure we have this Reader and if so return it
		if (readerMap.containsKey(readerType)) {
			return readerMap.get(readerType);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#getTemplatedReader(java.lang.
	 * String)
	 */
	@Override
	public ITemplatedReader getTemplatedReader(String readerType) {

		// Make sure we have this Reader and if so return it
		if (templatedReaderMap.containsKey(readerType)) {
			return templatedReaderMap.get(readerType);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#getWriter(java.lang.String)
	 */
	@Override
	public IWriter getWriter(String writerType) {

		// Make sure we have this Writer and if so return it
		if (writerMap.containsKey(writerType)) {
			return writerMap.get(writerType);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.io.serializable.IIOService#getTemplatedWriter(java.lang.
	 * String)
	 */
	@Override
	public ITemplatedWriter getTemplatedWriter(String writerType) {

		// Make sure we have this Writer and if so return it
		if (templatedWriterMap.containsKey(writerType)) {
			return templatedWriterMap.get(writerType);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IIOService#getNumberOfReaders()
	 */
	@Override
	public int getNumberOfReaders() {
		return readerMap.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.io.serializable.IIOService#getNumberOfWriters()
	 */
	@Override
	public int getNumberOfWriters() {
		return writerMap.size();
	}

}
