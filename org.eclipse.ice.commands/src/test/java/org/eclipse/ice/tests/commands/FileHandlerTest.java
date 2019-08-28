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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.ice.commands.FileHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.FileHandler}.
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class FileHandlerTest {

	String localSource = null;
	String localDestination = null;
	
	/**
	 * Set up some dummy local files to work with
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		// First create a dummy text file to test 
		String source = "dummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, source);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
				
		// Turn the path into a string to pass to the command
		localSource = sourcePath.toString();
				
		// Do the same for the destination
		Path destinationPath = null;
		String dest = "testCopyDirectory";
		try {
			destinationPath = Files.createTempDirectory(dest);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
				
		// Turn the path into a string to give to the command
		localDestination = destinationPath.toString();
		
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.FileHandler#FileHandler()}.
	 */
	//@Test
	public void testFileHandler() {
		fail("Not yet implemented");
	}
	
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.FileHandler#copy(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLocalCopy() {
		System.out.println("Testing testLocalCopy() function.");
		try {
			FileHandler.copy(localSource, localDestination+"copy");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Check that it exists
		try {
			FileHandler.exists(localDestination);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished testing testLocalCopy() function.");
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.FileHandler#move(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLocalMove() {
		System.out.println("Testing testLocalMove() function.");
		try {
			FileHandler.move(localSource, localDestination);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Check that it exists
		try { 
			FileHandler.exists(localDestination);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished testing testLocalMove() function.");
		
	}



	/**
	 * Test method for {@link org.eclipse.ice.commands.FileHandler#exists(java.lang.String)}.
	 */
	@Test
	public void testExists() {
		System.out.println("Testing testExists() function.");
		
		/**
		 * Test a file that everyone should have existing
		 */
		try {
			assert(FileHandler.exists("/usr/lib/python2.7"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Testing testExists() with a non existing file");
		try {
			assert(!FileHandler.exists("/usr/file_that_doesnot_exist.txt"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished testing testExists()");
	}

	
	
	
}
