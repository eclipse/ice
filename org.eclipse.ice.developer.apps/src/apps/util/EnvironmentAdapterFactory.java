/**
 */
package apps.util;

import apps.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see apps.EnvironmentPackage
 * @generated
 */
public class EnvironmentAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EnvironmentPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = EnvironmentPackage.eINSTANCE;
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
	protected EnvironmentSwitch<Adapter> modelSwitch =
		new EnvironmentSwitch<Adapter>() {
			@Override
			public Adapter caseIEnvironment(IEnvironment object) {
				return createIEnvironmentAdapter();
			}
			@Override
			public Adapter caseIEnvironmentBuilder(IEnvironmentBuilder object) {
				return createIEnvironmentBuilderAdapter();
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
			public Adapter caseEnvironment(Environment object) {
				return createEnvironmentAdapter();
			}
			@Override
			public Adapter caseProjectLauncher(ProjectLauncher object) {
				return createProjectLauncherAdapter();
			}
			@Override
			public Adapter casePTPSyncProjectLauncher(PTPSyncProjectLauncher object) {
				return createPTPSyncProjectLauncherAdapter();
			}
			@Override
			public Adapter caseLocalCDTProjectLauncher(LocalCDTProjectLauncher object) {
				return createLocalCDTProjectLauncherAdapter();
			}
			@Override
			public Adapter caseScienceApp(ScienceApp object) {
				return createScienceAppAdapter();
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
	 * Creates a new adapter for an object of class '{@link apps.IEnvironmentBuilder <em>IEnvironment Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.IEnvironmentBuilder
	 * @generated
	 */
	public Adapter createIEnvironmentBuilderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.EnvironmentManager <em>Manager</em>}'.
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
	 * Creates a new adapter for an object of class '{@link apps.Environment <em>Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.Environment
	 * @generated
	 */
	public Adapter createEnvironmentAdapter() {
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
	 * Creates a new adapter for an object of class '{@link apps.PTPSyncProjectLauncher <em>PTP Sync Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.PTPSyncProjectLauncher
	 * @generated
	 */
	public Adapter createPTPSyncProjectLauncherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.LocalCDTProjectLauncher <em>Local CDT Project Launcher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.LocalCDTProjectLauncher
	 * @generated
	 */
	public Adapter createLocalCDTProjectLauncherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link apps.ScienceApp <em>Science App</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see apps.ScienceApp
	 * @generated
	 */
	public Adapter createScienceAppAdapter() {
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

} //EnvironmentAdapterFactory
