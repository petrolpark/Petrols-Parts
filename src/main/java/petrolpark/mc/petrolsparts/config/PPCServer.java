package petrolpark.mc.petrolsparts.config;

import net.createmod.catnip.config.ConfigBase;

public class PPCServer extends ConfigBase {

    public final ConfigInt redstoneTransmissionMaxLength = i(8, 0, 32, "redstoneTransmissionMaxLength", "Maximum length of a Redstone Transmission");

    public final PPCStress stress = nested(1, PPCStress::new, Comments.stress);

    public final ConfigGroup tubes = group(1, "tubes", Comments.tubes);
        public final ConfigFloat hydraulicTransmissionCost = f(2f, 0f, Float.MAX_VALUE, "hydraulicTransmissionCost", Comments.hydraulicTransmissionCost);
        public final ConfigFloat pneumaticTubeCost = f(0.5f, 0f, Float.MAX_VALUE, "pneumaticTubeCost", Comments.pneumaticTubeCost);
        public final ConfigFloat pneumaticTubeSpacing = f(1f, 0f, Float.MAX_VALUE, "pneumaticTubeSpacing", Comments.pneumaticTubeSpacing, Comments.meters);

    public final ConfigGroup frictionHeater = group(1, "frictionHeater", "Friction Heaters");
        public final ConfigInt heatedMinSpeedDifference = i(128, 1, "heatedMinSpeedDifference", "The minimum speed difference between the two wheels of a Friction Heater to be considered Heated");
        public final ConfigInt superHeatedMinSpeedDifference = i(288, 2, "superHeatedMinSpeedDifference", "The minimum speed difference between the two wheels of a Friction Heater to be considered Superheated");

    @Override
    public String getName() {
        return "server";
    };

    private static class Comments {
        static String

        tubes = "Tubes (Hydraulic Transmissions and Pneumatic Tubes)",
        hydraulicTransmissionCost = "Cost to build Hydraulic Transmissions per meter",
        pneumaticTubeCost = "Cost to build Pneumatic Tubes per meter",
        pneumaticTubeSpacing = "Minimum space between Items transported by the Pneumatic Tube",
        meters = "[in meters/block lengths]",
		stress = "Fine tune the kinetic stats of individual components";
	};
    
};
