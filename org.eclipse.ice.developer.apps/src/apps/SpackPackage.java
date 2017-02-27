/**
 */
package apps;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spack Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The SpackPackage class encapsulates the data required to describe a spack package, and the data need to install the package like the compiler to use, any C++ build flags, and dependencies and dependency providers (for example mpi and mpich, respectively)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.SpackPackage#getCompiler <em>Compiler</em>}</li>
 *   <li>{@link apps.SpackPackage#getCppFlags <em>Cpp Flags</em>}</li>
 *   <li>{@link apps.SpackPackage#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getSpackPackage()
 * @model
 * @generated
 */
public interface SpackPackage extends apps.Package {
	/**
	 * Returns the value of the '<em><b>Compiler</b></em>' attribute.
	 * The default value is <code>"gcc@6.3.1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The compiler to use for this package. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Compiler</em>' attribute.
	 * @see #setCompiler(String)
	 * @see apps.AppsPackage#getSpackPackage_Compiler()
	 * @model default="gcc@6.3.1"
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
	 * Returns the value of the '<em><b>Dependencies</b></em>' reference list.
	 * The list contents are of type {@link apps.SpackDependency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependencies</em>' reference list.
	 * @see apps.AppsPackage#getSpackPackage_Dependencies()
	 * @model
	 * @generated
	 */
	EList<SpackDependency> getDependencies();

} // SpackPackage
