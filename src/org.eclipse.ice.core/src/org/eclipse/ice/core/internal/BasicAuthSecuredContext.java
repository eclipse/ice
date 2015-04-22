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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.osgi.service.http.HttpContext;

/**
 * This class implements the HttpContext interface and provides basic
 * authentication for single users to the ICE Core server.
 * 
 * This class is based on and largely taken from the code in OSGi and Equinox:
 * Creating Highly Modular Java Systems.
 * 
 * @author Jay Jay Billings
 * 
 */
public class BasicAuthSecuredContext implements HttpContext {

	/**
	 * A URL to the resource directory where files and data are stored for the
	 * plugin.
	 */
	private URL resourceBase;

	/**
	 * A URL to the JAAS configuration file where the identity of the login
	 * module is provided.
	 */
	private URL configFile;

	/**
	 * The HTTP realm used in authentication.
	 */
	private String realm;

	/**
	 * The constructor
	 * 
	 * @param resourceBase
	 *            A URL to the resource directory where files and data are
	 *            stored for the plugin.
	 * @param configFile
	 *            A URL to the JAAS configuration file where the identity of the
	 *            login module is provided.
	 * @param realm
	 *            The HTTP realm used in authentication.
	 */
	public BasicAuthSecuredContext(URL resourceBase, URL configFile,
			String realm) {

		// Set the resource base location, the location of the configuration
		// file and the HTTP realm.
		this.resourceBase = resourceBase;
		this.configFile = configFile;
		this.realm = realm;

		// Print some diagnostic information.
		System.out.println("ICore Message: Resource URL = "
				+ resourceBase.getPath());
		System.out.println("ICore Message: Authentication config "
				+ "file URL = " + configFile.getPath());

	}

	/**
	 * A utility operation that is used to set the response code and header if
	 * the authentication fails.
	 * 
	 * @param request
	 *            The request that failed authentication.
	 * @param response
	 *            The response to describe why authentication failed.
	 * @return
	 */
	private boolean failAuthorization(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("ICore Message: Basic Authentication failed!");

		// Force a session to be created
		request.getSession(true);
		// Configure the header
		response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
		// Send the response error.
		try {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (IOException e) {
			// Print the stack trace if the error can not be sent.
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Empty implementation of getMimeType.
	 */
	public String getMimeType(String name) {
		return null;
	}

	/**
	 * Get a resource by name
	 */
	public URL getResource(String name) {
		try {
			return new URL(resourceBase, name);
		} catch (MalformedURLException e) {
			System.out.println("Unable to create resource URL");
			return null;
		}
	}

	/**
	 * This operation handles the authentication request and calls login() to
	 * handle the actual credentials.
	 * 
	 */
	public boolean handleSecurity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// Make sure an authorization header is available
		String auth = request.getHeader("Authorization");

		// Fail if not
		if (auth == null) {
			return failAuthorization(request, response);
		}
		// Determine the authentication schemed
		StringTokenizer tokens = new StringTokenizer(auth);
		String authscheme = tokens.nextToken();

		// Fail if HTTP Basic authentication is not used
		if (!("Basic".equals(authscheme))) {
			return failAuthorization(request, response);
		}

		// Decode the credentials, which are encoded in Base64
		String base64credentials = tokens.nextToken();
		String credentials = new String(Base64.decodeBase64(base64credentials
				.getBytes()));

		// The userid is on the left side of the colon and the password on the
		// right
		int colon = credentials.indexOf(':');
		String userid = credentials.substring(0, colon);
		String password = credentials.substring(colon + 1);

		// Try to log in with the credentials
		Subject subject = null;
		try {
			subject = login(request, userid, password);
		} catch (LoginException e) {
			e.printStackTrace();
			return failAuthorization(request, response);
		}

		// Set the attributes on the HTTP request
		request.setAttribute(HttpContext.REMOTE_USER, userid);
		request.setAttribute(HttpContext.AUTHENTICATION_TYPE, authscheme);
		request.setAttribute(HttpContext.AUTHORIZATION, subject);

		return true;
	}

	/**
	 * This operation checks the credentials.
	 * 
	 * @param request
	 *            The HTTP request
	 * @param userid
	 *            The userid
	 * @param password
	 *            The password
	 * @return A Login Subject or null if authentication failed
	 * @throws LoginException
	 */
	private Subject login(HttpServletRequest request, final String userid,
			final String password) throws LoginException {

		// Make sure the session is valid
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		// Get the LoginContext that is configured for authentication
		ILoginContext context = (ILoginContext) session
				.getAttribute("securitycontext");
		if (context != null) {
			return context.getSubject();
		}
		// Create a login context if one does not exist. The Login context must
		// be available as a plugin to the Equinox Extension Registry and
		// provide an implementation of LoginModule.
		context = LoginContextFactory.createContext("SimpleConfig", configFile,
				new CallbackHandler() {
					// Provide a callback handler to check the user name and
					// password
					public void handle(Callback[] callbacks)
							throws IOException, UnsupportedCallbackException {
						for (int i = 0; i < callbacks.length; i++) {
							if (callbacks[i] instanceof NameCallback) {
								((NameCallback) callbacks[i]).setName(userid);
							} else if (callbacks[i] instanceof PasswordCallback) {
								((PasswordCallback) callbacks[i])
										.setPassword(password.toCharArray());
							} else {
								throw new UnsupportedCallbackException(
										callbacks[i]);
							}
						}
					}
				});
		// Try the login and set the context on the session if it works
		Subject result = context.getSubject();
		session.setAttribute("securitycontext", context);

		return result;
	}
}
