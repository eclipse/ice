package org.eclipse.ice.modeling.workflowEngine;

public class MetaData {

	/**
	 * This is an identifier for the instrument that produced the data set.
	 */
	private String instrumentID;
	private String experimentID;
	private String runID;

	public MetaData() {
		System.out.println("MetaData() constructor");
		
		this.setInstrumentID("instrumentID");
		this.setExperimentID("experimentID");
		this.setRunID("runID");
	}

	/**
	 * @return the instrumentID
	 */
	public String getInstrumentID() {
		return instrumentID;
	}

	/**
	 * @param instrumentID the instrumentID to set
	 */
	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}

	/**
	 * @return the experimentID
	 */
	public String getExperimentID() {
		return experimentID;
	}

	/**
	 * @param experimentID the experimentID to set
	 */
	public void setExperimentID(String experimentID) {
		this.experimentID = experimentID;
	}

	/**
	 * @return the runID
	 */
	public String getRunID() {
		return runID;
	}

	/**
	 * @param runID the runID to set
	 */
	public void setRunID(String runID) {
		this.runID = runID;
	}

}