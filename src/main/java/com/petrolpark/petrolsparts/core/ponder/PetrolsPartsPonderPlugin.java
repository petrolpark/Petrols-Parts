package com.petrolpark.petrolsparts.core.ponder;

import com.petrolpark.petrolsparts.PetrolsParts;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.PetrolsPartsItems;
import com.simibubi.create.Create;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.BeltScenes;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
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
        final PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> itemProviderHelper = helper.withKeyFunction(RegistryEntry::getId);

        itemProviderHelper.forComponents(PetrolsPartsBlocks.BRASS_DEPOT)
            .addStoryBoard("brass_depot", PetrolsPartsScenes::brassDepot);

        // Colossal Cogwheel
        itemProviderHelper.forComponents(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .addStoryBoard("colossal_cogwheel", PetrolsPartsScenes::colossalCogwheel);

        // Differential
        itemProviderHelper.forComponents(PetrolsPartsBlocks.DIFFERENTIAL)
            .addStoryBoard("differential", PetrolsPartsScenes::differential);

        // Double Corner Shaft
        itemProviderHelper.forComponents(PetrolsPartsBlocks.CORNER_SHAFT)
            .addStoryBoard("corner_shaft", PetrolsPartsScenes::cornerShaft);

        // Hydraulic Transmission
        itemProviderHelper.forComponents(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .addStoryBoard("hydraulic_transmission", PetrolsPartsScenes::hydraulicTransmission);

        // Planetary Gearset
        itemProviderHelper.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .addStoryBoard("planetary_gearset", PetrolsPartsScenes::planetaryGearset);
    
        itemProviderHelper.forComponents(PetrolsPartsBlocks.PNEUMATIC_TUBE)
            .addStoryBoard("pneumatic_tube", PetrolsPartsScenes::pneumaticTube);
    };

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        final PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        
        HELPER.addToTag(AllCreatePonderTags.ARM_TARGETS)
            .add(PetrolsPartsBlocks.BRASS_DEPOT);

        HELPER.addToTag(AllCreatePonderTags.THRESHOLD_SWITCH_TARGETS)
            .add(PetrolsPartsBlocks.MOVEMENT);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_SOURCES)
            .add(PetrolsPartsBlocks.MOVEMENT);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_RELAYS)
            .add(PetrolsPartsItems.SHAFT_HALF)
            .add(PetrolsPartsItems.SHAFTLESS_COGWHEEL)
            .add(PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL)
            .add(PetrolsPartsItems.COAXIAL_COGWHEEL)
            .add(PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL)
            .add(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .add(PetrolsPartsBlocks.DIFFERENTIAL)
            .add(PetrolsPartsBlocks.CORNER_SHAFT)
            .add(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .add(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .add(PetrolsPartsBlocks.TRANSMISSION)
        ;

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
            .add(PetrolsPartsBlocks.MOVEMENT)
            .add(PetrolsPartsBlocks.PNEUMATIC_TUBE);

        HELPER.addToTag(AllCreatePonderTags.LOGISTICS)
            .add(PetrolsPartsBlocks.PNEUMATIC_TUBE);

        HELPER.addToTag(AllCreatePonderTags.REDSTONE)
            .add(PetrolsPartsBlocks.TRANSMISSION);
    };

    /**
     * Adds Create scenes to Petrol's Parts blocks
     */
    public static class PetrolsPartsCreatePonderPlugin implements PonderPlugin {

        @Override
        public String getModId() {
            return Create.ID;
        };

        @Override
        public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
            final PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> itemProviderHelper = helper.withKeyFunction(RegistryEntry::getId);

            itemProviderHelper.forComponents(PetrolsPartsBlocks.BRASS_DEPOT)
                .addStoryBoard("depot", BeltScenes::depot, entry -> entry.orderBefore(PetrolsParts.MOD_ID, "brass_depot"));

            itemProviderHelper.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
                .addStoryBoard("cog/speedup", KineticsScenes::cogsSpeedUp, entry -> entry.orderBefore("cog/large"))
                .addStoryBoard("cog/large", KineticsScenes::largeCogAsRelay, entry -> entry.orderBefore(PetrolsParts.MOD_ID, "coaxial_gear/shaftless"));
        };
    };
};
