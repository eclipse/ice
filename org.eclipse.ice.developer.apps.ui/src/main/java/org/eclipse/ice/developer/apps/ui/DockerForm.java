/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;


import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Anara Kozhokanova
 *
 */
public class DockerForm extends HorizontalLayout{
	
	private VerticalLayout vLayout = new VerticalLayout();
	private Label titleLabel = new Label();
	
	@PropertyId("name")
	private TextField nameTxtField;
	
	@PropertyId("ports")
	private TextField portsTxtField;
	
	@PropertyId("volumes")
	private TextField volumesTxtField;
	
	@PropertyId("ephemeral")
	private CheckBox ephemeralChBox;
	
	@PropertyId("commands")
	private TextArea dockerCommandsTxtArea;
	
	
	/**
	 * 
	 */
	public DockerForm() {
		
		nameTxtField = new TextField();
		portsTxtField = new TextField();
		volumesTxtField = new TextField();
		ephemeralChBox = new CheckBox();
		dockerCommandsTxtArea = new TextArea();
		
		titleLabel.setCaption("Container Configuration:");
		nameTxtField.setCaption("Name:");
		portsTxtField.setCaption("Ports:");
		volumesTxtField.setCaption("Volumes:");
		ephemeralChBox.setCaption("Ephemeral");
		dockerCommandsTxtArea.setCaption("Additional Docker file settings");
		
		vLayout.addComponents(titleLabel, nameTxtField, portsTxtField, volumesTxtField, ephemeralChBox);
		addComponents(vLayout, dockerCommandsTxtArea);
		setSpacing(true);
	}
}
