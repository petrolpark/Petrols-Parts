package com.petrolpark.petrolsparts;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.petrolpark.compat.Mods;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class PetrolsPartsRemaps {

    //TODO recipes
    public static final Map<ResourceLocation, CompatItemReplacement> COMPAT_ITEM_REPLACEMENTS = Stream.of(
        gearsNKineticsItemReplacement("shaftless_cogwheel", PetrolsPartsItems.SHAFTLESS_COGWHEEL),
        gearsNKineticsItemReplacement("shaftless_large_cogwheel", PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL),
        gearsNKineticsItemReplacement("hollow_cogwheel", PetrolsPartsItems.COAXIAL_COGWHEEL),
        gearsNKineticsItemReplacement("hollow_large_cogwheel", PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL)
    ).collect(Collectors.toMap(CompatItemReplacement::id, Function.identity()));
    
    @SubscribeEvent
    public static final void onRegister(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();
        ResourceKey<?> key = registry.key();
        
        // Replace old Petrol's Parts components
        if (key == Registries.BLOCK || key == Registries.ITEM || key == Registries.BLOCK_ENTITY_TYPE) {
            registry.addAlias(PetrolsParts.asResource("double_cardan_shaft"), PetrolsPartsBlocks.CORNER_SHAFT.getId());
        };
        if (key == Registries.ITEM) {
            registry.addAlias(PetrolsParts.asResource("coaxial_gear"), PetrolsPartsItems.COAXIAL_COGWHEEL.getId());
            registry.addAlias(PetrolsParts.asResource("large_coaxial_gear"), PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL.getId());
        };

        // Replace components of other mods, if enabled
        if (key == Registries.ITEM) {
            COMPAT_ITEM_REPLACEMENTS.forEach((id, r) -> registry.addAlias(id, r.replacement.getId()));
        };
    };

    // private static final ItemReplacement connected(String name, ItemEntry<?> replacement) {
    //     return new ItemReplacement(Mods.CREATE_CONNECTED.asResource(name), () -> PetrolsPartsConfigs.common().replaceCreateConnectedComponents.get(), replacement);
    // };

    private static final CompatItemReplacement gearsNKineticsItemReplacement(String name, ItemEntry<?> replacement) {
        return new CompatItemReplacement(Mods.CREATE_GEARS_N_KINETICS.asResource(name), () -> PetrolsPartsConfigs.common().replaceCreateGearsNKineticsComponents.get(), replacement);
    };

    public record CompatItemReplacement(ResourceLocation id, Supplier<Boolean> condition, ItemEntry<?> replacement) {};
};
