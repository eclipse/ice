/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Anara Kozhokanova
 *
 */
public class AddOSWindow extends Window {

	/**
	 * 
	 */
	public AddOSWindow() {
		super("Add OS Package");
		center();
		VerticalLayout vLayout = new VerticalLayout();
		VerticalLayout pkgListLayout = new VerticalLayout();
		HorizontalLayout btnsLayout = new HorizontalLayout();
		CssLayout addPkgLayout = new CssLayout();
		
		Label captionLabel = new Label("Enter package name:");
		TextField osTextField = new TextField();
		Button addPkgButton = new Button();
		Button cancelButton = new Button("Cancel", e -> {
			close(); 
			pkgListLayout.removeAllComponents();
			osTextField.clear();
			osTextField.focus();
		});
		Button okButton = new Button("OK", e -> {
			close(); 
			pkgListLayout.removeAllComponents();
			osTextField.clear();
			osTextField.focus();
		});
		osTextField.setWidth("400px");
		cancelButton.setWidth("130px");
		okButton.setWidth("130px");
		osTextField.focus();
		btnsLayout.addComponents(cancelButton, okButton);
		btnsLayout.setSpacing(true);
		btnsLayout.setHeight("100px");
		btnsLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		btnsLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
		addPkgButton.setIcon(FontAwesome.PLUS);
		addPkgButton.addClickListener( e -> {
			if (!osTextField.getValue().isEmpty()) {
				pkgListLayout.addComponent(new Label(osTextField.getValue() 
						+ "- is added!"));
				osTextField.clear();
				osTextField.focus();
			}
		});
		addPkgLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		addPkgLayout.addComponents(osTextField, addPkgButton);
		vLayout.addComponents(captionLabel, addPkgLayout, pkgListLayout, btnsLayout);
		vLayout.setComponentAlignment(btnsLayout, Alignment.BOTTOM_RIGHT);
		vLayout.setSpacing(true);
		vLayout.setMargin(true);
		setContent(vLayout);
	}

	/**
	 * @param caption
	 */
	public AddOSWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param caption
	 * @param content
	 */
	public AddOSWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}

}
