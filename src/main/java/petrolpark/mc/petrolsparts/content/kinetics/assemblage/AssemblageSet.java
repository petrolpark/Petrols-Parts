package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Suppliers;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.library.util.CollectionHelper;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlockEntityTypes;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.PetrolsPartsShapes;

public record AssemblageSet(
    // Display
    ResourceLocation id,
    String descriptionId,
    // Blocks and BE
    BlockEntry<? extends SingleShaftAssemblageBlock> singleShaftAssemblageBlock, BlockEntry<? extends SeparateShaftHalvesAssemblageBlock> separateShaftsAssemblageBlock,
    BlockEntityEntry<? extends AssemblageBlockEntity> blockEntity,
    // Items
    ItemEntry<? extends ShaftHalfBlockItem> shaftHalfItem,
    ItemEntry<? extends AssemblageCogWheelBlockItem> smallCogItem, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCogItem,
    ItemEntry<? extends AssemblageCogWheelBlockItem> coaxialCogItem, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCoaxialCogItem,
    // Loot
    ResourceKey<LootTable> shaftLoot, ResourceKey<LootTable> shaftHalfLoot,
    ResourceKey<LootTable> smallCogLoot, ResourceKey<LootTable> largeCogLoot,
    ResourceKey<LootTable> coaxialCogLoot, ResourceKey<LootTable> largeCoaxialCogLoot,
    // Parts
    Map<Axis, AssemblagePart> shaftParts, Map<Direction, AssemblagePart> shaftHalfParts,
    Map<Axis, AssemblagePart> middleCogWheelParts, Map<Direction, AssemblagePart> cogWheelParts,
    Map<Axis, AssemblagePart> middleLargeCogWheelParts, Map<Direction, AssemblagePart> largeCogWheelParts,
    Map<Axis, AssemblagePart> middleCoaxialCogWheelParts, Map<Direction, AssemblagePart> coaxialCogWheelParts,
    Map<Axis, AssemblagePart> middleLargeCoaxialCogWheelParts, Map<Direction, AssemblagePart> largeCoaxialCogWheelParts,
    // Equivalent blocks
    BlockEntry<? extends ShaftBlock> shaftBlock, Optional<BlockEntry<? extends CogWheelBlock>> equivalentSmallCogBlock, Optional<BlockEntry<? extends CogWheelBlock>> equivalentLargeCogBlock
) {

    public AssemblageSet(
        // Display
        ResourceLocation id,
        // Blocks and BE
        BlockEntry<? extends SingleShaftAssemblageBlock> singleShaftAssemblageBlock, BlockEntry<? extends SeparateShaftHalvesAssemblageBlock> separateShaftsAssemblageBlock,
        BlockEntityEntry<? extends AssemblageBlockEntity> blockEntity,
        // Items
        ItemEntry<? extends ShaftHalfBlockItem> shaftHalfItem,
        ItemEntry<? extends AssemblageCogWheelBlockItem> smallCogItem, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCogItem,
        ItemEntry<? extends AssemblageCogWheelBlockItem> coaxialCogItem, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCoaxialCogItem,
        // Loot
        ResourceKey<LootTable> shaftLoot, ResourceKey<LootTable> shaftHalfLoot,
        ResourceKey<LootTable> smallCogLoot, ResourceKey<LootTable> largeCogLoot,
        ResourceKey<LootTable> coaxialCogLoot, ResourceKey<LootTable> largeCoaxialCogLoot,
        // Equivalent blocks
        BlockEntry<? extends ShaftBlock> shaftBlock, Optional<BlockEntry<? extends CogWheelBlock>> equivalentSmallCogBlock, Optional<BlockEntry<? extends CogWheelBlock>> equivalentLargeCogBlock
    ) {
        this(
            // Display
            id, Util.makeDescriptionId("block", id),
            // Blocks
            singleShaftAssemblageBlock, separateShaftsAssemblageBlock,
            blockEntity,
            // Items
            shaftHalfItem,
            smallCogItem, largeCogItem,
            coaxialCogItem, largeCoaxialCogItem,
            // Loot
            shaftLoot, shaftHalfLoot,
            smallCogLoot, largeCogLoot,
            coaxialCogLoot, largeCoaxialCogLoot,
            // Parts
                // Shafts
                CollectionHelper.map(Axis.values(), axis -> new AssemblagePart(
                    true,
                    dir -> dir.getAxis() == axis,
                    AllShapes.SIX_VOXEL_POLE.get(axis),
                    shaftLoot,
                    state -> BlockHelper.copyAll(separateShaftsAssemblageBlock.getDefaultState(), state),
                    be -> be.shaftPart,
                    shaftBlock
                )),
                // Shaft halves
                CollectionHelper.map(Direction.values(), dir -> new AssemblagePart(
                    true,
                    dir::equals,
                    PetrolsPartsShapes.SHAFT_HALF.get(dir),
                    shaftHalfLoot,
                    state -> state.setValue(dir.getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, false),
                    be -> be.shaftPart,
                    shaftHalfItem
                )),
                // Middle cogs
                CollectionHelper.map(Axis.values(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_COGWHEEL.get(axis),
                    smallCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    smallCogItem
                )),
                // Cogs
                CollectionHelper.map(Direction.values(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_COGWHEEL.get(dir),
                    smallCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    smallCogItem
                )),
                // Middle large cogs
                CollectionHelper.map(Axis.values(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_LARGE_COGWHEEL.get(axis),
                    largeCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    largeCogItem
                )),
                // Large cogs
                CollectionHelper.map(Direction.values(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_LARGE_COGWHEEL.get(dir),
                    largeCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    largeCogItem
                )),
                // Middle coaxial cogs
                CollectionHelper.map(Axis.values(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_COGWHEEL.get(axis),
                    coaxialCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    coaxialCogItem
                )),
                // Coaxial cogs
                CollectionHelper.map(Direction.values(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_COGWHEEL.get(dir),
                    coaxialCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    coaxialCogItem
                )),
                // Middle large coaxial cogs
                CollectionHelper.map(Axis.values(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_LARGE_COGWHEEL.get(axis),
                    largeCoaxialCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    largeCoaxialCogItem
                )),
                // Large coaxial cogs
                CollectionHelper.map(Direction.values(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_LARGE_COGWHEEL.get(dir),
                    largeCoaxialCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    largeCoaxialCogItem
                )),
            // Equivalent blocks
            shaftBlock, equivalentSmallCogBlock, equivalentLargeCogBlock
        );
    };

    public AssemblageSet(
        // Display
        ResourceLocation id,
        // Blocks and BE
        BlockEntry<? extends SingleShaftAssemblageBlock> singleShaftAssemblageBlock, BlockEntry<? extends SeparateShaftHalvesAssemblageBlock> separateShaftsAssemblageBlock,
        BlockEntityEntry<? extends AssemblageBlockEntity> blockEntity,
        // Items
        ItemEntry<? extends ShaftHalfBlockItem> shaftHalfItem,
        ItemEntry<? extends AssemblageCogWheelBlockItem> smallCogItem, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCogItem,
        ItemEntry<? extends AssemblageCogWheelBlockItem> coaxialCogItem, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCoaxialCogItem,
        // Equivalent blocks
        BlockEntry<? extends ShaftBlock> shaftBlock, Optional<BlockEntry<? extends CogWheelBlock>> equivalentSmallCogBlock, Optional<BlockEntry<? extends CogWheelBlock>> equivalentLargeCogBlock
    ) {
        this(
            // Display
            id,
            // Blocks
            singleShaftAssemblageBlock, separateShaftsAssemblageBlock,
            blockEntity,
            // Items
            shaftHalfItem,
            smallCogItem, largeCogItem,
            coaxialCogItem, largeCoaxialCogItem,
            // Loot
            ResourceKey.create(Registries.LOOT_TABLE, shaftBlock.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, shaftHalfItem.getId().withPrefix("blocks/")),
            ResourceKey.create(Registries.LOOT_TABLE, smallCogItem.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, largeCogItem.getId().withPrefix("blocks/")),
            ResourceKey.create(Registries.LOOT_TABLE, coaxialCogItem.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, largeCoaxialCogItem.getId().withPrefix("blocks/")),
            // Equivalent blocks
            shaftBlock, equivalentSmallCogBlock, equivalentLargeCogBlock
        );
    };
   
    public BlockState getEquivalent(BlockState state) {
        BlockState oldState = state;
        if (shaftBlock().has(oldState)) {
            state = singleShaftAssemblageBlock().getDefaultState();
        } else if (equivalentSmallCogBlock().map(entry -> entry.has(oldState)).orElse(false)) {
            state = singleShaftAssemblageBlock().getDefaultState().setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.SMALL);
        } else if (equivalentLargeCogBlock().map(entry -> entry.has(oldState)).orElse(false)) {
            state = singleShaftAssemblageBlock().getDefaultState().setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.LARGE);
        } else {
            return state;
        }
        return state.setValue(IAssemblageBlock.AXIS, oldState.getValue(BlockStateProperties.AXIS));
    };

    @Nullable
    public AssemblagePart getTargetedPart(BlockPlaceContext context) {
        if (context.replacingClickedOnBlock()) {
            final BlockState state = getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
            if (state.getBlock() instanceof AssemblageBlock assemblage && assemblage.getSet() == this) {
                return assemblage.getTargetedPart(state, context.getClickedPos(), context.getPlayer());
            };
        };
        return null;
    };
    
    public static final Supplier<AssemblageSet> VANILLA = Suppliers.memoize(() -> new AssemblageSet(
        // Display
        PetrolsParts.asResource("assemblage"),
        // Blocks and BE
        PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE, PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE,
        PetrolsPartsBlockEntityTypes.ASSEMBLAGE,
        // Items
        PetrolsPartsItems.SHAFT_HALF,
        PetrolsPartsItems.SHAFTLESS_COGWHEEL, PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL,
        PetrolsPartsItems.COAXIAL_COGWHEEL, PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL,
        // Equivalent blocks
        AllBlocks.SHAFT, Optional.of(AllBlocks.COGWHEEL), Optional.of(AllBlocks.LARGE_COGWHEEL)
    ));
    
};
