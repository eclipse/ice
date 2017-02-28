/**
 */
package apps.eclipse.impl;

import apps.SourcePackage;
import apps.docker.impl.DockerProjectLauncherImpl;

import apps.eclipse.DockerPTPSyncProjectLauncher;
import apps.eclipse.EclipsePackage;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Docker PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerPTPSyncProjectLauncherImpl extends DockerProjectLauncherImpl implements DockerPTPSyncProjectLauncher {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerPTPSyncProjectLauncherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipsePackage.Literals.DOCKER_PTP_SYNC_PROJECT_LAUNCHER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public void launchProject(SourcePackage project) {
		
		// Do PTP specific things with project and container config
	}
} //DockerPTPSyncProjectLauncherImpl
