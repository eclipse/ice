package org.eclipse.ice.data.neutrons;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "SANSInstrumentConfigurationSpec")
public class SANSInstrumentConfigurationSpec {
	
	//@DataField private String name; name is just how the configuration is referenced in the UI
	@DataField private String userOutputDir; //user defined subdirectory within the IPTS shared
	@DataField private String iptsSharedDir; //Base directory, IPTS shared
	@DataField private String outputDir; //Output directory, should be iptsSharedDir+userOutputDir
	
	@DataField private String sampeApertureSize; //sample aperture size(mm)
	@DataField private String maskFileName; //mask file that is set to be used
	@DataField.Default(value = "false") @DataField private boolean useMaskFileName; //option to use a mask file
	@DataField.Default(value = "false") @DataField private boolean useDefaultMask; //option to use default mask
	@DataField private String beamFluxFileName; //beam flux file that is set to be used
	@DataField.Default(value = "false") @DataField private boolean useBeamFluxFileName; //option to use beam flux file
	@DataField.Default(value = "false") @DataField private boolean useDarkFileBlockedBeam; //option to use either dark file or blocked beam
	@DataField.Default(value = "false") @DataField private boolean useBlockedBeam; //option to use blocked beam
	@DataField private String darkFileName; //dark file that is set to be used
	@DataField.Default(value = "false") @DataField private boolean useDarkFileName; //option to use dark file
	@DataField private String sensitivityFileName; //sensitivity file that is set to be used
	@DataField.Default(value = "false") @DataField private boolean useSensitivityFileName; //option to use sensitivity file
	@DataField private String absoluteScale; //the absolute scale used
	@DataField private String normalization; //normalization method used. Total charge, Monitor, Time, or None
	@DataField private String fluxMonitorRatioFile; //File used for normalization by monitor. Only used if normalization is "Monitor"
	@DataField private String sampleOffset; //sample offset (mm) to be used
	@DataField.Default(value = "false") @DataField private boolean useSampleOffset; //option to apply sample offset
	@DataField private String detectorOffset; //detector offset (mm) to be used
	@DataField.Default(value = "false") @DataField private boolean useDetectorOffset; //option to use detector offset
	@DataField.Default(value = "false") @DataField private boolean useSolidAngleCorrection; //option to use Solid Angle Correction
	@DataField.Default(value = "false") @DataField private boolean useDetectorTubeType; //option to use Detector Tube type
	@DataField.Default(value = "false") @DataField private boolean useFlightPathCorrection; //option to use Flight Path Correction
	@DataField.Default(value = "false") @DataField private boolean useThetaDepTransCorrection; //option to use Theta Dependent Transmission Correction
	@DataField private float mmRadiusForTransmission; //transmission radius (pixels)
	@DataField.Default(value = "100") @DataField private int numQxQyBins; //I(Qx, Qy) number of bins
	@DataField private String QbinType; //I(Q) binning type; linear or log
	@DataField.Default(value = "100") @DataField private int numQBins; //number of bins for 1D, I(Q)
	@DataField.Default(value = "false") @DataField private boolean useErrorWeighting; //option to bin with error weighting
	@DataField.Default(value = "false") @DataField private boolean useTOFcuts; //option to use TOF cutoffs
	@DataField private String TOFmin; //TOF minimum cutoff
	@DataField private String TOFmax; //TOF maximum cutoff
	@DataField.Default(value = "false") @DataField private boolean useMaskBackTubes; //option to mask back tubes
	@DataField private String wavelenStepType; //wavelength step type; constant lambda or constant delta-lambda/lambda 
	@DataField private double wavelenStep; //wavelength step size (Å)
}
