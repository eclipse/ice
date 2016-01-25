/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.datastructures.docker;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.Image;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class DockerComponent extends MasterDetailsComponent {

	/**
	 * 
	 */
	private DockerClient dockerClient;

	/**
	 * 
	 */
	public DockerComponent() {
		super();

		// Local Declarations
		int counter = 1;
		ArrayList<MasterDetailsPair> pairs = new ArrayList<MasterDetailsPair>();

		try {
			dockerClient = DefaultDockerClient.fromEnv().build();
		} catch (DockerCertificateException e1) {
			e1.printStackTrace();
			logger.error("", e1);
		}

		// Create the Globals Component
		DataComponent global = new DataComponent();
		global.setName("Global Docker Parameters.");
		global.setDescription("Indicate whether to enable "
				+ "Docker in this Job Launch, and the host containing the docker client.");

		IEntry useDocker = new DiscreteEntry("yes", "no");
		useDocker.setDefaultValue("no");
		useDocker.setName("Use Docker");
		useDocker.setDescription("Indicate whether this job should use Docker containers.");
		useDocker.setId(1);

		global.addEntry(useDocker);

		setGlobalsComponent(global);

		try {
			for (Image i : dockerClient.listImages()) {
				MasterDetailsPair pair = new MasterDetailsPair();
				pair.setMaster(i.repoTags().get(0));
				DockerDetailsData data = new DockerDetailsData(i);
//				data.retrieveEntry("Container Name").register(this);
				pair.setDetails(data);
				pair.setMasterDetailsPairId(counter);
				pairs.add(pair);
			}
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
		}

		setTemplates(pairs);
		toggleAddRemoveButton(true);
	}

}
