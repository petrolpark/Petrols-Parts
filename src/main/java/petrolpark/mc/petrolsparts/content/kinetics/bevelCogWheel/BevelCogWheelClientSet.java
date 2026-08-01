package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public record BevelCogWheelClientSet(
    PartialModel fourTeeth, PartialModel fiveTeeth
) {
    
    @OnlyIn(Dist.CLIENT)
    public static final BevelCogWheelClientSet CREATE = new BevelCogWheelClientSet(
        PetrolsPartsPartialModels.BEVEL_COGWHEEL,
        PetrolsPartsPartialModels.BEVEL_COGWHEEL_FIVE_TEETH
    );

    public static final BevelCogWheelClientSet create() {
        return CREATE;
    };
};
