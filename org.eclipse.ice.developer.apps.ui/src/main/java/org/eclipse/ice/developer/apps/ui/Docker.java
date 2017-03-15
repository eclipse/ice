/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

/**
 * @author Anara Kozhokanova
 *
 */
public class Docker {
	private String name;
	private String ports;
	private String volumes;
	private String commands;
	private boolean ephemeral;
	
	public Docker() {
		name = "";  // set to empty, otherwise 'null' is shown in bound fields
		ports = "";
		volumes = "";
		commands = "";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPorts() {
		return ports;
	}
	public void setPorts(String ports) {
		this.ports = ports;
	}
	public String getVolumes() {
		return volumes;
	}
	public void setVolumes(String volumes) {
		this.volumes = volumes;
	}
	public String getCommands() {
		return commands;
	}
	public void setCommands(String commands) {
		this.commands = commands;
	}

	public boolean isEphemeral() {
		return ephemeral;
	}

	public void setEphemeral(boolean ephemeral) {
		this.ephemeral = ephemeral;
	}

}
