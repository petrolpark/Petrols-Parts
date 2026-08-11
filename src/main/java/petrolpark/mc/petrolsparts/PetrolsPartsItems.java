package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import petrolpark.mc.library.PetrolparkTags;
import petrolpark.mc.library.compat.create.core.world.item.tooltip.ItemKineticStats;
import petrolpark.mc.petrolsparts.config.PPCStress;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageClientSet;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCogWheelBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageSet;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.ShaftHalfBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;

public class PetrolsPartsItems {

    public static final ItemEntry<AssemblageCogWheelBlockItem>
    
    SHAFTLESS_COGWHEEL = assemblageCog("shaftless_cogwheel", AssemblageCog.SMALL),
    LARGE_SHAFTLESS_COGWHEEL = assemblageCog("large_shaftless_cogwheel", AssemblageCog.LARGE),
    COAXIAL_COGWHEEL = assemblageCog("coaxial_cogwheel", AssemblageCog.SMALL_COAXIAL),
    LARGE_COAXIAL_COGWHEEL = assemblageCog("large_coaxial_cogwheel", AssemblageCog.LARGE_COAXIAL);

    public static final ItemEntry<ShaftHalfBlockItem> SHAFT_HALF = REGISTRATE.item("shaft_half", p -> new ShaftHalfBlockItem(AssemblageSet.VANILLA, p))
        .onRegister(AssemblageBlockItem.registerClientSet(AssemblageClientSet::vanilla))
        .register();

    public static final ItemEntry<BevelCogWheelBlockItem> BEVEL_COGWHEEL = REGISTRATE.item("bevel_cogwheel", p -> new BevelCogWheelBlockItem(BevelCogWheelSet.VANILLA, p))
        .onRegister(item -> TooltipModifier.REGISTRY.register(item, KineticStats.create(item)))
        .register();

    public static final ItemEntry<Item> HIGH_FRICTION_COMPOUND = REGISTRATE.item("high_friction_compound", Item::new)
        .defaultModel()
        .tag(PetrolparkTags.Items.FLAGGABLE.tag)
        .register();

    public static final void register() {};

    private static final ItemEntry<AssemblageCogWheelBlockItem> assemblageCog(String name, AssemblageCog cog) {
        return REGISTRATE.item(name, p -> new AssemblageCogWheelBlockItem(AssemblageSet.VANILLA, cog, p))
            .transform(PPCStress.setNoImpact())
            .onRegister(AssemblageBlockItem.registerClientSet(AssemblageClientSet::vanilla))
            .onRegister(item -> TooltipModifier.REGISTRY.register(item, new ItemKineticStats(item)))
            .register();
    };
};
