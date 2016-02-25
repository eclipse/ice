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
 *******************************************************************************/
package org.eclipse.eavp.viz.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.eavp.viz.service.csv.CSVVizService;
import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.FileEditorMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the basic implementation of the IVizServiceFactory in ICE. It
 * is registered with the platform as an OSGi service.
 *
 * The default IVizService is "ice-plot" and it is registered when the service
 * is started.
 *
 * @author Jay Jay Billings
 *
 */
public class BasicVizServiceFactory implements IVizServiceFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BasicVizServiceFactory.class);

	/**
	 * The map that stores all of the services.
	 */
	private Map<String, IVizService> serviceMap;

	/**
	 * A reference to the associated preference page's {@link IPreferenceStore}.
	 * If this has been determined previously, then it should be returned in
	 * {@link #getPreferenceStore()}.
	 */
	private IPreferenceStore preferenceStore = null;

	/**
	 * The constructor
	 */
	public BasicVizServiceFactory() {
		// Create the map to hold the services
		serviceMap = new HashMap<String, IVizService>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#get()
	 */
	@Override
	public IVizService get() {
		return get("ice-plot");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#get(java.lang.String)
	 */
	@Override
	public IVizService get(String serviceName) {

		IVizService service = null;

		if (serviceMap.containsKey(serviceName)) {
			service = serviceMap.get(serviceName);
		}

		return service;
	}

	/**
	 * Gets the {@link IPreferenceStore} for the associated preference page.
	 *
	 * @return The {@code IPreferenceStore} whose defaults should be set.
	 */
	private IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			// Get the PreferenceStore for the bundle.
			preferenceStore = new CustomScopedPreferenceStore(getClass());
		}
		return preferenceStore;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#getServiceNames()
	 */
	@Override
	public String[] getServiceNames() {

		String[] names = {};
		names = serviceMap.keySet().toArray(names);

		return names;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#register(org.eclipse.eavp.viz.service.IVizService)
	 */
	@Override
	public void register(IVizService service) {
		if (service != null) {
			String name = service.getName();

			// Put the service in service map so it can be retrieved later
			serviceMap.put(name, service);

			// Register the plot editor as default editor for all file
			// extensions handled by the new viz service
			for (String ext : service.getSupportedExtensions()) {
				EditorRegistry editorReg = (EditorRegistry) PlatformUI
						.getWorkbench().getEditorRegistry();
				EditorDescriptor editor = (EditorDescriptor) editorReg
						.findEditor("org.eclipse.eavp.viz.service.PlotEditor");
				FileEditorMapping mapping = new FileEditorMapping(ext);
				mapping.addEditor(editor);
				mapping.setDefaultEditor(editor);

				IFileEditorMapping[] mappings = editorReg
						.getFileEditorMappings();
				FileEditorMapping[] newMappings = new FileEditorMapping[mappings.length
						+ 1];
				for (int i = 0; i < mappings.length; i++) {
					newMappings[i] = (FileEditorMapping) mappings[i];
				}
				newMappings[mappings.length] = mapping;
				editorReg.setFileEditorMappings(newMappings);
			}

			logger.info("VizServiceFactory message: " + "Viz service \"" + name
					+ "\" registered.");
		}

		return;
	}

	/**
	 * This operation starts the service, including registering the basic CSV
	 * plotter viz service, "ice-plot," with the platform.
	 */
	public void start() {
		// Initialize "ice-plot" viz service
		register(new CSVVizService());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#unregister(org.eclipse.eavp.viz.service.IVizService)
	 */
	@Override
	public void unregister(IVizService service) {
		if (service != null) {
			serviceMap.remove(service.getName());
		}
	}
}
