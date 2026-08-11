package petrolpark.mc.petrolsparts.core.ponder;

import com.simibubi.create.AllBlocks;
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
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.PetrolsPartsKineticsScenes;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageScenes;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelScenes;
import petrolpark.mc.petrolsparts.content.processing.PetrolsPartsProcessingScenes;

public class PetrolsPartsPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return PetrolsParts.MOD_ID;
    };

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        final PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> itemProviderHelper = helper.withKeyFunction(RegistryEntry::getId);

        // Assemblage
        itemProviderHelper.forComponents(PetrolsPartsItems.SHAFTLESS_COGWHEEL, PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL)
            .addStoryBoard("kinetics/assemblage/shaftless_cogwheel", AssemblageScenes::shaftlessCogwheels)
            .addStoryBoard("kinetics/assemblage/shaft", AssemblageScenes::shafts);
        itemProviderHelper.forComponents(PetrolsPartsItems.COAXIAL_COGWHEEL, PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL)
            .addStoryBoard("kinetics/assemblage/shaftless_cogwheel", AssemblageScenes::shaftlessCogwheels)
            .addStoryBoard("kinetics/assemblage/coaxial_cogwheel", AssemblageScenes::shafts);
        itemProviderHelper.forComponents(PetrolsPartsItems.SHAFT_HALF, AllBlocks.SHAFT)
            .addStoryBoard("kinetics/assemblage/shaftless_cogwheel", AssemblageScenes::shaftlessCogwheels)
            .addStoryBoard("kinetics/assemblage/shaft", AssemblageScenes::shafts);
        itemProviderHelper.forComponents(
            PetrolsPartsItems.SHAFTLESS_COGWHEEL, PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL,
            PetrolsPartsItems.COAXIAL_COGWHEEL, PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL,
            PetrolsPartsItems.SHAFT_HALF,
            AllBlocks.ANDESITE_CASING, AllBlocks.BRASS_CASING
        )
            .addStoryBoard("kinetics/assemblage/encasing", AssemblageScenes::encasing);

        // Bevel Cogwheel
        itemProviderHelper.forComponents(PetrolsPartsItems.BEVEL_COGWHEEL)
            .addStoryBoard("kinetics/bevel_cogwheel/orthogonal", BevelCogWheelScenes::orthogonal)
            .addStoryBoard("kinetics/bevel_cogwheel/diagonal", BevelCogWheelScenes::diagonal);
        itemProviderHelper.forComponents(PetrolsPartsItems.BEVEL_COGWHEEL, AllBlocks.ANDESITE_CASING, AllBlocks.BRASS_CASING)
            .addStoryBoard("kinetics/bevel_cogwheel/encasing", BevelCogWheelScenes::encasing);

        // Brass Depot
        itemProviderHelper.forComponents(PetrolsPartsBlocks.BRASS_DEPOT)
            .addStoryBoard("processing/brass_depot", PetrolsPartsProcessingScenes::brassDepot);

        // Colossal Cogwheel
        itemProviderHelper.forComponents(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .addStoryBoard("kinetics/colossal_cogwheel", PetrolsPartsKineticsScenes::colossalCogwheel);

        // Corner Shaft
        itemProviderHelper.forComponents(PetrolsPartsBlocks.CORNER_SHAFT)
            .addStoryBoard("kinetics/corner_shaft", PetrolsPartsKineticsScenes::cornerShaft);

        // // Differential
        // itemProviderHelper.forComponents(PetrolsPartsBlocks.DIFFERENTIAL)
        //     .addStoryBoard("kinetics/differential", PetrolsPartsScenes::differential);
    
        // Friction Heater
        itemProviderHelper.forComponents(PetrolsPartsBlocks.FRICTION_HEATER)
            .addStoryBoard("processing/friction_heater", PetrolsPartsProcessingScenes::frictionHeater);

        // Hydraulic Transmission
        itemProviderHelper.forComponents(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .addStoryBoard("kinetics/hydraulic_transmission", PetrolsPartsKineticsScenes::hydraulicTransmission);

        // Movement
        itemProviderHelper.forComponents(PetrolsPartsBlocks.MOVEMENT)
            .addStoryBoard("kinetics/movement/movement", PetrolsPartsKineticsScenes::movement)
            .addStoryBoard("kinetics/movement/battery", PetrolsPartsKineticsScenes::movementBattery);

        // Overload Clutch
        itemProviderHelper.forComponents(PetrolsPartsBlocks.OVERLOAD_CLUTCH)
            .addStoryBoard("kinetics/overload_clutch", PetrolsPartsKineticsScenes::overloadClutch);

        // Planetary Gearset
        itemProviderHelper.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .addStoryBoard("kinetics/planetary_gearset", PetrolsPartsKineticsScenes::planetaryGearset);
    
        // Pneumatic Tube
        itemProviderHelper.forComponents(PetrolsPartsBlocks.PNEUMATIC_TUBE)
            .addStoryBoard("logistics/pneumatic_tube", PetrolsPartsKineticsScenes::pneumaticTube);
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
            .add(PetrolsPartsItems.BEVEL_COGWHEEL)
            .add(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .add(PetrolsPartsBlocks.DIFFERENTIAL)
            .add(PetrolsPartsBlocks.CORNER_SHAFT)
            .add(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .add(PetrolsPartsBlocks.OVERLOAD_CLUTCH)
            .add(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .add(PetrolsPartsBlocks.REDSTONE_TRANSMISSION)
        ;

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
            .add(PetrolsPartsBlocks.FRICTION_HEATER)
            .add(PetrolsPartsBlocks.MOVEMENT)
            .add(PetrolsPartsBlocks.PNEUMATIC_TUBE);

        HELPER.addToTag(AllCreatePonderTags.LOGISTICS)
            .add(PetrolsPartsBlocks.PNEUMATIC_TUBE);
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

            // Assemblage
            itemProviderHelper.forComponents(PetrolsPartsItems.SHAFTLESS_COGWHEEL, PetrolsPartsItems.COAXIAL_COGWHEEL)
                .addStoryBoard("cog/small", KineticsScenes::cogAsRelay, entry -> entry.orderBefore("cog/speedup"))
                .addStoryBoard("cog/speedup", KineticsScenes::cogsSpeedUp, entry -> entry.orderBefore(PetrolsParts.MOD_ID, "kinetics/assemblage/shaftless_cogwheel"));
            itemProviderHelper.forComponents(PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL, PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL)
                .addStoryBoard("cog/speedup", KineticsScenes::cogsSpeedUp, entry -> entry.orderBefore(PetrolsParts.MOD_ID, "kinetics/assemblage/shaftless_cogwheel"));

            // Brass Depot
            itemProviderHelper.forComponents(PetrolsPartsBlocks.BRASS_DEPOT)
                .addStoryBoard("depot", BeltScenes::depot, entry -> entry.orderBefore(PetrolsParts.MOD_ID, "brass_depot"));

            // Planetary Gearset
            itemProviderHelper.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
                .addStoryBoard("cog/speedup", KineticsScenes::cogsSpeedUp, entry -> entry.orderBefore("cog/large"))
                .addStoryBoard("cog/large", KineticsScenes::largeCogAsRelay, entry -> entry.orderBefore(PetrolsParts.MOD_ID, "kinetics/planetary_gearset"));
        };
    };
};
