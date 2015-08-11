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
package org.eclipse.ice.viz.service.jme.internal;

import org.eclipse.ice.viz.service.jme3.application.MasterApplication;

/**
 * This is an interface for a factory that can create and dispose of
 * {@link MasterApplication}s. This is intended to be used by OSGi DS to provide
 * a single, static <code>MasterApplication</code> to render jME scenes.
 * 
 * @author Jordan
 * 
 */
public interface IMasterApplicationFactory {

	/**
	 * Creates and starts a new <code>MasterApplication</code>. Ideally, this
	 * 
	 * @return A newly created and started <code>MasterApplication</code>.
	 */
	public MasterApplication createApplication();

	/**
	 * Stops and disposes of the specified <code>MasterApplication</code>.
	 * 
	 * @param app
	 *            The application to stop.
	 */
	public void disposeApplication(MasterApplication app);
}
