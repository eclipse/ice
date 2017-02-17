/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironmentDataManager;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SC Environment Data Manager</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentDataManagerImpl#getScenvironments <em>Scenvironments</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SCEnvironmentDataManagerImpl extends MinimalEObjectImpl.Container implements SCEnvironmentDataManager {
	/**
	 * The cached value of the '{@link #getScenvironments() <em>Scenvironments</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenvironments()
	 * @generated
	 * @ordered
	 */
	protected EList<SCEnvironment> scenvironments;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SCEnvironmentDataManagerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenvironmentPackage.Literals.SC_ENVIRONMENT_DATA_MANAGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SCEnvironment> getScenvironments() {
		if (scenvironments == null) {
			scenvironments = new EObjectResolvingEList<SCEnvironment>(SCEnvironment.class, this, ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS);
		}
		return scenvironments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SCEnvironment> listExistingSCEenv() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SCEnvironment createSCEnvironment() {
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
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS:
				return getScenvironments();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS:
				getScenvironments().clear();
				getScenvironments().addAll((Collection<? extends SCEnvironment>)newValue);
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
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS:
				getScenvironments().clear();
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
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER__SCENVIRONMENTS:
				return scenvironments != null && !scenvironments.isEmpty();
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
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER___LIST_EXISTING_SC_EENV:
				return listExistingSCEenv();
			case ScenvironmentPackage.SC_ENVIRONMENT_DATA_MANAGER___CREATE_SC_ENVIRONMENT:
				return createSCEnvironment();
		}
		return super.eInvoke(operationID, arguments);
	}

} //SCEnvironmentDataManagerImpl
