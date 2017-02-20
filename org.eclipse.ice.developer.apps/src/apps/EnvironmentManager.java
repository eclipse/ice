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
 *   <li>{@link apps.EnvironmentManager#getBuilder <em>Builder</em>}</li>
 * </ul>
 *
 * @see apps.EnvironmentPackage#getEnvironmentManager()
 * @model
 * @generated
 */
public interface EnvironmentManager extends EObject {
	/**
	 * Returns the value of the '<em><b>Builder</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Builder</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Builder</em>' reference.
	 * @see #setBuilder(IEnvironmentBuilder)
	 * @see apps.EnvironmentPackage#getEnvironmentManager_Builder()
	 * @model
	 * @generated
	 */
	IEnvironmentBuilder getBuilder();

	/**
	 * Sets the value of the '{@link apps.EnvironmentManager#getBuilder <em>Builder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Builder</em>' reference.
	 * @see #getBuilder()
	 * @generated
	 */
	void setBuilder(IEnvironmentBuilder value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Create a new Environment from the provided JSON properties string. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment createEnvironment(String properties);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return a list of String names for each existing Environment. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	EList<String> listExisting();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Load the Environment with the provided String name. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment loadExisting(String name);

} // EnvironmentManager
