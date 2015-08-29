/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.geometry.test;

import org.eclipse.ice.viz.service.jme3.application.MasterApplication;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class can be used to test an embedded, custom {@link ViewAppState}. This
 * test in particular tests the <code>GeometryEditor</code>'s view.
 * 
 * @author Jordan Deyton
 * 
 */
public class GeometryViewLauncher {

	public static void main(String[] args) {
		// Create the display and shell.
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();

		// Create the application and widgets that can create and dispose of jME
		// views.
		final MasterApplication app = MasterApplication.createApplication();

		// Set the shell's Layout and create a SashForm with a left and right
		// Composite.
		shell.setLayout(new FillLayout());

		// Wait until the Application is initialized.
		app.blockUntilInitialized(0);

		// Create a geometry view, start it, and embed it in the shell.
		// TODO Uncomment this when class is implemented.
		// GeometryAppState view = new GeometryAppState(app);
		// view.start();
		// view.createComposite(shell);

		// Lay out the shell.
		shell.layout();

		// ---- Add geometries to the view. ---- //
		// TODO
		// ------------------------------------- //

		// Close the display and shell.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		// Stop the ViewAppState and the Application.
//		view.stop();
		app.stop();

		return;
	}
}
