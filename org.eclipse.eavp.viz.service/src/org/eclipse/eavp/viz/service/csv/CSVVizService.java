/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.eavp.viz.service.csv;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.eavp.viz.service.AbstractVizService;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IVizCanvas;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.IControllerFactory;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the IVizService interface to provide CSV plotting tools
 * to the platform via the IVizServiceFactory. It used the classes already
 * available in org.eclipse.eavp.viz and provides a rudimentary plotting
 * capability.
 * 
 * It is the default "ice-plot" implementation provided by the platform.
 * 
 * @author Jay Jay Billings
 * 
 */
public class CSVVizService extends AbstractVizService {

	/**
	 * The logger for error messages.
	 */
	Logger logger = LoggerFactory.getLogger(CSVVizService.class);

	/**
	 * A map from file URIs to the plots created from them.
	 */
	private final Map<URI, CSVPlot> dataPlots = new HashMap<URI, CSVPlot>();

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> Only OSGi should call this method!
	 * </p>
	 */
	public CSVVizService() {
		// Nothing to do.
	}

	/*
	 * Overrides a super class method. Tries to create a CSVPlot.
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {
		// Call the super method to validate the URI's extension.
		super.createPlot(file);

		// Get the associated data plot. Create one if necessary.
		CSVPlot dataPlot = dataPlots.get(file);
		if (dataPlot == null) {
			dataPlot = new CSVPlot();
			dataPlot.setDataSource(file);
			dataPlots.put(file, dataPlot);
		}

		// Create a proxy to it. The proxy can be drawn anywhere.
		CSVProxyPlot proxyPlot = new CSVProxyPlot();
		proxyPlot.setSource(dataPlot);

		return proxyPlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.AbstractVizService#findSupportedExtensions()
	 */
	@Override
	protected Set<String> findSupportedExtensions() {
		Set<String> extensions = new HashSet<String>();
		extensions.add("csv");
		return extensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "ice-plot";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "2.0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.IVizService#createCanvas(org.eclipse.eavp.viz.
	 * service.datastructures.VizObject)
	 */
	@Override
	public IVizCanvas createCanvas(AbstractController object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IControllerFactory getFactory() {
		// CSV visualization service does not make use of the model framework,
		// so it has no factory
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getNumAdditionalPages()
	 */
	@Override
	public int getNumAdditionalPages() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.IVizService#createAdditionalPage(org.eclipse.
	 * swt.widgets.Composite, java.net.URI, int)
	 */
	@Override
	public String createAdditionalPage(MultiPageEditorPart parent,
			IFileEditorInput file, int pageNum) {

		// Create the specified page
		switch (pageNum) {

		// Page 2 is the file's data displayed in text
		case 1:

			// Create a text editor with the file as input and add its page with
			// the name Data
			int index;
			try {
				index = parent.addPage((IEditorPart) new TextEditor(), file);
				return "Data";
			} catch (PartInitException e) {
				logger.error(
						"Error initializing text editor for CSV Plot Editor.");
			}
			break;
		}

		// If the page number is not supported, return null
		return null;
	}

}
