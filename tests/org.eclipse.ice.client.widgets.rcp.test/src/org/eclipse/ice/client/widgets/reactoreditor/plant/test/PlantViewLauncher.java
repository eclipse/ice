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
package org.eclipse.ice.client.widgets.reactoreditor.plant.test;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.jme.MasterApplication;
import org.eclipse.ice.client.widgets.jme.ViewAppState;
import org.eclipse.ice.client.widgets.reactoreditor.plant.PlantAppState;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class can be used to test an embedded, custom {@link ViewAppState}. This
 * test in particular tests the <code>MeshEditor</code>'s view.
 * 
 * @author Jordan Deyton
 * 
 */
public class PlantViewLauncher {

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

		// Create a plant view, start it, and embed it in the shell.
		PlantAppState view = new PlantAppState();
		view.start(app);
		view.createComposite(shell);

		// Lay out the shell.
		shell.layout();

		// ---- Add a plant to the view. ---- //
		PlantComposite plant = new PlantComposite();

		Junction j1, j2;
		Pipe p1, p2;
		HeatExchanger h;

		p1 = new Pipe(3.0, 0.5);
		p1.setId(1);
		p1.setPosition(new double[] { 0, 0, 0 });
		p1.setOrientation(new double[] { 0, 1, 0 });

		p2 = new Pipe(5.0, 0.3);
		p2.setId(2);
		p2.setPosition(new double[] { 3.5, 7, -1 });
		p2.setOrientation(new double[] { 1, 1, 1 });

		h = new HeatExchanger();
		h.setId(3);
		h.setPosition(new double[] { 3, 0, -1 });
		h.setOrientation(new double[] { 0, 1, 0 });
		h.setLength(7);
		h.setInnerRadius(0.5);

		j1 = new Junction();
		j1.setId(4);
		j1.addInput(p1);
		j1.addOutput(h);

		j2 = new Junction();
		j2.setId(5);
		j2.addInput(h.getPrimaryPipe());
		j2.addOutput(p2);

		// Set the plant.
		view.setPlant(plant);

		// Add several plant components.
		// plant.addPlantComponent(j1);
		// plant.addPlantComponent(p1);
		// plant.addPlantComponent(j2);
		// plant.addPlantComponent(p2);
		// plant.addPlantComponent(h);

		CoreChannel c1 = new CoreChannel();
		c1.setId(7);
		c1.setLength(5);
		c1.setPosition(new double[] { 5, 0, 0 });
		c1.setOrientation(new double[] { 0, 1, 0 });
		c1.setRadius(0.5f);
		plant.addPlantComponent(c1);

		Reactor r = new Reactor();
		r.setId(6);
		plant.addPlantComponent(r);

		ArrayList<CoreChannel> coreChannels = new ArrayList<CoreChannel>(3);
		coreChannels.add(c1);
		r.setCoreChannels(coreChannels);
		// ---------------------------------- //

		// Close the display and shell.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		// Stop the ViewAppState and the Application.
		view.stop();
		app.stop();

		return;
	}
}
