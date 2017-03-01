/**
 */
package apps.docker.impl;

import apps.docker.ContainerConfiguration;
import apps.docker.DockerAPI;
import apps.docker.DockerPackage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import com.spotify.docker.client.messages.ProgressMessage;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>API</b></em>'. <!-- end-user-doc -->
 *
 * @generated
 */
public class DockerAPIImpl extends MinimalEObjectImpl.Container implements DockerAPI {

	private DockerClient dockerClient;

	private boolean imageBuildSuccess = true;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.DOCKER_API;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean buildImage(String buildDir, String imagename) {

		try {
			dockerClient.build(Paths.get(buildDir), imagename, new ProgressHandler() {
				@Override
				public void progress(ProgressMessage message) throws DockerException {
					if (message.stream() != null) {
						System.out.println("[DockerBuild] " + message.stream());
					}
					if (message.error() != null) {
						System.out.println("Error in building image: " + message.error());
						DockerAPIImpl.this.imageBuildSuccess = false;
					}

				}
			});
		} catch (DockerException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		// If it wasn't successful, then
		// reset the flag and return false.
		if (!imageBuildSuccess) {
			imageBuildSuccess = true;
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean createContainer(String imageName, ContainerConfiguration config) {
		// Local Declarations
		String port = "";
		List<String> envs = new ArrayList<String>();
		final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
		List<PortBinding> randomPort = new ArrayList<>();
		randomPort.add(PortBinding.randomPort("0.0.0.0"));
		portBindings.put("22", randomPort);

		if (dockerClient != null) {

			HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

			// Create container with exposed ports
			ContainerConfig containerConfig = ContainerConfig.builder().image(imageName).hostConfig(hostConfig)
					.tty(true).attachStdout(true).attachStderr(true).exposedPorts(new String[] { "22" })
					.domainname("HELLO").build();

			ContainerInfo info = null;
			try {
				ContainerCreation creation;
				creation = dockerClient.createContainer(containerConfig, config.getName());
				String containerId = creation.id();
				config.setId(containerId);
				dockerClient.startContainer(containerId);

				// Query the info on the new container.
				info = dockerClient.inspectContainer(containerId);

			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}

			// Get the dynamically assigned port
			port = info.networkSettings().ports().get("22/tcp").get(0).hostPort();
			config.setRemoteSSHPort(Integer.valueOf(port));
			return true;
		}

		return false;

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean connectToExistingContainer(String id) {
		try {
			dockerClient.startContainer(id);
		} catch (DockerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean deleteImage(String imageName) {
		if (dockerClient != null) {
			try {
				dockerClient.removeImage(imageName);
			} catch (DockerException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public boolean stopContainer(String id) {
		if (dockerClient != null) {
			try {
				dockerClient.stopContainer(id, 2);
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public boolean deleteContainer(String id) {
		if (dockerClient != null) {
			try {
				dockerClient.removeContainer(id);
			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case DockerPackage.DOCKER_API___BUILD_IMAGE__STRING_STRING:
				return buildImage((String)arguments.get(0), (String)arguments.get(1));
			case DockerPackage.DOCKER_API___CREATE_CONTAINER__STRING_CONTAINERCONFIGURATION:
				return createContainer((String)arguments.get(0), (ContainerConfiguration)arguments.get(1));
			case DockerPackage.DOCKER_API___CONNECT_TO_EXISTING_CONTAINER__STRING:
				return connectToExistingContainer((String)arguments.get(0));
			case DockerPackage.DOCKER_API___DELETE_CONTAINER__STRING:
				return deleteContainer((String)arguments.get(0));
			case DockerPackage.DOCKER_API___DELETE_IMAGE__STRING:
				return deleteImage((String)arguments.get(0));
			case DockerPackage.DOCKER_API___STOP_CONTAINER__STRING:
				return stopContainer((String)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // DockerAPIImpl
