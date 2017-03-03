/**
 */
package eclipseapps;

import apps.AppsPackage;

import apps.docker.DockerPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see eclipseapps.EclipseappsFactory
 * @model kind="package"
 * @generated
 */
public interface EclipseappsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "eclipseapps";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/ice/apps/eclipseimpl";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "eclipseappsimpl";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EclipseappsPackage eINSTANCE = eclipseapps.impl.EclipseappsPackageImpl.init();

	/**
	 * The meta object id for the '{@link eclipseapps.impl.EclipseEnvironmentStorageImpl <em>Eclipse Environment Storage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eclipseapps.impl.EclipseEnvironmentStorageImpl
	 * @see eclipseapps.impl.EclipseappsPackageImpl#getEclipseEnvironmentStorage()
	 * @generated
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE = 0;

	/**
	 * The number of structural features of the '<em>Eclipse Environment Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE_FEATURE_COUNT = AppsPackage.ENVIRONMENT_STORAGE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Store</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE___STORE__ELIST = AppsPackage.ENVIRONMENT_STORAGE___STORE__ELIST;

	/**
	 * The operation id for the '<em>Load</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE___LOAD = AppsPackage.ENVIRONMENT_STORAGE___LOAD;

	/**
	 * The number of operations of the '<em>Eclipse Environment Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE_OPERATION_COUNT = AppsPackage.ENVIRONMENT_STORAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link eclipseapps.impl.DockerPTPSyncProjectLauncherImpl <em>Docker PTP Sync Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eclipseapps.impl.DockerPTPSyncProjectLauncherImpl
	 * @see eclipseapps.impl.EclipseappsPackageImpl#getDockerPTPSyncProjectLauncher()
	 * @generated
	 */
	int DOCKER_PTP_SYNC_PROJECT_LAUNCHER = 1;

	/**
	 * The feature id for the '<em><b>Languageprojectprovider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_SYNC_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER = DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER;

	/**
	 * The feature id for the '<em><b>Containerconfiguration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_SYNC_PROJECT_LAUNCHER__CONTAINERCONFIGURATION = DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION;

	/**
	 * The number of structural features of the '<em>Docker PTP Sync Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_SYNC_PROJECT_LAUNCHER_FEATURE_COUNT = DockerPackage.DOCKER_PROJECT_LAUNCHER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = DockerPackage.DOCKER_PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Docker PTP Sync Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_SYNC_PROJECT_LAUNCHER_OPERATION_COUNT = DockerPackage.DOCKER_PROJECT_LAUNCHER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link eclipseapps.impl.EclipseCppProjectProviderImpl <em>Eclipse Cpp Project Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eclipseapps.impl.EclipseCppProjectProviderImpl
	 * @see eclipseapps.impl.EclipseappsPackageImpl#getEclipseCppProjectProvider()
	 * @generated
	 */
	int ECLIPSE_CPP_PROJECT_PROVIDER = 2;

	/**
	 * The number of structural features of the '<em>Eclipse Cpp Project Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_CPP_PROJECT_PROVIDER_FEATURE_COUNT = AppsPackage.LANGUAGE_PROJECT_PROVIDER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Create Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_CPP_PROJECT_PROVIDER___CREATE_PROJECT__STRING = AppsPackage.LANGUAGE_PROJECT_PROVIDER___CREATE_PROJECT__STRING;

	/**
	 * The number of operations of the '<em>Eclipse Cpp Project Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_CPP_PROJECT_PROVIDER_OPERATION_COUNT = AppsPackage.LANGUAGE_PROJECT_PROVIDER_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link eclipseapps.EclipseEnvironmentStorage <em>Eclipse Environment Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Eclipse Environment Storage</em>'.
	 * @see eclipseapps.EclipseEnvironmentStorage
	 * @generated
	 */
	EClass getEclipseEnvironmentStorage();

	/**
	 * Returns the meta object for class '{@link eclipseapps.DockerPTPSyncProjectLauncher <em>Docker PTP Sync Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker PTP Sync Project Launcher</em>'.
	 * @see eclipseapps.DockerPTPSyncProjectLauncher
	 * @generated
	 */
	EClass getDockerPTPSyncProjectLauncher();

	/**
	 * Returns the meta object for the '{@link eclipseapps.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see eclipseapps.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage)
	 * @generated
	 */
	EOperation getDockerPTPSyncProjectLauncher__LaunchProject__SourcePackage();

	/**
	 * Returns the meta object for class '{@link eclipseapps.EclipseCppProjectProvider <em>Eclipse Cpp Project Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Eclipse Cpp Project Provider</em>'.
	 * @see eclipseapps.EclipseCppProjectProvider
	 * @generated
	 */
	EClass getEclipseCppProjectProvider();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EclipseappsFactory getEclipseappsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link eclipseapps.impl.EclipseEnvironmentStorageImpl <em>Eclipse Environment Storage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eclipseapps.impl.EclipseEnvironmentStorageImpl
		 * @see eclipseapps.impl.EclipseappsPackageImpl#getEclipseEnvironmentStorage()
		 * @generated
		 */
		EClass ECLIPSE_ENVIRONMENT_STORAGE = eINSTANCE.getEclipseEnvironmentStorage();

		/**
		 * The meta object literal for the '{@link eclipseapps.impl.DockerPTPSyncProjectLauncherImpl <em>Docker PTP Sync Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eclipseapps.impl.DockerPTPSyncProjectLauncherImpl
		 * @see eclipseapps.impl.EclipseappsPackageImpl#getDockerPTPSyncProjectLauncher()
		 * @generated
		 */
		EClass DOCKER_PTP_SYNC_PROJECT_LAUNCHER = eINSTANCE.getDockerPTPSyncProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = eINSTANCE.getDockerPTPSyncProjectLauncher__LaunchProject__SourcePackage();

		/**
		 * The meta object literal for the '{@link eclipseapps.impl.EclipseCppProjectProviderImpl <em>Eclipse Cpp Project Provider</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eclipseapps.impl.EclipseCppProjectProviderImpl
		 * @see eclipseapps.impl.EclipseappsPackageImpl#getEclipseCppProjectProvider()
		 * @generated
		 */
		EClass ECLIPSE_CPP_PROJECT_PROVIDER = eINSTANCE.getEclipseCppProjectProvider();

	}

} //EclipseappsPackage
