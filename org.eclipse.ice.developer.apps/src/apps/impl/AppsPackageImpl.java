/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.IEnvironment;
import apps.OSPackage;
import apps.PackageType;
import apps.ProjectLauncher;
import apps.SourcePackage;
import apps.SpackDependency;
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
	private EClass projectLauncherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass packageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sourcePackageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass osPackageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spackDependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum packageTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum environmentStateEEnum = null;

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
	public EAttribute getIEnvironment_Name() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_Os() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIEnvironment_DependentPackages() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIEnvironment_PrimaryApp() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIEnvironment_Projectlauncher() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnvironment_State() {
		return (EAttribute)iEnvironmentEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIEnvironment__Build() {
		return iEnvironmentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIEnvironment__Connect() {
		return iEnvironmentEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIEnvironment__Delete() {
		return iEnvironmentEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIEnvironment__Stop() {
		return iEnvironmentEClass.getEOperations().get(3);
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
	public EOperation getEnvironmentManager__LoadEnvironment__String() {
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
	public EOperation getEnvironmentManager__PersistToXMIString__String() {
		return environmentManagerEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__PersistXMIToFile__String_String() {
		return environmentManagerEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__ConnectToExistingEnvironment__String() {
		return environmentManagerEClass.getEOperations().get(6);
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
	public EAttribute getSpackPackage_Compiler() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackPackage_CppFlags() {
		return (EAttribute)spackPackageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpackPackage_Dependencies() {
		return (EReference)spackPackageEClass.getEStructuralFeatures().get(2);
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
	public EOperation getProjectLauncher__LaunchProject__SourcePackage() {
		return projectLauncherEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPackage() {
		return packageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPackage_Name() {
		return (EAttribute)packageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPackage_Version() {
		return (EAttribute)packageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPackage_Type() {
		return (EAttribute)packageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSourcePackage() {
		return sourcePackageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSourcePackage_RepoURL() {
		return (EAttribute)sourcePackageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSourcePackage_Branch() {
		return (EAttribute)sourcePackageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSourcePackage_BuildCommand() {
		return (EAttribute)sourcePackageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOSPackage() {
		return osPackageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpackDependency() {
		return spackDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackDependency_Dependency() {
		return (EAttribute)spackDependencyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpackDependency_Provider() {
		return (EAttribute)spackDependencyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPackageType() {
		return packageTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEnvironmentState() {
		return environmentStateEEnum;
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
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__NAME);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__OS);
		createEReference(iEnvironmentEClass, IENVIRONMENT__DEPENDENT_PACKAGES);
		createEReference(iEnvironmentEClass, IENVIRONMENT__PRIMARY_APP);
		createEReference(iEnvironmentEClass, IENVIRONMENT__PROJECTLAUNCHER);
		createEAttribute(iEnvironmentEClass, IENVIRONMENT__STATE);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___BUILD);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___CONNECT);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___DELETE);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___STOP);

		environmentManagerEClass = createEClass(ENVIRONMENT_MANAGER);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__STRING_STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___CONNECT_TO_EXISTING_ENVIRONMENT__STRING);

		spackPackageEClass = createEClass(SPACK_PACKAGE);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__COMPILER);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__CPP_FLAGS);
		createEReference(spackPackageEClass, SPACK_PACKAGE__DEPENDENCIES);

		projectLauncherEClass = createEClass(PROJECT_LAUNCHER);
		createEOperation(projectLauncherEClass, PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE);

		packageEClass = createEClass(PACKAGE);
		createEAttribute(packageEClass, PACKAGE__NAME);
		createEAttribute(packageEClass, PACKAGE__VERSION);
		createEAttribute(packageEClass, PACKAGE__TYPE);

		sourcePackageEClass = createEClass(SOURCE_PACKAGE);
		createEAttribute(sourcePackageEClass, SOURCE_PACKAGE__REPO_URL);
		createEAttribute(sourcePackageEClass, SOURCE_PACKAGE__BRANCH);
		createEAttribute(sourcePackageEClass, SOURCE_PACKAGE__BUILD_COMMAND);

		osPackageEClass = createEClass(OS_PACKAGE);

		spackDependencyEClass = createEClass(SPACK_DEPENDENCY);
		createEAttribute(spackDependencyEClass, SPACK_DEPENDENCY__DEPENDENCY);
		createEAttribute(spackDependencyEClass, SPACK_DEPENDENCY__PROVIDER);

		// Create enums
		packageTypeEEnum = createEEnum(PACKAGE_TYPE);
		environmentStateEEnum = createEEnum(ENVIRONMENT_STATE);
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
		spackPackageEClass.getESuperTypes().add(this.getPackage());
		sourcePackageEClass.getESuperTypes().add(this.getPackage());
		osPackageEClass.getESuperTypes().add(this.getPackage());

		// Initialize classes, features, and operations; add parameters
		initEClass(iEnvironmentEClass, IEnvironment.class, "IEnvironment", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIEnvironment_Name(), ecorePackage.getEString(), "name", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_Os(), ecorePackage.getEString(), "os", "fedora", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_DependentPackages(), this.getPackage(), null, "dependentPackages", null, 0, -1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_PrimaryApp(), this.getPackage(), null, "primaryApp", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_Projectlauncher(), this.getProjectLauncher(), null, "projectlauncher", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_State(), this.getEnvironmentState(), "state", "NotCreated", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getIEnvironment__Build(), theXMLTypePackage.getBoolean(), "build", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getIEnvironment__Connect(), theXMLTypePackage.getBoolean(), "connect", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getIEnvironment__Delete(), theXMLTypePackage.getBoolean(), "delete", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getIEnvironment__Stop(), theXMLTypePackage.getBoolean(), "stop", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(environmentManagerEClass, EnvironmentManager.class, "EnvironmentManager", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getEnvironmentManager__CreateEnvironment__String(), this.getIEnvironment(), "createEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "dataString", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__ListExistingEnvironments(), ecorePackage.getEString(), "listExistingEnvironments", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadEnvironment__String(), this.getIEnvironment(), "loadEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadEnvironmentFromFile__String(), this.getIEnvironment(), "loadEnvironmentFromFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "fileName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__PersistToXMIString__String(), ecorePackage.getEString(), "persistToXMIString", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__PersistXMIToFile__String_String(), null, "persistXMIToFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "fileName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__ConnectToExistingEnvironment__String(), null, "connectToExistingEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spackPackageEClass, SpackPackage.class, "SpackPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackPackage_Compiler(), ecorePackage.getEString(), "compiler", "gcc@6.3.1", 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_CppFlags(), ecorePackage.getEString(), "cppFlags", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpackPackage_Dependencies(), this.getSpackDependency(), null, "dependencies", null, 0, -1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(projectLauncherEClass, ProjectLauncher.class, "ProjectLauncher", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getProjectLauncher__LaunchProject__SourcePackage(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getSourcePackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(packageEClass, apps.Package.class, "Package", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPackage_Name(), ecorePackage.getEString(), "name", null, 0, 1, apps.Package.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackage_Version(), ecorePackage.getEString(), "version", null, 0, 1, apps.Package.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackage_Type(), this.getPackageType(), "type", null, 0, 1, apps.Package.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sourcePackageEClass, SourcePackage.class, "SourcePackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSourcePackage_RepoURL(), ecorePackage.getEString(), "repoURL", null, 0, 1, SourcePackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourcePackage_Branch(), ecorePackage.getEString(), "branch", "master", 0, 1, SourcePackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourcePackage_BuildCommand(), ecorePackage.getEString(), "buildCommand", null, 0, 1, SourcePackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(osPackageEClass, OSPackage.class, "OSPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(spackDependencyEClass, SpackDependency.class, "SpackDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackDependency_Dependency(), ecorePackage.getEString(), "dependency", null, 0, 1, SpackDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackDependency_Provider(), ecorePackage.getEString(), "provider", null, 0, 1, SpackDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(packageTypeEEnum, PackageType.class, "PackageType");
		addEEnumLiteral(packageTypeEEnum, PackageType.OS);
		addEEnumLiteral(packageTypeEEnum, PackageType.SPACK);
		addEEnumLiteral(packageTypeEEnum, PackageType.SOURCE);

		initEEnum(environmentStateEEnum, EnvironmentState.class, "EnvironmentState");
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.STOPPED);
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.RUNNING);
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.NOT_CREATED);

		// Create resource
		createResource(eNS_URI);
	}

} //AppsPackageImpl
