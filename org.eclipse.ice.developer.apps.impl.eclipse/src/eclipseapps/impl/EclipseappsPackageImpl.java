/**
 */
package eclipseapps.impl;

import apps.AppsPackage;

import apps.docker.DockerPackage;

import eclipseapps.DockerPTPSyncProjectLauncher;
import eclipseapps.EclipseCppProjectProvider;
import eclipseapps.EclipseEnvironmentConsole;
import eclipseapps.EclipseEnvironmentStorage;
import eclipseapps.EclipseappsFactory;
import eclipseapps.EclipseappsPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EclipseappsPackageImpl extends EPackageImpl implements EclipseappsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eclipseEnvironmentStorageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dockerPTPSyncProjectLauncherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eclipseCppProjectProviderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eclipseEnvironmentConsoleEClass = null;

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
	 * @see eclipseapps.EclipseappsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EclipseappsPackageImpl() {
		super(eNS_URI, EclipseappsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EclipseappsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EclipseappsPackage init() {
		if (isInited) return (EclipseappsPackage)EPackage.Registry.INSTANCE.getEPackage(EclipseappsPackage.eNS_URI);

		// Obtain or create and register package
		EclipseappsPackageImpl theEclipseappsPackage = (EclipseappsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EclipseappsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EclipseappsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		AppsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theEclipseappsPackage.createPackageContents();

		// Initialize created meta-data
		theEclipseappsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEclipseappsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EclipseappsPackage.eNS_URI, theEclipseappsPackage);
		return theEclipseappsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEclipseEnvironmentStorage() {
		return eclipseEnvironmentStorageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDockerPTPSyncProjectLauncher() {
		return dockerPTPSyncProjectLauncherEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDockerPTPSyncProjectLauncher__LaunchProject__SourcePackage() {
		return dockerPTPSyncProjectLauncherEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEclipseCppProjectProvider() {
		return eclipseCppProjectProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEclipseEnvironmentConsole() {
		return eclipseEnvironmentConsoleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseappsFactory getEclipseappsFactory() {
		return (EclipseappsFactory)getEFactoryInstance();
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
		eclipseEnvironmentStorageEClass = createEClass(ECLIPSE_ENVIRONMENT_STORAGE);

		dockerPTPSyncProjectLauncherEClass = createEClass(DOCKER_PTP_SYNC_PROJECT_LAUNCHER);
		createEOperation(dockerPTPSyncProjectLauncherEClass, DOCKER_PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE);

		eclipseCppProjectProviderEClass = createEClass(ECLIPSE_CPP_PROJECT_PROVIDER);

		eclipseEnvironmentConsoleEClass = createEClass(ECLIPSE_ENVIRONMENT_CONSOLE);
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
		DockerPackage theDockerPackage = (DockerPackage)EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		eclipseEnvironmentStorageEClass.getESuperTypes().add(theAppsPackage.getEnvironmentStorage());
		dockerPTPSyncProjectLauncherEClass.getESuperTypes().add(theDockerPackage.getDockerProjectLauncher());
		eclipseCppProjectProviderEClass.getESuperTypes().add(theAppsPackage.getLanguageProjectProvider());
		eclipseEnvironmentConsoleEClass.getESuperTypes().add(theAppsPackage.getEnvironmentConsole());

		// Initialize classes, features, and operations; add parameters
		initEClass(eclipseEnvironmentStorageEClass, EclipseEnvironmentStorage.class, "EclipseEnvironmentStorage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dockerPTPSyncProjectLauncherEClass, DockerPTPSyncProjectLauncher.class, "DockerPTPSyncProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getDockerPTPSyncProjectLauncher__LaunchProject__SourcePackage(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theAppsPackage.getSourcePackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eclipseCppProjectProviderEClass, EclipseCppProjectProvider.class, "EclipseCppProjectProvider", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eclipseEnvironmentConsoleEClass, EclipseEnvironmentConsole.class, "EclipseEnvironmentConsole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //EclipseappsPackageImpl
