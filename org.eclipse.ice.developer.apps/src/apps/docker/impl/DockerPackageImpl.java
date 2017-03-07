/**
 */
package apps.docker.impl;

import apps.AppsPackage;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import apps.docker.DockerPackage;

import apps.docker.DockerProjectLauncher;
import apps.impl.AppsPackageImpl;
import apps.local.LocalPackage;

import apps.local.impl.LocalPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DockerPackageImpl extends EPackageImpl implements DockerPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dockerEnvironmentEClass = null;

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
	private EClass dockerProjectLauncherEClass = null;

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
	 * @see apps.docker.DockerPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DockerPackageImpl() {
		super(eNS_URI, DockerFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link DockerPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DockerPackage init() {
		if (isInited) return (DockerPackage)EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI);

		// Obtain or create and register package
		DockerPackageImpl theDockerPackage = (DockerPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DockerPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DockerPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		AppsPackageImpl theAppsPackage = (AppsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI) instanceof AppsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI) : AppsPackage.eINSTANCE);
		LocalPackageImpl theLocalPackage = (LocalPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) instanceof LocalPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) : LocalPackage.eINSTANCE);

		// Create package meta-data objects
		theDockerPackage.createPackageContents();
		theAppsPackage.createPackageContents();
		theLocalPackage.createPackageContents();

		// Initialize created meta-data
		theDockerPackage.initializePackageContents();
		theAppsPackage.initializePackageContents();
		theLocalPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDockerPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DockerPackage.eNS_URI, theDockerPackage);
		return theDockerPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDockerEnvironment() {
		return dockerEnvironmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDockerEnvironment_Docker() {
		return (EReference)dockerEnvironmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDockerEnvironment_ContainerConfiguration() {
		return (EReference)dockerEnvironmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDockerEnvironment_Dockerfile() {
		return (EAttribute)dockerEnvironmentEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getDockerAPI_ContainerRemotePort() {
		return (EAttribute)dockerAPIEClass.getEStructuralFeatures().get(0);
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
	public EClass getDockerProjectLauncher() {
		return dockerProjectLauncherEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDockerProjectLauncher_Containerconfiguration() {
		return (EReference)dockerProjectLauncherEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDockerProjectLauncher_ProjectName() {
		return (EAttribute)dockerProjectLauncherEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerProjectLauncher__LaunchProject__SourcePackage() {
		return dockerProjectLauncherEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerProjectLauncher__UpdateConnection__int() {
		return dockerProjectLauncherEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerFactory getDockerFactory() {
		return (DockerFactory)getEFactoryInstance();
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
		dockerEnvironmentEClass = createEClass(DOCKER_ENVIRONMENT);
		createEReference(dockerEnvironmentEClass, DOCKER_ENVIRONMENT__DOCKER);
		createEReference(dockerEnvironmentEClass, DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION);
		createEAttribute(dockerEnvironmentEClass, DOCKER_ENVIRONMENT__DOCKERFILE);

		dockerAPIEClass = createEClass(DOCKER_API);
		createEAttribute(dockerAPIEClass, DOCKER_API__CONTAINER_REMOTE_PORT);
		createEOperation(dockerAPIEClass, DOCKER_API___BUILD_IMAGE__STRING_STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION);
		createEOperation(dockerAPIEClass, DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___DELETE_CONTAINER__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___DELETE_IMAGE__STRING);
		createEOperation(dockerAPIEClass, DOCKER_API___STOP_CONTAINER__STRING);

		containerConfigurationEClass = createEClass(CONTAINER_CONFIGURATION);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__NAME);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__EPHEMERAL);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__PORTS);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__VOLUMES_CONFIG);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__REMOTE_SSH_PORT);
		createEAttribute(containerConfigurationEClass, CONTAINER_CONFIGURATION__ID);

		dockerProjectLauncherEClass = createEClass(DOCKER_PROJECT_LAUNCHER);
		createEReference(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION);
		createEAttribute(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER__PROJECT_NAME);
		createEOperation(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE);
		createEOperation(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER___UPDATE_CONNECTION__INT);
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
		AppsPackage theAppsPackage = (AppsPackage)EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		dockerEnvironmentEClass.getESuperTypes().add(theAppsPackage.getIEnvironment());
		dockerProjectLauncherEClass.getESuperTypes().add(theAppsPackage.getProjectLauncher());

		// Initialize classes, features, and operations; add parameters
		initEClass(dockerEnvironmentEClass, DockerEnvironment.class, "DockerEnvironment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDockerEnvironment_Docker(), this.getDockerAPI(), null, "docker", null, 0, 1, DockerEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDockerEnvironment_ContainerConfiguration(), this.getContainerConfiguration(), null, "containerConfiguration", null, 0, 1, DockerEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDockerEnvironment_Dockerfile(), ecorePackage.getEString(), "Dockerfile", null, 0, 1, DockerEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dockerAPIEClass, DockerAPI.class, "DockerAPI", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDockerAPI_ContainerRemotePort(), ecorePackage.getEInt(), "containerRemotePort", null, 0, 1, DockerAPI.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		initEClass(containerConfigurationEClass, ContainerConfiguration.class, "ContainerConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getContainerConfiguration_Name(), ecorePackage.getEString(), "name", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Ephemeral(), ecorePackage.getEBoolean(), "ephemeral", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Ports(), theXMLTypePackage.getInt(), "ports", null, 0, -1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_VolumesConfig(), ecorePackage.getEString(), "volumesConfig", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_RemoteSSHPort(), theXMLTypePackage.getInt(), "remoteSSHPort", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainerConfiguration_Id(), ecorePackage.getEString(), "id", null, 0, 1, ContainerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dockerProjectLauncherEClass, DockerProjectLauncher.class, "DockerProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDockerProjectLauncher_Containerconfiguration(), this.getContainerConfiguration(), null, "containerconfiguration", null, 0, 1, DockerProjectLauncher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDockerProjectLauncher_ProjectName(), ecorePackage.getEString(), "projectName", null, 0, 1, DockerProjectLauncher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getDockerProjectLauncher__LaunchProject__SourcePackage(), theXMLTypePackage.getBoolean(), "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theAppsPackage.getSourcePackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerProjectLauncher__UpdateConnection__int(), null, "updateConnection", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "port", 0, 1, IS_UNIQUE, IS_ORDERED);
	}

} //DockerPackageImpl
