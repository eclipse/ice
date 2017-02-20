/**
 */
package apps.docker.impl;

import apps.docker.DockerAPI;
import apps.docker.DockerEnvironment;
import apps.docker.DockerPackage;

import apps.impl.EnvironmentImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.DockerEnvironmentImpl#getDocker <em>Docker</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DockerEnvironmentImpl extends EnvironmentImpl implements DockerEnvironment {
	/**
	 * The cached value of the '{@link #getDocker() <em>Docker</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDocker()
	 * @generated
	 * @ordered
	 */
	protected DockerAPI docker;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerEnvironmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.DOCKER_ENVIRONMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI getDocker() {
		if (docker != null && docker.eIsProxy()) {
			InternalEObject oldDocker = (InternalEObject)docker;
			docker = (DockerAPI)eResolveProxy(oldDocker);
			if (docker != oldDocker) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DockerPackage.DOCKER_ENVIRONMENT__DOCKER, oldDocker, docker));
			}
		}
		return docker;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPI basicGetDocker() {
		return docker;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocker(DockerAPI newDocker) {
		DockerAPI oldDocker = docker;
		docker = newDocker;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_ENVIRONMENT__DOCKER, oldDocker, docker));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				if (resolve) return getDocker();
				return basicGetDocker();
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				setDocker((DockerAPI)newValue);
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				setDocker((DockerAPI)null);
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
			case DockerPackage.DOCKER_ENVIRONMENT__DOCKER:
				return docker != null;
		}
		return super.eIsSet(featureID);
	}

} //DockerEnvironmentImpl
