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
package org.eclipse.eavp.viz.visit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.lbnl.visit.swt.VisItSwtWidget;

/**
 * This class is used for managing a daemon thread for processing mouse input.
 * Mouse locations are continuously passed to instances of this class via a
 * {@link MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)}
 * implementation and stored in an AtomicReference<Point>. The thread that calls
 * {@link VisItSwtWidget#mouseMove(int, int, boolean, boolean)} gets the mouse
 * location from the AtomicReference<Point> field. This approach causes the
 * image to rotate according to the location of the mouse whenever
 * AtomicReference<Point>#get() is called in the thread.
 *
 * @author Taylor Patterson
 *
 */
public class VisitMouseManager {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(VisitMouseManager.class);

	/**
	 * The widget that an instance of this object will manage the mouse
	 * movements for
	 */
	private VisItSwtWidget vizWidget;

	/**
	 * The daemon Thread used to process mouse movements.
	 */
	private Thread thread;

	/**
	 * The Executor for the thread
	 */
	private ExecutorService threadExec;

	/**
	 * Used to indicate when the Thread should or shouldn't be executed.
	 */
	private AtomicBoolean listen;

	/**
	 * The mouse position used to process the image rotation.
	 */
	private AtomicReference<Point> mouseLocation;

	/**
	 * Indicates whether or not the user is holding the 'Ctrl' button while
	 * executing a click-and-drag.
	 */
	private boolean ctrlPressed;

	/**
	 * Indicates whether or not the user is holding the 'Shift' button while
	 * executing a click-and-drag.
	 */
	private boolean shiftPressed;

	/**
	 * The constructor
	 *
	 * @param widget
	 *            The VisItSwtWidget for which this instance will manage the
	 *            mouse input.
	 */
	public VisitMouseManager(VisItSwtWidget widget) {

		// Initialize the fields of this class
		vizWidget = widget;
		listen = new AtomicBoolean(false);
		mouseLocation = new AtomicReference<Point>();
		ctrlPressed = false;
		shiftPressed = false;

		// Create the thread for processing the mouse input
		thread = new Thread() {
			@Override
			public void run() {
				// Check the status of the execution flag
				while (listen.get()) {
					// Get the new mouse location to process the rotation
					Point newLoc = mouseLocation.getAndSet(null);
					if (newLoc != null) {
						// Call the widget function that processes rotations
						vizWidget.mouseMove(newLoc.x, newLoc.y, ctrlPressed,
								shiftPressed);
					}
				}
			}
		};

		// Flag this as a daemon thread so that it's execution is halted when
		// ICE is closed
		thread.setDaemon(true);

		// Create the Executor for the thread
		threadExec = Executors.newFixedThreadPool(1);
	}

	/**
	 * This operation is called to begin executing the mouse movement processing
	 * thread.
	 *
	 * @param x
	 *            The horizontal location where the click-and-drag began
	 * @param y
	 *            The vertical location where the click-and-drag began
	 * @param ctrl
	 *            The boolean indicator for whether or not the user is pressing
	 *            the 'Ctrl' button
	 * @param shift
	 *            The boolean indicator for whether or not the user is pressing
	 *            the 'Shift' button
	 */
	public void start(int x, int y, boolean ctrl, boolean shift) {

		// Set the indication flags for the 'Ctrl' and 'Shift' keys
		ctrlPressed = ctrl;
		shiftPressed = shift;

		// Get the location where the click-and-drag began and set it for the
		// widget
		vizWidget.mouseStart(x, y, ctrlPressed, shiftPressed);

		// Set the execution flag to true
		listen.set(true);

		// Begin thread execution
		threadExec.execute(thread);

		return;
	}

	/**
	 * This operation stops the thread execution.
	 */
	public void stop() {

		// Set the execution flag to false
		listen.set(false);

		// Stop thread execution
		try {
			thread.join();
		} catch (InterruptedException e) {
			logger.error(getClass().getName() + " Exception!",e);
		}

		// Call the stop operation in the VisItSwtWidget
		vizWidget.mouseStop(0, 0, ctrlPressed, shiftPressed);

		return;
	}

	/**
	 * This function collects the necessary state information execute an image
	 * rotation operation.
	 *
	 * @param x
	 *            The horizontal location of the mouse pointer
	 * @param y
	 *            The vertical location of the mouse pointer
	 */
	public void enqueueMouseLocation(int x, int y) {

		// Set the mouse location
		mouseLocation.set(new Point(x, y));

		return;
	}

}
