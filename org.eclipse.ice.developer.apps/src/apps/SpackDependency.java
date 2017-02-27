/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spack Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A SpackDependency describes a package's dependency and the dependency provider for that dependency (for example, mpi and mpich).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.SpackDependency#getDependency <em>Dependency</em>}</li>
 *   <li>{@link apps.SpackDependency#getProvider <em>Provider</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getSpackDependency()
 * @model
 * @generated
 */
public interface SpackDependency extends EObject {
	/**
	 * Returns the value of the '<em><b>Dependency</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependency</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependency</em>' attribute.
	 * @see #setDependency(String)
	 * @see apps.AppsPackage#getSpackDependency_Dependency()
	 * @model
	 * @generated
	 */
	String getDependency();

	/**
	 * Sets the value of the '{@link apps.SpackDependency#getDependency <em>Dependency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dependency</em>' attribute.
	 * @see #getDependency()
	 * @generated
	 */
	void setDependency(String value);

	/**
	 * Returns the value of the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provider</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provider</em>' attribute.
	 * @see #setProvider(String)
	 * @see apps.AppsPackage#getSpackDependency_Provider()
	 * @model
	 * @generated
	 */
	String getProvider();

	/**
	 * Sets the value of the '{@link apps.SpackDependency#getProvider <em>Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider</em>' attribute.
	 * @see #getProvider()
	 * @generated
	 */
	void setProvider(String value);

} // SpackDependency
