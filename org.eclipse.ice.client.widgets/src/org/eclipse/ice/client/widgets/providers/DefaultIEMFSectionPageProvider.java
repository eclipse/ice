package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.EMFSectionPage;
import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

public class DefaultIEMFSectionPageProvider extends DefaultPageProvider implements IEMFSectionPageProvider{
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components){
		ArrayList<IFormPage> pages = new ArrayList<IFormPage>();
		EMFComponent emfComponent = null;
		EMFSectionPage emfPage = null;
		if (components.size() > 0) {
			for (Component comp : components) {
				emfComponent = (EMFComponent) comp;
				if (emfComponent != null) {
					// Make the EMFSectionPage
					emfPage = new EMFSectionPage(formEditor, emfComponent.getName(),
							emfComponent.getName());
					// Set the EMFComponent
					emfPage.setEMFComponent(emfComponent);
					pages.add(emfPage);
				}
			}
		}
		
		return pages;
	}
}
