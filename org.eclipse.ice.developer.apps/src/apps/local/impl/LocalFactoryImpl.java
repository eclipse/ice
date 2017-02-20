/**
 */
package apps.local.impl;

import apps.local.*;

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
public class LocalFactoryImpl extends EFactoryImpl implements LocalFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LocalFactory init() {
		try {
			LocalFactory theLocalFactory = (LocalFactory)EPackage.Registry.INSTANCE.getEFactory(LocalPackage.eNS_URI);
			if (theLocalFactory != null) {
				return theLocalFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LocalFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalFactoryImpl() {
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
			case LocalPackage.LOCAL_ENVIRONMENT_BUILDER: return createLocalEnvironmentBuilder();
			case LocalPackage.LOCAL_ENVIRONMENT: return createLocalEnvironment();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalEnvironmentBuilder createLocalEnvironmentBuilder() {
		LocalEnvironmentBuilderImpl localEnvironmentBuilder = new LocalEnvironmentBuilderImpl();
		return localEnvironmentBuilder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalEnvironment createLocalEnvironment() {
		LocalEnvironmentImpl localEnvironment = new LocalEnvironmentImpl();
		return localEnvironment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalPackage getLocalPackage() {
		return (LocalPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LocalPackage getPackage() {
		return LocalPackage.eINSTANCE;
	}

} //LocalFactoryImpl
