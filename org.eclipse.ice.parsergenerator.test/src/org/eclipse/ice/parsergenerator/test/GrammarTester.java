/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.parsergenerator.test;

import org.junit.Test;

/**
 * 
 * 
 * @author Andrew Bennett
 */
public class GrammarTester {

	private String sep = java.lang.System.lineSeparator();
	private String iniGrammar = 
			"grammar org.eclipse.ice.parsergenerator.ini"+ sep +
			"INIFile:" + sep +
			"    sections+=Section*;" + sep +
			"Section:" + sep +
			"    OPEN + name=ID + CLOSE" + sep +
			"    (NEWLINE+ lines+=Line)+" + sep +
			"    NEWLINE+;" + sep +
			"Line:" + sep +
			"    (Entry | Comment);" + sep +
			"Entry:" + sep +
			"    name=ID + ASSIGN + value=TEXT;" + sep +
			"Comment:" + sep + 
			"    WHITESPACE? COMMENT TEXT? NEWLINE;" + sep +
			"terminal WHITESPACE:" + sep +
			"    (' ' | '\t')+;" + sep +
			"terminal NEWLINE:" + sep +
			"    '\r'? '\n';" + sep +
			"terminal ID:" + sep +
			"    ('A'..'Z' | 'a'..'z') ('A'..'Z' | 'a'..'z' | '_' | '-' | '0'..'9');" + sep +
			"terminal TEXT:" + sep +
			"    !('\n' | '\r');" + sep +
			"terminal COMMENT:" + sep +
			"    '#';" + sep +
			"terminal OPEN:" + sep +
			"    '[';" + sep +
			"terminal CLOSE:" + sep +
			"    ']';" + sep +
			"terminal ASSIGN:" + sep +
			"    '=';" + sep; 

				
	@Test
	public void TestGrammar() {
		
	}
}
