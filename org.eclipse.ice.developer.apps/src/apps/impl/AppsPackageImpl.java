/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.EnvironmentConsole;
import apps.EnvironmentCreator;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.EnvironmentStorage;
import apps.IEnvironment;
import apps.JsonEnvironmentCreator;
import apps.LanguageProjectProvider;
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
	private EClass environmentCreatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jsonEnvironmentCreatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass environmentStorageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass languageProjectProviderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass environmentConsoleEClass = null;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType environmentListEDataType = null;

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
	public EReference getIEnvironment_Console() {
		return (EReference)iEnvironmentEClass.getEStructuralFeatures().get(6);
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
	public EReference getEnvironmentManager_EnvironmentCreator() {
		return (EReference)environmentManagerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnvironmentManager_EnvironmentStorage() {
		return (EReference)environmentManagerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnvironmentManager_Console() {
		return (EReference)environmentManagerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__Create__String() {
		return environmentManagerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__List() {
		return environmentManagerEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__Get__String() {
		return environmentManagerEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__LoadFromFile__String() {
		return environmentManagerEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__PersistToString__String() {
		return environmentManagerEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__PersistToFile__String_String() {
		return environmentManagerEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__ListAvailableSpackPackages() {
		return environmentManagerEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__PersistEnvironments() {
		return environmentManagerEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__CreateEmpty__String() {
		return environmentManagerEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__LoadFromXMI__String() {
		return environmentManagerEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__LoadEnvironments() {
		return environmentManagerEClass.getEOperations().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__StartAllStoppedEnvironments() {
		return environmentManagerEClass.getEOperations().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__StopRunningEnvironments() {
		return environmentManagerEClass.getEOperations().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentManager__DeleteEnvironment__String() {
		return environmentManagerEClass.getEOperations().get(13);
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
	public EReference getProjectLauncher_Languageprojectprovider() {
		return (EReference)projectLauncherEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProjectLauncher_Environmentconsole() {
		return (EReference)projectLauncherEClass.getEStructuralFeatures().get(1);
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
	public EClass getEnvironmentCreator() {
		return environmentCreatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentCreator__Create__String() {
		return environmentCreatorEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJsonEnvironmentCreator() {
		return jsonEnvironmentCreatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvironmentStorage() {
		return environmentStorageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentStorage__Store__EList() {
		return environmentStorageEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentStorage__Load() {
		return environmentStorageEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLanguageProjectProvider() {
		return languageProjectProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getLanguageProjectProvider__CreateProject__String() {
		return languageProjectProviderEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getLanguageProjectProvider__Configure() {
		return languageProjectProviderEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvironmentConsole() {
		return environmentConsoleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEnvironmentConsole__Print__String() {
		return environmentConsoleEClass.getEOperations().get(0);
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
	public EDataType getEnvironmentList() {
		return environmentListEDataType;
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
		createEReference(iEnvironmentEClass, IENVIRONMENT__CONSOLE);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___BUILD);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___CONNECT);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___DELETE);
		createEOperation(iEnvironmentEClass, IENVIRONMENT___STOP);

		environmentManagerEClass = createEClass(ENVIRONMENT_MANAGER);
		createEReference(environmentManagerEClass, ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR);
		createEReference(environmentManagerEClass, ENVIRONMENT_MANAGER__ENVIRONMENT_STORAGE);
		createEReference(environmentManagerEClass, ENVIRONMENT_MANAGER__CONSOLE);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___CREATE__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LIST);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___GET__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_FROM_FILE__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_TO_STRING__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_TO_FILE__STRING_STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LIST_AVAILABLE_SPACK_PACKAGES);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___PERSIST_ENVIRONMENTS);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___CREATE_EMPTY__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_FROM_XMI__STRING);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___LOAD_ENVIRONMENTS);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___START_ALL_STOPPED_ENVIRONMENTS);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___STOP_RUNNING_ENVIRONMENTS);
		createEOperation(environmentManagerEClass, ENVIRONMENT_MANAGER___DELETE_ENVIRONMENT__STRING);

		spackPackageEClass = createEClass(SPACK_PACKAGE);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__COMPILER);
		createEAttribute(spackPackageEClass, SPACK_PACKAGE__CPP_FLAGS);
		createEReference(spackPackageEClass, SPACK_PACKAGE__DEPENDENCIES);

		projectLauncherEClass = createEClass(PROJECT_LAUNCHER);
		createEReference(projectLauncherEClass, PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER);
		createEReference(projectLauncherEClass, PROJECT_LAUNCHER__ENVIRONMENTCONSOLE);
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

		environmentCreatorEClass = createEClass(ENVIRONMENT_CREATOR);
		createEOperation(environmentCreatorEClass, ENVIRONMENT_CREATOR___CREATE__STRING);

		jsonEnvironmentCreatorEClass = createEClass(JSON_ENVIRONMENT_CREATOR);

		environmentStorageEClass = createEClass(ENVIRONMENT_STORAGE);
		createEOperation(environmentStorageEClass, ENVIRONMENT_STORAGE___STORE__ELIST);
		createEOperation(environmentStorageEClass, ENVIRONMENT_STORAGE___LOAD);

		languageProjectProviderEClass = createEClass(LANGUAGE_PROJECT_PROVIDER);
		createEOperation(languageProjectProviderEClass, LANGUAGE_PROJECT_PROVIDER___CREATE_PROJECT__STRING);
		createEOperation(languageProjectProviderEClass, LANGUAGE_PROJECT_PROVIDER___CONFIGURE);

		environmentConsoleEClass = createEClass(ENVIRONMENT_CONSOLE);
		createEOperation(environmentConsoleEClass, ENVIRONMENT_CONSOLE___PRINT__STRING);

		// Create enums
		packageTypeEEnum = createEEnum(PACKAGE_TYPE);
		environmentStateEEnum = createEEnum(ENVIRONMENT_STATE);

		// Create data types
		environmentListEDataType = createEDataType(ENVIRONMENT_LIST);
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
		jsonEnvironmentCreatorEClass.getESuperTypes().add(this.getEnvironmentCreator());

		// Initialize classes, features, and operations; add parameters
		initEClass(iEnvironmentEClass, IEnvironment.class, "IEnvironment", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIEnvironment_Name(), ecorePackage.getEString(), "name", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_Os(), ecorePackage.getEString(), "os", "fedora", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_DependentPackages(), this.getPackage(), null, "dependentPackages", null, 0, -1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_PrimaryApp(), this.getPackage(), null, "primaryApp", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_Projectlauncher(), this.getProjectLauncher(), null, "projectlauncher", null, 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEnvironment_State(), this.getEnvironmentState(), "state", "NotCreated", 0, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIEnvironment_Console(), this.getEnvironmentConsole(), null, "console", null, 1, 1, IEnvironment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getIEnvironment__Build(), theXMLTypePackage.getBoolean(), "build", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getIEnvironment__Connect(), theXMLTypePackage.getBoolean(), "connect", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getIEnvironment__Delete(), theXMLTypePackage.getBoolean(), "delete", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getIEnvironment__Stop(), theXMLTypePackage.getBoolean(), "stop", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(environmentManagerEClass, EnvironmentManager.class, "EnvironmentManager", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnvironmentManager_EnvironmentCreator(), this.getEnvironmentCreator(), null, "environmentCreator", null, 0, 1, EnvironmentManager.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnvironmentManager_EnvironmentStorage(), this.getEnvironmentStorage(), null, "environmentStorage", null, 1, 1, EnvironmentManager.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnvironmentManager_Console(), this.getEnvironmentConsole(), null, "console", null, 1, 1, EnvironmentManager.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getEnvironmentManager__Create__String(), this.getIEnvironment(), "create", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "dataString", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__List(), ecorePackage.getEString(), "list", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__Get__String(), this.getIEnvironment(), "get", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadFromFile__String(), this.getIEnvironment(), "loadFromFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "fileName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__PersistToString__String(), ecorePackage.getEString(), "persistToString", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__PersistToFile__String_String(), null, "persistToFile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "environmentName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "fileName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__ListAvailableSpackPackages(), ecorePackage.getEString(), "listAvailableSpackPackages", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__PersistEnvironments(), null, "persistEnvironments", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__CreateEmpty__String(), this.getIEnvironment(), "createEmpty", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__LoadFromXMI__String(), this.getIEnvironment(), "loadFromXMI", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "xmiStr", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__LoadEnvironments(), null, "loadEnvironments", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__StartAllStoppedEnvironments(), null, "startAllStoppedEnvironments", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentManager__StopRunningEnvironments(), null, "stopRunningEnvironments", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getEnvironmentManager__DeleteEnvironment__String(), null, "deleteEnvironment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spackPackageEClass, SpackPackage.class, "SpackPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackPackage_Compiler(), ecorePackage.getEString(), "compiler", "gcc@6.3.1", 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackPackage_CppFlags(), ecorePackage.getEString(), "cppFlags", null, 0, 1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpackPackage_Dependencies(), this.getSpackDependency(), null, "dependencies", null, 0, -1, SpackPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(projectLauncherEClass, ProjectLauncher.class, "ProjectLauncher", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProjectLauncher_Languageprojectprovider(), this.getLanguageProjectProvider(), null, "languageprojectprovider", null, 0, 1, ProjectLauncher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProjectLauncher_Environmentconsole(), this.getEnvironmentConsole(), null, "environmentconsole", null, 1, 1, ProjectLauncher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getProjectLauncher__LaunchProject__SourcePackage(), theXMLTypePackage.getBoolean(), "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getSourcePackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(packageEClass, apps.Package.class, "Package", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPackage_Name(), ecorePackage.getEString(), "name", null, 0, 1, apps.Package.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackage_Version(), ecorePackage.getEString(), "version", "latest", 0, 1, apps.Package.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackage_Type(), this.getPackageType(), "type", null, 0, 1, apps.Package.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sourcePackageEClass, SourcePackage.class, "SourcePackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSourcePackage_RepoURL(), ecorePackage.getEString(), "repoURL", null, 0, 1, SourcePackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourcePackage_Branch(), ecorePackage.getEString(), "branch", "master", 0, 1, SourcePackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourcePackage_BuildCommand(), ecorePackage.getEString(), "buildCommand", null, 0, 1, SourcePackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(osPackageEClass, OSPackage.class, "OSPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(spackDependencyEClass, SpackDependency.class, "SpackDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpackDependency_Dependency(), ecorePackage.getEString(), "dependency", null, 0, 1, SpackDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpackDependency_Provider(), ecorePackage.getEString(), "provider", null, 0, 1, SpackDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(environmentCreatorEClass, EnvironmentCreator.class, "EnvironmentCreator", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getEnvironmentCreator__Create__String(), this.getIEnvironment(), "create", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "dataString", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(jsonEnvironmentCreatorEClass, JsonEnvironmentCreator.class, "JsonEnvironmentCreator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(environmentStorageEClass, EnvironmentStorage.class, "EnvironmentStorage", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getEnvironmentStorage__Store__EList(), null, "store", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getEnvironmentList(), "environments", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEnvironmentStorage__Load(), this.getIEnvironment(), "load", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(languageProjectProviderEClass, LanguageProjectProvider.class, "LanguageProjectProvider", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getLanguageProjectProvider__CreateProject__String(), null, "createProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "projectName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getLanguageProjectProvider__Configure(), null, "configure", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(environmentConsoleEClass, EnvironmentConsole.class, "EnvironmentConsole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getEnvironmentConsole__Print__String(), null, "print", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "message", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(packageTypeEEnum, PackageType.class, "PackageType");
		addEEnumLiteral(packageTypeEEnum, PackageType.OS);
		addEEnumLiteral(packageTypeEEnum, PackageType.SPACK);
		addEEnumLiteral(packageTypeEEnum, PackageType.SOURCE);

		initEEnum(environmentStateEEnum, EnvironmentState.class, "EnvironmentState");
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.STOPPED);
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.RUNNING);
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.NOT_CREATED);
		addEEnumLiteral(environmentStateEEnum, EnvironmentState.CREATED);

		// Initialize data types
		initEDataType(environmentListEDataType, EList.class, "EnvironmentList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "org.eclipse.emf.common.util.EList<IEnvironment>");

		// Create resource
		createResource(eNS_URI);
	}

} //AppsPackageImpl
