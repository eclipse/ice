/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;


import org.eclipse.ice.developer.apps.docker.environment.EnvironmentFactory;
import org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment;
import org.eclipse.ice.docker.api.ContainerConfiguration;
import org.eclipse.ice.docker.api.DockerapiFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import apps.IEnvironment;

/**
 * @author Anara Kozhokanova
 *
 */
public class DockerForm extends HorizontalLayout{
	
	private VerticalLayout vLayout = new VerticalLayout();
	private Label titleLabel = new Label();
	
	private IEnvironment environment;
	
	private TextField nameTxtField;
	
	private TextField portsTxtField;
	
	private TextField volumesTxtField;
	
	private CheckBox ephemeralChBox;
	
//	private TextArea dockerCommandsTxtArea;
	
	/**
	 * 
	 */
	public DockerForm(IEnvironment env) {
		environment = env;
		ContainerConfiguration config = DockerapiFactory.eINSTANCE.createContainerConfiguration();
		((DockerEnvironment)environment).setContainerConfiguration(config);
		
		nameTxtField = new TextField();
		nameTxtField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				((DockerEnvironment)environment).getContainerConfiguration().setName(event.getText());
			}
		});
		
		portsTxtField = new TextField();
		portsTxtField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				((DockerEnvironment)environment).getContainerConfiguration().getPorts().add(Integer.valueOf(event.getText()));
			}
		});
				
		volumesTxtField = new TextField();
		volumesTxtField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				((DockerEnvironment)environment).getContainerConfiguration().setVolumesConfig(event.getText());
			}
		});
		
		ephemeralChBox = new CheckBox();
		ephemeralChBox.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("HELLO FROM EPHEMERAL: " + event.getProperty().getValue());
				
			}
		});
//		dockerCommandsTxtArea = new TextArea();
		
		titleLabel.setCaption("Container Configuration:");
		nameTxtField.setCaption("Name:");
		portsTxtField.setCaption("Ports:");
		volumesTxtField.setCaption("Volumes:");
		ephemeralChBox.setCaption("Ephemeral");
//		dockerCommandsTxtArea.setCaption("Additional Docker file settings");
		
		vLayout.addComponents(titleLabel, nameTxtField, portsTxtField, volumesTxtField, ephemeralChBox);
		addComponents(vLayout);
		setSpacing(true);
	}
}
