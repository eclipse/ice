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
import org.eclipse.ice.dev.annotations.IDataElement;

public class VelocityUtils {
	
	private ProcessingEnvironment processingEnvironment;
	
	VelocityUtils(ProcessingEnvironment processingEnvironment){
		this.processingEnvironment = processingEnvironment;
	}
	
	public static TypeMirror getCollectionParameterType(TypeMirror type) {
		if(type != null && DeclaredType.class.isAssignableFrom(type.getClass())) {
			if(((DeclaredType)type).getTypeArguments() != null && ((DeclaredType)type).getTypeArguments().size() > 0) {
				return ((DeclaredType)type).getTypeArguments().get(0);
			}
		}
		return null;
	}
	
	public static Class<?> getClass(TypeMirror type) throws ClassNotFoundException{
		if(type != null && DeclaredType.class.isAssignableFrom(type.getClass())) {
			return ClassUtils.getClass(((DeclaredType)type).asElement().toString());
		}
		return null;
	}
	
	public List<Field> collectAllDataElementFields(List<Field> fields) {
		return fields.stream()
				.filter(field -> field.getMirror() != null && isIDataElementOrCollectionOf(field.getMirror()))
				.collect(Collectors.toList());	
	}
	
	public boolean anyDataElementsExist(List<Field> fields) {
		return fields.stream()
				.anyMatch(field -> field.getMirror() != null && isIDataElementOrCollectionOf(field.getMirror()));
	}
	
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
	
	public boolean isIDataElementOrCollectionOf(TypeMirror type) {
		return this.isIDataElement(type) || (getCollectionParameterType(type) != null && this.isIDataElement(getCollectionParameterType(type)));
	}
	
	
	//debugging method
	public String getSupers(TypeMirror type) {
		if(type != null) {
			if(type.toString().contains(IDataElement.class.getSimpleName()))
			{
				return type.toString();
			}
			StringBuilder s = new StringBuilder("");
				for (TypeMirror supertype : processingEnvironment.getTypeUtils().directSupertypes(type)) {
					   DeclaredType declared = (DeclaredType)supertype; 
					   Element supertypeElement = declared.asElement();
					   //if(supertypeElement.asType().toString().contains("Data")) {
					   s.append(supertypeElement.asType().toString() + " ");
					   //}
				}
				return s.toString();
		
		}
		return "";
		
	}
}
