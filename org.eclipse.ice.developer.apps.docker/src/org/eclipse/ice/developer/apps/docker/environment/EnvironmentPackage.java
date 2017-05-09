/**
 */
package org.eclipse.ice.developer.apps.docker.environment;

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
 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentFactory
 * @model kind="package"
 * @generated
 */
public interface EnvironmentPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "environment";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/ice/apps/docker";

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
	EnvironmentPackage eINSTANCE = org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl <em>Docker Environment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl
	 * @see org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl#getDockerEnvironment()
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
	 * The feature id for the '<em><b>Console</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__CONSOLE = AppsPackage.IENVIRONMENT__CONSOLE;

	/**
	 * The feature id for the '<em><b>Container Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dockerfile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__DOCKERFILE = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Docker API</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT__DOCKER_API = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Docker Environment</em>' class.
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
	 * The operation id for the '<em>Execute</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___EXECUTE__STRING_STRING = AppsPackage.IENVIRONMENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Has Docker</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___HAS_DOCKER = AppsPackage.IENVIRONMENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Pull Image</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT___PULL_IMAGE__STRING = AppsPackage.IENVIRONMENT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Docker Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_OPERATION_COUNT = AppsPackage.IENVIRONMENT_OPERATION_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerProjectLauncherImpl <em>Docker Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.apps.docker.environment.impl.DockerProjectLauncherImpl
	 * @see org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl#getDockerProjectLauncher()
	 * @generated
	 */
	int DOCKER_PROJECT_LAUNCHER = 1;

	/**
	 * The feature id for the '<em><b>Languageprojectprovider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER = AppsPackage.PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER;

	/**
	 * The feature id for the '<em><b>Environmentconsole</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER__ENVIRONMENTCONSOLE = AppsPackage.PROJECT_LAUNCHER__ENVIRONMENTCONSOLE;

	/**
	 * The feature id for the '<em><b>Containerconfiguration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION = AppsPackage.PROJECT_LAUNCHER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER__PROJECT_NAME = AppsPackage.PROJECT_LAUNCHER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Docker Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER_FEATURE_COUNT = AppsPackage.PROJECT_LAUNCHER_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = AppsPackage.PROJECT_LAUNCHER_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Update Connection</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER___UPDATE_CONNECTION__INT = AppsPackage.PROJECT_LAUNCHER_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Docker Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_PROJECT_LAUNCHER_OPERATION_COUNT = AppsPackage.PROJECT_LAUNCHER_OPERATION_COUNT + 2;


	/**
	 * The meta object id for the '{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentBuilderImpl <em>Docker Environment Builder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentBuilderImpl
	 * @see org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl#getDockerEnvironmentBuilder()
	 * @generated
	 */
	int DOCKER_ENVIRONMENT_BUILDER = 2;

	/**
	 * The number of structural features of the '<em>Docker Environment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER_FEATURE_COUNT = AppsPackage.ENVIRONMENT_BUILDER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER___BUILD = AppsPackage.ENVIRONMENT_BUILDER___BUILD;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER___NAME = AppsPackage.ENVIRONMENT_BUILDER___NAME;

	/**
	 * The number of operations of the '<em>Docker Environment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_ENVIRONMENT_BUILDER_OPERATION_COUNT = AppsPackage.ENVIRONMENT_BUILDER_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment <em>Docker Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker Environment</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment
	 * @generated
	 */
	EClass getDockerEnvironment();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getContainerConfiguration <em>Container Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Container Configuration</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getContainerConfiguration()
	 * @see #getDockerEnvironment()
	 * @generated
	 */
	EReference getDockerEnvironment_ContainerConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerfile <em>Dockerfile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dockerfile</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerfile()
	 * @see #getDockerEnvironment()
	 * @generated
	 */
	EAttribute getDockerEnvironment_Dockerfile();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerAPI <em>Docker API</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Docker API</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#getDockerAPI()
	 * @see #getDockerEnvironment()
	 * @generated
	 */
	EReference getDockerEnvironment_DockerAPI();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#execute(java.lang.String, java.lang.String[]) <em>Execute</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Execute</em>' operation.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#execute(java.lang.String, java.lang.String[])
	 * @generated
	 */
	EOperation getDockerEnvironment__Execute__String_String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#hasDocker() <em>Has Docker</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Has Docker</em>' operation.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#hasDocker()
	 * @generated
	 */
	EOperation getDockerEnvironment__HasDocker();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#pullImage(java.lang.String) <em>Pull Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Pull Image</em>' operation.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment#pullImage(java.lang.String)
	 * @generated
	 */
	EOperation getDockerEnvironment__PullImage__String();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher <em>Docker Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker Project Launcher</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher
	 * @generated
	 */
	EClass getDockerProjectLauncher();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getContainerconfiguration <em>Containerconfiguration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Containerconfiguration</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getContainerconfiguration()
	 * @see #getDockerProjectLauncher()
	 * @generated
	 */
	EReference getDockerProjectLauncher_Containerconfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getProjectName <em>Project Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Project Name</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#getProjectName()
	 * @see #getDockerProjectLauncher()
	 * @generated
	 */
	EAttribute getDockerProjectLauncher_ProjectName();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#launchProject(apps.SourcePackage)
	 * @generated
	 */
	EOperation getDockerProjectLauncher__LaunchProject__SourcePackage();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#updateConnection(int) <em>Update Connection</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Update Connection</em>' operation.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher#updateConnection(int)
	 * @generated
	 */
	EOperation getDockerProjectLauncher__UpdateConnection__int();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.developer.apps.docker.environment.DockerEnvironmentBuilder <em>Docker Environment Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker Environment Builder</em>'.
	 * @see org.eclipse.ice.developer.apps.docker.environment.DockerEnvironmentBuilder
	 * @generated
	 */
	EClass getDockerEnvironmentBuilder();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EnvironmentFactory getEnvironmentFactory();

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
		 * The meta object literal for the '{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl <em>Docker Environment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentImpl
		 * @see org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl#getDockerEnvironment()
		 * @generated
		 */
		EClass DOCKER_ENVIRONMENT = eINSTANCE.getDockerEnvironment();

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
		 * The meta object literal for the '<em><b>Docker API</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCKER_ENVIRONMENT__DOCKER_API = eINSTANCE.getDockerEnvironment_DockerAPI();

		/**
		 * The meta object literal for the '<em><b>Execute</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_ENVIRONMENT___EXECUTE__STRING_STRING = eINSTANCE.getDockerEnvironment__Execute__String_String();

		/**
		 * The meta object literal for the '<em><b>Has Docker</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_ENVIRONMENT___HAS_DOCKER = eINSTANCE.getDockerEnvironment__HasDocker();

		/**
		 * The meta object literal for the '<em><b>Pull Image</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_ENVIRONMENT___PULL_IMAGE__STRING = eINSTANCE.getDockerEnvironment__PullImage__String();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerProjectLauncherImpl <em>Docker Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.apps.docker.environment.impl.DockerProjectLauncherImpl
		 * @see org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl#getDockerProjectLauncher()
		 * @generated
		 */
		EClass DOCKER_PROJECT_LAUNCHER = eINSTANCE.getDockerProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Containerconfiguration</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION = eINSTANCE.getDockerProjectLauncher_Containerconfiguration();

		/**
		 * The meta object literal for the '<em><b>Project Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCKER_PROJECT_LAUNCHER__PROJECT_NAME = eINSTANCE.getDockerProjectLauncher_ProjectName();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = eINSTANCE.getDockerProjectLauncher__LaunchProject__SourcePackage();

		/**
		 * The meta object literal for the '<em><b>Update Connection</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_PROJECT_LAUNCHER___UPDATE_CONNECTION__INT = eINSTANCE.getDockerProjectLauncher__UpdateConnection__int();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentBuilderImpl <em>Docker Environment Builder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.developer.apps.docker.environment.impl.DockerEnvironmentBuilderImpl
		 * @see org.eclipse.ice.developer.apps.docker.environment.impl.EnvironmentPackageImpl#getDockerEnvironmentBuilder()
		 * @generated
		 */
		EClass DOCKER_ENVIRONMENT_BUILDER = eINSTANCE.getDockerEnvironmentBuilder();

	}

} //EnvironmentPackage
