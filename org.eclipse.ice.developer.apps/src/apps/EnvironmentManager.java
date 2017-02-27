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
	IEnvironment createEnvironment(String dataString);

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
	IEnvironment loadEnvironment(String environmentName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load an environment from the provided file. The file type should be the EMF XMI persisted model. This is different from loadExisting as it provides a means to load an environment that Eclipse does not know about. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment loadEnvironmentFromFile(String fileName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Persist the given IEnvironment to a String and return it. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	String persistToXMIString(String environmentName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Persist the given IEnvironment to an EMF XMI file. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void persistXMIToFile(String environmentName, String fileName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Connect to the IEnvironment with the given name if the IEnvironment is in the Stopped state. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void connectToExistingEnvironment(String environmentName);

} // EnvironmentManager
