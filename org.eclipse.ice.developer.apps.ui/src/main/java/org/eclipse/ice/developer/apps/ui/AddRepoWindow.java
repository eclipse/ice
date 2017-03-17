/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Anara Kozhokanova
 *
 */
public class AddRepoWindow extends Window {
	private VerticalLayout vLayout;
	private HorizontalLayout btnsLayout;
	private Label captionLabel;
	
	@PropertyId("link")
	private TextField uriTextField;
	@PropertyId("branch")
	private TextField branchTextField;
	@PropertyId("name")
	private TextField nameTextField;
	
	private Button cancelButton;
	private Button okButton;
	
	// create binder
	private BeanFieldGroup<SourcePackage> binder;

	// create container
	private BeanContainer<String, SourcePackage> container;
	
	
	/**
	 * 
	 */
	public AddRepoWindow() {
		super("Clone Git Repository");
		center();
		vLayout = new VerticalLayout();
		btnsLayout = new HorizontalLayout();
		captionLabel = new Label("Specify location of the source repository:");
		uriTextField = new TextField("URI:");
		branchTextField = new TextField("Branch:");
		nameTextField = new TextField("Name:");
		
		binder = new BeanFieldGroup<SourcePackage>(SourcePackage.class);
		// create bean
		binder.setItemDataSource(new SourcePackage());
		// bind member field 'osTextField' to the OSPackage bean
		binder.bindMemberFields(this);
		// set the binder's buffer
		binder.setBuffered(true);
		// create container that will store beans
		container = new BeanContainer<String, SourcePackage>(SourcePackage.class);
		
		cancelButton = new Button("Cancel", e -> {
			close();
			nameTextField.clear();
			uriTextField.clear();
			branchTextField.clear();
			nameTextField.focus();
		});
		
		okButton = new Button("OK", e -> {
			
			if (!uriTextField.getValue().isEmpty() && !nameTextField.getValue().isEmpty()) {
				try {
					// validate and get data
					binder.commit();
					// after successful validation add data to container
					container.addItem("sourceId", binder.getItemDataSource().getBean());
				} catch (CommitException e1) {
					Notification.show("Something went wrong :(",
							Notification.Type.WARNING_MESSAGE);
				}
			}
			close();
			nameTextField.clear();
			uriTextField.clear();
			branchTextField.clear();
			nameTextField.focus();
		});
		
		okButton.setEnabled(false);
		okButton.setDescription("Specify at least one package source.");
		uriTextField.setWidth("400px");
		nameTextField.setWidth("400px");
		branchTextField.setWidth("400px");
		nameTextField.focus();
		
		// set 'ok' button enabled and update its tooltip
		// if uri is entered by user
		uriTextField.addValueChangeListener( e -> {
			if (!uriTextField.getValue().isEmpty()) {
				okButton.setEnabled(true);
				okButton.setDescription("Add package to the basket.");
			}
			
		});
		
		cancelButton.setWidth("130px");
		okButton.setWidth("130px");
		btnsLayout.addComponents(cancelButton, okButton);
		btnsLayout.setSpacing(true);
		btnsLayout.setHeight("100px");
		btnsLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		btnsLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
				
		vLayout.addComponents(captionLabel, nameTextField, uriTextField, branchTextField, btnsLayout);
		vLayout.setComponentAlignment(btnsLayout, Alignment.BOTTOM_RIGHT);
		vLayout.setSpacing(true);
		vLayout.setMargin(true);
		setContent(vLayout);
	}


	/**
	 * @return container with beans if any
	 */
	public BeanContainer<String, SourcePackage> getContainer() {
		return container;
	}
}
