/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import lombok.Builder;
import lombok.Data;

/**
 * Metadata extracted from persistence annotations.
 * @author Daniel Bluhm
 */
@Data
@Builder
public class PersistenceMetadata {
	/**
	 * Collection name for persistence.
	 */
	private String collection;
}
