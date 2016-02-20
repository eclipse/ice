/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.visit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Listener;
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
 * @author Taylor Patterson, Jordan Deyton
 *
 */
public class VisItMouseManager {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(VisItMouseManager.class);

	/**
	 * The widget that an instance of this object will manage the mouse
	 * movements for
	 */
	private final VisItSwtWidget widget;

	/**
	 * The daemon Thread used to process mouse movements.
	 */
	private final Thread thread;

	/**
	 * The Executor for the thread
	 */
	private final ExecutorService threadExec;

	/**
	 * Used to indicate when the Thread should or shouldn't be executed.
	 */
	private final AtomicBoolean listen;

	/**
	 * The mouse position used to process the image rotation.
	 */
	private final AtomicReference<Point> mouseLocation;

	/**
	 * Indicates whether or not the user is holding the left mouse button down
	 * while inside the VisIt {@link #widget}.
	 */
	private boolean mousePressed;
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
	 * A wheel listener to trigger zoom events.
	 */
	private MouseWheelListener wheelListener;
	/**
	 * A move listener to keep track of mouse location when dragging to rotate.
	 */
	private MouseMoveListener moveListener;
	/**
	 * A listener for mouse press (down) and release (up) events. These events
	 * should trigger drag rotation.
	 */
	private MouseListener mouseListener;
	/**
	 * A listener that will dispose resources used by this manager when the
	 * {@link #widget} is disposed.
	 */
	private DisposeListener disposeListener;

	/**
	 * The constructor
	 *
	 * @param visItWidget
	 *            The VisItSwtWidget for which this instance will manage the
	 *            mouse input.
	 */
	public VisItMouseManager(VisItSwtWidget visItWidget) {

		// Initialize the fields of this class
		widget = visItWidget;
		listen = new AtomicBoolean(false);
		mouseLocation = new AtomicReference<Point>();
		mousePressed = false;
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
						widget.mouseMove(newLoc.x, newLoc.y, ctrlPressed,
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

		// See if the widget is null or disposed.
		if (visItWidget == null) {
			throw new NullPointerException("VisItMouseManager error: "
					+ "Cannot handle mouse events for a null widget.");
		} else if (visItWidget.isDisposed()) {
			throw new NullPointerException("VisItMouseManager error: "
					+ "Cannot handle mouse events for a disposed widget.");
		}

		// Register the listeners. This should not be called if the widget is
		// null or disposed.
		registerListeners(widget);

		return;
	}

	/**
	 * Disposes all resources used by this mouse manager. After this method is
	 * called, the manager no longer processes mouse input events for the
	 * {@link #widget}.
	 */
	private void dispose() {

		// Unregister and dispose all listeners.
		unregisterListeners(widget);

		// Stop processing thread events.
		stop();

		// Unset all flags.
		mousePressed = false;
		ctrlPressed = false;
		shiftPressed = false;

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
	private void enqueueMouseLocation(int x, int y) {

		// Set the mouse location
		mouseLocation.set(new Point(x, y));

		return;
	}

	/**
	 * Creates and registers all listeners for the specified widget.
	 *
	 * @param widget
	 *            The widget that will receive listeners.
	 */
	private void registerListeners(final VisItSwtWidget widget) {
		// Create a wheel listener to zoom in or out based on the wheel.
		if (wheelListener == null) {
			wheelListener = new MouseWheelListener() {
				@Override
				public void mouseScrolled(MouseEvent e) {
					String direction = (e.count > 0) ? "in" : "out";
					widget.zoom(direction);
				}
			};
			// Dummy listener required to give widget proper focus for mouse wheel
			// scrolling.
			Listener dummy = new Listener(){
				@Override
				public void handleEvent(org.eclipse.swt.widgets.Event event) {
				}
			};
			widget.addListener(SWT.KeyDown, dummy);
			widget.addMouseWheelListener(wheelListener);

		}
		// Create a MouseMoveListener to keep track of the mouse location when
		// dragging.
		if (moveListener == null) {
			moveListener = new MouseMoveListener() {
				@Override
				public void mouseMove(MouseEvent e) {
					if (mousePressed) {
						// Pass the event to the manager
						enqueueMouseLocation(e.x, e.y);
					}
				}
			};
			widget.addMouseMoveListener(moveListener);
		}
		// Create a MouseListener to track mouse press and release events. These
		// should trigger drag rotations.
		if (mouseListener == null) {
			mouseListener = new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// Nothing to do yet.
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// Set the pressed flag
					mousePressed = true;
					// Start the mouseManager thread
					start(e.x, e.y, (e.stateMask & SWT.CTRL) != 0,
							(e.stateMask & SWT.SHIFT) != 0);
				}

				@Override
				public void mouseUp(MouseEvent e) {
					// Set the mouse pressed flag
					mousePressed = false;
					// Stop the mouseManager thread
					stop();
				}
			};
			widget.addMouseListener(mouseListener);
		}
		// Create a dispose listener to dispose the mouse manager's resources
		// when the widget is disposed.
		if (disposeListener == null) {
			disposeListener = new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					dispose();
				}
			};
			widget.addDisposeListener(disposeListener);
		}

		return;
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
	private void start(int x, int y, boolean ctrl, boolean shift) {

		// Set the indication flags for the 'Ctrl' and 'Shift' keys
		ctrlPressed = ctrl;
		shiftPressed = shift;

		// Get the location where the click-and-drag began and set it for the
		// widget
		widget.mouseStart(x, y, ctrlPressed, shiftPressed);

		// Set the execution flag to true
		listen.set(true);

		// Begin thread execution
		threadExec.execute(thread);

		return;
	}

	/**
	 * This operation stops the thread execution.
	 */
	private void stop() {

		// Set the execution flag to false
		listen.set(false);

		// Stop thread execution
		try {
			thread.join();
		} catch (InterruptedException e) {
			logger.error(getClass().getName() + " Exception!",e);
		}

		// Call the stop operation in the VisItSwtWidget
		widget.mouseStop(0, 0, ctrlPressed, shiftPressed);

		return;
	}

	/**
	 * Unregisters and unsets all listeners for the specified widget.
	 *
	 * @param widget
	 *            The widget whose listeners should be removed.
	 */
	private void unregisterListeners(VisItSwtWidget widget) {
		// Remove and dispose all listeners.
		if (disposeListener != null) {
			widget.removeDisposeListener(disposeListener);
			disposeListener = null;
		}
		if (wheelListener != null) {
			widget.removeMouseWheelListener(wheelListener);
			wheelListener = null;
		}
		if (moveListener != null) {
			widget.removeMouseMoveListener(moveListener);
			moveListener = null;
		}
		if (mouseListener != null) {
			widget.removeMouseListener(mouseListener);
			mouseListener = null;
		}

		return;
	}
}
