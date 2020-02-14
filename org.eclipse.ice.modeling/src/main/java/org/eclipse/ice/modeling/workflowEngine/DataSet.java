package org.eclipse.ice.modeling.workflowEngine;

public class DataSet {

	/**
	 * This is a reference or a link to a file that contains the actual raw data that needs or is undergoing processing
	 */
	private String rawDataRef;
	private MetaData metaData;

	public DataSet() {
		System.out.println("DataSet() constructor");
		
		metaData   = new MetaData();
		rawDataRef = "Raw Data File Reference";
	}   // end DataSet() constructor

	/**
	 * @return the rawDataRef
	 */
	public String getRawDataRef() {
		return rawDataRef;
	}   // end DataSet.getRawDataRef()

	/**
	 * @param rawDataRef the rawDataRef to set
	 */
	public void setRawDataRef(String rawDataRef) {
		this.rawDataRef = rawDataRef;
	}   // end DataSet.setRawDataRef(String rawDataRef)

	/**
	 * @return the metaData
	 */
	public MetaData getMetaData() {
		return metaData;
	}   // end DataSet.getMetaData()

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}   // end DataSet.setMetaData(MetaData metaData)

}   // end class DataSet