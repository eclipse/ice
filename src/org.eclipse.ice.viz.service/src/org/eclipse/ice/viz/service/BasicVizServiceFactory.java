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
package org.eclipse.ice.viz.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory;

/**
 * This class is the basic implementation of the IVizServiceFactory in ICE. It
 * is registered with the platform as an OSGi service.
 * 
 * The default IVizService if "ice-plot" if it is registered.
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
	 * The constructor
	 */
	public BasicVizServiceFactory() {
		// Create the map to hold the services
		serviceMap = new HashMap<String,IVizService>();
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
			serviceMap.put(service.getName(), service);
		}
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
		}
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
		
		String [] names = {};
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

}
