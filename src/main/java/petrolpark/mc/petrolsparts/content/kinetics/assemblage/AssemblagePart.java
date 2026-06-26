package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Suppliers;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.Create;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;
import petrolpark.mc.library.compat.create.core.world.block.CreateMultiPartBlock;
import petrolpark.mc.library.util.BlockHelper;
import petrolpark.mc.petrolsparts.PetrolsParts;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.PetrolsPartsShapes;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;

public final class AssemblagePart implements CreateMultiPartBlock.ICreatePart {

    public static final ResourceKey<LootTable> SHAFT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Create.asResource("blocks/shaft"));
    public static final ResourceKey<LootTable> SHAFT_HALF_LOOT = ResourceKey.create(Registries.LOOT_TABLE, PetrolsParts.asResource("blocks/shaft_half"));

    public static final UnaryOperator<BlockState> REMOVE_TOP_COG = state -> state.setValue(IAssemblageBlock.TOP_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> REMOVE_BOTTOM_COG = state -> state.setValue(IAssemblageBlock.BOTTOM_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> REMOVE_MIDDLE_COG = state -> state.setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> removeCog(Direction direction) {
        return direction.getAxisDirection() == AxisDirection.POSITIVE ? REMOVE_TOP_COG : REMOVE_BOTTOM_COG;
    };

    public static final Function<AssemblageBlockEntity, AssemblageBlockEntityPart> getCogPart(Direction direction) {
        return direction.getAxisDirection() == AxisDirection.POSITIVE ? be -> be.topCogPart : be -> be.bottomCogPart;
    };

    public static final Map<Axis, AssemblagePart> SHAFTS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(true, dir -> dir.getAxis() == axis, AllShapes.SIX_VOXEL_POLE.get(axis), SHAFT_LOOT, state -> BlockHelper.copyAll(PetrolsPartsBlocks.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.getDefaultState(), state), be -> be.shaftPart, AllBlocks.SHAFT)));
    public static final Map<Direction, AssemblagePart> SHAFT_HALVES = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(true, dir::equals, PetrolsPartsShapes.SHAFT_HALF.get(dir), SHAFT_HALF_LOOT, state -> state.setValue(dir.getAxisDirection() == AxisDirection.POSITIVE ? IAssemblageBlock.TOP_SHAFT_HALF : IAssemblageBlock.BOTTOM_SHAFT_HALF, false), be -> be.shaftPart, PetrolsPartsItems.SHAFT_HALF)));
    public static final Map<Direction, AssemblagePart> COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(false, dir::equals, PetrolsPartsShapes.FACIAL_COGWHEEL.get(dir), AssemblageCog.SMALL.getLootTable(), removeCog(dir), getCogPart(dir), PetrolsPartsItems.SHAFTLESS_COGWHEEL)));
    public static final Map<Axis, AssemblagePart> MIDDLE_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(false, dir -> false, PetrolsPartsShapes.MIDDLE_COGWHEEL.get(axis), AssemblageCog.SMALL.getLootTable(), REMOVE_MIDDLE_COG, be -> be.middleCogPart, PetrolsPartsItems.SHAFTLESS_COGWHEEL)));
    public static final Map<Direction, AssemblagePart> LARGE_COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(false, dir::equals, PetrolsPartsShapes.FACIAL_LARGE_COGWHEEL.get(dir), AssemblageCog.LARGE.getLootTable(), removeCog(dir), getCogPart(dir), PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL)));
    public static final Map<Axis, AssemblagePart> LARGE_MIDDLE_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(false, dir -> false, PetrolsPartsShapes.MIDDLE_LARGE_COGWHEEL.get(axis), AssemblageCog.LARGE.getLootTable(), REMOVE_MIDDLE_COG, be -> be.middleCogPart,PetrolsPartsItems.LARGE_SHAFTLESS_COGWHEEL)));
    public static final Map<Direction, AssemblagePart> COAXIAL_COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(false, dir::equals, PetrolsPartsShapes.FACIAL_COGWHEEL.get(dir), AssemblageCog.SMALL_COAXIAL.getLootTable(), removeCog(dir), getCogPart(dir), PetrolsPartsItems.COAXIAL_COGWHEEL)));
    public static final Map<Axis, AssemblagePart> MIDDLE_COAXIAL_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(false, dir -> false, PetrolsPartsShapes.MIDDLE_COGWHEEL.get(axis), AssemblageCog.SMALL_COAXIAL.getLootTable(), REMOVE_MIDDLE_COG, be -> be.middleCogPart,PetrolsPartsItems.COAXIAL_COGWHEEL)));
    public static final Map<Direction, AssemblagePart> LARGE_COAXIAL_COGWHEELS = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new AssemblagePart(false, dir::equals, PetrolsPartsShapes.FACIAL_LARGE_COGWHEEL.get(dir), AssemblageCog.LARGE_COAXIAL.getLootTable(), removeCog(dir), getCogPart(dir), PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL)));
    public static final Map<Axis, AssemblagePart> LARGE_MIDDLE_COAXIAL_COGWHEELS = Stream.of(Axis.values()).collect(Collectors.toMap(Function.identity(), axis -> new AssemblagePart(false, dir -> false, PetrolsPartsShapes.MIDDLE_LARGE_COGWHEEL.get(axis), AssemblageCog.LARGE_COAXIAL.getLootTable(), REMOVE_MIDDLE_COG, be -> be.middleCogPart, PetrolsPartsItems.LARGE_COAXIAL_COGWHEEL)));

    protected final boolean isShaft;
    protected final Predicate<Direction> onEnd;
    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;
    protected final UnaryOperator<BlockState> remover;
    protected final Function<AssemblageBlockEntity, AssemblageBlockEntityPart> kineticPartGetter;
    protected final ItemLike item;
    protected final Supplier<ItemRequirement> itemRequirement;

    private AssemblagePart(boolean isShaft, Predicate<Direction> onEnd, VoxelShape shape, ResourceKey<LootTable> loot, UnaryOperator<BlockState> remover, Function<AssemblageBlockEntity, AssemblageBlockEntityPart> kineticPartGetter, ItemLike item) {
        this.isShaft = isShaft;
        this.onEnd = onEnd;
        this.shape = shape;
        this.loot = loot;
        this.remover = remover;
        this.kineticPartGetter = kineticPartGetter;
        this.item = item;
        this.itemRequirement = Suppliers.memoize(() -> new ItemRequirement(ItemUseType.CONSUME, item.asItem()));
    };

    public boolean isShaft() {
        return isShaft;
    };

    public boolean isOnEnd(Direction face) {
        return onEnd.test(face);
    };

    @Override
    public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(item);
    };

    @Override
    public VoxelShape shape() {
        return shape;
    };

    @Override
    public ResourceKey<LootTable> loot() {
        return loot;
    };

    @Override
    public ItemRequirement itemRequirement() {
        return itemRequirement.get();
    };

    public AssemblageBlockEntityPart getKineticPart(AssemblageBlockEntity be) {
        return kineticPartGetter.apply(be);
    };

    public boolean isEndCog(Direction face) {
        return this == COGWHEELS.get(face) || this == LARGE_COGWHEELS.get(face) || this == COAXIAL_COGWHEELS.get(face) || this == LARGE_COAXIAL_COGWHEELS.get(face);
    };

    public boolean isMiddleCog(Axis axis) {
        return this == MIDDLE_COGWHEELS.get(axis) || this == LARGE_MIDDLE_COGWHEELS.get(axis) || this == MIDDLE_COAXIAL_COGWHEELS.get(axis) || this == LARGE_MIDDLE_COAXIAL_COGWHEELS.get(axis);
    };
    
};
