package org.eclipse.ice.sassena;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class builds the Sassena Incoherent Model Builder. It inherits its operations
 * from ItemBuilder.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SassenaIncoherentModelBuilder implements ItemBuilder {

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public String getItemName() {
		return "Sassena Incoherent Model";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(Interface projectSpace)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public Item build(IProject projectSpace) {
		SassenaIncoherentModel sassena = new SassenaIncoherentModel(projectSpace);
		sassena.setItemBuilderName(getItemName());
		return sassena;
	}

}
