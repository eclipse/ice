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
package org.eclipse.ice.io.serializable.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.junit.Test;

/**
 * This class tests the IOService, specifically that it can take and return
 * valid IReaders and IWriters.
 * 
 * @author Alex McCaskey
 *
 */
public class IOServiceTester {

	/**
	 * Reference to the IOService to test
	 */
	private IOService service;

	/**
	 * This method checks that we can give and get valid IReaders and IWriters. 
	 */
	@Test
	public void checkAddGetReaderWriter() {

		// Create a new IOService to test
		service = new IOService();

		// Create a fake IReader realization
		IReader fakeReader = new IReader() {

			@Override
			public Form read(IFile file) {
				return new Form();
			}

			@Override
			public ArrayList<Entry> findAll(IFile file, String regex) {
				ArrayList<Entry> fakeEntry = new ArrayList<Entry>();
				return fakeEntry;
			}

			@Override
			public String getReaderType() {
				return "FakeReader";
			}

		};

		// Create a fake IWriter realization
		IWriter fakeWriter = new IWriter() {

			@Override
			public void write(Form formToWrite, IFile file) {
				return;
			}

			@Override
			public void replace(IFile file, String regex, String value) {
				return;
			}

			@Override
			public String getWriterType() {
				return "FakeWriter";
			}

		};

		// Check that maps were initialized correctly
		assertEquals(0, service.getNumberOfReaders());
		assertEquals(0, service.getNumberOfWriters());

		// Add the reader to the IOService
		service.addReader(fakeReader);

		// Make sure it was stored correctly
		assertNotNull(service.getReader("FakeReader"));
		assertTrue(fakeReader == service.getReader("FakeReader"));
		assertEquals(1, service.getNumberOfReaders());

		// Make sure adding a null IReader doesn't add anything
		service.addReader(null);
		assertEquals(1, service.getNumberOfReaders());

		// Test asking for a non-exisitant reader
		assertNull(service.getReader("SomeOtherReader"));

		// Now do the same for the IWriter
		service.addWriter(fakeWriter);

		assertNotNull(service.getWriter("FakeWriter"));
		assertTrue(fakeWriter == service.getWriter("FakeWriter"));
		assertEquals(1, service.getNumberOfWriters());

		// Make sure adding a null IWriter doesn't add anything
		service.addWriter(null);
		assertEquals(1, service.getNumberOfWriters());

		// Test asking for a non-existant writer
		assertNull(service.getWriter("SomeOtherWriter"));
	}

}
