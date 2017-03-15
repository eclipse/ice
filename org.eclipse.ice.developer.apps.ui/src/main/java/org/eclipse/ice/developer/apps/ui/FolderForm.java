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
	
	private BeanFieldGroup<Folder> folderFieldGroup;
	
	/**
	 * 
	 */
	public FolderForm() {
		dirNameTxtField = new TextField();
		dirNameTxtField.setCaption("Directory:");
		
		folderFieldGroup = new BeanFieldGroup<Folder>(Folder.class);
		folderFieldGroup.setItemDataSource(new Folder());
		folderFieldGroup.bindMemberFields(this);
		folderFieldGroup.setBuffered(true);
		
		addComponent(this.dirNameTxtField);
		setSpacing(true);
	}
}
