/**
 */
package org.eclipse.ice.developer.apps.docker.environment.impl;

import apps.AppsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.ice.developer.apps.docker.environment.DockerEnvironment;
import org.eclipse.ice.developer.apps.docker.environment.DockerEnvironmentBuilder;
import org.eclipse.ice.developer.apps.docker.environment.DockerProjectLauncher;
import org.eclipse.ice.developer.apps.docker.environment.EnvironmentFactory;
import org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage;

import org.eclipse.ice.docker.api.DockerapiPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EnvironmentPackageImpl extends EPackageImpl implements EnvironmentPackage {
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
	private EClass dockerProjectLauncherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dockerEnvironmentBuilderEClass = null;

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
	 * @see org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EnvironmentPackageImpl() {
		super(eNS_URI, EnvironmentFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EnvironmentPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EnvironmentPackage init() {
		if (isInited) return (EnvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(EnvironmentPackage.eNS_URI);

		// Obtain or create and register package
		EnvironmentPackageImpl theEnvironmentPackage = (EnvironmentPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EnvironmentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EnvironmentPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		AppsPackage.eINSTANCE.eClass();
		DockerapiPackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theEnvironmentPackage.createPackageContents();

		// Initialize created meta-data
		theEnvironmentPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEnvironmentPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EnvironmentPackage.eNS_URI, theEnvironmentPackage);
		return theEnvironmentPackage;
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
	public EReference getDockerEnvironment_ContainerConfiguration() {
		return (EReference)dockerEnvironmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDockerEnvironment_Dockerfile() {
		return (EAttribute)dockerEnvironmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDockerEnvironment_DockerAPI() {
		return (EReference)dockerEnvironmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerEnvironment__Execute__String_String() {
		return dockerEnvironmentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerEnvironment__HasDocker() {
		return dockerEnvironmentEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerEnvironment__PullImage__String() {
		return dockerEnvironmentEClass.getEOperations().get(2);
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
	public EClass getDockerEnvironmentBuilder() {
		return dockerEnvironmentBuilderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentFactory getEnvironmentFactory() {
		return (EnvironmentFactory)getEFactoryInstance();
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
		createEReference(dockerEnvironmentEClass, DOCKER_ENVIRONMENT__CONTAINER_CONFIGURATION);
		createEAttribute(dockerEnvironmentEClass, DOCKER_ENVIRONMENT__DOCKERFILE);
		createEReference(dockerEnvironmentEClass, DOCKER_ENVIRONMENT__DOCKER_API);
		createEOperation(dockerEnvironmentEClass, DOCKER_ENVIRONMENT___EXECUTE__STRING_STRING);
		createEOperation(dockerEnvironmentEClass, DOCKER_ENVIRONMENT___HAS_DOCKER);
		createEOperation(dockerEnvironmentEClass, DOCKER_ENVIRONMENT___PULL_IMAGE__STRING);

		dockerProjectLauncherEClass = createEClass(DOCKER_PROJECT_LAUNCHER);
		createEReference(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION);
		createEAttribute(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER__PROJECT_NAME);
		createEOperation(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE);
		createEOperation(dockerProjectLauncherEClass, DOCKER_PROJECT_LAUNCHER___UPDATE_CONNECTION__INT);

		dockerEnvironmentBuilderEClass = createEClass(DOCKER_ENVIRONMENT_BUILDER);
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
		DockerapiPackage theDockerapiPackage = (DockerapiPackage)EPackage.Registry.INSTANCE.getEPackage(DockerapiPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		dockerEnvironmentEClass.getESuperTypes().add(theAppsPackage.getIEnvironment());
		dockerProjectLauncherEClass.getESuperTypes().add(theAppsPackage.getProjectLauncher());
		dockerEnvironmentBuilderEClass.getESuperTypes().add(theAppsPackage.getEnvironmentBuilder());

		// Initialize classes, features, and operations; add parameters
		initEClass(dockerEnvironmentEClass, DockerEnvironment.class, "DockerEnvironment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDockerEnvironment_ContainerConfiguration(), theDockerapiPackage.getContainerConfiguration(), null, "containerConfiguration", null, 0, 1, DockerEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDockerEnvironment_Dockerfile(), ecorePackage.getEString(), "Dockerfile", null, 0, 1, DockerEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDockerEnvironment_DockerAPI(), theDockerapiPackage.getDockerAPI(), null, "dockerAPI", null, 1, 1, DockerEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getDockerEnvironment__Execute__String_String(), ecorePackage.getEString(), "execute", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imageName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theDockerapiPackage.getCommandList(), "command", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDockerEnvironment__HasDocker(), theXMLTypePackage.getBoolean(), "hasDocker", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerEnvironment__PullImage__String(), null, "pullImage", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "imageName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(dockerProjectLauncherEClass, DockerProjectLauncher.class, "DockerProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDockerProjectLauncher_Containerconfiguration(), theDockerapiPackage.getContainerConfiguration(), null, "containerconfiguration", null, 0, 1, DockerProjectLauncher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDockerProjectLauncher_ProjectName(), ecorePackage.getEString(), "projectName", null, 0, 1, DockerProjectLauncher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getDockerProjectLauncher__LaunchProject__SourcePackage(), theXMLTypePackage.getBoolean(), "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theAppsPackage.getSourcePackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getDockerProjectLauncher__UpdateConnection__int(), null, "updateConnection", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "port", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(dockerEnvironmentBuilderEClass, DockerEnvironmentBuilder.class, "DockerEnvironmentBuilder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.obeo.fr/dsl/dnc/archetype
		createArchetypeAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.obeo.fr/dsl/dnc/archetype</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createArchetypeAnnotations() {
		String source = "http://www.obeo.fr/dsl/dnc/archetype";	
		addAnnotation
		  (dockerProjectLauncherEClass, 
		   source, 
		   new String[] {
			 "archetype", "Thing"
		   });
	}

} //EnvironmentPackageImpl
