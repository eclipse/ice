/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.connections;

import org.eclipse.ice.viz.service.connections.VizConnection;
import org.eclipse.ice.viz.service.connections.VizConnectionManager;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

/**
 * This class provides an {@link VizConnectionManager} that creates and manages
 * preferences for {@link ParaViewConnection}s.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnectionManager extends VizConnectionManager<IParaViewWebClient> {

	/*
	 * Implements an abstract method from VizConnectionManager.
	 */
	@Override
	protected VizConnection<IParaViewWebClient> createConnection(String name, String preferences) {
		// If any additional properties are required to be pulled from the
		// preferences string, it should be done here and set on the connection
		// before returning it.
		return new ParaViewConnection();
	}

}
