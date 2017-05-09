/**
 */
package org.eclipse.ice.docker.api.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.ice.docker.api.ContainerConfiguration;
import org.eclipse.ice.docker.api.DockerAPI;
import org.eclipse.ice.docker.api.DockerMessageConsole;
import org.eclipse.ice.docker.api.DockerapiFactory;
import org.eclipse.ice.docker.api.DockerapiPackage;
import org.eclipse.ice.docker.api.StdOutConsole;
import org.eclipse.ice.docker.api.spotify.SpotifyPackage;
import org.eclipse.ice.docker.api.spotify.impl.SpotifyPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DockerapiPackageImpl extends EPackageImpl implements DockerapiPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dockerAPIEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass containerConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dockerMessageConsoleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stdOutConsoleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType commandListEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.ice.docker.api.DockerapiPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DockerapiPackageImpl() {
		super(eNS_URI, DockerapiFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link DockerapiPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DockerapiPackage init() {
		if (isInited) return (DockerapiPackage)EPackage.Registry.INSTANCE.getEPackage(DockerapiPackage.eNS_URI);

		// Obtain or create and register package
		DockerapiPackageImpl theDockerapiPackage = (DockerapiPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DockerapiPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DockerapiPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		SpotifyPackageImpl theSpotifyPackage = (SpotifyPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SpotifyPackage.eNS_URI) instanceof SpotifyPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SpotifyPackage.eNS_URI) : SpotifyPackage.eINSTANCE);

		// Create package meta-data objects
		theDockerapiPackage.createPackageContents();
		theSpotifyPackage.createPackageContents();

		// Initialize created meta-data
		theDockerapiPackage.initializePackageContents();
		theSpotifyPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDockerapiPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DockerapiPackage.eNS_URI, theDockerapiPackage);
		return theDockerapiPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDockerAPI() {
		return dockerAPIEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDockerAPI_Console() {
		return (EReference)dockerAPIEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__BuildImage__String_String() {
		return dockerAPIEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__CreateContainer__String_ContainerConfiguration() {
		return dockerAPIEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__ConnectToExistingContainer__String() {
		return dockerAPIEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__DeleteContainer__String() {
		return dockerAPIEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__DeleteImage__String() {
		return dockerAPIEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__StopContainer__String() {
		return dockerAPIEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__CreateContainerExecCommand__String_String() {
		return dockerAPIEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__Pull__String() {
		return dockerAPIEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__ListAvailableImages() {
		return dockerAPIEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerAPI__IsContainerRunning__String() {
		return dockerAPIEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContainerConfiguration() {
		return containerConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_Name() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_Ephemeral() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_Ports() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_VolumesConfig() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_RemoteSSHPort() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContainerConfiguration_Id() {
		return (EAttribute)containerConfigurationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDockerMessageConsole() {
		return dockerMessageConsoleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerMessageConsole__Print__String() {
		return dockerMessageConsoleEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStdOutConsole() {
		return stdOutConsoleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCommandList() {
		return commandListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerapiFactory getDockerapiFactory() {
		return (DockerapiFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		dockerAPIEClass = createEClass(DOCKER_API);
		createEReference(dockerAPIEClass, DOCKER_API__CONSOLE);
		createEOperation(dockerAPIEClass, DOCKER_API___BUILD_IMAGE__STRING_STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION);
		createEOperation(dockerAPIEClass, DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___DELETE_CONTAINER__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___DELETE_IMAGE__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___STOP_CONTAINER__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___CREATE_CONTAINER_EXEC_COMMAND__STRING_STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___PULL__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___LIST_AVAILABLE_IMAGES);
		createEOperation(dockerAPIEClass, DOCKER_API___IS_CONTAINER_RUNNING__STRING);

		containerConfigurationEClass = createEClass(CONTAINER_CONFIGURATION);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__NAME);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__EPHEMERAL);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__PORTS);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__VOLUMES_CONFIG);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__REMOTE_SSH_PORT);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__ID);

		dockerMessageConsoleEClass = createEClass(DOCKER_MESSAGE_CONSOLE);
		createEOperation(dockerMessageConsoleEClass, DOCKER_MESSAGE_CONSOLE___PRINT__STRING);

		stdOutConsoleEClass = createEClass(STD_OUT_CONSOLE);

		// Create data types
		commandListEDataType = createEDataType(COMMAND_LIST);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		SpotifyPackage theSpotifyPackage = (SpotifyPackage)EPackage.Registry.INSTANCE.getEPackage(SpotifyPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theSpotifyPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		stdOutConsoleEClass.getESuperTypes().add(this.getDockerMessageConsole());

		// Initialize classes, features, and operations; add parameters
		initEClass(dockerAPIEClass, DockerAPI.class, "DockerAPI", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDockerAPI_Console(), this.getDockerMessageConsole(), null, "console", null, 1, 1, DockerAPI.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getDockerAPI__BuildImage__String_String(), theXMLTypePackage.getBoolean(), "buildImage", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "buildFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imagename", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__CreateContainer__String_ContainerConfiguration(), theXMLTypePackage.getBoolean(), "createContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imageName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getContainerConfiguration(), "config", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__ConnectToExistingContainer__String(), theXMLTypePackage.getBoolean(), "connectToExistingContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "id", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__DeleteContainer__String(), theXMLTypePackage.getBoolean(), "deleteContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "id", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__DeleteImage__String(), theXMLTypePackage.getBoolean(), "deleteImage", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imageName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__StopContainer__String(), theXMLTypePackage.getBoolean(), "stopContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "id", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__CreateContainerExecCommand__String_String(), ecorePackage.getEString(), "createContainerExecCommand", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imageName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getCommandList(), "command", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__Pull__String(), null, "pull", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imageName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDockerAPI__ListAvailableImages(), ecorePackage.getEString(), "listAvailableImages", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerAPI__IsContainerRunning__String(), theXMLTypePackage.getBoolean(), "isContainerRunning", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "id", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(containerConfigurationEClass, ContainerConfiguration.class, "ContainerConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getContainerConfiguration_Name(), ecorePackage.getEString(), "name", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Ephemeral(), ecorePackage.getEBoolean(), "ephemeral", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Ports(), theXMLTypePackage.getInt(), "ports", null, 0, -1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_VolumesConfig(), ecorePackage.getEString(), "volumesConfig", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_RemoteSSHPort(), theXMLTypePackage.getInt(), "remoteSSHPort", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Id(), ecorePackage.getEString(), "id", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dockerMessageConsoleEClass, DockerMessageConsole.class, "DockerMessageConsole", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getDockerMessageConsole__Print__String(), null, "print", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "msg", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(stdOutConsoleEClass, StdOutConsole.class, "StdOutConsole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize data types
		initEDataType(commandListEDataType, String[].class, "CommandList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //DockerapiPackageImpl
