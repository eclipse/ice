/**
 */
package apps.local.impl;

import apps.IEnvironment;

import apps.local.LocalEnvironmentBuilder;
import apps.local.LocalPackage;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Environment Builder</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class LocalEnvironmentBuilderImpl extends MinimalEObjectImpl.Container implements LocalEnvironmentBuilder {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalEnvironmentBuilderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LocalPackage.Literals.LOCAL_ENVIRONMENT_BUILDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnvironment build(String properties) {
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
			case LocalPackage.LOCAL_ENVIRONMENT_BUILDER___BUILD__STRING:
				return build((String)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} //LocalEnvironmentBuilderImpl
