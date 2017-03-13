/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import apps.docker.impl.ContainerConfigurationImpl;

/**
 * @author Anara Kozhokanova
 *
 */
public class DockerForm extends HorizontalLayout{
	private BeanFieldGroup<ContainerConfigurationImpl> dockerFieldGroup;
	private VerticalLayout vLayout = new VerticalLayout();
	private Label titleLabel = new Label();
	
	@PropertyId("name")
	private TextField nameTxtField = new TextField();
	
	@PropertyId("ports")
	private TextField portsTxtField = new TextField();
	
	@PropertyId("volumesConfig")
	private TextField volumesTxtField = new TextField();
	
	@PropertyId("ephemeral")
	private CheckBox ephemeralChBox = new CheckBox();
	private TextArea dockerCommandsTxtArea = new TextArea();
	
	
	/**
	 * 
	 */
	public DockerForm() {
		
		// binding fields annotated '@PropertyId' to data model
		//this.dockerFieldGroup = new BeanFieldGroup<ContainerConfigurationImpl>(ContainerConfigurationImpl.class);
		//this.dockerFieldGroup.setItemDataSource(new ContainerConfigurationImpl());
		//this.dockerFieldGroup.bindMemberFields(this);
		//this.dockerFieldGroup.setBuffered(true);
		
		this.titleLabel.setCaption("Container Configuration:");
		this.nameTxtField.setCaption("Name:");
		this.portsTxtField.setCaption("Ports:");
		this.volumesTxtField.setCaption("Volumes:");
		this.ephemeralChBox.setCaption("Ephemeral");
		this.dockerCommandsTxtArea.setCaption("Additional Docker file settings");
		
		vLayout.addComponents(titleLabel, nameTxtField, portsTxtField, volumesTxtField, ephemeralChBox);
		addComponents(vLayout, dockerCommandsTxtArea);
		setSpacing(true);
	}
}
