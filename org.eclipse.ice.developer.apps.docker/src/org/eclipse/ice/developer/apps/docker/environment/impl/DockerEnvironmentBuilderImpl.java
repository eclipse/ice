/**
 */
package org.eclipse.ice.developer.apps.docker.environment.impl;

import apps.IEnvironment;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.ice.developer.apps.docker.environment.DockerEnvironmentBuilder;
import org.eclipse.ice.developer.apps.docker.environment.EnvironmentFactory;
import org.eclipse.ice.developer.apps.docker.environment.EnvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Docker Environment Builder</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerEnvironmentBuilderImpl extends MinimalEObjectImpl.Container implements DockerEnvironmentBuilder {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerEnvironmentBuilderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EnvironmentPackage.Literals.DOCKER_ENVIRONMENT_BUILDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public IEnvironment build() {
		// Ensure that you remove @generated or mark it @generated NOT
		return EnvironmentFactory.eINSTANCE.createDockerEnvironment();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String name() {
		return "Docker";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EnvironmentPackage.DOCKER_ENVIRONMENT_BUILDER___BUILD:
				return build();
			case EnvironmentPackage.DOCKER_ENVIRONMENT_BUILDER___NAME:
				return name();
		}
		return super.eInvoke(operationID, arguments);
	}

} //DockerEnvironmentBuilderImpl
