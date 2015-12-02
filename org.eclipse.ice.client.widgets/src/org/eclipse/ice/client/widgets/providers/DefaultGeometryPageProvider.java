package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ICEGeometryPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.ICEGeometry;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;


public class DefaultGeometryPageProvider extends DefaultPageProvider 
		implements IGeometryPageProvider {
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components){
		ICEGeometryPage geometryPage;
		GeometryComponent geometryComponent = new GeometryComponent();
		geometryComponent.setGeometry(new ICEGeometry());
		ArrayList<IFormPage> regeometryPage = new ArrayList<IFormPage>();
		// Get the GeometryComponent and create the GeometryPage.
		if (!(components.isEmpty())) {
			geometryComponent = (GeometryComponent) (components
					.get(0));

			if (geometryComponent != null) {

				// Make the GeometryPage
				geometryPage = new ICEGeometryPage(formEditor, "GPid",
						geometryComponent.getName());

				// Set the GeometryComponent
				geometryPage.setGeometry(geometryComponent);
				regeometryPage.add(geometryPage);
			}

		}
		
		return regeometryPage;
	}
}
