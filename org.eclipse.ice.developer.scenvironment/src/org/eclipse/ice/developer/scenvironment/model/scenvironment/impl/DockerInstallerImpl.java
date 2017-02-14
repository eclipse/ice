/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.DockerInstaller;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Docker Installer</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerInstallerImpl extends MinimalEObjectImpl.Container implements DockerInstaller {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerInstallerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenvironmentPackage.Literals.DOCKER_INSTALLER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createDockerInstaller() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createInstaller() {
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
			case ScenvironmentPackage.DOCKER_INSTALLER___CREATE_DOCKER_INSTALLER:
				createDockerInstaller();
				return null;
			case ScenvironmentPackage.DOCKER_INSTALLER___CREATE_INSTALLER:
				createInstaller();
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //DockerInstallerImpl
