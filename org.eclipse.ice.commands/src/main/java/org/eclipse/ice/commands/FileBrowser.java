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

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface sets the structure for the FileWalker subclasses, which
 * contain the logic to browse file and directory tree structures on local and
 * remote systems.
 * 
 * @author Joe Osborn
 *
 */
public interface FileBrowser {

	/**
	 * Logger for handling event messages and other information.
	 */
	static final Logger logger = LoggerFactory.getLogger(LocalFileBrowser.class);

	/**
	 * An array list of files that are visited within the top directory
	 */
	ArrayList<String> fileList = new ArrayList<String>();

	/**
	 * An array list of directories that are visited within the top directory
	 */
	ArrayList<String> directoryList = new ArrayList<String>();

	/**
	 * Getter for the array list which contains the directory list paths
	 * 
	 * @return
	 */
	public ArrayList<String> getDirectoryList();

	/**
	 * Getter for the hashmap which contains the file list paths
	 * 
	 * @return
	 */
	public ArrayList<String> getFileList();

}
