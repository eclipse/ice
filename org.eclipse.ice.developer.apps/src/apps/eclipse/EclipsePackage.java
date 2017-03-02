/**
 */
package apps.eclipse;

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
 * @see apps.eclipse.EclipseFactory
 * @model kind="package"
 * @generated
 */
public interface EclipsePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "eclipse";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://eclipse.org/apps/eclipse";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "eclipseextensions";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EclipsePackage eINSTANCE = apps.eclipse.impl.EclipsePackageImpl.init();

	/**
	 * The meta object id for the '{@link apps.eclipse.impl.EclipseEnvironmentStorageImpl <em>Environment Storage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.eclipse.impl.EclipseEnvironmentStorageImpl
	 * @see apps.eclipse.impl.EclipsePackageImpl#getEclipseEnvironmentStorage()
	 * @generated
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE = 0;

	/**
	 * The number of structural features of the '<em>Environment Storage</em>' class.
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
	 * The number of operations of the '<em>Environment Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_ENVIRONMENT_STORAGE_OPERATION_COUNT = AppsPackage.ENVIRONMENT_STORAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.eclipse.impl.DockerPTPSyncProjectLauncherImpl <em>Docker PTP Sync Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.eclipse.impl.DockerPTPSyncProjectLauncherImpl
	 * @see apps.eclipse.impl.EclipsePackageImpl#getDockerPTPSyncProjectLauncher()
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
	 * The meta object id for the '{@link apps.eclipse.impl.EclipseCppProjectProviderImpl <em>Cpp Project Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.eclipse.impl.EclipseCppProjectProviderImpl
	 * @see apps.eclipse.impl.EclipsePackageImpl#getEclipseCppProjectProvider()
	 * @generated
	 */
	int ECLIPSE_CPP_PROJECT_PROVIDER = 2;

	/**
	 * The number of structural features of the '<em>Cpp Project Provider</em>' class.
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
	 * The number of operations of the '<em>Cpp Project Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLIPSE_CPP_PROJECT_PROVIDER_OPERATION_COUNT = AppsPackage.LANGUAGE_PROJECT_PROVIDER_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link apps.eclipse.EclipseEnvironmentStorage <em>Environment Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment Storage</em>'.
	 * @see apps.eclipse.EclipseEnvironmentStorage
	 * @generated
	 */
	EClass getEclipseEnvironmentStorage();

	/**
	 * Returns the meta object for class '{@link apps.eclipse.DockerPTPSyncProjectLauncher <em>Docker PTP Sync Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker PTP Sync Project Launcher</em>'.
	 * @see apps.eclipse.DockerPTPSyncProjectLauncher
	 * @generated
	 */
	EClass getDockerPTPSyncProjectLauncher();

	/**
	 * Returns the meta object for the '{@link apps.eclipse.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.eclipse.DockerPTPSyncProjectLauncher#launchProject(apps.SourcePackage)
	 * @generated
	 */
	EOperation getDockerPTPSyncProjectLauncher__LaunchProject__SourcePackage();

	/**
	 * Returns the meta object for class '{@link apps.eclipse.EclipseCppProjectProvider <em>Cpp Project Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cpp Project Provider</em>'.
	 * @see apps.eclipse.EclipseCppProjectProvider
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
	EclipseFactory getEclipseFactory();

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
		 * The meta object literal for the '{@link apps.eclipse.impl.EclipseEnvironmentStorageImpl <em>Environment Storage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.eclipse.impl.EclipseEnvironmentStorageImpl
		 * @see apps.eclipse.impl.EclipsePackageImpl#getEclipseEnvironmentStorage()
		 * @generated
		 */
		EClass ECLIPSE_ENVIRONMENT_STORAGE = eINSTANCE.getEclipseEnvironmentStorage();

		/**
		 * The meta object literal for the '{@link apps.eclipse.impl.DockerPTPSyncProjectLauncherImpl <em>Docker PTP Sync Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.eclipse.impl.DockerPTPSyncProjectLauncherImpl
		 * @see apps.eclipse.impl.EclipsePackageImpl#getDockerPTPSyncProjectLauncher()
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
		 * The meta object literal for the '{@link apps.eclipse.impl.EclipseCppProjectProviderImpl <em>Cpp Project Provider</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.eclipse.impl.EclipseCppProjectProviderImpl
		 * @see apps.eclipse.impl.EclipsePackageImpl#getEclipseCppProjectProvider()
		 * @generated
		 */
		EClass ECLIPSE_CPP_PROJECT_PROVIDER = eINSTANCE.getEclipseCppProjectProvider();

	}

} //EclipsePackage
