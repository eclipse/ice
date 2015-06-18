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
package org.eclipse.ice.client.widgets.jme.internal;

import org.eclipse.ice.client.widgets.jme.MasterApplication;
import org.eclipse.ice.client.widgets.jme.ViewAppState;


/**
 * This class provides a single, static instance of a {@link MasterApplication}.
 * The idea is that every jME-based view or {@link ViewAppState} is connected to
 * a single <code>MasterApplication</code>. There should only be one application
 * running at a time.
 * 
 * @author Jordan
 * 
 */
public class MasterApplicationHolder {

	/**
	 * The factory used to create a <code>MasterApplication</code>. <b>This
	 * should only be set via OSGi DS!</b>
	 */
	private static IMasterApplicationFactory factory = null;

	/**
	 * The <code>MasterApplication</code> responsible for rendering all
	 * jME-based views. <b>This should only be initialized once and should be
	 * stopped when the bundle is closing!</b>
	 */
	private static MasterApplication app = null;

	/**
	 * Gets the single <code>MasterApplication</code> responsible for rendering
	 * all jME-based views. This method creates and starts the application if
	 * this is the first request. Views should be attached to this application
	 * as <code>AppState</code>s.
	 * 
	 * @return The <code>MasterApplication</code> responsible for rendering all
	 *         jME-based views. If OSGi is not working or the bundle is not
	 *         started, this will be null.
	 */
	public static MasterApplication getApplication() {

		// If the app has not been initialized and a factory is available,
		// create and start the application.
		if (app == null && factory != null) {
			app = factory.createApplication();
		}

		return app;
	}

	/**
	 * Sets the factory used to create a <code>MasterApplication</code>. <b>This
	 * should only be called via OSGi DS!</b>
	 * 
	 * @param factory
	 *            The factory that will be used to create a single
	 *            <code>MasterApplication</code>.
	 */
	public void setApplicationFactory(IMasterApplicationFactory factory) {
		System.out.println("MasterApplicationHolder message: "
				+ "Setting MasterApplication factory.");

		// Set the static reference to the factory for use later.
		MasterApplicationHolder.factory = factory;

		return;
	}

	/**
	 * Unsets the factory used to create a <code>MasterApplication</code>. When
	 * called, this also stops the currently available
	 * <code>MasterApplication</code>. This method is called when OSGi is
	 * closing. <b>This should only be called via OSGi DS!</b>
	 * 
	 * @param factory
	 *            The factory that will be used to create a single
	 *            <code>MasterApplication</code>.
	 */
	public void unsetApplicationFactory(IMasterApplicationFactory factory) {

		if (factory == MasterApplicationHolder.factory) {
			System.out.println("MasterApplicationHolder message: "
					+ "Unsetting MasterApplication factory and disposing "
					+ "MasterApplication.");

			// Dispose/stop the MasterApplication.
			factory.disposeApplication(app);
			// Unset the references to signify that this holder is no longer
			// initialized.
			MasterApplicationHolder.app = null;
			MasterApplicationHolder.factory = null;
		}

		return;
	}

}
