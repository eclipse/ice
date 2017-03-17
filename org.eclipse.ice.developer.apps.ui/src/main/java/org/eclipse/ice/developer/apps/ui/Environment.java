/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Bean for the environment field
 * @author Anara Kozhokanova
 *
 */
public class Environment extends VerticalLayout {
	//VerticalLayout vLayout;
	@PropertyId("name")
	TextField envNameTxtField;

	public Environment() {
		//vLayout = new VerticalLayout();
		envNameTxtField = new TextField();
    	envNameTxtField.setCaption("Environment Name:");
    	addComponent(envNameTxtField);
    	setSpacing(true);
    	setMargin(new MarginInfo(false,true,false,true));
	}


}
