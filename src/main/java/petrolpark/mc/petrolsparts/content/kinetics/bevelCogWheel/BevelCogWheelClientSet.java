package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import com.simibubi.create.AllPartialModels;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public record BevelCogWheelClientSet(
    PartialModel fourTeeth, PartialModel fiveTeeth,
    PartialModel shaft, PartialModel cogCap, PartialModel diagonalShaft
) {

    @OnlyIn(Dist.CLIENT)
    private static final BevelCogWheelClientSet VANILLA = new BevelCogWheelClientSet(
        PetrolsPartsPartialModels.BEVEL_COGWHEEL, PetrolsPartsPartialModels.BEVEL_COGWHEEL_FIVE_TEETH,
        AllPartialModels.SHAFT, PetrolsPartsPartialModels.BEVEL_COGWHEEL_CAP, PetrolsPartsPartialModels.BEVEL_COGWHEEL_DIAGONAL_SHAFT
    );

    public static final BevelCogWheelClientSet vanilla() {
        return VANILLA;
    };
};
