/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.parsergenerator.ebnf.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.ice.parsergenerator.ebnf.EBNFComment;
import org.junit.Test;

public class EBNFCommentTester {

	@Test
	public void checkToString() {
		EBNFComment cmt = new EBNFComment();
		cmt.setComment('#');
		assertEquals(cmt.getComment(), '#');
		assertEquals(cmt.toString(), "whitespace*, #, ascii*");
	}
}
