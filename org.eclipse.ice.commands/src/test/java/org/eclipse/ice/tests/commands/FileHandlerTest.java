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

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.ice.commands.FileHandler;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.FileHandler}.
 * @author Jay Jay Billings
 *
 */
public class FileHandlerTest {



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
	public void testCopy() {
		System.out.println("Testing testCopy() function.");
		try {
			FileHandler.copy("/Users/4jo/testfile.txt", "/Users/4jo/dummy_dir2");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Finished testing testCopy() function.");
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.FileHandler#move(java.lang.String, java.lang.String)}.
	 */
	//@Test
	public void testMove() {
		try {
			FileHandler.move("/Users/4jo/dummy_dir/testfile.txt", "/Users/4jo/testfile_mv.txt");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
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
