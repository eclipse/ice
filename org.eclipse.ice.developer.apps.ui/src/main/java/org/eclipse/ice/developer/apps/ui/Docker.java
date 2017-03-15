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
	private String[] commands;
	private boolean ephemeral;
	
	public Docker() {
		
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
	public String getVolumens() {
		return volumes;
	}
	public void setVolumens(String volumens) {
		this.volumes = volumens;
	}
	public String[] getCommands() {
		return commands;
	}
	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public boolean isEphemeral() {
		return ephemeral;
	}

	public void setEphemeral(boolean ephemeral) {
		this.ephemeral = ephemeral;
	}

}
