package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.diagonal;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;
import petrolpark.mc.library.compat.create.core.world.block.multiPart.CreateMultiPartBlock.ICreatePart;
import petrolpark.mc.library.util.Orientation;
import petrolpark.mc.library.util.Orientation.OrientedVoxelShaper;
import petrolpark.mc.petrolsparts.PetrolsPartsShapes;

public sealed abstract class DiagonalBevelCogWheelPart implements ICreatePart permits DiagonalBevelCogWheelPart.Cog, DiagonalBevelCogWheelPart.ShaftHalf {

    //public static final Map<Orientation, DiagonalBevelCogWheelPart.Cog> COGS = Stream.of(Orientation.EDGE_ORIENTATIONS).collect(Collectors.toMap(Function.identity(), DiagonalBevelCogWheelPart.Cog::new));
    //public static final Map<Direction, DiagonalBevelCogWheelPart.ShaftHalf> SHAFT_HALVES = Stream.of(Direction.values()).collect(Collectors.toMap(Function.identity(), ShaftHalf::new));

    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;
    protected final ItemLike item;
    protected final Supplier<ItemRequirement> itemRequirement;

    public DiagonalBevelCogWheelPart(VoxelShape shape, ResourceKey<LootTable> loot, ItemLike item) {
        this.shape = shape;
        this.loot = loot;
        this.item = item;
        this.itemRequirement = Suppliers.memoize(() -> new ItemRequirement(ItemUseType.CONSUME, new ItemStack(item)));
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
    public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(item);
    };

    @Override
    public ItemRequirement itemRequirement() {
        return itemRequirement.get();
    };

    public static non-sealed class Cog extends DiagonalBevelCogWheelPart {

        public static final Orientation.OrientedVoxelShaper SHAPER = new OrientedVoxelShaper(Block.box(3d, 7d, 7d, 13d, 15d, 15d));

        public final Orientation orientation;

        public Cog(Orientation orientation, ResourceKey<LootTable> loot, ItemLike item) {
            super(SHAPER.get(orientation), loot, item);
            this.orientation = orientation;
        };

    };

    public static non-sealed class ShaftHalf extends DiagonalBevelCogWheelPart {

        public final Direction face;

        public ShaftHalf(Direction face, ResourceKey<LootTable> loot, ItemLike item) {
            super(PetrolsPartsShapes.SHAFT_HALF.get(face), loot, item);
            this.face = face;
        };
    };
    
};
