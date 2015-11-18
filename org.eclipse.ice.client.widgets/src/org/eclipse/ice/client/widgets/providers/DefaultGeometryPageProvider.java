package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.client.widgets.ICEGeometryPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.ICEGeometry;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ice.client.widgets.ICEGeometryPage;

public class DefaultGeometryPageProvider implements IGeometryPageProvider{
	public static final String PROVIDER_NAME = "default";
	
	@Override
	public String getName() {
		return PROVIDER_NAME;
	}

	@Override
	public ICEFormPage getPage(FormEditor formEditor,
			Map<String, ArrayList<Component>> componentMap) {
			GeometryComponent geometryComponent = new GeometryComponent();
			geometryComponent.setGeometry(new ICEGeometry());
			
			ICEGeometryPage geometryPage = null;

			if (!(componentMap.get("geometry").isEmpty())) {
				geometryComponent = (GeometryComponent) (componentMap.get("geometry").get(0));

				if (geometryComponent != null) {

					// Make the GeometryPage
					geometryPage = new ICEGeometryPage(formEditor, "GPid", geometryComponent.getName());
				
					// Set the GeometryComponent
					geometryPage.setGeometry(geometryComponent);
				}

			}
			return geometryPage;
	}
	

}
