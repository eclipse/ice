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
package org.eclipse.ice.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * This class allows for local file and directory browsing on a remote host
 * 
 * @author Joe Osborn
 *
 */
public class LocalFileBrowser extends SimpleFileVisitor<Path> implements FileBrowser {

	/**
	 * Default constructor
	 */
	public LocalFileBrowser() {
		// Make sure we are starting with a fresh file/directory list
		fileList.clear();
		directoryList.clear();
	}
	
	/**
	 * Default constructor with a given path to fill the ArrayLists
	 */
	public LocalFileBrowser(final String topDirectory) {
		// Make sure we are starting with a fresh file/directory list
		fileList.clear();
		directoryList.clear();
		// Execute the file walking logic, which fills the arrays
		walkTree(topDirectory);
		
	}

	/**
	 * Add the file path to the array list fileList 
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		// Add the file, depending on it's attribute
		if (!attr.isDirectory()) {
			fileList.add(file.toString());
		}

		return FileVisitResult.CONTINUE;
	}

	/**
	 *  Add the directory path to the array list directoryList
	 */
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		directoryList.add(dir.toString());
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Function that lets the user know if there is some error accessing this file
	 * Still add it to the list, though.
	 */
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		logger.error("Couldn't access path at: " + file.toString() + ". Adding to the list as error.", exc);
		fileList.add(file.toString());
		return FileVisitResult.CONTINUE;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileBrowser#getFileList()}
	 * 
	 * @return - ArrayList<String> of files
	 */
	@Override
	public ArrayList<String> getFileList() {
		return fileList;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileBrowser#getDirectoryList()}
	 * 
	 * @return - ArrayList<String> of directories
	 */
	@Override
	public ArrayList<String> getDirectoryList() {
		return directoryList;
	}

	/**
	 * This function performs the action of walking the file tree for the file
	 * browsing capabilities of LocalFileHandler. It returns a LocalFileWalker so
	 * that the files or directories could be obtained
	 * 
	 * @param topDirectory - top most directory to recursively walk through
	 */
	private void walkTree(final String topDirectory) {

		// Make a path variable of the topDirectory
		Path topPath = Paths.get(topDirectory);

		// Make a local file walker instance, which contains the logic of what to do
		// with the results from Files.walkFileTree

		try {
			// Make a dummy path that just gets the return top directory from
			// Files.walkFileTree
			Path path = Files.walkFileTree(topPath, this);
		} catch (IOException e) {
			logger.error("Unable to walk file tree at path " + topPath.toString(), e);
		}

		return;
	}

}
