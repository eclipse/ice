package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IGeometryPageProvider {
	public static final String EXTENSION_POINT_ID = "org.eclipse.ice.client.widgets.IGeometryPageProvider";
	
	public static IGeometryPageProvider[] getProviders() throws CoreException {
		Logger logger = LoggerFactory.getLogger(IGeometryPageProvider.class);
		IGeometryPageProvider[] GeometryPageProvider = null;
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT_ID);
		if(point != null){
			IConfigurationElement[] elements = point.getConfigurationElements();
			GeometryPageProvider = new IGeometryPageProvider[elements.length];
			for(int i = 0; i < elements.length; i++){
				GeometryPageProvider[i] = (IGeometryPageProvider)elements[i].createExecutableExtension("class");
			}
		}else {
			logger.error("Extension Point " + EXTENSION_POINT_ID + " does not exist");
		}
		
		return GeometryPageProvider;
	}

	/**
	 * This operation returns the name of the provider.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * This operation directs the provider to create and return all of its pages
	 * based on the provided set of pages.
	 * 
	 * @param componentMap
	 *            This map must contain the Components in the Form organized by
	 *            type. The type is the key and a string equal to one of "data,"
	 *            "output," "matrix," "masterDetails", "table," "geometry,"
	 *            "shape," "tree," "mesh," or "reactor." The value is a list
	 *            that stores all components of that type; DataComponent,
	 *            ResourceComponent, MatrixComponent, MasterDetailsComponent,
	 *            TableComponent, GeometryComponent, ShapeComponent,
	 *            TreeComponent, MeshComponent, ReactorComponent, etc. This is a
	 *            simulated multimap.
	 * @return the form pages created from the map
	 */
	public ICEFormPage getPage(FormEditor formEditor,
			Map<String, ArrayList<Component>> componentMap);
	
	
}
