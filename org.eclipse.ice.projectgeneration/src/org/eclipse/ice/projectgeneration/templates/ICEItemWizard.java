package org.eclipse.ice.projectgeneration.templates;

import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

public class ICEItemWizard extends NewPluginTemplateWizard {

	public ICEItemWizard() {
		super();
	}
	
	@Override
	public void init(IFieldData data) {
		super.init(data);
		setWindowTitle("New ICE Item Parameters");
	}
	
	@Override
	public ITemplateSection[] createTemplateSections() {
		return new ITemplateSection[] {new ICEItemTemplate()};
	}

}
