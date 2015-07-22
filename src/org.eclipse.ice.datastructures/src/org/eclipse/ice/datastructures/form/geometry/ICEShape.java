package org.eclipse.ice.datastructures.form.geometry;


import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.viz.service.geometry.AbstractShape;
import org.eclipse.ice.viz.service.geometry.ComplexShape;
import org.eclipse.ice.viz.service.geometry.IShape;
import org.eclipse.ice.viz.service.geometry.OperatorType;
import org.eclipse.ice.viz.service.geometry.PrimitiveShape;
import org.eclipse.ice.viz.service.geometry.ShapeType;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.form.GeometryComponent;

@XmlRootElement(name = "ICEShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class ICEShape extends ICEObject implements Component  {

	@XmlAnyElement()
	@XmlElementRefs(value = {
			@XmlElementRef(name = "ComplexShape", type = ComplexShape.class),
			@XmlElementRef(name = "PrimitiveShape", type = PrimitiveShape.class) })
	private IShape shape;
	
	@XmlAnyElement()
	@XmlElementRef(name = "ICEShape", type = ICEShape.class)
	private ICEShape parent;
	
	@XmlAnyElement()
	@XmlElementRef(name = "ICEShape", type = ICEShape.class)
	private ArrayList<ICEShape> children;
	
	public IShape getShape(){
		return shape;
	}
	
	public void setShape(IShape newShape){
		shape = newShape;
		notifyListeners();
	}
	
	public ICEShape(){
		shape = null;
		parent = null;
		children = new ArrayList<ICEShape>();
	}
	
	public ICEShape(ICEShapeType shapeType){
		shape = new PrimitiveShape(ShapeType.values()[shapeType.ordinal()]);
		parent = null;
		children = new ArrayList<ICEShape>();
	}
	
	public ICEShape(ICEOperatorType operatorType){
		shape = new ComplexShape(OperatorType.values()[operatorType.ordinal()]);
		parent = null;
		children = new ArrayList<ICEShape>();
	}
	
	public ICETransformation getTransformation(){
		return new ICETransformation(shape.getTransformation());
	}
	
	public boolean setTransformation(ICETransformation newTransformation){
		if (newTransformation != null){
			notifyListeners();
		return shape.setTransformation(newTransformation.getTransformation());
		}
		return false;
	}
	
	public String getProperty(String key){
		return shape.getProperty(key);
	}
	
	public boolean setProperty(String key, String value){
		notifyListeners();
		return shape.setProperty(key, value);
	}
	
	public boolean removeProperty(String key){
		return shape.removeProperty(key);
	}
	
	public int hashCode(){
		int hash = 31 * shape.hashCode();
		return hash;
	}
	
	public boolean equals(Object otherObject){
		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof ICEShape)) {
			return false;
		}
		
		// At this point, other object must be a PrimitiveShape, so cast it
		ICEShape iceShape = (ICEShape) otherObject;
		
		if (!shape.equals(iceShape.getShape())){
			return false;
		}
		
		return true;
	}
	
	public void copy(ICEShape otherShape){
		super.copy(otherShape);
		if (otherShape != null){
			notifyListeners();
		AbstractShape abstractShape = (AbstractShape) otherShape.getShape();
		((AbstractShape) shape).copy(abstractShape);
		}
	}
	
	public ICEShape getShapeParent(){
		return parent;
	}
	
	protected void setParent(ICEShape newParent){
		notifyListeners();
		((AbstractShape) shape).setParent(newParent.getShape());
		parent = newParent;
	}
	
	public Object clone(){
		ICEShape newShape = new ICEShape(ICEShapeType.Cone); 
		newShape.copy(this);
		//newShape.setShape((IShape) shape.clone());
		return newShape;
	}
	
	public void addShape(ICEShape newShape){
		if (isComplex()){
			((ComplexShape) shape).addShape(newShape.getShape());
			newShape.setParent(this);
			children.add(newShape);
			notifyListeners();
		}
	}
	
	public boolean isComplex(){
		if (shape instanceof ComplexShape){
			return true;
		}
		
		return false;
	}
	
	public void removeShape(ICEShape oldShape){
		if (isComplex()){
			notifyListeners();
			((ComplexShape) shape).removeShape(oldShape.getShape());
			children.remove(oldShape);
		}
		
	}
	
	public ArrayList<ICEShape> getShapes(){
		return children;
	}
	
	public void setShapes(ArrayList<ICEShape> newChildren){
		if (isComplex()){
			ArrayList<IShape> shapes = new ArrayList<IShape>();
			for (ICEShape iceShape : newChildren){
				shapes.add(iceShape.getShape());
			}
			((ComplexShape) shape).setShapes(shapes);
			children = newChildren;
			notifyListeners();
		}
	}
	
	public ICEShapeType getShapeType(){
		if (!isComplex()){
		return ICEShapeType.values()[((PrimitiveShape) shape).getType().ordinal()];
		}
		else return null;
	}
	
	public ICEOperatorType getOperatorType(){
		if (isComplex()){
		return ICEOperatorType.values()[((ComplexShape) shape).getType().ordinal()];
		}
		else return null;
	}
	
	public void setShapeName(String newName){
		shape.setName(newName);
		notifyListeners();
	}
	
	public void setShapeId(int newID){
		shape.setId(newID);
		notifyListeners();
	}
	
	public void setShapeType(ICEShapeType type){
		((PrimitiveShape) shape).setType(ShapeType.values()[type.ordinal()]);
		notifyListeners();
	}	
	
	public void setOperatorType(ICEOperatorType type){
		((ComplexShape) shape).setType(OperatorType.values()[type.ordinal()]);
		notifyListeners();
	}

	/**
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of this GeometryComponent
	 * </p>
	 * 
	 */
	@Override
	protected void notifyListeners() {

		final ICEShape iceShape = this;

		// If the listeners are empty, return
		if (this.listeners == null || this.listeners.isEmpty()) {
			return;
		}
		// Create a thread object that notifies all listeners

		Thread notifyThread = new Thread() {

			@Override
			public void run() {
				// Loop over all listeners and update them
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).update(iceShape);
				}
			}
		};

		// Start the thread
		notifyThread.start();

	}
	
	@Override
	public void accept(IComponentVisitor visitor) {
		// TODO Auto-generated method stub
			}


}
