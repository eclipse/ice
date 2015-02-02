/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.ice.viz.service.preferences.TableComponentComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Launcher {

	public static void main(String[] args) {

		// Create the Display.
		final Display display = new Display();

		// Create the Shell (window).
		final Shell shell = new Shell(display);
		shell.setText("VisIt Tester");
		shell.setSize(1024, 768);
		shell.setLayout(new GridLayout(1, false));

		Logger.getGlobal().setLevel(Level.ALL);

		final ConnectionManager connections = new ConnectionManager();

		// Create a TableComponentComposite.
		TableComponentComposite table = new TableComponentComposite(shell,
				SWT.NONE);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setTableComponent(connections);

		// Open the shell.
		shell.open();

		// SOP UI loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		return;
	}
}
