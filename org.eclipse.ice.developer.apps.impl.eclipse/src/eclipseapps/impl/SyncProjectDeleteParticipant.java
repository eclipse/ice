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
package eclipseapps.impl;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apps.AppsFactory;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.IEnvironment;
import eclipseapps.EclipseappsFactory;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class SyncProjectDeleteParticipant extends DeleteParticipant {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SyncProjectDeleteParticipant.class);

	private IEnvironment environmentToDelete;
	private EnvironmentManager manager;
	
	public SyncProjectDeleteParticipant() {
		super();
		manager = AppsFactory.eINSTANCE.createEnvironmentManager();
		manager.setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
	 * initialize(java.lang.Object)
	 */
	@Override
	protected boolean initialize(Object element) {
		
		IProject project = null;
		
		if (element instanceof IProject) {
			project = (IProject) element;
		}
		
		if (project != null) {
			manager.loadEnvironments();
			for (String envName : manager.list()) {
				IEnvironment env = manager.get(envName);
				if (env.getPrimaryApp().getName().equals(project.getName())) {
					// We are deleting this project...
					System.out.println("Found Environment - " + env.getName());
					environmentToDelete = env;
					return true;
				}
			}
		}
		
		// Indicate we don't need to do anything
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
		if (environmentToDelete != null) {
			if (environmentToDelete.getState() == EnvironmentState.RUNNING) {
				environmentToDelete.stop();
			}
			environmentToDelete.delete();
//			manager.deleteEnvironment(environmentToDelete.getName());
		}
		return null;
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
		return "ICE App Store Delete Participant";
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
