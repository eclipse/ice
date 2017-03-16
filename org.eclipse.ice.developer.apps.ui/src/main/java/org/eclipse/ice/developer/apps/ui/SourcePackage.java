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

	/**
	 * 
	 */
	public SourcePackage() {
		link = "";      // needs to set empty, otherwise 'null' is shown in bound fields
		branch = "";
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link, link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * @param branch, branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}

}
