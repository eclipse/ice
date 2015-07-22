package org.eclipse.ice.datastructures.form.geometry;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.viz.service.geometry.Transformation;

public class ICETransformation extends ICEObject{

	private Transformation transformation;

	public ICETransformation() {
		transformation = new Transformation();
	}

	public ICETransformation(Transformation newTransformation) {
		transformation = newTransformation;
	}

	public Transformation getTransformation() {
		return transformation;
	}

	public double[] getSkew() {
		return transformation.getSkew();
	}

	public double getSize() {
		return transformation.getSize();
	}

	public double[] getScale() {
		return transformation.getScale();
	}

	public double[] getRotation() {
		return transformation.getRotation();
	}

	public double[] getTranslation() {
		return transformation.getTranslation();
	}

	public void setSkew(double newX, double newY, double newZ) {

			transformation.setSkew(newX, newY, newZ);

	}
	
	public void setSize(double newSize){
		transformation.setSize(newSize);
	}
	
	public void setScale(double newX, double newY, double newZ) {

			transformation.setScale(newX, newY, newZ);

	}
	
	public void setRotation(double newX, double newY, double newZ) {

			transformation.setRotation(newZ, newY, newZ);

	}
	
	public void setTranslation(double newX, double newY, double newZ) {

			transformation.setSkew(newX, newY, newZ);

	}

	public void translate(double[] translation) {
		transformation.translate(translation);
	}

	public Object clone() {
		ICETransformation newTransformation = new ICETransformation(
				(Transformation) transformation.clone());
		return newTransformation;
	}
	
	public boolean equals(Object otherObject){
		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof ICETransformation)) {
			return false;
		}
		
		// At this point, other object must be a PrimitiveShape, so cast it
		ICETransformation iceTransformation = (ICETransformation) otherObject;
		
		if (!transformation.equals(iceTransformation.getTransformation())){
			return false;
		}
		
		return true;
	}

}
