package org.eclipse.ice.data.neutrons;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "SANSInstrumentConfigurationSpec")
public class SANSInstrumentConfigurationSpec {
	
	//@DataField private String name; name is just how the configuration is referenced in the UI
	@DataField private String userOutputDir; //user defined subdirectory within the IPTS shared
	@DataField private String iptsSharedDir; //Base directory, IPTS shared
	@DataField private String outputDir; //Output directory, should be iptsSharedDir+userOutputDir
	
	@DataField private String sampeApertureSize;
	@DataField private String maskFileName;
	@DataField.Default(value = "false") @DataField private boolean useMaskFileName;
	@DataField.Default(value = "false") @DataField private boolean useDefaultMask;
	@DataField private String beamFluxFileName;
	@DataField.Default(value = "false") @DataField private boolean useBeamFluxFileName;
	@DataField.Default(value = "false") @DataField private boolean useDarkFileBlockedBeam;
	@DataField.Default(value = "false") @DataField private boolean useBlockedBeam;
	@DataField private String darkFileName;
	@DataField.Default(value = "false") @DataField private boolean useDarkFileName;
	@DataField private String sensitivityFileName;
	@DataField.Default(value = "false") @DataField private boolean useSensitivityFileName;
	@DataField private String absoluteScale;
	@DataField private String normalization;
	@DataField private String fluxMonitorRatioFile;
	@DataField private String sampleOffset;
	@DataField.Default(value = "false") @DataField private boolean useSampleOffset;
	@DataField private String detectorOffset;
	@DataField.Default(value = "false") @DataField private boolean useDetectorOffset;
	@DataField.Default(value = "false") @DataField private boolean useSolidAngleCorrection;
	@DataField.Default(value = "false") @DataField private boolean useDetectorTubeType;
	@DataField.Default(value = "false") @DataField private boolean useFlightPathCorrection;
	@DataField.Default(value = "false") @DataField private boolean useThetaDepTransCorrection;
	@DataField private float mmRadiusForTransmission;
	@DataField.Default(value = "100") @DataField private int numQxQyBins;
	@DataField private String QbinType;
	@DataField.Default(value = "100") @DataField private int numQBins;
	@DataField.Default(value = "false") @DataField private boolean useErrorWeighting;
	@DataField.Default(value = "false") @DataField private boolean useTOFcuts;
	@DataField private String TOFmin;
	@DataField private String TOFmax;
	@DataField.Default(value = "false") @DataField private boolean useMaskBackTubes;
	@DataField private String wavelenStepType;
	@DataField private double wavelenStep;
}
