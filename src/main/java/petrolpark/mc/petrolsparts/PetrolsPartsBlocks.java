package petrolpark.mc.petrolsparts;

import static petrolpark.mc.petrolsparts.PetrolsParts.REGISTRATE;

import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import petrolpark.mc.library.PetrolparkTags;
import petrolpark.mc.library.compat.create.core.world.block.tube.TubeBlockItem;
import petrolpark.mc.petrolsparts.config.PPCStress;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageSet;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.EncasedAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.EncasedAssemblageBlockDataGen;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.EncasedAssemblageCTBehaviour;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.EncasedSeparateShaftHalvesAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.EncasedSingleShaftAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.SeparateShaftHalvesAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.SingleShaftAssemblageBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelSet;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IEncasedBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.DualDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.EncasedDualDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.BevelCogWheelAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.CornerBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.FourBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.ThreeBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.FourBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SingleAxisBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.ThreeBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.colossalCogwheel.ColossalCogwheelBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.AbstractCornerShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.CornerShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.CornerShaftBlockItem;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.EncasedCornerShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.EncasedStraightCornerShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.cornerShaft.StraightCornerShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.differential.DifferentialBlock;
import petrolpark.mc.petrolsparts.content.kinetics.hydraulicTransmission.HydraulicTransmissionBlock;
import petrolpark.mc.petrolsparts.content.kinetics.movement.MovementBlock;
import petrolpark.mc.petrolsparts.content.kinetics.overloadClutch.OverloadClutchBlock;
import petrolpark.mc.petrolsparts.content.kinetics.planetaryGearset.PlanetaryGearsetBlock;
import petrolpark.mc.petrolsparts.content.kinetics.redstoneTransmission.TransmissionBlock;
import petrolpark.mc.petrolsparts.content.legacy.LegacyCoaxialGearBlock;
import petrolpark.mc.petrolsparts.content.logistics.pneumaticTube.PneumaticTubeBlock;
import petrolpark.mc.petrolsparts.content.processing.brassDepot.BrassDepotBlock;
import petrolpark.mc.petrolsparts.content.processing.frictionHeater.FrictionHeaterBlock;
import petrolpark.mc.petrolsparts.core.PetrolsPartsRegistrate;

public class PetrolsPartsBlocks {

    // ASSEMBLAGES

