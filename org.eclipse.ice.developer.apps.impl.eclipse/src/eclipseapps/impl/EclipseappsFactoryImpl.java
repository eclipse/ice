/**
 */
package eclipseapps.impl;

import eclipseapps.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EclipseappsFactoryImpl extends EFactoryImpl implements EclipseappsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EclipseappsFactory init() {
		try {
			EclipseappsFactory theEclipseappsFactory = (EclipseappsFactory)EPackage.Registry.INSTANCE.getEFactory(EclipseappsPackage.eNS_URI);
			if (theEclipseappsFactory != null) {
				return theEclipseappsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EclipseappsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseappsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case EclipseappsPackage.ECLIPSE_ENVIRONMENT_STORAGE: return createEclipseEnvironmentStorage();
			case EclipseappsPackage.DOCKER_PTP_SYNC_PROJECT_LAUNCHER: return createDockerPTPSyncProjectLauncher();
			case EclipseappsPackage.ECLIPSE_CPP_PROJECT_PROVIDER: return createEclipseCppProjectProvider();
			case EclipseappsPackage.ECLIPSE_ENVIRONMENT_CONSOLE: return createEclipseEnvironmentConsole();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseEnvironmentStorage createEclipseEnvironmentStorage() {
		EclipseEnvironmentStorageImpl eclipseEnvironmentStorage = new EclipseEnvironmentStorageImpl();
		return eclipseEnvironmentStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerPTPSyncProjectLauncher createDockerPTPSyncProjectLauncher() {
		DockerPTPSyncProjectLauncherImpl dockerPTPSyncProjectLauncher = new DockerPTPSyncProjectLauncherImpl();
		return dockerPTPSyncProjectLauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseCppProjectProvider createEclipseCppProjectProvider() {
		EclipseCppProjectProviderImpl eclipseCppProjectProvider = new EclipseCppProjectProviderImpl();
		return eclipseCppProjectProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseEnvironmentConsole createEclipseEnvironmentConsole() {
		EclipseEnvironmentConsoleImpl eclipseEnvironmentConsole = new EclipseEnvironmentConsoleImpl();
		return eclipseEnvironmentConsole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseappsPackage getEclipseappsPackage() {
		return (EclipseappsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EclipseappsPackage getPackage() {
		return EclipseappsPackage.eINSTANCE;
	}

} //EclipseappsFactoryImpl
