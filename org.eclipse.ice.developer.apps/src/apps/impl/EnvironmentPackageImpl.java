/**
 */
package apps.impl;

import apps.Environment;
import apps.EnvironmentFactory;
import apps.EnvironmentManager;
import apps.EnvironmentPackage;
import apps.EnvironmentType;
import apps.IEnvironment;
import apps.IEnvironmentBuilder;
import apps.LocalCDTProjectLauncher;
import apps.PTPSyncProjectLauncher;
import apps.ProjectLauncher;
import apps.ScienceApp;
import apps.SpackPackage;

import apps.docker.DockerPackage;

import apps.docker.impl.DockerPackageImpl;

import apps.local.LocalPackage;

import apps.local.impl.LocalPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
public class EnvironmentPackageImpl extends EPackageImpl implements EnvironmentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iEnvironmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iEnvironmentBuilderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass environmentManagerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spackPackageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass environmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass projectLauncherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ptpSyncProjectLauncherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass localCDTProjectLauncherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scienceAppEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum environmentTypeEEnum = null;

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
	 * @see apps.EnvironmentPackage#eNS_URI
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
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		DockerPackageImpl theDockerPackage = (DockerPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) instanceof DockerPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) : DockerPackage.eINSTANCE);
		LocalPackageImpl theLocalPackage = (LocalPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) instanceof LocalPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) : LocalPackage.eINSTANCE);

		// Create package meta-data objects
		theEnvironmentPackage.createPackageContents();
		theDockerPackage.createPackageContents();
		theLocalPackage.createPackageContents();

		// Initialize created meta-data
		theEnvironmentPackage.initializePackageContents();
		theDockerPackage.initializePackageContents();
		theLocalPackage.initializePackageContents();

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
	public EClass getIEnvironment() {
		return iEnvironmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_Type() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_Name() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_Os() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIEnvironment_Spackpackage() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIEnvironment_Scienceapp() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIEnvironment__Launch() {
		return iEnvironmentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIEnvironmentBuilder() {
		return iEnvironmentBuilderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIEnvironmentBuilder__Build__String() {
		return iEnvironmentBuilderEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvironmentManager() {
		return environmentManagerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnvironmentManager_Builder() {
		return (EReference)environmentManagerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__CreateEnvironment__String() {
		return environmentManagerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__ListExisting() {
		return environmentManagerEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__LoadExisting__String() {
		return environmentManagerEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpackPackage() {
		return spackPackageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Name() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_DesiredVersion() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Compiler() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Versions() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_CppFlags() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_VirtualDependency() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_VirtualDependencyProvider() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvironment() {
		return environmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnvironment_Projectlauncher() {
		return (EReference)environmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProjectLauncher() {
		return projectLauncherEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getProjectLauncher__LaunchProject__ScienceApp() {
		return projectLauncherEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPTPSyncProjectLauncher() {
		return ptpSyncProjectLauncherEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocalCDTProjectLauncher() {
		return localCDTProjectLauncherEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScienceApp() {
		return scienceAppEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_RepoURL() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_Name() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_Branch() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_LocalProjectName() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_RecursiveClone() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_AbsolutePath() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_RemotePort() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScienceApp_RemoteHost() {
		return (EAttribute)scienceAppEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEnvironmentType() {
		return environmentTypeEEnum;
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
		iEnvironmentEClass = createEClass(IENVIRONMENT);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__TYPE);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__NAME);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__OS);
		createEReference(iEnvironmentEClass, IENVIRONMENT__SPACKPACKAGE);
		createEReference(iEnvironmentEClass, IENVIRONMENT__SCIENCEAPP);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___LAUNCH);

		iEnvironmentBuilderEClass = createEClass(IENVIRONMENT_BUILDER);
		createEOperation(iEnvironmentBuilderEClass, IENVIRONMENT_BUILDER___BUILD__STRING);

		environmentManagerEClass = createEClass(ENVIRONMENT_MANAGER);
		createEReference(environmentManagerEClass, ENVIRONMENT_MANAGER__BUILDER);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LIST_EXISTING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_EXISTING__STRING);

		spackPackageEClass = createEClass(SPACK_PACKAGE);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__NAME);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__DESIRED_VERSION);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__COMPILER);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VERSIONS);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__CPP_FLAGS);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VIRTUAL_DEPENDENCY);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER);

		environmentEClass = createEClass(ENVIRONMENT);
		createEReference(environmentEClass, ENVIRONMENT__PROJECTLAUNCHER);

		projectLauncherEClass = createEClass(PROJECT_LAUNCHER);
		createEOperation(projectLauncherEClass, PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP);

		ptpSyncProjectLauncherEClass = createEClass(PTP_SYNC_PROJECT_LAUNCHER);

		localCDTProjectLauncherEClass = createEClass(LOCAL_CDT_PROJECT_LAUNCHER);

		scienceAppEClass = createEClass(SCIENCE_APP);
		createEAttribute(scienceAppEClass, SCIENCE_APP__REPO_URL);
		createEAttribute(scienceAppEClass, SCIENCE_APP__NAME);
		createEAttribute(scienceAppEClass, SCIENCE_APP__BRANCH);
		createEAttribute(scienceAppEClass, SCIENCE_APP__LOCAL_PROJECT_NAME);
		createEAttribute(scienceAppEClass, SCIENCE_APP__RECURSIVE_CLONE);
		createEAttribute(scienceAppEClass, SCIENCE_APP__ABSOLUTE_PATH);
		createEAttribute(scienceAppEClass, SCIENCE_APP__REMOTE_PORT);
		createEAttribute(scienceAppEClass, SCIENCE_APP__REMOTE_HOST);

		// Create enums
		environmentTypeEEnum = createEEnum(ENVIRONMENT_TYPE);
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
		DockerPackage theDockerPackage = (DockerPackage)EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI);
		LocalPackage theLocalPackage = (LocalPackage)EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theDockerPackage);
		getESubpackages().add(theLocalPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		environmentEClass.getESuperTypes().add(this.getIEnvironment());
		ptpSyncProjectLauncherEClass.getESuperTypes().add(this.getProjectLauncher());
		localCDTProjectLauncherEClass.getESuperTypes().add(this.getProjectLauncher());

		// Initialize classes, features, and operations; add parameters
		initEClass(iEnvironmentEClass, IEnvironment.class, "IEnvironment", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIEnvironment_Type(), this.getEnvironmentType(), "type", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_Name(), ecorePackage.getEString(), "name", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_Os(), ecorePackage.getEString(), "os", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_Spackpackage(), this.getSpackPackage(), null, "spackpackage", null, 0, -1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_Scienceapp(), this.getScienceApp(), null, "scienceapp", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getIEnvironment__Launch(), theXMLTypePackage.getBoolean(), "launch", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iEnvironmentBuilderEClass, IEnvironmentBuilder.class, "IEnvironmentBuilder", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getIEnvironmentBuilder__Build__String(), this.getIEnvironment(), "build", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "properties", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(environmentManagerEClass, EnvironmentManager.class, "EnvironmentManager", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnvironmentManager_Builder(), this.getIEnvironmentBuilder(), null, "builder", null, 0, 1, EnvironmentManager.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__CreateEnvironment__String(), this.getIEnvironment(), "createEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "properties", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__ListExisting(), ecorePackage.getEString(), "listExisting", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadExisting__String(), this.getIEnvironment(), "loadExisting", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spackPackageEClass, SpackPackage.class, "SpackPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackPackage_Name(), ecorePackage.getEString(), "name", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_DesiredVersion(), ecorePackage.getEString(), "desiredVersion", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_Compiler(), ecorePackage.getEString(), "compiler", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_Versions(), ecorePackage.getEString(), "versions", null, 0, -1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_CppFlags(), ecorePackage.getEString(), "cppFlags", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_VirtualDependency(), ecorePackage.getEString(), "virtualDependency", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_VirtualDependencyProvider(), ecorePackage.getEString(), "virtualDependencyProvider", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(environmentEClass, Environment.class, "Environment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnvironment_Projectlauncher(), this.getProjectLauncher(), null, "projectlauncher", null, 0, 1, Environment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(projectLauncherEClass, ProjectLauncher.class, "ProjectLauncher", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getProjectLauncher__LaunchProject__ScienceApp(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getScienceApp(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ptpSyncProjectLauncherEClass, PTPSyncProjectLauncher.class, "PTPSyncProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(localCDTProjectLauncherEClass, LocalCDTProjectLauncher.class, "LocalCDTProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(scienceAppEClass, ScienceApp.class, "ScienceApp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScienceApp_RepoURL(), ecorePackage.getEString(), "repoURL", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_Name(), ecorePackage.getEString(), "name", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_Branch(), ecorePackage.getEString(), "branch", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_LocalProjectName(), ecorePackage.getEString(), "localProjectName", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_RecursiveClone(), ecorePackage.getEBoolean(), "recursiveClone", "true", 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_AbsolutePath(), ecorePackage.getEString(), "absolutePath", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_RemotePort(), theXMLTypePackage.getInt(), "remotePort", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScienceApp_RemoteHost(), ecorePackage.getEString(), "remoteHost", null, 0, 1, ScienceApp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(environmentTypeEEnum, EnvironmentType.class, "EnvironmentType");
		addEEnumLiteral(environmentTypeEEnum, EnvironmentType.DOCKER);
		addEEnumLiteral(environmentTypeEEnum, EnvironmentType.LOCAL);

		// Create resource
		createResource(eNS_URI);
	}

} //EnvironmentPackageImpl
