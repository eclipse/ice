/**
 */
package apps;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Manager</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The EnvironmentManager provides the functionality to create new Environments, list existing available Environments, or load an existing Environment. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.AppsPackage#getEnvironmentManager()
 * @model
 * @generated
 */
public interface EnvironmentManager extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Create a new Environment from the provided JSON properties string. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment createEnvironment(String type);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return a list of String names for each existing Environment. The existing environments are stored internally by Eclipse. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	EList<String> listExistingEnvironments();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load the Environment with the provided String name. Existing here means stored and tracked internally by Eclipse Preferences. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment loadExistingEnvironment(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load an environment from the provided file. The file type should be the EMF XMI persisted model. This is different from loadExisting as it provides a means to load an environment that Eclipse does not know about. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment loadEnvironmentFromFile(String file);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String persistToXMIString(IEnvironment environment);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void persistXMIToFile(IEnvironment environment, String fileName);

} // EnvironmentManager
