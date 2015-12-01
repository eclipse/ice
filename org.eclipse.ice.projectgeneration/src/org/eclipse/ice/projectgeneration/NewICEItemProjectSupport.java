/*******************************************************************************
* Copyright (c) 2013, 2014 UT-Battelle, LLC.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Initial API and implementation and/or initial documentation - Jay Jay Billings,
*   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
*   Claire Saunders, Matthew Wang, Anna Wojtowicz
*******************************************************************************/
package org.eclipse.ice.projectgeneration;

import java.net.URI;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author arbennett
 */
public class NewICEItemProjectSupport {

	// Allocate a logger
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// Booleans set by the wizard to determine which files should be generated
	private static boolean modelBuilderNeeded;
	private static boolean jobLauncherNeeded;
	private static boolean ioClassesNeeded;
	
	/**
	 * Set up a new New ICE Item Project.
	 * 
	 * @param projectName
	 * @param location
	 * @return the new New ICE Item Project
	 */
	public static IProject createProject(String projectName, URI location) {
		if (projectName == null || projectName.trim().length() <= 0) {
			return null;
		}
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		
		// Set up the base project
		IProject project = workspaceRoot.getProject(projectName);
		if (!project.exists()) {
			IProjectDescription description = 
					project.getWorkspace().newProjectDescription(project.getName());
			if (location != null && workspaceRoot.getLocationURI().equals(location)) {
				location = null;
			}
			description.setLocationURI(location);
			try {
				project.create(description, new NullProgressMonitor());
				if (!project.isOpen()) {
					project.open(new NullProgressMonitor());
				}
			} catch (CoreException e) {
				//TODO: Replace this with a logger
				e.printStackTrace();
			}
		}
		
		// Add the ICE Item project nature
		try {
			setNature(project);
		} catch (CoreException e) {
			// TODO: Replace this with a logger
			e.printStackTrace();
		}
		
		//TODO: Create model builder if specified
		if (modelBuilderNeeded) {
			createModelBuilder();
		}
		
		//TODO: Create job launcher if specified
		if (jobLauncherNeeded) {
			createJobLauncher();
		}
		
		//TODO: Create readers/writers if specified
		if (ioClassesNeeded) {
			createIOClasses();
		}
		
		//TODO: Configure manifest, classpath, 
		//      bundle dependencies, and extensions
		setupPluginInfrastructure();
		
		return project;
	}

	
	/**
	 * Make sure that the project has the ICEItemNature associated with it.
	 * 
	 * @param project
	 */
	private static void setNature(IProject project) throws CoreException {
		if (!project.hasNature(ICEItemNature.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] projNatures = description.getNatureIds();
			projNatures = Arrays.copyOf(projNatures, projNatures.length+1);
			projNatures[projNatures.length - 1] = ICEItemNature.NATURE_ID;
			description.setNatureIds(projNatures);
			project.setDescription(description, new NullProgressMonitor());
		}
	}
	
	
	/**
	 * TODO: Create Java classes for model and model builder which 
	 * 		 extend Item and AbstractModelBuilder respectively
	 */
	private static void createModelBuilder() {
		return;
	}
	
	/**
	 * TODO: Create Java classes for job launcher and launcher builder which
	 * 		 extend Item and AbstractModelBuilder respectively
	 */
	private static void createJobLauncher() {
		return;
	}
	
	/**
	 * TODO: Create the parser generator item
	 */
	private static void createIOClasses() {
		return;
	}
	
	/**
	 * TODO: Configure the manifest, classpath, bundle deps, and extensions
	 */
	private static void setupPluginInfrastructure() {
		return;
	}
}
