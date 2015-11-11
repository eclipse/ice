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
package org.eclipse.ice.client.widgets.reactoreditor.grid.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This class is used to launch the test GridViewerLauncher. It could also be
 * launched through OSGi services, but that method has not been implemented.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class Activator implements BundleActivator {

	private GridViewerLauncher launcher;

	@Override
	public void start(BundleContext context) throws Exception {
		launcher = new GridViewerLauncher();
		launcher.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		launcher.stopThread();
		launcher.join();
	}

}
