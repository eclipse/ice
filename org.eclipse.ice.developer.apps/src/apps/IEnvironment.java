/**
 */
package apps;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IEnvironment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The IEnvironment interface provides the data and methods necessary to describe a scientific application environment. It describes the name of the environment, the OS it runs in, and the current running state of the environment, the application being developed in the environment, and the dependencies for that application.  IEnvironments can be created and deleted, stopped and connected or re-connected to.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.IEnvironment#getName <em>Name</em>}</li>
 *   <li>{@link apps.IEnvironment#getOs <em>Os</em>}</li>
 *   <li>{@link apps.IEnvironment#getDependentPackages <em>Dependent Packages</em>}</li>
 *   <li>{@link apps.IEnvironment#getPrimaryApp <em>Primary App</em>}</li>
 *   <li>{@link apps.IEnvironment#getProjectlauncher <em>Projectlauncher</em>}</li>
 *   <li>{@link apps.IEnvironment#getState <em>State</em>}</li>
 *   <li>{@link apps.IEnvironment#getConsole <em>Console</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getIEnvironment()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IEnvironment extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of this environment
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see apps.AppsPackage#getIEnvironment_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Os</b></em>' attribute.
	 * The default value is <code>"fedora"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This environment's operating system. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Os</em>' attribute.
	 * @see #setOs(String)
	 * @see apps.AppsPackage#getIEnvironment_Os()
	 * @model default="fedora"
	 * @generated
	 */
	String getOs();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getOs <em>Os</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Os</em>' attribute.
	 * @see #getOs()
	 * @generated
	 */
	void setOs(String value);

	/**
	 * Returns the value of the '<em><b>Dependent Packages</b></em>' containment reference list.
	 * The list contents are of type {@link apps.Package}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The list of packages that the application in this environment depends on. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dependent Packages</em>' containment reference list.
	 * @see apps.AppsPackage#getIEnvironment_DependentPackages()
	 * @model containment="true"
	 * @generated
	 */
	EList<apps.Package> getDependentPackages();

	/**
	 * Returns the value of the '<em><b>Primary App</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary App</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The primary application that this IEnvironment serves for development. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Primary App</em>' containment reference.
	 * @see #setPrimaryApp(apps.Package)
	 * @see apps.AppsPackage#getIEnvironment_PrimaryApp()
	 * @model containment="true"
	 * @generated
	 */
	apps.Package getPrimaryApp();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getPrimaryApp <em>Primary App</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary App</em>' containment reference.
	 * @see #getPrimaryApp()
	 * @generated
	 */
	void setPrimaryApp(apps.Package value);

	/**
	 * Returns the value of the '<em><b>Projectlauncher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Projectlauncher</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projectlauncher</em>' containment reference.
	 * @see #setProjectlauncher(ProjectLauncher)
	 * @see apps.AppsPackage#getIEnvironment_Projectlauncher()
	 * @model containment="true"
	 * @generated
	 */
	ProjectLauncher getProjectlauncher();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getProjectlauncher <em>Projectlauncher</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Projectlauncher</em>' containment reference.
	 * @see #getProjectlauncher()
	 * @generated
	 */
	void setProjectlauncher(ProjectLauncher value);

	/**
	 * Returns the value of the '<em><b>State</b></em>' attribute.
	 * The default value is <code>"NotCreated"</code>.
	 * The literals are from the enumeration {@link apps.EnvironmentState}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The state of this IEnvironment. The state can be Running, Stopped, or NotCreated. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>State</em>' attribute.
	 * @see apps.EnvironmentState
	 * @see #setState(EnvironmentState)
	 * @see apps.AppsPackage#getIEnvironment_State()
	 * @model default="NotCreated"
	 * @generated
	 */
	EnvironmentState getState();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getState <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>State</em>' attribute.
	 * @see apps.EnvironmentState
	 * @see #getState()
	 * @generated
	 */
	void setState(EnvironmentState value);

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
	 * @see apps.AppsPackage#getIEnvironment_Console()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EnvironmentConsole getConsole();

	/**
	 * Sets the value of the '{@link apps.IEnvironment#getConsole <em>Console</em>}' containment reference.
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
	 * Build this IEnvironment from the given data string. The data string can be formatted using JSon, for example. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean build();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Connect to this IEnvironment if it is in a Stopped state. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean connect();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Delete the IEnvironment. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean delete();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Stop this IEnvironment. This sets the state from Running to Stopped. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean stop();

} // IEnvironment
