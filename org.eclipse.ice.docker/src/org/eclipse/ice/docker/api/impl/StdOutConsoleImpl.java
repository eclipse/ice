/**
 */
package org.eclipse.ice.docker.api.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.ice.docker.api.DockerapiPackage;
import org.eclipse.ice.docker.api.StdOutConsole;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Std Out Console</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class StdOutConsoleImpl extends MinimalEObjectImpl.Container implements StdOutConsole {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StdOutConsoleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerapiPackage.Literals.STD_OUT_CONSOLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void print(String msg) {
		System.out.println(msg);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case DockerapiPackage.STD_OUT_CONSOLE___PRINT__STRING:
				print((String)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //StdOutConsoleImpl
