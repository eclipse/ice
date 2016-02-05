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
package org.eclipse.ice.parsergenerator.grammars;

import java.util.function.BiFunction;


/**
 * Generates an ANTLR grammar that is capable of handling flat formats
 * such as INI.
 * 
 * @author arbennett
 */
public class FlatGrammar {

	private StringBuilder sb;

	private String sep = System.lineSeparator();
	private String header = "grammar ItemParser";
	
	private String declaration = "sections+=Section*";
	private String content = "section*";
	private String entry = "name=ID + ASSIGN + value=TEXT";
	private String section = "OPEN + name=ID + CLOSE" + sep + 
			             "    (NEWLINE+ lines+=Line)+" + sep + 
			             "    NEWLINE+";
	
	private String id = "('A'..'Z' | 'a'..'z') ('A'..'Z' | 'a'..'z' | '_' | '-' | '0'..'9')"; 
	private String whitespace = "(' '|'\t')+;";
	private String newline = "'\r'? '\n'+";
	private String text = "(WHITESPACE+ | STRING+)*";
	
	private String open;
	private String close;
	private String assign;
	private String comment;

	private BiFunction<String, String, String> build = (k,v) -> (k + ":" + sep + "    " + v + ";" + sep);	
	
	public FlatGrammar() {
		sb = new StringBuilder();
	}
	
	public void setSection(String o, String c) {
		open = "'" + o + "'";
		close = "'" + c + "'";
	}
	
	public void setAssignmentOperator(String a) {
		assign = "'" + a + "'";
	}
	
	public void setCommentSymbol(String c) {
		comment = "'" + c + "'";
	}
	
	private void buildGrammar() {
		if (open==null || close==null || assign==null || comment==null) 
			return;
		
		sb.append(header);
		sb.append(build.apply("ItemParser", declaration));
		
		// Intermediate nodes in parse tree
		sb.append(build.apply("content", content));
		sb.append(build.apply("section", section));
		sb.append(build.apply("comment", "COMMENT " + text));
		sb.append(build.apply("entry", entry));
		
		// 'terminal' prefix denotes leaf nodes of parse tree
		sb.append(build.apply("terminal ID", id));
		sb.append(build.apply("terminal TEXT", text));
		sb.append(build.apply("terminal NEWLINE", newline));
		sb.append(build.apply("terminal WHITESPACE", whitespace));
		
		sb.append(build.apply("terminal OPEN", open));
		sb.append(build.apply("terminal CLOSE", close));
		sb.append(build.apply("terminal ASSIGN", assign));
		sb.append(build.apply("terminal COMMENT", comment));
	}
	
	public String toString() {
		buildGrammar();
		return sb.toString();
	}
	
}
