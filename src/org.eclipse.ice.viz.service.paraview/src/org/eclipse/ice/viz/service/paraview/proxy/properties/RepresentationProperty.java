package org.eclipse.ice.viz.service.paraview.proxy.properties;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractProxyProperty;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RepresentationProperty extends AbstractProxyProperty {

	public RepresentationProperty(IParaViewProxy proxy,
			ParaViewConnection connection) {
		super("Representation", proxy, connection);
	}

	@Override
	protected String findValue(ParaViewConnection connection) {
		String value = null;

		int repId = proxy.getRepresentationId();

		JsonObject repProxy = connection.getProxyObject(repId);
		try {
			JsonArray properties = repProxy.get("properties").getAsJsonArray();
			JsonObject repObject = properties.get(1).getAsJsonObject();
			value = repObject.get("value").getAsString();
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return value;
	}

	@Override
	protected Set<String> findAllowedValues(ParaViewConnection connection) {
		Set<String> allowedValues = new HashSet<String>();

		int repId = proxy.getRepresentationId();

		JsonObject repProxy = connection.getProxyObject(repId);
		try {
			JsonArray ui = repProxy.get("ui").getAsJsonArray();
			JsonObject repObject = ui.get(1).getAsJsonObject();
			JsonArray values = repObject.get("values").getAsJsonArray();
			for (int i = 0; i < values.size(); i++) {
				allowedValues.add(values.get(i).getAsString());
			}
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return allowedValues;
	}

	@Override
	protected boolean setValueOnClient(String value,
			ParaViewConnection connection) {
		int repId = proxy.getRepresentationId();
		return setProxyProperty(repId, getName(), value);
	}

}
