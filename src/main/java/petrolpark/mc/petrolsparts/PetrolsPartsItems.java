package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import com.tterrag.registrate.util.entry.ItemEntry;

import petrolpark.mc.petrolsparts.config.PPCStress;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageCogWheelBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.ShaftHalfBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelItem;

public class PetrolsPartsItems {

    public static final ItemEntry<AssemblageCogWheelBlockItem>
    
    SHAFTLESS_COGWHEEL = REGISTRATE.item("shaftless_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.SMALL, p))
        .transform(PPCStress.setNoImpact())
        .register(),
    LARGE_SHAFTLESS_COGWHEEL = REGISTRATE.item("large_shaftless_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.LARGE, p))
        .register(),
    COAXIAL_COGWHEEL = REGISTRATE.item("coaxial_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.SMALL_COAXIAL, p))
        .register(),
    LARGE_COAXIAL_COGWHEEL = REGISTRATE.item("large_coaxial_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.LARGE_COAXIAL, p))
        .register();

    public static final ItemEntry<ShaftHalfBlockItem> SHAFT_HALF = REGISTRATE.item("shaft_half", ShaftHalfBlockItem::new)
        .register();

    public static final ItemEntry<BevelCogWheelItem> BEVEL_COGWHEEL = REGISTRATE.item("bevel_cogwheel", BevelCogWheelItem::new)
        .register();

    public static final void register() {};
};
