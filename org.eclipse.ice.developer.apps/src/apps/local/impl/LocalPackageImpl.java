/**
 */
package apps.local.impl;

import apps.AppsPackage;
import apps.docker.DockerPackage;

import apps.docker.impl.DockerPackageImpl;

import apps.impl.AppsPackageImpl;
import apps.local.LocalEnvironment;
import apps.local.LocalFactory;
import apps.local.LocalPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LocalPackageImpl extends EPackageImpl implements LocalPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass localEnvironmentEClass = null;

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
	 * @see apps.local.LocalPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private LocalPackageImpl() {
		super(eNS_URI, LocalFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link LocalPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static LocalPackage init() {
		if (isInited) return (LocalPackage)EPackage.Registry.INSTANCE.getEPackage(LocalPackage.eNS_URI);

		// Obtain or create and register package
		LocalPackageImpl theLocalPackage = (LocalPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LocalPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LocalPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		AppsPackageImpl theAppsPackage = (AppsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI) instanceof AppsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AppsPackage.eNS_URI) : AppsPackage.eINSTANCE);
		DockerPackageImpl theDockerPackage = (DockerPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) instanceof DockerPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DockerPackage.eNS_URI) : DockerPackage.eINSTANCE);

		// Create package meta-data objects
		theLocalPackage.createPackageContents();
		theAppsPackage.createPackageContents();
		theDockerPackage.createPackageContents();

		// Initialize created meta-data
		theLocalPackage.initializePackageContents();
		theAppsPackage.initializePackageContents();
		theDockerPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theLocalPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(LocalPackage.eNS_URI, theLocalPackage);
		return theLocalPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocalEnvironment() {
		return localEnvironmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalFactory getLocalFactory() {
		return (LocalFactory)getEFactoryInstance();
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
		localEnvironmentEClass = createEClass(LOCAL_ENVIRONMENT);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		localEnvironmentEClass.getESuperTypes().add(theAppsPackage.getIEnvironment());

		// Initialize classes, features, and operations; add parameters
		initEClass(localEnvironmentEClass, LocalEnvironment.class, "LocalEnvironment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //LocalPackageImpl
