/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Language Project Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see apps.AppsPackage#getLanguageProjectProvider()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface LanguageProjectProvider extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void createProject(String projectName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void configure();

} // LanguageProjectProvider
