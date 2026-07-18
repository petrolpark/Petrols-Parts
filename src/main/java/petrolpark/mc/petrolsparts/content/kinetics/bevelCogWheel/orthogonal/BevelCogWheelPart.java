package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.orthogonal;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.simibubi.create.AllShapes;
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
import petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IBevelCogWheelBlock;

public sealed abstract class BevelCogWheelPart implements CreateMultiPartBlock.ICreatePart permits BevelCogWheelPart.Cog, BevelCogWheelPart.Shaft {
    
    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;
    protected final ItemLike item;
    protected final Supplier<ItemRequirement> itemRequirement;

    public BevelCogWheelPart(VoxelShape shape, ResourceKey<LootTable> loot, ItemLike item) {
        this.shape = shape;
        this.loot = loot;
        this.item = item;
        this.itemRequirement = Suppliers.memoize(() -> new ItemRequirement(ItemUseType.CONSUME, item.asItem()));
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

    public static non-sealed class Cog extends BevelCogWheelPart {

        public final Direction face;

        public Cog(Direction face, ResourceKey<LootTable> loot, ItemLike item) {
            super(IBevelCogWheelBlock.COG_SHAPE.get(face), loot, item);
            this.face = face;
        };

        public boolean isTop() {
            return face.getAxisDirection() == AxisDirection.POSITIVE;
        };
    };

    public static non-sealed class Shaft extends BevelCogWheelPart {

        public final Axis axis;

        public Shaft(Axis axis, ResourceKey<LootTable> loot, ItemLike item) {
            super(AllShapes.SIX_VOXEL_POLE.get(axis), loot, item);
            this.axis = axis;
        };
    };
    
};
