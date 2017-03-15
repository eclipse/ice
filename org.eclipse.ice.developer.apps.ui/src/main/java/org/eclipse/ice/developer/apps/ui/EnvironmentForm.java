/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Anara Kozhokanova
 *
 */
public class EnvironmentForm extends HorizontalLayout {
	DockerForm dockerView = new DockerForm();
	FolderForm folderView = new FolderForm();
	private VerticalLayout vLayout = new VerticalLayout();
	private OptionGroup installTypeOptGroup = new OptionGroup();
	private Label titleLabel = new Label();
	
	/**
	 * 
	 */
	public EnvironmentForm() {
		this.installTypeOptGroup.setCaption("Install Type:");
		this.installTypeOptGroup.addItem("Docker");
		this.installTypeOptGroup.addItem("Folder");
		this.installTypeOptGroup.setValue("Docker");
		
		this.titleLabel.setCaption("Environment Setup");
		
		
		//this.titleLabel.addStyleName(ValoTheme.LABEL_H1);
		vLayout.addComponents(titleLabel, installTypeOptGroup);
		vLayout.setMargin(new MarginInfo(false,true,false,true));
		vLayout.setSpacing(true);
		addComponents(vLayout, dockerView);
		
		installTypeOptGroup.addValueChangeListener( e -> {
			if (this.installTypeOptGroup.getValue().equals("Docker")) {
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
