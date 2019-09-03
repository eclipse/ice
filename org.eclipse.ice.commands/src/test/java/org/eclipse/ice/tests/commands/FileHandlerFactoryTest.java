/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.tests.commands;

import java.io.IOException;

import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.FileHandler;
import org.eclipse.ice.commands.FileHandlerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class implements several test methods for {@link org.eclipse.ice.commands.FileHandlerFactory}
 * @author Joe Osborn
 *
 */
public class FileHandlerFactoryTest {

	/**
	 * A default factory with which to create FileHandler instances
	 */
	FileHandlerFactory factory = new FileHandlerFactory();
	
	/**
	 * A command factory test that is only useful for accessing some of its member functions
	 */
	CommandFactoryTest factorytest = new CommandFactoryTest();
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler(String, String, ConnectionConfiguration)}
	 * and local file transfers.
	 */
	@Test
	public void testLocalFileHandlerFactory() {
		FileHandler handler = null;
		String hostname = factorytest.getLocalHostname();
		
		ConnectionConfiguration localConnection = new ConnectionConfiguration(hostname);
		try {
			handler = factory.getFileHandler("some_source_file.txt","some_dest.txt",localConnection, localConnection);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		
		try {
			handler.copy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			handler.move();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//assert that files exist now
	}

}
