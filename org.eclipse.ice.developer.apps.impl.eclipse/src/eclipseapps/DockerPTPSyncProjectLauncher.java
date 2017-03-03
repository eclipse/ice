/**
 */
package eclipseapps;

import apps.SourcePackage;

import apps.docker.DockerProjectLauncher;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Docker PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see eclipseapps.EclipseappsPackage#getDockerPTPSyncProjectLauncher()
 * @model
 * @generated
 */
public interface DockerPTPSyncProjectLauncher extends DockerProjectLauncher {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Launch the new Eclipse project for the given application. 
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	boolean launchProject(SourcePackage project);

} // DockerPTPSyncProjectLauncher
