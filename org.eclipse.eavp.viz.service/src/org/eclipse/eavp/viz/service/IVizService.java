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
package org.eclipse.eavp.viz.service;

import java.net.URI;
import java.util.Set;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.IControllerFactory;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

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
 * @author Robert Smith
 * @author Kasper Gammeltoft
 * @author Jordan Deyton
 */
public interface IVizService {

	/**
	 * This operation directs the IVizService to create a new canvas using the
	 * given VizObject and to return a handle to that canvas to the caller so
	 * that it may modify the canvas.
	 * 
	 * @param object
	 *            The input object which will be rendered in the new canvas
	 * @return The IVizCanvas that will render the object
	 * @throws Exception
	 *             An exception indicating that the IVizService could not create
	 *             a canvas with the given object and giving the reason why.
	 */
	public IVizCanvas createCanvas(AbstractController object) throws Exception;

	/**
	 * Returns a factory which will create views and controllers for model parts
	 * specific to the service's rendering program.
	 * 
	 * @return A factory that will create controllers compatible with this
	 *         service
	 */
	public IControllerFactory getFactory();

	/**
	 * This operation returns the name of the service. The name should be
	 * something simple and human-readable.
	 * 
	 * @return The name of the IVizService
	 */
	public String getName();

	/**
	 * Gets the number of pages this service will display in the plot editor in
	 * addition to the main canvas.
	 * 
	 * @return The number of additional pages displayed by the plot editor. 0 if
	 *         there is only one page.
	 */
	public int getNumAdditionalPages();

	/**
	 * This operation returns a version number for the service. It should be
	 * more or less human readable and contain a major and a minor version
	 * (Version 2.1 instead of just 2, for example).
	 * 
	 * @return The version of the IVizService
	 */
	public String getVersion();

	/**
	 * Directs the service to draw one of its additional pages and add it to the
	 * MultiPageEditorPart. The expected use is to call
	 * creatAddionalPage(parent, file, 1), createAdditonalPage(parent, file, 2),
	 * etc. until all additional pages have been drawn in separate tabs.
	 * 
	 * @param parent
	 *            The part which the page will be added to.
	 * @param file
	 *            The file to use as input for the page.
	 * @param pageNum
	 *            The id of the page to be drawn.
	 * 
	 * @return The name of the new page, to be displayed as the title of the tab
	 *         containing it
	 */
	public String createAdditionalPage(MultiPageEditorPart parent,
			IFileEditorInput file, int pageNum);

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

}
