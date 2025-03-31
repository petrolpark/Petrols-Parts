package com.petrolpark.petrolsparts.core.ponder;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class PetrolsPartsPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return PetrolsParts.MOD_ID;
    };

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        final PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        //TODO figure out way to add Create scenes to non-Create components

        // Coaxial Gear
        HELPER.forComponents(PetrolsPartsBlocks.COAXIAL_GEAR)
            //.addStoryBoard(Create.asResource("cog/small"), com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogAsRelay)
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

        // Hydraulic Transmission
        HELPER.forComponents(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .addStoryBoard("hydraulic_transmission", PetrolsPartsScenes::hydraulicTransmission);

        // Large Coaxial Cogwheel
        HELPER.forComponents(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            //.addStoryBoard(Create.asResource("cog/speedup"), com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogsSpeedUp)
            //.addStoryBoard(Create.asResource("cog/large"), com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::largeCogAsRelay, AllCreatePonderTags.KINETIC_RELAYS)
            .addStoryBoard("coaxial_gear/shaftless", PetrolsPartsScenes::coaxialGearShaftless)
            .addStoryBoard("coaxial_gear/through", PetrolsPartsScenes::coaxialGearThrough);

        // Planetary Gearset
        HELPER.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
            //.addStoryBoard(Create.asResource("cog/speedup"), com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogsSpeedUp)
            //.addStoryBoard(Create.asResource("cog/large"), com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::largeCogAsRelay)
            .addStoryBoard("planetary_gearset", PetrolsPartsScenes::planetaryGearset);
    
        HELPER.forComponents(PetrolsPartsBlocks.PNEUMATIC_TUBE)
            .addStoryBoard("pneumatic_tube", PetrolsPartsScenes::pneumaticTube);
    };

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        final PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        
        HELPER.addToTag(AllCreatePonderTags.KINETIC_RELAYS)
            .add(PetrolsPartsBlocks.COAXIAL_GEAR)
            .add(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .add(PetrolsPartsBlocks.DIFFERENTIAL)
            .add(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT)
            .add(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            .add(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .add(PetrolsPartsBlocks.PLANETARY_GEARSET)
        ;

        HELPER.addToTag(AllCreatePonderTags.LOGISTICS)
            .add(PetrolsPartsBlocks.PNEUMATIC_TUBE);
    };
};
