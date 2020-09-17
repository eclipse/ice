package org.eclipse.ice.data.neutrons;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name="RunSpec")
public class RunSpec {
	
	/**
	 * unique ID for every run, this is set by the `nextId` in ReductionParameters
	 */
	@DataField private long id;
	
	/**
	 * instrument for this Run
	 */
	@DataField private String instrumentName;
	
	/**
	 * IPTS number for this Run
	 */
	@DataField private String iptsNumber;
	
	/**
	 * run number for this Run
	 */
	@DataField private String runNumber;

	/**
	 * metadata.entry.title
	 */
	@DataField private String runTitle;
	
	/**
	 * metadata.entry.total_counts
	 */
	@DataField private long totalCounts;
	
	/**
	 * metadata.entry.daslogs.detectorz.average_value or metadata.entry.daslogs.sample_detector_distance.average_value
	 */
	@DataField private float detectorZ; 
	
	/**
	 * metadata.entry.daslogs.wavelength.average_value
	 */
	@DataField private float wavelength; 
	
	/**
	 * metadata.entry.daslogs.wavelength.average_value
	 */
	@DataField private float wavelength_spread; 
	
	/**
	 * metadata.entry.daslogs.beamstopy30.average_value
	 */
	@DataField private float beamstopy30;
	
	/**
	 * metadata.entry.daslogs.beamstopy60.average_value
	 */
	@DataField private float beamstopy60; 
	
	/**
	 * metadata.entry.daslogs.beamstopy90.average_value
	 */
	@DataField private float beamstopy90;
	
	/**
	 * metadata.entry.daslogs.cg[2|3]:cs:transscat.average_value
	 */
	@DataField private int transscatt; 
	
	/**
	 * metadata.entry.daslogs.cg[2|3]:cs:sampletype.average_value
	 */
	@DataField private int sampletype; 

	/**
	 * user entered value
	 */
	@DataField private String thickness;

	/**
	 * selected Run to use for reduction
	 */ 
	@DataField private Run transmission;

	/**
	 * selected Run to use for reduction
	 */
	@DataField private Run background;
	
	/**
	 * selected Run to use for reduction
	 */
	@DataField private Run empty;

	/**
	 * selected configuration to user for reduction
	 */
	@DataField private ReductionConfiguration configuration;

	/**
	 * user definable output filename
	 */
	@DataField private String outputFilename;

	/**
	 * user definable label
	 */	
	@DataField private String backLabel;
	
	/**
	 * user definable label
	 */
	@DataField private String emptyLabel;

	/**
	 * a hacky place to store just a value, not associated with any run, typically
	 * setting the transmission to _e.g._ 0.9
	 */
	@DataField private String value;


}
