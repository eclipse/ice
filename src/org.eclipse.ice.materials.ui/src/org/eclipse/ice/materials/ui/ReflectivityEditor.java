 
package org.eclipse.ice.materials.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ReflectivityEditor {
	@Inject
	public ReflectivityEditor() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Text text = new Text(parent,SWT.None);
		text.setText("Reflectivity Editor");
	}
	
	
	
	
}