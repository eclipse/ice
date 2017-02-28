/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.JsonEnvironmentCreator;
import apps.OSPackage;
import apps.PackageType;
import apps.SourcePackage;
import apps.SpackDependency;
import apps.SpackPackage;
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
			case AppsPackage.SOURCE_PACKAGE: return createSourcePackage();
			case AppsPackage.OS_PACKAGE: return createOSPackage();
			case AppsPackage.SPACK_DEPENDENCY: return createSpackDependency();
			case AppsPackage.JSON_ENVIRONMENT_CREATOR: return createJsonEnvironmentCreator();
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
			case AppsPackage.PACKAGE_TYPE:
				return createPackageTypeFromString(eDataType, initialValue);
			case AppsPackage.ENVIRONMENT_STATE:
				return createEnvironmentStateFromString(eDataType, initialValue);
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
			case AppsPackage.PACKAGE_TYPE:
				return convertPackageTypeToString(eDataType, instanceValue);
			case AppsPackage.ENVIRONMENT_STATE:
				return convertEnvironmentStateToString(eDataType, instanceValue);
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
	public SourcePackage createSourcePackage() {
		SourcePackageImpl sourcePackage = new SourcePackageImpl();
		return sourcePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OSPackage createOSPackage() {
		OSPackageImpl osPackage = new OSPackageImpl();
		return osPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpackDependency createSpackDependency() {
		SpackDependencyImpl spackDependency = new SpackDependencyImpl();
		return spackDependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JsonEnvironmentCreator createJsonEnvironmentCreator() {
		JsonEnvironmentCreatorImpl jsonEnvironmentCreator = new JsonEnvironmentCreatorImpl();
		return jsonEnvironmentCreator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PackageType createPackageTypeFromString(EDataType eDataType, String initialValue) {
		PackageType result = PackageType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPackageTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentState createEnvironmentStateFromString(EDataType eDataType, String initialValue) {
		EnvironmentState result = EnvironmentState.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnvironmentStateToString(EDataType eDataType, Object instanceValue) {
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
