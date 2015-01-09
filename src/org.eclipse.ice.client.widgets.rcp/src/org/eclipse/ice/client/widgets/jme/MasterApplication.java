package org.eclipse.ice.client.widgets.jme;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapFont;
import com.jme3.system.AppSettings;
import com.jme3.system.Natives;
import com.jme3.system.JmeSystem;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;

/**
 * A MasterApplication is a {@link SimpleApplication} that provides a rendering
 * functionality for jME-based scenes. Typical (and suggested) workflow for
 * using a MasterApplication is as follows:
 * 
 * <ol>
 * <li>
 * Instantiate a MasterApplication with {@link #createApplication()}.</li>
 * <li>
 * Create an AppState (see {@link ViewAppState}).</li>
 * <li>
 * Attach the AppState to this MasterApplication. (see
 * {@link ViewAppState#start()}).</li>
 * <li>
 * Acquire an {@link EmbeddedView} via {@link #getEmbeddedView()}. This view can
 * be used to display the AppState's scene. (see
 * {@link ViewAppState#createComposite(org.eclipse.swt.widgets.Composite)})</li>
 * <li>
 * When finished with the EmbeddedView, release it via
 * {@link MasterApplication#releaseEmbeddedView(EmbeddedView)}. (for a
 * ViewAppState, this happens when you dispose its Composite)</li>
 * <li>
 * When finished with the AppState, detach it (see {@link ViewAppState#stop()}).
 * </li>
 * <li>
 * When finished with the MasterApplication, call {@link #stop()}.</li>
 * </ol>
 * 
 * Any number of EmbeddedViews can be created and used for a single AppState
 * provided all EmbeddedViews are released to the MasterApplication before the
 * AppState is detached.<br>
 * <br>
 * MasterApplication is built on {@link AwtPanel}s and the
 * {@link AwtPanelsContext}. AwtPanels (and any associated objects contained in
 * the EmbeddedView class) are reused when possible.
 * 
 * @author Jordan Deyton
 * 
 */
public class MasterApplication extends SimpleApplication {

	/**
	 * A dummy AwtPanel. This is used as the AwtPanelsContext's (see
	 * {@link #getContext()}) input source when no AwtPanel is currently
	 * visible. It is also used as a workaround to keep the mouse from
	 * recentering on the previously focused AwtPanel: when the focus is lost,
	 * the dummy panel is moved to the mouse's current location and is set as
	 * the context's input source.
	 */
	private AwtPanel dummyPanel;

	/**
	 * A counter used to give unique IDs to client views. Client views or
	 * {@link ViewAppStates} should call {@link #getNextId()}.
	 */
	private final AtomicInteger viewId;

	/**
	 * A List of {@link EmbeddedView}s that are currently in use.
	 */
	private final List<EmbeddedView> usedViews;

	/**
	 * A Queue of {@link EmbeddedView}s that are not currently in use. We use a
	 * FIFO structure for LRU access, i.e. re-use the least recently used
	 * EmbeddedView.
	 */
	private final Queue<EmbeddedView> unusedViews;

	/**
	 * When any particular AwtPanel for an EmbeddedView is used, it must be set
	 * as the application's {@link AwtPanelsContext}'s input source. This
	 * listener does just that. This focus listener should be added to every
	 * AwtPanel that is created.
	 */
	private final FocusListener focusListener;

	/**
	 * Whether or not the Application has initialized.
	 */
	private final AtomicBoolean initialized;

