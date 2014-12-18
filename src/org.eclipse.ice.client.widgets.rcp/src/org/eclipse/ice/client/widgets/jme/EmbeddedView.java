package org.eclipse.ice.client.widgets.jme;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;
import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;

/**
 * This class provides a jME-based view that can be embedded within an AWT
 * {@link Frame}. Use of this class requires use of the {@link AwtPanelsContext}
 * in the associated jME {@link Application}.
 * <p>
 * Each embedded view comes with the necessary jME components to be rendered in
 * a AWT <code>Frame</code> (which may be embedded within an SWT
 * <code>Composite</code>), although these components are largely hidden from
 * the client <code>AppState</code>
 * <ul>
 * <li>An {@link AwtPanel} used to render a scene within an AWT
 * <code>Frame</code></li>
 * <li>A {@link ViewPort} that can be attached to the root of a scene graph.</li>
 * <li>A master {@link Camera} associated with the ViewPort.</li>
 * </ul>
 * </p>
 * <p>
 * To use an <code>EmbeddedView</code>, you must do the following:
 * <ol>
 * <li>Instantiate an <code>EmbeddedView</code>.</li>
 * <li>Create an AWT <code>Frame</code> and call
 * {@link #addToEmbeddedFrame(Frame)}.</li>
 * <li>Register an {@link IEmbeddedViewClient} with the
 * <code>EmbeddedView</code> by calling
 * {@link #registerViewClient(IEmbeddedViewClient)}.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Note:</b> For each <code>EmbeddedView</code>, there can be only one
 * embedded <code>Frame</code> and one registered
 * <code>IEmbeddedViewClient</code> at a time.
 * </p>
 * 
 * @author Jordan
 * 
 */
public class EmbeddedView implements SceneProcessor {

	// ---- Constant properties ---- //
	/**
	 * An ID associated with this <code>EmbeddedView</code>. This should be
	 * unique per <code>EmbeddedView</code> within an {@link Application}.
	 */
	private final int id;
	/**
	 * The <code>AwtPanel</code> used to render a jME scene in an AWT
	 * <code>Frame</code>.
	 */
	private final AwtPanel renderPanel;
	/**
	 * The <code>ViewPort</code> associated with this view. Each
	 * <code>ViewPort</code> can be associated with at least one scene graph,
	 * although in our case we use at most one attached scene at a time.
	 */
	private final ViewPort viewPort;
	/**
	 * The <code>ViewPort</code> associated with the scene's GUI or HUD
	 * {@link Node}.
	 */
	private final ViewPort guiViewPort;
	/**
	 * A <code>Camera</code> associated with the scene graph's root {@link Node}
	 * and {@link #viewPort}.
	 */
	private final Camera camRenderer;
	/**
	 * The <code>Application</code> that is responsible for rendering this view.
	 */
	private final Application app;

	/**
	 * This listens for focus events from the {@link #renderPanel} and forwards
	 * them to the {@link #client} so the client can activate or deactivate its
	 * camera and any other focus-dependent properties.
	 */
	private final FocusListener focusListener;
	/**
	 * This listens for component change events from the {@link #renderPanel}
	 * and forwards them to the {@link #client} to update its HUD and any other
	 * component- (size-) dependent properties.
	 */
	private final ComponentListener componentListener;
	// ----------------------------- //

	// ---- AWT Resize Event throttling ---- //
	/**
	 * A reference to the current AWT <code>Component</code> resize event. The
	 * value may be null.
	 */
	private final AtomicReference<ComponentEvent> resizeEvent;
	/**
	 * The currently executing Thread that throttles AWT <code>Component</code>
	 * resize events to keep from clogging the render thread when the
	 * {@link #client} is notified that the {@link #renderPanel} has changed
	 * size.
	 */
	private Thread resizeThread;
	// ------------------------------------- //

