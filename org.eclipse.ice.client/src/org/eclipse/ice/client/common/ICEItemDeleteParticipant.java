/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Alexander J. McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class subclasses the DeleteParticipant class from 
 * the Eclipse Language Toolkit to provide a hook for 
 * correctly deleting ICE Item XML files. 
 * 
 * @author Alex McCaskey
 *
 */
public class ICEItemDeleteParticipant extends DeleteParticipant {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ICEItemDeleteParticipant.class);
	
	/**
	 * Reference to the Item ID if the provided 
	 * file is an ICE XML Item file. 
	 */
	private int itemID;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#initialize(java.lang.Object)
	 */
	@Override
	protected boolean initialize(Object element) {
		// Local Declarations
		IFile itemFile = null;
		
		// See if we can cast to an IFile, 
		// if not we do nothing
		if (element instanceof IFile) {
			itemFile = (IFile) element;
		}
		
		// If this is an IFile
		if (itemFile != null) {
			
			// Check that its an XML File first
			if ("xml".equals(itemFile.getFileExtension())) {
				
				// Now make sure this is an ICE XML File
				// by reading the file and searching for 
				// ICE-specific stuff
				try {
					
					// Create a reader for the file
					BufferedReader reader = new BufferedReader(new InputStreamReader(itemFile.getContents()));
					
					// Get the first 4 lines of its content
					String firstLines = "", nextLine;
					int counter = 0;
					while (((nextLine = reader.readLine()) != null) && counter < 3) {
						firstLines += nextLine;
						counter++; 
					}
					
					// Check the lines for ICE stuff
					if (firstLines.contains("<?xml version=")) {
						if (firstLines.contains("itemType=") && firstLines.contains("builderName=")
								&& firstLines.contains("<Form")) {
							
							// Now we know this is an ICE XML Item, so 
							// get a reference to its ID and return true 
							// to indicate we will be a delete participant
							int index = firstLines.indexOf("itemID=\"");
							int endIndex = firstLines.indexOf("\"", index+8);
							String itemIdString = firstLines.substring(index, endIndex+1).replace("\"", "");
							itemID = Integer.valueOf(itemIdString.split("=")[1]);
							return true;
						} else {
							// Not an ICE XML Item, return false
							return false;
						}
					}
					
				} catch (CoreException | IOException e) {
					logger.error("Could not read the XML file, returning false and not participating in this deletion.", e);
					return false;
				}
				
				return true;
			}
			
		} 

		// Indicate we don't need to do anythin 
		// with this element
		return false;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#createChange(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		
		// If we've made it here, we have a valid ICE XML Item 
		// to delete and we've collected its Item ID. 
		// So grab the client...
		IClient client = null;
		try {
			client = IClient.getClient();
		} catch (CoreException e) {
			logger.error("Could not get a reference to the client.", e);
		}
		
		// Then use the client to delete the Item.
		if (client != null) {
			client.deleteItem(itemID);
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#getName()
	 */
	@Override
	public String getName() {
		return "Delete ICE XML File Participant";
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#checkConditions(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
	 */
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
			throws OperationCanceledException {
		return null;
	}
}
