/**
 */
package apps.local;

import apps.AppsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see apps.local.LocalFactory
 * @model kind="package"
 * @generated
 */
public interface LocalPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "local";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://eclipse.org/apps/local";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "localenvironment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LocalPackage eINSTANCE = apps.local.impl.LocalPackageImpl.init();

	/**
	 * The meta object id for the '{@link apps.local.impl.LocalEnvironmentBuilderImpl <em>Environment Builder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.local.impl.LocalEnvironmentBuilderImpl
	 * @see apps.local.impl.LocalPackageImpl#getLocalEnvironmentBuilder()
	 * @generated
	 */
	int LOCAL_ENVIRONMENT_BUILDER = 0;

	/**
	 * The number of structural features of the '<em>Environment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT_BUILDER_FEATURE_COUNT = AppsPackage.IENVIRONMENT_BUILDER_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT_BUILDER___BUILD__STRING = AppsPackage.IENVIRONMENT_BUILDER___BUILD__STRING;

	/**
	 * The number of operations of the '<em>Environment Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT_BUILDER_OPERATION_COUNT = AppsPackage.IENVIRONMENT_BUILDER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.local.impl.LocalEnvironmentImpl <em>Environment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.local.impl.LocalEnvironmentImpl
	 * @see apps.local.impl.LocalPackageImpl#getLocalEnvironment()
	 * @generated
	 */
	int LOCAL_ENVIRONMENT = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT__TYPE = AppsPackage.IENVIRONMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT__NAME = AppsPackage.IENVIRONMENT__NAME;

	/**
	 * The feature id for the '<em><b>Os</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT__OS = AppsPackage.IENVIRONMENT__OS;

	/**
	 * The feature id for the '<em><b>Spackpackage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT__SPACKPACKAGE = AppsPackage.IENVIRONMENT__SPACKPACKAGE;

	/**
	 * The feature id for the '<em><b>Scienceapp</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT__SCIENCEAPP = AppsPackage.IENVIRONMENT__SCIENCEAPP;

	/**
	 * The number of structural features of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT_FEATURE_COUNT = AppsPackage.IENVIRONMENT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Launch</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT___LAUNCH = AppsPackage.IENVIRONMENT___LAUNCH;

	/**
	 * The number of operations of the '<em>Environment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_ENVIRONMENT_OPERATION_COUNT = AppsPackage.IENVIRONMENT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link apps.local.LocalEnvironmentBuilder <em>Environment Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment Builder</em>'.
	 * @see apps.local.LocalEnvironmentBuilder
	 * @generated
	 */
	EClass getLocalEnvironmentBuilder();

	/**
	 * Returns the meta object for class '{@link apps.local.LocalEnvironment <em>Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment</em>'.
	 * @see apps.local.LocalEnvironment
	 * @generated
	 */
	EClass getLocalEnvironment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	LocalFactory getLocalFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link apps.local.impl.LocalEnvironmentBuilderImpl <em>Environment Builder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.local.impl.LocalEnvironmentBuilderImpl
		 * @see apps.local.impl.LocalPackageImpl#getLocalEnvironmentBuilder()
		 * @generated
		 */
		EClass LOCAL_ENVIRONMENT_BUILDER = eINSTANCE.getLocalEnvironmentBuilder();

		/**
		 * The meta object literal for the '{@link apps.local.impl.LocalEnvironmentImpl <em>Environment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.local.impl.LocalEnvironmentImpl
		 * @see apps.local.impl.LocalPackageImpl#getLocalEnvironment()
		 * @generated
		 */
		EClass LOCAL_ENVIRONMENT = eINSTANCE.getLocalEnvironment();

	}

} //LocalPackage
