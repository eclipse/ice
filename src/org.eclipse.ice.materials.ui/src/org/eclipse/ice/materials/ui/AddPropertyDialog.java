package org.eclipse.ice.materials.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This is a JFace Dialog for adding properties to materials in the materials Database. 
 * It has two text fields: A name for the property and a value for the property that should be 
 * a String and a double, respectively. 
 * 
 * 
 * @author Kasper Gammeltoft
 *
 */
public class AddPropertyDialog extends Dialog {
	
	
	/**
	 * The material property to return after the dialog closes. 
	 */
	MaterialProperty newProperty;
	
	/**
	 * Text field for holding the name of the property to be added. 
	 */
	Text nameText;
	
	
	/**
	 * Text field for holding the value of the property to be added. 
	 */
	Text valueText;
	

	/**
	 * The constructor. Creates a new Dialog for adding properties to materials. 
	 * @param parentShell
	 */
	public AddPropertyDialog(Shell parentShell) {
		super(parentShell);
		newProperty = new MaterialProperty();
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		//Create the composite to hold the text fields and labels 
		Composite comp = (Composite) super.createDialogArea(parent);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//Create property name composite for storing that input
		Composite propertyName = new Composite(comp, SWT.NONE);
		propertyName.setLayout(new GridLayout(2, false));
		propertyName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		//The label for the property's name input
		Label nameLbl = new Label(propertyName, SWT.NONE);
		nameLbl.setText("Property: ");
		
		//The text field for the property's name
		nameText = new Text(propertyName, SWT.BORDER | SWT.LEAD);
		
		//Create the property value composite for storing that input
		Composite propertyValue = new Composite(comp, SWT.NONE);
		propertyValue.setLayout(new GridLayout(2, false));
		propertyValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		//The label for the property's value input
		Label valLbl = new Label(propertyValue, SWT.NONE);
		valLbl.setText("Value: ");
		
		//The text field for the property's value
		valueText = new Text(propertyValue, SWT.BORDER | SWT.LEAD);
		
		return comp;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell shell){
		super.configureShell(shell);
		shell.setText("Add Property");
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		
		//Locks the current values in the text fields into a MaterialProperty to add to the database. 
		
		//Gets the text for the name.
		newProperty.key = nameText.getText();
		
		//Gets the text for the value.
		String val = valueText.getText();
		double dVal = 0.0;
		
		//If the text for the value is a number, will set that number as the value. Otherwise it will be zero
		try{
			dVal = Double.parseDouble(val);
		} catch(Exception e){
			e.printStackTrace();
		}
		newProperty.value = dVal;
		super.okPressed();
	}

	/**
	 * Returns the new material property.
	 * @return
	 * 			The material property that was created by this dialog to be added to a material. 
	 */
	public MaterialProperty getSelection() {
		return newProperty;
	}
	

}
