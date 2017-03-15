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
	
	// create binders
	private BeanFieldGroup<Docker> dockerBinder;
	private BeanFieldGroup<Folder> folderBinder;
	
	/**
	 * 
	 */
	public EnvironmentForm() {
		dockerView = new DockerForm();
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

		// binding fields annotated '@PropertyId' to data model
		dockerBinder = new BeanFieldGroup<Docker>(Docker.class);
		folderBinder = new BeanFieldGroup<Folder>(Folder.class);
		
		// set the docker and folder beans as the source
		dockerBinder.setItemDataSource(new Docker());
		folderBinder.setItemDataSource(new Folder());
		

		// bind fields of the beans to the forms
		dockerBinder.bindMemberFields(dockerView);
		folderBinder.bindMemberFields(folderView);
		
		// enable buffering
		dockerBinder.setBuffered(true);
		folderBinder.setBuffered(true);
	}

	/**
	 * @return the docker binder
	 */
	public BeanFieldGroup<Docker> getDockerBinder() {
		return dockerBinder;
	}

	/**
	 * @return the folder binder
	 */
	public BeanFieldGroup<Folder> getFolderBinder() {
		return folderBinder;
	}

}
