/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class provides email updates for Command job statuses. It takes a
 * provided email address, password, etc. and just sends an email to the same
 * address
 * 
 * @author Joe Osborn
 *
 */
public class EmailUpdateHandler implements ICommandUpdateHandler {

	// The text that the message should contain
	private String emailText = "";

	// The subject of the message
	private String emailSubject = "Commands API Message";

	// Text file credential handler for sender's email creds
	TxtFileConnectionAuthorizationHandler credHandler;
	
	/// Mail port to email over, depends on client
	private String mailPort = "25";

	/**
	 * Default constructor
	 */
	public EmailUpdateHandler() {
	}

	/**
	 * See {@link org.eclipse.ice.commands.ICommandUpdateHandler#postUpdate()}
	 */
	@Override
	public void postUpdate() throws IOException {
		// Create some properties and setup the default 
		// server properties
		Properties properties = System.getProperties();
		properties.put("mail.smtp.port", mailPort);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true"); // TLS
		// Setup mail server
		properties.setProperty("mail.smtp.host", credHandler.getHostname());
	
		// Get the default Session object.
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(credHandler.getUsername(), 
						                          String.valueOf(credHandler.getPassword()));
			}
		});
		
		// Set session to debug just to be explicit
		session.setDebug(true);

		try {
			// Create a default MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set the sender and recipient of the email
			message.setFrom(new InternetAddress(credHandler.getUsername()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(credHandler.getUsername()));

			// Give the email a subject and message content
			message.setSubject(emailSubject);
			message.setText(emailText);

			Transport.send(message);
			logger.info("Email sent successfully");
		} catch (MessagingException mex) {
			logger.error("Couldn't send email.", mex);
			throw new IOException();
		}

	}

	/**
	 * Setter for the email subject, see
	 * {@link org.eclipse.ice.commands.EmailUpdateHandler#emailSubject}
	 * 
	 * @param emailSubject
	 */
	public void setSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	/**
	 * Setter for the text file credential handler, see
	 * {@link org.eclipse.ice.commands.EmailUpdateHandler#credHandler}
	 * 
	 * @param credHandler
	 */
	public void setCredHandler(TxtFileConnectionAuthorizationHandler credHandler){
		this.credHandler = credHandler;
	}

	/**
	 * See {@link org.eclipse.ice.commands.ICommandUpdateHandler#setMessage(String)}
	 */
	@Override
	public void setMessage(String message) {
		this.emailText = message;
	}
	
	/**
	 * Setter for mail port that depends on email client used
	 * @param mailPort
	 */
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

}
