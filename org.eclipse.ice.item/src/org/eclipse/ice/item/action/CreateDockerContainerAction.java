package org.eclipse.ice.item.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ice.item.jobLauncher.DockerClientFactory;
import org.eclipse.january.form.FormStatus;
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

public class CreateDockerContainerAction extends RemoteExecutionAction {

	private DockerClient dockerClient;
	
	private String containerId;
	
	private IRemoteConnectionType connectionType;
	
	/**
	 * The constructor.
	 */
	public CreateDockerContainerAction() {
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
		
		// Local Declarations
		String imageName = dictionary.get("imageName");
		String port = "";
		List<String> envs = new ArrayList<String>();
		final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
		List<PortBinding> hostPorts = new ArrayList<PortBinding>();
		hostPorts.add(PortBinding.of("0.0.0.0", null));
		portBindings.put("22/tcp", hostPorts);
		envs.add("ROOT_PASS=password");
		
		// Now that we're executing, set the
		// status to Processing
		status = FormStatus.Processing;
		
		try {
			dockerClient = new DockerClientFactory().getDockerClient();
		} catch (DockerCertificateException | IOException | InterruptedException e1) {
			actionError("Error in getting a reference to Docker or listing available Images.", e1);
			return status;
		}

		if (dockerClient != null) {
		
			HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();
			
			// Create container with exposed ports
			ContainerConfig containerConfig = ContainerConfig.builder().image(imageName).hostConfig(hostConfig).env(envs)
					.tty(true).attachStdout(true).attachStderr(true)
					.exposedPorts(new String[] { "22" }).build();

			ContainerInfo info  = null;
			try {
				ContainerCreation creation = dockerClient.createContainer(containerConfig);
				containerId = creation.id();
				dockerClient.startContainer(containerId);

				// Query the info on the new container.
				info = dockerClient.inspectContainer(containerId);
				
			} catch (DockerException | InterruptedException e) {
				actionError("Error in creating the container.", e);
				return status;
			}

			// Get the dynamically assigned port
			port = info.networkSettings().ports().get("22/tcp").get(0).hostPort();

			// Get the hostname for the Docker container
			String hostName = dockerClient.getHost();
			dictionary.put("hostname", hostName);
			dictionary.put("remoteConnectionName", "Docker Host - " + hostName + ":" + port);
			dictionary.put("containerId", containerId);
			
			// Create the connection to the container
			IRemoteServicesManager remoteManager = getService(IRemoteServicesManager.class);

			// If valid, continue on an get the IRemoteConnection
			if (remoteManager != null) {

				// Get the connection type - basically Jsch is index 0
				connectionType = remoteManager.getRemoteConnectionTypes().get(0);
				IRemoteConnectionWorkingCopy workingCopy = null;

				try {
					workingCopy = connectionType.newConnection("Docker Host - " + hostName + ":" + port);
				} catch (RemoteConnectionException e) {
					actionError("Could not get the remote working copy.", e);
				}

				// Set up the Host Service
				IRemoteConnectionHostService service = workingCopy.getService(IRemoteConnectionHostService.class);
				service.setUsePassword(true);
				service.setHostname(hostName);
				service.setPassword("password");
				service.setUsername("root");
				service.setPort(Integer.valueOf(port));

				// Create the new IRemoteConnection
				try {
					connection = workingCopy.save();
					connection.open(new NullProgressMonitor());
				} catch (RemoteConnectionException e) {
					e.printStackTrace();
					actionError("Could not create the IRemoteConnection.", e);
					return status;
				}
			}
			
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
		return "Create Docker Container";
	}

	/**
	 * Private utility for getting string from InputStream.
	 * 
	 * @param stream
	 * @return
	 */
	private String streamToString(InputStream stream) {
		BufferedReader buff = new BufferedReader(new InputStreamReader(stream));
		StringBuffer res = new StringBuffer();
		String line = ""; //$NON-NLS-1$
		try {
			while ((line = buff.readLine()) != null) {
				res.append(System.getProperty("line.separator"));
				res.append(line);
			}
			buff.close();
		} catch (IOException e) {
		}
		return res.length() > 0 ? res.substring(1) : "";
	}
}
