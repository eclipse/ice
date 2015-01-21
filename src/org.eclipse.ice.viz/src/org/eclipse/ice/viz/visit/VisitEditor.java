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
package org.eclipse.ice.viz.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtConnectionManager;
import gov.lbnl.visit.swt.VisItSwtWidget;

import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * This is an editor for interacting with the VisIt SWT Widget. It is opened by
 * the associated visualization views in org.eclipse.ice.viz.
 * 
 * @authors Jay Jay Billings, Taylor Patterson
 */
public class VisitEditor extends EditorPart {

	/**
	 * The ID for this editor.
	 */
	public static final String ID = "org.eclipse.ice.viz.visit.VisitEditor";

	/**
	 * 
	 */
	VisItSwtConnection vizConnection;

	/**
	 * The VisIt canvas.
	 */
	VisItSwtWidget vizWidget;

	/**
	 * The top level composite that holds the editor's contents.
	 */
	Composite vizComposite;

	/**
	 * The object used for managing the mouse input daemon thread.
	 */
	VisitMouseManager mouseManager;

	/**
	 * A flag for keeping track of whether or not the mouse left-click button is
	 * being pressed.
	 */
	private boolean mousePressed;

	/**
	 * The constructor
	 */
	public VisitEditor() {
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 *      IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// Set the site, input and name
		setSite(site);
		setInput(input);
		setPartName("VisIt Editor");

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * This operation sets up the Composite that contains the VisIt canvas and
	 * create the VisIt widget.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 *      .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create a top level composite to hold the canvas or text
		vizComposite = new Composite(parent, SWT.FILL);
		vizComposite.setLayout(new FillLayout());

		// Only create the VisIt canvas it there input is available and of the
		// correct type.
		if (getEditorInput() != null
				&& getEditorInput() instanceof VisitEditorInput) {
			// Create the VisIt widget
			vizWidget = new VisItSwtWidget(vizComposite, SWT.BORDER
					| SWT.DOUBLE_BUFFERED);
			// Create the mouse manager
			mouseManager = new VisitMouseManager(vizWidget);
			// Use the mouse wheel to zoom
			vizWidget.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseScrolled(MouseEvent e) {
					String direction = (e.count > 0) ? "in" : "out";
					vizWidget.zoom(direction);
				}
			});
			// Use mouse click to move the plot
			vizWidget.addMouseMoveListener(new MouseMoveListener() {
				@Override
				public void mouseMove(MouseEvent e) {
					if (mousePressed) {
						// Pass the event to the manager
						mouseManager.enqueueMouseLocation(e.x, e.y);
					}
				}
			});
			// Update the mouse in the widget based on its movements
			vizWidget.addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(MouseEvent e) {
					// Set the mouse pressed flag
					mousePressed = false;
					// Stop the mouseManager thread
					mouseManager.stop();
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// Set the pressed flag
					mousePressed = true;
					// Start the mouseManager thread
					mouseManager.start(e.x, e.y, (e.stateMask & SWT.CTRL) != 0,
							(e.stateMask & SWT.SHIFT) != 0);
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
			});
			// Shut down the widget
			vizWidget.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
				}
			});

			// Get the input map and all of the required parameters
			HashMap<String, String> inputMap = ((VisitEditorInput) getEditorInput())
					.getInputMap();

			VisItSwtConnection conn = null;

			String key = inputMap.get("connId");
			if (VisItSwtConnectionManager.hasConnection(key)) {
				conn = VisItSwtConnectionManager.getConnection(key);
			} else {
				conn = VisItSwtConnectionManager.createConnection(key,
						new Shell(Display.getDefault()), inputMap);
			}

			boolean result = (conn != null);
			boolean isRemote = Boolean.valueOf(inputMap.get("isRemote"));

			// Check for a successful launch of and connection to VisIt
			// TODO Provider with greater feedback (i.e. VisIt not at path,
			// failed connection, etc.)
			if (!result) {
				if (isRemote) {
					MessageDialog.openError(parent.getShell(),
							"Failed to Connect to VisIt",
							"Unable to connect to a running VisIt client.");
				} else {
					MessageDialog.openError(parent.getShell(),
							"Failed to Launch VisIt",
							"VisIt has failed to launch.");
				}
			}

			if (result) {

				try {
					int windowId = Integer.parseInt(inputMap.get("windowId"));
					int windowWidth = Integer.parseInt(inputMap
							.get("windowWidth"));
					int windowHeight = Integer.parseInt(inputMap
							.get("windowHeight"));

					vizWidget.setVisItSwtConnection(conn, windowId,
							windowWidth, windowHeight);
				} catch (Exception e) {
					MessageDialog.openError(parent.getShell(),
							"Failed to Set VisIt Connection", e.getMessage());
				}
			}

		} else {
			// Otherwise create the error text
			Text errText = new Text(vizComposite, SWT.NONE);
			errText.setText("No input available.");
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		vizWidget.redraw();
	}

	/**
	 * This operation refreshes the VisIt view.
	 */
	public void refresh() {
		setFocus();
	}

	/**
	 * This operation returns the widget that is responsible for drawing the
	 * VisIt canvas.
	 * 
	 * @return The widget.
	 */
	public VisItSwtWidget getVizWidget() {
		return vizWidget;
	}

}
