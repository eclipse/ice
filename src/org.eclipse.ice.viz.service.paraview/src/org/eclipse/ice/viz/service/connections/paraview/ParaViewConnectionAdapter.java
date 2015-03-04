package org.eclipse.ice.viz.service.connections.paraview;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.connections.ConnectionAdapter;

import com.kitware.vtk.web.VtkWebClient;
import com.kitware.vtk.web.VtkWebClientHttpImpl;

/**
 * This class provides a {@link ConnectionAdapter} that wraps a
 * {@link VtkWebClient}. It handles connecting and disconnecting as well as
 * updating the required connection properties.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnectionAdapter extends ConnectionAdapter<VtkWebClient> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionAdapter#openConnection
	 * ()
	 */
	@Override
	protected VtkWebClient openConnection() {

		// Set the default return value.
		VtkWebClient client = null;

		// Try to create and connect to a VtkWebClient.
		boolean connected = false;
		try {
			// Create an HTTP implementation of the VtkWebClient.
			client = new VtkWebClientHttpImpl();
			// Get the host and port from the connection properties.
			String host = getConnectionProperty("host");
			String port = getConnectionProperty("port");
			// Set up the HTTP URL.
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
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionAdapter#closeConnection
	 * (java.lang.Object)
	 */
	@Override
	protected boolean closeConnection(VtkWebClient connection) {
		boolean closed = false;
		// To close the connection, we need only tell it to close.
		if (connection != null) {
			connection.close();
			closed = true;
		}
		return closed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionAdapter#
	 * setConnectionProperties(java.util.List)
	 */
	@Override
	public boolean setConnectionProperties(List<Entry> row) {
		// We need to get the following information:
		// host
		// port

		// TODO Update this when we can launch the python server.

		boolean changed = false;

		if (row != null && row.size() >= 3) {
			// Update the name of the connection.
			objectName = row.get(0).getValue();

			// Update the required properties. If a property change requires the
			// connection to be reset, set the changed flag to true.
			changed |= setConnectionProperty("host", row.get(1).getValue());
			changed |= setConnectionProperty("port", row.get(2).getValue());
		}

		return changed;
	}

}
