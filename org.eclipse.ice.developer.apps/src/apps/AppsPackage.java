/**
 */
package apps;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see apps.AppsFactory
 * @model kind="package"
 * @generated
 */
public interface AppsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "apps";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/ice/apps";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "developerappstore";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AppsPackage eINSTANCE = apps.impl.AppsPackageImpl.init();

	/**
	 * The meta object id for the '{@link apps.IEnvironment <em>IEnvironment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment
	 * @see apps.impl.AppsPackageImpl#getIEnvironment()
	 * @generated
	 */
	int IENVIRONMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Os</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__OS = 1;

	/**
	 * The feature id for the '<em><b>Dependent Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__DEPENDENT_PACKAGES = 2;

	/**
	 * The feature id for the '<em><b>Primary App</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__PRIMARY_APP = 3;

	/**
	 * The feature id for the '<em><b>Projectlauncher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__PROJECTLAUNCHER = 4;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT__STATE = 5;

	/**
	 * The number of structural features of the '<em>IEnvironment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_FEATURE_COUNT = 6;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT___BUILD = 0;

	/**
	 * The operation id for the '<em>Connect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT___CONNECT = 1;

	/**
	 * The operation id for the '<em>Delete</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT___DELETE = 2;

	/**
	 * The operation id for the '<em>Stop</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT___STOP = 3;

	/**
	 * The number of operations of the '<em>IEnvironment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENVIRONMENT_OPERATION_COUNT = 4;

	/**
	 * The meta object id for the '{@link apps.impl.EnvironmentManagerImpl <em>Environment Manager</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.EnvironmentManagerImpl
	 * @see apps.impl.AppsPackageImpl#getEnvironmentManager()
	 * @generated
	 */
	int ENVIRONMENT_MANAGER = 1;

	/**
	 * The number of structural features of the '<em>Environment Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Create Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING = 0;

	/**
	 * The operation id for the '<em>List Existing Environments</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS = 1;

	/**
	 * The operation id for the '<em>Load Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT__STRING = 2;

	/**
	 * The operation id for the '<em>Load Environment From File</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING = 3;

	/**
	 * The operation id for the '<em>Persist To XMI String</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__STRING = 4;

	/**
	 * The operation id for the '<em>Persist XMI To File</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__STRING_STRING = 5;

	/**
	 * The operation id for the '<em>Connect To Existing Environment</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER___CONNECT_TO_EXISTING_ENVIRONMENT__STRING = 6;

	/**
	 * The number of operations of the '<em>Environment Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_MANAGER_OPERATION_COUNT = 7;

	/**
	 * The meta object id for the '{@link apps.Package <em>Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.Package
	 * @see apps.impl.AppsPackageImpl#getPackage()
	 * @generated
	 */
	int PACKAGE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__VERSION = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link apps.impl.SpackPackageImpl <em>Spack Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.SpackPackageImpl
	 * @see apps.impl.AppsPackageImpl#getSpackPackage()
	 * @generated
	 */
	int SPACK_PACKAGE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__NAME = PACKAGE__NAME;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__VERSION = PACKAGE__VERSION;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__TYPE = PACKAGE__TYPE;

	/**
	 * The feature id for the '<em><b>Compiler</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__COMPILER = PACKAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cpp Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__CPP_FLAGS = PACKAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE__DEPENDENCIES = PACKAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_FEATURE_COUNT = PACKAGE_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Spack Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_PACKAGE_OPERATION_COUNT = PACKAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.ProjectLauncher <em>Project Launcher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.ProjectLauncher
	 * @see apps.impl.AppsPackageImpl#getProjectLauncher()
	 * @generated
	 */
	int PROJECT_LAUNCHER = 3;

	/**
	 * The number of structural features of the '<em>Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_LAUNCHER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Launch Project</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = 0;

	/**
	 * The number of operations of the '<em>Project Launcher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_LAUNCHER_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link apps.impl.SourcePackageImpl <em>Source Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.SourcePackageImpl
	 * @see apps.impl.AppsPackageImpl#getSourcePackage()
	 * @generated
	 */
	int SOURCE_PACKAGE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE__NAME = PACKAGE__NAME;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE__VERSION = PACKAGE__VERSION;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE__TYPE = PACKAGE__TYPE;

	/**
	 * The feature id for the '<em><b>Repo URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE__REPO_URL = PACKAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Branch</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE__BRANCH = PACKAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Build Command</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE__BUILD_COMMAND = PACKAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Source Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE_FEATURE_COUNT = PACKAGE_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Source Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_PACKAGE_OPERATION_COUNT = PACKAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.impl.OSPackageImpl <em>OS Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.OSPackageImpl
	 * @see apps.impl.AppsPackageImpl#getOSPackage()
	 * @generated
	 */
	int OS_PACKAGE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_PACKAGE__NAME = PACKAGE__NAME;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_PACKAGE__VERSION = PACKAGE__VERSION;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_PACKAGE__TYPE = PACKAGE__TYPE;

	/**
	 * The number of structural features of the '<em>OS Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_PACKAGE_FEATURE_COUNT = PACKAGE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>OS Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_PACKAGE_OPERATION_COUNT = PACKAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link apps.impl.SpackDependencyImpl <em>Spack Dependency</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.impl.SpackDependencyImpl
	 * @see apps.impl.AppsPackageImpl#getSpackDependency()
	 * @generated
	 */
	int SPACK_DEPENDENCY = 7;

	/**
	 * The feature id for the '<em><b>Dependency</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_DEPENDENCY__DEPENDENCY = 0;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_DEPENDENCY__PROVIDER = 1;

	/**
	 * The number of structural features of the '<em>Spack Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_DEPENDENCY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Spack Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPACK_DEPENDENCY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link apps.PackageType <em>Package Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.PackageType
	 * @see apps.impl.AppsPackageImpl#getPackageType()
	 * @generated
	 */
	int PACKAGE_TYPE = 8;

	/**
	 * The meta object id for the '{@link apps.EnvironmentState <em>Environment State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentState
	 * @see apps.impl.AppsPackageImpl#getEnvironmentState()
	 * @generated
	 */
	int ENVIRONMENT_STATE = 9;

	/**
	 * Returns the meta object for class '{@link apps.IEnvironment <em>IEnvironment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEnvironment</em>'.
	 * @see apps.IEnvironment
	 * @generated
	 */
	EClass getIEnvironment();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see apps.IEnvironment#getName()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_Name();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#getOs <em>Os</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Os</em>'.
	 * @see apps.IEnvironment#getOs()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_Os();

	/**
	 * Returns the meta object for the containment reference list '{@link apps.IEnvironment#getDependentPackages <em>Dependent Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependent Packages</em>'.
	 * @see apps.IEnvironment#getDependentPackages()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_DependentPackages();

	/**
	 * Returns the meta object for the containment reference '{@link apps.IEnvironment#getPrimaryApp <em>Primary App</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Primary App</em>'.
	 * @see apps.IEnvironment#getPrimaryApp()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_PrimaryApp();

	/**
	 * Returns the meta object for the containment reference '{@link apps.IEnvironment#getProjectlauncher <em>Projectlauncher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Projectlauncher</em>'.
	 * @see apps.IEnvironment#getProjectlauncher()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EReference getIEnvironment_Projectlauncher();

	/**
	 * Returns the meta object for the attribute '{@link apps.IEnvironment#getState <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see apps.IEnvironment#getState()
	 * @see #getIEnvironment()
	 * @generated
	 */
	EAttribute getIEnvironment_State();

	/**
	 * Returns the meta object for the '{@link apps.IEnvironment#build() <em>Build</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Build</em>' operation.
	 * @see apps.IEnvironment#build()
	 * @generated
	 */
	EOperation getIEnvironment__Build();

	/**
	 * Returns the meta object for the '{@link apps.IEnvironment#connect() <em>Connect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Connect</em>' operation.
	 * @see apps.IEnvironment#connect()
	 * @generated
	 */
	EOperation getIEnvironment__Connect();

	/**
	 * Returns the meta object for the '{@link apps.IEnvironment#delete() <em>Delete</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Delete</em>' operation.
	 * @see apps.IEnvironment#delete()
	 * @generated
	 */
	EOperation getIEnvironment__Delete();

	/**
	 * Returns the meta object for the '{@link apps.IEnvironment#stop() <em>Stop</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Stop</em>' operation.
	 * @see apps.IEnvironment#stop()
	 * @generated
	 */
	EOperation getIEnvironment__Stop();

	/**
	 * Returns the meta object for class '{@link apps.EnvironmentManager <em>Environment Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment Manager</em>'.
	 * @see apps.EnvironmentManager
	 * @generated
	 */
	EClass getEnvironmentManager();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#createEnvironment(java.lang.String) <em>Create Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Environment</em>' operation.
	 * @see apps.EnvironmentManager#createEnvironment(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__CreateEnvironment__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#listExistingEnvironments() <em>List Existing Environments</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>List Existing Environments</em>' operation.
	 * @see apps.EnvironmentManager#listExistingEnvironments()
	 * @generated
	 */
	EOperation getEnvironmentManager__ListExistingEnvironments();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#loadEnvironment(java.lang.String) <em>Load Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Load Environment</em>' operation.
	 * @see apps.EnvironmentManager#loadEnvironment(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__LoadEnvironment__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String) <em>Load Environment From File</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Load Environment From File</em>' operation.
	 * @see apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__LoadEnvironmentFromFile__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#persistToXMIString(java.lang.String) <em>Persist To XMI String</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Persist To XMI String</em>' operation.
	 * @see apps.EnvironmentManager#persistToXMIString(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__PersistToXMIString__String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#persistXMIToFile(java.lang.String, java.lang.String) <em>Persist XMI To File</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Persist XMI To File</em>' operation.
	 * @see apps.EnvironmentManager#persistXMIToFile(java.lang.String, java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__PersistXMIToFile__String_String();

	/**
	 * Returns the meta object for the '{@link apps.EnvironmentManager#connectToExistingEnvironment(java.lang.String) <em>Connect To Existing Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Connect To Existing Environment</em>' operation.
	 * @see apps.EnvironmentManager#connectToExistingEnvironment(java.lang.String)
	 * @generated
	 */
	EOperation getEnvironmentManager__ConnectToExistingEnvironment__String();

	/**
	 * Returns the meta object for class '{@link apps.SpackPackage <em>Spack Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spack Package</em>'.
	 * @see apps.SpackPackage
	 * @generated
	 */
	EClass getSpackPackage();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getCompiler <em>Compiler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Compiler</em>'.
	 * @see apps.SpackPackage#getCompiler()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_Compiler();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackPackage#getCppFlags <em>Cpp Flags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cpp Flags</em>'.
	 * @see apps.SpackPackage#getCppFlags()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EAttribute getSpackPackage_CppFlags();

	/**
	 * Returns the meta object for the reference list '{@link apps.SpackPackage#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Dependencies</em>'.
	 * @see apps.SpackPackage#getDependencies()
	 * @see #getSpackPackage()
	 * @generated
	 */
	EReference getSpackPackage_Dependencies();

	/**
	 * Returns the meta object for class '{@link apps.ProjectLauncher <em>Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project Launcher</em>'.
	 * @see apps.ProjectLauncher
	 * @generated
	 */
	EClass getProjectLauncher();

	/**
	 * Returns the meta object for the '{@link apps.ProjectLauncher#launchProject(apps.SourcePackage) <em>Launch Project</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Launch Project</em>' operation.
	 * @see apps.ProjectLauncher#launchProject(apps.SourcePackage)
	 * @generated
	 */
	EOperation getProjectLauncher__LaunchProject__SourcePackage();

	/**
	 * Returns the meta object for class '{@link apps.Package <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Package</em>'.
	 * @see apps.Package
	 * @generated
	 */
	EClass getPackage();

	/**
	 * Returns the meta object for the attribute '{@link apps.Package#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see apps.Package#getName()
	 * @see #getPackage()
	 * @generated
	 */
	EAttribute getPackage_Name();

	/**
	 * Returns the meta object for the attribute '{@link apps.Package#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see apps.Package#getVersion()
	 * @see #getPackage()
	 * @generated
	 */
	EAttribute getPackage_Version();

	/**
	 * Returns the meta object for the attribute '{@link apps.Package#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see apps.Package#getType()
	 * @see #getPackage()
	 * @generated
	 */
	EAttribute getPackage_Type();

	/**
	 * Returns the meta object for class '{@link apps.SourcePackage <em>Source Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Source Package</em>'.
	 * @see apps.SourcePackage
	 * @generated
	 */
	EClass getSourcePackage();

	/**
	 * Returns the meta object for the attribute '{@link apps.SourcePackage#getRepoURL <em>Repo URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repo URL</em>'.
	 * @see apps.SourcePackage#getRepoURL()
	 * @see #getSourcePackage()
	 * @generated
	 */
	EAttribute getSourcePackage_RepoURL();

	/**
	 * Returns the meta object for the attribute '{@link apps.SourcePackage#getBranch <em>Branch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Branch</em>'.
	 * @see apps.SourcePackage#getBranch()
	 * @see #getSourcePackage()
	 * @generated
	 */
	EAttribute getSourcePackage_Branch();

	/**
	 * Returns the meta object for the attribute '{@link apps.SourcePackage#getBuildCommand <em>Build Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Build Command</em>'.
	 * @see apps.SourcePackage#getBuildCommand()
	 * @see #getSourcePackage()
	 * @generated
	 */
	EAttribute getSourcePackage_BuildCommand();

	/**
	 * Returns the meta object for class '{@link apps.OSPackage <em>OS Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>OS Package</em>'.
	 * @see apps.OSPackage
	 * @generated
	 */
	EClass getOSPackage();

	/**
	 * Returns the meta object for class '{@link apps.SpackDependency <em>Spack Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spack Dependency</em>'.
	 * @see apps.SpackDependency
	 * @generated
	 */
	EClass getSpackDependency();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackDependency#getDependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dependency</em>'.
	 * @see apps.SpackDependency#getDependency()
	 * @see #getSpackDependency()
	 * @generated
	 */
	EAttribute getSpackDependency_Dependency();

	/**
	 * Returns the meta object for the attribute '{@link apps.SpackDependency#getProvider <em>Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Provider</em>'.
	 * @see apps.SpackDependency#getProvider()
	 * @see #getSpackDependency()
	 * @generated
	 */
	EAttribute getSpackDependency_Provider();

	/**
	 * Returns the meta object for enum '{@link apps.PackageType <em>Package Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Package Type</em>'.
	 * @see apps.PackageType
	 * @generated
	 */
	EEnum getPackageType();

	/**
	 * Returns the meta object for enum '{@link apps.EnvironmentState <em>Environment State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Environment State</em>'.
	 * @see apps.EnvironmentState
	 * @generated
	 */
	EEnum getEnvironmentState();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AppsFactory getAppsFactory();

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
		 * The meta object literal for the '{@link apps.IEnvironment <em>IEnvironment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.IEnvironment
		 * @see apps.impl.AppsPackageImpl#getIEnvironment()
		 * @generated
		 */
		EClass IENVIRONMENT = eINSTANCE.getIEnvironment();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__NAME = eINSTANCE.getIEnvironment_Name();

		/**
		 * The meta object literal for the '<em><b>Os</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__OS = eINSTANCE.getIEnvironment_Os();

		/**
		 * The meta object literal for the '<em><b>Dependent Packages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__DEPENDENT_PACKAGES = eINSTANCE.getIEnvironment_DependentPackages();

		/**
		 * The meta object literal for the '<em><b>Primary App</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__PRIMARY_APP = eINSTANCE.getIEnvironment_PrimaryApp();

		/**
		 * The meta object literal for the '<em><b>Projectlauncher</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENVIRONMENT__PROJECTLAUNCHER = eINSTANCE.getIEnvironment_Projectlauncher();

		/**
		 * The meta object literal for the '<em><b>State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENVIRONMENT__STATE = eINSTANCE.getIEnvironment_State();

		/**
		 * The meta object literal for the '<em><b>Build</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT___BUILD = eINSTANCE.getIEnvironment__Build();

		/**
		 * The meta object literal for the '<em><b>Connect</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT___CONNECT = eINSTANCE.getIEnvironment__Connect();

		/**
		 * The meta object literal for the '<em><b>Delete</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT___DELETE = eINSTANCE.getIEnvironment__Delete();

		/**
		 * The meta object literal for the '<em><b>Stop</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IENVIRONMENT___STOP = eINSTANCE.getIEnvironment__Stop();

		/**
		 * The meta object literal for the '{@link apps.impl.EnvironmentManagerImpl <em>Environment Manager</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.EnvironmentManagerImpl
		 * @see apps.impl.AppsPackageImpl#getEnvironmentManager()
		 * @generated
		 */
		EClass ENVIRONMENT_MANAGER = eINSTANCE.getEnvironmentManager();

		/**
		 * The meta object literal for the '<em><b>Create Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING = eINSTANCE.getEnvironmentManager__CreateEnvironment__String();

		/**
		 * The meta object literal for the '<em><b>List Existing Environments</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LIST_EXISTING_ENVIRONMENTS = eINSTANCE.getEnvironmentManager__ListExistingEnvironments();

		/**
		 * The meta object literal for the '<em><b>Load Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT__STRING = eINSTANCE.getEnvironmentManager__LoadEnvironment__String();

		/**
		 * The meta object literal for the '<em><b>Load Environment From File</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___LOAD_ENVIRONMENT_FROM_FILE__STRING = eINSTANCE.getEnvironmentManager__LoadEnvironmentFromFile__String();

		/**
		 * The meta object literal for the '<em><b>Persist To XMI String</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___PERSIST_TO_XMI_STRING__STRING = eINSTANCE.getEnvironmentManager__PersistToXMIString__String();

		/**
		 * The meta object literal for the '<em><b>Persist XMI To File</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___PERSIST_XMI_TO_FILE__STRING_STRING = eINSTANCE.getEnvironmentManager__PersistXMIToFile__String_String();

		/**
		 * The meta object literal for the '<em><b>Connect To Existing Environment</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ENVIRONMENT_MANAGER___CONNECT_TO_EXISTING_ENVIRONMENT__STRING = eINSTANCE.getEnvironmentManager__ConnectToExistingEnvironment__String();

		/**
		 * The meta object literal for the '{@link apps.impl.SpackPackageImpl <em>Spack Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.SpackPackageImpl
		 * @see apps.impl.AppsPackageImpl#getSpackPackage()
		 * @generated
		 */
		EClass SPACK_PACKAGE = eINSTANCE.getSpackPackage();

		/**
		 * The meta object literal for the '<em><b>Compiler</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__COMPILER = eINSTANCE.getSpackPackage_Compiler();

		/**
		 * The meta object literal for the '<em><b>Cpp Flags</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_PACKAGE__CPP_FLAGS = eINSTANCE.getSpackPackage_CppFlags();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPACK_PACKAGE__DEPENDENCIES = eINSTANCE.getSpackPackage_Dependencies();

		/**
		 * The meta object literal for the '{@link apps.ProjectLauncher <em>Project Launcher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.ProjectLauncher
		 * @see apps.impl.AppsPackageImpl#getProjectLauncher()
		 * @generated
		 */
		EClass PROJECT_LAUNCHER = eINSTANCE.getProjectLauncher();

		/**
		 * The meta object literal for the '<em><b>Launch Project</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE = eINSTANCE.getProjectLauncher__LaunchProject__SourcePackage();

		/**
		 * The meta object literal for the '{@link apps.Package <em>Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.Package
		 * @see apps.impl.AppsPackageImpl#getPackage()
		 * @generated
		 */
		EClass PACKAGE = eINSTANCE.getPackage();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE__NAME = eINSTANCE.getPackage_Name();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE__VERSION = eINSTANCE.getPackage_Version();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE__TYPE = eINSTANCE.getPackage_Type();

		/**
		 * The meta object literal for the '{@link apps.impl.SourcePackageImpl <em>Source Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.SourcePackageImpl
		 * @see apps.impl.AppsPackageImpl#getSourcePackage()
		 * @generated
		 */
		EClass SOURCE_PACKAGE = eINSTANCE.getSourcePackage();

		/**
		 * The meta object literal for the '<em><b>Repo URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_PACKAGE__REPO_URL = eINSTANCE.getSourcePackage_RepoURL();

		/**
		 * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_PACKAGE__BRANCH = eINSTANCE.getSourcePackage_Branch();

		/**
		 * The meta object literal for the '<em><b>Build Command</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_PACKAGE__BUILD_COMMAND = eINSTANCE.getSourcePackage_BuildCommand();

		/**
		 * The meta object literal for the '{@link apps.impl.OSPackageImpl <em>OS Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.OSPackageImpl
		 * @see apps.impl.AppsPackageImpl#getOSPackage()
		 * @generated
		 */
		EClass OS_PACKAGE = eINSTANCE.getOSPackage();

		/**
		 * The meta object literal for the '{@link apps.impl.SpackDependencyImpl <em>Spack Dependency</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.impl.SpackDependencyImpl
		 * @see apps.impl.AppsPackageImpl#getSpackDependency()
		 * @generated
		 */
		EClass SPACK_DEPENDENCY = eINSTANCE.getSpackDependency();

		/**
		 * The meta object literal for the '<em><b>Dependency</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_DEPENDENCY__DEPENDENCY = eINSTANCE.getSpackDependency_Dependency();

		/**
		 * The meta object literal for the '<em><b>Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPACK_DEPENDENCY__PROVIDER = eINSTANCE.getSpackDependency_Provider();

		/**
		 * The meta object literal for the '{@link apps.PackageType <em>Package Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.PackageType
		 * @see apps.impl.AppsPackageImpl#getPackageType()
		 * @generated
		 */
		EEnum PACKAGE_TYPE = eINSTANCE.getPackageType();

		/**
		 * The meta object literal for the '{@link apps.EnvironmentState <em>Environment State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see apps.EnvironmentState
		 * @see apps.impl.AppsPackageImpl#getEnvironmentState()
		 * @generated
		 */
		EEnum ENVIRONMENT_STATE = eINSTANCE.getEnvironmentState();

	}

} //AppsPackage
