/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.geometry;

import org.eclipse.ice.client.widgets.mesh.MeshApplication;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a factory class to create Composites that contain the Geometry
 * widgets. It is meant to be implemented by whichever version of the Geometry
 * widgets is loaded, whether RAP or RCP, but called only once from the
 * ICEGeometryPage.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly, tnp, djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class GeometryCompositeFactory {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The actual JME3 application running the geometry under the hood.
	 * </p>
	 * 
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private SimpleApplication simpleApplication;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation renders the GeometryComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param parent
	 *            <p>
	 *            The parent composite to which the Geometry composite belongs.
	 *            </p>
	 * @param geometryComp
	 *            <p>
	 *            The GeometryComponent that should be rendered.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void renderGeometryComposite(Composite parent,
			GeometryComponent geometryComp) {
		// begin-user-code

		// Set JME3 application settings
		AppSettings settings = new AppSettings(true);
		settings.setFrameRate(60);
		settings.setRenderer(AppSettings.LWJGL_OPENGL_ANY);

		simpleApplication = new GeometryApplication();

		final GeometryApplication geometryApplication = (GeometryApplication) simpleApplication;
		geometryApplication.setSettings(settings);
		geometryApplication.setPauseOnLostFocus(false);

		// Place JME3 canvas on the parent composite

		// Create the embedded frame
		Composite embeddedFrame = new Composite(parent, SWT.EMBEDDED);

		geometryApplication.createCanvas();

		JmeCanvasContext canvasContext = (JmeCanvasContext) geometryApplication
				.getContext();
		canvasContext.setSystemListener(geometryApplication);

		// Create the SWT frame
		Frame window = SWT_AWT.new_Frame(embeddedFrame);
		Canvas canvas = canvasContext.getCanvas();
		window.add(canvas);

		window.pack();
		window.setVisible(true);

		// Load the Geometry if available
		if (geometryComp != null) {
			geometryApplication.loadGeometry(geometryComp);
		}

		// Start the canvas
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				geometryApplication.startCanvas();
			}
		});

		return;
		// end-user-code
	}

	/**
	 * <p>
	 * This operation renders the GeometryComponent.
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The parent composite to which the Geometry composite belongs.
	 *            </p>
	 * @param geometryComp
	 *            <p>
	 *            The GeometryComponent that should be rendered.
	 *            </p>
	 */
	public void renderMeshComposite(Composite parent, MeshComponent meshComp) {
		// begin-user-code

		// Initialize the SimpleApplication as a MeshApplication.
		simpleApplication = new MeshApplication();
		final MeshApplication meshApplication = (MeshApplication) simpleApplication;

		// Set the JME3 application settings.
		AppSettings settings = new AppSettings(true);
		settings.setFrameRate(60);
		settings.setCustomRenderer(AwtPanelsContext.class);
		meshApplication.setSettings(settings);
		meshApplication.setPauseOnLostFocus(false);

		// Create the embedded frame.
		Composite embeddedComposite = new Composite(parent, SWT.EMBEDDED);
		embeddedComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Create the JME3 canvas.
		meshApplication.createCanvas();

		// Get the application's [AwtPanels] context and create an AwtPanel from
		// it. The panel will be put into an AWT Frame embedded in the
		// Composite.
		AwtPanelsContext context = (AwtPanelsContext) meshApplication
				.getContext();
		final AwtPanel panel = context.createPanel(PaintMode.Accelerated);
		context.setInputSource(panel);
		context.setSystemListener(meshApplication);

		// The MeshApplication needs to know the AwtPanel used for rendering.
		meshApplication.setAwtPanel(panel);

		// Create the AWT frame inside the SWT.EMBEDDED Composite.
		Frame embeddedFrame = SWT_AWT.new_Frame(embeddedComposite);
		// Add the AwtPanel to the embedded AWT Frame. The panel needs to fill
		// the Frame.
		embeddedFrame.setLayout(new BorderLayout());
		embeddedFrame.add(panel, BorderLayout.CENTER);
		embeddedFrame.pack();
		embeddedFrame.setVisible(true);

		// // FIXME - The mouse listener works, but the Runnable that's created
		// // will only reveal the SWT Menu whenever ICE is moved or resized. I
		// // still don't understand why this happens, and I haven't been able
		// to
		// // find anyone experiencing this problem. -djg
		// // final Composite fParent = parent;
		// // final Menu menu = new Menu(parent);
		// // MenuItem item = new MenuItem(menu, SWT.NONE);
		// // item.setText("MenuItem1");
		// // item = new MenuItem(menu, SWT.NONE);
		// // item.setText("MenuItem2");
		//
		// MouseListener l = new MouseListener() {
		// public void mouseClicked(MouseEvent e) {
		// System.out.println("AWT Mouse Clicked.");
		// // if (e.getButton() == MouseEvent.BUTTON3) {
		// // System.out.println("Right click!!!");
		// //
		// // final int x = e.getX();
		// // final int y = e.getY();
		// //
		// // Display.getDefault().asyncExec(new Runnable() {
		// // public void run() {
		// // Point p = fParent.toDisplay(x, y);
		// // System.out.println("Opening menu!!! " + p.x + " " + p.y);
		// // menu.setLocation(p.x, p.y);
		// // menu.setVisible(true);
		// // }
		// // });
		// // }
		// return;
		// }
		// public void mouseEntered(MouseEvent arg0) {
		// // Do nothing.
		// System.out.println("AWT Mouse Entered.");
		// }
		// public void mouseExited(MouseEvent arg0) {
		// // Do nothing.
		// System.out.println("AWT Mouse Exited.");
		// }
		// public void mousePressed(MouseEvent arg0) {
		// // Do nothing.
		// System.out.println("AWT Mouse Pressed.");
		// }
		// public void mouseReleased(MouseEvent arg0) {
		// // Do nothing.
		// System.out.println("AWT Mouse Released.");
		// }
		// };
		//
		// KeyListener kl = new KeyListener() {
		// public void keyPressed(KeyEvent arg0) {
		// System.out.println("AWT keyPressed.");
		// }
		// public void keyReleased(KeyEvent arg0) {
		// System.out.println("AWT keyReleased.");
		// }
		// public void keyTyped(KeyEvent arg0) {
		// System.out.println("AWT keyTyped.");
		// }
		// };
		//
		// panel.addMouseListener(l);
		// panel.addKeyListener(kl);

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
				meshApplication.updateApplicationDimensions(panel.getWidth(),
						panel.getHeight());
			}

			public void componentShown(ComponentEvent arg0) {
				// Do nothing.
			}
		});

		// Load the mesh if the MeshComponent is available.
		if (meshComp != null) {
			meshApplication.setMesh(meshComp);
		}
		// Start the JME3 canvas.
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				meshApplication.startCanvas();
			}
		});

		return;
		// end-user-code
	}

	/**
	 * This operation retrieves the SimpleApplication created by this factory.
	 * 
	 * @return The SimpleApplication created by this factory
	 */
	public SimpleApplication getApplication() {
		return simpleApplication;
	}
}