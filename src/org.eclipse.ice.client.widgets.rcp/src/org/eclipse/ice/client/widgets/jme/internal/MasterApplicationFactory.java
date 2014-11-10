package org.eclipse.ice.client.widgets.jme.internal;

import org.eclipse.ice.client.widgets.jme.MasterApplication;

/**
 * This class provides an implementation for {@link IMasterApplicationFactory}.
 * 
 * @author Jordan
 * 
 */
public class MasterApplicationFactory implements IMasterApplicationFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.internal.IMasterApplicationFactory
	 * #createApplication()
	 */
	@Override
	public MasterApplication createApplication() {
		// We use a static method here because there are private variables that
		// must be properly initialized inside MasterApplication. We should not
		// have to expose them to sub-classes or package classes.
		return MasterApplication.createApplication();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.internal.IMasterApplicationFactory
	 * #disposeApplication(org.eclipse.ice.client.widgets.jme.MasterApplication)
	 */
	@Override
	public void disposeApplication(MasterApplication app) {
		if (app != null) {
			app.stop();
		}
	}

}
