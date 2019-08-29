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
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ice.commands.LocalCopyFileCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * @author Joe Osborn 
 *
 */
public class LocalCopyFileCommandTest {

	/**
	 * A source file that is created for testing
	 */
	String source = null;
	
	/**
	 * A destination path that is created for testing 
	 */
	String dest = null;
	
	/**
	 * This function sets up and creates a dummy test file for 
	 * the testing of the class {@link org.eclipse.ice.commands.LocalCopyFileCommand#LocalCopyFileCommand(String, String)}
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Set a dummy source and destination to copy

		// First create a dummy text file to test 
		source = "dummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, source);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
				
		// Turn the path into a string to pass to the command
		source = sourcePath.toString();
				
		// Do the same for the destination
		Path destinationPath = null;
		dest = "testCopyDirectory";
		try {
			destinationPath = Files.createTempDirectory(dest);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
				
		// Turn the path into a string to give to the command
		dest = destinationPath.toString();
	}

	/**
	 * Deletes the temporarily made files since they are not useful
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
		// Get the individual directories
		String delims = "[/]";
		String[] tokens = source.split(delims);
		String filename = tokens[tokens.length-1];
		
		// Make the destination path + the filename
		String fullDestination = dest + "/" + filename;
		
		// Get the paths
		Path sourcePath = Paths.get(source);
		Path destPath = Paths.get(fullDestination);
		Path destDirectory = Paths.get(dest);
		
		// Delete the files
		try {
			Files.deleteIfExists(sourcePath);
		}
		catch (NoSuchFileException e) {
		    System.err.format("%s: no such" + " file or directory%n", sourcePath);
		    e.printStackTrace();
		} 
		catch (DirectoryNotEmptyException e) {
		    System.err.format("%s not empty%n", sourcePath);
		    e.printStackTrace();
		} 
		catch (IOException e) {
		    System.err.println(e);
		    e.printStackTrace();
		}
		try {
			Files.deleteIfExists(destPath);
		}
		catch (NoSuchFileException e) {
		    System.err.format("%s: no such" + " file or directory%n", destPath);
		    e.printStackTrace();
		} 
		catch (DirectoryNotEmptyException e) {
		    System.err.format("%s not empty%n", destPath);
		    e.printStackTrace();
		} 
		catch (IOException e) {
		    System.err.println(e);
		    e.printStackTrace();
		}
		try {
			Files.deleteIfExists(destDirectory);
		}
		catch (NoSuchFileException e) {
		    System.err.format("%s: no such" + " file or directory%n", destDirectory);
		    e.printStackTrace();
		} 
		catch (DirectoryNotEmptyException e) {
		    System.err.format("%s not empty%n", destDirectory);
		    e.printStackTrace();
		} 
		catch (IOException e) {
		    System.err.println(e);
		    e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCopyFileCommand#LocalCopyFileCommand(String, String)}
	 * 
	 */
	@Test
	public void testLocalCopyFileCommand() {
		
		
		
		System.out.println("Copying: " + source + " to destination: " + dest);
		// Make the command
		LocalCopyFileCommand command = 
				new LocalCopyFileCommand(source, dest);
		command.execute();
		// Check if the path exists now
		Path path = Paths.get(dest);
		assert(Files.exists(path));
		
	}
}
