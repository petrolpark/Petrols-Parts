package com.petrolpark.petrolsparts.config;

import net.createmod.catnip.config.ConfigBase;

public class PPCCommon extends ConfigBase {

    public final ConfigGroup recipes = group(0, "recipes", "Recipes for Petrol's Parts components");
        public final ConfigBool brassDepot = b(true, "brassDepot", "Enable default Recipe for the Brass Depot");
        public final ConfigBool coaxialGears = b(true, "coaxialGears", "Enable default Recipes for the Coaxial Cogwheel and Large Coaxial Cogwheel");
        public final ConfigBool colossalCogwheel = b(true, "colossalCogwheel", "Enable default Recipe for the Colossal Cogwheel");
        public final ConfigBool cornerShaft = b(true, "cornerShaft", "Enable default Recipes for the Corner Shaft");
        public final ConfigBool differential = b(true, "differential", "Enable default Recipe for the Differential");
        public final ConfigBool hydraulicTransmission = b(true, "hydraulicTransmission", "Enable default Recipe for the Hydraulic Transmission");
        public final ConfigBool planetaryGearset = b(true, "planetaryGearset", "Enable default Recipe for the Planetary Gearset");
        public final ConfigBool pneumaticTube = b(true, "pneumaticTube", "Enable default Recipe for the Pneumatic Tube");
        public final ConfigBool redstoneProgrammer = b(true, "redstoneProgrammer", "Enable default Recipe for the Redstone Programmer");

    @Override
    public String getName() {
        return "common";
    };
    
};
