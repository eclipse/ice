/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Anara Kozhokanova
 *
 */
public class AddRepoWindow extends Window {

	/**
	 * 
	 */
	public AddRepoWindow() {
		super("Clone Git Repository");
		center();
		VerticalLayout vLayout = new VerticalLayout();
		HorizontalLayout btnsLayout = new HorizontalLayout();
		Label captionLabel = new Label("Specify location of the source repository:");
		TextField uriTextField = new TextField("URI:");
		TextField branchTextField = new TextField("Branch:");
		Button cancelButton = new Button("Cancel", e -> {
			close(); 
			uriTextField.clear();
			branchTextField.clear();
			uriTextField.focus();
		});
		Button okButton = new Button("OK", e -> {
			close();
			uriTextField.clear();
			branchTextField.clear();
			uriTextField.focus();
		});
		uriTextField.setWidth("400px");
		uriTextField.focus();
		branchTextField.setWidth("400px");
		cancelButton.setWidth("130px");
		okButton.setWidth("130px");
		btnsLayout.addComponents(cancelButton, okButton);
		btnsLayout.setSpacing(true);
		btnsLayout.setHeight("100px");
		btnsLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		btnsLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
		
		
		vLayout.addComponents(captionLabel, uriTextField, branchTextField, btnsLayout);
		vLayout.setComponentAlignment(btnsLayout, Alignment.BOTTOM_RIGHT);
		vLayout.setSpacing(true);
		vLayout.setMargin(true);
		setContent(vLayout);
	}

	/**
	 * @param caption
	 */
	public AddRepoWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param caption
	 * @param content
	 */
	public AddRepoWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}

}
