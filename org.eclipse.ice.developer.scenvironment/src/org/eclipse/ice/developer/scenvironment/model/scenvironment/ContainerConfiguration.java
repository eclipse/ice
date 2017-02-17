/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#getPorts <em>Ports</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#getVolumes <em>Volumes</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#isIsEphemeral <em>Is Ephemeral</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getContainerConfiguration()
 * @model
 * @generated
 */
public interface ContainerConfiguration extends InstallerTypeConfiguration {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' attribute list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getContainerConfiguration_Ports()
	 * @model required="true"
	 * @generated
	 */
	EList<Integer> getPorts();

	/**
	 * Returns the value of the '<em><b>Volumes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volumes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volumes</em>' attribute list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getContainerConfiguration_Volumes()
	 * @model
	 * @generated
	 */
	EList<String> getVolumes();

	/**
	 * Returns the value of the '<em><b>Is Ephemeral</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Ephemeral</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Ephemeral</em>' attribute.
	 * @see #setIsEphemeral(boolean)
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getContainerConfiguration_IsEphemeral()
	 * @model default="true"
	 * @generated
	 */
	boolean isIsEphemeral();

	/**
	 * Sets the value of the '{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration#isIsEphemeral <em>Is Ephemeral</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Ephemeral</em>' attribute.
	 * @see #isIsEphemeral()
	 * @generated
	 */
	void setIsEphemeral(boolean value);

} // ContainerConfiguration
