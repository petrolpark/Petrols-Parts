package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.compat.create.core.tube.TubeBlockItem;
import com.petrolpark.petrolsparts.config.PPCStress;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.SeparateShaftHalvesAssemblageBlock;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.CoaxialGearBlock;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.CoaxialGearBlockItem;
import com.petrolpark.petrolsparts.content.kinetics.coaxialGear.LongShaftBlock;
import com.petrolpark.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlock;
import com.petrolpark.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockItem;
import com.petrolpark.petrolsparts.content.kinetics.cornerShaft.AbstractCornerShaftBlock;
import com.petrolpark.petrolsparts.content.kinetics.cornerShaft.CornerShaftBlock;
import com.petrolpark.petrolsparts.content.kinetics.cornerShaft.EncasedCornerShaftBlock;
import com.petrolpark.petrolsparts.content.kinetics.differential.DifferentialBlock;
import com.petrolpark.petrolsparts.content.kinetics.differential.DummyDifferentialBlock;
import com.petrolpark.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionBlock;
import com.petrolpark.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetBlock;
import com.petrolpark.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlock;
import com.petrolpark.petrolsparts.content.processing.brassDepot.BrassDepotBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class PetrolsPartsBlocks {

    public static final BlockEntry<SeparateShaftHalvesAssemblageBlock> SEPARATE_SHAFT_HALVES_ASSEMBLAGE = REGISTRATE.block("separate_shaft_halves_assemblage", SeparateShaftHalvesAssemblageBlock::new)
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p.noOcclusion())
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<BrassDepotBlock> BRASS_DEPOT = REGISTRATE.block("brass_depot", BrassDepotBlock::new)
        .initialProperties(SharedProperties::softMetal)
		.properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW))
        .defaultLoot()
        .transform(TagGen.axeOrPickaxe())
        .transform(DisplaySource.displaySource(AllDisplaySources.ITEM_NAMES))
		.onRegister(MovingInteractionBehaviour.interactionBehaviour(new MountedDepotInteractionBehaviour()))
		.transform(MountedItemStorageType.mountedItemStorage(AllMountedStorageTypes.DEPOT))
		.item()
        .build()
        .register();
    
    public static final BlockEntry<CoaxialGearBlock> COAXIAL_GEAR = REGISTRATE.block("coaxial_gear", CoaxialGearBlock::small)
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p
            .sound(SoundType.WOOD)
            .mapColor(MapColor.DIRT)
            .noOcclusion()
        ).defaultLoot()
        .transform(PPCStress.setNoImpact())
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .transform(TagGen.axeOrPickaxe())
        .item(CoaxialGearBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<CoaxialGearBlock> LARGE_COAXIAL_GEAR = REGISTRATE.block("large_coaxial_gear", CoaxialGearBlock::large)
        .initialProperties(COAXIAL_GEAR)
        .defaultLoot()
        .transform(PPCStress.setNoImpact())
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .transform(TagGen.axeOrPickaxe())
        .item(CoaxialGearBlockItem::new)
        .build()
        .register();

    // public static final BlockEntry<ChainedCogwheelBlock> CHAINED_COGWHEEL = REGISTRATE.block("chained_cogwheel", ChainedCogwheelBlock::small)
    //     .initialProperties(AllBlocks.COGWHEEL)
    //     .properties(p -> p
    //         .noOcclusion()
    //     ).transform(PPCStress.setNoImpact())
    //     .register();

    // public static final BlockEntry<ChainedCogwheelBlock> CHAINED_LARGE_COGWHEEL = REGISTRATE.block("chained_large_cogwheel", ChainedCogwheelBlock::large)
    //     .initialProperties(CHAINED_COGWHEEL)
    //     .properties(p -> p
    //         .noOcclusion()
    //     ).transform(PPCStress.setNoImpact())
    //     .register();

    public static final BlockEntry<ColossalCogwheelBlock> COLOSSAL_COGWHEEL = REGISTRATE.block("colossal_cogwheel", ColossalCogwheelBlock::new)
        .initialProperties(AllBlocks.LARGE_WATER_WHEEL)
        .defaultLoot()
        .properties(p -> p
            .noOcclusion()
        ).transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .item(ColossalCogwheelBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<DifferentialBlock> DIFFERENTIAL = REGISTRATE.block("differential", DifferentialBlock::new)
        .initialProperties(AllBlocks.LARGE_COGWHEEL)
        .defaultLoot()
        .properties(p -> p
            .noOcclusion()
            .sound(SoundType.WOOD)
		    .mapColor(MapColor.DIRT)
        ).transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .item(CogwheelBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<DummyDifferentialBlock> DUMMY_DIFFERENTIAL = REGISTRATE.block("dummy_differential", DummyDifferentialBlock::new)
        .initialProperties(DIFFERENTIAL)
        .defaultLoot()
        .transform(PPCStress.setNoImpact())
        .register();

    public static final BlockEntry<CornerShaftBlock> CORNER_SHAFT = REGISTRATE.block("corner_shaft", CornerShaftBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .properties(p -> p
            .mapColor(MapColor.METAL)
            .noOcclusion()
        ).defaultLoot()
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.pickaxeOnly())
        .item()
        .build()
        .register();

    public static final BlockEntry<EncasedCornerShaftBlock> ANDESITE_ENCASED_CORNER_SHAFT = REGISTRATE.block("andesite_encased_corner_shaft", p -> new EncasedCornerShaftBlock(p, AllBlocks.ANDESITE_CASING::get))
        .initialProperties(SharedProperties::stone)
        .properties(p -> p
            .noOcclusion()
            .mapColor(MapColor.PODZOL)
        ).transform(PPCStress.setNoImpact())
        .loot((p, lb) -> p.dropOther(lb, CORNER_SHAFT))
        .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING)))
		.onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.ANDESITE_CASING, (s, f) -> !AbstractCornerShaftBlock.hasShaftTowards(s, f))))
		.transform(TagGen.axeOrPickaxe())
		.register();

    public static final BlockEntry<EncasedCornerShaftBlock> BRASS_ENCASED_CORNER_SHAFT = REGISTRATE.block("brass_encased_corner_shaft", p -> new EncasedCornerShaftBlock(p, AllBlocks.BRASS_CASING::get))
        .initialProperties(SharedProperties::stone)
        .properties(p -> p
            .noOcclusion()
            .mapColor(MapColor.TERRACOTTA_BROWN)
        ).transform(PPCStress.setNoImpact())
        .loot((p, lb) -> p.dropOther(lb, CORNER_SHAFT))
        .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.BRASS_CASING)))
		.onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.BRASS_CASING, (s, f) -> !AbstractCornerShaftBlock.hasShaftTowards(s, f))))
		.transform(TagGen.axeOrPickaxe())
		.register();

    public static final BlockEntry<HydraulicTransmissionBlock> HYDRAULIC_TRANSMISSION = REGISTRATE.block("hydraulic_transmission", HydraulicTransmissionBlock::new)
        .initialProperties(AllBlocks.MECHANICAL_CRAFTER)
        .properties(p -> p
            .noOcclusion()
        ).defaultLoot()
        .transform(PPCStress.setImpact(2.0))
        .transform(TagGen.axeOrPickaxe())
        .item(TubeBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<LongShaftBlock> LONG_SHAFT = REGISTRATE.block("long_shaft", LongShaftBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .loot((lt, b) -> lt.dropOther(b, AllBlocks.SHAFT))
        .transform(PPCStress.setNoImpact())
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .register();

    public static final BlockEntry<PlanetaryGearsetBlock> PLANETARY_GEARSET = REGISTRATE.block("planetary_gearset", PlanetaryGearsetBlock::new)
        .initialProperties(AllBlocks.LARGE_COGWHEEL)
        .properties(p -> p
            .noOcclusion()
            .sound(SoundType.WOOD)
		    .mapColor(MapColor.DIRT)
        ).defaultLoot()
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .item(CogwheelBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<PneumaticTubeBlock> PNEUMATIC_TUBE = REGISTRATE.block("pneumatic_tube", PneumaticTubeBlock::filterable)
        .initialProperties(HYDRAULIC_TRANSMISSION)
        .properties(p -> p
            .noOcclusion()
        ).defaultLoot()
        .transform(PPCStress.setImpact(2.0))
        .transform(TagGen.axeOrPickaxe())
        .item(TubeBlockItem::new)
        .build()
        .register();

    public static final void register() {};

};
