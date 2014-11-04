/**
 * 
 */
package org.eclipse.ice.materials.ui;

import org.eclipse.ice.materials.IMaterialsDatabase;

/**
 * This is a simple class that catches the active Materials Database service and
 * holds the reference for the Materials UI. It is a singleton.
 * 
 * This class has no tests because it is a simple bean.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MaterialsDatabaseServiceHolder {

	/**
	 * The reference to the IMaterialsDatabase service.
	 */
	private static IMaterialsDatabase materialsDB;

	/**
	 * This operation sets the reference to the service.
	 * 
	 * @param database
	 *            The database service
	 */
	public static void set(IMaterialsDatabase database) {
		materialsDB = database;
		System.out.println("MaterialsDatabaseServiceHolder Message: "
				+ "Service Handle Received!");
	}

	/**
	 * This operation retrieves the reference to the service.
	 * 
	 * @return
	 */
	public static IMaterialsDatabase get() {
		return materialsDB;
	}

}
