package org.eclipse.ice.modeling.workflowEngine;

/**
 * Workflow is the entity that is responsible for managing the processing of a data set.
 * It is an abstract class which will be instantiated by a concrete class.  It is part
 * of composition pattern so clients only work with the Workflow interface and do not
 * need to know anything about the concrete classes or their interfaces.
 */
public class Workflow {

	private String wfState;
	private String wfID;
	private DataSet dataSet;
	private Procedure procedure;
	private Step currentStep;

	public Workflow() {
		System.out.println("Workflow() constructor");
		
		// initialize the attributes
		wfState = "";
		wfID    = "Workflow-ID";
	}   // end Workflow() constructor

	/**
	 * 
	 * @param msgIn
	 */
	public Message handleMsg(Message msgIn) {
		System.out.println("Workflow.nextMsg()");
		System.out.println("   msgIn: " + msgIn.getExpId());
		
		Message msgOut = new Message();
		msgOut.setExpId("Message out from WF");
		
		System.out.println("   msgOut: " + msgOut.getExpId());
		
		return msgOut;
	}   // end Workflow.nextMsg()

	public String getWfID() {
		return this.wfID;
	}

	/**
	 * 
	 * @param id
	 */
	public void setWfID(String id) {
		this.wfID = id;
	}

	/**
	 * @return the dataSet
	 */
	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @return the procedure
	 */
	public Procedure getProcedure() {
		return procedure;
	}

	/**
	 * @param procedure the procedure to set
	 */
	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	/**
	 * @return the currentStep
	 */
	public Step getCurrentStep() {
		return currentStep;
	}

	/**
	 * @param currentStep the currentStep to set
	 */
	public void setCurrentStep(Step currentStep) {
		this.currentStep = currentStep;
	}

}