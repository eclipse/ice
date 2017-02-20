/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SC Environment Data Manager</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager#getScenvironments <em>Scenvironments</em>}</li>
 * </ul>
 *
 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironmentDataManager()
 * @model
 * @generated
 */
public interface SCEnvironmentDataManager extends EObject {
	/**
	 * Returns the value of the '<em><b>Scenvironments</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenvironments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenvironments</em>' reference list.
	 * @see org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage#getSCEnvironmentDataManager_Scenvironments()
	 * @model
	 * @generated
	 */
	EList<SCEnvironment> getScenvironments();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<SCEnvironment> listExistingSCEenv();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	SCEnvironment createSCEnvironment();

} // SCEnvironmentDataManager
