/**
 */
package apps.docker.impl;

import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerPackage;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>API</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerAPIImpl extends MinimalEObjectImpl.Container implements DockerAPI {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerAPIImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.DOCKER_API;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void buildImage(String buildFile) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void launchContainer(String name, ContainerConfiguration config) {
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
			case DockerPackage.DOCKER_API___BUILD_IMAGE__STRING:
				buildImage((String)arguments.get(0));
				return null;
			case DockerPackage.DOCKER_API___LAUNCH_CONTAINER__STRING_CONTAINERCONFIGURATION:
				launchContainer((String)arguments.get(0), (ContainerConfiguration)arguments.get(1));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //DockerAPIImpl
