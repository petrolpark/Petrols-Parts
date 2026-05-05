package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageCog;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblageCogWheelBlockItem;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.ShaftHalfBlockItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class PetrolsPartsItems {

    public static final ItemEntry<AssemblageCogWheelBlockItem>
    
    SHAFTLESS_COGWHEEL = REGISTRATE.item("shaftless_cogwheel", AssemblageCog.SMALL::item)
        .register(),
    LARGE_SHAFTLESS_COGWHEEL = REGISTRATE.item("large_shaftless_cogwheel", AssemblageCog.LARGE::item)
        .register(),
    COAXIAL_COGWHEEL = REGISTRATE.item("coaxial_cogwheel", AssemblageCog.SMALL_COAXIAL::item)
        .register(),
    LARGE_COAXIAL_COGWHEEL = REGISTRATE.item("large_coaxial_cogwheel", AssemblageCog.LARGE_COAXIAL::item)
        .register();

    public static final ItemEntry<ShaftHalfBlockItem> SHAFT_HALF = REGISTRATE.item("shaft_half", ShaftHalfBlockItem::new)
        .register();

    public static final void register() {};
};
