package org.eclipse.ice.item.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.PortBinding;

public class DockerExecutionAction extends RemoteExecutionAction {

	/**
	 * The constructor.
	 */
	public DockerExecutionAction() {
		// Initialize the cancelled flag and
		// the form status.
		status = FormStatus.ReadyToProcess;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Now that we're executing, set the
		// status to Processing
		status = FormStatus.Processing;

		// Need to start a docker container and
		// get the port of SSH 22.

		// Set up the allowed values
		DockerClient dockerClient = null;
		try {
			dockerClient = DefaultDockerClient.fromEnv().build();
		} catch (DockerCertificateException e1) {
			e1.printStackTrace();
			logger.error("Error in getting a reference to Docker or listing available Images.", e1);
		}

		if (dockerClient != null) {

			String imageName = dictionary.get("imageName");
			String port = "", id = "";
			String[] ports = new String[] { "22" };

			final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
			for (String p : ports) {
				List<PortBinding> hostPorts = new ArrayList<PortBinding>();
				hostPorts.add(PortBinding.of("0.0.0.0", null));
				portBindings.put(p + "/tcp", hostPorts);
			}
			final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

			System.out.println("HELLO: " + hostConfig.portBindings().get("22/tcp").get(0));
			// Create container with exposed ports
			final ContainerConfig containerConfig = ContainerConfig.builder().image(imageName).hostConfig(hostConfig)
					.cmd("sh", "-c", "while :; do sleep 1; done").exposedPorts(new String[] { "22" }).build();

			containerConfig.attachStdout();
			containerConfig.attachStderr();

			ContainerCreation creation;

			try {

				creation = dockerClient.createContainer(containerConfig);

				id = creation.id();

				// Inspect container
				ContainerInfo info = dockerClient.inspectContainer(id);

				// Start container
				dockerClient.startContainer(id);

			} catch (DockerException | InterruptedException e) {
				e.printStackTrace();
				status = FormStatus.InfoError;
				return status;
			}

			// Get the docker port
			ProcessBuilder jobBuilder = new ProcessBuilder("docker", "port", id, "22");
			Process job = null;
			try {
				job = jobBuilder.start();
			} catch (IOException e1) {
				e1.printStackTrace();
				status = FormStatus.InfoError;
				return status;
			}
			InputStream stdOutStream = job.getInputStream();

			String result = new BufferedReader(new InputStreamReader(stdOutStream)).lines()
					.collect(Collectors.joining("\n"));

			port = result.split(":")[1];
			System.out.println("DOCKER PORT RESULT: " + port);

			String dockerHost = System.getenv("DOCKER_HOST");
			String hostName = dockerHost.split(":")[1].replace("/", "");

			System.out.println("DOCKER HOST IS " + hostName);
			dictionary.put("executable", "ls");
			dictionary.put("hostname", hostName);

			// Get the IRemoteServicesManager
			IRemoteServicesManager remoteManager = getService(IRemoteServicesManager.class);

			// If valid, continue on an get the IRemoteConnection
			if (remoteManager != null) {

				// Get the connection type - basically Jsch is index 0
				IRemoteConnectionType connectionType = remoteManager.getRemoteConnectionTypes().get(0);
				IRemoteConnectionWorkingCopy workingCopy = null;
				for (IRemoteConnection c : connectionType.getConnections()) {
					if (("Docker Host - " + hostName).equals(c.getName())) {
						connection = c;
					}
				}
				if (connection == null) {
					try {
						workingCopy = connectionType.newConnection("Docker Host - " + hostName);
					} catch (RemoteConnectionException e) {
						e.printStackTrace();
					}

					IRemoteConnectionHostService service = workingCopy.getService(IRemoteConnectionHostService.class);
					service.setUsePassword(true);
					service.setHostname(hostName);
					service.setPassword("password");
					service.setUsername("root");
					service.setPort(Integer.valueOf(port));

					try {
						connection = workingCopy.save();
						connection.open(new NullProgressMonitor());
					} catch (RemoteConnectionException e) {
						e.printStackTrace();
					}
				} 
			}
			status = super.execute(dictionary);
		} else {
			status = FormStatus.InfoError;
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#getActionName()
	 */
	@Override
	public String getActionName() {
		return "Docker Execution";
	}

}
