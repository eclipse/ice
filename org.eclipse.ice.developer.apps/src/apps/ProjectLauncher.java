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
 *
 * @see apps.AppsPackage#getProjectLauncher()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProjectLauncher extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch the new Eclipse project for the given application. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void launchProject(SourcePackage project);

} // ProjectLauncher
