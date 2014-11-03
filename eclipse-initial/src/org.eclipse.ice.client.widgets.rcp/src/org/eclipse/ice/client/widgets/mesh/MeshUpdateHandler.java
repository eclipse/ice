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
package org.eclipse.ice.client.widgets.mesh;

import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * This class handles queuing MeshComponent updates for the MeshApplication. The
 * reason this is handled in this class is two-fold:
 * </p>
 * <p>
 * </p>
 * <p>
 * First, the IUpdateableListener.update() method does not give away any more
 * information about the prospective update other than that something has
 * changed. This means that each update to the MeshComponent requires a
 * comprehensive comparison between what is currently displayed in the
 * MeshApplication and what is currently contained in the MeshComponent. In
 * other words, the method implementing the update is heavyweight, and multiple
 * updates cannot be performed simultaneously.
 * </p>
 * <p>
 * </p>
 * <p>
 * Second, we need to process *all* updates. Since the update process may take a
 * significant period, it is possible that several consecutive notifications
 * from the MeshComponent arrive while the MeshApplication is already updating.
 * Instead of running the update n times for n calls, only the current update
 * and the last remaining update need to be handled.
 * </p>
 * 
 * @author djg
 * 
 */
public class MeshUpdateHandler extends Thread implements IUpdateableListener {

	/**
	 * A flag denoting whether or not this Thread should continue executing the
	 * loop in the run method.
	 */
	private final AtomicBoolean listen;

	/**
	 * A flag denoting whether or not another update is required.
	 */
	private final AtomicBoolean needsUpdate;

	/**
	 * The current MeshComponent to which this listener/updater listens.
	 */
	private MeshComponent mesh;

	/**
	 * The host MeshApplication that needs to process updates to its
	 * MeshComponent.
	 */
	private final MeshApplication app;

	/**
	 * The default constructor. Stores a final reference to the host
	 * MeshApplication.
	 * 
	 * @param app
	 *            The host MeshApplication that needs to process updates to its
	 *            MeshComponent.
	 */
	public MeshUpdateHandler(MeshApplication app) {
		super();

		// Initially, we have no mesh to listen to.
		mesh = null;

		// Initialize the atomics needed for the thread to run and update
		// properly.
		listen = new AtomicBoolean(true);
		needsUpdate = new AtomicBoolean(false);

		// Set the host MeshApplication.
		this.app = app;

		return;
	}

	/**
	 * Processes any updates to the mesh listener. If multiple updates arrive
	 * before the current update completes, only one additional update will be
	 * processed.
	 */
	@Override
	public void run() {

		// Initially, we need to wait for the MeshApplication to be initialized,
		// otherwise, the jME3 root Node will not be ready for the updates.
		while (listen.get() && !app.isInitialized()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err
						.println("MeshUpdateHandler error: Thread interrupted "
								+ "while waiting for MeshApplication "
								+ " initialization to complete.");
				e.printStackTrace();
			}
		}

		// The outer loop will continue to run based on the value of listen.
		// The inner loop will run an update if the needsUpdate flag is true.
		while (listen.get()) {
			if (needsUpdate.compareAndSet(true, false) && mesh != null) {
				// Run an update! Since there are several components within the
				// MeshApplication that need to be updated (controllers), we
				// let the MeshApplication handle the update directly (even
				// though this thread is doing the leg-work).
				this.app.update(mesh);
			}
		}

		return;
	}

	/**
	 * Stops the listener from updating.
	 */
	public void halt() {
		listen.set(false);
	}

	/**
	 * Stops any future updates and sets the current mesh that is sending
	 * updates to this listener.
	 * 
	 * @param mesh
	 *            The new mesh to listen to.
	 */
	public void setMesh(MeshComponent mesh) {

		// Validate the input. Don't change anything if the mesh is null or is
		// the same instance.
		if (mesh != null && mesh != this.mesh) {
			// Unregister from the current mesh if possible.
			if (this.mesh != null) {
				this.mesh.unregister(this);
			}
			// Stop updates until we can register with the new mesh.
			needsUpdate.set(false);
			this.mesh = mesh;
			mesh.register(this);

			// After registering with the new mesh, we need to update.
			update(mesh);
		}

		return;
	}

	/**
	 * Implements IUpdateableListener. This method needs to flag that another
	 * update needs to occur.
	 */
	public void update(IUpdateable component) {

		// If the component is the one we should be listening to and an update
		// is not already queued, queue an update.
		if (component == mesh) {
			needsUpdate.set(true);
		}
		return;
	}

}
