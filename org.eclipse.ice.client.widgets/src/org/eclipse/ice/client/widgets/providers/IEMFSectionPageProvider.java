package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ExtensionHelper;

public interface IEMFSectionPageProvider extends IPageProvider {
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.IEMFSectionPageProvider";
	public static ArrayList<IEMFSectionPageProvider> getProviders() throws CoreException {
		ExtensionHelper<IEMFSectionPageProvider> extensionHelper = new ExtensionHelper<IEMFSectionPageProvider>();
		return extensionHelper.getExtensions(EXTENSION_POINT_ID);
		
	}
}
