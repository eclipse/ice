package org.eclipse.ice.viz.service.internal;

import org.eclipse.ice.viz.service.IVizServiceFactory;

/**
 * Holder class for a VizServiceFactory.
 * 
 * @author Robert Smith
 *
 */

public class VizServiceFactoryHolder {
	private static IVizServiceFactory factory;

	/**
	 * Setter for the VizServiceFactory.
	 * 
	 * @param input
	 *            the VizServiceFactory to hold
	 */
	public static void setVizServiceFactory(IVizServiceFactory input) {
		VizServiceFactoryHolder.factory = input;
		return;
	}

	/**
	 * Remove the held VizServiceFactory.
	 * 
	 */
	public static void unsetVizServiceFactory(IVizServiceFactory input) {
		factory = null;
		return;
	}

	/**
	 * Getter for the held VizServiceFactory.
	 * 
	 * @return the held VizServiceFactory
	 */
	public static IVizServiceFactory getFactory() {
		return factory;
	}

}
