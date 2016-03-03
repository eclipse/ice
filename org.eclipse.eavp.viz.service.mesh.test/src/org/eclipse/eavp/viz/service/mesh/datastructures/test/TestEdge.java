/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.mesh.datastructures.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.LinearEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.VertexController;

/**
 * <p>
 * This class provides a simple generalization of an Edge that keeps track of
 * when one of its vertices was updated. Additional functionality may be added
 * at a later time.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class TestEdge extends EdgeController {
	/**
	 * <p>
	 * Whether or not the Edge's update method was called.
	 * </p>
	 * 
	 */
	private AtomicBoolean wasUpdated = new AtomicBoolean(false);

	/**
	 * <p>
	 * Calls the super constructor with the same signature.
	 * </p>
	 * 
	 * @param start
	 *            <p>
	 *            The first Vertex in this Edge.
	 *            </p>
	 * @param end
	 *            <p>
	 *            The second Vertex in this Edge.
	 *            </p>
	 */
	public TestEdge(VertexController start, VertexController end) {
		super(new LinearEdgeMesh(start, end), new BasicView());
	}

	/**
	 * <p>
	 * Calls the super constructor with the same signature.
	 * </p>
	 * 
	 * @param vertices
	 *            <p>
	 *            The two Vertices this Edge connects.
	 *            </p>
	 */
	public TestEdge(ArrayList<VertexController> vertices) {
		super(new EdgeMesh(vertices.get(0), vertices.get(1)),
				new BasicView());
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public TestEdge(EdgeMesh model, BasicView view) {
		super(model, view);
	}

	/**
	 * <p>
	 * Overrides the update method of Edge to mark a flag when the Edge is
	 * notified.
	 * </p>
	 * 
	 * @param vertex
	 *            <p>
	 *            The Vertex that has been updated.
	 *            </p>
	 */
	@Override
	public void update(IManagedUpdateable component,
			SubscriptionType[] type) {

		// Call the super's update method and update the flag.
		super.update(component, type);
		wasUpdated.set(true);

		return;
	}

	/**
	 * <p>
	 * Gets whether or not the Edge's update method was called. This also resets
	 * the value to false after the call.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         True if the Edge's update method was called, false otherwise.
	 *         </p>
	 */
	public boolean wasUpdated() {

		// Wait a couple of seconds so that the thread can work, but break early
		// if the thread has finished.
		double seconds = 2;
		double sleepTime = 0.0;
		while (!wasUpdated.get() && sleepTime < seconds) {
			try {
				Thread.sleep(50);
				sleepTime += 0.05;
			} catch (InterruptedException e) {
				// Complain and fail
				e.printStackTrace();
				fail();
			}
		}

		// Reset wasUpdated and return its previous value.
		return wasUpdated.getAndSet(false);
	}

	/**
	 * <p>
	 * Resets the Test class' flag that notifies it was updated. This is
	 * normally not necessary but can be used in lieu of multiple calls to
	 * wasUpdated().
	 * </p>
	 * 
	 */
	public void reset() {
		wasUpdated.set(false);
	}
}