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

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.MultiValueEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;

import com.spotify.docker.client.messages.Image;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class DockerDetailsData extends DataComponent {

	private Image image;
	
	/**
	 * 
	 * @param i
	 */
	public DockerDetailsData(Image i) {
		image = i;
		setName(image.repoTags().get(0) + " Parameters");
		setDescription("Please provide container-specific parameters.");
		
		IEntry name = new StringEntry();
		name.setDefaultValue(image.repoTags().get(0));
		name.setName("Container Name");
		name.setDescription("The alias of this container when it is created.");
		name.setId(1);
	
		IEntry asDaemon = new DiscreteEntry("yes", "no");
		asDaemon.setDefaultValue("no");
		asDaemon.setName("Daemon");
		asDaemon.setDescription("Indicate whether this container should be launched as a background daemon (docker run -d).");
		asDaemon.setId(2);

		IEntry interactive = new DiscreteEntry("yes", "no");
		interactive.setName("Interactive");
		interactive.setDefaultValue("no");
		interactive.setDescription("Pass -i to docker run, interactive");
		interactive.setId(3);
		
		IEntry tty = new DiscreteEntry("yes", "no");
		interactive.setName("Allocate TTY");
		interactive.setDefaultValue("no");
		interactive.setDescription("Pass -t to docker run, allocate tty");
		interactive.setId(4);

		// List Entry
		MultiValueEntry volumes = new MultiValueEntry();
		volumes.setName("Volumes");
		volumes.setDescription("Provide any directories this container should expose.");
		volumes.setId(5);
		
		DiscreteEntry volumesFrom = new DiscreteEntry("No volumes to choose from.");
		volumesFrom.setName("Volumes From");
		volumesFrom.setDescription("Specify volumes to mount in this image from another image.");
		volumesFrom.setId(6);
		
		addEntry(name);
		addEntry(asDaemon);
		addEntry(interactive);
		addEntry(tty);
		addEntry(volumes);
		addEntry(volumesFrom);
	}

}
