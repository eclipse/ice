package org.eclipse.ice.client.widgets.jme;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;

/**
 * A <code>ViewAppState</code> is a custom {@link SimpleAppState} designed to
 * plug into a core {@link MasterApplication}. Each <code>ViewAppState</code>
 * can request multiple {@link EmbeddedView}s from the
 * <code>MasterApplication</code> and can embed these views in an SWT
 * {@link Composite}.
 * 
 * <p>
 * The standard lifecycle of a <code>ViewAppState</code> is as follows:
 * <ol>
 * <li>A <code>MasterApplication</code> already exists and is initialized.</li>
 * <li>A new <code>ViewAppState</code> is initialized.</li>
 * <li>The <code>ViewAppState</code> is started (see {@link #start()}).</li>
 * <li>The view is embedded in a <code>Composite</code> (see
 * {@link #createComposite(Composite)}).</li>
 * <li>The embedded <code>Composite</code> is disposed.</li>
 * <li>The <code>ViewAppState</code> is stopped (see {@link #stop()}).</li>
 * </ol>
 * Each <code>ViewAppState</code> can be simultaneously embedded in multiple
 * <code>Composite</code>s, and each <code>ViewAppState</code> should support
 * the ability to be stopped and restarted at a later time.
 * </p>
 * 
 * @author Jordan
 * 
 */
