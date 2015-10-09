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
 *   Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.developer.moose.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * MooseNature represents an implementation of the IProjectNature that is used 
 * by projects that are MOOSE-based applications. It allows the addition of 
 * menu commands that should only pertain to MOOSE applications.
 * 
 * @author Alex McCaskey
 *
 */
public class MooseNature implements IProjectNature {

	/**
	 * Public ID 
	 */
	public static final String NATURE_ID = "org.eclipse.ice.client.widgets.moose.nature.moosenature";
	
	/**
	 * Reference to the Project Space
	 */
	private IProject project;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	@Override
	public void configure() throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	@Override
	public void deconfigure() throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	@Override
	public IProject getProject() {
		return project;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	@Override
	public void setProject(IProject proj) {
		project = proj;
	}

}
