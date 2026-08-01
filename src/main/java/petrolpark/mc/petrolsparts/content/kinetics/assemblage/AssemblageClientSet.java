package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import com.simibubi.create.AllPartialModels;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;

public record AssemblageClientSet(
    // Cogwheel models
    PartialModel shaftlessCogWheel, PartialModel largeShaftlessCogWheel,
    PartialModel coaxialCogWheel, PartialModel largeCoaxialCogWheel,
    // Shaft half models
    PartialModel shaftHalfTop, PartialModel shaftHalfBottom,
    // Shaft models
    PartialModel shaftNone,
    PartialModel shaftAll, PartialModel shaftNoBottom, PartialModel shaftNoTop,
    PartialModel shaftMiddle, PartialModel shaftTop, PartialModel shaftBottom
) {

    public PartialModel getModel(AssemblageCog cog) {
        return switch (cog) {
            case LARGE -> largeShaftlessCogWheel();
            case SMALL_COAXIAL -> coaxialCogWheel();
            case LARGE_COAXIAL -> largeCoaxialCogWheel();
            default -> shaftlessCogWheel();
        };
    };

    @OnlyIn(Dist.CLIENT)
    public static final AssemblageClientSet CREATE = new AssemblageClientSet(
        // Cogwheel models
        AllPartialModels.SHAFTLESS_COGWHEEL, AllPartialModels.SHAFTLESS_LARGE_COGWHEEL,
        PetrolsPartsPartialModels.COAXIAL_COGWHEEL, PetrolsPartsPartialModels.LARGE_COAXIAL_COGWHEEL,
        // Shaft half models
        PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_HALF_TOP, PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_HALF_BOTTOM,
        // Shaft models
        AllPartialModels.SHAFT,
        PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_ALL, PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_NO_BOTTOM, PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_NO_TOP,
        AllPartialModels.COGWHEEL_SHAFT, PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_TOP, PetrolsPartsPartialModels.ASSEMBLAGE_SHAFT_BOTTOM
    );

    public static final AssemblageClientSet create() {
        return CREATE;
    };
};
