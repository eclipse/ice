/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or
 *      initial documentation
 *    Dasha Gorin (UT-Battelle, LLC.) - code and documentation cleanup
 *    Jay Jay Billings (UT-Battelle) - refactor of datastructures bundle
 *******************************************************************************/
package org.eclipse.ice.client.widgets.mesh;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;

/**
 * This class handles queuing {@link MeshComponent} updates for a
 * {@link MeshAppState}. The reason this is handled in this class is two-fold:
 * <p>
 * </p>
 * <p>
 * First, the {@link IUpdateableListener#update(IUpdateable)} method does not
 * give away any more information about the prospective update other than that
 * something has changed. This means that each update from the
 * <code>MeshComponent</code> requires a comprehensive comparison with what is
 * currently displayed in the <code>MeshAppState</code>. In other words, the
 * method implementing the update is heavy-weight, and multiple updates cannot
 * be performed simultaneously.
 * </p>
 * <p>
 * Second, we need to process <i>all</i> updates. Since the update process may
 * take a significant period, it is possible that several consecutive
 * notifications from the MeshComponent arrive while the MeshApplication is
 * already updating. Instead of running the update n times for n calls, only the
 * current update and the last remaining update need to be handled.
 * </p>
 * 
 * @author Jordan Deyton
 * 
 */
public class MeshUpdateHandler implements IUpdateableListener {

	/**
	 * The current <code>MeshComponent</code> to which this listener/updater
	 * listens.
	 */
	private MeshComponent mesh;

	/**
	 * The host <code>MeshAppState</code> that needs to process updates to its
	 * <code>MeshComponent</code>.
	 */
	private final AtomicReference<MeshAppState> app;

	/**
	 * A flag denoting whether or not another update is required.
	 */
	private final AtomicBoolean needsUpdate;

	/**
	 * This is the daemon thread that handles the updates.
	 */
	private Thread updateThread;

	/**
	 * The default constructor.
	 */
	public MeshUpdateHandler() {
		// Initially, we have no mesh to listen to or app to work for.
		mesh = null;
		app = new AtomicReference<MeshAppState>(null);

		// Initialize the atomics needed for the thread to run and update
		// properly.
		needsUpdate = new AtomicBoolean(false);

		return;
	}

	/**
	 * Changes the mesh that is currently being listened to. If an update is
	 * currently being processed, it is finished and then all remaining
	 * unprocessed updates are ignored. An initial update is run with the new
	 * mesh.
	 * 
	 * @param mesh
	 *            The new mesh to listen to.
	 */
	public void setMesh(MeshComponent mesh) {

		// Validate the input. Don't change anything if the mesh is null or is
		// the same instance.
		if (mesh != null && mesh != this.mesh) {
			// Stop updates until we can register with the new mesh.
			needsUpdate.set(false);

			// Unregister from the current mesh if possible.
			if (this.mesh != null) {
				this.mesh.unregister(this);
			}

			// Set the reference to the new mesh.
			this.mesh = mesh;
			mesh.register(this);

			// After registering with the new mesh, we need to update.
			update(mesh);
		}

		return;
	}

	/**
	 * Starts the <code>MeshUpdateHandler</code> so that updates from a
	 * <code>MeshComponent</code> can be handled one at a time and reduced in
	 * number when multiple updates are required at the same time.
	 * 
	 * @param app
	 *            The <code>MeshAppState</code> that needs to update based on
	 *            changes in the mesh.
	 */
	public void start(MeshAppState app) {

		// We can only proceed if the following is true:
		// The app is not null and is initialized.
		// The current app is null. (Note: the app is set by this condition)
		if (app != null // && app.isInitialized()
				&& this.app.compareAndSet(null, app)) {

			// Create and start the worker thread that handles updates.
			updateThread = createUpdateThread();
			updateThread.setDaemon(true);
			updateThread.start();
		}

		return;
	}

	/**
	 * Stops the <code>MeshUpdateHandler</code>.
	 */
	public void stop() {
		needsUpdate.set(false);
		// Setting the app to null stops the update thread.
		app.set(null);
		// The update daemon thread will terminate on its own.
		updateThread = null;
	}

	// ---- Implements IUpdateableListener ---- //
	/**
	 * Notifies the <code>MeshUpdateHandler</code> of another mesh update.
	 */
	@Override
	public void update(IUpdateable component) {
		// Queue an update if the component matches.
		if (component == mesh) {
			needsUpdate.set(true);
		}
	}

	// ---------------------------------------- //

	/**
	 * Creates the thread that updates the {@link #app} when the
	 * {@link #needsUpdate} flag is set to true.
	 * 
	 * @return An update thread.
	 */
	private Thread createUpdateThread() {
		return new Thread() {
			@Override
			public void run() {
				// The outer loop will run as long as there is a MeshAppState.
				MeshAppState currentApp;
				while ((currentApp = app.get()) != null) {
					if (needsUpdate.compareAndSet(true, false) && mesh != null) {
						// Run an update! Since there are several components
						// within the MeshAppState that need to be updated
						// (controllers), we let the MeshAppState handle the
						// update directly (even though this thread is doing the
						// leg-work).
						currentApp.updateMesh(mesh);
					}

					// Pause for 50 ms between attempts to update.
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				return;
			}
		};
	}

}
