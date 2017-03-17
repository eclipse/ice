/**
 * 
 */
package org.eclipse.ice.developer.apps.ui;

/**
 * Bean for the source package
 * @author Anara Kozhokanova
 *
 */
public class SourcePackage {
	private String link;
	private String branch;
	private String name;

	public SourcePackage() {
		link = "";      // needs to be set empty, otherwise 'null' is shown in bound fields
		branch = "";
		name = "";
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
