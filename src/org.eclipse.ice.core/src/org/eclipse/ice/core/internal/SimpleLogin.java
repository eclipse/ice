/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.core.internal;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * This class implements the LoginModule interface and provides a simple,
 * single-user authentication scheme for ICE. It gets the username and password
 * from the System properties.
 * 
 * This class does not provide a very secure authentication solution and it
 * should not be used when more secure options are available.
 * 
 * @author bkj
 * 
 */
public class SimpleLogin implements LoginModule {

	// The call-back handler that is used to retrieve the username and password
	private CallbackHandler handler;
	// The subject that is the source of the request
	private Subject subject;
	// The user, which is a principal of the subject
	private Principal user;

	/**
	 * This operation aborts the login attempt
	 */
	public boolean abort() throws LoginException {
		return true;
	}

	/**
	 * This operation commits the new principle to the subject.
	 */
	public boolean commit() throws LoginException {
		subject.getPrincipals().add(user);
		return true;
	}

	/**
	 * This operation creates and returns a new users
	 * 
	 * @param callbacks
	 *            The Callback from which the user information is retrieved
	 * @return
	 */
	private Principal createUser(final Callback[] callbacks) {

		// Return the new Principal
		return new Principal() {

			// Check the equality of this principal
			public boolean equals(Object obj) {
				if (!(obj instanceof Principal)) {
					return false;
				}
				return getName().equals(((Principal) obj).getName());
			}

			// Get the name of the principal from the Callback
			public String getName() {
				return ((NameCallback) callbacks[0]).getName();
			}

			// Get a hashcode for the principal
			public int hashCode() {
				return getName().hashCode();
			}

			// Serialize this principal as a string
			public String toString() {
				return getName().toString();
			}
		};
	}

	/**
	 * Initialize this LoginModule by setting the subject and the call-back
	 * handler.
	 */
	public void initialize(Subject subject, final CallbackHandler handler,
			Map arg2, Map arg3) {
		this.handler = handler;
		this.subject = subject;
	}

	/**
	 * Perform the login operation.
	 * 
	 * @return True if the user has valid credentials, false otherwise.
	 */
	public boolean login() throws LoginException {

		// The user name and password, hardwired for now
		String username = "ice", icePassword = "veryice";

		// Create the callbacks array
		final Callback[] callbacks = { new NameCallback("Username"),
				new PasswordCallback("Password", false) };

		// Have the handler fill the callbacks array with credentials
		try {
			handler.handle(callbacks);
		} catch (IOException e) {
			throw new LoginException(e.getMessage());
		} catch (UnsupportedCallbackException e) {
			throw new LoginException(e.getMessage());
		}

		// Get the user name and password
		String name = ((NameCallback) callbacks[0]).getName();
		String password = new String(
				((PasswordCallback) callbacks[1]).getPassword());

		// Check the user name and password
		if (username.equals(name) && icePassword.equals(password)) {
			user = createUser(callbacks);
			return true;
		} else {
			// Throw an exception if the user can not be authenticated
			throw new LoginException("Login failed");
		}
	}

	/**
	 * Logout the user out of the system
	 * 
	 * @return True if the user is logged out successfully, false otherwise
	 */
	public boolean logout() throws LoginException {
		return true;
	}
}
