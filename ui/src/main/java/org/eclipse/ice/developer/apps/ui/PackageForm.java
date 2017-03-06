/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Anara Kozhokanova
 *
 */
public class PackageForm extends VerticalLayout {
	private CssLayout cssLayout = new CssLayout();
	private VerticalLayout vLayout = new VerticalLayout();
	private Label pkgDescrTxtField = new Label();
	
	/**
	 * @return the pkgDescrTxtField
	 */
	public Label getPkgDescrTxtField() {
		return pkgDescrTxtField;
	}

	/**
	 * @param pkgDescrTxtField the pkgDescrTxtField to set
	 */
	public void setPkgDescrTxtField(Label pkgDescrTxtField) {
		this.pkgDescrTxtField = pkgDescrTxtField;
	}

	private CheckBox pkgChBox = new CheckBox();
	
	/**
	 * @return the pkgChBox
	 */
	public CheckBox getPkgChBox() {
		return pkgChBox;
	}

	/**
	 * @param pkgChBox the pkgChBox to set
	 */
	public void setPkgChBox(CheckBox pkgChBox) {
		this.pkgChBox = pkgChBox;
	}

	public PackageForm() {
		this.pkgDescrTxtField.setWidth("95%");
		this.pkgDescrTxtField.setHeight("-1px");
		vLayout.addComponents(pkgChBox, pkgDescrTxtField);
		vLayout.setExpandRatio(pkgChBox, 0.0f);
		vLayout.setExpandRatio(pkgDescrTxtField, 1.0f);
		
		vLayout.setComponentAlignment(pkgDescrTxtField, Alignment.MIDDLE_RIGHT);
		addComponents(vLayout);
	}
}
