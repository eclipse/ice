/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alexander J. McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.common;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.eclipse.ice.datastructures.form.FormTextContentDescriber;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class subclasses the RenameParticipant class from the Eclipse Language
 * Toolkit to provide a hook for correctly renaming ICE Item XML files.
 * 
 * @author Alex McCaskey
 *
 */
public class ICEItemRenameParticipant extends RenameParticipant {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ICEItemDeleteParticipant.class);

	/**
	 * Reference to the Item ID if the provided file is an ICE XML Item file.
	 */
	private int itemID;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
	 * initialize(java.lang.Object)
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
			try {
				// Get the ICE Form Text Content Describer
				FormTextContentDescriber describer = null;
				int isValid = ITextContentDescriber.INVALID;									

				for (FormTextContentDescriber desc : FormTextContentDescriber.getFormTextContentDescribers()) {
					// Check if this is an ICE XML File
					isValid = desc.describe(itemFile.getContents(), null);
					if (isValid == ITextContentDescriber.VALID) {
						describer = desc;
						break;
					}
				}

				// Make sure we got a valid describer
				if (describer == null) {
					return false;
				}
				
				// If so, get the ItemID
				itemID = describer.getItemID();
				return true;
				
			} catch (IOException | CoreException e1) {
				logger.error("Could not describe the provided file.", e1);
				return false;
			}

		}

		// Indicate we don't need to do anythin
		// with this element
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
	 * createChange(org.eclipse.core.runtime.IProgressMonitor)
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

		// Then use the client to rename the Item.
		if (client != null) {
			String newName = getArguments().getNewName();
			client.renameItem(itemID, newName.substring(0, newName.lastIndexOf(".")));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#getName()
	 */
	@Override
	public String getName() {
		return "ICE Item Rename Participant";
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
