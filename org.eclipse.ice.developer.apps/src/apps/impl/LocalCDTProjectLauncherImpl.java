/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.LocalCDTProjectLauncher;
import apps.ScienceApp;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Local CDT Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class LocalCDTProjectLauncherImpl extends MinimalEObjectImpl.Container implements LocalCDTProjectLauncher {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalCDTProjectLauncherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.LOCAL_CDT_PROJECT_LAUNCHER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void launchProject(ScienceApp project) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AppsPackage.LOCAL_CDT_PROJECT_LAUNCHER___LAUNCH_PROJECT__SCIENCEAPP:
				launchProject((ScienceApp)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //LocalCDTProjectLauncherImpl
