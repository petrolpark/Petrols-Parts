package com.petrolpark.petrolsparts.config;

import net.createmod.catnip.config.ConfigBase;

public class PPCCommon extends ConfigBase {

    public final ConfigGroup compat = group(0, "compat", "Compatibility with other mods");
        public final ConfigBool replaceCreateGearsNKineticsComponents = b(false, "replaceCreateGearsNKineticsComponents", "Replace Create: Gears 'n' Kinetics Shaftless and Hollow Cogwheels with Petrol's Parts equivalents");
        //public final ConfigBool replaceCreateConnectedComponents = b(false, "replaceCreateConnectedComponents", "Replace Create: Connected Shaftless Cogwheels with Petrol's Parts equivalents");

    public final ConfigGroup recipes = group(0, "recipes", "Recipes for Petrol's Parts components");
        public final ConfigBool bevelCogwheel = b(true, "bevelCogwheel", "Enable default Recipe for the Bevel Cogwheel");
        public final ConfigBool brassDepot = b(true, "brassDepot", "Enable default Recipe for the Brass Depot");
        public final ConfigBool assemblage = b(true, "assemblage", "Enable default Recipes for the Shaftless and Coaxial Small and Large Cogwheels");
        public final ConfigBool colossalCogwheel = b(true, "colossalCogwheel", "Enable default Recipe for the Colossal Cogwheel");
        public final ConfigBool cornerShaft = b(true, "cornerShaft", "Enable default Recipes for the Corner Shaft");
        public final ConfigBool differential = b(true, "differential", "Enable default Recipe for the Differential");
        public final ConfigBool hydraulicTransmission = b(true, "hydraulicTransmission", "Enable default Recipe for the Hydraulic Transmission");
        public final ConfigBool movement = b(true, "movement", "Enable default Recipe for the Movement");
        public final ConfigBool planetaryGearset = b(true, "planetaryGearset", "Enable default Recipe for the Planetary Gearset");
        public final ConfigBool pneumaticTube = b(true, "pneumaticTube", "Enable default Recipe for the Pneumatic Tube");
        public final ConfigBool redstoneProgrammer = b(true, "redstoneProgrammer", "Enable default Recipe for the Redstone Programmer");

    @Override
    public String getName() {
        return "common";
    };
    
};
