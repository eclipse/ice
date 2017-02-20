/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SC Environment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getImageName <em>Image Name</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAvailableOSs <em>Available OSs</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAvailablePackages <em>Available Packages</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getSelectedPackages <em>Selected Packages</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getAddedFiles <em>Added Files</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getInstallerType <em>Installer Type</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getConfigurationType <em>Configuration Type</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getSpackpackage <em>Spackpackage</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment()
 * @model
 * @generated
 */
public interface SCEnvironment extends EObject {
	/**
	 * Returns the value of the '<em><b>Image Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Image Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Image Name</em>' attribute.
	 * @see #setImageName(String)
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_ImageName()
	 * @model required="true"
	 * @generated
	 */
	String getImageName();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getImageName <em>Image Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Image Name</em>' attribute.
	 * @see #getImageName()
	 * @generated
	 */
	void setImageName(String value);

	/**
	 * Returns the value of the '<em><b>Available OSs</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Available OSs</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Available OSs</em>' attribute list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_AvailableOSs()
	 * @model required="true"
	 * @generated
	 */
	EList<String> getAvailableOSs();

	/**
	 * Returns the value of the '<em><b>Available Packages</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Available Packages</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Available Packages</em>' attribute list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_AvailablePackages()
	 * @model dataType="org.eclipse.ice.developer.scenvironment.model.scenvironment.Packages"
	 * @generated
	 */
	EList<Object> getAvailablePackages();

	/**
	 * Returns the value of the '<em><b>Selected Packages</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected Packages</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selected Packages</em>' attribute list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_SelectedPackages()
	 * @model dataType="org.eclipse.ice.developer.scenvironment.model.scenvironment.Packages"
	 * @generated
	 */
	EList<Object> getSelectedPackages();

	/**
	 * Returns the value of the '<em><b>Added Files</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Added Files</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Added Files</em>' attribute list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_AddedFiles()
	 * @model
	 * @generated
	 */
	EList<String> getAddedFiles();

	/**
	 * Returns the value of the '<em><b>Installer Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Installer Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Installer Type</em>' attribute.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum
	 * @see #setInstallerType(InstallerEnum)
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_InstallerType()
	 * @model required="true"
	 * @generated
	 */
	InstallerEnum getInstallerType();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getInstallerType <em>Installer Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Installer Type</em>' attribute.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum
	 * @see #getInstallerType()
	 * @generated
	 */
	void setInstallerType(InstallerEnum value);

	/**
	 * Returns the value of the '<em><b>Configuration Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configuration Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configuration Type</em>' containment reference.
	 * @see #setConfigurationType(InstallerTypeConfiguration)
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_ConfigurationType()
	 * @model containment="true" required="true"
	 * @generated
	 */
	InstallerTypeConfiguration getConfigurationType();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment#getConfigurationType <em>Configuration Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Configuration Type</em>' containment reference.
	 * @see #getConfigurationType()
	 * @generated
	 */
	void setConfigurationType(InstallerTypeConfiguration value);

	/**
	 * Returns the value of the '<em><b>Spackpackage</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spackpackage</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spackpackage</em>' containment reference list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironment_Spackpackage()
	 * @model containment="true"
	 * @generated
	 */
	EList<SpackPackage> getSpackpackage();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="org.eclipse.ice.developer.scenvironment.model.scenvironment.Packages"
	 * @generated
	 */
	EList<Object> searchPackages();

} // SCEnvironment
