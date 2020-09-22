package gov.ornl.rse.renderer.client.test;

import java.util.Date;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

/**
 * Car DataElement.
 * @author Daniel Bluhm
 */
@DataElement(name = "Car")
public class CarSpec {

	/**
	 * Make of the car.
	 */
	@DataField private String make;

	/**
	 * Model of the car.
	 */
	@DataField private String model;

	/**
	 * Year of car manufacture.
	 */
	@DataField private Date year;
}