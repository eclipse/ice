/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.application;

import com.jme3.scene.Node;

/**
 * This is an interface for clients (generally {@link ViewAppState}s) that use
 * {@link VizEmbeddedView}s. <code>EmbeddedView</code>s can register and
 * unregister a single <code>IEmbeddedViewClient</code>. The methods provided by
 * this interface enable the <code>EmbeddedView</code> to hook up to the client.
 * <b>For some of these operations, the implementation should not worry about
 * operating on the render thread. These operations will have a note in their
 * documentation.</b>
 * 
 * <p>
 * <b>Note:</b> Each <code>EmbeddedView</code> can support only a single
 * <code>IEmbeddedViewClient</code> at a time, but the reverse is not
 * necessarily true depending on your implementation of this interface.
 * </p>
 * 
 * @author Jordan
 * 
 */
public interface IEmbeddedViewClient {

	/**
	 * Gets the root <code>Node</code> for the client's scene. This is the
	 * <code>Node</code> that will be shown in the <code>EmbeddedView</code>'s
	 * main <code>ViewPort</code>.
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> that is requesting the root
	 *            <code>Node</code>.
	 * @return The root <code>Node</code> for the client's scene.
	 */
	public Node getSceneRoot(EmbeddedView view);

	/**
	 * Creates a new GUI or HUD <code>Node</code>. This is necessary because the
	 * coordinates of objects in the HUD associated with each
	 * <code>EmbeddedView</code> must be updated when the underlying
	 * <code>AwtPanel</code> changes size.
	 * 
	 * <p>
	 * <b>Note:</b> This method is called from the render thread.
	 * </p>
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> that is requesting the GUI/HUD
	 *            <code>Node</code>.
	 * @return A new GUI or HUD <code>Node</code>.
	 */
	public Node createHUD(EmbeddedView view);

	/**
	 * Notifies the client that a HUD must be updated based on the provided
	 * width and height of the associated <code>EmbeddedView</code>'s underlying
	 * <code>AwtPanel</code>.
	 * 
	 * <p>
	 * <b>Note:</b> This method is called from the render thread.
	 * </p>
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> whose size has changed.
	 * @param width
	 *            The new width of the <code>EmbeddedView</code>'s
	 *            <code>AwtPanel</code>.
	 * @param height
	 *            The new width of the <code>EmbeddedView</code>'s
	 *            <code>AwtPanel</code>.
	 */
	public void updateHUD(EmbeddedView view, int width, int height);

	/**
	 * Disposes the HUD <code>Node</code> associated with the specified
	 * <code>EmbeddedView</code>.
	 * 
	 * <p>
	 * <b>Note:</b> This method is called from the render thread.
	 * </p>
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> that is detaching from the
	 *            GUI/HUD <code>Node</code>.
	 */
	public void disposeHUD(EmbeddedView view);

	/**
	 * Creates a new interactive camera for an <code>EmbeddedView</code>. This
	 * is necessary because each <code>EmbeddedView</code> is associated with a
	 * unique camera, and each camera should be enabled or disabled along with
	 * the view's underlying <code>AwtPanel</code>.
	 * 
	 * <p>
	 * <b>Note:</b> This method is called from the render thread.
	 * </p>
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> that is requesting an
	 *            interactive camera.
	 * @return The interactive camera that was created.
	 */
	public Object createViewCamera(EmbeddedView view);

	/**
	 * Notifies the client that a camera must be updated. This is typically
	 * called when the associated <code>EmbeddedView</code>'s underlying
	 * <code>AwtPanel</code> has gained or lost focus to enable or disable the
	 * specified camera.
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> that has gained or lost focus.
	 * @param enabled
	 *            Whether or not to enable the interactive camera.
	 */
	public void updateViewCamera(EmbeddedView view, boolean enabled);

	/**
	 * Disposes specified interactive camera associated with the specified
	 * <code>EmbeddedView</code>.
	 * 
	 * <p>
	 * <b>Note:</b> This method is called from the render thread.
	 * </p>
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> that is removing the interactive
	 *            camera.
	 */
	public void disposeViewCamera(EmbeddedView view);

	/**
	 * Notifies the client that the <code>EmbeddedView</code>'s underlying
	 * <code>AwtPanel</code> has been resized.
	 * 
	 * <p>
	 * <b>Note:</b> This method is called from the render thread.
	 * </p>
	 * 
	 * @param view
	 *            The <code>EmbeddedView</code> whose size has changed.
	 * @param width
	 *            The new width of the <code>EmbeddedView</code>'s
	 *            <code>AwtPanel</code>.
	 * @param height
	 *            The new width of the <code>EmbeddedView</code>'s
	 *            <code>AwtPanel</code>.
	 */
	public void viewResized(EmbeddedView view, int width, int height);

	/**
	 * Notifies the client that the <code>EmbeddedView</code> (i.e., the
	 * underlying <code>AwtPanel</code>) has been activated.
	 * 
	 * @param view
	 *            The activated view.
	 */
	public void viewActivated(EmbeddedView view);

	/**
	 * Notifies the client that the <code>EmbeddedView</code> (i.e., the
	 * underlying <code>AwtPanel</code>) has been deactivated.
	 * 
	 * @param view
	 *            The deactivated view.
	 */
	public void viewDeactivated(EmbeddedView view);
}