	// ---- Client-dependent properties ---- //
	/**
	 * The current client that is using this <code>EmbeddedView</code>. Only one
	 * client can be used at a time.
	 */
	private IEmbeddedViewClient client;
	/**
	 * The interactive camera used by the client.
	 */
	private Object cam;
	/**
	 * The GUI or HUD <code>Node</code> for this particular view. This is stored
	 * here because the HUD's coordinates must be changed when the
	 * {@link #renderPanel}'s size changes.
	 */
	private Node HUD;

	/**
	 * The current AWT <code>Frame</code> used to render the
	 * <code>EmbeddedView</code>. This is an <code>AtomicReference</code>
	 * because the {@link #resizeThread} uses it to determine if the view is
	 * currently embedded.
	 */
	private final AtomicReference<Frame> embeddedFrame;

	// ------------------------------------- //

	// ---- Screenshot variables ---- //
	/**
	 * The renderer associated with the {@link #viewPort}.
	 */
	private Renderer renderer;
	/**
	 * The output buffer used to generate a screenshot image from the
	 * {@link #renderer}.
	 */
	private ByteBuffer outputBuffer;
	/**
	 * The AWT image that will be written to the file as the screenshot.
	 */
	private BufferedImage image;
	/**
	 * The current width of the {@link #viewPort}.
	 */
	private int width;
	/**
	 * The current height of the {@link #viewPort}.
	 */
	private int height;
	/**
	 * Whether or not to take a screenshot on the next render pass.
	 */
	private final AtomicBoolean takeScreenshot = new AtomicBoolean(false);
	/**
	 * The file to which the screenshot will be written.
	 */
	private File screenshotFile;

	// ------------------------------ //

	/**
	 * The default constructor. <b>The ID should be unique for this view and
	 * application!</b>
	 * 
	 * @param app
	 *            The <code>Application</code> responsible for rendering scene
	 *            graphs.
	 * @param id
	 *            The unique ID of this view.
	 */
	public EmbeddedView(Application app, int id) {

		// Set the unique ID.
		this.id = id;

		// Store a reference to the Application.
		this.app = app;

		// Create an AwtPanel for the Application.
		renderPanel = ((AwtPanelsContext) app.getContext())
				.createPanel(PaintMode.Accelerated);

		// Create an AtomicReference to point to the current Frame in which this
		// view is embedded.
		embeddedFrame = new AtomicReference<Frame>(null);

		// Create a copy of the app's default camera.
		camRenderer = new Camera(1, 1);
		camRenderer.copyFrom(app.getCamera());

		// Get the app's RenderManager for creating the ViewPorts.
		RenderManager renderManager = app.getRenderManager();

		// Create the primary ViewPort. Client AppStates should attach this
		// ViewPort to their root Node.
		viewPort = renderManager.createMainView("view-" + id, camRenderer);
		viewPort.setClearFlags(true, true, true);
		renderPanel.attachTo(false, viewPort);
		// Add this to the main ViewPort as a SceneProcessor to let us take
		// screenshots of the EmbeddedView.
		viewPort.addProcessor(this);

		// Create the guiNode ViewPort. This is used for the HUD.
		guiViewPort = renderManager
				.createMainView("guiView-" + id, camRenderer);
		renderPanel.attachTo(false, guiViewPort);

		// Create a FocusListener to mark the focused AwtPanel as the input
		// source for the application.
		focusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent event) {
				// We remove the focus listener when no client is attached, but
				// it is possible for the focus listener to be removed before a
				// focus event is processed. Thus, check for a null client.
				if (client != null) {
					// Enable the interactive camera for this view.
					client.updateViewCamera(EmbeddedView.this, true);
					// Notify the client that this view has been activated.
					client.viewActivated(EmbeddedView.this);
				}
			}

