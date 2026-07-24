package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import com.tterrag.registrate.util.entry.ItemEntry;

import petrolpark.mc.petrolsparts.config.PPCStress;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockItem;
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

    public static final ItemEntry<ShaftHalfBlockItem> SHAFT_HALF = REGISTRATE.item("shaft_half", p -> new ShaftHalfBlockItem(AssemblageSet.CREATE, p))
        .onRegister(AssemblageBlockItem.registerClientSet(() -> AssemblageSet.CREATE_CLIENT))
        .register();

    public static final ItemEntry<BevelCogWheelBlockItem> BEVEL_COGWHEEL = REGISTRATE.item("bevel_cogwheel", p -> new BevelCogWheelBlockItem(BevelCogWheelSet.CREATE, p))
        .onRegister(BevelCogWheelBlockItem.registerClientSet(() -> BevelCogWheelSet.CREATE_CLIENT))
        .register();

    public static final void register() {};

    private static final ItemEntry<AssemblageCogWheelBlockItem> assemblageCog(String name, AssemblageCog cog) {
        return REGISTRATE.item(name, p -> new AssemblageCogWheelBlockItem(AssemblageSet.CREATE, cog, p))
            .transform(PPCStress.setNoImpact())
            .onRegister(AssemblageBlockItem.registerClientSet(() -> AssemblageSet.CREATE_CLIENT))
            .register();
    };
};
