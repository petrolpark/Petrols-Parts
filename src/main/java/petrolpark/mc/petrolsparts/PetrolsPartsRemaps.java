package petrolpark.mc.petrolsparts;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import petrolpark.mc.library.compat.Mods;

@EventBusSubscriber
public class PetrolsPartsRemaps {

    // Remove Items from JEI
    public static final List<CompatRemoval> COMPAT_ITEM_REMOVALS = Stream.of(
        gearsNKineticsRemoval("hollow_cogwheel"),
        gearsNKineticsRemoval("hollow_large_cogwheel"),
        gearsNKineticsRemoval("shaftless_cogwheel"),
        gearsNKineticsRemoval("shaftless_large_cogwheel")
    ).toList();

    // Remove recipes
    public static final Map<ResourceLocation, CompatRemoval> COMPAT_RECIPE_REMOVALS = Stream.of(
        gearsNKineticsRemoval("crafting/cogwheel_from_conversion"),
        gearsNKineticsRemoval("crafting/large_cogwheel_from_conversion"),
        gearsNKineticsRemoval("crafting/hollow_cogwheel_from_conversion"),
        gearsNKineticsRemoval("crafting/hollow_large_cogwheel_from_conversion"),
        gearsNKineticsRemoval("crafting/shaftless_cogwheel_from_conversion"),
        gearsNKineticsRemoval("crafting/shaftless_large_cogwheel_from_conversion")
    ).collect(Collectors.toMap(CompatRemoval::id, Function.identity()));
    
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
    

    public record CompatRemoval(ResourceLocation id, Supplier<Boolean> condition) {};

    private static final CompatRemoval gearsNKineticsRemoval(String name) {
        return new CompatRemoval(Mods.CREATE_GEARS_N_KINETICS.asResource(name), () -> PetrolsPartsConfigs.common().removeCreateGearsNKineticsRecipes.get());
    };
};
