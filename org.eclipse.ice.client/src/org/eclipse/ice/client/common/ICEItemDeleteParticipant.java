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
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class subclasses the DeleteParticipant class from the Eclipse Language
 * Toolkit to provide a hook for correctly deleting ICE Item XML files.
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

			// Get the ICE Form Text Content Describer
			FormTextContentDescriber describer = getFormTextContentDescriber("ICE Form");
			
			try {
				// Check if this is an ICE XML File
				int isValid = describer.describe(itemFile.getContents(), null);
				
				// If so, get the itemID and return true to 
				// indicate we want to participate in the deletion
				if (isValid == ITextContentDescriber.VALID) {
					itemID = describer.getItemID();
					return true;
				} else {
					return false;
				}
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

		// Then use the client to delete the Item.
		if (client != null) {
			client.deleteItem(itemID);
		}

		return null;
	}
	
	/**
	 * This utility method is used to return the correct ITextContentDescriber 
	 * so we can validate incoming IFiles being deleted as true ICE Item files. 
	 * 
	 * @param type
	 * @return
	 */
	private FormTextContentDescriber getFormTextContentDescriber(String type) {
		// Local Declarations
		FormTextContentDescriber describer = null;
		String id = "org.eclipse.core.contenttype.contentTypes";
		
		// Get the extension point
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(id);
		
		// If the point is available, get a reference to the correct ITextContentDescriber
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (IConfigurationElement e : elements) {
				if ("content-type".equals(e.getName()) && type.equals(e.getAttribute("name"))) {
					try {
						describer = (FormTextContentDescriber) e.createExecutableExtension("describer");
					} catch (CoreException e1) {
						e1.printStackTrace();
						logger.error("Could not get ITextContentDescriber " + type, e1);
					}
				}
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return describer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
	 * getName()
	 */
	@Override
	public String getName() {
		return "ICE Item Delete Participant";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
	 * checkConditions(org.eclipse.core.runtime.IProgressMonitor,
	 * org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
	 */
	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
			throws OperationCanceledException {
		return null;
	}
}
