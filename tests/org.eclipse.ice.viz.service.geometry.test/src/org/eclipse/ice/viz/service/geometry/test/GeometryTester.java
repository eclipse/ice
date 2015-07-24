package org.eclipse.ice.viz.service.geometry.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.test.TestComponentListener;
import org.eclipse.ice.viz.service.geometry.ComplexShape;
import org.eclipse.ice.viz.service.geometry.Geometry;
import org.eclipse.ice.viz.service.geometry.IShape;
import org.eclipse.ice.viz.service.geometry.OperatorType;
import org.eclipse.ice.viz.service.geometry.PrimitiveShape;
import org.eclipse.ice.viz.service.geometry.ShapeType;
import org.junit.Test;

public class GeometryTester {
	/**
	 * <p>
	 * This operation checks the ability of the GeometryComponent to persist
	 * itself to XML and to load itself from an XML input stream.
	 * </p>
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 * @throws NullPointerException
	 * 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException,
			JAXBException, IOException {
		// Local Declarations
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Geometry.class);

		// Instantiate a GeometryComponent
		Geometry geometry = new Geometry();
		geometry.addShape(new PrimitiveShape(ShapeType.Sphere));
		geometry.setId(25);
		geometry.setDescription("description");
		geometry.setName("name");

		// Load it into XML
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(geometry, classList, outputStream);

		// convert information inside of outputStream to inputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		String xmlFile2 = new String(outputStream.toByteArray());
		// System.err.println(xmlFile2);

		// load contents into xml
		Geometry loadGeometry = new Geometry();
		loadGeometry = (Geometry) xmlHandler.read(classList, inputStream);

		// Check contents
		assertTrue(loadGeometry.equals(geometry));

	}

	/**
	 * Check the functionality of the methods for interacting with the
	 * Geometry's list of IShapes.
	 */
	@Test
	public void checkShapes() {
		Geometry geometry = new Geometry();
		ArrayList<IShape> testShapes = new ArrayList<IShape>();

		// Set a list of different types of shapes as the geometry's list
		testShapes.add(new ComplexShape(OperatorType.Union));
		PrimitiveShape cone = new PrimitiveShape(ShapeType.Cone);
		testShapes.add(new PrimitiveShape(ShapeType.Cone));
		geometry.setShapes(testShapes);

		// Check that the geometry has the correct list
		assertTrue(geometry.getShapes().equals(testShapes));

		// Remove a shape from the list
		ArrayList<IShape> testShapes2 = (ArrayList<IShape>) testShapes.clone();
		geometry.removeShape(cone);
		testShapes2.remove(cone);

		// Check that the shape is gone
		assertTrue(geometry.getShapes().equals(testShapes2));

		// Add a shape to the list
		PrimitiveShape cube = new PrimitiveShape(ShapeType.Cube);
		geometry.addShape(cube);
		testShapes2.add(cube);

		// Check that the shape is gone
		assertTrue(geometry.getShapes().equals(testShapes2));

	}

	/**
	 * Check that a geometry can properly creat a copy of itself.
	 */
	@Test
	public void checkClone() {
		// Create a geometry with a shape containing shapes of its own
		Geometry geometry = new Geometry();
		geometry.addShape(new PrimitiveShape(ShapeType.Cone));
		ComplexShape shape = new ComplexShape(OperatorType.Complement);
		shape.addShape(new PrimitiveShape(ShapeType.Cube));
		shape.addShape(new PrimitiveShape(ShapeType.Cylinder));

		// Create a deep copy
		Geometry geometryClone = (Geometry) geometry.clone();

		// Check that the copy is equal to the new shape
		assertTrue(geometry.equals(geometryClone));

	}

	/**
	 * Check that the equality comparator for Geometries is correct
	 */
	@Test
	public void checkEquals() {
		Geometry geometry = new Geometry();
		Geometry equalGeometry = new Geometry();
		Geometry unequalGeometry = new Geometry();

		PrimitiveShape cone = new PrimitiveShape(ShapeType.Cone);
		PrimitiveShape cube = new PrimitiveShape(ShapeType.Cube);

		geometry.addShape(cone);
		equalGeometry.addShape(cone);
		unequalGeometry.addShape(cube);

		// Check that two equal geometries are equal
		assertTrue(geometry.equals(equalGeometry));

		// Check that two unequal geometries are unequal
		assertFalse(geometry.equals(unequalGeometry));

		// Check that a geometry equals itself
		assertTrue(geometry.equals(geometry));

		// Check that the equality operator is symmetric
		assertTrue(equalGeometry.equals(geometry));

	}

	/**
	 * Checks that a geometry properly notifies its listeners of update events
	 */
	@Test
	public void checkListenerNotification() {
		VizTestComponentListener listener1 = new VizTestComponentListener();
		VizTestComponentListener listener2 = new VizTestComponentListener();
		Geometry geometry = new Geometry();
		geometry.register(listener1);
		geometry.register(listener2);

		// Add a shape. This should prompt an update to the geometry's
		// listeners.
		geometry.addShape(new PrimitiveShape(ShapeType.Cone));

		// Check that both listeners received an update.
		assertTrue(listener1.wasNotified());
		assertTrue(listener2.wasNotified());

	}
}
