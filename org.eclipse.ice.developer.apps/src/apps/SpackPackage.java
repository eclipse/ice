/**
 */
package apps;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link apps.SpackPackage#getDesiredVersion <em>Desired Version</em>}</li>
 *   <li>{@link apps.SpackPackage#getCompiler <em>Compiler</em>}</li>
 *   <li>{@link apps.SpackPackage#getVersions <em>Versions</em>}</li>
 *   <li>{@link apps.SpackPackage#getCppFlags <em>Cpp Flags</em>}</li>
 *   <li>{@link apps.SpackPackage#getVirtualDependency <em>Virtual Dependency</em>}</li>
 *   <li>{@link apps.SpackPackage#getVirtualDependencyProvider <em>Virtual Dependency Provider</em>}</li>
 * </ul>
 *
 * @see apps.EnvironmentPackage#getSpackPackage()
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
	 * @see apps.EnvironmentPackage#getSpackPackage_Name()
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
	 * Returns the value of the '<em><b>Desired Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The desired version for the package. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Desired Version</em>' attribute.
	 * @see #setDesiredVersion(String)
	 * @see apps.EnvironmentPackage#getSpackPackage_DesiredVersion()
	 * @model
	 * @generated
	 */
	String getDesiredVersion();

	/**
	 * Sets the value of the '{@link apps.SpackPackage#getDesiredVersion <em>Desired Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Desired Version</em>' attribute.
	 * @see #getDesiredVersion()
	 * @generated
	 */
	void setDesiredVersion(String value);

	/**
	 * Returns the value of the '<em><b>Compiler</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The compiler to use for this package. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Compiler</em>' attribute.
	 * @see #setCompiler(String)
	 * @see apps.EnvironmentPackage#getSpackPackage_Compiler()
	 * @model
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
	 * Returns the value of the '<em><b>Versions</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The available versions. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Versions</em>' attribute list.
	 * @see apps.EnvironmentPackage#getSpackPackage_Versions()
	 * @model
	 * @generated
	 */
	EList<String> getVersions();

	/**
	 * Returns the value of the '<em><b>Cpp Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The desired C++ flags. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cpp Flags</em>' attribute.
	 * @see #setCppFlags(String)
	 * @see apps.EnvironmentPackage#getSpackPackage_CppFlags()
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
	 * @see apps.EnvironmentPackage#getSpackPackage_VirtualDependency()
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
	 * @see apps.EnvironmentPackage#getSpackPackage_VirtualDependencyProvider()
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

} // SpackPackage
