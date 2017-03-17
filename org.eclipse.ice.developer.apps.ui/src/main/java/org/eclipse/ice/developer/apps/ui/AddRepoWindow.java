/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import apps.AppsFactory;
import apps.IEnvironment;

/**
 * @author Anara Kozhokanova
 *
 */
public class AddRepoWindow extends Window {
	private VerticalLayout vLayout;
	private HorizontalLayout btnsLayout;
	private Label captionLabel;
	
	private TextField uriTextField;
	private TextField branchTextField;
	private TextField nameTextField;
	
	private Button cancelButton;
	private Button okButton;
	
	private IEnvironment environment;
	
	
	/**
	 * 
	 */
	public AddRepoWindow(IEnvironment env, VerticalLayout basket) {
		super("Clone Git Repository");
		environment = env;
		center();
		apps.SourcePackage p = AppsFactory.eINSTANCE.createSourcePackage();
		env.setPrimaryApp(p);
		
		vLayout = new VerticalLayout();
		btnsLayout = new HorizontalLayout();
		captionLabel = new Label("Specify location of the source repository:");
		uriTextField = new TextField("URI:");
		uriTextField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				((apps.SourcePackage)environment.getPrimaryApp()).setRepoURL(event.getText());
			}
		});
		
		branchTextField = new TextField("Branch:");
		branchTextField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				((apps.SourcePackage)environment.getPrimaryApp()).setBranch(event.getText());
			}
		});
		nameTextField = new TextField("Name:");
		nameTextField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				((apps.SourcePackage)environment.getPrimaryApp()).setName(event.getText());
			}
		});
		
		cancelButton = new Button("Cancel", e -> {
			close();
			nameTextField.clear();
			uriTextField.clear();
			branchTextField.clear();
			nameTextField.focus();
		});
		
		okButton = new Button("OK", e -> {
			// Add to the packages basket
			Label label = new Label();
			label.setCaption(environment.getPrimaryApp().getName());
			basket.addComponent(label);
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
}
