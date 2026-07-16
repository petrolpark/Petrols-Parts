package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.PetrolsPartsShapes;

public record AssemblageSet(
    // Display
    ResourceLocation id,
    String descriptionId,
    // Blocks
    BlockEntry<? extends SingleShaftAssemblageBlock> singleShaftAssemblage, BlockEntry<? extends SeparateShaftHalvesAssemblageBlock> separateShaftsAssemblage,
    // Items
    ItemEntry<? extends ShaftHalfBlockItem> shaftHalf,
    ItemEntry<? extends AssemblageCogWheelBlockItem> smallCog, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCog,
    ItemEntry<? extends AssemblageCogWheelBlockItem> coaxialCog, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCoaxialCog,
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
    BlockEntry<? extends ShaftBlock> shaft, Optional<BlockEntry<? extends CogWheelBlock>> equivalentSmallCogWheel, Optional<BlockEntry<? extends CogWheelBlock>> equivalentLargeCogWheel
) {

    public AssemblageSet(
        // Display
        ResourceLocation id,
        // Blocks
        BlockEntry<? extends SingleShaftAssemblageBlock> singleShaftAssemblage, BlockEntry<? extends SeparateShaftHalvesAssemblageBlock> separateShaftsAssemblage,
        // Items
        ItemEntry<? extends ShaftHalfBlockItem> shaftHalf,
        ItemEntry<? extends AssemblageCogWheelBlockItem> smallCog, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCog,
        ItemEntry<? extends AssemblageCogWheelBlockItem> coaxialCog, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCoaxialCog,
        // Loot
        ResourceKey<LootTable> shaftLoot, ResourceKey<LootTable> shaftHalfLoot,
        ResourceKey<LootTable> smallCogLoot, ResourceKey<LootTable> largeCogLoot,
        ResourceKey<LootTable> coaxialCogLoot, ResourceKey<LootTable> largeCoaxialCogLoot,
        // Equivalent blocks
        BlockEntry<? extends ShaftBlock> shaft, Optional<BlockEntry<? extends CogWheelBlock>> equivalentSmallCogWheel, Optional<BlockEntry<? extends CogWheelBlock>> equivalentLargeCogWheel
    ) {
        this(
            // Display
            id, Util.makeDescriptionId("block", id),
            // Blocks
            singleShaftAssemblage, separateShaftsAssemblage,
            // Items
            shaftHalf,
            smallCog, largeCog,
            coaxialCog, largeCoaxialCog,
            // Loot
            shaftLoot, shaftHalfLoot,
            smallCogLoot, largeCogLoot,
            coaxialCogLoot, largeCoaxialCogLoot,
            // Parts
                // Shafts
                Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(
                    true,
                    dir -> dir.getAxis() == axis,
                    AllShapes.SIX_VOXEL_POLE.get(axis),
                    shaftLoot,
                    state -> BlockHelper.copyAll(separateShaftsAssemblage.getDefaultState(), state),
                    be -> be.shaftPart,
                    shaft
                ))),
                // Shaft halves
                Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(
                    true,
                    dir::equals,
                    PetrolsPartsShapes.SHAFT_HALF.get(dir),
                    shaftHalfLoot,
                    state -> state.setValue(dir.getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, false),
                    be -> be.shaftPart,
                    shaftHalf
                ))),
                // Middle cogs
                Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_COGWHEEL.get(axis),
                    smallCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    smallCog
                ))),
                // Cogs
                Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_COGWHEEL.get(dir),
                    smallCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    smallCog
                ))),
                // Middle large cogs
                Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_LARGE_COGWHEEL.get(axis),
                    largeCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    largeCog
                ))),
                // Large cogs
                Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_LARGE_COGWHEEL.get(dir),
                    largeCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    largeCog
                ))),
                // Middle coaxial cogs
                Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_COGWHEEL.get(axis),
                    coaxialCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    coaxialCog
                ))),
                // Coaxial cogs
                Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_COGWHEEL.get(dir),
                    coaxialCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    coaxialCog
                ))),
                // Middle large coaxial cogs
                Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(
                    false,
                    dir -> false,
                    PetrolsPartsShapes.MIDDLE_LARGE_COGWHEEL.get(axis),
                    largeCoaxialCogLoot,
                    AssemblagePart.REMOVE_MIDDLE_COG,
                    be -> be.middleCogPart,
                    largeCoaxialCog
                ))),
                // Large coaxial cogs
                Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(
                    false,
                    dir::equals,
                    PetrolsPartsShapes.FACIAL_LARGE_COGWHEEL.get(dir),
                    largeCoaxialCogLoot,
                    AssemblagePart.removeCog(dir),
                    AssemblagePart.getCogPart(dir),
                    largeCoaxialCog
                ))),
            // Equivalent blocks
            shaft, equivalentSmallCogWheel, equivalentLargeCogWheel
        );
    };

    public AssemblageSet(
        // Display
        ResourceLocation id,
        // Blocks
        BlockEntry<? extends SingleShaftAssemblageBlock> singleShaftAssemblage, BlockEntry<? extends SeparateShaftHalvesAssemblageBlock> separateShaftsAssemblage,
        // Items
        ItemEntry<? extends ShaftHalfBlockItem> shaftHalf,
        ItemEntry<? extends AssemblageCogWheelBlockItem> smallCog, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCog,
        ItemEntry<? extends AssemblageCogWheelBlockItem> coaxialCog, ItemEntry<? extends AssemblageCogWheelBlockItem> largeCoaxialCog,
        // Equivalent blocks
        BlockEntry<? extends ShaftBlock> shaft, Optional<BlockEntry<? extends CogWheelBlock>> equivalentSmallCogWheel, Optional<BlockEntry<? extends CogWheelBlock>> equivalentLargeCogWheel
    ) {
        this(
            // Display
            id,
            // Blocks
            singleShaftAssemblage, separateShaftsAssemblage,
            // Items
            shaftHalf,
            smallCog, largeCog,
            coaxialCog, largeCoaxialCog,
            // Loot
            ResourceKey.create(Registries.LOOT_TABLE, shaft.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, shaftHalf.getId().withPrefix("blocks/")),
            ResourceKey.create(Registries.LOOT_TABLE, smallCog.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, largeCog.getId().withPrefix("blocks/")),
            ResourceKey.create(Registries.LOOT_TABLE, coaxialCog.getId().withPrefix("blocks/")), ResourceKey.create(Registries.LOOT_TABLE, largeCoaxialCog.getId().withPrefix("blocks/")),
            // Equivalent blocks
            shaft, equivalentSmallCogWheel, equivalentLargeCogWheel
        );
    };
   
    public BlockState getEquivalent(BlockState state) {
        BlockState oldState = state;
        if (shaft().has(oldState)) {
            state = singleShaftAssemblage().getDefaultState();
        } else if (equivalentSmallCogWheel().map(entry -> entry.has(oldState)).orElse(false)) {
            state = singleShaftAssemblage().getDefaultState().setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.SMALL);
        } else if (equivalentLargeCogWheel().map(entry -> entry.has(oldState)).orElse(false)) {
            state = singleShaftAssemblage().getDefaultState().setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.LARGE);
        } else {
            return state;
        }
        return state.setValue(IAssemblageBlock.AXIS, oldState.getValue(BlockStateProperties.AXIS));
    };

    @Nullable
    public AssemblagePart getTargetedPart(BlockPlaceContext context) {
        if (context.replacingClickedOnBlock()) {
            final BlockState state = getEquivalent(context.getLevel().getBlockState(context.getClickedPos()));
            if (state.getBlock() instanceof AssemblageBlock assemblage) {
                return assemblage.getTargetedPart(state, context.getClickedPos(), context.getPlayer());
            };
        };
        return null;
    };

    @OnlyIn(Dist.CLIENT)
    public record Client(
        // Cogwheel models
        PartialModel shaftlessCogWheel, PartialModel largeShaftlessCogWheel,
        PartialModel coaxialCogWheel, PartialModel largeCoaxialCogWheel,
        // Shaft half models
        PartialModel shaftHalfTop, PartialModel shaftHalfBottom
    ) {

        public PartialModel getModel(AssemblageCog cog) {
            return switch (cog) {
                case LARGE -> shaftlessCogWheel();
                case SMALL_COAXIAL -> coaxialCogWheel();
                case LARGE_COAXIAL -> largeCoaxialCogWheel();
                default -> shaftlessCogWheel();
            };
        };
    };

    public static final AssemblageSet CREATE = new AssemblageSet(
        // Display
        PetrolsParts.asResource("assemblage"),
        // Blocks
        PetrolsPartsBlocks.SINGLE_SHAFT_ASSEMBLAGE, PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE,
        // Items
        PetrolsPartsItems.SHAFT_HALF,
        PetrolsPartsItems.SHAFTLESS_COGWHEEL, PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL,
        PetrolsPartsItems.COAXIAL_COGWHEEL, PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL,
        // Equivalent blocks
        AllBlocks.SHAFT, Optional.of(AllBlocks.COGWHEEL), Optional.of(AllBlocks.LARGE_COGWHEEL)
    );
};
