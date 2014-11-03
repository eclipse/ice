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
package org.eclipse.ice.client.widgets.reactoreditor.plant;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;

/**
 * This is a factory that uses a specified jME3-based PlantApplication
 * 
 * @author djg
 * 
 */
public class PlantCompositeFactory {

	/**
	 * Renders the PlantApplication inside a parent SWT Composite.
	 * 
	 * @param parent
	 *            The parent SWT Composite that should contain the
	 *            PlantApplication.
	 * @param plantApp
	 *            The PlantApplication to be rendered inside the parent
	 *            Composite.
	 * @return The resulting Composite that has the embedded jME3 application.
	 *         If layout data needs to be set for the view of the application,
	 *         it should be set for this Composite.
	 */
	public Composite renderPlantComposite(Composite parent,
			final PlantApplication plantApp) {

		// Set the jME3 application settings.
		AppSettings settings = new AppSettings(true);
		settings.setFrameRate(60);
		settings.setCustomRenderer(AwtPanelsContext.class);
		plantApp.setSettings(settings);
		plantApp.setPauseOnLostFocus(false);
		
		// Create the embedded frame.
		Composite embeddedComposite = new Composite(parent, SWT.EMBEDDED);

		// Create the jME3 canvas.
		plantApp.createCanvas();

		// Get the application's [AwtPanels] context and create an AwtPanel from
		// it. The panel will be put into an AWT Frame embedded in the
		// Composite.
		AwtPanelsContext context = (AwtPanelsContext) plantApp.getContext();
		final AwtPanel panel = context.createPanel(PaintMode.Accelerated);
		context.setInputSource(panel);
		context.setSystemListener(plantApp);

		// The MeshApplication needs to know the AwtPanel used for rendering.
		plantApp.setAwtPanel(panel);

		// Create the AWT frame inside the SWT.EMBEDDED Composite.
		Frame embeddedFrame = SWT_AWT.new_Frame(embeddedComposite);
		// Add the AwtPanel to the embedded AWT Frame. The panel needs to fill
		// the Frame.
		embeddedFrame.setLayout(new BorderLayout());
		embeddedFrame.add(panel, BorderLayout.CENTER);
		embeddedFrame.pack();
		embeddedFrame.setVisible(true);

		// Add a listener to send the canvas size to the jME3 application. This
		// is critical for parts that rely on the size of the application (like
		// the crosshairs).
		panel.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent arg0) {
				// Do nothing.
			}

			public void componentMoved(ComponentEvent arg0) {
				// Do nothing.
			}

			public void componentResized(ComponentEvent arg0) {
				// TODO
				// plantApp.updateApplicationDimensions(panel.getWidth(),
				// panel.getHeight());
			}

			public void componentShown(ComponentEvent arg0) {
				// Do nothing.
			}
		});

		// Start the jME3 canvas.
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				plantApp.startCanvas();
			}
		});

		return embeddedComposite;
	}

}
