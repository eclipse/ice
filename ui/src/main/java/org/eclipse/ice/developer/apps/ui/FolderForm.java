/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Anara Kozhokanova
 *
 */
public class FolderForm extends VerticalLayout{
	private TextField dirNameTxtField = new TextField();
	
	/**
	 * 
	 */
	public FolderForm() {
		this.dirNameTxtField.setCaption("Directory:");
		addComponent(this.dirNameTxtField);
	}

}
