/**
 */
package apps.eclipse.impl;

import apps.eclipse.*;

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
public class EclipseFactoryImpl extends EFactoryImpl implements EclipseFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EclipseFactory init() {
		try {
			EclipseFactory theEclipseFactory = (EclipseFactory)EPackage.Registry.INSTANCE.getEFactory(EclipsePackage.eNS_URI);
			if (theEclipseFactory != null) {
				return theEclipseFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EclipseFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EclipseFactoryImpl() {
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
			case EclipsePackage.ECLIPSE_ENVIRONMENT_STORAGE: return createEclipseEnvironmentStorage();
			case EclipsePackage.DOCKER_PTP_SYNC_PROJECT_LAUNCHER: return createDockerPTPSyncProjectLauncher();
			case EclipsePackage.ECLIPSE_CPP_PROJECT_PROVIDER: return createEclipseCppProjectProvider();
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
	public EclipsePackage getEclipsePackage() {
		return (EclipsePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EclipsePackage getPackage() {
		return EclipsePackage.eINSTANCE;
	}

} //EclipseFactoryImpl
