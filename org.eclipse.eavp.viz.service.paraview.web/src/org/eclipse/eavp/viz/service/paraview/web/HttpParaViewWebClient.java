/*******************************************************************************
 * Copyright (c) 2014, 2015 Kitware Inc. and UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sebastien Jourdain (Kitware Inc.) - initial API and implementation and/or 
 *      initial documentation
 *    Jordan Deyton (UT-Battelle, LLC.) - implemented disconnect()
 *    Jordan Deyton - updated to use GSON
 *    Jordan Deyton - removed temporary print statements; rearranged return 
 *      value in connect()
 *    Jordan Deyton - ExecutorService now starts/stops on connect/disconnect
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * TODO Documentation
 * 
 * @author Sebastien Jourdain
 *
 */
public class HttpParaViewWebClient implements IParaViewWebClient {

	/**
	 * 
	 */
	private String baseEndPointURL;

	/**
	 * 
	 */
	private ExecutorService requestExecutor;

	/**
	 * The default constructor.
	 */
	public HttpParaViewWebClient() {
		requestExecutor = null;
	}

	/**
	 * 
	 */
	private JsonObject makeRequest(String method, JsonObject content) {
		if (requestExecutor == null) {
			return null;
		}

		JsonObject retVal = null;

		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			String fullUrl = baseEndPointURL + method;
			url = new URL(fullUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/octetstream");
			connection.setRequestProperty("Content-Length", Integer.toString(content.entrySet().size()));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			connection.getOutputStream().write(content.toString().getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\n');
			}
			rd.close();

			// Parse the response into a JsonObject if possible.
			try {
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(response.toString());
				if (element.isJsonObject()) {
					retVal = element.getAsJsonObject();
				}
			} catch (JsonSyntaxException e) {
				// Do nothing if the response is not a parseable JsonObject.
			}
			// If a response could not be processed, create an empty one.
			if (retVal == null) {
				retVal = new JsonObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// If there was an error connecting, try to disconnect.
			if (connection != null) {
				connection.disconnect();
			}
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient#connect(java.lang.String)
	 */
	@Override
	public Future<Boolean> connect(String url) {
		this.baseEndPointURL = url;

		// Now that a connection is requested, create the worker thread.
		requestExecutor = Executors.newSingleThreadExecutor();

		return requestExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {

				boolean connected = false;

				// ---- Send a HEAD request. ---- //
				URL url = new URL(baseEndPointURL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("HEAD");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				// ------------------------------ //

				// ---- Get the response. ---- //
				// Note: We let the ExecutorService handle the timeout.

				// Determine the connection code from the request.
				int code = -1;
				try {
					code = connection.getResponseCode();
					if (code == HttpURLConnection.HTTP_OK) {
						connected = true;
					}
				} catch (IOException e) {
					// There was an error. The connection failed.
				}
				// --------------------------- //

				return connected;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient#disconnect()
	 */
	@Override
	public Future<Boolean> disconnect() {
		Future<Boolean> future = requestExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return true;
			}
		});
		// Stop and unset the worker thread.
		requestExecutor.shutdown();
		requestExecutor = null;
		return future;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient#render(int, int, int, int)
	 */
	@Override
	public Future<JsonObject> render(int viewId, int quality, int width, int height) {
		if (requestExecutor == null) {
			return null;
		}

		// Set up the size array (with x height).
		JsonArray size = new JsonArray();
		size.add(new JsonPrimitive(width));
		size.add(new JsonPrimitive(height));

		// Set up the content of the request.
		JsonObject reqObj = new JsonObject();
		reqObj.add("size", size);
		reqObj.addProperty("view", viewId);
		reqObj.addProperty("quality", quality);
		reqObj.addProperty("localtime", System.currentTimeMillis());

		// Set up the main request object. Note that it *must* provide an "args"
		// property set to a JSON array.
		JsonArray args = new JsonArray();
		args.add(reqObj);
		final JsonObject mainObj = new JsonObject();
		mainObj.add("args", args);

		Callable<JsonObject> request = new Callable<JsonObject>() {
			public JsonObject call() throws Exception {
				return makeRequest("viewport.image.render", mainObj);
			}
		};
		return requestExecutor.submit(request);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient#event(int, double, double, java.lang.String, boolean[])
	 */
	@Override
	public Future<JsonObject> event(int viewId, double x, double y, String action, boolean[] mouseState) {
		if (requestExecutor == null) {
			return null;
		}

		// Set up the content of the request.
		JsonObject reqObj = new JsonObject();
		reqObj.addProperty("view", viewId);
		reqObj.addProperty("x", x);
		reqObj.addProperty("y", y);
		reqObj.addProperty("buttonLeft", mouseState[0] ? 1 : 0);
		reqObj.addProperty("buttonMiddle", mouseState[1] ? 1 : 0);
		reqObj.addProperty("buttonRight", mouseState[2] ? 1 : 0);
		reqObj.addProperty("shiftKey", mouseState[3] ? 1 : 0);
		reqObj.addProperty("ctrlKey", mouseState[4] ? 1 : 0);
		reqObj.addProperty("altKey", mouseState[5] ? 1 : 0);
		reqObj.addProperty("metaKey", mouseState[6] ? 1 : 0);
		reqObj.addProperty("action", action);

		// Set up the main request object. Note that it *must* provide an "args"
		// property set to a JSON array.
		JsonArray args = new JsonArray();
		args.add(reqObj);
		final JsonObject mainObj = new JsonObject();
		mainObj.add("args", args);

		Callable<JsonObject> request = new Callable<JsonObject>() {
			public JsonObject call() throws Exception {
				return makeRequest("viewport.mouse.interaction", mainObj);
			}
		};
		return requestExecutor.submit(request);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient#call(java.lang.String, com.google.gson.JsonArray)
	 */
	@Override
	public Future<JsonObject> call(String method, JsonArray args) {
		if (requestExecutor == null) {
			return null;
		}

		final String methodRef = method;
		final JsonObject reqObj = new JsonObject();

		reqObj.add("args", args);
		Callable<JsonObject> request = new Callable<JsonObject>() {
			public JsonObject call() throws Exception {
				return makeRequest(methodRef, reqObj);
			}
		};
		return requestExecutor.submit(request);
	}
}
