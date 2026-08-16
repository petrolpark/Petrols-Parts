package petrolpark.mc.petrolsparts.compat.jade;

import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class PetrolsPartsJade implements IWailaPlugin {
    
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.usePickedResult(PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE.get());
        registration.usePickedResult(PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.get());
        BevelCogWheelSet.VANILLA.get().streamBlocks().forEach(registration::usePickedResult);
    };
};
