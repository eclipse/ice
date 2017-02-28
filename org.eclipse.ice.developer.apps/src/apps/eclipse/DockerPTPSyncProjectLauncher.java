/**
 */
package apps.eclipse;

import apps.SourcePackage;

import apps.docker.DockerProjectLauncher;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Docker PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see apps.eclipse.EclipsePackage#getDockerPTPSyncProjectLauncher()
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
	void launchProject(SourcePackage project);

} // DockerPTPSyncProjectLauncher
