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

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.ice.data.IDataElement;

/*
 * Util class passed to the Velocity template engine that enables 
 * logic to be executed through Java rather than templating syntax
 */
public class VelocityUtils {
	
	private ProcessingEnvironment processingEnvironment;
	
	public VelocityUtils(ProcessingEnvironment processingEnvironment){
		this.processingEnvironment = processingEnvironment;
	}
	
	/**
	 * Util method that attempts to determine the parameterized type of a given TypeMirror 
	 * @param type A TypeMirror that has a Parameterized Type e.g. List<ParameterizedType>
	 * @return TypeMirror The ParamterizedType's TypeMirror or null if it doesn not exist
	 */
	public static TypeMirror getCollectionParameterType(TypeMirror type) {
		if(type != null && DeclaredType.class.isAssignableFrom(type.getClass())) {
			if(((DeclaredType)type).getTypeArguments() != null && ((DeclaredType)type).getTypeArguments().size() > 0) {
				return ((DeclaredType)type).getTypeArguments().get(0);
			}
		}
		return null;
	}
	
	/**
	 * Get the Class object the parameter type represents
	 * @param type
	 * @return Class The actual class the TypeMirror type represents
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(TypeMirror type) throws ClassNotFoundException{
		if(type != null && DeclaredType.class.isAssignableFrom(type.getClass())) {
			return ClassUtils.getClass(((DeclaredType)type).asElement().toString());
		}
		return null;
	}
	
	
	/**
	 * Util method for collection all IDataElement and IDataElement collections in a given list of fields
	 * @param fields
	 * @return IDataElement fields
	 */
	public List<Field> collectAllDataElementFields(List<Field> fields) {
		return fields.stream()
				.filter(field -> field.getMirror() != null && isIDataElementOrCollectionOf(field.getMirror()))
				.collect(Collectors.toList());	
	}
	
	/**
	 * Util method for checking if there are any fields of type IDataElement or a collection of IDataElements in a given list of fields
	 * @param fields
	 * @return
	 */
	public boolean anyDataElementsExist(List<Field> fields) {
		return fields.stream()
				.anyMatch(field -> field.getMirror() != null && isIDataElementOrCollectionOf(field.getMirror()));
	}
	
	/**
	 * Checks if the given TypeMirror type represents an IDataElement
	 * @param type
	 * @return boolean
	 */
	public boolean isIDataElement(TypeMirror type) {
		if(type != null) {
			if(type.toString().contains(IDataElement.class.getSimpleName()))
			{
				return true;
			}
				for (TypeMirror supertype : processingEnvironment.getTypeUtils().directSupertypes(type)) {
					   DeclaredType declared = (DeclaredType)supertype; 
					   Element supertypeElement = declared.asElement();
					   if(supertypeElement.asType().toString().contains(IDataElement.class.getSimpleName())) {
						   return true;
					   }
				}
		
		}
		return false;
		
	}
	
	/**
	 * Checks if a given TypeMirror type represents an IDataElement or a parameterized collection of IDataElement's
	 * @param type
	 * @return boolean
	 */
	public boolean isIDataElementOrCollectionOf(TypeMirror type) {
		return this.isIDataElement(type) || (getCollectionParameterType(type) != null && this.isIDataElement(getCollectionParameterType(type)));
	}
	
}
