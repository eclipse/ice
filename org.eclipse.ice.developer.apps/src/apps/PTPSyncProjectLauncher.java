/**
 */
package apps;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The PTPSyncProjectLauncher launches a PTP Synchronized Project for the remotely hosted application. 
 * <!-- end-model-doc -->
 *
 *
 * @see apps.AppsPackage#getPTPSyncProjectLauncher()
 * @model
 * @generated
 */
public interface PTPSyncProjectLauncher extends ProjectLauncher {

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
} // PTPSyncProjectLauncher
