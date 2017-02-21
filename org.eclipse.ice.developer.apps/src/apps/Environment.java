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
 * @see apps.AppsPackage#getEnvironment()
 * @model
 * @generated
 */
public interface Environment extends IEnvironment {
	/**
	 * Returns the value of the '<em><b>Projectlauncher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Projectlauncher</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projectlauncher</em>' containment reference.
	 * @see #setProjectlauncher(ProjectLauncher)
	 * @see apps.AppsPackage#getEnvironment_Projectlauncher()
	 * @model containment="true"
	 * @generated
	 */
	ProjectLauncher getProjectlauncher();

	/**
	 * Sets the value of the '{@link apps.Environment#getProjectlauncher <em>Projectlauncher</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Projectlauncher</em>' containment reference.
	 * @see #getProjectlauncher()
	 * @generated
	 */
	void setProjectlauncher(ProjectLauncher value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This method directs the Environment to launch itself with its provided data. The implementation on this class 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean launch();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This method is to be implemented by subclasses to achieve subclass-specific launch behavior for the environment. It is invoked by the Environment.launch method. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean launchDerived();

} // Environment
