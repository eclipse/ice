/**
 */
package apps;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The Environment is a concrete implementation of IEnvironment that provides functionality to create an Eclipse Project after derived classes have launched themselves. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.Environment#getProjectlauncher <em>Projectlauncher</em>}</li>
 * </ul>
 *
 * @see apps.EnvironmentPackage#getEnvironment()
 * @model
 * @generated
 */
public interface Environment extends IEnvironment {
	/**
	 * Returns the value of the '<em><b>Projectlauncher</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Projectlauncher</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projectlauncher</em>' reference.
	 * @see #setProjectlauncher(ProjectLauncher)
	 * @see apps.EnvironmentPackage#getEnvironment_Projectlauncher()
	 * @model
	 * @generated
	 */
	ProjectLauncher getProjectlauncher();

	/**
	 * Sets the value of the '{@link apps.Environment#getProjectlauncher <em>Projectlauncher</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Projectlauncher</em>' reference.
	 * @see #getProjectlauncher()
	 * @generated
	 */
	void setProjectlauncher(ProjectLauncher value);

} // Environment
