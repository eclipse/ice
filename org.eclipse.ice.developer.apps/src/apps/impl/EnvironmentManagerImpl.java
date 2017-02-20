/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.EnvironmentManager;
import apps.IEnvironment;
import apps.IEnvironmentBuilder;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Manager</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.impl.EnvironmentManagerImpl#getBuilder <em>Builder</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvironmentManagerImpl extends MinimalEObjectImpl.Container implements EnvironmentManager {
	/**
	 * The cached value of the '{@link #getBuilder() <em>Builder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuilder()
	 * @generated
	 * @ordered
	 */
	protected IEnvironmentBuilder builder;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnvironmentManagerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.ENVIRONMENT_MANAGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnvironmentBuilder getBuilder() {
		if (builder != null && builder.eIsProxy()) {
			InternalEObject oldBuilder = (InternalEObject)builder;
			builder = (IEnvironmentBuilder)eResolveProxy(oldBuilder);
			if (builder != oldBuilder) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AppsPackage.ENVIRONMENT_MANAGER__BUILDER, oldBuilder, builder));
			}
		}
		return builder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnvironmentBuilder basicGetBuilder() {
		return builder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBuilder(IEnvironmentBuilder newBuilder) {
		IEnvironmentBuilder oldBuilder = builder;
		builder = newBuilder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT_MANAGER__BUILDER, oldBuilder, builder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnvironment createEnvironment(String properties) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> listExisting() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnvironment loadExisting(String name) {
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__BUILDER:
				if (resolve) return getBuilder();
				return basicGetBuilder();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__BUILDER:
				setBuilder((IEnvironmentBuilder)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__BUILDER:
				setBuilder((IEnvironmentBuilder)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT_MANAGER__BUILDER:
				return builder != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AppsPackage.ENVIRONMENT_MANAGER___CREATE_ENVIRONMENT__STRING:
				return createEnvironment((String)arguments.get(0));
			case AppsPackage.ENVIRONMENT_MANAGER___LIST_EXISTING:
				return listExisting();
			case AppsPackage.ENVIRONMENT_MANAGER___LOAD_EXISTING__STRING:
				return loadExisting((String)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} //EnvironmentManagerImpl
