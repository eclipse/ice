/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.viewer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;

/**
 * <p>
 * Default IRenderer implementation to be used by client implementations.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class Renderer implements IRenderer {

	/** Stores attachment manager associations. */
	protected Map<Class<? extends IAttachment>, IAttachmentManager> attachmentMgr;

	/**
	 * @see IRenderer#supportsAttachment(Class)
	 */
	@Override
	public boolean supportsAttachment(Class<? extends IAttachment> clazz) {
		if (attachmentMgr == null) {
			return false;
		}

		return attachmentMgr.containsKey(clazz);
	}

	/**
	 * @see IRenderer#createAttachment(Class)
	 */
	@Override
	public IAttachment createAttachment(Class<? extends IAttachment> clazz) {
		if (attachmentMgr == null) {
			attachmentMgr = new HashMap<>();
		}

		IAttachmentManager attachManagerInst = attachmentMgr.get(clazz);

		if (attachManagerInst == null) {
			return null;
		}

		IAttachment allocation = attachManagerInst.allocate();

		return allocation;
	}

	/**
	 * @see IRenderer#register(Class, IAttachmentManager)
	 */
	@Override
	public void register(Class<? extends IAttachment> type,
			IAttachmentManager mgr) {
		if (attachmentMgr == null) {
			attachmentMgr = new HashMap<>();
		}

		attachmentMgr.put(type, mgr);
	}

	/**
	 * @see IRenderer#unregister(Class)
	 */
	@Override
	public void unregister(Class<? extends IAttachment> type) {
		if (attachmentMgr == null) {
			return;
		}

		attachmentMgr.remove(type);
	}

}
