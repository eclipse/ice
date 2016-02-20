/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton (UT-Battelle, LLC.) - Initial API and implementation and/or
 *     initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.test.utils.AbstractSWTTester;
import org.eclipse.eavp.viz.service.widgets.ComboDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class provides automated UI tests for the {@link ComboDialog}.
 * 
 * @author Jordan Deyton
 *
 */
// TODO Figure out why these tests won't pass on the build server....
@Ignore
public class ComboDialogTester extends AbstractSWTTester {

	/**
	 * A reference to the dialog that will be tested. It will need to be created
	 * and opened for each test, as well as closed afterward.
	 */
	private ComboDialog dialog;

	/**
	 * The SWTBot for the {@link #dialog}'s shell.
	 */
	private SWTBot bot;

	/**
	 * A list of allowed values. Contains 1, 2, 3, 1, and 4, in that order.
	 */
	private List<String> allowedValues;

	// TODO Figure out how we can check the error decorator on the combo widget.

	/**
	 * Creates an SWTBot for the dialog's shell, not for the test shell.
	 */
	@Override
	protected SWTBot getBot() {
		// Get the shell for the dialog, then return its associated bot.
		for (SWTBotShell shell : super.getBot().shells()) {
			if (shell.widget == dialog.getShell()) {
				return shell.bot();
			}
		}
		return null;
	}

	/**
	 * Sets up the {@link #allowedValues}.
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

		allowedValues = new ArrayList<String>();
		allowedValues.add("1");
		allowedValues.add("2");
		allowedValues.add("3");
		allowedValues.add("1");
		allowedValues.add("4");

		return;
	}

	/**
	 * Checks the default appearance for
	 */
	@Test
	public void checkDefaults() {
		// Create a basic, default dialog.
		dialog = new ComboDialog(getShell(), false);

		// Open the dialog and get an SWTBot for it.
		openDialog();

		// Get the widgets.
		SWTBotLabel label = getInfoLabel();
		SWTBotCombo combo = getCombo();
		SWTBotButton okButton = getOKButton();
		SWTBotButton cancelButton = getCancelButton();

		// Check the default appearance.
		assertEquals("", getBot().activeShell().getText()); // Check the title.
		assertEquals("Please select a value.", label.getText());
		assertEnabled(combo);
		assertEquals("", getCombo().getText());
		assertEquals(0, getCombo().itemCount());
		assertNotEnabled(okButton);
		assertEnabled(cancelButton);
		// Check the default value.
		assertNull(dialog.getValue());

		// Close the dialog.
		closeDialog();

		return;
	}

	/**
	 * Checks that the title can be changed from the default as well as the
	 * associated method's return value.
	 */
	@Test
	public void checkSetTitle() {

		final String nullString = null;

		dialog = new ComboDialog(getShell(), false);

		// Trying to set a new title should return true. Trying to set the same
		// title should return false.
		assertTrue(dialog.setTitle("bill and ted's"));
		assertTrue(dialog.setTitle("bogus journey"));
		assertFalse(dialog.setTitle("bogus journey"));
		// Trying to set an invalid title should return false.
		assertFalse(dialog.setTitle(nullString));

		openDialog();

		// Check the title.
		assertEquals("bogus journey", getBot().activeShell().getText());

		closeDialog();

		return;
	}

	/**
	 * Checks that the info label's text can be changed from the default as well
	 * as the associated method's return value.
	 */
	@Test
	public void checkSetInfoLabelText() {

		final String nullString = null;

		dialog = new ComboDialog(getShell(), false);

		// Trying to set a new title should return true. Trying to set the same
		// title should return false.
		assertTrue(dialog.setInfoText("bill and ted's"));
		assertTrue(dialog.setInfoText("bogus journey"));
		assertFalse(dialog.setInfoText("bogus journey"));
		// Trying to set an invalid title should return false.
		assertFalse(dialog.setInfoText(nullString));

		openDialog();

		// Check the info label text.
		assertEquals("bogus journey", getInfoLabel().getText());

		closeDialog();

		return;
	}

