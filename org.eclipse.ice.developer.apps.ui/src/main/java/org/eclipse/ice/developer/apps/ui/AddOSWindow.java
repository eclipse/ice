/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanContainer;
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
	
	// create binder
	private BeanFieldGroup<OSPackage> binder;
	
	// create container
	private BeanContainer<String, OSPackage> container;
	
	/**
	 * 
	 */
	public AddOSWindow() {
		super("Add OS Package");
		center();
		vLayout = new VerticalLayout();
		pkgListLayout = new VerticalLayout();
		btnsLayout = new HorizontalLayout();
		addPkgLayout = new CssLayout();
		
		captionLabel = new Label("Enter package name:");
		osTextField = new TextField();
		addPkgButton = new Button();
		
		binder = new BeanFieldGroup<OSPackage>(OSPackage.class);
		// create bean
		binder.setItemDataSource(new OSPackage());
		// bind member field 'osTextField' to the OSPackage bean
		binder.bindMemberFields(this);
		// set the binder's buffer
		binder.setBuffered(true);
		// create container that will store beans
		container = new BeanContainer<String, OSPackage>(OSPackage.class);
		// set id resolver
		container.setBeanIdProperty("name");
		
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
						+ "- is added!"));
				try {
					// validate and get data
					binder.commit();
					// after successful validation add data to container
					container.addBean(binder.getItemDataSource().getBean());
					// create a new bean
					binder.setItemDataSource(new OSPackage());
				} catch (CommitException e1) {
					Notification.show("Something went wrong :(",
							Notification.Type.WARNING_MESSAGE);
				}
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

	
	/**
	 * @return container with beans if any
	 */
	public BeanContainer<String, OSPackage> getContainer() {
		return container;
	}

}
