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
 * @see org.eclipse.ice.dev.annotations.IDataElement
 * @see org.eclipse.ice.dev.annotations.DataField
 * @author Daniel Bluhm
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
