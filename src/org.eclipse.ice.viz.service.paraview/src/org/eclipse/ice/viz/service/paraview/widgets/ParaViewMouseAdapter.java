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
package org.eclipse.ice.viz.service.paraview.widgets;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
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
public class ParaViewMouseAdapter implements MouseListener, MouseMoveListener, MouseWheelListener {

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
	 * The service used to start worker threads.
	 */
	private final ExecutorService executorService;
	/**
	 * Whether or not the mouse button is pressed down.
	 */
	private final AtomicBoolean mouseDown;
	/**
	 * The current mouse event that needs to be processed.
	 */
	private final AtomicReference<MouseInteraction> mouseInteraction;

	private final AtomicBoolean scrolled = new AtomicBoolean();
	
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
		public int x;
		/**
		 * The y position of the mouse event. Remember that y increases from top
		 * to bottom.
		 */
		public int y;
		/**
		 * The "action" for the event. Usually one of "down", "up", "move",
		 * "dblclick", "scroll".
		 */
		public final String action;

		/**
		 * The default constructor.
		 * 
		 * @param e
		 *            The associated MouseEvent. Its position is retrieved.
		 * @param action
		 *            The associated action. If you don't know, use "none".
		 */
		public MouseInteraction(MouseEvent e, String action) {
			x = e.x;
			y = e.y;
			this.action = action;
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
			return new boolean[] { false, false, false, false, false, false, false };
		}
	}

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

		// Set up the ExecutorService so we can start threads later.
		executorService = Executors.newSingleThreadExecutor();

		// Initialize any flags or concurrent utilities here.
		mouseDown = new AtomicBoolean();
		mouseInteraction = new AtomicReference<MouseInteraction>();

		// Set the specified variables.
		setClient(client);
		setViewId(viewId);
		setControl(control);

		return;
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
				this.control.removeMouseListener(this);
				this.control.removeMouseMoveListener(this);
				this.control.removeMouseWheelListener(this);
			}

			// Update the reference to the control.
			this.control = control;
			changed = true;

			// If the new control is valid, register its listeners.
			if (control != null) {
				control.addMouseListener(this);
				control.addMouseMoveListener(this);
				control.addMouseWheelListener(this);
			}
		}

		return changed;
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

	private int scrollCount = 0;

	/*
	 * Implements a method from MouseWheelListener.
	 */
	@Override
	public void mouseScrolled(MouseEvent e) {
		// Add a new zoom request.

		if (scrolled.compareAndSet(false, true)) {
			final MouseInteraction mouseDownEvent = new MouseInteraction(e, "down") {
				@Override
				public boolean[] getState() {
					boolean[] state = super.getState();
					state[2] = true; // right mouse
					state[4] = true; // ctrl
					return state;
				}
			};
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					
					double x = 0.005 * scrollCount;
					double normY = Math.atan(x) / Math.PI + 0.5;
					// double normY = x / (Math.PI * Math.sqrt(1.0 + x*x)) + 0.5;
					System.err.println(normY);
					
					// Send the request to the client.
					IParaViewWebClient clientRef = client;
					if (clientRef != null) {
						try {
							clientRef.event(viewId, 0.0, normY, mouseDownEvent.action, mouseDownEvent.getState()).get();
						} catch (InterruptedException | ExecutionException e) {
							// The event could not be processed.
						}
					}
				}
			});
		}
		
		// Triggering a mouse zoom means we need to pretend the CTRL key is
		// pressed while a click and drag occurs.
		final MouseInteraction event = new MouseInteraction(e, "move") {
			@Override
			public boolean[] getState() {
				return new boolean[] { false, false, true, false, true, false, false };
			}
		};

		scrollCount -= e.count;

		executorService.submit(new Runnable() {
			@Override
			public void run() {

				double x = 0.005 * scrollCount;
				double normY = Math.atan(x) / Math.PI + 0.5;
				// double normY = x / (Math.PI * Math.sqrt(1.0 + x*x)) + 0.5;
				System.err.println(normY);

				// Send the request to the client.
				IParaViewWebClient clientRef = client;
				if (clientRef != null) {
					try {
						clientRef.event(viewId, 0.0, normY, event.action, event.getState()).get();
					} catch (InterruptedException | ExecutionException e) {
						// The event could not be processed.
					}
				}

				// Refresh the Canvas.
				final ParaViewCanvas canvasRef = canvas;
				if (canvasRef != null) {
					canvas.refresh();
				}

				return;
			}
		});

		return;
	}

	/*
	 * Implements a method from MouseMoveListener.
	 */
	@Override
	public void mouseMove(MouseEvent e) {
		// If the mouse is being dragged, send a mouse drag event to the web
		// client.
		if (mouseDown.get()) {
			MouseInteraction event = new MouseInteraction(e, "move") {
				@Override
				public boolean[] getState() {
					return new boolean[] { true, false, false, false, false, false, false };
				}
			};
			refreshMousePosition(event, true);
		}

		return;
	}

	/*
	 * Implements a method from MouseListener.
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Nothing to do yet.
	}

	/*
	 * Implements a method from MouseListener.
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		
		if (scrolled.compareAndSet(true, false)) {
			final MouseInteraction mouseDownEvent = new MouseInteraction(e, "up") {
				@Override
				public boolean[] getState() {
					boolean[] state = super.getState();
//					state[2] = true; // right mouse
//					state[4] = true; // ctrl
					return state;
				}
			};
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					
					double x = 0.005 * scrollCount;
					double normY = Math.atan(x) / Math.PI + 0.5;
					// double normY = x / (Math.PI * Math.sqrt(1.0 + x*x)) + 0.5;
					System.err.println(normY);
					
					// Send the request to the client.
					IParaViewWebClient clientRef = client;
					if (clientRef != null) {
						try {
							clientRef.event(viewId, 0.0, normY, mouseDownEvent.action, mouseDownEvent.getState()).get();
						} catch (InterruptedException | ExecutionException e) {
							// The event could not be processed.
						}
					}
				}
			});
		}
		
		// Send the mouse down event to the web client.
		refreshMousePosition(new MouseInteraction(e, "down") {
			@Override
			public boolean[] getState() {
				boolean[] state = super.getState();
				state[0] = true;
				return state;
			}
		}, false);
		// Now we can set the mouse drag flag to true.
		mouseDown.set(true);
	}

	/*
	 * Implements a method from MouseListener.
	 */
	@Override
	public void mouseUp(MouseEvent e) {
		// Unset the mouse drag flag.
		mouseDown.set(false);
		// Send the mouse up event to the web client.
		refreshMousePosition(new MouseInteraction(e, "up") {
			@Override
			public boolean[] getState() {
				boolean[] state = super.getState();
				state[0] = false;
				return state;
			}
		}, false);
	}

	/**
	 * Sends a mouse interaction or mouse event to the web client.
	 * 
	 * @param e
	 *            The mouse interaction. It contains the mouse coordinates as
	 *            well as the necessary meta info required by the web client.
	 * @param refreshCanvas
	 *            Whether or not to trigger a refresh of the {@link #canvas}--if
	 *            set--after the client is updated.
	 */
	private void refreshMousePosition(MouseInteraction e, final boolean refreshCanvas) {
		// Update the mouse event, getting its current value in the process. If
		// there's already a pending event, then another task will eventually
		// process this new event. Otherwise, we'll need to create a new task to
		// process the drag event.
		if (mouseInteraction.getAndSet(e) == null) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {

					// Get the current event.
					MouseInteraction event = mouseInteraction.getAndSet(null);
					if (event != null) {

						// Get the current size of the associated Control.
						final Point size = new Point(1, 1);
						final Control controlRef = control;
						if (controlRef != null && !controlRef.isDisposed()) {
							control.getDisplay().syncExec(new Runnable() {
								@Override
								public void run() {
									if (!controlRef.isDisposed()) {
										Point controlSize = control.getSize();
										size.x = controlSize.x;
										size.y = controlSize.y;
									}
								}
							});
						}

						// Compute the normalized x and y positions using the
						// control's current size.
						double normalizedX = (double) event.x / (double) size.x;
						double normalizedY = 1.0 - (double) event.y / (double) size.y;

						// Send the request to the client.
						final IParaViewWebClient clientRef = client;
						if (clientRef != null) {
							try {
								clientRef.event(viewId, normalizedX, normalizedY, event.action, event.getState()).get();
							} catch (InterruptedException | ExecutionException e) {
								// The event could not be processed.
							}
						}

						// Refresh the Canvas.
						final ParaViewCanvas canvasRef = canvas;
						if (refreshCanvas && canvasRef != null) {
							canvas.refresh();
						}
					}
					return;
				}
			});
		}
		return;
	}

}
