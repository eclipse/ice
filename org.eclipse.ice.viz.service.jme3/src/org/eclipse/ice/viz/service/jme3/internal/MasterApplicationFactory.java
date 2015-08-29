/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.internal;

import org.eclipse.ice.viz.service.jme3.application.MasterApplication;

/**
 * This class provides an implementation for {@link IMasterApplicationFactory}.
 * 
 * @author Jordan
 * 
 */
public class MasterApplicationFactory implements IMasterApplicationFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.internal.IMasterApplicationFactory
	 * #createApplication()
	 */
	@Override
	public MasterApplication createApplication() {
		// We use a static method here because there are private variables that
		// must be properly initialized inside MasterApplication. We should not
		// have to expose them to sub-classes or package classes.
		return MasterApplication.createApplication();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.internal.IMasterApplicationFactory
	 * #disposeApplication(org.eclipse.ice.client.widgets.jme.MasterApplication)
	 */
	@Override
	public void disposeApplication(MasterApplication app) {
		if (app != null) {
			app.stop();
		}
	}

}
