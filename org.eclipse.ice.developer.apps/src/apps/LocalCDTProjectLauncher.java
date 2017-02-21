/**
 */
package apps;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Local CDT Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The LocalCDTProjectLauncher launches C++ Development Toolkit projects on the localhost. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.AppsPackage#getLocalCDTProjectLauncher()
 * @model
 * @generated
 */
public interface LocalCDTProjectLauncher extends ProjectLauncher {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch the new Eclipse project for the given application. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void launchProject(SpackPackage project);
} // LocalCDTProjectLauncher
