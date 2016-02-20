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
package org.eclipse.eavp.viz.service;

/**
 * This is a pluggable service interface for managing a collection of
 * visualization engines that have been published to the platform.
 * Implementations of this interface should be relatively straightforward,
 * simple classes that index all of the IVizServices registered to the factory.
 * 
 * @author Jay Jay Billings
 */
public interface IVizServiceFactory {

	/**
	 * This operation returns the default IVizService provided by the factory,
	 * which is entirely up to the factory, or null if the factory does not
	 * contain a default service.
	 * 
	 * @return The default IVizService or null if no service is available
	 */
	public IVizService get();

	/**
	 * This operation returns the IVizService with the given name or null it if
	 * is unavailable. It is the preferred way of retrieving an IVizService
	 * because it is explicit in what needs to be retrieved.
	 * 
	 * @param serviceName
	 *            The name of the service to find
	 * @return The visualization service with the given name or null if that
	 *         service cannot be found by the factory
	 */
	public IVizService get(String serviceName);

	/**
	 * This operation returns the names of all of the IVizServices registered
	 * with the factory. It is makes it possible to give clients a choice of
	 * services instead of relying on the default service or guessing at what
	 * may be registered.
	 * 
	 * @return The names of the registered services
	 */
	public String[] getServiceNames();

	/**
	 * This operation registers an IVizService with the factory so that it can
	 * be retrieved and used by clients.
	 * <p>
	 * The factory will register the IVizService's preference page with the
	 * platform when this operation is called and if the IVizService has
	 * properties.
	 * </p>
	 * 
	 * @param service
	 *            The visualization service to register
	 */
	public void register(IVizService service);

	/**
	 * This operation unregisters an IVizService with the factory so that it
	 * will no longer be used by clients.
	 * 
	 * @param service
	 *            The visualization service to register
	 */
	public void unregister(IVizService service);

}
