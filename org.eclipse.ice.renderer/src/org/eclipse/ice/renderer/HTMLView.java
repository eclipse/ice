/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.renderer;

public class HTMLView {

	public HTMLView() {
		
	}
	
	public void draw(DataElement data) {
		String content = "<html><head><title>Hello World!</title></head><body>Hello World! " + 
						data.toString() + "</body></html>";
		System.out.println(content);
		return;
	}

}
