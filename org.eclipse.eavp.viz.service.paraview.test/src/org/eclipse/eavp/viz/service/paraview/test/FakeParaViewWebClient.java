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
package org.eclipse.eavp.viz.service.paraview.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty.PropertyType;
import org.eclipse.eavp.viz.service.paraview.proxy.test.FakeProxyFeature;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This class provides a fake {@link IParaViewWebClient} for testing purposes only. It
 * does not actually establish a connection. To imitate a working connection,
 * the {@link #responseMap} should be updated to include supported action names
 * mapped to {@code Callable}s that return {@link JsonObject}s with the desired
 * structure.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeParaViewWebClient implements IParaViewWebClient {

	/**
	 * A map of responses to RPC methods. This is used to produce a test
	 * response for a particular RPC method in lieu of having a running client.
	 */
	public final Map<String, Callable<JsonObject>> responseMap;
	
	/**
	 * This is used to queue tasks for the client to perform.
	 */
	private ExecutorService requestExecutor;

	private final List<Double> times;
	private final List<FakeProxyFeature> properties;
	
	/**
	 * The default constructor. {@link #responseMap} is initialized, but left
	 * empty.
	 */
	public FakeParaViewWebClient() {
		requestExecutor = Executors.newSingleThreadExecutor();

		responseMap = new HashMap<String, Callable<JsonObject>>();
		properties = new ArrayList<FakeProxyFeature>();
		times = new ArrayList<Double>();
		times.add(0.0);
		
		responseMap.put("createView", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				JsonObject object = new JsonObject();
				object.addProperty("proxyId", 1);
				object.addProperty("repId", 2);
				object.addProperty("viewId", 3);
				return object;
			}
		});
		
		responseMap.put("pv.proxy.manager.get", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				JsonObject proxyObject = new JsonObject();
				
				JsonObject object;
				JsonArray array;
				
				JsonObject dataObject = new JsonObject();
				JsonArray uiArray = new JsonArray();
				JsonArray propertiesArray = new JsonArray();
				
				proxyObject.add("data", dataObject);
				proxyObject.add("ui", uiArray);
				proxyObject.add("properties", propertiesArray);
				
				// Add the "time" array to the "data" object.
				JsonArray timeArray = new JsonArray();
				for (Double time : times) {
					timeArray.add(new JsonPrimitive(time));
				}
				dataObject.add("time", timeArray);
								
				// Add a placeholder property for the first space.
				uiArray.add(new JsonObject());
				propertiesArray.add(new JsonObject());
				
				// Add the representation property to the "ui" and "properties".
				object = new JsonObject();
				object.addProperty("name", "Representation");
				array = new JsonArray();
				array.add(new JsonPrimitive("Surface"));
				array.add(new JsonPrimitive("Wireframe"));
				object.add("values", array);
				uiArray.add(object);
				object = new JsonObject();
				object.addProperty("name", "Representation");
				object.addProperty("value", "Surface");
				propertiesArray.add(object);
				
				// Add the "ui" and "properties" objects for each property.
				Map<Integer, FakeProxyFeature> map = new TreeMap<Integer, FakeProxyFeature>();
				for (FakeProxyFeature property : properties) {
					map.put(property.index, property);
				}
				for (FakeProxyFeature property : map.values()) {
					// Add the property to the "ui" array.
					object = new JsonObject();
					object.addProperty("name", property.name);
					array = new JsonArray();
					for (String allowedValue : property.allowedValues) {
						array.add(new JsonPrimitive(allowedValue));
					}
					object.add("values", array);
					uiArray.add(object);
					
					// Add the property to the "properties" array.
					object = new JsonObject();
					object.addProperty("name", property.propertyName);
					// The value for a discrete-multi property needs to be a
					// JsonArray.
					if (property
							.getPropertyType() == PropertyType.DISCRETE_MULTI) {
						array = new JsonArray();
						array.add(new JsonPrimitive(property.initialValue));
						object.add("value", array);
					} else {
						object.addProperty("value", property.initialValue);
					}
					propertiesArray.add(object);
				}
				
				return proxyObject;
			}
		});
		
		responseMap.put("pv.proxy.manager.update", new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				JsonObject object = new JsonObject();
				object.addProperty("success", true);
				return object;
			}
		});
	}
	
	public void setTimes(double... times) {
		this.times.clear();
		for (double time : times) {
			this.times.add(time);
		}
	}
	
	public void addProxyProperty(FakeProxyFeature property) {
		properties.add(property);
	}

	/**
	 * Runs a callable that simply returns true.
	 */
	@Override
	public Future<Boolean> connect(String url) {
		return requestExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return true;
			}
		});
	}

	/**
	 * Returns a 1x1 blank JPEG image.
	 */
	@Override
	public Future<JsonObject> render(final int viewId, int quality, int width,
			int height) {
		return requestExecutor.submit(new Callable<JsonObject>() {
			@Override
			public JsonObject call() throws Exception {
				// Return an object containing a blank image.
				JsonObject object = new JsonObject();

				// Create a 1x1 empty JPEG image.
				BufferedImage img = new BufferedImage(1, 1,
						BufferedImage.TYPE_3BYTE_BGR);
				img.setRGB(0, 0, 0);
				// Convert that to a byte array.
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", outputStream);
				byte[] bytes = outputStream.toByteArray();
				// Convert that to an encoded String.
				String base64Image = null;
				base64Image = DatatypeConverter.printBase64Binary(bytes);

				// Create a JsonArray for the size of the image.
				JsonArray size = new JsonArray();
				size.add(new JsonPrimitive(1));
				size.add(new JsonPrimitive(1));

				// Fill out the expected response for viewport.image.render.
				object.add("image", new JsonPrimitive(base64Image));
				object.add("stale", new JsonPrimitive(false));
				object.add("mtime", new JsonPrimitive(0));
				object.add("size", size);
				object.add("format", new JsonPrimitive("jpeg;base64"));
				object.add("global_id", new JsonPrimitive(viewId));
				object.add("localTime", new JsonPrimitive(0));
				object.add("workTime", new JsonPrimitive(0));

				return object;
			}
		});
	}

	/**
	 * Does nothing yet.
	 */
	@Override
	public Future<JsonObject> event(int viewId, double x, double y,
			String action, boolean[] mouseState) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * If the method is specified in {@link #responseMap}, then the associated
	 * {@code Callable} is called. Otherwise, a new {@code Callable} that
	 * returns an empty {@link JsonObject} is constructed.
	 */
	@Override
	public Future<JsonObject> call(String method, JsonArray args) {
		Callable<JsonObject> callable = responseMap.get(method);
		if (callable == null) {
			callable = new Callable<JsonObject>() {
				@Override
				public JsonObject call() throws Exception {
					return new JsonObject();
				}
			};
		}
		return requestExecutor.submit(callable);
	}

	/**
	 * Does nothing.
	 */
	@Override
	public Future<Boolean> disconnect() {
		// Nothing to do.
		return null;
	}

}
