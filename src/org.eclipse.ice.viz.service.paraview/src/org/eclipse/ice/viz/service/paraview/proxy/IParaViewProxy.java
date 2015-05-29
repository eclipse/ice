/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;

/**
 * A proxy serves as an intermediary between client code and a particular file
 * loaded into a view in ParaView. Each proxy is responsible for <i>exactly</i>
 * one particular file for a given view. If another file needs to be
 * manipulated, another proxy should be created for that file.
 * 
 * @author Jordan Deyton
 *
 */
public interface IParaViewProxy {

	boolean open(ParaViewConnectionAdapter connection);

	Set<String> getFeatures();

	boolean setFeature(String feature);

	Map<String, Set<String>> getProperties();

	boolean setProperty(String property, String value);

	int setProperties(Map<String, String> properties);

	URI getFile();
}