	/**
	 * The default constructor. You should use {@link #createApplication()}
	 * instead. Only use this <b>if you know what you are doing</b>.
	 */
	public MasterApplication() {
		// Do not use a FlyCamAppState. We can attach our own FlyCam later if
		// necessary.
		super(new StatsAppState(), new DebugKeysAppState());

		// Initialize the integer used to give unique IDs to client views.
		viewId = new AtomicInteger();

		// Create the collections of used and unused views.
		usedViews = new ArrayList<EmbeddedView>();
		unusedViews = new LinkedList<EmbeddedView>();

		// Create the FocusListener responsible for setting the
		// AwtPanelsContext's input source.
		focusListener = new FocusListener() {
			public void focusGained(FocusEvent e) {
				// If one of the AwtPanels gained focus, set it as the
				// AwtPanelsContext input source (otherwise the context will get
				// angry... you wouldn't like it when it's angry).
				AwtPanel panel = (AwtPanel) e.getComponent();
				if (panel != null) {
					((AwtPanelsContext) getContext()).setInputSource(panel);
				}

				return;
			}

			public void focusLost(FocusEvent e) {
				/*
				 * There is a feature of the AwtPanelsContext's AwtMouseInput
				 * where, if the input source AwtPanel loses focus, the mouse is
				 * re-centered on the AwtPanel. This is affected by calls to
				 * inputManager.setCursorVisible(true). Unfortunately, we must
				 * keep the cursor visible except when dragging to rotate.
				 */

				/*
				 * This is a problem when the AwtPanel loses focus to an SWT
				 * control. To keep the cursor from being moved back to the
				 * center of the AwtPanel, we move the dummy panel to the
				 * cursor's current location and set the input source to the
				 * dummy panel when the other AwtPanel loses focus.
				 */
				if (e.getOppositeComponent() == null) {
					Point p = MouseInfo.getPointerInfo().getLocation();
					dummyPanel.setSize(0, 0);
					dummyPanel.setLocation(p);
					((AwtPanelsContext) getContext())
							.setInputSource(dummyPanel);
				}

				return;
			}
		};

		// Create the initialized flag.
		initialized = new AtomicBoolean(false);

		return;
	}

