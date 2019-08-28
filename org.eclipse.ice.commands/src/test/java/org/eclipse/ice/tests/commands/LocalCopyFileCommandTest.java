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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ice.commands.LocalCopyFileCommand;
import org.junit.Test;


/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * @author Joe Osborn 
 *
 */
public class LocalCopyFileCommandTest {


	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCopyFileCommand#LocalCopyFileCommand(String, String)}
	 * 
	 */
	@Test
	public void testLocalCopyFileCommand() {
		
		// Set a dummy source and destination to copy

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
		source = sourcePath.toString();
		
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
		dest = destinationPath.toString();
		
		System.out.println("Copying: " + source + " to destination: " + dest);
		// Make the command
		LocalCopyFileCommand command = 
				new LocalCopyFileCommand(source, dest);
		
		// Check if the path exists now
		Path path = Paths.get(dest);
		assert(Files.exists(path));
		
	}
}