    public static final BlockEntry<SeparateShaftHalvesAssemblageBlock> SEPARATE_SHAFT_HALVES_ASSEMBLAGE = REGISTRATE.block("separate_shaft_halves_assemblage", AssemblageBlock.create(AssemblageSet.VANILLA, SeparateShaftHalvesAssemblageBlock::new))
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p
            .noOcclusion()
            .noLootTable()
        ).transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<SingleShaftAssemblageBlock> SINGLE_SHAFT_ASSEMBLAGE = REGISTRATE.block("single_shaft_assemblage", AssemblageBlock.create(AssemblageSet.VANILLA, SingleShaftAssemblageBlock::new))
        .initialProperties(SEPARATE_SHAFT_HALVES_ASSEMBLAGE)
        .properties(p -> p
            .noLootTable()
        ).transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<EncasedSeparateShaftHalvesAssemblageBlock> ANDESITE_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE = REGISTRATE.block("andesite_encased_separate_shaft_halves_assemblage", EncasedAssemblageBlock.andesite(AssemblageSet.VANILLA, EncasedSeparateShaftHalvesAssemblageBlock::new))
        .initialProperties(AllBlocks.ANDESITE_ENCASED_COGWHEEL)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate(EncasedAssemblageBlockDataGen.separateShaftHalvesBlockState("andesite"))
        .loot(EncasedAssemblageBlockDataGen::separateShaftHalvesLoot)
        .transform(EncasingRegistry.addVariantTo(SEPARATE_SHAFT_HALVES_ASSEMBLAGE))
        .onRegister(EncasedAssemblageBlock.registerCTs(() -> EncasedAssemblageCTBehaviour.ANDESITE))
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<EncasedSingleShaftAssemblageBlock> ANDESITE_ENCASED_SINGLE_SHAFT_ASSEMBLAGE = REGISTRATE.block("andesite_encased_single_shaft_assemblage", EncasedAssemblageBlock.andesite(AssemblageSet.VANILLA, EncasedSingleShaftAssemblageBlock::new))
        .initialProperties(ANDESITE_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate(EncasedAssemblageBlockDataGen.singleShaftBlockState("andesite"))
        .loot(EncasedAssemblageBlockDataGen::singleShaftLoot)
        .transform(EncasingRegistry.addVariantTo(SINGLE_SHAFT_ASSEMBLAGE))
        .onRegister(EncasedAssemblageBlock.registerCTs(() -> EncasedAssemblageCTBehaviour.ANDESITE))
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<EncasedSeparateShaftHalvesAssemblageBlock> BRASS_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE = REGISTRATE.block("brass_encased_separate_shaft_halves_assemblage", EncasedAssemblageBlock.brass(AssemblageSet.VANILLA, EncasedSeparateShaftHalvesAssemblageBlock::new))
        .initialProperties(AllBlocks.BRASS_ENCASED_COGWHEEL)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate(EncasedAssemblageBlockDataGen.separateShaftHalvesBlockState("brass"))
        .loot(EncasedAssemblageBlockDataGen::separateShaftHalvesLoot)
        .transform(EncasingRegistry.addVariantTo(SEPARATE_SHAFT_HALVES_ASSEMBLAGE))
        .onRegister(EncasedAssemblageBlock.registerCTs(() -> EncasedAssemblageCTBehaviour.BRASS))
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<EncasedSingleShaftAssemblageBlock> BRASS_ENCASED_SINGLE_SHAFT_ASSEMBLAGE = REGISTRATE.block("brass_encased_single_shaft_assemblage", EncasedAssemblageBlock.brass(AssemblageSet.VANILLA, EncasedSingleShaftAssemblageBlock::new))
        .initialProperties(BRASS_ENCASED_SEPARATE_SHAFT_HALVES_ASSEMBLAGE)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate(EncasedAssemblageBlockDataGen.singleShaftBlockState("brass"))
        .loot(EncasedAssemblageBlockDataGen::singleShaftLoot)
        .transform(EncasingRegistry.addVariantTo(SINGLE_SHAFT_ASSEMBLAGE))
        .onRegister(EncasedAssemblageBlock.registerCTs(() -> EncasedAssemblageCTBehaviour.BRASS))
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .register();

    // BEVEL COGWHEELS

    public static final BlockEntry<SingleAxisBevelCogWheelBlock> SINGLE_AXIS_BEVEL_COGWHEEL = REGISTRATE.block("bevel_cogwheel/single_axis", p -> new SingleAxisBevelCogWheelBlock(BevelCogWheelSet.VANILLA, p))
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p
            .noOcclusion()
            .noLootTable()
        ).blockstate(SingleAxisBevelCogWheelBlock::blockState)
        .transform(TagGen.axeOrPickaxe())
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .register();

    public static final BlockEntry<CornerBevelCogWheelsBlock> CORNER_BEVEL_COGWHEELS = bevelCogwheel("corner", CornerBevelCogWheelsBlock::new)
        .register();
    
    public static final BlockEntry<ThreeBevelCogWheelsBlock> THREE_BEVEL_COGWHEELS = bevelCogwheel("three", ThreeBevelCogWheelsBlock::new)
        .register();

    public static final BlockEntry<FourBevelCogWheelsBlock> FOUR_BEVEL_COGWHEELS = bevelCogwheel("four", FourBevelCogWheelsBlock::new)
        .register();

    public static final BlockEntry<BevelCogWheelAndShaftBlock> SINGLE_BEVEL_COGWHEEL_AND_SHAFT = bevelCogwheel("single_and_shaft", BevelCogWheelAndShaftBlock::new)
        .register();

    public static final BlockEntry<OppositeBevelCogWheelsBlock> OPPOSITE_BEVEL_COGWHEELS = bevelCogwheel("opposite", OppositeBevelCogWheelsBlock::new)
        .register();

    public static final BlockEntry<OppositeBevelCogWheelsAndShaftBlock> OPPOSITE_BEVEL_COGWHEELS_AND_SHAFT = bevelCogwheel("opposite_and_shaft", OppositeBevelCogWheelsAndShaftBlock::new)
        .register();

    public static final BlockEntry<CornerBevelCogWheelsAndShaftBlock> CORNER_BEVEL_COGWHEELS_AND_SHAFT = bevelCogwheel("corner_and_shaft", CornerBevelCogWheelsAndShaftBlock::new)
        .register();

    public static final BlockEntry<ThreeBevelCogWheelsAndShaftBlock> THREE_BEVEL_COGWHEELS_AND_SHAFT = bevelCogwheel("three_and_shaft", ThreeBevelCogWheelsAndShaftBlock::new)
        .register();

    public static final BlockEntry<FourBevelCogWheelsAndShaftBlock> FOUR_BEVEL_COGWHEELS_AND_SHAFT = bevelCogwheel("four_and_shaft", FourBevelCogWheelsAndShaftBlock::new)
        .register();

    public static final BlockEntry<SingleDiagonalBevelCogWheelBlock> SINGLE_DIAGONAL_BEVEL_COGWHEEL = bevelCogwheel("diagonal/single", SingleDiagonalBevelCogWheelBlock::new)
        .register();

    public static final BlockEntry<DualDiagonalBevelCogWheelBlock> DUAL_DIAGONAL_BEVEL_COGWHEEL = bevelCogwheel("diagonal/dual", DualDiagonalBevelCogWheelBlock::new)
        .register();

    public static final BlockEntry<EncasedDualDiagonalBevelCogWheelBlock> ANDESITE_ENCASED_DUAL_DIAGONAL_BEVEL_COGWHEEL = REGISTRATE.block("bevel_cogwheel/diagonal/encased/dual/andesite", IEncasedBevelCogWheelBlock.andesite(BevelCogWheelSet.VANILLA, EncasedDualDiagonalBevelCogWheelBlock::new))
        .initialProperties(AllBlocks.ANDESITE_ENCASED_COGWHEEL)
        .transform(EncasedDualDiagonalBevelCogWheelBlock.builderTransformer(AllSpriteShifts.ANDESITE_CASING, "andesite"))
        .register();

    public static final BlockEntry<EncasedDualDiagonalBevelCogWheelBlock> BRASS_ENCASED_DUAL_DIAGONAL_BEVEL_COGWHEEL = REGISTRATE.block("bevel_cogwheel/diagonal/encased/dual/brass", IEncasedBevelCogWheelBlock.brass(BevelCogWheelSet.VANILLA, EncasedDualDiagonalBevelCogWheelBlock::new))
        .initialProperties(AllBlocks.BRASS_ENCASED_COGWHEEL)
        .transform(EncasedDualDiagonalBevelCogWheelBlock.builderTransformer(AllSpriteShifts.BRASS_CASING, "brass"))
        .register();

    //

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
    
    // CORNER SHAFTS

    public static final BlockEntry<CornerShaftBlock> CORNER_SHAFT = REGISTRATE.block("corner_shaft", CornerShaftBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .properties(p -> p
            .mapColor(MapColor.METAL)
            .noOcclusion()
        ).defaultLoot()
        .transform(PPCStress.setNoImpact())
        .transform(TagGen.pickaxeOnly())
        .item(CornerShaftBlockItem::new)
        .tag(PetrolparkTags.Items.FLAGGABLE.tag)
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

    public static final BlockEntry<StraightCornerShaftBlock> STRAIGHT_CORNER_SHAFT = REGISTRATE.block("straight_corner_shaft", StraightCornerShaftBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .loot((lt, b) -> lt.dropOther(b, CORNER_SHAFT))
		.transform(PPCStress.setNoImpact())
		.transform(TagGen.pickaxeOnly())
		.blockstate((ctx, prov) -> BlockStateGen.axisBlock(ctx, prov, $ -> prov.models().getExistingFile(prov.modLoc("block/corner_shaft/straight"))))
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .register();

    public static final BlockEntry<EncasedStraightCornerShaftBlock> ANDESITE_ENCASED_STRAIGHT_CORNER_SHAFT = REGISTRATE.block("andesite_encased_straight_corner_shaft", p -> new EncasedStraightCornerShaftBlock(p, AllBlocks.ANDESITE_CASING::get))
        .transform(encasedStraightCornerShaft("andesite", () -> AllSpriteShifts.ANDESITE_CASING))
        .transform(EncasingRegistry.addVariantTo(STRAIGHT_CORNER_SHAFT))
        .transform(TagGen.axeOrPickaxe())
        .register();

	public static final BlockEntry<EncasedStraightCornerShaftBlock> BRASS_ENCASED_STRAIGHT_CORNER_SHAFT = REGISTRATE.block("brass_encased_straight_corner_shaft", p -> new EncasedStraightCornerShaftBlock(p, AllBlocks.BRASS_CASING::get))
        .transform(encasedStraightCornerShaft("brass", () -> AllSpriteShifts.BRASS_CASING))
        .transform(EncasingRegistry.addVariantTo(STRAIGHT_CORNER_SHAFT))
        .transform(TagGen.axeOrPickaxe())
        .register();

    //

    public static final BlockEntry<DifferentialBlock> DIFFERENTIAL = REGISTRATE.block("differential", DifferentialBlock::new)
        .initialProperties(AllBlocks.LARGE_COGWHEEL)
        .defaultLoot()
        .properties(p -> p
            .noOcclusion()
            .sound(SoundType.WOOD)
		    .mapColor(MapColor.DIRT)
        ).transform(PPCStress.setNoImpact())
        .transform(TagGen.axeOrPickaxe())
        .item()
        .build()
        .register();

    public static final BlockEntry<FrictionHeaterBlock> FRICTION_HEATER = REGISTRATE.block("friction_heater", FrictionHeaterBlock::new)
        .initialProperties(AllBlocks.BLAZE_BURNER)
        .defaultLoot()
        .properties(p -> p
        
        ).transform(PPCStress.setImpact(8.0d))
        .transform(TagGen.axeOrPickaxe())
        .register();

    public static final BlockEntry<HydraulicTransmissionBlock> HYDRAULIC_TRANSMISSION = REGISTRATE.block("hydraulic_transmission", HydraulicTransmissionBlock::new)
        .initialProperties(AllBlocks.MECHANICAL_CRAFTER)
        .properties(p -> p
            .noOcclusion()
        ).defaultLoot()
        .transform(PPCStress.setImpact(2.0d))
        .transform(TagGen.axeOrPickaxe())
        .item(TubeBlockItem::new)
        .build()
        .register();

    public static final BlockEntry<MovementBlock> MOVEMENT = REGISTRATE.block("movement", MovementBlock::new)
        .initialProperties(SharedProperties::softMetal)
        .loot((lt, b) -> lt.add(b, LootTable.lootTable()
            .withPool(
                lt.applyExplosionCondition(b, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1f))
                    .add(LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                        .include(PetrolsPartsDataComponentTypes.MOVEMENT_DATA)
                    ))
                )
            ))
        ).transform(TagGen.axeOrPickaxe())
        .item()
        .tag(PetrolparkTags.Items.FLAGGABLE.tag)
        .build()
        .register();

    public static final BlockEntry<OverloadClutchBlock> OVERLOAD_CLUTCH = REGISTRATE.block("overload_clutch", OverloadClutchBlock::new)
        .initialProperties(AllBlocks.COGWHEEL)
        .properties(p -> p
        
        ).defaultLoot()
        .item()
        .build()
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
        .tag(PetrolparkTags.Items.FLAGGABLE.tag)
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

    public static final BlockEntry<TransmissionBlock> REDSTONE_TRANSMISSION = REGISTRATE.block("redstone_transmission", TransmissionBlock::new)
        .defaultLoot()
        .item()
        .build()
        .register();

    // OLD

    @Deprecated public static final BlockEntry<LegacyCoaxialGearBlock> LEGACY_COAXIAL_GEAR = REGISTRATE.block("coaxial_gear", LegacyCoaxialGearBlock::small).properties(BlockBehaviour.Properties::noLootTable).register();
    @Deprecated public static final BlockEntry<LegacyCoaxialGearBlock> LEGACY_LARGE_COAXIAL_GEAR = REGISTRATE.block("large_coaxial_gear", LegacyCoaxialGearBlock::large).properties(BlockBehaviour.Properties::noLootTable).register();
    
    //

    public static final void register() {};

    private static final <B extends Block & IBevelCogWheelBlock> BlockBuilder<B, PetrolsPartsRegistrate> bevelCogwheel(String suffix, NonNullBiFunction<Supplier<BevelCogWheelSet>, BlockBehaviour.Properties, B> factory) {
        return REGISTRATE.block("bevel_cogwheel/" + suffix, p -> factory.apply(BevelCogWheelSet.VANILLA, p))
            .initialProperties(SINGLE_AXIS_BEVEL_COGWHEEL)
            .properties(p -> p
                .noOcclusion()
                .noLootTable()
            ).blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().getExistingFile(PetrolsParts.asResource("block/bevel_cogwheel/block"))))
            .transform(TagGen.axeOrPickaxe());  
    };

    private static final <B extends EncasedStraightCornerShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedStraightCornerShaft(String casing, Supplier<CTSpriteShiftEntry> casingShift) {
		return builder -> builder.initialProperties(AllBlocks.ANDESITE_ENCASED_SHAFT)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(PPCStress.setNoImpact())
			.loot((p, lb) -> p.dropOther(lb, CORNER_SHAFT))
			.onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(casingShift.get())))
			.onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, casingShift.get(), (s, f) -> f.getAxis() != s.getValue(EncasedShaftBlock.AXIS))))
			// .item()
			// .model(AssetLookup.customBlockItemModel("encased_shaft", "item_" + casing))
			// .build()
            ;
	};

};
