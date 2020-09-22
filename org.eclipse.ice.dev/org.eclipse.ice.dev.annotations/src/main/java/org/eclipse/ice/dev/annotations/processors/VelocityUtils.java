/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.ice.data.IDataElement;

/**
 * Util class passed to the Velocity template engine that enables 
 * logic to be executed through Java rather than templating syntax
 * 
 * @author Michael Walsh
 *
 */
public class VelocityUtils {

	/**
	 * Utility class necessary for certain class meta data reading during templating
	 */
	private ProcessingEnvironment processingEnvironment;

	/**
	 * Constructor with processingEnvironment parameter necessary for {@link #isIDataElement(TypeMirror)}
	 * @param processingEnvironment
	 */
	public VelocityUtils(ProcessingEnvironment processingEnvironment) {
		this.processingEnvironment = processingEnvironment;
	}

	/**
	 * Util method that attempts to determine the parameterized type of a given
	 * TypeMirror
	 * 
	 * @param type A TypeMirror that has a Parameterized Type e.g.
	 *             List<ParameterizedType>
	 * @return TypeMirror The ParamterizedType's TypeMirror or null if it does not
	 *         exist
	 */
	public static TypeMirror getCollectionParameterType(TypeMirror type) {
		if (type != null && DeclaredType.class.isAssignableFrom(type.getClass())
				&& ((DeclaredType) type).getTypeArguments() != null
				&& !((DeclaredType) type).getTypeArguments().isEmpty()) {
			return ((DeclaredType) type).getTypeArguments().get(0);
		}

		return null;
	}

	/**
	 * Get the Class object the parameter type represents, if it is null or not a 
	 * {@link javax.lang.model.type.DeclaredType} then this method returns null
	 * @param type Any given TypeMirror
	 * @return Class The actual class the TypeMirror type represents
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(TypeMirror type) throws ClassNotFoundException {
		if (type != null && DeclaredType.class.isAssignableFrom(type.getClass())) {
			return ClassUtils.getClass(((DeclaredType) type).asElement().toString());
		}
		return null;
	}

	/**
	 * Util method for collection all IDataElement and IDataElement collections in a
	 * given list of fields
	 * 
	 * @param fields to be filtered
	 * @return a list of {@link IDataElement} (or collections of) contained within the given list
	 */
	public List<Field> collectAllDataElementFields(List<Field> fields) {
		return fields.stream()
				.filter(field -> field.getMirror() != null && isIDataElementOrCollectionOf(field.getMirror()))
				.collect(Collectors.toList());
	}

	/**
	 * Util method for checking if there are any fields of type IDataElement or a
	 * collection of IDataElements in a given list of fields
	 * 
	 * @param fields to be searched
	 * @return boolean true if any results are found, false if none
	 */
	public boolean anyDataElementsExist(List<Field> fields) {
		return fields.stream()
				.anyMatch(field -> field.getMirror() != null && isIDataElementOrCollectionOf(field.getMirror()));
	}

	/**
	 * Checks if the given TypeMirror type represents an IDataElement
	 * 
	 * @param type any TypeMirror
	 * @return boolean true if it or its parent classes are of type {@link IDataElement}, else false
	 */
	public boolean isIDataElement(TypeMirror type) {
		if (type != null) {
			if (type.toString().contains(IDataElement.class.getSimpleName())) {
				return true;
			}
			for (TypeMirror supertype : processingEnvironment.getTypeUtils().directSupertypes(type)) {
				DeclaredType declared = (DeclaredType) supertype;
				Element supertypeElement = declared.asElement();
				if (supertypeElement.asType().toString().contains(IDataElement.class.getSimpleName())) {
					return true;
				}
			}

		}
		return false;

	}

	/**
	 * Checks if a given TypeMirror type represents an IDataElement or a
	 * parameterized collection of IDataElement's
	 * 
	 * @param type any given TypeMirror
	 * @return boolean true if it is an {@link IDataElement} or collection of {@link IDataElement}, else false
	 */
	public boolean isIDataElementOrCollectionOf(TypeMirror type) {
		return this.isIDataElement(type)
				|| (getCollectionParameterType(type) != null && this.isIDataElement(getCollectionParameterType(type)));
	}

}
