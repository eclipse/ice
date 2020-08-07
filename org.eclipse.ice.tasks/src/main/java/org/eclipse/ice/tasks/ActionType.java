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

/**
 * This enumeration is the master list of ActionTypes supported by Eclipse ICE
 * and defines the type of the action that will be executed. It distinguishes 
 * among types of actions such as shell functions, conditionals, languages, 
 * etc.
 * 
 * This is a hierarchical enumeration of action types that are organized
 * according to the ontology produced by the author as part of the work
 * "Ontological Considerations for Interoperability in Scientific Workflows."
 * As hierarchical enumerations in Java are not natively supported, this
 * enumeration engages in some trickery.
 * 
 * @author Jay Jay Billings
 *
 */
public interface ActionType {

	/**
	 * The basic action type is the base class for basic actions that are 
	 * typically considered native actions of workflow engines. This includes 
	 * actions such as moving files or doing simple reductions.
	 * @author Jay Jay Billings
	 *
	 */
	public enum BASIC implements ActionType {
		
		/**
		 * Actions of this type move files
		 */
		MOVE_FILE,
		
		/**
		 * Actions of this type are used for diagnostics or testing
		 */
		DIAGNOSTIC
		
	}
	
	/**
	 * The executable action type describes types of executables that are
	 * launched by actions.
	 * @author Jay Jay Billings
	 *
	 */
	public enum EXECUTABLE implements ActionType {
		
		/**
		 * Actions of this type are systems calls to local executables.
		 */
		LOCAL,
		
		/**
		 * Actions of this type launch remotely running executables.
		 */
		REMOTE
	}
	
	/**
	 * The function action type describes functions that can be executed by the
	 * Action.
	 * @author Jay Jay Billings
	 *
	 */
	public enum FUNCTION implements ActionType {
		
		/**
		 * Actions of this type call Java function.
		 */
		JAVA
		
	}
	
	/**
	 * This enumeration describes the the hooks supported by tasks, including
	 * preprocessing, postprocessing, error, and conditional hooks.
	 * 
	 * @author Jay Jay Billings
	 *
	 */
	public enum HOOK implements ActionType {
		
		/**
		 * Pre-hook type. Preprocessors for the main Action. These hooks run 
		 * before all other tasks except conditional tasks if their execution is 
		 * triggered.
		 */
		PRE,
		
		/**
		 * Post-hook type. Postprocessors for the main Action. These hooks run 
		 * last.
		 */
		POST,
		
		/**
		 * Error hook type. Error processors for unexpected faults, conditions, and 
		 * exceptions. These hooks only run when errors are encountered.
		 */
		ERROR,
		
		/**
		 * Conditional hook type. These hooks run when certain conditions are 
		 * satisfied and they directly influence the execution of the main action, 
		 * up to and including pre-empting and replacing it. These hooks are 
		 * executed before other hooks if their condition for execution is 
		 * satisfied.
		 */
		CONDITIONAL

	}
	
}
