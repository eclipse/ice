package org.eclipse.ice.tests.data;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.ice.data.JavascriptValidator;
import org.junit.jupiter.api.Test;

/**
 * This class is responsible for testing the ability of the package to do
 * Javascript-based validation of data classes.
 * 
 * @author Jay Jay Billings
 *
 */
class JavascriptValidatorTest {

	/**
	 * This function tests that Javascript functions can be correctly set on the
	 * validator and that validation can be run successfully. Since functions are
	 * simply strings, the first part of the test is a rudimentary accessor test
	 * only.
	 */
	@Test
	void testValidation() {
		// Check function accessors
		JavascriptValidator<String> validator = new JavascriptValidator<String>();
		String jsFunction = "var checkData = function (data) {return data == 'Solar Fields';}";
		validator.setFunction(jsFunction);
		assertEquals(validator.getFunction(), jsFunction);
		
		// Check actual validation, which requires executing the Javascript
		try {
			assertTrue(validator.validate("Solar Fields"));
		} catch (NoSuchMethodException e) {
			// Complain
			e.printStackTrace();
			fail();
		}
		
		return;
	}

	/**
	 * 
	 */
	@Test
	void testEquality() {
		JavascriptValidator<String> validator = new JavascriptValidator<String>();
		String jsFunction = "The Glitch Mob";
		validator.setFunction(jsFunction);
		
		JavascriptValidator<String> validator2 = new JavascriptValidator<String>();
		validator2.setFunction(jsFunction);
		
		// Check basic equality types
		assertEquals(validator,validator);
		assertEquals(validator,validator2);
		JavascriptValidator<String> validator3 = new JavascriptValidator<String>();
		validator3.setFunction(validator2.getFunction());
		assertEquals(validator,validator3);
		// Check some wrong answers
		validator3.setFunction("Blackmill");
		assertFalse(validator3.equals(validator));
		assertFalse(validator.equals(null));
		
		// Check cloning
		assertEquals(validator,validator.clone());
		
		// Check copying
		validator2 = new JavascriptValidator<String>(validator);
		assertEquals(validator,validator2);
		
		// Check the hash codes
		assertEquals(validator.hashCode(), validator2.hashCode());

		return;
	}

}
