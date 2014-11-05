package org.eclipse.ice.client.widgets.jme.internal;

import org.eclipse.ice.client.widgets.jme.MasterApplication;

/**
 * This is an interface for a factory that can create and dispose of
 * {@link MasterApplication}s. This is intended to be used by OSGi DS to provide
 * a single, static <code>MasterApplication</code> to render jME scenes.
 * 
 * @author Jordan
 * 
 */
public interface IMasterApplicationFactory {

	/**
	 * Creates and starts a new <code>MasterApplication</code>. Ideally, this
	 * 
	 * @return A newly created and started <code>MasterApplication</code>.
	 */
	public MasterApplication createApplication();

	/**
	 * Stops and disposes of the specified <code>MasterApplication</code>.
	 * 
	 * @param app
	 *            The application to stop.
	 */
	public void disposeApplication(MasterApplication app);
}
