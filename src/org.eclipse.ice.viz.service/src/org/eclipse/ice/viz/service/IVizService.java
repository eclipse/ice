/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *   Jordan Deyton - added extension getter, removed connection methods
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import java.net.URI;
import java.util.Set;

/**
 * This is a pluggable service interface whereby visualization engines can
 * publish their services to the platform. It is designed to be implemented as a
 * pluggable, declarative OSGi service that is provided dynamically to an
 * implementation of the IVizServiceFactory.
 * <p>
 * IVizServices should be considered handles to the services that a particular
 * visualization engine provides. Its primary purpose a means to configure a
 * valid connection to the visualization service, if required, and to act as a
 * factory for creating IPlots.
 * </p>
 * <p>
 * IVizServices are responsible for managing their own preferences and providing
 * both an IPreferencesPage that can be registered with the Platform and a
 * simple map of preferences. (The simplest way to do this is to keep everything
 * in a bundle preferences store, register listeners on the page, and handle
 * merges from setConnectionProperties manually.)
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IVizService {

	/**
	 * This operation directs the IVizService to create a new plot using the
	 * specified file and to return a handle to that plot to the caller so that
	 * it may modify the plot.
	 * 
	 * @param file
	 *            The file from which the plot should be created
	 * @return The IPlot that will render a plot from the file
	 * @throws Exception
	 *             An exception indicating that the IVizService could not create
	 *             a plot with the given file and giving the reason why.
	 */
	public IPlot createPlot(URI file) throws Exception;

	/**
	 * This operation returns the name of the service. The name should be
	 * something simple and human-readable.
	 * 
	 * @return The name of the IVizService
	 */
	public String getName();

	/**
	 * Gets a set containing all supported file extensions for which an IPlot
	 * can be created. Extensions in the set are expected to conform to the
	 * following format:
	 * <ul>
	 * <li>simple (tar and gz, but not tar.gz),</li>
	 * <li>should not include the leading period (doc, not .doc), and</li>
	 * <li>should be lower case (txt, not TXT).</li>
	 * </ul>
	 * 
	 * @return A set containing all supported extensions.
	 */
	public Set<String> getSupportedExtensions();

	/**
	 * This operation returns a version number for the service. It should be
	 * more or less human readable and contain a major and a minor version
	 * (Version 2.1 instead of just 2, for example).
	 * 
	 * @return The version of the IVizService
	 */
	public String getVersion();
}
