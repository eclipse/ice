package org.eclipse.ice.datastructures.docker;

import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;

import com.spotify.docker.client.messages.Image;

public class DockerDetailsData extends DataComponent {

	private Image image;
	
	public DockerDetailsData(Image i) {
		// TODO Auto-generated constructor stub
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
		asDaemon.setDescription("Indicate whether this container should be launched as a background daemon.");
		asDaemon.setId(2);

		// List Entry
//		Entry volumes = new Entry() {
//			@Override
//			public void setup() {
//				//allowedValueType = AllowedValueType.List;
//				setName("Volumes");
//				setDescription("Provide any directories this container should expose.");
//				setId(3);
//			}
//			
//		};
		
		addEntry(name);
		addEntry(asDaemon);
//		addEntry(volumes);
		
	}

}
