package org.eclipse.ice.ui.swtbot.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.MaterialWritableTableFormat;
import org.eclipse.ice.materials.XMLMaterialsDatabase;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

public class FakeMaterialsDatabase extends XMLMaterialsDatabase {

	public FakeMaterialsDatabase(){
		restoreDefaults();
	}
	
	private void loadDatabase(File fileToLoad) {
			ArrayList<Material> materials = new ArrayList<Material>();
			
			Material Ag = new Material();
			Ag.setName("Ag");
			Ag.setProperty(Material.ABS_X_SECTION, 63.3);
			Ag.setProperty(Material.COHERENT_SCAT_LENGTH, 5.922);
			Ag.setProperty(Material.ATOMIC_DENSITY, 58.62);
			
			Material Al = new Material();
			Al.setName("Al");
			Al.setProperty(Material.ABS_X_SECTION, .231);
			Al.setProperty(Material.COHERENT_SCAT_LENGTH, 3.449);
			Al.setProperty(Material.ATOMIC_DENSITY, 60.31);
			
			Material Am = new Material();
			Am.setName("Am");
			Am.setProperty(Material.ABS_X_SECTION, 75.3);
			Am.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
			Am.setProperty(Material.ATOMIC_DENSITY, 1.04);
			
			Material Ar = new Material();
			Ar.setName("Ar");
			Ar.setProperty(Material.ABS_X_SECTION, 0.675);
			Ar.setProperty(Material.COHERENT_SCAT_LENGTH, 1.909);
			Ar.setProperty(Material.ATOMIC_DENSITY, 0.03);
			
			Material As = new Material();
			As.setName("As");
			As.setProperty(Material.ABS_X_SECTION, 4.5);
			As.setProperty(Material.COHERENT_SCAT_LENGTH, 6.58);
			As.setProperty(Material.ATOMIC_DENSITY, 46.03);	
			
			Material Au = new Material();
			Au.setName("Au");
			Au.setProperty(Material.ABS_X_SECTION, 98.65);
			Au.setProperty(Material.COHERENT_SCAT_LENGTH, 7.63);
			Au.setProperty(Material.ATOMIC_DENSITY, 57.72);	
			
			materials.add(Ag);
			materials.add(Al);
			materials.add(Am);
			materials.add(Ar);
			materials.add(As);
			materials.add(Au);
			
//			Material Am1 = new Material();
//			Am1.setName("M1");
//			Am1.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am1.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am1.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am2 = new Material();
//			Am2.setName("M2");
//			Am2.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am2.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am2.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am3 = new Material();
//			Am3.setName("M3");
//			Am3.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am3.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am3.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am4 = new Material();
//			Am4.setName("M4");
//			Am4.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am4.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am4.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am5 = new Material();
//			Am5.setName("M5");
//			Am5.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am5.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am5.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am6 = new Material();
//			Am6.setName("M6");
//			Am6.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am6.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am6.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am7 = new Material();
//			Am7.setName("M7");
//			Am7.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am7.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am7.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am8 = new Material();
//			Am8.setName("M8");
//			Am8.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am8.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am8.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am9 = new Material();
//			Am9.setName("M9");
//			Am9.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am9.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am9.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			Material Am10 = new Material();
//			Am10.setName("M10");
//			Am10.setProperty(Material.ABS_X_SECTION, 75.3);
//			Am10.setProperty(Material.COHERENT_SCAT_LENGTH, 8.3);
//			Am10.setProperty(Material.ATOMIC_DENSITY, 1.04);
//			
//			materials.add(Am1);
//			materials.add(Am2);
//			materials.add(Am3);
//			materials.add(Am4);
//			materials.add(Am5);
//			materials.add(Am6);
//			materials.add(Am7);
//			materials.add(Am8);
//			materials.add(Am9);
//			materials.add(Am10);
			
			Collections.sort(materials);

			// Load the list into the material map
			materialsMap = new Hashtable<String, Material>();
			for (Material material : materials) {
				materialsMap.put(material.getName(), material);
			}
	}
	
	@Override
	public void restoreDefaults(){
		loadDatabase(new File(""));
	}
	
	@Override
	public void start(){
		loadDatabase(new File(""));
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IElementSource#getTableFormat()
	 */
	@Override
	public TableFormat<Material> getTableFormat() {

		MaterialWritableTableFormat format = null;

		// Build and return a table format if there are materials in the
		// database
		if (!materialsMap.isEmpty()) {
			// Get the properties off the map. Pulling back the array is more
			// efficient than getting an iterator. I think...
//			Material[] emptyArray = {};
//			Map<String, Double> props = materialsMap.values().toArray(
//					emptyArray)[0].getProperties();
//			ArrayList<String> propNames = new ArrayList<String>(props.keySet());
//			// Initialize the table format
			ArrayList<String> propNames = new ArrayList<String>();
			propNames.add(Material.ABS_X_SECTION);
			propNames.add(Material.COHERENT_SCAT_LENGTH);
			propNames.add(Material.ATOMIC_DENSITY);			
			format = new MaterialWritableTableFormat(propNames);
		}

		return format;
	}
}
