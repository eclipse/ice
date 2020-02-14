package org.eclipse.ice.modeling.workflowEngine;

public class Procedure {

	private Step[] steps;    // This should really be a List where steps can be added dynamically
	private CompleteCriteria completeCriteria;
	private int curIndex;
	private int numSteps;

	public Procedure() {
		System.out.println("Procedure() constructor");
		
		// Create a default set of steps
		// This should really be a List where steps can be added dynamically
		numSteps = 3;
		steps = new Step[numSteps];
		System.out.println( "##### BEGIN:  CREATE SETPS FOR PROCEDURE " );
		for (int i=0; i < 3; i++) {
			steps[i] = new Step();
			//System.out.println("   step " +i + " " + steps[i].getSuccess().getSuccessMsg());
		}
		System.out.println( "##### END:  CREATE SETPS FOR PROCEDURE " );
		
		// Seet the step index to 0 the first element.
		curIndex = 0;
		completeCriteria = new CompleteCriteria("Procedure Completion Criteria");
	}

	/**
	 * 
	 * @param curStep
	 */
	public Step nextStep(Step curStep) {
		System.out.println("Procedure.nextStep()");
		
		// Find the current step in the list of steps and
		// then return the next step
		if (curStep == null) {
			curIndex = 0;
			return steps[curIndex];
		}
		else {
			curIndex++;
			return steps[curIndex];
		}
	}
	

	/**
	 * 
	 * @param step
	 */
	public void addStep(Step step) {
		System.out.println("Procedure.addStep(Step step)");
		
		// take in a single step and append it to the list of current steps
	}

	/**
	 * This is not a smart function.  The caller could go right to the step success criteria
	 * themselves.  It woudl be better if this were simply an index or something.
	 * NEEDS WORK
	 * 
	 * @param currentStep
	 */
	public SuccessCriteria getStepSuccess(Step currentStep) {
		System.out.println("Procedure.getStepSuccess(Step currentStep)");
		
		return currentStep.getSuccess();   
	}

	public CompleteCriteria getCompleteCriteria() {
		return this.completeCriteria;
	}

	/**
	 * 
	 * @param completeCriteria
	 */
	public void setCompleteCriteria(CompleteCriteria criteria) {
		this.completeCriteria = criteria;
	}

}   // end class Procedure