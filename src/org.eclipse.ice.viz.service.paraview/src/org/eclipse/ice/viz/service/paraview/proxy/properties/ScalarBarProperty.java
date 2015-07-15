package org.eclipse.ice.viz.service.paraview.proxy.properties;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractProxyProperty;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ScalarBarProperty extends AbstractProxyProperty {

	public ScalarBarProperty(IParaViewProxy proxy,
			ParaViewConnection connection) {
		super("Toggle Scalar Bar", proxy, connection);
	}

	@Override
	protected String findValue(ParaViewConnection connection) {
		return "Hide";
	}

	@Override
	protected Set<String> findAllowedValues(ParaViewConnection connection) {
		Set<String> allowedValues = new HashSet<String>();
		allowedValues.add("Show");
		allowedValues.add("Hide");
		return allowedValues;
	}

	@Override
	protected boolean setValueOnClient(String value,
			ParaViewConnection connection) {
		IParaViewWebClient widget = connection.getWidget();

		boolean updated = false;

		int fileId = proxy.getFileId();
		boolean show = "Show".equals(value) ? true : false;

		// Set the visibility of the legend to true.
		JsonObject legendVisibilities = new JsonObject();
		legendVisibilities.addProperty(Integer.toString(fileId), show);
		JsonArray args = new JsonArray();
		args.add(legendVisibilities);
		try {
			widget.call("pv.color.manager.scalarbar.visibility.set", args)
					.get();
			updated = true;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return updated;
	}

}
