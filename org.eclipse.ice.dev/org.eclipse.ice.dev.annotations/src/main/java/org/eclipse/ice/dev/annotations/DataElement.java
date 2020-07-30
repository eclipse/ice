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
package org.eclipse.ice.dev.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark a class as a DataElement Specification.
 *
 * Classes marked as {@code @DataElement} are expected to contain Fields marked
 * as {@code @DataField}. These specifications will then be expanded to an
 * interface and implementation of a DataElement derived class. For example:
 *
 * <pre>
 * {@literal @DataElement}(name = "Person")
 * public class PersonSpec {
 *   {@literal @DataField} private int age;
 *   {@literal @DataField} private String firstName;
 *   {@literal @DataField} private String lastName;
 * }
 * </pre>
 *
 * Will generate an interface like the following:
 *
 * <pre>
 * public interface Person extends IDataElement{@code<Person>} {
 *   public int getAge();
 *   public void setAge(int age);
 *   public String getFirstName();
 *   public void setFirstName(String firstName);
 *   public String getLastName();
 *   public void setLastName(String lastName);
 * }
 * </pre>
 *
 * And an associated implementing class (in this case, the class would be called
 * {@code PersonImplementation}) that fulfills that interface.
 *
 *
 * @see org.eclipse.ice.data.IDataElement
 * @see org.eclipse.ice.dev.annotations.DataField
 * @author Daniel Bluhm
 * @author Jay Jay Billings
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DataElement {
	/**
	 * Name of the DataElement to generate
	 * @return name annotation value
	 */
	String name();
}
