/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Installer Type Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getInstallerTypeConfiguration()
 * @model abstract="true"
 * @generated
 */
public interface InstallerTypeConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getInstallerTypeConfiguration_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // InstallerTypeConfiguration
