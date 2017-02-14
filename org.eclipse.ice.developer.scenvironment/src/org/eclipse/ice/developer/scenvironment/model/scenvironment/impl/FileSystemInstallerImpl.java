/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.ice.developer.scenvironment.model.scenvironment.FileSystemInstaller;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File System Installer</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class FileSystemInstallerImpl extends MinimalEObjectImpl.Container implements FileSystemInstaller {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FileSystemInstallerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenvironmentPackage.Literals.FILE_SYSTEM_INSTALLER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createFSInstaller() {
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
			case ScenvironmentPackage.FILE_SYSTEM_INSTALLER___CREATE_FS_INSTALLER:
				createFSInstaller();
				return null;
			case ScenvironmentPackage.FILE_SYSTEM_INSTALLER___CREATE_INSTALLER:
				createInstaller();
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //FileSystemInstallerImpl
