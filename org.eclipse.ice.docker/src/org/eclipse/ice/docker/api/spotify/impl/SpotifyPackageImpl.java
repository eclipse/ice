/**
 */
package org.eclipse.ice.docker.api.spotify.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.ice.docker.api.DockerapiPackage;

import org.eclipse.ice.docker.api.impl.DockerapiPackageImpl;

import org.eclipse.ice.docker.api.spotify.SpotifyDockerClient;
import org.eclipse.ice.docker.api.spotify.SpotifyFactory;
import org.eclipse.ice.docker.api.spotify.SpotifyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotifyPackageImpl extends EPackageImpl implements SpotifyPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotifyDockerClientEClass = null;

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
	 * @see org.eclipse.ice.docker.api.spotify.SpotifyPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SpotifyPackageImpl() {
		super(eNS_URI, SpotifyFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link SpotifyPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SpotifyPackage init() {
		if (isInited) return (SpotifyPackage)EPackage.Registry.INSTANCE.getEPackage(SpotifyPackage.eNS_URI);

		// Obtain or create and register package
		SpotifyPackageImpl theSpotifyPackage = (SpotifyPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SpotifyPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SpotifyPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		DockerapiPackageImpl theDockerapiPackage = (DockerapiPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DockerapiPackage.eNS_URI) instanceof DockerapiPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DockerapiPackage.eNS_URI) : DockerapiPackage.eINSTANCE);

		// Create package meta-data objects
		theSpotifyPackage.createPackageContents();
		theDockerapiPackage.createPackageContents();

		// Initialize created meta-data
		theSpotifyPackage.initializePackageContents();
		theDockerapiPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSpotifyPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SpotifyPackage.eNS_URI, theSpotifyPackage);
		return theSpotifyPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotifyDockerClient() {
		return spotifyDockerClientEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotifyFactory getSpotifyFactory() {
		return (SpotifyFactory)getEFactoryInstance();
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
		spotifyDockerClientEClass = createEClass(SPOTIFY_DOCKER_CLIENT);
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
		DockerapiPackage theDockerapiPackage = (DockerapiPackage)EPackage.Registry.INSTANCE.getEPackage(DockerapiPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		spotifyDockerClientEClass.getESuperTypes().add(theDockerapiPackage.getDockerAPI());

		// Initialize classes, features, and operations; add parameters
		initEClass(spotifyDockerClientEClass, SpotifyDockerClient.class, "SpotifyDockerClient", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //SpotifyPackageImpl
