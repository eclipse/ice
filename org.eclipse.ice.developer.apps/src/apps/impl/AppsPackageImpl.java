/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.Environment;
import apps.EnvironmentManager;
import apps.EnvironmentType;
import apps.IEnvironment;
import apps.LocalCDTProjectLauncher;
import apps.PTPSyncProjectLauncher;
import apps.ProjectLauncher;
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
public class AppsPackageImpl extends EPackageImpl implements AppsPackage {
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
	 * @see apps.AppsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AppsPackageImpl() {
		super(eNS_URI, AppsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link AppsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AppsPackage init() {
		if (isInited) return (AppsPackage)EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI);

		// Obtain or create and register package
		AppsPackageImpl theAppsPackage = (AppsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AppsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AppsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		DockerPackageImpl theDockerPackage = (DockerPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) instanceof DockerPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) : DockerPackage.eINSTANCE);
		LocalPackageImpl theLocalPackage = (LocalPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) instanceof LocalPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) : LocalPackage.eINSTANCE);

		// Create package meta-data objects
		theAppsPackage.createPackageContents();
		theDockerPackage.createPackageContents();
		theLocalPackage.createPackageContents();

		// Initialize created meta-data
		theAppsPackage.initializePackageContents();
		theDockerPackage.initializePackageContents();
		theLocalPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAppsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AppsPackage.eNS_URI, theAppsPackage);
		return theAppsPackage;
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
	public EReference getIEnvironment_DependentPackages() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_DevelopmentEnvironment() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_GenerateProject() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIEnvironment_PrimaryApp() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(6);
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
	public EClass getEnvironmentManager() {
		return environmentManagerEClass;
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
	public EOperation getEnvironmentManager__ListExistingEnvironments() {
		return environmentManagerEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__LoadExistingEnvironment__String() {
		return environmentManagerEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__LoadEnvironmentFromFile__String() {
		return environmentManagerEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__PersistToXMIString__IEnvironment() {
		return environmentManagerEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__PersistXMIToFile__IEnvironment_String() {
		return environmentManagerEClass.getEOperations().get(5);
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
	public EAttribute getSpackPackage_Compiler() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Version() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_CppFlags() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_VirtualDependency() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_VirtualDependencyProvider() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_RepoURL() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_Branch() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(7);
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
	public EOperation getEnvironment__Launch() {
		return environmentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironment__LaunchDerived() {
		return environmentEClass.getEOperations().get(1);
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
	public EOperation getProjectLauncher__LaunchProject__SpackPackage() {
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
	public EOperation getPTPSyncProjectLauncher__LaunchProject__SpackPackage() {
		return ptpSyncProjectLauncherEClass.getEOperations().get(0);
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
	public EOperation getLocalCDTProjectLauncher__LaunchProject__SpackPackage() {
		return localCDTProjectLauncherEClass.getEOperations().get(0);
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
	public AppsFactory getAppsFactory() {
		return (AppsFactory)getEFactoryInstance();
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
		createEReference(iEnvironmentEClass, IENVIRONMENT__DEPENDENT_PACKAGES);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__DEVELOPMENT_ENVIRONMENT);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__GENERATE_PROJECT);
		createEReference(iEnvironmentEClass, IENVIRONMENT__PRIMARY_APP);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___LAUNCH);

		environmentManagerEClass = createEClass(ENVIRONMENT_MANAGER);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_EXISTING_ENVIRONMENT__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__IENVIRONMENT);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__IENVIRONMENT_STRING);

		spackPackageEClass = createEClass(SPACK_PACKAGE);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__NAME);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__COMPILER);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VERSION);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__CPP_FLAGS);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VIRTUAL_DEPENDENCY);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__REPO_URL);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__BRANCH);

		environmentEClass = createEClass(ENVIRONMENT);
		createEReference(environmentEClass, ENVIRONMENT__PROJECTLAUNCHER);
		createEOperation(environmentEClass, ENVIRONMENT___LAUNCH);
		createEOperation(environmentEClass, ENVIRONMENT___LAUNCH_DERIVED);

		projectLauncherEClass = createEClass(PROJECT_LAUNCHER);
		createEOperation(projectLauncherEClass, PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE);

		ptpSyncProjectLauncherEClass = createEClass(PTP_SYNC_PROJECT_LAUNCHER);
		createEOperation(ptpSyncProjectLauncherEClass, PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE);

		localCDTProjectLauncherEClass = createEClass(LOCAL_CDT_PROJECT_LAUNCHER);
		createEOperation(localCDTProjectLauncherEClass, LOCAL_CDT_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE);

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
		initEAttribute(getIEnvironment_Os(), ecorePackage.getEString(), "os", "fedora", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_DependentPackages(), this.getSpackPackage(), null, "dependentPackages", null, 0, -1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_DevelopmentEnvironment(), theXMLTypePackage.getBoolean(), "developmentEnvironment", "true", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_GenerateProject(), theXMLTypePackage.getBoolean(), "generateProject", "false", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_PrimaryApp(), this.getSpackPackage(), null, "primaryApp", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getIEnvironment__Launch(), theXMLTypePackage.getBoolean(), "launch", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(environmentManagerEClass, EnvironmentManager.class, "EnvironmentManager", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getEnvironmentManager__CreateEnvironment__String(), this.getIEnvironment(), "createEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__ListExistingEnvironments(), ecorePackage.getEString(), "listExistingEnvironments", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadExistingEnvironment__String(), this.getIEnvironment(), "loadExistingEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadEnvironmentFromFile__String(), this.getIEnvironment(), "loadEnvironmentFromFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "file", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__PersistToXMIString__IEnvironment(), ecorePackage.getEString(), "persistToXMIString", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getIEnvironment(), "environment", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__PersistXMIToFile__IEnvironment_String(), null, "persistXMIToFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getIEnvironment(), "environment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "fileName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spackPackageEClass, SpackPackage.class, "SpackPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackPackage_Name(), ecorePackage.getEString(), "name", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_Compiler(), ecorePackage.getEString(), "compiler", "gcc@6.3.1", 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_Version(), ecorePackage.getEString(), "version", "latest", 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_CppFlags(), ecorePackage.getEString(), "cppFlags", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_VirtualDependency(), ecorePackage.getEString(), "virtualDependency", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_VirtualDependencyProvider(), ecorePackage.getEString(), "virtualDependencyProvider", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_RepoURL(), ecorePackage.getEString(), "repoURL", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_Branch(), ecorePackage.getEString(), "branch", "master", 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(environmentEClass, Environment.class, "Environment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnvironment_Projectlauncher(), this.getProjectLauncher(), null, "projectlauncher", null, 0, 1, Environment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getEnvironment__Launch(), theXMLTypePackage.getBoolean(), "launch", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironment__LaunchDerived(), theXMLTypePackage.getBoolean(), "launchDerived", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(projectLauncherEClass, ProjectLauncher.class, "ProjectLauncher", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getProjectLauncher__LaunchProject__SpackPackage(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getSpackPackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ptpSyncProjectLauncherEClass, PTPSyncProjectLauncher.class, "PTPSyncProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getPTPSyncProjectLauncher__LaunchProject__SpackPackage(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getSpackPackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(localCDTProjectLauncherEClass, LocalCDTProjectLauncher.class, "LocalCDTProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getLocalCDTProjectLauncher__LaunchProject__SpackPackage(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getSpackPackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(environmentTypeEEnum, EnvironmentType.class, "EnvironmentType");
		addEEnumLiteral(environmentTypeEEnum, EnvironmentType.DOCKER);
		addEEnumLiteral(environmentTypeEEnum, EnvironmentType.LOCAL);

		// Create resource
		createResource(eNS_URI);
	}

} //AppsPackageImpl
