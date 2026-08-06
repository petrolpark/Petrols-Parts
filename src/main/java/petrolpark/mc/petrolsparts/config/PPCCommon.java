package petrolpark.mc.petrolsparts.config;

import net.createmod.catnip.config.ConfigBase;

public class PPCCommon extends ConfigBase {

    public final ConfigGroup compat = group(0, "compat", "Compatibility with other mods");
        public final ConfigBool removeCreateGearsNKineticsRecipes = b(false, "replaceCreateGearsNKineticsComponents", "Remove recipes for Create: Gears n' Kinetics Shaftless and Hollow Cogwheels (as they have Petrol's Parts equivalents)");
        //public final ConfigBool replaceCreateConnectedComponents = b(false, "replaceCreateConnectedComponents", "Replace Create: Connected Shaftless Cogwheels with Petrol's Parts equivalents");

    public final ConfigGroup recipes = group(0, "recipes", "recipes for Petrol's Parts components");
        public final ConfigBool bevelCogwheel = b(true, "bevelCogwheel", "Enable default recipe for the Bevel Cogwheel");
        public final ConfigBool brassDepot = b(true, "brassDepot", "Enable default recipe for the Brass Depot");
        public final ConfigBool assemblage = b(true, "assemblage", "Enable default recipes for the Shaftless and Coaxial Small and Large Cogwheels");
        public final ConfigBool colossalCogwheel = b(true, "colossalCogwheel", "Enable default recipe for the Colossal Cogwheel");
        public final ConfigBool cornerShaft = b(true, "cornerShaft", "Enable default recipes for the Corner Shaft");
        public final ConfigBool differential = b(true, "differential", "Enable default recipe for the Differential");
        public final ConfigBool frictionHeater = b(true, "frictionHeater", "Enable default recipe for the Friction Heater");
        public final ConfigBool hydraulicTransmission = b(true, "hydraulicTransmission", "Enable default recipe for the Hydraulic Transmission");
        public final ConfigBool movement = b(true, "movement", "Enable default recipe for the Movement");
        public final ConfigBool overloadClutch = b(true, "overloadClutch", "Enable default recipe for the Overload Clutch");
        public final ConfigBool planetaryGearset = b(true, "planetaryGearset", "Enable default recipe for the Planetary Gearset");
        public final ConfigBool pneumaticTube = b(true, "pneumaticTube", "Enable default recipe for the Pneumatic Tube");
        public final ConfigBool redstoneProgrammer = b(true, "redstoneProgrammer", "Enable default recipe for the Redstone Programmer");
        public final ConfigBool redstoneTransmission = b(true, "redstoneTransmission", "Enable default recipe for the Redstone Transmission");

    @Override
    public String getName() {
        return "common";
    };
    
};
