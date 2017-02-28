/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Environment Creator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The EnvironmentCreator interface provides the functionality to create a new IEnvironment from a string containing the data for the IEnvironment. It is up to realizations to parse the format of the string. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.AppsPackage#getEnvironmentCreator()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface EnvironmentCreator extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Create an IEnvironment from the given data string. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment create(String dataString);

} // EnvironmentCreator