	/**
	 * Creates a default MasterApplication that runs in the background. To stop
	 * the application, its {@link #stop()} method must be called.
	 * 
	 * @return A pre-configured MasterApplication.
	 */
	public static MasterApplication createApplication() {
		// Create the new application.
		final MasterApplication application = new MasterApplication();

		// Configure the application settings.
		AppSettings settings = new AppSettings(true);
		settings.setFrameRate(80);
		settings.setCustomRenderer(AwtPanelsContext.class);
		// Disable audio. There is a bug where setting the audio renderer to
		// null prevents jME from extracting the native LWJGL binaries. To get
		// around this, you can manually extract them first. This is drawn from
		// http://hub.jmonkeyengine.org/forum/topic/setting-audio-renderer-to-null-results-in-a-failure-to-load-lwjgl-native-library/
		try {
			Natives.extractNativeLibs(JmeSystem.getPlatform(), settings);
		} catch (IOException e) {
			throw new RuntimeException("MasterApplication error: "
					+ "Cannot load native libraries.");
		}
		settings.setAudioRenderer(null);
		application.setSettings(settings);
		application.setPauseOnLostFocus(false);

		// Create the application's canvas.
		application.createCanvas();

		// Start the canvas in an AWT thread. This starts the renderer thread
		// (and the associated audio and timer thread).
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				application.startCanvas();
			}
		});

		// Set the system listener for the application's context.
		AwtPanelsContext context = (AwtPanelsContext) application.getContext();
		context.setSystemListener(application);

		// We need to call context.setInputSource() at some point, or we will
		// get NullPointerExceptions.
		application.dummyPanel = context.createPanel(PaintMode.OnRequest);
		context.setInputSource(application.dummyPanel);

		return application;
	}

	/**
	 * Disposes all resources directly managed by the
	 * <code>MasterApplication</code>.
	 */
	public void stop() {
		super.stop();

		// Dispose all used EmbeddedViews.
		for (EmbeddedView view : usedViews) {
			view.getRenderPanel().removeFocusListener(focusListener);
			view.dispose();
		}
		usedViews.clear();

		// Dispose all unused EmbeddedViews.
		for (EmbeddedView view : unusedViews) {
			view.getRenderPanel().removeFocusListener(focusListener);
			view.dispose();
		}
		unusedViews.clear();

		return;
	}

	/**
	 * Gets the next available unique integer associated with views or
	 * {@link ViewAppState}s. This is used to keep two top-level
	 * <code>Node</code>s attached to the root <code>Node</code> from having the
	 * same name.
	 * 
	 * @return The next available ID for a jME view that uses this
	 *         <code>MasterApplication</code>.
	 */
	public int getNextId() {
		return viewId.getAndIncrement();
	}

	/**
	 * Gets an {@link EmbeddedView} that uses this Application for rendering a
	 * jME-based scene. This view cannot be used with more than one SWT
	 * Composite with an embedded AWT Frame. When the view is no longer needed,
	 * the client AppState should call
	 * {@link #releaseEmbeddedView(EmbeddedView)} with the EmbeddedView returned
	 * by this method.
	 * 
	 * @return An EmbeddedView.
	 */
	public EmbeddedView getEmbeddedView() {
		// Try to grab an available view.
		EmbeddedView embeddedView = unusedViews.poll();

		// If none is available, we must use the context to create a new one.
		if (embeddedView == null) {
			// Get the next available ID (note: we do not yet destroy views!)
			int id = usedViews.size();
			// Create a new view.
			embeddedView = new EmbeddedView(this, id);
			// Register the primary focus listener with the new AwtPanel.
			embeddedView.getRenderPanel().addFocusListener(focusListener);
		}

		// Add the panel to the list of used panels.
		usedViews.add(embeddedView);

		return embeddedView;
	}

	/**
	 * Releases an {@link EmbeddedView} for use elsewhere in the program. This
	 * is important because {@link AwtPanel}s cannot yet be easily disposed in
	 * jME.
	 * 
	 * @param embeddedView
	 *            The EmbeddedView that can be recycled for use elsewhere.
	 */
	public void releaseEmbeddedView(EmbeddedView embeddedView) {

		// If we view can be removed from the list of used views, then we should
		// add it to the collection of unused views for re-use later.
		if (usedViews.remove(embeddedView)) {
			// Clean up the EmbeddedView before letting it be re-used elsewhere.
			embeddedView.cleanupView();
			unusedViews.add(embeddedView);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.SimpleApplication#simpleInitApp()
	 */
	@Override
	public void simpleInitApp() {
		// Set the initialized flag to true.
		initialized.set(true);
	}

	/**
	 * @return Whether or not the <code>MasterApplication</code> has been
	 *         initialized yet. Adding any <code>ViewAppState</code>s should
	 *         wait until this has been initialized.
	 * @see #blockUntilInitialized(int)
	 */
	public boolean isInitialized() {
		return initialized.get();
	}

	/**
	 * Blocks the calling thread until the <code>MasterApplication</code> has
	 * been initialized. This method will return either when the app is
	 * initialized or until the limit is reached. By specifying 0 or a negative
	 * number, you can wait indefinitely.
	 * 
	 * @param limit
	 *            The limit, in seconds, to wait until the
	 *            <code>MasterApplication</code> has initialized.
	 * @return True if the <code>MasterApplication</code> was initialized, false
	 *         otherwise.
	 */
	public boolean blockUntilInitialized(int limit) {

		// Whether or not the application was initialized.
		boolean success = false;
		// The limit in milliseconds.
		int msLimit = limit * 1000;
		// How long we have waited.
		int wait = 0;
		// How long to wait for each Thread.sleep() call.
		int step = 50;

		if (limit > 0) {
			// If the limit is positive, wait until the app initialized or the
			// limit is reached.
			while (!(success = initialized.get()) && wait < msLimit) {
				try {
					Thread.sleep(step);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				wait += step;
			}
		} else {
			// If the limit is negative or zero, wait indefinitely.
			while (!(success = initialized.get())) {
				try {
					Thread.sleep(step);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}

	// ---- Getters and Setters ---- //
	/**
	 * @return The <code>SimpleApplication</code>'s GUI font.
	 */
	public BitmapFont getGuiFont() {
		return guiFont;
	}
	// ----------------------------- //
}
