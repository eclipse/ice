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

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This class provides an SWT Canvas that can render images passed through via
 * an {@link IParaViewWebClient}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewCanvas extends Canvas implements PaintListener,
		ControlListener {

	/**
	 * The client used to render meshes remotely. It sends images back that will
	 * be painted onto this Canvas.
	 */
	private IParaViewWebClient client;

	/**
	 * The current view ID on the client.
	 */
	private int viewId = -1;

	/**
	 * The current image acquired from the {@link #client}.
	 */
	private final AtomicReference<Image> image = new AtomicReference<Image>();

	/**
	 * The service used to start worker threads.
	 */
	private final ExecutorService executorService;

	/**
	 * A runnable that can be used to update the Canvas. It queries the
	 * {@link #client}, updates the {@link #image}, and triggers a UI update
	 * based on the new image.
	 * <p>
	 * To avoid multiple running threads (which may conflict since each event
	 * needs to be synced with the UI), we use {@link #stale} to determine
	 * whether the runnable needs to be executed along with {@link #refreshLock}
	 * to prevent the newer thread from starting its real work until after the
	 * running thread completes.
	 * </p>
	 */
	private final Runnable refreshRunnable;

	/**
	 * This lock is used by {@link #refreshRunnable} to determine if the refresh
	 * thread is currently running. In the unlikely--but possible--case that a
	 * second refresh thread gets started, this prevents the refresh threads
	 * from racing.
	 */
	private final Lock refreshLock = new ReentrantLock();

	/**
	 * If true, then the client needs to be queried and the Canvas updated. This
	 * is used to see if the {@link #refreshRunnable} needs to be started or if
	 * it should process another refresh event.
	 */
	private final AtomicBoolean stale = new AtomicBoolean();

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent Composite that will contain this Canvas.
	 * @param style
	 *            The style of the Canvas. By default, the Canvas has the
	 *            {@link SWT#DOUBLE_BUFFERED} style set.
	 */
	public ParaViewCanvas(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);

		// Set up the ExecutorService so we can start threads later.
		executorService = Executors.newSingleThreadExecutor();

		// Set up the Runnable that queries the client and triggers a UI update
		// if a new image is generated.
		refreshRunnable = new Runnable() {
			@Override
			public void run() {

				// Before we start handling refresh events, we need to make sure
				// another refresh thread is not running. This blocks until that
				// thread finishes.
				refreshLock.lock();
				try {

					// Continue as long as the view is stale.
					while (stale.getAndSet(false)) {

						// Get the current size of the Canvas.
						final Point size = new Point(0, 0);
						getDisplay().syncExec(new Runnable() {
							@Override
							public void run() {
								if (!isDisposed()) {
									Point canvasSize = getSize();
									size.x = canvasSize.x;
									size.y = canvasSize.y;
								}
							}
						});

						// Request a new Image from the client.
						final Image newImage = refreshClient(client, viewId,
								size.x, size.y);

						// If a new Image could be retrieved, sync it with the
						// UI thread. Note: We don't need to wait on the UI
						// thread to handle this update.
						if (newImage != null) {
							getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!isDisposed()) {
										image.set(newImage);
										redraw();
									}
								}
							});
						}
					}

				} finally {
					// Notify any pending refresh thread that this thread has
					// finished.
					refreshLock.unlock();
				}

				return;
			}
		};

		// Register for paint and control resize events.
		addPaintListener(this);
		addControlListener(this);

		return;
	}

	public boolean setClient(IParaViewWebClient client) {
		boolean changed = false;
		if (client != this.client) {
			this.client = client;
			changed = true;
		}
		return changed;
	}

	public boolean setViewId(int viewId) {
		boolean changed = false;
		if (viewId != this.viewId) {
			this.viewId = viewId;
			changed = true;
		}
		return changed;
	}

	/**
	 * Triggers a refresh of the Canvas. This method may be called from off the
	 * UI thread.
	 */
	public void refresh() {
		// TODO Remove this.
		System.out.println("refresh() called");

		// Mark the stale flag. If it was unset, then we need to start a
		// new thread to refresh based on the current state of the client.
		if (!stale.getAndSet(true)) {
			executorService.submit(refreshRunnable);
		}

		return;
	}

	/*
	 * Implements a method from PaintListener.
	 */
	@Override
	public void paintControl(PaintEvent e) {
		// Paint the current image onto the Canvas.
		Image image = this.image.get();
		if (image != null) {
			Rectangle imgBounds = image.getBounds();
			e.gc.drawImage(image, 0, 0, imgBounds.width, imgBounds.height, 0,
					0, e.width, e.height);
		}
		return;
	}

	/*
	 * Implements a method from ControlListener.
	 */
	@Override
	public void controlMoved(ControlEvent e) {
		// Nothing to do.
	}

	/*
	 * Implements a method from ControlListener.
	 */
	@Override
	public void controlResized(ControlEvent e) {
		// Trigger an update to the client.
		refresh();
	}

	/**
	 * Sends an update request to the specified client. This operation waits for
	 * the reponse, after which it will post a request for the Canvas to update.
	 * 
	 * @param client
	 *            The client from which to request a new image.
	 * @param viewId
	 *            The ID of the view to render on the client.
	 * @param width
	 *            The width of the Canvas when making the request.
	 * @param height
	 *            The height of the Canvas when making the request.
	 * @return An Image from the client, or {@code null} if the render request
	 *         could not be completed.
	 */
	private Image refreshClient(IParaViewWebClient client, int viewId,
			int width, int height) {

		Image image = null;

		// TODO Remove this.
		System.out.println("refreshClient(...) called");

		if (client != null && width > 0 && height > 0) {

			// The request to draw will return an object containing an encoded
			// image string and a flag stating whether the image is stale.
			String base64Image = null;
			boolean stale = false;

			try {
				// Send a response request.
				JsonObject response = client.render(viewId, 100, width, height)
						.get();

				// Read the base 64 image string from the response.
				JsonElement element = response.get("image");
				if (element != null && element.isJsonPrimitive()) {
					base64Image = element.getAsString();
				}

				// Read whether or not the sent image is stale.
				element = response.get("stale");
				if (element != null && element.isJsonPrimitive()) {
					stale = element.getAsBoolean();
				}

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			// If the returned image is old, trigger another update to the
			// client.
			if (stale) {
				refresh();
			}

			// Draw the new version of the image.
			if (base64Image != null) {
				// TODO When we start using Java 8, replace the
				// DatatypeConverter with the java.util.Base64
				// class.
				byte[] decode = DatatypeConverter
						.parseBase64Binary(base64Image);
				// byte[] decode = Base64.getDecoder().decode(
				// base64Image.getBytes());
				ByteArrayInputStream inputStream = new ByteArrayInputStream(
						decode);

				// Load the input stream into a new Image.
				ImageData[] data = new ImageLoader().load(inputStream);
				if (data.length > 0) {
					image = new Image(getDisplay(), data[0]);
				}
			}
		}

		return image;
	}
}
