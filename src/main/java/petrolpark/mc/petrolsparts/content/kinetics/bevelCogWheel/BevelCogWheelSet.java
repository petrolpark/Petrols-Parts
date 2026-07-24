package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Suppliers;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.library.util.CollectionHelper;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.PetrolsPartsPartialModels;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.DiagonalBevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.DualDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.dual.DualDiagonalBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal.single.SingleDiagonalBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.BevelCogWheelPart;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.IOrthogonalBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.BevelCogWheelAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.CompositeBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.CornerBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.FourBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.OppositeBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.composite.ThreeBevelCogWheelsAndShaftBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.CornerBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.FourBevelCogWheelsBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SimpleBevelCogWheelBlockEntity;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.SingleAxisBevelCogWheelBlock;
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal.simple.ThreeBevelCogWheelsBlock;

public record BevelCogWheelSet(
    // Display
    ResourceLocation id, String translationKey,
    // Blocks (orthogonal)
    BlockEntry<? extends ShaftBlock> shaftBlock,
    BlockEntry<? extends SingleAxisBevelCogWheelBlock> singleAxisBlock, BlockEntry<? extends CornerBevelCogWheelsBlock> cornerBlock,
    BlockEntry<? extends ThreeBevelCogWheelsBlock> threeBlock, BlockEntry<? extends FourBevelCogWheelsBlock> fourBlock,
    BlockEntry<? extends BevelCogWheelAndShaftBlock> singleAndShaftBlock,
    BlockEntry<? extends OppositeBevelCogWheelsBlock> oppositesBlock, BlockEntry<? extends OppositeBevelCogWheelsAndShaftBlock> oppositesAndShaftBlock,
    BlockEntry<? extends CornerBevelCogWheelsAndShaftBlock> cornerAndShaftBlock, BlockEntry<? extends ThreeBevelCogWheelsAndShaftBlock> threeAndShaftBlock, BlockEntry<? extends FourBevelCogWheelsAndShaftBlock> fourAndShaftBlock,
    // Blocks (diagonal)
    BlockEntry<? extends SingleDiagonalBevelCogWheelBlock> singleDiagonalBlock, BlockEntry<? extends DualDiagonalBevelCogWheelBlock> dualDiagonalBlock,
    // BEs
    BlockEntityEntry<? extends KineticBlockEntity> singleAxisBE, BlockEntityEntry<? extends SimpleBevelCogWheelBlockEntity> simpleBE, BlockEntityEntry<? extends CompositeBevelCogWheelBlockEntity> compositeBE,
    BlockEntityEntry<? extends SingleDiagonalBevelCogWheelBlockEntity> singleDiagonalBE, BlockEntityEntry<? extends DualDiagonalBevelCogWheelBlockEntity> dualDiagonalBE, 
    // Items
    ItemEntry<? extends BevelCogWheelBlockItem> item, ItemEntry<? extends Item> shaftHalfItem,
    // Loot
    ResourceKey<LootTable> cogLoot, ResourceKey<LootTable> shaftLoot, ResourceKey<LootTable> shaftHalfLoot,
    // Parts
    Map<Direction, BevelCogWheelPart.Cog> cogParts, Map<Axis, BevelCogWheelPart.Shaft> shaftParts,
    Map<Orientation, DiagonalBevelCogWheelPart.Cog> diagonalCogParts, Map<Direction, DiagonalBevelCogWheelPart.ShaftHalf> shaftHalfParts
) {
    
    public BevelCogWheelSet(
        // Display
        ResourceLocation id,
        // Blocks (orthogonal)
        BlockEntry<? extends ShaftBlock> shaftBlock,
        BlockEntry<? extends SingleAxisBevelCogWheelBlock> singleAxisBlock, BlockEntry<? extends CornerBevelCogWheelsBlock> cornerBlock,
        BlockEntry<? extends ThreeBevelCogWheelsBlock> threeBlock, BlockEntry<? extends FourBevelCogWheelsBlock> fourBlock,
        BlockEntry<? extends BevelCogWheelAndShaftBlock> singleAndShaftBlock,
        BlockEntry<? extends OppositeBevelCogWheelsBlock> oppositesBlock, BlockEntry<? extends OppositeBevelCogWheelsAndShaftBlock> oppositesAndShaftBlock,
        BlockEntry<? extends CornerBevelCogWheelsAndShaftBlock> cornerAndShaftBlock, BlockEntry<? extends ThreeBevelCogWheelsAndShaftBlock> threeAndShaftBlock, BlockEntry<? extends FourBevelCogWheelsAndShaftBlock> fourAndShaftBlock,
        // Blocks (diagonal)
        BlockEntry<? extends SingleDiagonalBevelCogWheelBlock> singleDiagonalBlock, BlockEntry<? extends DualDiagonalBevelCogWheelBlock> dualDiagonalBlock,
        // BEs
        BlockEntityEntry<? extends KineticBlockEntity> singleAxisBE, BlockEntityEntry<? extends SimpleBevelCogWheelBlockEntity> simpleBE, BlockEntityEntry<? extends CompositeBevelCogWheelBlockEntity> compositeBE,
        BlockEntityEntry<? extends SingleDiagonalBevelCogWheelBlockEntity> singleDiagonalBE, BlockEntityEntry<? extends DualDiagonalBevelCogWheelBlockEntity> dualDiagonalBE,
        // Items
        ItemEntry<? extends BevelCogWheelBlockItem> item, ItemEntry<? extends Item> shaftHalfItem,
        // Loot
        ResourceKey<LootTable> cogLoot, ResourceKey<LootTable> shaftLoot, ResourceKey<LootTable> shaftHalfLoot
    ) {
        this(
            id, Util.makeDescriptionId("block", id),
            shaftBlock,
            singleAxisBlock, cornerBlock,
            threeBlock, fourBlock,
            singleAndShaftBlock,
            oppositesBlock, oppositesAndShaftBlock,
            cornerAndShaftBlock, threeAndShaftBlock, fourAndShaftBlock,
            singleDiagonalBlock, dualDiagonalBlock,
            singleAxisBE, simpleBE, compositeBE,
            singleDiagonalBE, dualDiagonalBE,
            item, shaftHalfItem,
            cogLoot, shaftLoot, shaftHalfLoot,
            CollectionHelper.map(Direction.values(), dir -> new BevelCogWheelPart.Cog(dir, cogLoot, item)),
            CollectionHelper.map(Axis.values(), axis -> new BevelCogWheelPart.Shaft(axis, shaftLoot, shaftBlock)),
            CollectionHelper.map(Orientation.EDGE_ORIENTATIONS, orientation -> new DiagonalBevelCogWheelPart.Cog(orientation, cogLoot, item)),
            CollectionHelper.map(Direction.values(), dir -> new DiagonalBevelCogWheelPart.ShaftHalf(dir, shaftHalfLoot, null))
        );
    };

    public BevelCogWheelSet(
        // Display
        ResourceLocation id,
        // Blocks (orthogonal)
        BlockEntry<? extends ShaftBlock> shaftBlock,
        BlockEntry<? extends SingleAxisBevelCogWheelBlock> singleAxisBlock, BlockEntry<? extends CornerBevelCogWheelsBlock> cornerBlock,
        BlockEntry<? extends ThreeBevelCogWheelsBlock> threeBlock, BlockEntry<? extends FourBevelCogWheelsBlock> fourBlock,
        BlockEntry<? extends BevelCogWheelAndShaftBlock> singleAndShaftBlock,
        BlockEntry<? extends OppositeBevelCogWheelsBlock> oppositesBlock, BlockEntry<? extends OppositeBevelCogWheelsAndShaftBlock> oppositesAndShaftBlock,
        BlockEntry<? extends CornerBevelCogWheelsAndShaftBlock> cornerAndShaftBlock, BlockEntry<? extends ThreeBevelCogWheelsAndShaftBlock> threeAndShaftBlock, BlockEntry<? extends FourBevelCogWheelsAndShaftBlock> fourAndShaftBlock,
        // Blocks (diagonal)
        BlockEntry<? extends SingleDiagonalBevelCogWheelBlock> singleDiagonalBlock, BlockEntry<? extends DualDiagonalBevelCogWheelBlock> dualDiagonalBlock,
        // BEs
        BlockEntityEntry<? extends KineticBlockEntity> singleAxisBE, BlockEntityEntry<? extends SimpleBevelCogWheelBlockEntity> simpleBE, BlockEntityEntry<? extends CompositeBevelCogWheelBlockEntity> compositeBE,
        BlockEntityEntry<? extends SingleDiagonalBevelCogWheelBlockEntity> singleDiagonalBE, BlockEntityEntry<? extends DualDiagonalBevelCogWheelBlockEntity> dualDiagonalBE,
        // Items
        ItemEntry<? extends BevelCogWheelBlockItem> item, ItemEntry<? extends Item> shaftHalfItem
    ) {
        this(
            id,
            shaftBlock,
            singleAxisBlock, cornerBlock,
            threeBlock, fourBlock,
            singleAndShaftBlock, oppositesBlock, oppositesAndShaftBlock,
            cornerAndShaftBlock, threeAndShaftBlock, fourAndShaftBlock,
            singleDiagonalBlock, dualDiagonalBlock,
            singleAxisBE, simpleBE, compositeBE,
            singleDiagonalBE, dualDiagonalBE,
            item, shaftHalfItem,
            ResourceKey.create(Registries.LOOT_TABLE, item.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, shaftBlock.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, shaftHalfItem.getId().withPrefix("blocks/"))
        );
    };

    public boolean isReplaceable(BlockState state) {
        return (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock block && block.getSet() == this)
            || shaftBlock().has(state);
    };

    @Nullable
    public BevelCogWheelPart getTargetedPart(BlockPlaceContext context) {
        if (context.replacingClickedOnBlock()) {
            final BlockState state = context.getLevel().getBlockState(context.getClickedPos());
            if (state.getBlock() instanceof IOrthogonalBevelCogWheelBlock bevelCogWheel && bevelCogWheel.getSet() == this) {
                return bevelCogWheel.getTargetedPart(state, context.getClickedPos(), context.getPlayer());
            } else if (shaftBlock().has(state)) {
                return shaftParts().get(state.getValue(ShaftBlock.AXIS));
            };
        };
        return null;
    };

    @OnlyIn(Dist.CLIENT)
    public record Client(
        PartialModel fourTeeth, PartialModel fiveTeeth
    ) {};

    public static final Supplier<BevelCogWheelSet> CREATE = Suppliers.memoize(() -> new BevelCogWheelSet(
        // Display
        PetrolsParts.asResource("bevel_cogwheel"),
        // Blocks (orthogonal)
        AllBlocks.SHAFT,
        PetrolsPartsBlocks.SINGLE_AXIS_BEVEL_COGWHEEL, PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS,
        PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS, PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS,
        PetrolsPartsBlocks.SINGLE_BEVEL_COGWHEEL_AND_SHAFT, PetrolsPartsBlocks.OPPOSITE_BEVEL_COGWHEELS,
        PetrolsPartsBlocks.OPPOSITE_BEVEL_COGWHEELS_AND_SHAFT,
        PetrolsPartsBlocks.CORNER_BEVEL_COGWHEELS_AND_SHAFT, PetrolsPartsBlocks.THREE_BEVEL_COGWHEELS_AND_SHAFT, PetrolsPartsBlocks.FOUR_BEVEL_COGWHEELS_AND_SHAFT,
        // Blocks (diagonal)
        PetrolsPartsBlocks.SINGLE_DIAGONAL_BEVEL_COGWHEEL, PetrolsPartsBlocks.DUAL_DIAGONAL_BEVEL_COGWHEEL,
        // BEs
        PetrolsPartsBlockEntityTypes.SINGLE_AXIS_BEVEL_COGWHEEL, PetrolsPartsBlockEntityTypes.SIMPLE_BEVEL_COGWHEEL, PetrolsPartsBlockEntityTypes.COMPOSITE_BEVEL_COGWHEEL,
        PetrolsPartsBlockEntityTypes.SINGLE_DIAGONAL_BEVEL_COGWHEEL, PetrolsPartsBlockEntityTypes.DUAL_DIAGONAL_BEVEL_COGWHEEL,
        // Items
        PetrolsPartsItems.BEVEL_COGWHEEL, PetrolsPartsItems.SHAFT_HALF
    ));

    @OnlyIn(Dist.CLIENT)
    public static final BevelCogWheelSet.Client CREATE_CLIENT = new BevelCogWheelSet.Client(
        PetrolsPartsPartialModels.BEVEL_COGWHEEL,
        PetrolsPartsPartialModels.BEVEL_COGWHEEL_FIVE_TEETH
    );
};
