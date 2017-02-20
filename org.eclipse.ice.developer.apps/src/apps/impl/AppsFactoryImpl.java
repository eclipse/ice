/**
 */
package apps.impl;

import apps.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class AppsFactoryImpl extends EFactoryImpl implements AppsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AppsFactory init() {
		try {
			AppsFactory theAppsFactory = (AppsFactory)EPackage.Registry.INSTANCE.getEFactory(AppsPackage.eNS_URI);
			if (theAppsFactory != null) {
				return theAppsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AppsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppsFactoryImpl() {
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
			case AppsPackage.ENVIRONMENT_MANAGER: return createEnvironmentManager();
			case AppsPackage.SPACK_PACKAGE: return createSpackPackage();
			case AppsPackage.ENVIRONMENT: return createEnvironment();
			case AppsPackage.PTP_SYNC_PROJECT_LAUNCHER: return createPTPSyncProjectLauncher();
			case AppsPackage.LOCAL_CDT_PROJECT_LAUNCHER: return createLocalCDTProjectLauncher();
			case AppsPackage.SCIENCE_APP: return createScienceApp();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case AppsPackage.ENVIRONMENT_TYPE:
				return createEnvironmentTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case AppsPackage.ENVIRONMENT_TYPE:
				return convertEnvironmentTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentManager createEnvironmentManager() {
		EnvironmentManagerImpl environmentManager = new EnvironmentManagerImpl();
		return environmentManager;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpackPackage createSpackPackage() {
		SpackPackageImpl spackPackage = new SpackPackageImpl();
		return spackPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Environment createEnvironment() {
		EnvironmentImpl environment = new EnvironmentImpl();
		return environment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PTPSyncProjectLauncher createPTPSyncProjectLauncher() {
		PTPSyncProjectLauncherImpl ptpSyncProjectLauncher = new PTPSyncProjectLauncherImpl();
		return ptpSyncProjectLauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalCDTProjectLauncher createLocalCDTProjectLauncher() {
		LocalCDTProjectLauncherImpl localCDTProjectLauncher = new LocalCDTProjectLauncherImpl();
		return localCDTProjectLauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScienceApp createScienceApp() {
		ScienceAppImpl scienceApp = new ScienceAppImpl();
		return scienceApp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentType createEnvironmentTypeFromString(EDataType eDataType, String initialValue) {
		EnvironmentType result = EnvironmentType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnvironmentTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppsPackage getAppsPackage() {
		return (AppsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AppsPackage getPackage() {
		return AppsPackage.eINSTANCE;
	}

} //AppsFactoryImpl
