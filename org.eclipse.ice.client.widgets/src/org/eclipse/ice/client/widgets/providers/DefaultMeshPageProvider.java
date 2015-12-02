package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.ICEMeshPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

public class DefaultMeshPageProvider extends DefaultPageProvider implements IMeshPageProvider{
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components){
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
		
		MeshComponent meshComponent = new MeshComponent();

		// Get the GeometryComponent and create the GeometryPage.
		if (!(components.isEmpty())) {
			meshComponent = (MeshComponent) (components.get(0));

			if (meshComponent != null) {
				ICEMeshPage meshPage;
				// Make the MeshPage
				meshPage = new ICEMeshPage(formEditor, "MeshPid",
						meshComponent.getName());

				// Set the MeshComponent
				meshPage.setMeshComponent(meshComponent);
				pages.add(meshPage);
			}

		}

		return pages;
	}
}
