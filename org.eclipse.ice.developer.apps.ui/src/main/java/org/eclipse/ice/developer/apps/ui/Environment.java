/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import apps.IEnvironment;

/**
 * Bean for the environment field
 * @author Anara Kozhokanova
 *
 */
public class Environment extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	TextField envNameTxtField;

	public Environment(IEnvironment env) {
		// vLayout = new VerticalLayout();
		envNameTxtField = new TextField();
		envNameTxtField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				env.setName(event.getText());
			}

		});
		envNameTxtField.setCaption("Environment Name:");
		addComponent(envNameTxtField);
		setSpacing(true);
		setMargin(new MarginInfo(false, true, false, true));
	}

}
