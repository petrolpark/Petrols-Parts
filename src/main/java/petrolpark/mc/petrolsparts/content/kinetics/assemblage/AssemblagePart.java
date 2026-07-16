package petrolpark.mc.petrolsparts.content.kinetics.assemblage;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.google.common.base.Suppliers;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;
import petrolpark.mc.library.compat.create.core.world.block.CreateMultiPartBlock;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblageBlockEntity.AssemblageBlockEntityPart;

public final class AssemblagePart implements CreateMultiPartBlock.ICreatePart {

    public static final UnaryOperator<BlockState> REMOVE_TOP_COG = state -> state.setValue(IAssemblageBlock.TOP_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> REMOVE_BOTTOM_COG = state -> state.setValue(IAssemblageBlock.BOTTOM_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> REMOVE_MIDDLE_COG = state -> state.setValue(IAssemblageBlock.MIDDLE_COG, AssemblageCog.NONE);
    public static final UnaryOperator<BlockState> removeCog(Direction direction) {
        return direction.getAxisDirection() == AxisDirection.POSITIVE ? REMOVE_TOP_COG : REMOVE_BOTTOM_COG;
    };

    public static final Function<AssemblageBlockEntity, AssemblageBlockEntityPart> getCogPart(Direction direction) {
        return direction.getAxisDirection() == AxisDirection.POSITIVE ? be -> be.topCogPart : be -> be.bottomCogPart;
    };

    protected final boolean isShaft;
    protected final Predicate<Direction> onEnd;
    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;
    protected final UnaryOperator<BlockState> remover;
    protected final Function<AssemblageBlockEntity, AssemblageBlockEntityPart> kineticPartGetter;
    protected final ItemLike item;
    protected final Supplier<ItemRequirement> itemRequirement;

    public AssemblagePart(boolean isShaft, Predicate<Direction> onEnd, VoxelShape shape, ResourceKey<LootTable> loot, UnaryOperator<BlockState> remover, Function<AssemblageBlockEntity, AssemblageBlockEntityPart> kineticPartGetter, ItemLike item) {
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

    public boolean isEndCog(AssemblageSet set, Direction face) {
        return this == set.cogWheelParts().get(face) || this == set.largeCogWheelParts().get(face) || this == set.coaxialCogWheelParts().get(face) || this == set.largeCoaxialCogWheelParts().get(face);
    };

    public boolean isMiddleCog(AssemblageSet set, Axis axis) {
        return this == set.middleCogWheelParts().get(axis) || this == set.middleLargeCogWheelParts().get(axis) || this == set.middleCoaxialCogWheelParts().get(axis) || this == set.middleLargeCoaxialCogWheelParts().get(axis);
    };
    
};
