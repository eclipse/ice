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
package org.eclipse.ice.commands.notification;

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
 * This class provides email updates for Command job statuses
 * 
 * @author Joe Osborn
 *
 */
public class EmailUpdateHandler implements ICommandUpdateHandler {

	// The email address to send the message to
	private String emailAddress = "";

	// The text that the message should contain
	private String emailText = "";

	// The subject of the message
	private String emailSubject = "";

	// The default commands api email that will send the message
	private String commandsEmail = "someemail@gmail.com";

	// The host smtp server for the commands api email address
	private String commandsHost = "smtp.gmail.com";

	
	private String commandsPassword = "password";
	
	/**
	 * Default constructor
	 */
	public EmailUpdateHandler() {
	}

	public void postUpdate() throws IOException {
		// Create some properties and setup the default gmail
		// server properties
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", commandsHost);
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true"); // TLS
		// Setup mail server
		properties.setProperty("mail.smtp.host", commandsHost);

		// Get the default Session object.
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(commandsEmail, commandsPassword);
			}
		});
		session.setDebug(true);

		try {
			// Create a default MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set the sender and recipient of the email
			message.setFrom(new InternetAddress(commandsEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

			// Give the email a subject and message content
			message.setSubject(emailSubject);
			message.setText(emailText);

			// Send message
			Transport.send(message);
			logger.info("Email sent successfully");
		} catch (MessagingException mex) {
			logger.error("Couldn't send email.", mex);
			throw new IOException();
		}

	}

	/**
	 * Setter for the email subject, see
	 * {@link org.eclipse.ice.commands.notification.EmailUpdateHandler#emailSubject}
	 * 
	 * @param emailSubject
	 */
	public void setSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	/**
	 * Setter for the email address, see
	 * {@link org.eclipse.ice.commands.notification.EmailUpdateHandler#emailAddress}
	 * 
	 * @param option
	 */
	public void setOption(String option) {
		this.emailAddress = option;
	}

	/**
	 * Setter for the email message, see
	 * {@link org.eclipse.ice.commands.notification.EmailUpdateHandler#emailText}
	 * 
	 * @param emailText
	 */
	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}

}