	/**
	 * Checks that setting the allowed values properly updates the contents of
	 * the underlying combo widget.
	 */
	@Test
	public void checkSetAllowedValues() {

		// Set up some invalid input.
		List<String> listWithNulls = new ArrayList<String>(allowedValues);
		List<String> nullList = null;
		listWithNulls.add(2, null);

		dialog = new ComboDialog(getShell(), false);

		// Trying to set a valid list of allowed values should return true.
		// Trying to set the same set or invalid sets should return false.
		assertTrue(dialog.setAllowedValues(allowedValues));
		assertFalse(dialog.setAllowedValues(allowedValues));
		assertFalse(dialog.setAllowedValues(listWithNulls));
		assertFalse(dialog.setAllowedValues(nullList));

		openDialog();

		SWTBotCombo combo = getCombo();

		// The combo should be enabled.
		assertEnabled(combo);
		// Check all of the items in the combo.
		assertEquals(allowedValues.size(), combo.itemCount());
		String[] items = combo.items();
		for (int i = 0; i < items.length; i++) {
			assertEquals(allowedValues.get(i), items[i]);
		}

		closeDialog();

		return;
	}

	/**
	 * Checks that if the read-only flag is set to false, the combo's style is
	 * editable.
	 */
	@Test
	public void checkEditableCombo() {

		dialog = new ComboDialog(getShell(), false);

		openDialog();

		final AtomicInteger styleRef = new AtomicInteger();

		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				styleRef.set(getCombo().widget.getStyle());
			}
		});

		// Check the style bit for an editable list. It must not have the
		// READ_ONLY bit set.
		assertFalse(SWT.READ_ONLY == (styleRef.get() & SWT.READ_ONLY));

		closeDialog();

		return;
	}

	/**
	 * Checks that if the read-only flag is set to true, the combo's style is
	 * read only.
	 */
	@Test
	public void checkReadOnlyCombo() {

		dialog = new ComboDialog(getShell(), true);

		openDialog();

		final AtomicInteger styleRef = new AtomicInteger();

		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				styleRef.set(getCombo().widget.getStyle());
			}
		});

		// Check the style bit for a read-only list. It must have the READ_ONLY
		// bit set.
		assertTrue(SWT.READ_ONLY == (styleRef.get() & SWT.READ_ONLY));

		closeDialog();

		return;
	}

	/**
	 * Checks that the value is {@code null} if cancelled, otherwise it should
	 * be the selected value from the combo.
	 */
	@Test
	public void checkGetValueEditable() {

		// Create a new dialog. Accept an additional value besides the allowed
		// values.
		dialog = new ComboDialog(getShell(), false) {
			@Override
			protected String validateSelection(String selection) {
				String validatedText = super.validateSelection(selection);
				if (validatedText == null && "wyld stallyns".equals(selection)) {
					validatedText = selection;
				}
				return validatedText;
			}
		};
		dialog.setAllowedValues(allowedValues);

		// Try selecting one of the allowed values.
		openDialog();
		getCombo().setSelection(1);
		getOKButton().click();
		assertEquals(allowedValues.get(1), dialog.getValue());

		// Clicking cancel should set the value to null.
		openDialog();
		getCombo().setSelection(1);
		getCancelButton().click();
		assertNull(dialog.getValue());

		// Try a custom text value.
		openDialog();
		getCombo().setText("wyld stallyns");
		getOKButton().click();
		assertEquals("wyld stallyns", dialog.getValue());

		return;
	}

	/**
	 * Checks that the value is {@code null} if cancelled, otherwise it should
	 * be the selected value from the combo.
	 */
	@Test
	public void checkGetValueReadOnly() {

		// Create a new dialog. Accept an additional value besides the allowed
		// values.
		dialog = new ComboDialog(getShell(), true);
		dialog.setAllowedValues(allowedValues);

		// Try selecting one of the allowed values.
		openDialog();
		getCombo().setSelection(2);
		getOKButton().click();
		assertEquals(allowedValues.get(2), dialog.getValue());

		// Clicking cancel should set the value to null.
		openDialog();
		getCombo().setSelection(1);
		getCancelButton().click();
		assertNull(dialog.getValue());

		return;
	}

	/**
	 * Checks that the validate operation is actually called when validating the
	 * text, so that sub-classes may override its behavior.
	 */
	@Test
	public void checkValidateTextCalled() {

		final AtomicReference<String> validatedText = new AtomicReference<String>();

		dialog = new ComboDialog(getShell(), false) {
			@Override
			protected String validateSelection(String selection) {
				validatedText.set(super.validateSelection(selection));
				return validatedText.get();
			}
		};
		dialog.setAllowedValues(allowedValues);

		// Open the dialog and type an allowed value in the text field. The
		// new value should have been validated (which sets the
		// AtomicReference).
		openDialog();
		getCombo().typeText("4");
		closeDialog();
		assertNotNull(validatedText.get());
		assertEquals("4", validatedText.get());

		return;
	}

	/**
	 * Checks that the OK button is initially disabled under the following
	 * circumstances:
	 * <ul>
	 * <li>the combo is read-only and has no allowed values</li>
	 * <li>the combo is editable and has an invalid initial value</li>
	 * </ul>
	 */
	@Test
	public void checkOKDisabledOnOpen() {

		// Read-only, no allowed values.
		dialog = new ComboDialog(getShell(), true);
		openDialog();
		assertNotEnabled(getOKButton());
		closeDialog();

		// Editable, invalid initial value.
		dialog = new ComboDialog(getShell(), false);
		dialog.setAllowedValues(allowedValues);
		dialog.setInitialValue("blah");
		openDialog();
		assertNotEnabled(getOKButton());
		closeDialog();

		return;
	}

	/**
	 * Checks that the combo's initial value is set properly.
	 * <ul>
	 * <li>if the combo is read-only:
	 * <ul>
	 * <li>if the initial value is valid, it uses the initial value</li>
	 * <li>if the initial value is invalid, it uses the first available value
	 * </li>
	 * </ul>
	 * </li>
	 * <li>if the combo is editable, it should be either the initial value
	 * <ul>
	 * <li>if the initial value is <i>set</i>, it uses the initial value</li>
	 * <li>if the initial value is <i>unset</i>, it uses the first available
	 * value</li>
	 * </ul>
	 * </li>
	 * </ul>
	 */
	@Test
	public void checkInitialValue() {

		// Read-only with valid initial value -- should use specified value.
		dialog = new ComboDialog(getShell(), true);
		dialog.setAllowedValues(allowedValues);
		dialog.setInitialValue("4");

		openDialog();
		assertEquals("4", getCombo().getText());
		closeDialog();

		// Read-only with invalid initial value -- should use first value.
		dialog = new ComboDialog(getShell(), true);
		dialog.setAllowedValues(allowedValues);
		assertFalse(dialog.setInitialValue("excellent adventure"));

		openDialog();
		assertEquals("1", getCombo().getText());
		closeDialog();

		// Editable with valid initial value -- should use specified value.
		dialog = new ComboDialog(getShell(), false);
		dialog.setAllowedValues(allowedValues);
		assertTrue(dialog.setInitialValue("4"));

		openDialog();
		assertEquals("4", getCombo().getText());
		closeDialog();

		// Editable with invalid initial value -- should use specified value.
		dialog = new ComboDialog(getShell(), false);
		dialog.setAllowedValues(allowedValues);
		assertTrue(dialog.setInitialValue("excellent adventure"));

		openDialog();
		assertEquals("excellent adventure", getCombo().getText());
		closeDialog();

		// Editable with no initial value -- should use first value.
		dialog = new ComboDialog(getShell(), false);
		dialog.setAllowedValues(allowedValues);

		openDialog();
		assertEquals("1", getCombo().getText());
		closeDialog();

		return;
	}

	/**
	 * Unsets the {@link #bot} and opens the current {@link #dialog} on the UI
	 * thread.
	 */
	private void openDialog() {
		// We should unset the bot as the old bot will no longer apply.
		bot = null;
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				dialog.setBlockOnOpen(false);
				dialog.open();
			}
		});
	}

	/**
	 * Closes the {@link #dialog} on the UI thread.
	 */
	private void closeDialog() {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				dialog.close();
			}
		});
	}

	/**
	 * Gets the dialog's info label.
	 * 
	 * @return The associated widget wrapped by SWTBot.
	 */
	private SWTBotLabel getInfoLabel() {
		return getBot().label();
	}

	/**
	 * Gets the dialog's combo widget.
	 * 
	 * @return The associated widget wrapped by SWTBot.
	 */
	private SWTBotCombo getCombo() {
		return getBot().comboBox();
	}

	/**
	 * Gets the dialog's OK button.
	 * 
	 * @return The associated widget wrapped by SWTBot.
	 */
	private SWTBotButton getOKButton() {
		return getBot().button(0);
	}

	/**
	 * Gets the dialog's Cancel button.
	 * 
	 * @return The associated widget wrapped by SWTBot.
	 */
	private SWTBotButton getCancelButton() {
		return getBot().button(1);
	}
}
