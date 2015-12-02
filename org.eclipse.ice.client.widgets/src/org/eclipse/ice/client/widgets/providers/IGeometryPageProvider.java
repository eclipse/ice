package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;

public interface  IGeometryPageProvider extends IPageProvider {
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.geometryPageProvider";
	public static ArrayList<IGeometryPageProvider> getProviders() throws CoreException {
		ExtensionHelper<IGeometryPageProvider> extensionHelper = new ExtensionHelper<IGeometryPageProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
	}
}
