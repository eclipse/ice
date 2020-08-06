package org.eclipse.ice;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "ReductionConfiguration")
public class ReductionConfigurationSpec {
	
	//@DataField private String name; name is just how the configuration is referenced in the UI
	@DataField private String userOutputDir; //user defined subdirectory within the IPTS shared
	@DataField private String iptsSharedDir; //Base directory, IPTS shared
	@DataField private String outputDir; //Output directory, should be iptsSharedDir+userOutputDir
	
	@DataField private String sampeApertureSize;
	@DataField private String maskFileName;
	@DataField private boolean useMaskFileName;
	@DataField private boolean useDefaultMask;
	@DataField private String beamFluxFileName;
	@DataField private boolean useBeamFluxFileName;
	@DataField private boolean useDarkFileBlockedBeam;
	@DataField private boolean useBlockedBeam;
	@DataField private String darkFileName;
	@DataField private boolean useDarkFileName;
	@DataField private String sensitivityFileName;
	@DataField private boolean useSensitivityFileName;
	@DataField private String absoluteScale;
	@DataField private String normalization;
	@DataField private String fluxMonitorRatioFile;
	@DataField private String sampleOffset;
	@DataField private boolean useSampleOffset;
	@DataField private String detectorOffset;
	@DataField private boolean useDetectorOffset;
	@DataField private boolean useSolidAngleCorrection;
	@DataField private boolean useDetectorTubeType;
	@DataField private boolean useFlightPathCorrection;
	@DataField private boolean useThetaDepTransCorrection;
	@DataField private float mmRadiusForTransmission;
	@DataField.Default(value = "100") @DataField private int numQxQyBins;
	@DataField private String QbinType;
	@DataField.Default(value = "100") @DataField private int numQBins;
	@DataField.Default(value = "true") @DataField private boolean useErrorWeighting;
	@DataField private boolean useTOFcuts;
	@DataField private String TOFmin;
	@DataField private String TOFmax;
	@DataField private boolean useMaskBackTubes;
	@DataField private String wavelenStepType;
	@DataField private double wavelenStep;
}
