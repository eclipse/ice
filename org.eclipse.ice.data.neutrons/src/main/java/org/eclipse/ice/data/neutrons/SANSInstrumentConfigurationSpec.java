package org.eclipse.ice.data.neutrons;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "SANSInstrumentConfigurationSpec")
public class SANSInstrumentConfigurationSpec {
	
	
	/**
	 * user defined subdirectory within the IPTS shared
	 */
	@DataField private String userOutputDir; 
	
	/**
	 *  base directory, IPTS shared
	 */
	@DataField private String iptsSharedDir;
	
	/**
	 * output directory, should be iptsSharedDir+userOutputDir
	 */
	@DataField private String outputDir; 
	
	/**
	 * sample aperture size(mm)
	 */
	@DataField private String sampeApertureSize; 
	
	/**
	 * mask file that is set to be used
	 */
	@DataField private String maskFileName;
	
	/**
	 * option to use a mask file
	 */
	@DataField.Default(value = "false") @DataField private boolean useMaskFileName; 
	
	/**
	 * option to use default mask
	 */
	@DataField.Default(value = "false") @DataField private boolean useDefaultMask; 
	
	/**
	 * beam flux file that is set to be used
	 */
	@DataField private String beamFluxFileName; 
	
	/**
	 * option to use beam flux file
	 */
	@DataField.Default(value = "false") @DataField private boolean useBeamFluxFileName;
	
	/**
	 * option to use either dark file or blocked beam
	 */
	@DataField.Default(value = "false") @DataField private boolean useDarkFileBlockedBeam; 
	
	/**
	 * option to use blocked beam
	 */
	@DataField.Default(value = "false") @DataField private boolean useBlockedBeam; 
	
	/**
	 * dark file that is set to be used
	 */
	@DataField private String darkFileName; 
	
	/**
	 * option to use dark file
	 */
	@DataField.Default(value = "false") @DataField private boolean useDarkFileName;
	
	/**
	 * sensitivity file that is set to be used
	 */
	@DataField private String sensitivityFileName;
	
	/**
	 * option to use sensitivity file
	 */
	@DataField.Default(value = "false") @DataField private boolean useSensitivityFileName;
	
	/**
	 * the absolute scale used
	 */
	@DataField private String absoluteScale;
	
	/**
	 * normalization method used. Total charge, Monitor, Time, or None
	 */
	@DataField private String normalization; 
	
	/**
	 * file used for normalization by monitor. Only used if normalization is "Monitor"
	 */
	@DataField private String fluxMonitorRatioFile; 
	
	/**
	 * sample offset (mm) to be used
	 */
	@DataField private String sampleOffset;
	
	/**
	 * option to apply sample offset
	 */
	@DataField.Default(value = "false") @DataField private boolean useSampleOffset; 
	
	/**
	 * detector offset (mm) to be used
	 */
	@DataField private String detectorOffset;
	
	/**
	 * option to use detector offset
	 */
	@DataField.Default(value = "false") @DataField private boolean useDetectorOffset;
	
	/**
	 * option to use Solid Angle Correction
	 */
	@DataField.Default(value = "false") @DataField private boolean useSolidAngleCorrection;
	
	/**
	 * option to use Detector Tube type
	 */
	@DataField.Default(value = "false") @DataField private boolean useDetectorTubeType;
	
	/**
	 * option to use Flight Path Correction
	 */
	@DataField.Default(value = "false") @DataField private boolean useFlightPathCorrection;
	
	/**
	 * option to use Theta Dependent Transmission Correction
	 */
	@DataField.Default(value = "false") @DataField private boolean useThetaDepTransCorrection;
	
	/**
	 * transmission radius (pixels)
	 */
	@DataField private float mmRadiusForTransmission;
	
	/**
	 * I(Qx, Qy) number of bins
	 */
	@DataField.Default(value = "100") @DataField private int numQxQyBins; 
	
	/**
	 * I(Q) binning type; linear or log
	 */
	@DataField private String QbinType; 
	
	/**
	 * number of bins for 1D, I(Q)
	 */
	@DataField.Default(value = "100") @DataField private int numQBins;
	
	/**
	 * option to bin with error weighting
	 */
	@DataField.Default(value = "false") @DataField private boolean useErrorWeighting;
	
	/**
	 * option to use TOF cutoffs
	 */
	@DataField.Default(value = "false") @DataField private boolean useTOFcuts;
	
	/**
	 * TOF minimum cutoff
	 */
	@DataField private String TOFmin;
	
	/**
	 * TOF maximum cutoff
	 */
	@DataField private String TOFmax;
	
	/**
	 * option to mask back tubes
	 */
	@DataField.Default(value = "false") @DataField private boolean useMaskBackTubes;
	
	/**
	 * wavelength step type; constant lambda or constant delta-lambda/lambda 
	 */
	@DataField private String wavelenStepType;
	
	/**
	 * wavelength step size (Å)
	 */
	@DataField private double wavelenStep;
}
