/*******************************************************************************
 * Copyright (c) 2014, 2015 Kitware Inc. and UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sebastien Jourdan (Kitware Inc.) - initial API and implementation and/or 
 *      initial documentation
 *    Jordan Deyton (UT-Battelle, LLC.) - added disconnect() operation
 *    Jordan Deyton (UT-Battelle, LLC.) - updated to use GSON
 *    
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.web;

import java.util.concurrent.Future;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * TODO Documentation for this class.
 * 
 * @author Sebastien Jourdain
 *
 */
public interface IParaViewWebClient {

	/**
	 * 
	 * @param url
	 * @return
	 */
	public Future<Boolean> connect(String url);

	/**
	 * 
	 * @param viewId
	 * @param quality
	 * @param width
	 * @param height
	 * @return
	 */
	public Future<JsonObject> render(int viewId, int quality, int width,
			int height);

	/**
	 * 
	 * @param viewId
	 * @param x
	 * @param y
	 * @param action
	 * @param mouseState
	 * @return
	 */
	public Future<JsonObject> event(int viewId, double x, double y,
			String action, boolean[] mouseState);

	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	public Future<JsonObject> call(String method, JsonArray args);

	/**
	 * 
	 * @return
	 */
	public Future<Boolean> disconnect();
}
