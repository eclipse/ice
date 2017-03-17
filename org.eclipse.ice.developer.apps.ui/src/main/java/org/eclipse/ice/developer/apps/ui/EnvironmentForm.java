/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

import apps.IEnvironment;

/**
 * @author Anara Kozhokanova
 *
 */
public class EnvironmentForm extends HorizontalLayout {
	private DockerForm dockerView;
	private FolderForm folderView;
	private VerticalLayout vLayout;
	private OptionGroup installTypeOptGroup;
	private Label titleLabel;
	
	private IEnvironment environment;
	
	/**
	 * 
	 */
	public EnvironmentForm(IEnvironment env) {
		environment = env;
		dockerView = new DockerForm(environment);
		folderView = new FolderForm();
		vLayout  = new VerticalLayout();
		titleLabel = new Label();
		installTypeOptGroup = new OptionGroup();
		installTypeOptGroup.setCaption("Install Type:");
		installTypeOptGroup.addItem("Docker");
		installTypeOptGroup.addItem("Folder");
		installTypeOptGroup.setValue("Docker");
		
		titleLabel.setCaption("Environment Setup");
		
		//this.titleLabel.addStyleName(ValoTheme.LABEL_H1);
		vLayout.addComponents(titleLabel, installTypeOptGroup);
		vLayout.setMargin(new MarginInfo(false,true,false,true));
		vLayout.setSpacing(true);
		addComponents(vLayout, dockerView);
		
		installTypeOptGroup.addValueChangeListener( e -> {
			if (installTypeOptGroup.getValue().equals("Docker")) {
				if (folderView.isVisible()) {
					removeComponent(folderView);
				}
				addComponent(dockerView);
				
				
			} else {
				if (dockerView.isVisible()) {
					removeComponent(dockerView);				
				}
				addComponent(folderView);
			}			
		});
	}
}
