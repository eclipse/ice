/**
 */
package apps.util;

import apps.AppsPackage;
import apps.EnvironmentBuilder;
import apps.EnvironmentConsole;
import apps.EnvironmentCreator;
import apps.EnvironmentManager;
import apps.EnvironmentStorage;
import apps.IEnvironment;
import apps.JsonEnvironmentCreator;
import apps.LanguageProjectProvider;
import apps.OSPackage;
import apps.ProjectLauncher;
import apps.SourcePackage;
import apps.SpackDependency;
import apps.SpackPackage;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see apps.AppsPackage
 * @generated
 */
public class AppsAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AppsPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppsAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = AppsPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AppsSwitch<Adapter> modelSwitch =
		new AppsSwitch<Adapter>() {
			@Override
			public Adapter caseIEnvironment(IEnvironment object) {
				return createIEnvironmentAdapter();
			}
			@Override
			public Adapter caseEnvironmentManager(EnvironmentManager object) {
				return createEnvironmentManagerAdapter();
			}
			@Override
			public Adapter caseSpackPackage(SpackPackage object) {
				return createSpackPackageAdapter();
			}
			@Override
			public Adapter caseProjectLauncher(ProjectLauncher object) {
				return createProjectLauncherAdapter();
			}
			@Override
			public Adapter casePackage(apps.Package object) {
				return createPackageAdapter();
			}
			@Override
			public Adapter caseSourcePackage(SourcePackage object) {
				return createSourcePackageAdapter();
			}
			@Override
			public Adapter caseOSPackage(OSPackage object) {
				return createOSPackageAdapter();
			}
			@Override
			public Adapter caseSpackDependency(SpackDependency object) {
				return createSpackDependencyAdapter();
			}
			@Override
			public Adapter caseEnvironmentCreator(EnvironmentCreator object) {
				return createEnvironmentCreatorAdapter();
			}
			@Override
			public Adapter caseJsonEnvironmentCreator(JsonEnvironmentCreator object) {
				return createJsonEnvironmentCreatorAdapter();
			}
			@Override
			public Adapter caseEnvironmentStorage(EnvironmentStorage object) {
				return createEnvironmentStorageAdapter();
			}
			@Override
			public Adapter caseLanguageProjectProvider(LanguageProjectProvider object) {
				return createLanguageProjectProviderAdapter();
			}
			@Override
			public Adapter caseEnvironmentConsole(EnvironmentConsole object) {
				return createEnvironmentConsoleAdapter();
			}
			@Override
			public Adapter caseEnvironmentBuilder(EnvironmentBuilder object) {
				return createEnvironmentBuilderAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link apps.IEnvironment <em>IEnvironment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.IEnvironment
	 * @generated
	 */
	public Adapter createIEnvironmentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentManager <em>Environment Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.EnvironmentManager
	 * @generated
	 */
	public Adapter createEnvironmentManagerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.SpackPackage <em>Spack Package</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.SpackPackage
	 * @generated
	 */
	public Adapter createSpackPackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.ProjectLauncher <em>Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.ProjectLauncher
	 * @generated
	 */
	public Adapter createProjectLauncherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.Package <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.Package
	 * @generated
	 */
	public Adapter createPackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.SourcePackage <em>Source Package</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.SourcePackage
	 * @generated
	 */
	public Adapter createSourcePackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.OSPackage <em>OS Package</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.OSPackage
	 * @generated
	 */
	public Adapter createOSPackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.SpackDependency <em>Spack Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.SpackDependency
	 * @generated
	 */
	public Adapter createSpackDependencyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentCreator <em>Environment Creator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.EnvironmentCreator
	 * @generated
	 */
	public Adapter createEnvironmentCreatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.JsonEnvironmentCreator <em>Json Environment Creator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.JsonEnvironmentCreator
	 * @generated
	 */
	public Adapter createJsonEnvironmentCreatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentStorage <em>Environment Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.EnvironmentStorage
	 * @generated
	 */
	public Adapter createEnvironmentStorageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.LanguageProjectProvider <em>Language Project Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.LanguageProjectProvider
	 * @generated
	 */
	public Adapter createLanguageProjectProviderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentConsole <em>Environment Console</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.EnvironmentConsole
	 * @generated
	 */
	public Adapter createEnvironmentConsoleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentBuilder <em>Environment Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.EnvironmentBuilder
	 * @generated
	 */
	public Adapter createEnvironmentBuilderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //AppsAdapterFactory
