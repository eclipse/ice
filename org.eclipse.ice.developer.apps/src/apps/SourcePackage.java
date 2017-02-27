/**
 */
package apps;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Source Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A SourcePackage is a Package that describes a repository URL, repository branch, and executable command to build the Package from scratch. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.SourcePackage#getRepoURL <em>Repo URL</em>}</li>
 *   <li>{@link apps.SourcePackage#getBranch <em>Branch</em>}</li>
 *   <li>{@link apps.SourcePackage#getBuildCommand <em>Build Command</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getSourcePackage()
 * @model
 * @generated
 */
public interface SourcePackage extends apps.Package {
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
	 * @see apps.AppsPackage#getSourcePackage_RepoURL()
	 * @model
	 * @generated
	 */
	String getRepoURL();

	/**
	 * Sets the value of the '{@link apps.SourcePackage#getRepoURL <em>Repo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repo URL</em>' attribute.
	 * @see #getRepoURL()
	 * @generated
	 */
	void setRepoURL(String value);

	/**
	 * Returns the value of the '<em><b>Branch</b></em>' attribute.
	 * The default value is <code>"master"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Branch</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Branch</em>' attribute.
	 * @see #setBranch(String)
	 * @see apps.AppsPackage#getSourcePackage_Branch()
	 * @model default="master"
	 * @generated
	 */
	String getBranch();

	/**
	 * Sets the value of the '{@link apps.SourcePackage#getBranch <em>Branch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Branch</em>' attribute.
	 * @see #getBranch()
	 * @generated
	 */
	void setBranch(String value);

	/**
	 * Returns the value of the '<em><b>Build Command</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Build Command</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Build Command</em>' attribute.
	 * @see #setBuildCommand(String)
	 * @see apps.AppsPackage#getSourcePackage_BuildCommand()
	 * @model
	 * @generated
	 */
	String getBuildCommand();

	/**
	 * Sets the value of the '{@link apps.SourcePackage#getBuildCommand <em>Build Command</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Build Command</em>' attribute.
	 * @see #getBuildCommand()
	 * @generated
	 */
	void setBuildCommand(String value);

} // SourcePackage
