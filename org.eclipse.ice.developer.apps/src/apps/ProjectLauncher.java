/**
 */
package apps;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The ProjectLauncher is a high-level interface for creating a new Eclipse Project corresponding to the primary application in the Environment. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link apps.ProjectLauncher#getLanguageprojectprovider <em>Languageprojectprovider</em>}</li>
 * </ul>
 *
 * @see apps.AppsPackage#getProjectLauncher()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProjectLauncher extends EObject {
	/**
	 * Returns the value of the '<em><b>Languageprojectprovider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Languageprojectprovider</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Languageprojectprovider</em>' containment reference.
	 * @see #setLanguageprojectprovider(LanguageProjectProvider)
	 * @see apps.AppsPackage#getProjectLauncher_Languageprojectprovider()
	 * @model containment="true"
	 * @generated
	 */
	LanguageProjectProvider getLanguageprojectprovider();

	/**
	 * Sets the value of the '{@link apps.ProjectLauncher#getLanguageprojectprovider <em>Languageprojectprovider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Languageprojectprovider</em>' containment reference.
	 * @see #getLanguageprojectprovider()
	 * @generated
	 */
	void setLanguageprojectprovider(LanguageProjectProvider value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch the new Eclipse project for the given application. 
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean launchProject(SourcePackage project);

} // ProjectLauncher
