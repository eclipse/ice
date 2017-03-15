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
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.EnvironmentManager#getEnvironmentCreator <em>Environment Creator</em>}</li>
 *   <li>{@link apps.EnvironmentManager#getEnvironmentStorage <em>Environment Storage</em>}</li>
 *   <li>{@link apps.EnvironmentManager#getConsole <em>Console</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getEnvironmentManager()
 * @model
 * @generated
 */
public interface EnvironmentManager extends EObject {
	/**
	 * Returns the value of the '<em><b>Environment Creator</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Environment Creator</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Environment Creator</em>' reference.
	 * @see #setEnvironmentCreator(EnvironmentCreator)
	 * @see apps.AppsPackage#getEnvironmentManager_EnvironmentCreator()
	 * @model
	 * @generated
	 */
	EnvironmentCreator getEnvironmentCreator();

	/**
	 * Sets the value of the '{@link apps.EnvironmentManager#getEnvironmentCreator <em>Environment Creator</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Environment Creator</em>' reference.
	 * @see #getEnvironmentCreator()
	 * @generated
	 */
	void setEnvironmentCreator(EnvironmentCreator value);

	/**
	 * Returns the value of the '<em><b>Environment Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Environment Storage</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Environment Storage</em>' reference.
	 * @see #setEnvironmentStorage(EnvironmentStorage)
	 * @see apps.AppsPackage#getEnvironmentManager_EnvironmentStorage()
	 * @model required="true"
	 * @generated
	 */
	EnvironmentStorage getEnvironmentStorage();

	/**
	 * Sets the value of the '{@link apps.EnvironmentManager#getEnvironmentStorage <em>Environment Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Environment Storage</em>' reference.
	 * @see #getEnvironmentStorage()
	 * @generated
	 */
	void setEnvironmentStorage(EnvironmentStorage value);

	/**
	 * Returns the value of the '<em><b>Console</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Console</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Console</em>' containment reference.
	 * @see #setConsole(EnvironmentConsole)
	 * @see apps.AppsPackage#getEnvironmentManager_Console()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EnvironmentConsole getConsole();

	/**
	 * Sets the value of the '{@link apps.EnvironmentManager#getConsole <em>Console</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Console</em>' containment reference.
	 * @see #getConsole()
	 * @generated
	 */
	void setConsole(EnvironmentConsole value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Create a new Environment from the provided JSON properties string. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment create(String dataString);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return a list of String names for each existing Environment. The existing environments are stored internally by Eclipse. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	EList<String> list();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load the Environment with the provided String name. Existing here means stored and tracked internally by Eclipse Preferences. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment get(String environmentName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load an environment from the provided file. The file type should be the EMF XMI persisted model. This is different from loadExisting as it provides a means to load an environment that Eclipse does not know about. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment loadFromFile(String fileName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Persist the given IEnvironment to a String and return it. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	String persistToString(String environmentName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Persist the given IEnvironment to an EMF XMI file. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void persistToFile(String environmentName, String fileName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<String> listAvailableSpackPackages();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Persist all IEnvironments to the Eclipse preference store. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void persistEnvironments();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Create a new IEnvironment of the given type. This is an empty IEnvironment that can then be filled by clients.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment createEmpty(String type);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load an IEnvironment from the existing XMI string. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment loadFromXMI(String xmiStr);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void loadEnvironments();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void startAllStoppedEnvironments();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void stopRunningEnvironments();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void deleteEnvironment(String name);

} // EnvironmentManager
