/**
 */
package apps.docker;

import apps.AppsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see apps.docker.DockerFactory
 * @model kind="package"
 * @generated
 */
public interface DockerPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "docker";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/apps/docker";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "dockerenvironment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DockerPackage eINSTANCE = apps.docker.impl.DockerPackageImpl.init();

	/**
	 * The meta object id for the '{@link apps.docker.impl.DockerEnvironmentImpl <em>Environment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.DockerEnvironmentImpl
	 * @see apps.docker.impl.DockerPackageImpl#getDockerEnvironment()
	 * @generated
	 */
	int DOCKER_ENVIRONMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__NAME = AppsPackage.IENVIRONMENT__NAME;

	/**
	 * The feature id for the '<em><b>Os</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__OS = AppsPackage.IENVIRONMENT__OS;

	/**
	 * The feature id for the '<em><b>Dependent Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__DEPENDENT_PACKAGES = AppsPackage.IENVIRONMENT__DEPENDENT_PACKAGES;

	/**
	 * The feature id for the '<em><b>Primary App</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__PRIMARY_APP = AppsPackage.IENVIRONMENT__PRIMARY_APP;

	/**
	 * The feature id for the '<em><b>Projectlauncher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__PROJECTLAUNCHER = AppsPackage.IENVIRONMENT__PROJECTLAUNCHER;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__STATE = AppsPackage.IENVIRONMENT__STATE;

	/**
	 * The feature id for the '<em><b>Docker</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__DOCKER = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Container Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Dockerfile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__DOCKERFILE = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_FEATURE_COUNT = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___BUILD = AppsPackage.IENVIRONMENT___BUILD;

	/**
	 * The operation id for the '<em>Connect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___CONNECT = AppsPackage.IENVIRONMENT___CONNECT;

	/**
	 * The operation id for the '<em>Delete</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___DELETE = AppsPackage.IENVIRONMENT___DELETE;

	/**
	 * The operation id for the '<em>Stop</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___STOP = AppsPackage.IENVIRONMENT___STOP;

	/**
	 * The number of operations of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_OPERATION_COUNT = AppsPackage.IENVIRONMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.docker.impl.DockerAPIImpl <em>API</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.DockerAPIImpl
	 * @see apps.docker.impl.DockerPackageImpl#getDockerAPI()
	 * @generated
	 */
	int DOCKER_API = 1;

	/**
	 * The number of structural features of the '<em>API</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Build Image</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___BUILD_IMAGE__STRING_STRING = 0;

	/**
	 * The operation id for the '<em>Create Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION = 1;

	/**
	 * The operation id for the '<em>Connect To Existing Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING = 2;

	/**
	 * The operation id for the '<em>Delete Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___DELETE_CONTAINER__STRING = 3;

	/**
	 * The operation id for the '<em>Delete Image</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___DELETE_IMAGE__STRING = 4;

	/**
	 * The number of operations of the '<em>API</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API_OPERATION_COUNT = 5;

	/**
	 * The meta object id for the '{@link apps.docker.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.ContainerConfigurationImpl
	 * @see apps.docker.impl.DockerPackageImpl#getContainerConfiguration()
	 * @generated
	 */
	int CONTAINER_CONFIGURATION = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Ephemeral</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__EPHEMERAL = 1;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__PORTS = 2;

	/**
	 * The feature id for the '<em><b>Volumes Config</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__VOLUMES_CONFIG = 3;

	/**
	 * The feature id for the '<em><b>Remote SSH Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__REMOTE_SSH_PORT = 4;

	/**
	 * The number of structural features of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link apps.docker.impl.DockerPTPProjectLauncherImpl <em>PTP Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.DockerPTPProjectLauncherImpl
	 * @see apps.docker.impl.DockerPackageImpl#getDockerPTPProjectLauncher()
	 * @generated
	 */
	int DOCKER_PTP_PROJECT_LAUNCHER = 3;

	/**
	 * The feature id for the '<em><b>Containerconfiguration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_PROJECT_LAUNCHER__CONTAINERCONFIGURATION = AppsPackage.PROJECT_LAUNCHER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>PTP Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_PROJECT_LAUNCHER_FEATURE_COUNT = AppsPackage.PROJECT_LAUNCHER_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = AppsPackage.PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>PTP Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PTP_PROJECT_LAUNCHER_OPERATION_COUNT = AppsPackage.PROJECT_LAUNCHER_OPERATION_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link apps.docker.DockerEnvironment <em>Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment</em>'.
	 * @see apps.docker.DockerEnvironment
	 * @generated
	 */
	EClass getDockerEnvironment();

	/**
	 * Returns the meta object for the reference '{@link apps.docker.DockerEnvironment#getDocker <em>Docker</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Docker</em>'.
	 * @see apps.docker.DockerEnvironment#getDocker()
	 * @see #getDockerEnvironment()
	 * @generated
	 */
	EReference getDockerEnvironment_Docker();

	/**
	 * Returns the meta object for the containment reference '{@link apps.docker.DockerEnvironment#getContainerConfiguration <em>Container Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Container Configuration</em>'.
	 * @see apps.docker.DockerEnvironment#getContainerConfiguration()
	 * @see #getDockerEnvironment()
	 * @generated
	 */
	EReference getDockerEnvironment_ContainerConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link apps.docker.DockerEnvironment#getDockerfile <em>Dockerfile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dockerfile</em>'.
	 * @see apps.docker.DockerEnvironment#getDockerfile()
	 * @see #getDockerEnvironment()
	 * @generated
	 */
	EAttribute getDockerEnvironment_Dockerfile();

	/**
	 * Returns the meta object for class '{@link apps.docker.DockerAPI <em>API</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>API</em>'.
	 * @see apps.docker.DockerAPI
	 * @generated
	 */
	EClass getDockerAPI();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#buildImage(java.lang.String, java.lang.String) <em>Build Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Build Image</em>' operation.
	 * @see apps.docker.DockerAPI#buildImage(java.lang.String, java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__BuildImage__String_String();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#createContainer(java.lang.String, apps.docker.ContainerConfiguration) <em>Create Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Container</em>' operation.
	 * @see apps.docker.DockerAPI#createContainer(java.lang.String, apps.docker.ContainerConfiguration)
	 * @generated
	 */
	EOperation getDockerAPI__CreateContainer__String_ContainerConfiguration();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#connectToExistingContainer(java.lang.String) <em>Connect To Existing Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Connect To Existing Container</em>' operation.
	 * @see apps.docker.DockerAPI#connectToExistingContainer(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__ConnectToExistingContainer__String();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#deleteContainer(java.lang.String) <em>Delete Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Delete Container</em>' operation.
	 * @see apps.docker.DockerAPI#deleteContainer(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__DeleteContainer__String();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#deleteImage(java.lang.String) <em>Delete Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Delete Image</em>' operation.
	 * @see apps.docker.DockerAPI#deleteImage(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__DeleteImage__String();

	/**
	 * Returns the meta object for class '{@link apps.docker.ContainerConfiguration <em>Container Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container Configuration</em>'.
	 * @see apps.docker.ContainerConfiguration
	 * @generated
	 */
	EClass getContainerConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link apps.docker.ContainerConfiguration#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see apps.docker.ContainerConfiguration#getName()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Name();

	/**
	 * Returns the meta object for the attribute '{@link apps.docker.ContainerConfiguration#isEphemeral <em>Ephemeral</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ephemeral</em>'.
	 * @see apps.docker.ContainerConfiguration#isEphemeral()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Ephemeral();

	/**
	 * Returns the meta object for the attribute list '{@link apps.docker.ContainerConfiguration#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Ports</em>'.
	 * @see apps.docker.ContainerConfiguration#getPorts()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Ports();

	/**
	 * Returns the meta object for the attribute '{@link apps.docker.ContainerConfiguration#getVolumesConfig <em>Volumes Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volumes Config</em>'.
	 * @see apps.docker.ContainerConfiguration#getVolumesConfig()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_VolumesConfig();

	/**
	 * Returns the meta object for the attribute '{@link apps.docker.ContainerConfiguration#getRemoteSSHPort <em>Remote SSH Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote SSH Port</em>'.
	 * @see apps.docker.ContainerConfiguration#getRemoteSSHPort()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_RemoteSSHPort();

	/**
	 * Returns the meta object for class '{@link apps.docker.DockerPTPProjectLauncher <em>PTP Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>PTP Project Launcher</em>'.
	 * @see apps.docker.DockerPTPProjectLauncher
	 * @generated
	 */
	EClass getDockerPTPProjectLauncher();

	/**
	 * Returns the meta object for the reference '{@link apps.docker.DockerPTPProjectLauncher#getContainerconfiguration <em>Containerconfiguration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Containerconfiguration</em>'.
	 * @see apps.docker.DockerPTPProjectLauncher#getContainerconfiguration()
	 * @see #getDockerPTPProjectLauncher()
	 * @generated
	 */
	EReference getDockerPTPProjectLauncher_Containerconfiguration();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerPTPProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.docker.DockerPTPProjectLauncher#launchProject(apps.SourcePackage)
	 * @generated
	 */
	EOperation getDockerPTPProjectLauncher__LaunchProject__SourcePackage();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DockerFactory getDockerFactory();

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
		 * The meta object literal for the '{@link apps.docker.impl.DockerEnvironmentImpl <em>Environment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.docker.impl.DockerEnvironmentImpl
		 * @see apps.docker.impl.DockerPackageImpl#getDockerEnvironment()
		 * @generated
		 */
		EClass DOCKER_ENVIRONMENT = eINSTANCE.getDockerEnvironment();

		/**
		 * The meta object literal for the '<em><b>Docker</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCKER_ENVIRONMENT__DOCKER = eINSTANCE.getDockerEnvironment_Docker();

		/**
		 * The meta object literal for the '<em><b>Container Configuration</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION = eINSTANCE.getDockerEnvironment_ContainerConfiguration();

		/**
		 * The meta object literal for the '<em><b>Dockerfile</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCKER_ENVIRONMENT__DOCKERFILE = eINSTANCE.getDockerEnvironment_Dockerfile();

		/**
		 * The meta object literal for the '{@link apps.docker.impl.DockerAPIImpl <em>API</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.docker.impl.DockerAPIImpl
		 * @see apps.docker.impl.DockerPackageImpl#getDockerAPI()
		 * @generated
		 */
		EClass DOCKER_API = eINSTANCE.getDockerAPI();

		/**
		 * The meta object literal for the '<em><b>Build Image</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___BUILD_IMAGE__STRING_STRING = eINSTANCE.getDockerAPI__BuildImage__String_String();

		/**
		 * The meta object literal for the '<em><b>Create Container</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION = eINSTANCE.getDockerAPI__CreateContainer__String_ContainerConfiguration();

		/**
		 * The meta object literal for the '<em><b>Connect To Existing Container</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING = eINSTANCE.getDockerAPI__ConnectToExistingContainer__String();

		/**
		 * The meta object literal for the '<em><b>Delete Container</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___DELETE_CONTAINER__STRING = eINSTANCE.getDockerAPI__DeleteContainer__String();

		/**
		 * The meta object literal for the '<em><b>Delete Image</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___DELETE_IMAGE__STRING = eINSTANCE.getDockerAPI__DeleteImage__String();

		/**
		 * The meta object literal for the '{@link apps.docker.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.docker.impl.ContainerConfigurationImpl
		 * @see apps.docker.impl.DockerPackageImpl#getContainerConfiguration()
		 * @generated
		 */
		EClass CONTAINER_CONFIGURATION = eINSTANCE.getContainerConfiguration();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__NAME = eINSTANCE.getContainerConfiguration_Name();

		/**
		 * The meta object literal for the '<em><b>Ephemeral</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__EPHEMERAL = eINSTANCE.getContainerConfiguration_Ephemeral();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__PORTS = eINSTANCE.getContainerConfiguration_Ports();

		/**
		 * The meta object literal for the '<em><b>Volumes Config</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__VOLUMES_CONFIG = eINSTANCE.getContainerConfiguration_VolumesConfig();

		/**
		 * The meta object literal for the '<em><b>Remote SSH Port</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__REMOTE_SSH_PORT = eINSTANCE.getContainerConfiguration_RemoteSSHPort();

		/**
		 * The meta object literal for the '{@link apps.docker.impl.DockerPTPProjectLauncherImpl <em>PTP Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.docker.impl.DockerPTPProjectLauncherImpl
		 * @see apps.docker.impl.DockerPackageImpl#getDockerPTPProjectLauncher()
		 * @generated
		 */
		EClass DOCKER_PTP_PROJECT_LAUNCHER = eINSTANCE.getDockerPTPProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Containerconfiguration</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCKER_PTP_PROJECT_LAUNCHER__CONTAINERCONFIGURATION = eINSTANCE.getDockerPTPProjectLauncher_Containerconfiguration();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_PTP_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = eINSTANCE.getDockerPTPProjectLauncher__LaunchProject__SourcePackage();

	}

} //DockerPackage
