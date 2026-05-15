package com.petrolpark.petrolsparts;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.petrolpark.compat.Mods;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class PetrolsPartsRemaps {

    public static final Map<ResourceLocation, CompatRecipeRemoval> COMPAT_RECIPE_REMOVALS = Stream.of(
        gearsNKineticsRecipeRemoval("crafting/cogwheel_from_conversion"),
        gearsNKineticsRecipeRemoval("crafting/large_cogwheel_from_conversion"),
        gearsNKineticsRecipeRemoval("crafting/hollow_cogwheel_from_conversion"),
        gearsNKineticsRecipeRemoval("crafting/hollow_large_cogwheel_from_conversion"),
        gearsNKineticsRecipeRemoval("crafting/shaftless_cogwheel_from_conversion"),
        gearsNKineticsRecipeRemoval("crafting/shaftless_large_cogwheel_from_conversion")
    ).collect(Collectors.toMap(CompatRecipeRemoval::id, Function.identity()));
    
    @SubscribeEvent
    public static final void onRegister(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();
        ResourceKey<?> key = registry.key();
        
        // Replace old Petrol's Parts components
        if (key == Registries.BLOCK || key == Registries.ITEM || key == Registries.BLOCK_ENTITY_TYPE) {
            registry.addAlias(PetrolsParts.asResource("double_cardan_shaft"), PetrolsPartsBlocks.CORNER_SHAFT.getId());
            registry.addAlias(PetrolsParts.asResource("long_shaft"), key == Registries.BLOCK_ENTITY_TYPE ? AllBlockEntityTypes.BRACKETED_KINETIC.getId() : AllBlocks.SHAFT.getId());
        };
        if (key == Registries.ITEM) {
            registry.addAlias(PetrolsParts.asResource("coaxial_gear"), PetrolsPartsItems.COAXIAL_COGWHEEL.getId());
            registry.addAlias(PetrolsParts.asResource("large_coaxial_gear"), PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL.getId());
        };
    };

    public record CompatRecipeRemoval(ResourceLocation id, Supplier<Boolean> condition) {};

    private static final CompatRecipeRemoval gearsNKineticsRecipeRemoval(String name) {
        return new CompatRecipeRemoval(Mods.CREATE_GEARS_N_KINETICS.asResource(name), () -> PetrolsPartsConfigs.common().removeCreateGearsNKineticsRecipes.get());
    };
};
