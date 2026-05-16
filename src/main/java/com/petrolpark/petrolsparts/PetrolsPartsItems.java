package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageCogWheelBlockItem;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.ShaftHalfBlockItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class PetrolsPartsItems {

    public static final ItemEntry<AssemblageCogWheelBlockItem>
    
    SHAFTLESS_COGWHEEL = REGISTRATE.item("shaftless_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.SMALL, p))
        .register(),
    LARGE_SHAFTLESS_COGWHEEL = REGISTRATE.item("large_shaftless_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.LARGE, p))
        .register(),
    COAXIAL_COGWHEEL = REGISTRATE.item("coaxial_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.SMALL_COAXIAL, p))
        .register(),
    LARGE_COAXIAL_COGWHEEL = REGISTRATE.item("large_coaxial_cogwheel", p -> new AssemblageCogWheelBlockItem(() -> AssemblageCog.LARGE_COAXIAL, p))
        .register();

    public static final ItemEntry<ShaftHalfBlockItem> SHAFT_HALF = REGISTRATE.item("shaft_half", ShaftHalfBlockItem::new)
        .register();

    public static final void register() {};
};
