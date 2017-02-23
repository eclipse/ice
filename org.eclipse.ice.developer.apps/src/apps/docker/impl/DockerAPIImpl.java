/**
 */
package apps.docker.impl;

import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerPackage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ProgressMessage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>API</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerAPIImpl extends MinimalEObjectImpl.Container implements DockerAPI {
	
	private DockerClient dockerClient;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected DockerAPIImpl() {
		super();
		DefaultDockerClient.Builder builder = null;
		try {
			builder = DefaultDockerClient.fromEnv();
		} catch (DockerCertificateException e) {
			e.printStackTrace();
		}
		dockerClient = builder.build();
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
	 */
	public void buildImage(String buildDir, String imagename) {

		try {
			dockerClient.build(Paths.get(buildDir), imagename, new ProgressHandler() {
				@Override
				public void progress(ProgressMessage message) throws DockerException {
					final String imageId = message.buildImageId();
					System.out.println(imageId + ", " + message.progress() + ", " + message.toString());
				}
			});
		} catch (DockerException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void launchContainer(String name, ContainerConfiguration config) {
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void connectToExistingContainer(String id) {
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
			case DockerPackage.DOCKER_API___BUILD_IMAGE__STRING_STRING:
				buildImage((String)arguments.get(0), (String)arguments.get(1));
				return null;
			case DockerPackage.DOCKER_API___LAUNCH_CONTAINER__STRING_CONTAINERCONFIGURATION:
				launchContainer((String)arguments.get(0), (ContainerConfiguration)arguments.get(1));
				return null;
			case DockerPackage.DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING:
				connectToExistingContainer((String)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //DockerAPIImpl