			@Override
			public void focusLost(FocusEvent event) {
				// We remove the focus listener when no client is attached, but
				// it is possible for the focus listener to be removed before a
				// focus event is processed. Thus, check for a null client.
				if (client != null) {
					// Disable the interactive camera for this view.
					client.updateViewCamera(EmbeddedView.this, false);
					// Notify the client that this view has been deactivated.
					client.viewDeactivated(EmbeddedView.this);
				}
			}
		};

		// Create a ComponentListener to notify the client when the window has
		// resized. Currently, this listener just updates resizeEvent when such
		// an event has occurred. The resizeThread reads this event periodically
		// and passes a notification to the client.
		resizeEvent = new AtomicReference<ComponentEvent>(null);
		componentListener = new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent event) {
				// Nothing to do.
			}

			@Override
			public void componentMoved(ComponentEvent event) {
				// Nothing to do.
			}

			@Override
			public void componentResized(ComponentEvent event) {
				resizeEvent.set(event);
			}

			@Override
			public void componentShown(ComponentEvent event) {
				// Nothing to do.
			}
		};

		return;
	}

	/**
	 * Registers or configures an {@link IEmbeddedViewClient} for this
	 * <code>EmbeddedView</code>. Each <code>EmbeddedView</code> can be
	 * registered with one client at a time.
	 * 
	 * @param newClient
	 *            The client that will be using this view.
	 * @see #unregisterViewClient(IEmbeddedViewClient)
	 */
	public void registerViewClient(final IEmbeddedViewClient newClient) {

		if (client == null && newClient != null) {
			// Get a final reference to this EmbeddedView for the callable.
			final EmbeddedView view = this;

			// Set the reference to the client.
			client = newClient;

			// The below operations must be performed on the render thread.
			app.enqueue(new Callable<Boolean>() {
				public Boolean call() {

					// Attach the scene root to the main ViewPort.
					viewPort.attachScene(client.getSceneRoot(view));

					// Create the HUD.
					HUD = client.createHUD(view);
					if (embeddedFrame != null) {
						client.updateHUD(view, renderPanel.getWidth(),
								renderPanel.getHeight());
					}
					// Attach the HUD to the GUI ViewPort.
					guiViewPort.attachScene(HUD);

					// Create the camera.
					cam = client.createViewCamera(view);

					// Add the focus and component listener since the client
					// is set. This does not have to be done on the render
					// thread, but it's not worth adding an extra thread to wait
					// to do this until after the future value returns.
					renderPanel.addFocusListener(focusListener);
					renderPanel.addComponentListener(componentListener);

					return true;
				}
			});
		}

		return;
	}

	/**
	 * Unregisters an {@link IEmbeddedViewClient} from this
	 * <code>EmbeddedView</code>.
	 * 
	 * @param oldClient
	 *            The client that will no longer be using this view.
	 * @see #registerViewClient(IEmbeddedViewClient)
	 */
	public void unregisterViewClient(IEmbeddedViewClient oldClient) {

		if (client != null && oldClient == client) {
			// Get a final reference to this EmbeddedView for the callable.
			final EmbeddedView view = this;

			// Remove the focus and component listeners.
			renderPanel.removeFocusListener(focusListener);
			renderPanel.removeComponentListener(componentListener);

			// Create a final reference to the client and unset the class'
			// reference. This is so the client != null check in
			// removeEmbeddedFrame() does not pass before the queued Callable
			// completes.
			final IEmbeddedViewClient client = this.client;
			this.client = null;

			// The below operations must be performed on the render thread.
			app.enqueue(new Callable<Boolean>() {
				public Boolean call() {
					// Detach the scene root from the main ViewPort.
					viewPort.detachScene(client.getSceneRoot(view));

					// Detach the HUD from the GUI ViewPort.
					guiViewPort.detachScene(HUD);
					// Dispose the HUD.
					client.disposeHUD(view);
					HUD = null;

					// Dispose the camera. Disable it first to unregister it
					// from the InputManager.
					client.updateViewCamera(view, false);
					client.disposeViewCamera(view);
					cam = null;

					return true;
				}
			});
		}

		return;
	}

	/**
	 * Adds this view to an AWT <code>Frame</code> to be rendered in an AWT- or
	 * SWT-based program. <b>Note:</b> A single <code>EmbeddedView</code> cannot
	 * be added to multiple <code>Frame</code>s at this time.
	 * 
	 * @param embeddedFrame
	 *            The AWT <code>Frame</code> in which to render this jME-based
	 *            view. If null or if the embedded <code>Frame</code> is already
	 *            configured, this operation does nothing. <b>The
	 *            <code>Frame</code> is expected to have a default
	 *            {@link BorderLayout}</b>
	 */
	public void addToEmbeddedFrame(Frame embeddedFrame) {

		// We can only embed this view if the new Frame is not null and the
		// current Frame is null.
		if (embeddedFrame != null
				&& this.embeddedFrame.compareAndSet(null, embeddedFrame)) {

			// Add the render panel to the Frame.
			embeddedFrame.add(renderPanel, BorderLayout.CENTER);

			// Start a thread to throttle panel resize events.
			resizeThread = createResizeThread();
			resizeThread.setDaemon(true);
			resizeThread.start();

			// If necessary, notify the client of the panel's size.
			if (client != null) {
				app.enqueue(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						// Get the width and height of the render panel, then
						// update the client.
						int width = renderPanel.getWidth();
						int height = renderPanel.getHeight();
						client.updateHUD(EmbeddedView.this, width, height);
						client.viewResized(EmbeddedView.this, width, height);

						return true;
					}
				});
			}
		}

		return;
	}

	/**
	 * Removes this view from its current AWT <code>Frame</code>. After this
	 * operation, the view can be added to a different <code>Frame</code>. If a
	 * <code>Frame</code> is not already configured, this operation does
	 * nothing.
	 */
	public void removeFromEmbeddedFrame() {

		// If the current Frame is not already null, set it to null.
		Frame frame = embeddedFrame.getAndSet(null);
		if (frame != null) {
			// Remove the render panel from the Frame.
			frame.remove(renderPanel);

			// Set the resize throttling thread to null. It should terminate on
			// its own now that embeddedFrame is null.
			resizeThread = null;

			// If necessary, disable the client's camera.
			if (client != null) {
				client.updateViewCamera(this, false);
			}
		}

		return;
	}

	/**
	 * Disposes any resources associated with the <code>EmbeddedView</code>.
	 */
	public void dispose() {
		// TODO At the moment, the only thing we can do to "dispose" each
		// EmbeddedView is to detach it from its client. This is done by calling
		// cleanup();
		cleanupView();
	}

	// ---- Getters and Setters ---- //
	/**
	 * Gets the unique ID associated with this <code>EmbeddedView</code>.
	 * 
	 * @return The view's String ID.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets the <code>Camera</code> associated with the scene graph's root
	 * {@link Node} and {@link #viewPort}. This is useful for initializing
	 * custom cameras for a particular scene graph.
	 * 
	 * @return The view's Camera instance.
	 */
	public Camera getCamera() {
		return camRenderer;
	}

	/**
	 * Gets the interactive camera currently associated with this
	 * <code>EmbeddedView</code>. This is the component that is registered with
	 * the {@link InputManager} for the rendering {@link Application}.
	 * 
	 * @return The current interactive camera.
	 */
	public Object getViewCamera() {
		return cam;
	}

	/**
	 * Gets the GUI/HUD <code>Node</code> currently associated with this
	 * <code>EmbeddedView</code>.
	 * 
	 * @return The current HUD <code>Node</code>.
	 */
	public Node getHUD() {
		return HUD;
	}

	// ----------------------------- //

	/**
	 * This operation is used to clean up any resources in case a programmer was
	 * lazy or uninformed and forgot to clean up the <code>EmbeddedView</code>
	 * before releasing it for re-use.
	 */
	protected void cleanupView() {

		// We should make sure the client is unregistered first.
		if (client != null) {
			System.err.println("EmbeddedView error: " + "View \"" + id
					+ "\"'s client was not properly unregistered!");

			// Unregister the view client.
			unregisterViewClient(client);
		}

		// Then we need to make sure the frame is not embedded.
		if (embeddedFrame != null) {
			System.err.println("EmbeddedView error: " + "View \"" + id
					+ "\" was not properly removed from its AWT Frame!");

			// Remove the view from the AWT Frame.
			removeFromEmbeddedFrame();
		}

		// Restore the default camera location.
		camRenderer.copyFrom(app.getCamera());

		return;
	}

	/**
	 * Gets the {@link AwtPanel} used to render this <code>EmbeddedView</code>.
	 * 
	 * @return The <code>AwtPanel</code> used to render a jME scene in an AWT
	 *         <code>Frame</code>.
	 */
	protected AwtPanel getRenderPanel() {
		return renderPanel;
	}

	/**
	 * Creates a <code>Thread</code> that loops while the {@link #renderPanel}
	 * is embedded inside the {@link #embeddedFrame} and handles all of the
	 * panel's resize events written to {@link #resizeEvent}. It pauses for a
	 * period before processing each resize event and does not process the next
	 * one until the render thread responds that the previous notification has
	 * been processed. The resize events are sent to the client.
	 * 
	 * @return A new thread that throttles the resize events received by
	 *         {@link #componentListener}.
	 */
	private Thread createResizeThread() {
		return new Thread() {
			@Override
			public void run() {
				// The minimum amount of time to sleep between each attempt to
				// notify the client of a change in size.
				int sleepTime = 100;

				// Loop as long as the renderPanel is embedded (visible).
				while (embeddedFrame.get() != null) {

					// If there is an unprocessed resize event, process it.
					final ComponentEvent e = resizeEvent.getAndSet(null);
					if (e != null) {

						// Enqueue a notification for the client to the app's
						// render thread.
						try {
							app.enqueue(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									// Get the width and height of the render
									// panel, then update the client.
									int width = renderPanel.getWidth();
									int height = renderPanel.getHeight();

									// Update the camera's size and aspect
									// ratio. This is done so that objects on
									// the edge won't be culled by mistake.
									camRenderer.resize(width, height, true);

									// Tell the client of the resize event.
									client.updateHUD(EmbeddedView.this, width,
											height);
									client.viewResized(EmbeddedView.this,
											width, height);

									return true;
								}
							}).get();
							// The above get() makes sure the thread blocks
							// until the render thread finishes this operation.
						} catch (InterruptedException exception) {
							exception.printStackTrace();
						} catch (ExecutionException exception) {
							exception.printStackTrace();
						}
					}

					// Sleep between each attempt to notify the client.
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException exception) {
						exception.printStackTrace();
					}
				}

				return;
			}
		};
	}

	// ---- Implements SceneProcessor ---- //
	// This interface is used to take screenshots. The code in this section is
	// based off of jME's ScreenshotAppState.

	/**
	 * Cleans up resources used by this class as a {@link SceneProcessor}.
	 */
	@Override
	public void cleanup() {
		renderer = null;
		outputBuffer = null;
		image = null;
	}

	/**
	 * Initializes the view as a {@link SceneProcessor}.
	 */
	@Override
	public void initialize(RenderManager rm, ViewPort vp) {
		if (vp == viewPort) {
			renderer = rm.getRenderer();
			// We already have a reference to the viewport.
			reshape(vp, camRenderer.getWidth(), camRenderer.getHeight());
		}
	}

	/**
	 * Updates the known dimensions of the associated {@link #viewPort}.
	 */
	@Override
	public void reshape(ViewPort vp, int w, int h) {
		if (vp == viewPort) {
			width = w;
			height = h;
			outputBuffer = BufferUtils.createByteBuffer(width * height * 4);
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_4BYTE_ABGR);
		}
	}

	/**
	 * Checks that the view has been initialized as a {@link SceneProcessor}.
	 */
	@Override
	public boolean isInitialized() {
		return renderer != null;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void preFrame(float tpf) {
		// Nothing to do.
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void postQueue(RenderQueue rq) {
		// Nothing to do.
	}

	/**
	 * Takes the screenshot after the frame has been rendered.
	 */
	@Override
	public void postFrame(FrameBuffer out) {
		if (takeScreenshot.compareAndSet(true, false)) {

			// The code between this comment and the next big one comes from
			// com.jme.app.state.ScreenShotAppState.
			int viewX = (int) (camRenderer.getViewPortLeft() * camRenderer
					.getWidth());
			int viewY = (int) (camRenderer.getViewPortBottom() * camRenderer
					.getHeight());
			int viewWidth = (int) ((camRenderer.getViewPortRight() - camRenderer
					.getViewPortLeft()) * camRenderer.getWidth());
			int viewHeight = (int) ((camRenderer.getViewPortTop() - camRenderer
					.getViewPortBottom()) * camRenderer.getHeight());

			renderer.setViewPort(0, 0, width, height);
			renderer.readFrameBuffer(out, outputBuffer);
			renderer.setViewPort(viewX, viewY, viewWidth, viewHeight);

			// The code below is pulled from com.jme3.util.Screenshots.java.
			// The original code has a bug in the outer loop where, for odd
			// height, the screen capture produces a bad horizontal line across
			// the output image. It used y < height / 2, which is the same for
			// each even height and the odd height one greater.
			WritableRaster wr = image.getRaster();
			DataBufferByte db = (DataBufferByte) wr.getDataBuffer();
			byte[] cpuArray = db.getData();

			outputBuffer.clear();
			outputBuffer.get(cpuArray);
			outputBuffer.clear();

			int width = wr.getWidth();
			int height = wr.getHeight();

			// flip the components the way AWT likes them
			for (int y = 0; y < Math.ceil(height * 0.5); y++) {
				int inOffset = y * width;
				int outOffset = (height - y - 1) * width;
				for (int x = 0; x < width; x++) {
					int inPtr = (inOffset + x) * 4;
					int outPtr = (outOffset + x) * 4;

					// Copy the original bgra values.
					byte b1 = cpuArray[inPtr + 0];
					byte g1 = cpuArray[inPtr + 1];
					byte r1 = cpuArray[inPtr + 2];
					byte a1 = cpuArray[inPtr + 3];

					// Move in the new abgr values according to AWT.
					cpuArray[inPtr + 0] = cpuArray[outPtr + 3];
					cpuArray[inPtr + 1] = cpuArray[outPtr + 0];
					cpuArray[inPtr + 2] = cpuArray[outPtr + 1];
					cpuArray[inPtr + 3] = cpuArray[outPtr + 2];

					// Move the original agbr values.
					cpuArray[outPtr + 0] = a1;
					cpuArray[outPtr + 1] = b1;
					cpuArray[outPtr + 2] = g1;
					cpuArray[outPtr + 3] = r1;
				}
			}

			// Write the image to the file.
			try {
				ImageIO.write(image, "png", screenshotFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Unset the screenshot file reference now that we have taken the
			// screenshot.
			screenshotFile = null;
		}

		return;
	}

	// ----------------------------------- //

	/**
	 * Exports the {@code EmbeddedView} as an image file.
	 * 
	 * @param file
	 *            The location to write the file. Any pre-existing image file
	 *            will be overwritten. If null or cannot write to this file,
	 *            nothing will happen.
	 */
	public void exportImage(File file) {
		if (file != null
				&& (!file.exists() || file.isFile() && file.canWrite())) {
			screenshotFile = file;
			takeScreenshot.set(true);
		}
	}
}
