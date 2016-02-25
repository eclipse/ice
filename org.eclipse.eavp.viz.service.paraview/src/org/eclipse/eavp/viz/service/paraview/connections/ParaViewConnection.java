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
package org.eclipse.eavp.viz.service.paraview.connections;

import java.util.concurrent.ExecutionException;

import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.paraview.web.HttpParaViewWebClient;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;

/**
 * Provides an {@link IVizConnection} for connecting to
 * {@link IParaViewWebClient}s. This connection specifically uses the
 * {@link HttpParaViewWebClient} implementation for the web client.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnection extends VizConnection<IParaViewWebClient> {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#connectToWidget()
	 */
	@Override
	protected IParaViewWebClient connectToWidget() {
		// Set the default return value.
		IParaViewWebClient client = null;

		// Try to create and connect to a ParaView web client.
		boolean connected = false;
		try {
			// Create an HTTP implementation of the ParaView web client..
			client = new HttpParaViewWebClient();
			// Set up the HTTP URL
			String host = getHost();
			String port = Integer.toString(getPort());
			String url = "http://" + host + ":" + port + "/rpc-http/";
			// Try to connect.
			connected = client.connect(url).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// If the connection was not successful, we should return null.
		if (!connected) {
			client = null;
		}

		return client;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.VizConnection#disconnectFromWidget(java.lang.Object)
	 */
	@Override
	protected boolean disconnectFromWidget(IParaViewWebClient widget) {
		boolean closed = false;
		// Attempt to disconnect, returning the success of the operation.
		if (widget != null) {
			try {
				closed = widget.disconnect().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return closed;
	}

}
