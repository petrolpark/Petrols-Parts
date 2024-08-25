package com.petrolpark.petrolsparts.ponder;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;

public class PetrolsPartsPonderIndex {
    
    public static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(PetrolsParts.MOD_ID);
    private static final PonderRegistrationHelper CREATE_HELPER = new PonderRegistrationHelper(Create.ID);

    public static void register() {

        // Coaxial Gear
        CREATE_HELPER.forComponents(PetrolsPartsBlocks.COAXIAL_GEAR)
            .addStoryBoard("cog/small", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogAsRelay);
        HELPER.forComponents(PetrolsPartsBlocks.COAXIAL_GEAR)
            .addStoryBoard("coaxial_gear/shaftless", PetrolsPartsScenes::coaxialGearShaftless)
            .addStoryBoard("coaxial_gear/through", PetrolsPartsScenes::coaxialGearThrough);

        // Colossal Cogwheel
        HELPER.forComponents(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .addStoryBoard("colossal_cogwheel", PetrolsPartsScenes::colossalCogwheel);

        // Differential
        HELPER.forComponents(PetrolsPartsBlocks.DIFFERENTIAL)
            .addStoryBoard("differential", PetrolsPartsScenes::differential);

        // Double Cardan Shaft
        HELPER.forComponents(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT)
            .addStoryBoard("double_cardan_shaft", PetrolsPartsScenes::doubleCardanShaft);

        // Large Coaxial Cogwheel
        CREATE_HELPER.forComponents(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            .addStoryBoard("cog/speedup", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogsSpeedUp)
            .addStoryBoard("cog/large", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::largeCogAsRelay, AllPonderTags.KINETIC_RELAYS);
        HELPER.forComponents(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            .addStoryBoard("coaxial_gear/shaftless", PetrolsPartsScenes::coaxialGearShaftless)
            .addStoryBoard("coaxial_gear/through", PetrolsPartsScenes::coaxialGearThrough);

        // Planetary Gearset
        CREATE_HELPER.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .addStoryBoard("cog/speedup", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogsSpeedUp)
            .addStoryBoard("cog/large", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::largeCogAsRelay);
        HELPER.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .addStoryBoard("planetary_gearset", PetrolsPartsScenes::planetaryGearset);
    };

    public static void registerTags() {
        PonderRegistry.TAGS.forTag(AllPonderTags.KINETIC_RELAYS)
            .add(PetrolsPartsBlocks.COAXIAL_GEAR)
            .add(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .add(PetrolsPartsBlocks.DIFFERENTIAL)
            .add(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT)
            .add(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            .add(PetrolsPartsBlocks.PLANETARY_GEARSET)
        ;
    };
};
