package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.petrolsparts.content.chained_cogwheel.ChainedCogwheelBlockEntity;
import com.petrolpark.petrolsparts.content.chained_cogwheel.ChainedCogwheelRenderer;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlockEntity;
import com.petrolpark.petrolsparts.content.coaxial_gear.LongShaftBlockEntity;
import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlockEntity;
import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelRenderer;
import com.petrolpark.petrolsparts.content.differential.DifferentialBlockEntity;
import com.petrolpark.petrolsparts.content.differential.DifferentialRenderer;
import com.petrolpark.petrolsparts.content.differential.DummyDifferentialBlockEntity;
import com.petrolpark.petrolsparts.content.double_cardan_shaft.DoubleCardanShaftBlockEntity;
import com.petrolpark.petrolsparts.content.double_cardan_shaft.DoubleCardanShaftInstance;
import com.petrolpark.petrolsparts.content.double_cardan_shaft.DoubleCardanShaftRenderer;
import com.petrolpark.petrolsparts.content.planetary_gearset.PlanetaryGearsetBlockEntity;
import com.petrolpark.petrolsparts.content.planetary_gearset.PlanetaryGearsetInstance;
import com.petrolpark.petrolsparts.content.planetary_gearset.PlanetaryGearsetRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class PetrolsPartsBlockEntityTypes {
    
    public static final BlockEntityEntry<ChainedCogwheelBlockEntity> CHAINED_COGWHEEL = REGISTRATE
        .blockEntity("chained_cogwheel", ChainedCogwheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.CHAINED_COGWHEEL, PetrolsPartsBlocks.CHAINED_LARGE_COGWHEEL)
        .renderer(() -> ChainedCogwheelRenderer::new)
        .register();

    public static final BlockEntityEntry<CoaxialGearBlockEntity> COAXIAL_GEAR = REGISTRATE
        .blockEntity("coaxial_gear", CoaxialGearBlockEntity::new)
        .instance(() -> BracketedKineticBlockEntityInstance::new, false)
        .validBlocks(PetrolsPartsBlocks.COAXIAL_GEAR, PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
        .renderer(() -> BracketedKineticBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<ColossalCogwheelBlockEntity> COLOSSAL_COGWHEEL = REGISTRATE
        .blockEntity("colossal_cogwheel", ColossalCogwheelBlockEntity::new)
        .validBlocks(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
        .renderer(() -> ColossalCogwheelRenderer::new)
        .register();

        public static final BlockEntityEntry<DoubleCardanShaftBlockEntity> DOUBLE_CARDAN_SHAFT = REGISTRATE
        .blockEntity("double_cardan_shaft", DoubleCardanShaftBlockEntity::new)
        .instance(() -> DoubleCardanShaftInstance::new)
        .validBlock(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT)
        .renderer(() -> DoubleCardanShaftRenderer::new)
        .register();

    public static final BlockEntityEntry<DifferentialBlockEntity> DIFFERENTIAL = REGISTRATE
        .blockEntity("differential", DifferentialBlockEntity::new)
        //TODO instance
        .validBlock(PetrolsPartsBlocks.DIFFERENTIAL)
        .renderer(() -> DifferentialRenderer::new)
        .register();

    public static final BlockEntityEntry<DummyDifferentialBlockEntity> DUMMY_DIFFERENTIAL = REGISTRATE
        .blockEntity("dummy_differential", DummyDifferentialBlockEntity::new)
        .validBlock(PetrolsPartsBlocks.DUMMY_DIFFERENTIAL)
        .register();

    public static final BlockEntityEntry<LongShaftBlockEntity> LONG_SHAFT = REGISTRATE
        .blockEntity("long_shaft", LongShaftBlockEntity::new)
        .instance(() -> BracketedKineticBlockEntityInstance::new, false)
        .validBlocks(PetrolsPartsBlocks.LONG_SHAFT)
        .renderer(() -> BracketedKineticBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<PlanetaryGearsetBlockEntity> PLANETARY_GEARSET = REGISTRATE
        .blockEntity("planetary_gearset", PlanetaryGearsetBlockEntity::new)
        .instance(() -> PlanetaryGearsetInstance::new, false)
        .validBlocks(PetrolsPartsBlocks.PLANETARY_GEARSET)
        .renderer(() -> PlanetaryGearsetRenderer::new)
        .register();

    public static final void register() {};
};
