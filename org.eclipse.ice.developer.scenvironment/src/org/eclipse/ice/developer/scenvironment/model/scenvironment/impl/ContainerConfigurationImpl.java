/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.ContainerConfiguration;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl#getVolumes <em>Volumes</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.ContainerConfigurationImpl#isIsEphemeral <em>Is Ephemeral</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContainerConfigurationImpl extends InstallerTypeConfigurationImpl implements ContainerConfiguration {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> ports;

	/**
	 * The cached value of the '{@link #getVolumes() <em>Volumes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> volumes;

	/**
	 * The default value of the '{@link #isIsEphemeral() <em>Is Ephemeral</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEphemeral()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_EPHEMERAL_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isIsEphemeral() <em>Is Ephemeral</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEphemeral()
	 * @generated
	 * @ordered
	 */
	protected boolean isEphemeral = IS_EPHEMERAL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContainerConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenvironmentPackage.Literals.CONTAINER_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getPorts() {
		if (ports == null) {
			ports = new EDataTypeUniqueEList<Integer>(Integer.class, this, ScenvironmentPackage.CONTAINER_CONFIGURATION__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getVolumes() {
		if (volumes == null) {
			volumes = new EDataTypeUniqueEList<String>(String.class, this, ScenvironmentPackage.CONTAINER_CONFIGURATION__VOLUMES);
		}
		return volumes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsEphemeral() {
		return isEphemeral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsEphemeral(boolean newIsEphemeral) {
		boolean oldIsEphemeral = isEphemeral;
		isEphemeral = newIsEphemeral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenvironmentPackage.CONTAINER_CONFIGURATION__IS_EPHEMERAL, oldIsEphemeral, isEphemeral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__PORTS:
				return getPorts();
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__VOLUMES:
				return getVolumes();
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__IS_EPHEMERAL:
				return isIsEphemeral();
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
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Integer>)newValue);
				return;
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__VOLUMES:
				getVolumes().clear();
				getVolumes().addAll((Collection<? extends String>)newValue);
				return;
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__IS_EPHEMERAL:
				setIsEphemeral((Boolean)newValue);
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
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__PORTS:
				getPorts().clear();
				return;
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__VOLUMES:
				getVolumes().clear();
				return;
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__IS_EPHEMERAL:
				setIsEphemeral(IS_EPHEMERAL_EDEFAULT);
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
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__PORTS:
				return ports != null && !ports.isEmpty();
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__VOLUMES:
				return volumes != null && !volumes.isEmpty();
			case ScenvironmentPackage.CONTAINER_CONFIGURATION__IS_EPHEMERAL:
				return isEphemeral != IS_EPHEMERAL_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (ports: ");
		result.append(ports);
		result.append(", volumes: ");
		result.append(volumes);
		result.append(", isEphemeral: ");
		result.append(isEphemeral);
		result.append(')');
		return result.toString();
	}

} //ContainerConfigurationImpl
