/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.EnvironmentConsole;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Environment Console</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EnvironmentConsoleImpl extends MinimalEObjectImpl.Container implements EnvironmentConsole {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnvironmentConsoleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.ENVIRONMENT_CONSOLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void print(String message) {
		System.out.println(message);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AppsPackage.ENVIRONMENT_CONSOLE___PRINT__STRING:
				print((String)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //EnvironmentConsoleImpl
