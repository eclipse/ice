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
 * This class provides a basic implementation of {@link IParaViewProxy} and
 * should be used whenever possible when dealing with the ParaView Java client.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractParaViewProxy implements IParaViewProxy {

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean open(ParaViewConnectionAdapter connection) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setFeature(String feature) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Map<String, Set<String>> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setProperty(String property, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public int setProperties(Map<String, String> properties) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public URI getFile() {
		// TODO Auto-generated method stub
		return null;
	}

}