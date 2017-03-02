/**
 */
package apps.eclipse.impl;

import apps.AppsPackage;

import apps.docker.DockerPackage;

import apps.docker.impl.DockerPackageImpl;

import apps.eclipse.DockerPTPSyncProjectLauncher;
import apps.eclipse.EclipseCppProjectProvider;
import apps.eclipse.EclipseEnvironmentStorage;
import apps.eclipse.EclipseFactory;
import apps.eclipse.EclipsePackage;
import apps.impl.AppsPackageImpl;

import apps.local.LocalPackage;

import apps.local.impl.LocalPackageImpl;

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
public class EclipsePackageImpl extends EPackageImpl implements EclipsePackage {
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
	 * @see apps.eclipse.EclipsePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EclipsePackageImpl() {
		super(eNS_URI, EclipseFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EclipsePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EclipsePackage init() {
		if (isInited) return (EclipsePackage)EPackage.Registry.INSTANCE.getEPackage(EclipsePackage.eNS_URI);

		// Obtain or create and register package
		EclipsePackageImpl theEclipsePackage = (EclipsePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EclipsePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EclipsePackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		AppsPackageImpl theAppsPackage = (AppsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI) instanceof AppsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI) : AppsPackage.eINSTANCE);
		DockerPackageImpl theDockerPackage = (DockerPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) instanceof DockerPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) : DockerPackage.eINSTANCE);
		LocalPackageImpl theLocalPackage = (LocalPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) instanceof LocalPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI) : LocalPackage.eINSTANCE);

		// Create package meta-data objects
		theEclipsePackage.createPackageContents();
		theAppsPackage.createPackageContents();
		theDockerPackage.createPackageContents();
		theLocalPackage.createPackageContents();

		// Initialize created meta-data
		theEclipsePackage.initializePackageContents();
		theAppsPackage.initializePackageContents();
		theDockerPackage.initializePackageContents();
		theLocalPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEclipsePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EclipsePackage.eNS_URI, theEclipsePackage);
		return theEclipsePackage;
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
	public EclipseFactory getEclipseFactory() {
		return (EclipseFactory)getEFactoryInstance();
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

		// Initialize classes, features, and operations; add parameters
		initEClass(eclipseEnvironmentStorageEClass, EclipseEnvironmentStorage.class, "EclipseEnvironmentStorage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dockerPTPSyncProjectLauncherEClass, DockerPTPSyncProjectLauncher.class, "DockerPTPSyncProjectLauncher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getDockerPTPSyncProjectLauncher__LaunchProject__SourcePackage(), null, "launchProject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theAppsPackage.getSourcePackage(), "project", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eclipseCppProjectProviderEClass, EclipseCppProjectProvider.class, "EclipseCppProjectProvider", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //EclipsePackageImpl
