/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

/**
 * Bean for the folder
 * @author Anara Kozhokanova
 *
 */
public class Folder {
	private String directory;
	/**
	 * 
	 */
	public Folder() {
		directory = ""; // set empty, otherwise 'null' is shown in its bound field
	}
	
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String dir) {
		this.directory = dir;
	}

}
