/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.mesh;

import org.eclipse.ice.client.widgets.geometry.GeometryCompositeFactory;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MeshLauncherTester {
	public static void main(String[] args) {

		// Create the Display.
		Display display = new Display();

		// Create the Shell (window).
		Shell shell = new Shell(display);
		shell.setText("Mesh Tester");
		shell.setSize(1024, 768);
		shell.setLayout(new FillLayout());

		// Construct the JME3 SimpleApplication inside the Shell.
		GeometryCompositeFactory factory = new GeometryCompositeFactory();
		factory.renderMeshComposite(shell, new MeshComponent());

		// Open the shell.
		shell.open();

		// SOP UI loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		// Right now, this is the simplest way to halt the JME3 application.
		// Otherwise, the program does not actually terminate.
		System.exit(0);

		return;
	}
}
