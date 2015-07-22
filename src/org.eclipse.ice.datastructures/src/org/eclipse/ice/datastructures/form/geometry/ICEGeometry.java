package org.eclipse.ice.datastructures.form.geometry;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.viz.service.geometry.Geometry;
import org.eclipse.ice.viz.service.geometry.IShape;

@XmlRootElement(name = "Geometry")
@XmlAccessorType(XmlAccessType.FIELD)
public class ICEGeometry implements IUpdateable, IUpdateableListener {

	@XmlAnyElement()
	@XmlElementRef(name = "Geometry", type = Geometry.class)
	Geometry geometry;
	
	@XmlAnyElement()
	@XmlElementRef(name = "ICEShape", type = ICEShape.class)
	ArrayList<ICEShape> shapes;
	
	@XmlTransient
	ArrayList<IUpdateableListener> listeners;
	
	public ICEGeometry(){
		geometry = new Geometry();
		
		shapes = new ArrayList<ICEShape>();
		
		listeners = new ArrayList<IUpdateableListener>();
	}
	
	public Geometry getGeometry(){
		notifyListeners();
		return geometry;
	}
	
	public void addShape(ICEShape shape){
		if (shape != null){
		geometry.addShape(shape.getShape());
		shapes.add(shape);
		shape.register(this);
		notifyListeners();
		}
	}
	
	public void removeShape(ICEShape shape){
		if (shape != null){
		geometry.removeShape(shape.getShape());
		shapes.remove(shape);
		notifyListeners();
		}
	}
	
	public ArrayList<ICEShape> getShapes(){
		return shapes;
	}
	
	public void setShapes(ArrayList<ICEShape> newShapes){
		
		ArrayList<IShape> ishapes = new ArrayList<IShape>();
		for (ICEShape iceShape : newShapes){
			iceShape.register(this);
			ishapes.add(iceShape.getShape());
		}
		geometry.setShapes(ishapes);
		shapes = newShapes;
		notifyListeners();
	}
	
	public int hashCode(){
		int hash = geometry.hashCode();
		for (ICEShape shape : shapes){
			hash += shape.hashCode();
		}
		return hash;
	}
	
	public boolean equals(Object otherObject){
		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof ICEGeometry)) {
			return false;
		}
		
		// At this point, other object must be a PrimitiveShape, so cast it
		ICEGeometry iceGeometry = (ICEGeometry) otherObject;
		
		if (!geometry.equals(iceGeometry.getGeometry())){
			return false;
		}
		
		return true;
	}
	
	public void copy (ICEGeometry otherGeometry){
		geometry.copy(otherGeometry.getGeometry());
		
		shapes.clear();
		
		for (ICEShape shape : otherGeometry.getShapes()){
			shapes.add((ICEShape) shape.clone());
		}
		
		for (ICEShape shape : shapes){
			shape.register(this);
		}
		
		notifyListeners();
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String updatedKey, String newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(IUpdateableListener listener) {
		listeners.add(listener);
		
	}

	@Override
	public void unregister(IUpdateableListener listener) {
		listeners.remove(listener);
		
	}
	
	public Object clone(){
		ICEGeometry newGeometry = new ICEGeometry();
		newGeometry.copy(this);
		
		return newGeometry;
	}
	
	/**
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of this GeometryComponent
	 * </p>
	 * 
	 */
	protected void notifyListeners() {

		final ICEGeometry geometry = this;

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
					listeners.get(i).update(geometry);
				}
			}
		};

		// Start the thread
		notifyThread.start();

	}

	@Override
	public void update(IUpdateable component) {
		if (component instanceof ICEShape){
			notifyListeners();
		}
		
	}
	
	
}
