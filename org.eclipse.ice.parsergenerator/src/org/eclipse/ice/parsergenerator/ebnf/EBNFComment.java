package org.eclipse.ice.parsergenerator.ebnf;

public class EBNFComment {

	private char comment;
	
	public EBNFComment() {
		comment = ';';
	}
	
	public String toString() {
		return String.valueOf(comment);
	}
	
	public void setComment(char c) { 
		comment = c;
	}
	
	public char getComment() {
		return comment;
	}
}
