/**
 */
package apps.docker;

import apps.EnvironmentPackage;

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
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__TYPE = EnvironmentPackage.ENVIRONMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__NAME = EnvironmentPackage.ENVIRONMENT__NAME;

	/**
	 * The feature id for the '<em><b>Os</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__OS = EnvironmentPackage.ENVIRONMENT__OS;

	/**
	 * The feature id for the '<em><b>Spackpackage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__SPACKPACKAGE = EnvironmentPackage.ENVIRONMENT__SPACKPACKAGE;

	/**
	 * The feature id for the '<em><b>Scienceapp</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__SCIENCEAPP = EnvironmentPackage.ENVIRONMENT__SCIENCEAPP;

	/**
	 * The feature id for the '<em><b>Projectlauncher</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__PROJECTLAUNCHER = EnvironmentPackage.ENVIRONMENT__PROJECTLAUNCHER;

	/**
	 * The feature id for the '<em><b>Docker</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__DOCKER = EnvironmentPackage.ENVIRONMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_FEATURE_COUNT = EnvironmentPackage.ENVIRONMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Launch</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___LAUNCH = EnvironmentPackage.ENVIRONMENT___LAUNCH;

	/**
	 * The number of operations of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_OPERATION_COUNT = EnvironmentPackage.ENVIRONMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.docker.impl.DockerEnvironmentBuilderImpl <em>Environment Builder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.DockerEnvironmentBuilderImpl
	 * @see apps.docker.impl.DockerPackageImpl#getDockerEnvironmentBuilder()
	 * @generated
	 */
	int DOCKER_ENVIRONMENT_BUILDER = 1;

	/**
	 * The number of structural features of the '<em>Environment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER_FEATURE_COUNT = EnvironmentPackage.IENVIRONMENT_BUILDER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER___BUILD__STRING = EnvironmentPackage.IENVIRONMENT_BUILDER___BUILD__STRING;

	/**
	 * The number of operations of the '<em>Environment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER_OPERATION_COUNT = EnvironmentPackage.IENVIRONMENT_BUILDER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.docker.impl.DockerAPIImpl <em>API</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.DockerAPIImpl
	 * @see apps.docker.impl.DockerPackageImpl#getDockerAPI()
	 * @generated
	 */
	int DOCKER_API = 2;

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
	int DOCKER_API___BUILD_IMAGE__STRING = 0;

	/**
	 * The operation id for the '<em>Launch Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___LAUNCH_CONTAINER__STRING_CONTAINERCONFIGURATION = 1;

	/**
	 * The number of operations of the '<em>API</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API_OPERATION_COUNT = 2;

	/**
	 * The meta object id for the '{@link apps.docker.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.impl.ContainerConfigurationImpl
	 * @see apps.docker.impl.DockerPackageImpl#getContainerConfiguration()
	 * @generated
	 */
	int CONTAINER_CONFIGURATION = 3;

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
	 * The number of structural features of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_OPERATION_COUNT = 0;


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
	 * Returns the meta object for class '{@link apps.docker.DockerEnvironmentBuilder <em>Environment Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment Builder</em>'.
	 * @see apps.docker.DockerEnvironmentBuilder
	 * @generated
	 */
	EClass getDockerEnvironmentBuilder();

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
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#buildImage(java.lang.String) <em>Build Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Build Image</em>' operation.
	 * @see apps.docker.DockerAPI#buildImage(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__BuildImage__String();

	/**
	 * Returns the meta object for the '{@link apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration) <em>Launch Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Container</em>' operation.
	 * @see apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration)
	 * @generated
	 */
	EOperation getDockerAPI__LaunchContainer__String_ContainerConfiguration();

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
		 * The meta object literal for the '{@link apps.docker.impl.DockerEnvironmentBuilderImpl <em>Environment Builder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.docker.impl.DockerEnvironmentBuilderImpl
		 * @see apps.docker.impl.DockerPackageImpl#getDockerEnvironmentBuilder()
		 * @generated
		 */
		EClass DOCKER_ENVIRONMENT_BUILDER = eINSTANCE.getDockerEnvironmentBuilder();

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
		EOperation DOCKER_API___BUILD_IMAGE__STRING = eINSTANCE.getDockerAPI__BuildImage__String();

		/**
		 * The meta object literal for the '<em><b>Launch Container</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___LAUNCH_CONTAINER__STRING_CONTAINERCONFIGURATION = eINSTANCE.getDockerAPI__LaunchContainer__String_ContainerConfiguration();

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

	}

} //DockerPackage
