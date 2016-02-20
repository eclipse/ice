/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.test;

import java.util.concurrent.ExecutionException;

import org.eclipse.eavp.viz.service.paraview.web.HttpParaViewWebClient;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.eavp.viz.service.paraview.widgets.ParaViewCanvas;
import org.eclipse.eavp.viz.service.paraview.widgets.ParaViewMouseAdapter;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This test class creates a new SWT Shell that contains a
 * {@link ParaViewCanvas} pointing to a remote ParaView server.
 * 
 * @author Jordan Deyton
 *
 */
public class SWTExample {

	/**
	 * The ID for the ParaView view proxy on the remote server.
	 */
	private static int viewId;
	/**
	 * The ID for the ParaView view proxy on the remote server.
	 */
	private static int fileId;
	/**
	 * The ID for the ParaView view proxy on the remote server.
	 */
	private static int repId;
	/**
	 * The plot type to use.
	 */
	private static String plotType;

	/**
	 * The main method that lets this class be run as a Java application. It
	 * attempts to open a file on the specified remote machine using a ParaView
	 * Java client.
	 * 
	 * @param jArgs
	 *            The command line arguments.
	 */
	public static void main(String[] jArgs) {

		final boolean useExodus = false;

		// Set up the shell to contain the render panel.
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("SWT Example");
		shell.setLayout(new FillLayout());

		// Get the URL for the ParaView host.
		String url = null;
		InputDialog d = new InputDialog(shell, "ParaView Server",
				"Enter the remote ParaView server URL.",
				"http://localhost:8080/rpc-http/", new IInputValidator() {
					@Override
					public String isValid(String newText) {
						return newText != null && !newText.isEmpty() ? null
								: "Must not be empty";
					}
				});
		if (d.open() == Window.OK) {
			url = d.getValue();
		}

		if (url != null) {
			final IParaViewWebClient client = new HttpParaViewWebClient();

			// Try to connect.
			try {
				// if (!client.connect(url).get(1, TimeUnit.SECONDS)) {
				if (!client.connect(url).get()) {
					System.out.println("Failed to connect.");
					return;
				}
			} catch (InterruptedException e) {
				System.out.println("Connection attempt interrupted.");
				return;
			} catch (ExecutionException e) {
				System.out.println("Connection threw an uncaught exception.");
				return;
			}

			// Open a file.
			if (useExodus) {
				openExodusFile(client);
			} else {
				openSiloFile(client);
			}

			// Create the ParaView SWT Canvas.
			final ParaViewCanvas canvas = new ParaViewCanvas(shell, SWT.NONE);
			canvas.setClient(client);
			canvas.setViewId(viewId);

			// Add a mouse adapter to add mouse controls.
			ParaViewMouseAdapter mouseAdapter = new ParaViewMouseAdapter(client, viewId, canvas);
			mouseAdapter.setCanvas(canvas);

			try {
				JsonObject object;
				JsonArray args;

				// Set the representation proxy to show the mesh as
				// "Surface With Edges".
				args = new JsonArray();
				JsonArray updatedProperties = new JsonArray();
				JsonObject repProperty = new JsonObject();
				repProperty.addProperty("id", Integer.toString(repId));
				repProperty.addProperty("name", "Representation");
				repProperty.addProperty("value", "Surface With Edges");
				updatedProperties.add(repProperty);

				// Update the properties that were configured.
				args = new JsonArray();
				args.add(updatedProperties);
				object = client.call("pv.proxy.manager.update", args).get();
				if (!object.get("success").getAsBoolean()) {
					System.out.println("Failed to set the representation: ");
					JsonArray array = object.get("errorList").getAsJsonArray();
					for (int i = 0; i < array.size(); i++) {
						System.out.println(array.get(i));
					}
				}

				// Populate the exodus view with something useful.
				if (useExodus) {
					populateExodusView(client);
				} else {
					populateSiloView(client);
				}

				// Set the representation to color based on the plot type.
				args = new JsonArray();
				args.add(new JsonPrimitive(Integer.toString(repId)));
				args.add(new JsonPrimitive("ARRAY"));
				args.add(new JsonPrimitive("POINTS"));
				args.add(new JsonPrimitive(plotType));
				args.add(new JsonPrimitive("Magnitude"));
				args.add(new JsonPrimitive(0));
				args.add(new JsonPrimitive(true));
				object = client.call("pv.color.manager.color.by", args).get();

				// Set the visibility of the legend to true.
				args = new JsonArray();
				JsonObject legendVisibilities = new JsonObject();
				legendVisibilities.addProperty(Integer.toString(fileId), true);
				args.add(legendVisibilities);
				object = client.call(
						"pv.color.manager.scalarbar.visibility.set", args)
						.get();

				// Auto-scale the color map to the data.
				args = new JsonArray();
				JsonObject scaleOptions = new JsonObject();
				scaleOptions.addProperty("type", "data");
				scaleOptions.addProperty("proxyId", fileId);
				args.add(scaleOptions);
				object = client.call(
						"pv.color.manager.rescale.transfer.function", args)
						.get();

				// Update the time frame to the last one. We can use any of
				// next, prev, first, or last.
				args = new JsonArray();
				args.add(new JsonPrimitive("last"));
				object = client.call("pv.vcr.action", args).get();
				System.out.println("The time was changed to "
						+ object.toString());

			} catch (Exception e) {
				e.printStackTrace();
			}

			// UI input loop below.
			shell.open();

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}

			// Stop the client and the canvas.
			canvas.dispose();
			client.disconnect();
			
			display.dispose();
			return;
		}
	}

	/**
	 * Opens an Exodus file and sets the {@link #plotType}.
	 * 
	 * @param client
	 *            The ParaView web client used to render the file.
	 */
	private static void openExodusFile(IParaViewWebClient client) {
		openFile(client, "mooseModel_out.e");
		plotType = "burnup";
	}

	/**
	 * Opens a Silo file and sets the {@link #plotType}.
	 * 
	 * @param client
	 *            The ParaView web client used to render the file.
	 */
	private static void openSiloFile(IParaViewWebClient client) {
		openFile(client, "output-Battery_1.1.silo");
		plotType = "Battery_/TemperatureP1";
	}

	/**
	 * Sets the data that is plotted for the Exodus file.
	 * 
	 * @param client
	 *            The ParaView web client used to render the file.
	 * @throws Exception
	 *             If there is an error while communicating with the client.
	 */
	private static void populateExodusView(IParaViewWebClient client)
			throws Exception {

		// Nothing to do in addition to the default behavior.

		return;
	}

	/**
	 * Sets the data that is plotted for the Silo file.
	 * 
	 * @param client
	 *            The ParaView web client used to render the file.
	 * @throws Exception
	 *             If there is an error while communicating with the client.
	 */
	private static void populateSiloView(IParaViewWebClient client)
			throws Exception {

		JsonArray args;
		JsonObject object;

		JsonArray updatedProperties = new JsonArray();

		// Update the "status" of the mesh/material/cell/point arrays.
		// For the silo file, we want to select Battery_/TemperatureP1.
		// FIXME I don't know why this is necessary, but not doing this prevents
		// data from being shown.
		JsonObject pointStatusProperty = new JsonObject();
		pointStatusProperty.addProperty("id", Integer.toString(fileId));
		pointStatusProperty.addProperty("name", "PointArrayStatus");
		JsonArray pointArrays = new JsonArray();
		pointArrays.add(new JsonPrimitive(plotType));
		pointStatusProperty.add("value", pointArrays);
		updatedProperties.add(pointStatusProperty);

		// Update the properties that were configured.
		args = new JsonArray();
		args.add(updatedProperties);
		object = client.call("pv.proxy.manager.update", args).get();
		if (!object.get("success").getAsBoolean()) {
			System.out.println("Failed to set the representation: ");
			JsonArray array = object.get("errorList").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				System.out.println(array.get(i));
			}
		}

		return;
	}

	/**
	 * Opens the file at the specified path on the server.
	 * 
	 * @param client
	 *            The ParaView web client used to open the file.
	 * @param path
	 *            The path to the file to open.
	 */
	private static void openFile(IParaViewWebClient client, String path) {
		// Create a new view on the server. A new view, file proxy, and
		// representation proxy will be created.
		try {
			JsonArray args = new JsonArray();
			args.add(new JsonPrimitive(path));
			JsonObject object;
			object = client.call("createView", args).get();
			System.out.println(object.toString());

			viewId = object.get("viewId").getAsInt();
			fileId = object.get("proxyId").getAsInt();
			repId = object.get("repId").getAsInt();
		} catch (InterruptedException e) {
			System.out.println("Connection attempt interrupted.");
			System.exit(0);
			return;
		} catch (ExecutionException e) {
			System.out.println("Connection threw an uncaught exception.");
			System.exit(0);
			return;
		}
	}
}
