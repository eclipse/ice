/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Science App</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.ScienceApp#getRepoURL <em>Repo URL</em>}</li>
 *   <li>{@link apps.ScienceApp#getName <em>Name</em>}</li>
 *   <li>{@link apps.ScienceApp#getBranch <em>Branch</em>}</li>
 *   <li>{@link apps.ScienceApp#getLocalProjectName <em>Local Project Name</em>}</li>
 *   <li>{@link apps.ScienceApp#isRecursiveClone <em>Recursive Clone</em>}</li>
 *   <li>{@link apps.ScienceApp#getAbsolutePath <em>Absolute Path</em>}</li>
 *   <li>{@link apps.ScienceApp#getRemotePort <em>Remote Port</em>}</li>
 *   <li>{@link apps.ScienceApp#getRemoteHost <em>Remote Host</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getScienceApp()
 * @model
 * @generated
 */
public interface ScienceApp extends EObject {
	/**
	 * Returns the value of the '<em><b>Repo URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repo URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repo URL</em>' attribute.
	 * @see #setRepoURL(String)
	 * @see apps.AppsPackage#getScienceApp_RepoURL()
	 * @model
	 * @generated
	 */
	String getRepoURL();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getRepoURL <em>Repo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repo URL</em>' attribute.
	 * @see #getRepoURL()
	 * @generated
	 */
	void setRepoURL(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see apps.AppsPackage#getScienceApp_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Branch</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Branch</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Branch</em>' attribute.
	 * @see #setBranch(String)
	 * @see apps.AppsPackage#getScienceApp_Branch()
	 * @model
	 * @generated
	 */
	String getBranch();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getBranch <em>Branch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Branch</em>' attribute.
	 * @see #getBranch()
	 * @generated
	 */
	void setBranch(String value);

	/**
	 * Returns the value of the '<em><b>Local Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Local Project Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Local Project Name</em>' attribute.
	 * @see #setLocalProjectName(String)
	 * @see apps.AppsPackage#getScienceApp_LocalProjectName()
	 * @model
	 * @generated
	 */
	String getLocalProjectName();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getLocalProjectName <em>Local Project Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Local Project Name</em>' attribute.
	 * @see #getLocalProjectName()
	 * @generated
	 */
	void setLocalProjectName(String value);

	/**
	 * Returns the value of the '<em><b>Recursive Clone</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Recursive Clone</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Recursive Clone</em>' attribute.
	 * @see #setRecursiveClone(boolean)
	 * @see apps.AppsPackage#getScienceApp_RecursiveClone()
	 * @model default="true"
	 * @generated
	 */
	boolean isRecursiveClone();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#isRecursiveClone <em>Recursive Clone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Recursive Clone</em>' attribute.
	 * @see #isRecursiveClone()
	 * @generated
	 */
	void setRecursiveClone(boolean value);

	/**
	 * Returns the value of the '<em><b>Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Absolute Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Absolute Path</em>' attribute.
	 * @see #setAbsolutePath(String)
	 * @see apps.AppsPackage#getScienceApp_AbsolutePath()
	 * @model
	 * @generated
	 */
	String getAbsolutePath();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getAbsolutePath <em>Absolute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Absolute Path</em>' attribute.
	 * @see #getAbsolutePath()
	 * @generated
	 */
	void setAbsolutePath(String value);

	/**
	 * Returns the value of the '<em><b>Remote Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Remote Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Remote Port</em>' attribute.
	 * @see #setRemotePort(int)
	 * @see apps.AppsPackage#getScienceApp_RemotePort()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Int"
	 * @generated
	 */
	int getRemotePort();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getRemotePort <em>Remote Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote Port</em>' attribute.
	 * @see #getRemotePort()
	 * @generated
	 */
	void setRemotePort(int value);

	/**
	 * Returns the value of the '<em><b>Remote Host</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Remote Host</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Remote Host</em>' attribute.
	 * @see #setRemoteHost(String)
	 * @see apps.AppsPackage#getScienceApp_RemoteHost()
	 * @model
	 * @generated
	 */
	String getRemoteHost();

	/**
	 * Sets the value of the '{@link apps.ScienceApp#getRemoteHost <em>Remote Host</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote Host</em>' attribute.
	 * @see #getRemoteHost()
	 * @generated
	 */
	void setRemoteHost(String value);

} // ScienceApp
