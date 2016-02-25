/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.widgets;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

/**
 * This class manages mouse controls for an {@link ParaViewCanvas} and feeds
 * events to an associated Canvas at the appropriate intervals.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewMouseAdapter {

	/*-
	 * Notes on requests sent to the ParaView client for mouse movement:
	 * 
	 * The ParaView web client expects a well-defined, ordered set of events
	 * when rotating or zooming. It is of critical importance that the below
	 * guidelines be followed when sending mouse events to the web client.
	 *
	 * Rotating:
	 * 1 - Left mouse is pressed
	 *   action - down
	 *   states - buttonLeft must be true, all else false
	 *   x,y    - should be the current cursor location, both normalized
	 * 2 - Mouse moves (this may repeat any number of times)
	 *   action - move
	 *   states - buttonLeft must be true, all else false
	 *   x,y    - should be the current cursor location, both normalized
	 * 3 - Left mouse is released
	 *   action - up
	 *   states - all states should be false
	 *   x,y    - should be the current cursor location, both normalized
	 * 
	 * Zooming:
	 * 1 - Right mouse + CTRL is pressed (alternatively, when scrolling starts)
	 *   action - down
	 *   states - buttonRight and ctrlKey should be true, all else false
	 *   x,y    - y should be the current zoom ratio, normalized. x is 0.
	 * 2 - Mouse moves (this may repeat any number of times)
	 *   action - move
	 *   states - buttonRight and ctrlKey should be true, all else false
	 *   x,y    - y should be the current zoom ratio, normalized. x is 0.
	 * 3 - Right mouse + CTRL is released (alternatively, when scrolling stops)
	 *   action - up
	 *   states - all states should be false
	 *   x,y    - y should be the current zoom ratio, normalized. x is 0.
	 * 
	 * In our code below, we adhere to these by using the following mechanisms:
	 * 
	 * Mouse movement is handled as expected using mouseDown(...), 
	 * mouseMove(...), and mouseUp(...).
	 * 
	 * Zooming is instead associated with the scroll wheel. As there is only a 
	 * mouseScrolled(...) event and no scroll equivalent to mouseDown(...) or 
	 * mouseUp(...), we instead must apply the following changes:
	 * 
	 * On the first scroll (using a boolean flag) -- send a zoom start event 
	 * (step 1) followed by a zoom event (step 2).
	 * On subsequent scrolls -- send a zoom event (step 2).
	 * On the next non-scroll event -- send a zoom end event (step 3).
	 */

	// TODO I'm not sure how well this works if you try zooming and rotating at
	// the same time.

	/**
	 * A simple wrapper for MouseEvents, except it also maintains metadata
	 * required when posting mouse events to the web client.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class MouseInteraction {
		/**
		 * The x position of the mouse event.
		 */
		public double x;
		/**
		 * The y position of the mouse event. Remember that y increases from top
		 * to bottom.
		 */
		public double y;
		/**
		 * The type of event.
		 */
		public final MouseInteractionType type;
		/**
		 * If true, tells the client that the left mouse button is pressed.
		 */
		public boolean buttonLeft = false;
		/**
		 * If true, tells the client that the right mouse button is pressed.
		 */
		public boolean buttonRight = false;
		/**
		 * If true, tells the client that the CTRL key is pressed.
		 */
		public boolean ctrlKey = false;

		/**
		 * The default constructor.
		 * 
		 * @param x
		 *            The normalized x coordinate for the mouse event.
		 * @param y
		 *            The normalized y coordinate for the mouse event.
		 * @param action
		 *            The action for the event. Expected not to be {@code null}.
		 */
		public MouseInteraction(double x, double y, MouseInteractionType action) {
			this.x = x;
			this.y = y;
			this.type = action;
		}

		/**
		 * Gets the state. These correspond to, respectively, "buttonLeft",
		 * "buttonMiddle", "buttonRight", "shiftKey", "ctrlKey", "altKey", and
		 * "metaKey".
		 * 
		 * @return An array of boolean representing the mouse buttons or keys
		 *         pressed.
		 */
		public boolean[] getState() {
			return new boolean[] { buttonLeft, false, buttonRight, false, ctrlKey, false, false };
		}
	}
	/**
	 * A simple enumeration representing the allowed action strings.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum MouseInteractionType {
		/**
		 * A button has been pressed down. Usually the first action in a chain.
		 */
		DOWN("down"),

		/**
		 * A button has been released. Usually the last action in a chain.
		 */
		UP("up"),

		/**
		 * The mouse has been moved. Usually between a {@link #DOWN} and
		 * {@link #UP} action.
		 */
		MOVE("move");

		/**
		 * The string used by the web client to denote the action.
		 */
		private final String actionString;

		/**
		 * Creates the enum value.
		 * 
		 * @param actionString
		 *            The string used by the web client to denote the action.
		 */
		private MouseInteractionType(String actionString) {
			this.actionString = actionString;
		}

		/**
		 * Returns the string used by the web client to denote the action.
		 */
		@Override
		public String toString() {
			return actionString;
		}
	}
	// ---- Zoom / Mouse-Scroll Variables ---- //
	/**
	 * Used when computing the normalized zoom.
	 */
	private static final double INVERSE_PI = 1.0 / Math.PI;
	/**
	 * The client that is providing a ParaView image. Mouse events will trigger
	 * updates to this client.
	 */
	private IParaViewWebClient client;

	/**
	 * The current view ID on the client. It will be updated by mouse events.
	 */
	private int viewId;

	/**
	 * The current SWT Control to which this adapter is registered for its
	 * various listener operations.
	 */
	private Control control;
	/**
	 * The target ParaView Canvas for this adapter's mouse events. This will be
	 * refreshed after each mouse event is processed.
	 */
	private ParaViewCanvas canvas;
	/**
	 * The service used to start worker threads. This should provide a single
	 * thread so that events are processed in order (which is required for
	 * correct behavior of the web client).
	 */
	private ExecutorService executorService;
	// ---- Listeners ---- //
	/**
	 * A listener that updates {@link #inverseSizeX} and {@link #inverseSizeY}
	 * when the associated control is resized. These values will be used when
	 * computing the normalized coordinates that must be sent to the web client.
	 */
	private final ControlListener controlListener;
	/**
	 * Listens for the associated control's dispose events. When the control is
	 * unset or disposed, the worker thread should be shut down.
	 */
	private final DisposeListener disposeListener;
	/**
	 * When focus is lost, this stops the scroll event. This prevents the canvas
	 * from constantly refreshing when it was last zoomed.
	 */
	private final FocusListener focusListener;

	/**
	 * The listener used to enable/disable rotating when the mouse buttons are
	 * pressed/released. This also disables zooming on mouse up if zooming is
	 * currently activated.
	 */
	private final MouseListener mouseListener;
	/**
	 * The listener used to send rotate events when the mouse is being (clicked
	 * and) dragged.
	 */
	private final MouseMoveListener mouseMoveListener;
	/**
	 * The listener used to start zooming and send additional zoom events when
	 * the mouse is scrolled.
	 */
	private final MouseWheelListener mouseWheelListener;
	// ------------------- //
	// ---- Rotation / Mouse-Drag Variables ---- //
	/**
	 * Holds the value 1.0 / the current width of the associated control.
	 * <p>
	 * This is only ever recomputed when the control's size changes.
	 * </p>
	 */
	private double inverseSizeX;
	/**
	 * Holds the value 1.0 / the current height of the associated control.
	 * <p>
	 * This is only ever recomputed when the control's size changes.
	 * </p>
	 */
	private double inverseSizeY;

	/**
	 * An array for holding the current normalized position.
	 * <p>
	 * This is only ever used on the lone worker thread from the
	 * {@link #executorService}, and using it eliminates the need to re-allocate
	 * a new array on every call to
	 * {@link #getNormalizedPosition(double, double)}.
	 * </p>
	 */
	private final double[] normalizedPosition;
	/**
	 * Whether or not the mouse button is pressed down.
	 * <p>
	 * This is only ever accessed from the UI thread in
	 * {@link #mouseDown(MouseEvent)}, {@link #mouseMove(MouseEvent)}, and
	 * {@link #mouseUp(MouseEvent)}.
	 * </p>
	 */
	private boolean rotating;
	/**
	 * The current rotation or mouse-drag event that needs to be processed.
	 * <p>
	 * This is used to eliminate intermediate requests to the client. When
	 * processed on the single worker thread, the <i>current</i> rotational
	 * values should be used.
	 * </p>
	 */
	private final AtomicReference<MouseInteraction> rotation;
	// ----------------------------------------- //
	/**
	 * An integer representing the number of scroll clicks in either direction.
	 * It starts off at 0 and is updated when the mouse wheel is scrolled.
	 * <p>
	 * This is only ever accessed from the UI thread in
	 * {@link #mouseScrolled(MouseEvent)}.
	 * </p>
	 */
	private int scrollCount;

	/**
	 * The current rotation or mouse-drag event that needs to be processed.
	 * <p>
	 * This is used to eliminate intermediate requests to the client. When
	 * processed on the single worker thread, the <i>current</i> rotational
	 * values should be used.
	 * </p>
	 */
	private final AtomicReference<MouseInteraction> zoom;

	/**
	 * Whether or not the mouse wheel has been scrolled.
	 * <p>
	 * This is only ever accessed from the UI thread in
	 * {@link #mouseScrolled(MouseEvent)} and {@link #mouseDown(MouseEvent)}.
	 * </p>
	 */
	private boolean zooming;
	// --------------------------------------- //

	/**
	 * The default constructor. The adapter will need to have its ParaView web
	 * client, view, and associated Control set.
	 */
	public ParaViewMouseAdapter() {
		this(null, -1, null);
	}

	/**
	 * The full constructor.
	 * 
	 * @param client
	 *            The client that is providing a ParaView image. Mouse events
	 *            will trigger updates to this client.
	 * @param viewId
	 *            The current view ID on the client. It will be updated by mouse
	 *            events.
	 * @param control
	 *            The current SWT Control to which this adapter is registered
	 *            for its various listener operations. Although this is
	 *            typically a {@link ParaViewCanvas}, it may be any SWT Control.
	 */
	public ParaViewMouseAdapter(IParaViewWebClient client, int viewId, Control control) {

		// There is no worker thread by default until a valid control is set.
		executorService = null;

		// Initialize rotation variables.
		rotating = false;
		normalizedPosition = new double[2];
		rotation = new AtomicReference<MouseInteraction>();
		controlListener = new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				refreshSizeVariables();
			}
		};

		// Initialize zoom variables.
		scrollCount = 0;
		zooming = false;
		zoom = new AtomicReference<MouseInteraction>();

		// Initialize the listeners.
		disposeListener = new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				// Unset the control as it is not of use when it is disposed.
				setControl(null);
			}
		};
		focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				stopZoom();
			}
		};
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				stopZoom();
				startRotate(e.x, e.y);
			}

			@Override
			public void mouseUp(MouseEvent e) {
				stopRotate(e.x, e.y);
			}
		};
		mouseMoveListener = new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				rotate(e.x, e.y);
			}
		};
		mouseWheelListener = new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				startZoom();
				zoom(e.count);
			}
		};

		// Set the specified variables.
		setClient(client);
		setViewId(viewId);
		setControl(control);

		return;
	}

	/**
	 * Normalizes the specified x and y coordinates based on the size of the
	 * associated {@link #control}.
	 * 
	 * @param x
	 *            The x value to normalize.
	 * @param y
	 *            The y value to normalize.
	 * @return An array of 2 elements containing the normalized x and y values,
	 *         in that order.
	 */
	private double[] getNormalizedPosition(double x, double y) {
		// Compute the normalized x and y positions using the
		// control's current size.
		normalizedPosition[0] = (double) x * inverseSizeX;
		normalizedPosition[1] = 1.0 - (double) y * inverseSizeY;
		return normalizedPosition;
	}

	/**
	 * Gets the current, normalized zoom based on the current scroll count.
	 * 
	 * @param y
	 *            The current scroll count. Should be the current or recent
	 *            value of {@link #scrollCount}.
	 * @return A normalized zoom value between 0 and 1.
	 */
	private double getNormalizedZoom(double y) {
		return Math.atan(0.005 * y) * INVERSE_PI + 0.5;
	}

	/**
	 * Updates {@link #inverseSizeX} and {@link #inverseSizeY} based on the size
	 * of the {@link #control}. <b>This must be called from the UI thread!</b>
	 */
	private void refreshSizeVariables() {
		if (!control.isDisposed()) {
			Point controlSize = control.getSize();
			inverseSizeX = 1.0 / controlSize.x;
			inverseSizeY = 1.0 / controlSize.y;
		}
	}

	/**
	 * Signals the client to rotate the view.
	 * 
	 * @param x
	 *            The current mouse x position when the mouse button was moved.
	 * @param y
	 *            The current mouse y position when the mouse button was moved.
	 */
	private void rotate(int x, int y) {
		// If the mouse is being dragged, send a mouse drag event to the web
		// client.
		if (rotating) {
			// Create a MouseInteraction for moving (rotating).
			rotation.set(new MouseInteraction(x, y, MouseInteractionType.MOVE));

			// Submit a task to send an MOVE event with the current position.
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// Get the current mouse interaction.
					MouseInteraction rotateEvent = rotation.getAndSet(null);
					if (rotateEvent != null) {
						// Update the rotate event.
						rotateEvent.buttonLeft = true;

						// Normalize the position values.
						double[] normPosition = getNormalizedPosition(rotateEvent.x, rotateEvent.y);
						rotateEvent.x = normPosition[0];
						rotateEvent.y = normPosition[1];

						// Send the request to the client. Refresh because a
						// rotation has been applied.
						sendMouseInteraction(rotateEvent, true);
					}

					return;
				}
			});
		}

		return;
	}

	/**
	 * Sends a {@link MouseInteraction} event to the {@link #client} and
	 * triggers a refresh of the {@link #canvas} afterward if requested.
	 * 
	 * @param interaction
	 *            The mouse event to pass to the client.
	 * @param refresh
	 *            If true and the event is successfully processed, the
	 *            associated canvas will be refreshed. Otherwise, the canvas
	 *            will <i>not</i> be refreshed.
	 * @return True if the event was successfully processed, false otherwise.
	 */
	private boolean sendMouseInteraction(MouseInteraction interaction, boolean refresh) {
		boolean sent = false;

		IParaViewWebClient clientRef = client;
		if (clientRef != null) {
			try {
				// Send the mouse event to the client.
				clientRef.event(viewId, interaction.x, interaction.y, interaction.type.toString(),
						interaction.getState()).get();

				// Set the flag that the event was processed.
				sent = true;

				// If the zoom request was processed, refresh the Canvas.
				final ParaViewCanvas canvasRef = canvas;
				if (refresh && canvasRef != null) {
					canvas.refresh();
				}
			} catch (InterruptedException | ExecutionException e) {
				// The event could not be processed.
			}
		}

		return sent;
	}

	/**
	 * Sets the {@link ParaViewCanvas} that will be refreshed after a mouse
	 * event in the {@link #control} is processed and posted to the web client.
	 * 
	 * @param canvas
	 *            The target Canvas that will be updated. If {@code null}, no
	 *            Canvas will be updated after mouse events.
	 * @return True if the Canvas was changed to a <i>new</i> value.
	 */
	public boolean setCanvas(ParaViewCanvas canvas) {
		boolean changed = false;
		if (canvas != this.canvas) {
			this.canvas = canvas;
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the current ParaView web client used by this mouse adapter.
	 * <p>
	 * <b>Note:</b> Any change is not guaranteed to take effect until the next
	 * mouse event on the associated {@link #control}.
	 * </p>
	 * 
	 * @param client
	 *            The new client. If {@code null} or not connected, then the
	 *            rendered image will not be able to update.
	 * @return True if the client was changed to a <i>new</i> value, false
	 *         otherwise.
	 */
	public boolean setClient(IParaViewWebClient client) {
		boolean changed = false;
		if (client != this.client) {
			this.client = client;
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the current control with which this mouse adapter is linked. After
	 * this call, this adapter will no longer be registered with the previous
	 * control but will be registered with the new control.
	 * 
	 * @param control
	 *            The new SWT Control on which this adapter should listen for
	 *            mouse events.
	 * @return True if the control was changed to a <i>new</i> value, false
	 *         otherwise.
	 */
	public boolean setControl(Control control) {

		boolean changed = false;

		if (control != this.control) {
			// If the previous control was valid, unregister its listeners.
			if (this.control != null) {
				this.control.removeControlListener(controlListener);
				this.control.removeDisposeListener(disposeListener);
				this.control.removeFocusListener(focusListener);
				this.control.removeMouseListener(mouseListener);
				this.control.removeMouseMoveListener(mouseMoveListener);
				this.control.removeMouseWheelListener(mouseWheelListener);

				// If there is no new control, shut down the worker thread.
				if (control == null) {
					executorService.shutdown();
					executorService = null;
				}
			}

			// Update the reference to the control.
			this.control = control;
			changed = true;

			// If the new control is valid, register its listeners.
			if (control != null) {
				// Create the worker thread if necessary.
				if (executorService == null) {
					executorService = Executors.newSingleThreadExecutor();
				}

				// Update the size variables based on the new control.
				control.getDisplay().syncExec(new Runnable() {
					@Override
					public void run() {
						refreshSizeVariables();
					}
				});

				control.addControlListener(controlListener);
				control.addDisposeListener(disposeListener);
				control.addFocusListener(focusListener);
				control.addMouseListener(mouseListener);
				control.addMouseMoveListener(mouseMoveListener);
				control.addMouseWheelListener(mouseWheelListener);
			}
		}

		return changed;
	}

	/**
	 * Sets the ID of the current view that is rendered via the associated
	 * ParaView web client.
	 * <p>
	 * <b>Note:</b> Any change is not guaranteed to take effect until the next
	 * mouse event on the associated {@link #control}.
	 * </p>
	 * 
	 * @param viewId
	 *            The ID of the view to be rendered. If invalid, then the
	 *            rendered image will not be able to update.
	 * @return True if the view ID was changed to a <i>new</i> value, false
	 *         otherwise.
	 */
	public boolean setViewId(int viewId) {
		boolean changed = false;
		if (viewId != this.viewId) {
			this.viewId = viewId;
			changed = true;
		}
		return changed;
	}

	/**
	 * Signals the client to start rotating the view.
	 * 
	 * @param x
	 *            The current mouse x position when the mouse button was
	 *            pressed.
	 * @param y
	 *            The current mouse y position when the mouse button was
	 *            pressed.
	 */
	private void startRotate(int x, int y) {
		if (!rotating) {
			// Get the actual mouse position when the mouse button was pressed.
			final int mouseX = x;
			final int mouseY = y;

			// Submit a task to send a DOWN event with the current mouse
			// position.
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// Get the normalized x and y values for the mouse position.
					double[] normPosition = getNormalizedPosition(mouseX, mouseY);
					double x = normPosition[0];
					double y = normPosition[1];

					// Create a MouseInteraction for pressing buttons.
					MouseInteraction moveStart;
					moveStart = new MouseInteraction(x, y, MouseInteractionType.DOWN);
					moveStart.buttonLeft = true;

					// Send the request to the client. There should be no need
					// to
					// refresh.
					sendMouseInteraction(moveStart, false);

					return;
				}
			});

			// Now we can set the mouse drag flag to true.
			rotating = true;
		}
		return;
	}

	/**
	 * Signals the client to start zooming the view.
	 */
	private void startZoom() {
		// On the first scroll event, send a client event to *start* scrolling.
		if (!zooming) {
			// Submit a task to send a DOWN event with the current zoom.
			final int currentZoom = scrollCount;
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// Get the normalized x and y values for the current zoom.
					double x = 0.0;
					double y = getNormalizedZoom(currentZoom);

					// Create a MouseInteraction for pressing buttons.
					MouseInteraction zoomStart;
					zoomStart = new MouseInteraction(x, y, MouseInteractionType.DOWN);
					zoomStart.buttonRight = true;
					zoomStart.ctrlKey = true;

					// Send the request to the client. There should be no need
					// to refresh.
					sendMouseInteraction(zoomStart, false);

					return;
				}
			});

			// Now we can set the zoom flag to true.
			zooming = true;
		}
		return;
	}

	/**
	 * Signals the client to stop rotating the view.
	 * 
	 * @param x
	 *            The current mouse x position when the mouse button was
	 *            released.
	 * @param y
	 *            The current mouse y position when the mouse button was
	 *            released.
	 */
	private void stopRotate(int x, int y) {
		// Unset the mouse drag flag.
		if (rotating == true) {
			rotating = false;

			// Get the actual mouse position when the mouse button was released.
			final int mouseX = x;
			final int mouseY = y;

			// Submit a task to send an UP event with the current mouse
			// position.
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// Get the normalized x and y values for the mouse position.
					double[] normPosition = getNormalizedPosition(mouseX, mouseY);
					double x = normPosition[0];
					double y = normPosition[1];

					// Create a MouseInteraction for releasing buttons.
					MouseInteraction moveStop;
					moveStop = new MouseInteraction(x, y, MouseInteractionType.UP);

					// Send the request to the client. There should be no need
					// to
					// refresh.
					sendMouseInteraction(moveStop, false);

					return;
				}
			});
		}
		return;
	}

	/**
	 * Signals the client to stop zooming the view.
	 */
	private void stopZoom() {
		// If the mouse was scrolled previously, send an event to *stop*
		// scrolling.
		if (zooming) {
			zooming = false;
			// Submit a task to send an UP event with the current zoom.
			final int currentZoom = scrollCount;
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// Get the normalized x and y values for the current zoom.
					double x = 0.0;
					double y = getNormalizedZoom(currentZoom);

					// Create a MouseInteraction for releasing buttons.
					MouseInteraction zoomStop;
					zoomStop = new MouseInteraction(x, y, MouseInteractionType.UP);

					// Send the request to the client. There should be no need
					// to refresh.
					sendMouseInteraction(zoomStop, false);

					return;
				}
			});
		}
		return;
	}

	/**
	 * Signals the client to zoom the view.
	 * 
	 * @param count
	 *            The count from the mouse scroll operation.
	 */
	private void zoom(int count) {
		if (zooming) {
			// Adjust the scroll count. Note that zooming in/out is inversely
			// related to the direction of the count.
			scrollCount -= count;

			// Create a MouseInteraction for moving (zooming).
			zoom.set(new MouseInteraction(0.0, scrollCount, MouseInteractionType.MOVE));

			// Submit a task to send an MOVE event with the current zoom.
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// Get the current mouse interaction.
					MouseInteraction zoomEvent = zoom.getAndSet(null);
					if (zoomEvent != null) {
						// Update the zoom event.
						zoomEvent.buttonRight = true;
						zoomEvent.ctrlKey = true;

						// Get the normalized y value for the current zoom
						// event.
						zoomEvent.y = getNormalizedZoom(zoomEvent.y);

						// Send the request to the client. Refresh because a
						// rotation has been applied.
						sendMouseInteraction(zoomEvent, true);
					}

					return;
				}
			});
		}
		return;
	}
}
