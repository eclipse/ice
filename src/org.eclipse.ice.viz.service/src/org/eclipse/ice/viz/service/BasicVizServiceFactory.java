/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory;
import org.eclipse.ice.viz.service.csv.CSVVizService;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

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
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory#register
	 * (org.eclipse.ice.client.widgets.viz.service.IVizService)
	 */
	@Override
	public void register(IVizService service) {
		if (service != null) {
			String name = service.getName();

			// Put the service in service map so it can be retrieved later
			serviceMap.put(name, service);

			System.out.println("VizServiceFactory message: " + "Viz service \""
					+ name + "\" registered.");

			// If the preference for automatically connecting to default viz
			// service connections is set, establish default connections.
			if (getPreferenceStore().getBoolean("autoConnectToDefaults")) {
				if (service.connect()) {
					System.out.println("VizServiceFactory message: "
							+ "Viz service \"" + name + "\" connected.");
				} else {
					System.out.println("VizServiceFactory message: "
							+ "Viz service \"" + name + "\" is connecting...");
				}
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory#unregister
	 * (org.eclipse.ice.client.widgets.viz.service.IVizService)
	 */
	@Override
	public void unregister(IVizService service) {
		if (service != null) {
			serviceMap.remove(service.getName());
			// Try to disconnect the service.
			if (service.disconnect()) {
				System.out.println("VizServiceFactory message: "
						+ service.getName() + "unregistered and disconnected.");
			} else {
				System.out.println("VizServiceFactory message: "
						+ service.getName()
						+ "unregistered and is currently disconnecting.");
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory#getServiceNames
	 * ()
	 */
	@Override
	public String[] getServiceNames() {

		String[] names = {};
		names = serviceMap.keySet().toArray(names);

		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory#get(java
	 * .lang.String)
	 */
	@Override
	public IVizService get(String serviceName) {

		IVizService service = null;

		if (serviceMap.containsKey(serviceName)) {
			service = serviceMap.get(serviceName);
		}

		return service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory#get()
	 */
	@Override
	public IVizService get() {
		return get("ice-plot");
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
}
