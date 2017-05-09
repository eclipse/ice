/**
 */
package org.eclipse.ice.docker.api;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.ice.docker.api.DockerapiFactory
 * @model kind="package"
 * @generated
 */
public interface DockerapiPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "api";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/ice/docker";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "dockerapi";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DockerapiPackage eINSTANCE = org.eclipse.ice.docker.api.impl.DockerapiPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ice.docker.api.DockerAPI <em>Docker API</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.DockerAPI
	 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getDockerAPI()
	 * @generated
	 */
	int DOCKER_API = 0;

	/**
	 * The feature id for the '<em><b>Console</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API__CONSOLE = 0;

	/**
	 * The number of structural features of the '<em>Docker API</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API_FEATURE_COUNT = 1;

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
	 * The operation id for the '<em>Stop Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___STOP_CONTAINER__STRING = 5;

	/**
	 * The operation id for the '<em>Create Container Exec Command</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING = 6;

	/**
	 * The operation id for the '<em>Pull</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___PULL__STRING = 7;

	/**
	 * The operation id for the '<em>List Available Images</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___LIST_AVAILABLE_IMAGES = 8;

	/**
	 * The operation id for the '<em>Is Container Running</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API___IS_CONTAINER_RUNNING__STRING = 9;

	/**
	 * The number of operations of the '<em>Docker API</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_API_OPERATION_COUNT = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.docker.api.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.impl.ContainerConfigurationImpl
	 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getContainerConfiguration()
	 * @generated
	 */
	int CONTAINER_CONFIGURATION = 1;

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
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION__ID = 5;

	/**
	 * The number of structural features of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Container Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.docker.api.DockerMessageConsole <em>Docker Message Console</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.DockerMessageConsole
	 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getDockerMessageConsole()
	 * @generated
	 */
	int DOCKER_MESSAGE_CONSOLE = 2;

	/**
	 * The number of structural features of the '<em>Docker Message Console</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_MESSAGE_CONSOLE_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Print</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_MESSAGE_CONSOLE___PRINT__STRING = 0;

	/**
	 * The number of operations of the '<em>Docker Message Console</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCKER_MESSAGE_CONSOLE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ice.docker.api.impl.StdOutConsoleImpl <em>Std Out Console</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.impl.StdOutConsoleImpl
	 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getStdOutConsole()
	 * @generated
	 */
	int STD_OUT_CONSOLE = 3;

	/**
	 * The number of structural features of the '<em>Std Out Console</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STD_OUT_CONSOLE_FEATURE_COUNT = DOCKER_MESSAGE_CONSOLE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Print</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STD_OUT_CONSOLE___PRINT__STRING = DOCKER_MESSAGE_CONSOLE___PRINT__STRING;

	/**
	 * The number of operations of the '<em>Std Out Console</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STD_OUT_CONSOLE_OPERATION_COUNT = DOCKER_MESSAGE_CONSOLE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '<em>Command List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getCommandList()
	 * @generated
	 */
	int COMMAND_LIST = 4;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.docker.api.DockerAPI <em>Docker API</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker API</em>'.
	 * @see org.eclipse.ice.docker.api.DockerAPI
	 * @generated
	 */
	EClass getDockerAPI();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ice.docker.api.DockerAPI#getConsole <em>Console</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Console</em>'.
	 * @see org.eclipse.ice.docker.api.DockerAPI#getConsole()
	 * @see #getDockerAPI()
	 * @generated
	 */
	EReference getDockerAPI_Console();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#buildImage(java.lang.String, java.lang.String) <em>Build Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Build Image</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#buildImage(java.lang.String, java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__BuildImage__String_String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#createContainer(java.lang.String, org.eclipse.ice.docker.api.ContainerConfiguration) <em>Create Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Container</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#createContainer(java.lang.String, org.eclipse.ice.docker.api.ContainerConfiguration)
	 * @generated
	 */
	EOperation getDockerAPI__CreateContainer__String_ContainerConfiguration();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#connectToExistingContainer(java.lang.String) <em>Connect To Existing Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Connect To Existing Container</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#connectToExistingContainer(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__ConnectToExistingContainer__String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#deleteContainer(java.lang.String) <em>Delete Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Delete Container</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#deleteContainer(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__DeleteContainer__String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#deleteImage(java.lang.String) <em>Delete Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Delete Image</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#deleteImage(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__DeleteImage__String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#stopContainer(java.lang.String) <em>Stop Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Stop Container</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#stopContainer(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__StopContainer__String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#createContainerExecCommand(java.lang.String, java.lang.String[]) <em>Create Container Exec Command</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Container Exec Command</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#createContainerExecCommand(java.lang.String, java.lang.String[])
	 * @generated
	 */
	EOperation getDockerAPI__CreateContainerExecCommand__String_String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#pull(java.lang.String) <em>Pull</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Pull</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#pull(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__Pull__String();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#listAvailableImages() <em>List Available Images</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>List Available Images</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#listAvailableImages()
	 * @generated
	 */
	EOperation getDockerAPI__ListAvailableImages();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerAPI#isContainerRunning(java.lang.String) <em>Is Container Running</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Container Running</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerAPI#isContainerRunning(java.lang.String)
	 * @generated
	 */
	EOperation getDockerAPI__IsContainerRunning__String();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.docker.api.ContainerConfiguration <em>Container Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container Configuration</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration
	 * @generated
	 */
	EClass getContainerConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.docker.api.ContainerConfiguration#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration#getName()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.docker.api.ContainerConfiguration#isEphemeral <em>Ephemeral</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ephemeral</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration#isEphemeral()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Ephemeral();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ice.docker.api.ContainerConfiguration#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Ports</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration#getPorts()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Ports();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.docker.api.ContainerConfiguration#getVolumesConfig <em>Volumes Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volumes Config</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration#getVolumesConfig()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_VolumesConfig();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.docker.api.ContainerConfiguration#getRemoteSSHPort <em>Remote SSH Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote SSH Port</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration#getRemoteSSHPort()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_RemoteSSHPort();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ice.docker.api.ContainerConfiguration#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ice.docker.api.ContainerConfiguration#getId()
	 * @see #getContainerConfiguration()
	 * @generated
	 */
	EAttribute getContainerConfiguration_Id();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.docker.api.DockerMessageConsole <em>Docker Message Console</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Docker Message Console</em>'.
	 * @see org.eclipse.ice.docker.api.DockerMessageConsole
	 * @generated
	 */
	EClass getDockerMessageConsole();

	/**
	 * Returns the meta object for the '{@link org.eclipse.ice.docker.api.DockerMessageConsole#print(java.lang.String) <em>Print</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Print</em>' operation.
	 * @see org.eclipse.ice.docker.api.DockerMessageConsole#print(java.lang.String)
	 * @generated
	 */
	EOperation getDockerMessageConsole__Print__String();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ice.docker.api.StdOutConsole <em>Std Out Console</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Std Out Console</em>'.
	 * @see org.eclipse.ice.docker.api.StdOutConsole
	 * @generated
	 */
	EClass getStdOutConsole();

	/**
	 * Returns the meta object for data type '<em>Command List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Command List</em>'.
	 * @model instanceClass="java.lang.String[]"
	 * @generated
	 */
	EDataType getCommandList();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DockerapiFactory getDockerapiFactory();

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
		 * The meta object literal for the '{@link org.eclipse.ice.docker.api.DockerAPI <em>Docker API</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.docker.api.DockerAPI
		 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getDockerAPI()
		 * @generated
		 */
		EClass DOCKER_API = eINSTANCE.getDockerAPI();

		/**
		 * The meta object literal for the '<em><b>Console</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCKER_API__CONSOLE = eINSTANCE.getDockerAPI_Console();

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
		 * The meta object literal for the '<em><b>Stop Container</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___STOP_CONTAINER__STRING = eINSTANCE.getDockerAPI__StopContainer__String();

		/**
		 * The meta object literal for the '<em><b>Create Container Exec Command</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING = eINSTANCE.getDockerAPI__CreateContainerExecCommand__String_String();

		/**
		 * The meta object literal for the '<em><b>Pull</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___PULL__STRING = eINSTANCE.getDockerAPI__Pull__String();

		/**
		 * The meta object literal for the '<em><b>List Available Images</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___LIST_AVAILABLE_IMAGES = eINSTANCE.getDockerAPI__ListAvailableImages();

		/**
		 * The meta object literal for the '<em><b>Is Container Running</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_API___IS_CONTAINER_RUNNING__STRING = eINSTANCE.getDockerAPI__IsContainerRunning__String();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.docker.api.impl.ContainerConfigurationImpl <em>Container Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.docker.api.impl.ContainerConfigurationImpl
		 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getContainerConfiguration()
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
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER_CONFIGURATION__ID = eINSTANCE.getContainerConfiguration_Id();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.docker.api.DockerMessageConsole <em>Docker Message Console</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.docker.api.DockerMessageConsole
		 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getDockerMessageConsole()
		 * @generated
		 */
		EClass DOCKER_MESSAGE_CONSOLE = eINSTANCE.getDockerMessageConsole();

		/**
		 * The meta object literal for the '<em><b>Print</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DOCKER_MESSAGE_CONSOLE___PRINT__STRING = eINSTANCE.getDockerMessageConsole__Print__String();

		/**
		 * The meta object literal for the '{@link org.eclipse.ice.docker.api.impl.StdOutConsoleImpl <em>Std Out Console</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.docker.api.impl.StdOutConsoleImpl
		 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getStdOutConsole()
		 * @generated
		 */
		EClass STD_OUT_CONSOLE = eINSTANCE.getStdOutConsole();

		/**
		 * The meta object literal for the '<em>Command List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ice.docker.api.impl.DockerapiPackageImpl#getCommandList()
		 * @generated
		 */
		EDataType COMMAND_LIST = eINSTANCE.getCommandList();

	}

} //DockerapiPackage
