package com.petrolpark.petrolsparts;

import static com.petrolpark.petrolsparts.PetrolsParts.REGISTRATE;

import com.petrolpark.petrolsparts.content.chained_cogwheel.ChainedCogwheelBlock;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlock;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlockItem;
import com.petrolpark.petrolsparts.content.coaxial_gear.LongShaftBlock;
import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlock;
import com.petrolpark.petrolsparts.content.colossal_cogwheel.ColossalCogwheelBlockItem;
import com.petrolpark.petrolsparts.content.differential.DifferentialBlock;
import com.petrolpark.petrolsparts.content.differential.DummyDifferentialBlock;
import com.petrolpark.petrolsparts.content.double_cardan_shaft.DoubleCardanShaftBlock;
import com.petrolpark.petrolsparts.content.planetary_gearset.PlanetaryGearsetBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class PetrolsPartsBlocks {
    
    public static final BlockEntry<CoaxialGearBlock> COAXIAL_GEAR = REGISTRATE.block("coaxial_gear", CoaxialGearBlock::small)
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p
            .sound(SoundType.WOOD)
            .mapColor(MapColor.DIRT)
            .noOcclusion()
        ).onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .transform(TagGen.axeOrPickaxe())
        .item(CoaxialGearBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<CoaxialGearBlock> LARGE_COAXIAL_GEAR = REGISTRATE.block("large_coaxial_gear", CoaxialGearBlock::large)
        .initialProperties(COAXIAL_GEAR)
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .transform(TagGen.axeOrPickaxe())
        .item(CoaxialGearBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<ChainedCogwheelBlock> CHAINED_COGWHEEL = REGISTRATE.block("chained_cogwheel", ChainedCogwheelBlock::small)
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p
            .noOcclusion()
        ).register();

    public static final BlockEntry<ChainedCogwheelBlock> CHAINED_LARGE_COGWHEEL = REGISTRATE.block("chained_large_cogwheel", ChainedCogwheelBlock::large)
        .initialProperties(CHAINED_COGWHEEL)
        .properties(p -> p
            .noOcclusion()
        ).register();

    public static final BlockEntry<ColossalCogwheelBlock> COLOSSAL_COGWHEEL = REGISTRATE.block("colossal_cogwheel", ColossalCogwheelBlock::new)
        .initialProperties(AllBlocks.LARGE_WATER_WHEEL)
        .properties(p -> p
            .noOcclusion()
        ).item(ColossalCogwheelBlockItem::new)
        .transform(ModelGen.customItemModel())
        .register();

    public static final BlockEntry<DifferentialBlock> DIFFERENTIAL = REGISTRATE.block("differential", DifferentialBlock::new)
        .initialProperties(AllBlocks.LARGE_COGWHEEL)
        .properties(p -> p
            .noOcclusion()
            .sound(SoundType.WOOD)
		    .mapColor(MapColor.DIRT)
        ).transform(TagGen.axeOrPickaxe())
        .item(CogwheelBlockItem::new)
        .transform(ModelGen.customItemModel())
        .register();

    public static final BlockEntry<DummyDifferentialBlock> DUMMY_DIFFERENTIAL = REGISTRATE.block("dummy_differential", DummyDifferentialBlock::new)
        .initialProperties(DIFFERENTIAL)
        .register();

    public static final BlockEntry<DoubleCardanShaftBlock> DOUBLE_CARDAN_SHAFT = REGISTRATE.block("double_cardan_shaft", DoubleCardanShaftBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .properties(p -> p
            .mapColor(MapColor.METAL)
            .noOcclusion()
        ).transform(TagGen.pickaxeOnly())
        .item()
        .transform(ModelGen.customItemModel())
        .register();

    public static final BlockEntry<LongShaftBlock> LONG_SHAFT = REGISTRATE.block("long_shaft", LongShaftBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .register();

    public static final BlockEntry<PlanetaryGearsetBlock> PLANETARY_GEARSET = REGISTRATE.block("planetary_gearset", PlanetaryGearsetBlock::new)
        .initialProperties(AllBlocks.LARGE_COGWHEEL)
        .properties(p -> p
            .noOcclusion()
            .sound(SoundType.WOOD)
		    .mapColor(MapColor.DIRT)
        ).transform(TagGen.axeOrPickaxe())
        .item(CogwheelBlockItem::new)
        .transform(ModelGen.customItemModel())
        .register();

    public static final void register() {};

};
