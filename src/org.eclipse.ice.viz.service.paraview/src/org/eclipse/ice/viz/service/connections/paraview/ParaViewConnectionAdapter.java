package org.eclipse.ice.viz.service.connections.paraview;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.connections.ConnectionAdapter;

import com.kitware.vtk.web.VtkWebClient;
import com.kitware.vtk.web.VtkWebClientHttpImpl;

public class ParaViewConnectionAdapter extends ConnectionAdapter<VtkWebClient> {

	@Override
	protected VtkWebClient openConnection() {

		boolean connected = false;
		VtkWebClient client = null;

		try {
			client = new VtkWebClientHttpImpl();
			String host = getConnectionProperty("host");
			String port = getConnectionProperty("port");
			String url = "http://" + host + ":" + port + "/rpc-http/";
			connected = client.connect(url).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (!connected) {
			client = null;
		}

		return client;
	}

	@Override
	protected boolean closeConnection(VtkWebClient connection) {
		connection.close();
		return true;
	}

	@Override
	public boolean setConnectionProperties(List<Entry> row) {
		// We need to get the following information:
		// host
		// port

		// TODO Update this when we can launch the server.

		boolean changed = false;

		if (row != null && row.size() >= 3) {
			objectName = row.get(0).getValue();
			changed |= setConnectionProperty("host", row.get(1).getValue());
			changed |= setConnectionProperty("port", row.get(2).getValue());
		}

		return changed;
	}

}
