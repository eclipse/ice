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
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joe Osborn
 *
 */
public class LocalFileWalker extends SimpleFileVisitor<Path> {


	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(LocalFileWalker.class);
	
	/**
	 * An array list of files that are visited within the top directory
	 */
	private ArrayList<String> fileList = new ArrayList<String>();

	/**
	 * An array list of directories that are visited within the top directory
	 */
	private ArrayList<String> directoryList = new ArrayList<String>();

	/**
	 * Default constructor
	 */
	public LocalFileWalker() {
	}

	// Add the file path tp fileList
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		// Add the file, depending on it's attribute
		if(!attr.isDirectory()) {
			fileList.add(file.toString());
		} 
		
		return FileVisitResult.CONTINUE;
	}

	// Add the directory path to the array list directoryList
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
	 * Getter for the hashmap which contains the file list paths
	 * @return
	 */
	public ArrayList<String> getFileList() {
		return fileList;
	}
	/**
	 * Getter for the array list which contains the directory list paths
	 * @return
	 */
	public ArrayList<String> getDirectoryList(){
		return directoryList;
	}
	
	
}
