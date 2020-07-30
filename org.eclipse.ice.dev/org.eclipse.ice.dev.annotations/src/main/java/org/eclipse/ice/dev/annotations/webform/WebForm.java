/******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Daniel Bluhm
 *****************************************************************************/
package org.eclipse.ice.dev.annotations.webform;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark a class as a WebForm Specification.
 *
 * Classes marked as {@code @WebForm} are expected to ....
 * * need to have required methods annotated
 * * declare hooks
 * 
 * Discuss lifecycle when appropriate and injection of dependencies
 * 
 * 
 * For example:
 *
 * <pre>
 * {@literal @WebForm}(name = "Talk Submission Page")
 * public class TalkFormSpec {
 *   ...
 * }
 * </pre>
 *
 * Will generate an interface like the following:
 *
 * <pre>
 * public interface TalkForm extends ... {
*   .... FIXME
 * }
 * </pre>
 *
 * And an associated implementing class (in this case, the class would be called
 * {@code TalkFormImplementation}) that implements that interface.
 *
 * @author Jay Jay Billings and Daniel Bluhm
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface WebForm {
	/**
	 * Name of the DataElement to generate
	 * @return name annotation value
	 */
	String name();
}
