/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import apps.AppsFactory;
import apps.IEnvironment;

/**
 * @author Anara Kozhokanova
 *
 */
public class AddOSWindow extends Window {
	private VerticalLayout vLayout;
	private VerticalLayout pkgListLayout;
	private HorizontalLayout btnsLayout;
	private CssLayout addPkgLayout;
	private Label captionLabel;
	
	@PropertyId("name")
	private TextField osTextField;
	
	private Button addPkgButton;
	private Button cancelButton;
	private Button okButton;
	
	// create a reference to basket
	private VerticalLayout basket;
	
	private IEnvironment environment;
	
	public AddOSWindow(IEnvironment env, VerticalLayout pkgLayout) {
		super("Add OS Package");
		environment = env;
		center();
		basket = pkgLayout;
		vLayout = new VerticalLayout();
		pkgListLayout = new VerticalLayout();
		btnsLayout = new HorizontalLayout();
		addPkgLayout = new CssLayout();
		
		captionLabel = new Label("Enter package name:");
		osTextField = new TextField();
		addPkgButton = new Button();
		
		cancelButton = new Button("Cancel", e -> {
			close(); 
			pkgListLayout.removeAllComponents();
			osTextField.clear();
			osTextField.focus();
		});
		
		okButton = new Button("OK", e -> {
			close(); 
			pkgListLayout.removeAllComponents();
			osTextField.clear();
			osTextField.focus();			
		});
		
		osTextField.setWidth("400px");
		cancelButton.setWidth("130px");
		okButton.setWidth("130px");
		okButton.setEnabled(false);
		okButton.setDescription("Add at least one package");
		osTextField.focus();
		
		btnsLayout.addComponents(cancelButton, okButton);
		btnsLayout.setSpacing(true);
		btnsLayout.setHeight("100px");
		btnsLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		btnsLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
		
		addPkgButton.setIcon(FontAwesome.PLUS);
		addPkgButton.addClickListener( e -> {
			// first check if the field is not empty
			if (!osTextField.getValue().isEmpty()) {
				// set 'ok' button enabled and update its tooltip
				okButton.setEnabled(true);
				okButton.setDescription("Add package(s) to the basket.");
				// display what package was entered
				pkgListLayout.addComponent(new Label(osTextField.getValue() 
						+ " - is added!"));
				apps.OSPackage p = AppsFactory.eINSTANCE.createOSPackage();
				p.setName(osTextField.getValue());
				environment.getDependentPackages().add(p);
				
				// Add to the packages basket
				Label label = new Label();
				label.setCaption(osTextField.getValue());
				basket.addComponent(label);
				
				osTextField.clear();
				osTextField.focus();
			}
		});
		
		osTextField.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1L;

			@Override
		    public void handleAction(Object sender, Object target) {
				addPkgButton.click();
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
}
