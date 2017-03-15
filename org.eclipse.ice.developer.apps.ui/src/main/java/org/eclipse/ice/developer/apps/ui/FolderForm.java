/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Anara Kozhokanova
 *
 */
public class FolderForm extends VerticalLayout{
	
	@PropertyId("directory")
	private TextField dirNameTxtField;
	
	/**
	 * 
	 */
	public FolderForm() {
		dirNameTxtField = new TextField();
		dirNameTxtField.setCaption("Directory:");
		
		addComponent(dirNameTxtField);
		setSpacing(true);
	}
}
