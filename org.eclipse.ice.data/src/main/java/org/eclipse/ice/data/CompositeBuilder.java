/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.data;

/**
 * This is a builder for constructing Composites, which are collections of
 * Components. It uses a standard builder API to dynamically build and construct
 * a new Composite based on the information passed through its methods.
 * 
 * All Composites should be constructed by using the with* operations to
 * configure data members before the build function is called.
 * 
 * The builder will clear its state between separate calls to the build
 * function. That is, successively calling build() will not return two resources
 * with the same values. The builder will do it automatically, although clients
 * may elect to do it themselves by calling clear().
 * 
 * @author Jay Jay Billings
 *
 */
public class CompositeBuilder {

}