public abstract class ViewAppState extends CompositeAppState implements
		IEmbeddedViewClient {

	// ---- Basic features that cannot be changed ---- //
	/**
	 * The root <code>Node</code> for this view's scene graph.
	 */
	protected final Node rootNode;
	/**
	 * The root <code>Node</code> for this view's GUI or HUD. This is the
	 * <code>Node</code> to which <code>ViewPort</code>s can be attached.
	 * Customizations to the HUD should be added to {@link #guiNode}.
	 */
	private final Node rootGuiNode;
	/**
	 * The <code>Node</code> for this view's GUI or HUD. Any customizations for
	 * a HUD should be added to this.
	 */
	protected final Node guiNode;
	// ----------------------------------------------- //

	/**
	 * The ID of this <code>ViewAppState</code>. This should be unique among all
	 * other <code>ViewAppState</code>s rendered in the master {@link #app}.
	 */
	private int id;
	/**
	 * The <code>MasterApplication</code> that renders this
	 * <code>ViewAppState</code>.
	 */
	private MasterApplication app;

	// ---- Customizable features ---- //
	/**
	 * Whether or not to display the {@link #guiNode} (HUD).
	 */
	private final AtomicBoolean displayHUD;
	/**
	 * A <code>Node</code> that contains axes. This should be configured in
	 * {@link #initAxes()}.
	 */
	protected final Node axes;
	/**
	 * Whether or not to display the {@link #axes}.
	 */
	private final AtomicBoolean displayAxes;
	// ------------------------------- //

	// ---- EmbeddedViews ---- //
	/**
	 * The <code>EmbeddedView</code> currently associated with this
	 * <code>ViewAppState</code>. <b>Currently, we only support having a single
	 * <code>EmbeddedView</code>. A sub-class that supports multiple
	 * <code>EmbeddedView</code>s for one scene should provide a new
	 * <code>IEmbeddedViewClient</code> for each view.</b>
	 */
	private EmbeddedView embeddedView;

	// ----------------------- //

	/**
	 * The default constructor.
	 */
	public ViewAppState() {

		// Initialize the other basic, unchangeable features.
		rootNode = new Node();
		rootGuiNode = new Node();
		guiNode = new Node("HUD");

		// Initialize the customizable features.
		displayHUD = new AtomicBoolean(true);
		axes = new Node("axes");
		displayAxes = new AtomicBoolean(true);

		return;
	}

	/**
	 * Creates a new SWT {@link Composite} with this <code>ViewAppState</code>
	 * embedded within it. This method gets an {@link EmbeddedView} from the
	 * {@link MasterApplication}, connects with that view, and embeds that view
	 * in the <code>Composite</code>.
	 * 
	 * @param parent
	 *            The parent <code>Composite</code>.
	 * @return The <code>Composite</code> that has an embedded jME view managed
	 *         by this <code>ViewAppState</code>. This <code>Composite</code>'s
	 *         layout should be set by the caller. <b>This
	 *         <code>Composite</code> should be disposed when it is no longer
	 *         required</b>.
	 */
	public Composite createComposite(Composite parent) {

		// Set the default return value and check the parameter.
		Composite composite = null;
		if (parent != null && embeddedView == null) {

			// Get an EmbeddedView that uses the app for rendering.
			embeddedView = app.getEmbeddedView();

			// Create the embedded Composite. When it is disposed, the
			// associated EmbeddedView should be released.
			composite = new Composite(parent, SWT.EMBEDDED) {
				@Override
				public void dispose() {
					// Dispose of resources tied with this specific view.
					disposeView(embeddedView);
					super.dispose();
				}
			};
			// Create the AWT frame inside the SWT.EMBEDDED Composite.
			Frame embeddedFrame = SWT_AWT.new_Frame(composite);
			// Add the AwtPanel to the embedded AWT Frame. The panel needs to
			// fill the Frame.
			embeddedFrame.setLayout(new BorderLayout());
			// Attach the EmbeddedView to the embedded Frame.
			embeddedView.addToEmbeddedFrame(embeddedFrame);
			embeddedFrame.pack();
			embeddedFrame.setVisible(true);

			// Wait for a maximum of 5 seconds if the view has not been
			// initialized yet. The ViewAppState must be initialized!!!
			int limit = 5000;
			int count = 0;
			while (!isInitialized() && count < limit) {
				try {
					Thread.sleep(50);
					count += 50;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Register with the EmbeddedView.
			embeddedView.registerViewClient(this);

			// Make sure the controls are enabled.
			enableControls();
		}

		return composite;
	}

	/**
	 * Exports the view to an image file. The user is prompted for the image
	 * location.
	 */
	public void exportImage() {
		if (embeddedView != null) {
			// Make the array of strings needed to pass to the file dialog.
			String[] extensionStrings = new String[] { ".png" };

			// Create the file save dialog.
			FileDialog fileDialog = new FileDialog(Display.getCurrent()
					.getActiveShell(), SWT.SAVE);
			fileDialog.setFilterExtensions(extensionStrings);
			fileDialog.setOverwrite(true);

			// Get the path of the new/overwritten image file.
			String path = fileDialog.open();
			if (path != null) {
				embeddedView.exportImage(new File(path));
			}
		}
		return;
	}

	/**
	 * Disposes of the specified <code>EmbeddedView</code>. This should only be
	 * called when the container for the <code>EmbeddedView</code> is disposed..
	 * 
	 * @param embeddedView
	 *            The view that should be released.
	 */
	private void disposeView(EmbeddedView embeddedView) {

		// We must remove the EmbeddedView and any associated objects.
		if (embeddedView == this.embeddedView && embeddedView != null) {
			embeddedView.unregisterViewClient(this);
			embeddedView.removeFromEmbeddedFrame();
			this.embeddedView = null;
		}

		return;
	}

	// ---- Starting and stopping ---- //

	/**
	 * Overrides the default behavior to do nothing. You should call
	 * {@link #start(MasterApplication)} instead.
	 */
	@Override
	public void start(Application app) {
		// Do nothing.
	}

	/**
	 * Starts the <code>ViewAppState</code> by attaching it to the specified
	 * <code>MasterApplication</code>.
	 * 
	 * @param app
	 *            The jME-based <code>MasterApplication</code> that will be
	 *            hosting this <code>ViewAppState</code>.
	 */
	public void start(MasterApplication app) {

		// Do not proceed if the AppState is initialized or the parameter is
		// null.
		if (!isInitialized() && app != null) {
			// Set the app and get the next available ID.
			this.app = app;
			this.id = app.getNextId();

			// Update the names of the root and GUI Node in the
			// MasterApplication's scene graph.
			rootNode.setName("root-" + id);
			guiNode.setName("gui-" + id);

			// Now proceed with the normal start procedure.
			super.start(app);
		}

		return;
	}

	/**
	 * This method detaches from the {@link #embeddedView} before performing the
	 * default stop operation.
	 */
	@Override
	public void stop() {

		if (isInitialized()) {
			// If the EmbeddedView exists, we should disconnect from it first so
			// the AwtPanelsContext does not try to update/render this
			// ViewAppState's scene.
			disposeView(embeddedView);
			// Continue with the default stop operation.
			super.stop();
		}

		return;
	}

	// ------------------------------- //

	// ---- Initialization methods ---- //
	/**
	 * Initializes the <code>ViewAppState</code>.
	 * <p>
	 * <b>Note:</b> If this method is overridden, then the first call from the
	 * sub-class should be <code>super.initAppState();</code> in order to
	 * properly initialize the default <code>SimpleAppState</code> and
	 * <code>ViewAppState</code> features.
	 * </p>
	 */
	@Override
	protected void initAppState() {
		// Proceed with the default initialize method.
		super.initAppState();

		// If the app should display the axes by default, attach the axes to the
		// scene graph.
		if (getDisplayAxes()) {
			rootNode.attachChild(axes);
		}
		// Create the axes.
		initAxes();

		// If the app should display the HUD by default, attach the guiNode to
		// the Application.
		if (getDisplayHUD()) {
			rootGuiNode.attachChild(guiNode);
		}

		// Attach this view's root node and GUI node to the application's
		// respective nodes.
		this.app.getRootNode().attachChild(rootNode);
		this.app.getGuiNode().attachChild(rootGuiNode);

		return;
	}

	/**
	 * Initialize the axes displayed in the scene. This method should be
	 * overridden if custom axes are desired. You can toggle the axes by using
	 * {@link #setDisplayAxes(boolean)}.
	 * <p>
	 * If this method is overridden, you should also override
	 * {@link #clearAxes()} to dispose of them properly when the
	 * <code>ViewAppState</code> is cleaned.
	 * </p>
	 */
	protected void initAxes() {

		// If necessary, add spatials to the axes.
		Geometry geometry;
		Line line;
		float lineWidth = 3f;

		// Create the materials for the axis. In a sub-class, this would be done
		// in initMaterials(). However, adding the materials here is easier for
		// sub-classes because there would be a dependency between initAxes()
		// and initMaterials().
		setMaterial("XAxis", createBasicMaterial(ColorRGBA.Red));
		setMaterial("YAxis", createBasicMaterial(ColorRGBA.Green));
		setMaterial("ZAxis", createBasicMaterial(ColorRGBA.Blue));

		// Create the x axis as a red line of unit length.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_X);
		line.setLineWidth(lineWidth);
		geometry = new Geometry("x", line);
		geometry.setMaterial(getMaterial("XAxis"));
		axes.attachChild(geometry);

		// Create the y axis as a green line of unit length.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_Y);
		line.setLineWidth(lineWidth);
		geometry = new Geometry("y", line);
		geometry.setMaterial(getMaterial("YAxis"));
		axes.attachChild(geometry);

		// Create the z axis as a blue line of unit length.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_Z);
		line.setLineWidth(lineWidth);
		geometry = new Geometry("z", line);
		geometry.setMaterial(getMaterial("ZAxis"));
		axes.attachChild(geometry);

		return;
	}

	// -------------------------------- //

	// ---- Cleanup methods ---- //
	/**
	 * Disconnects the <code>ViewAppState</code> from the associated
	 * {@link Application}.
	 * <p>
	 * <b>Note:</b> If this method is overridden, then the first call from the
	 * sub-class should be <code>super.cleanupAppState();</code> in order to
	 * properly clean up the default <code>SimpleAppState</code> and
	 * <code>ViewAppState</code> features.
	 * </p>
	 */
	@Override
	public void cleanupAppState() {

		// Detach this view's root node and GUI node from the application's
		// respective nodes.
		app.getRootNode().detachChild(rootNode);
		app.getGuiNode().detachChild(rootGuiNode);

		// Un-initialize all of the initialized components of the ViewAppState.
		clearAxes();

		// Unset the reference to the MasterApplication.
		app = null;

		// Proceed with the default cleanup last.
		super.cleanupAppState();
	}

	/**
	 * Clears the <code>ViewAppState</code>'s axes as set up in
	 * {@link #initAxes()}. If the initialization method was overridden, so
	 * should this method.
	 */
	protected void clearAxes() {
		// Remove all default spatials under the "axes" Node.
		axes.detachChildNamed("x");
		axes.detachChildNamed("y");
		axes.detachChildNamed("z");
	}

	// ------------------------- //

	// ---- Getters and Setters ---- //
	/**
	 * Gets the current jME-based <code>MasterApplication</code> to which this
	 * <code>ViewAppState</code> is attached.
	 * 
	 * @return The current jME-based master {@link #app}. This will be null if
	 *         the <code>SimpleAppState</code> has not been started.
	 */
	protected MasterApplication getMasterApplication() {
		return app;
	}

	/**
	 * Gets the <code>ViewAppState</code>'s root <code>Node</code>. Any updates
	 * to this value should be done on the render thread if the
	 * <code>ViewAppState</code> is initialized.
	 * 
	 * @return The <code>ViewAppState</code>'s root <code>Node</code>.
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * @return Whether or not the HUD ({@link #guiNode}) should be displayed.
	 * @see #setDisplayHUD(boolean)
	 */
	public boolean getDisplayHUD() {
		return displayHUD.get();
	}

	/**
	 * Sets whether or not to show the HUD ({@link #guiNode}). If the value
	 * changes, this will enable/disable the HUD in the rendering thread.
	 * 
	 * @param display
	 *            Whether or not to show the HUD.
	 * @see #getDisplayHUD()
	 */
	public void setDisplayHUD(final boolean display) {
		// If the value has changed, we need to update the Application's guiNode
		// by either attaching or detaching this view's guiNode.
		if (displayHUD.compareAndSet(!display, display)) {
			app.enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					if (display) {
						rootGuiNode.attachChild(guiNode);
					} else {
						rootGuiNode.detachChild(guiNode);
					}
					return true;
				}
			});
		}
		return;
	}

	/**
	 * @return Whether or not the {@link #axes} should be displayed.
	 * @see #setDisplayAxes(boolean)
	 */
	public boolean getDisplayAxes() {
		return displayAxes.get();
	}

	/**
	 * Sets whether or not to show the {@link #axes}. If the value changes, this
	 * will enable/disable the axes in the rendering thread.
	 * 
	 * @param display
	 *            Whether or not to show the axes.
	 * @see #getDisplayAxes()
	 */
	public void setDisplayAxes(final boolean display) {
		// If the value has changed, we need to update the Application's scene
		// by either attaching or detaching the axis Spatials.
		if (displayAxes.compareAndSet(!display, display)) {
			app.enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					if (display) {
						// Attach the axes to the root Node.
						rootNode.attachChild(axes);
					} else {
						// Detach the axes from the root Node.
						rootNode.detachChild(axes);
					}
					return true;
				}
			});
		}
		return;
	}

	/**
	 * @return The current <code>EmbeddedView</code> associated with this
	 *         <code>ViewAppState</code>.
	 */
	protected EmbeddedView getEmbeddedView() {
		return embeddedView;
	}

	// ----------------------------- //

	// ---- Implements IEmbeddedViewClient ---- //
	/**
	 * Returns the {@link #rootNode}.
	 */
	@Override
	public Node getSceneRoot(EmbeddedView view) {
		return rootNode;
	}

	/**
	 * Returns the {@link #rootGuiNode}, which should be attached to a ViewPort.
	 * Attach custom spatials to the {@link #guiNode}, which can be toggled with
	 * {@link #setDisplayHUD(boolean)}.
	 */
	@Override
	public Node createHUD(EmbeddedView view) {
		// We do not yet need a custom HUD, so just return the default guiNode.
		return rootGuiNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.IEmbeddedViewClient#updateHUD(org.
	 * eclipse.ice.client.widgets.jme.EmbeddedView, int, int)
	 */
	@Override
	public void updateHUD(EmbeddedView view, int width, int height) {
		// Nothing to do yet.
	}

	/**
	 * Detaches all children from the GUI/HUD <code>Node</code>,
	 * {@link #guiNode}.
	 */
	@Override
	public void disposeHUD(EmbeddedView view) {
		// Detach all children from the guiNode.
		guiNode.detachAllChildren();
	}

	/**
	 * Creates a default {@link FlightCamera} for the specified
	 * <code>EmbeddedView</code>.
	 */
	@Override
	public Object createViewCamera(EmbeddedView view) {
		Object cam = null;

		if (view != null && view == embeddedView) {
			FlightCamera flyCam = new FlightCamera(view.getCamera());
			flyCam.setInputManager(app.getInputManager());
			// The camera should be enabled initially.
			flyCam.setEnabled(true);
			cam = flyCam;
		}

		return cam;
	}

	/**
	 * Enables or disables the default {@link FlightCamera} associated with the
	 * <code>EmbeddedView</code>.
	 */
	@Override
	public void updateViewCamera(EmbeddedView view, boolean enabled) {

		if (view != null && view == embeddedView) {
			FlightCamera flyCam = (FlightCamera) view.getViewCamera();
			flyCam.setEnabled(enabled);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.IEmbeddedViewClient#disposeViewCamera
	 * (org.eclipse.ice.client.widgets.jme.EmbeddedView)
	 */
	@Override
	public void disposeViewCamera(EmbeddedView view) {

		if (view != null && view == embeddedView) {
			// Cast the camera to a FlightCamera, which is the default.
			FlightCamera flyCam = (FlightCamera) view.getViewCamera();
			// Dispose the camera.
			flyCam.dispose();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.IEmbeddedViewClient#viewResized(org
	 * .eclipse.ice.client.widgets.jme.EmbeddedView, int, int)
	 */
	@Override
	public void viewResized(EmbeddedView view, int width, int height) {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.IEmbeddedViewClient#viewActivated(
	 * org.eclipse.ice.client.widgets.jme.EmbeddedView)
	 */
	@Override
	public void viewActivated(EmbeddedView view) {
		if (view != null && view == embeddedView) {
			// Activate the controls.
			enableControls();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.IEmbeddedViewClient#viewDeactivated
	 * (org.eclipse.ice.client.widgets.jme.EmbeddedView)
	 */
	@Override
	public void viewDeactivated(EmbeddedView view) {
		if (view != null && view == embeddedView) {
			// Deactivate the controls.
			disableControls();
		}
	}

	// ---------------------------------------- //

	// ---- Utility methods ---- //
	/**
	 * Gets a Ray from the crosshair's location to the grid. This uses the
	 * camera's position and direction.
	 * 
	 * @return A Ray derived from the crosshair's location.
	 */
	public Ray getCrosshairRay() {
		return (embeddedView != null ? getCrosshairRay(embeddedView.getCamera())
				: null);
	}

	/**
	 * Gets a Ray from the camera to the cursor. The Ray can be used to
	 * determine where on the grid the cursor is pointed.
	 * <p>
	 * <b>Use this method when getting the cursor's location after a mouse-move
	 * event. For mouse-click events, use {@link #getCursorRayFromClick()}.</b>
	 * </p>
	 * 
	 * @return A Ray derived from the cursor's location.
	 */
	public Ray getCursorRay() {
		return (embeddedView != null ? getCursorRay(embeddedView.getCamera())
				: null);
	}

	/**
	 * Gets a Ray from the camera to the cursor. The Ray can be used to
	 * determine where on the grid the cursor is pointed.
	 * <p>
	 * <b>During click events, jME3 calculates the 2D cursor position as
	 * Y-decreasing from top to bottom. Use this method when getting the
	 * cursor's location after a mouse-click event. For mouse-move events, use
	 * {@link #getCursorRay()}.</b>
	 * </p>
	 * 
	 * @return A Ray derived from the cursor's location.
	 */
	public Ray getCursorRayFromClick() {
		return getCursorRayFromClick(embeddedView);
	}
	// ------------------------- //
}
