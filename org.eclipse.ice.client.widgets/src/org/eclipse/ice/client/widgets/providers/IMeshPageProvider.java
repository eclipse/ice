package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;

public interface IMeshPageProvider extends IPageProvider{
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.meshPageProvider";
	public static ArrayList<IMeshPageProvider> getProviders() throws CoreException {
		ExtensionHelper<IMeshPageProvider> extensionHelper = new ExtensionHelper<IMeshPageProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
	}
}
