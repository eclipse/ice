/******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.tasks;

import org.eclipse.ice.data.IDataElement;

/**
 * 
 * Tasks are executed by workflows, either fully automated workflows (such as 
 * ICE workflows) or workflows controlled by intelligent agents without full 
 * automation.
 * 
 * Tasks combine Actions and data (templated on 
 * {@link org.eclipse.ice.data.IDataElement} classes). They also include 
 * properties and other metadata that may influence the way the task is 
 * executed.
 * 
 * In addition to actions, tasks use Hooks, a special type of Action that 
 * executes either before or after another Action to support the execution of 
 * that main Action. The list of supported hook types is provided below.
 * 
 * When directed to execute, tasks will execute all hooks and the action 
 * according to the execution order provided in the hook type table (see 
 * {@link TaskHookType}). Tasks support multiple hooks of each type since many 
 * different things may be required to support the execution of the main 
 * action. Hooks of the same type are executed as a queue (first-in-first-out). 
 * 
 * Tasks execute in one of several Task States (see {@link TaskState}) that are 
 * roughly correlated to hooks and action. Tasks start their life cycle with 
 * initialization and end with the execution of the action and any 
 * postprocessing hooks.
 * 
 * Tasks store state data externally and do not control their own storage. Thus
 * the must be configured when built to store state to the proper location. If
 * no state controller is provided, tasks will attempt to store state locally
 * by default. State storage is separate from data storage, which is tracked by
 * individual data models.
 * 
 * Tasks can be observed by listeners (see {@link ITaskListener}). Events are
 * dispatched asynchronously and listeners are not consulted for command and
 * control.
 * 
 * Data is injected into tasks using a setter instead of including it directly
 * in the run() operation. This supports option-operand separation and a
 * specific case of its use is in the execution of multiple tasks
 * simultaneously without the caller knowing anything about the data
 * configuration.
 * 
 * Thoughts on provenance tracking? ICE 2.0 used a log file.
 * 
 * They are modeled as the combination of an 
 * action and properties defining the way that action should be executed. Tasks 
 * may also be assigned conditional actions that evaluate when a certain 
 * condition has been met based on the execution of the primary action with its 
 * properties.
 * 
 * @author Jay Jay Billings
 *
 */
public interface ITask<T extends IDataElement<T>> {
	
	/**
	 * This operation sets the data on which the task should execute.
	 * @param taskData The data for the task
	 */
	public void setData(T taskData);
	
	/**
	 * This operation gets the data on which the task is working.
	 * @return the data
	 */
	public T getData();
	
	/**
	 * This function sets the Action that the task executes.
	 * @param taskAction the task's action.
	 */
	public void setAction(final Action<T> taskAction);
	
	/**
	 * This operation adds a hook to the task that will be executed in support
	 * of the action.
	 * @param hook a hook to execute before, after, or conditionally in place of 
	 * the action.
	 */
	public void addHook(final Hook<T> hook);
	
	/**
	 * This operation executes the action.
	 * @return the state of the task after exection.
	 */
	public TaskState execute();
	
	/**
	 * This operation returns the current state of the Task
	 * @return the state
	 */
	public TaskState getState();

}