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

import org.eclipse.ice.commands.LocalMoveFileCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalMoveFileCommand}.
 * @author Joe Osborn
 *
 */
public class LocalMoveFileCommandTest {

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
	 * the testing of the class {@link org.eclipse.ice.commands.LocalMoveFileCommand#LocalMoveFileCommand(String, String)}
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
		
		// Need to get the filename individually
		String delims = "[/]";
		String[] tokens = source.split(delims);
		String filename = tokens[tokens.length-1];
		
		// Make the destination path + the filename
		String fullDestination = dest + "/" + filename;
		

		// Get the paths
		Path destFile = Paths.get(fullDestination);
		Path destDir = Paths.get(dest);
		
		
		// Delete the files
		// Only need to delete the moved file and destination directory,
		// since the source file was created in the default system directory
		// which can't be deleted and the moved file, by definition, doesn't
		// exist in the original directory
		try {
			Files.deleteIfExists(destFile);
		}
		catch (NoSuchFileException e) {
		    System.err.format("%s: no such" + " file or directory%n", destFile);
		    e.printStackTrace();
		} 
		catch (DirectoryNotEmptyException e) {
		    System.err.format("%s not empty%n", destFile);
		    e.printStackTrace();
		} 
		catch (IOException e) {
		    System.err.println(e);
		    e.printStackTrace();
		}
		
		try {
			Files.deleteIfExists(destDir);
		}
		catch (NoSuchFileException e) {
		    System.err.format("%s: no such" + " file or directory%n", destDir);
		    e.printStackTrace();
		} 
		catch (DirectoryNotEmptyException e) {
		    System.err.format("%s not empty%n", destDir);
		    e.printStackTrace();
		} 
		catch (IOException e) {
		    System.err.println(e);
		    e.printStackTrace();
		}
	
		
	}
	
	
	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalMoveFileCommand()}
	 */
	@Test
	public void testLocalMoveFileCommand() {
	
		System.out.println("Moving: " + source + " to destination: " + dest);
		// Make the command
		LocalMoveFileCommand command = 
				new LocalMoveFileCommand(source, dest);		
		command.execute();
		
		// Check if the path exists now
		Path path = Paths.get(dest);
		assert(Files.exists(path));
				
	}

}
