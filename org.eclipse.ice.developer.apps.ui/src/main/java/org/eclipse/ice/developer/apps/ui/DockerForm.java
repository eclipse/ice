/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

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
	private TextField nameTxtField = new TextField();
	private TextField portsTxtField = new TextField();
	private TextField volumesTxtField = new TextField();
	private CheckBox ephemeralChBox = new CheckBox();
	private TextArea dockerCommandsTxtArea = new TextArea();
	
	
	/**
	 * 
	 */
	public DockerForm() {
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
