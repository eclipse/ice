/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.data;

import java.io.Serializable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class provides a simple utility for checking that the data in objects is
 * valid and meets some basic expectations. For example, it can be used to check
 * that numbers lie within certain bounds or that strings are spelled correctly.
 * 
 * All validation in this class is performed using a Javascript function that is
 * injected with the function accessors. All clients are expected to configure a
 * validation function by passing a Javascript function in the form of a string
 * to the setFunction() operation. The function signature is of the form "var
 * checkData = function (data) {return data == 'Solar Fields';}" and this class
 * expects to be able to call the checkData() function by name.
 * 
 * Clients should provide functions that, in general, perform both verification
 * and validation. That is, functions should verify that data exists within
 * expected parameters and insure that the values provided are accurate in a
 * larger context and conform to business rules.
 * 
 * Future ideas: 1) Can we take Javascript function objects instead of strings?
 * 2) Do we need to create a script engine for *every* validator? Most likely
 * not! 3) Can we get feedback from Javascript functions to identify what the
 * error was? 4) Can we read functions from files too? 5) Can we inject a
 * function name to call instead of defaulting to checkData()?
 * 
 * @author Jay Jay Billings
 *
 */
public class JavascriptValidator implements Serializable {

	/**
	 * Logging tool
	 */
	private static final Logger logger = LoggerFactory.getLogger(JavascriptValidator.class);

	/**
	 * An id for the Serializable interface implementation.
	 */
	private static final long serialVersionUID = 4748960154143122573L;

	/**
	 * A valid Javascript function, stored as a string, that can be called when the
	 * validate() operation is executed.
	 */
	private String function;

	/**
	 * The script engine manager for executing Javascript scripts
	 */
	@JsonIgnore
	ScriptEngineManager scriptEngineManager;

	/**
	 * The Nashorn Javascript engine
	 */
	@JsonIgnore
	ScriptEngine engine;

	/**
	 * Constructor
	 */
	public JavascriptValidator() {
		setFunction(new String());
		setupScriptEngine();
	}

	/**
	 * Copy constructor
	 * @param otherValidator to copy
	 */
	public JavascriptValidator(JavascriptValidator otherValidator) {
		if (otherValidator != null) {
			function = otherValidator.function;
		}
		// Still need to setup the scripting engine if we copy it.
		setupScriptEngine();
	}

	/**
	 * This function sets up the Nashorn scripting engine.
	 */
	private void setupScriptEngine() {
		scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("JavaScript");
	}

	/**
	 * This function returns the Javascript function that will be executed as a
	 * string.
	 * 
	 * @return the Javascript function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * This operation sets the validation function from a Javascript function stored
	 * as a string.
	 * 
	 * @param function a Javascript function stored as a string that can be called
	 *                 by the validate() operation.
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * See {@link java.lang.Object#equals(Object)}.
	 */
	@Override
	public boolean equals(Object otherObject) {

		boolean retValue = false;

		// Check shallow identify and type first
		if (this == otherObject) {
			retValue = true;
		} else if (otherObject instanceof JavascriptValidator) {
			@SuppressWarnings("unchecked")
			JavascriptValidator otherValidator = (JavascriptValidator) otherObject;
			retValue = this.function.equals(otherValidator.function);
		}

		return retValue;
	}

	/**
	 * This operation checks the data for validity.
	 * 
	 * @param data the data to check
	 * @return true if the data is in a valid state, false otherwise
	 * @throws NoSuchMethodException This exception is thrown if the Javascript
	 *                               validation function cannot be found.
	 */
	public boolean validate(final IDataElement data) throws NoSuchMethodException {

		boolean retValue = false;
		Object result = null;

		try {
			engine.eval(function);
			Invocable invocableEngine = (Invocable) engine;
			result = invocableEngine.invokeFunction("checkData", data);
			retValue = (boolean) result;
		} catch (ScriptException e) {
			logger.error("Error running validation function!", e);
		}

		return retValue;
	}

	/**
	 * See {@link java.lang.Object#hashCode()}.
	 */
	@Override
	public int hashCode() {
		// Using a somewhat generic and common technique for computing the hash code
		// here. It matches the version in the old ICE 2.x product line, but I
		// incremented the initial hash seed to 31 from 11 since this is for version 3.
		int hash = 31;
		// The 31 below is just coincidental and part of the original source where I
		// read
		// about hash codes.
		hash = 31 * hash + function.hashCode();

		return hash;
	}

	/**
	 * This operation clones the object. Note that it differs from the base class
	 * implementation in that it will return null if it cannot create the clone to
	 * promote fast failure. See {@link java.lang.Object#clone()};
	 */
	@Override
	public Object clone() {
		try {
			// Call the copy constructor to create the clone.
			return new JavascriptValidator(this);
		} catch (Exception e) {
			logger.error("Unable to clone DataElement!", e);
			return null;
		}
	}

}
