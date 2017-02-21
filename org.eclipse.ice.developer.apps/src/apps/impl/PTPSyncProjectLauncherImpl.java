/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.PTPSyncProjectLauncher;
import apps.SpackPackage;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>PTP Sync Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class PTPSyncProjectLauncherImpl extends MinimalEObjectImpl.Container implements PTPSyncProjectLauncher {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PTPSyncProjectLauncherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.PTP_SYNC_PROJECT_LAUNCHER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void launchProject(SpackPackage project) {
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
			case AppsPackage.PTP_SYNC_PROJECT_LAUNCHER___LAUNCH_PROJECT__SPACKPACKAGE:
				launchProject((SpackPackage)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //PTPSyncProjectLauncherImpl
