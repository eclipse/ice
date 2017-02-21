/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spack Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The SpackPackage class encapsulates the data required to describe a spack package, and the data need to install the package. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.SpackPackage#getName <em>Name</em>}</li>
 *   <li>{@link apps.SpackPackage#getCompiler <em>Compiler</em>}</li>
 *   <li>{@link apps.SpackPackage#getVersion <em>Version</em>}</li>
 *   <li>{@link apps.SpackPackage#getCppFlags <em>Cpp Flags</em>}</li>
 *   <li>{@link apps.SpackPackage#getVirtualDependency <em>Virtual Dependency</em>}</li>
 *   <li>{@link apps.SpackPackage#getVirtualDependencyProvider <em>Virtual Dependency Provider</em>}</li>
 *   <li>{@link apps.SpackPackage#getRepoURL <em>Repo URL</em>}</li>
 *   <li>{@link apps.SpackPackage#getBranch <em>Branch</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getSpackPackage()
 * @model
 * @generated
 */
public interface SpackPackage extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the package. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see apps.AppsPackage#getSpackPackage_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Compiler</b></em>' attribute.
	 * The default value is <code>"gcc@6.3.0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The compiler to use for this package. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Compiler</em>' attribute.
	 * @see #setCompiler(String)
	 * @see apps.AppsPackage#getSpackPackage_Compiler()
	 * @model default="gcc@6.3.0"
	 * @generated
	 */
	String getCompiler();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getCompiler <em>Compiler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Compiler</em>' attribute.
	 * @see #getCompiler()
	 * @generated
	 */
	void setCompiler(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"latest"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The available versions. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see apps.AppsPackage#getSpackPackage_Version()
	 * @model default="latest"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Cpp Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The desired C++ flags. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cpp Flags</em>' attribute.
	 * @see #setCppFlags(String)
	 * @see apps.AppsPackage#getSpackPackage_CppFlags()
	 * @model
	 * @generated
	 */
	String getCppFlags();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getCppFlags <em>Cpp Flags</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cpp Flags</em>' attribute.
	 * @see #getCppFlags()
	 * @generated
	 */
	void setCppFlags(String value);

	/**
	 * Returns the value of the '<em><b>Virtual Dependency</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to requested virtual dependencies.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Virtual Dependency</em>' attribute.
	 * @see #setVirtualDependency(String)
	 * @see apps.AppsPackage#getSpackPackage_VirtualDependency()
	 * @model
	 * @generated
	 */
	String getVirtualDependency();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getVirtualDependency <em>Virtual Dependency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Virtual Dependency</em>' attribute.
	 * @see #getVirtualDependency()
	 * @generated
	 */
	void setVirtualDependency(String value);

	/**
	 * Returns the value of the '<em><b>Virtual Dependency Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to virtual dependency provider. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Virtual Dependency Provider</em>' attribute.
	 * @see #setVirtualDependencyProvider(String)
	 * @see apps.AppsPackage#getSpackPackage_VirtualDependencyProvider()
	 * @model
	 * @generated
	 */
	String getVirtualDependencyProvider();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getVirtualDependencyProvider <em>Virtual Dependency Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Virtual Dependency Provider</em>' attribute.
	 * @see #getVirtualDependencyProvider()
	 * @generated
	 */
	void setVirtualDependencyProvider(String value);

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
	 * @see apps.AppsPackage#getSpackPackage_RepoURL()
	 * @model
	 * @generated
	 */
	String getRepoURL();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getRepoURL <em>Repo URL</em>}' attribute.
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
	 * @see apps.AppsPackage#getSpackPackage_Branch()
	 * @model default="master"
	 * @generated
	 */
	String getBranch();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getBranch <em>Branch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Branch</em>' attribute.
	 * @see #getBranch()
	 * @generated
	 */
	void setBranch(String value);

} // SpackPackage
