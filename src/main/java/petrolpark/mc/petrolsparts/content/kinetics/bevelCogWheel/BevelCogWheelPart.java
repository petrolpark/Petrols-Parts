package petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel;

import static petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IBevelCogWheelBlock.COG_SHAPE;
import static petrolpark.mc.petrolsparts.content.kinetics.bevelCogWheel.IBevelCogWheelBlock.LOOT;

import java.util.Map;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;
import petrolpark.mc.library.compat.create.core.world.block.CreateMultiPartBlock;
import petrolpark.mc.petrolsparts.PetrolsPartsItems;
import petrolpark.mc.petrolsparts.content.kinetics.assemblage.AssemblagePart;

public enum BevelCogWheelPart implements CreateMultiPartBlock.ICreatePart {
    
    NORTH_COG(true, COG_SHAPE.get(Direction.NORTH), LOOT),
    SOUTH_COG(true, COG_SHAPE.get(Direction.SOUTH), LOOT),
    EAST_COG(true, COG_SHAPE.get(Direction.EAST), LOOT),
    WEST_COG(true, COG_SHAPE.get(Direction.WEST), LOOT),
    WHATS_UPCOG(true, COG_SHAPE.get(Direction.UP), LOOT),
    DOWN_COG(true, COG_SHAPE.get(Direction.DOWN), LOOT),

    X_SHAFT(false, AllShapes.SIX_VOXEL_POLE.get(Axis.X), AssemblagePart.SHAFT_LOOT),
    Y_SHAFT(false, AllShapes.SIX_VOXEL_POLE.get(Axis.Y), AssemblagePart.SHAFT_LOOT),
    Z_SHAFT(false, AllShapes.SIX_VOXEL_POLE.get(Axis.Z), AssemblagePart.SHAFT_LOOT),
    ;

    public static final Map<Direction, BevelCogWheelPart> COGS = Map.of(Direction.NORTH, NORTH_COG, Direction.SOUTH, SOUTH_COG, Direction.EAST, EAST_COG, Direction.WEST, WEST_COG, Direction.UP, WHATS_UPCOG, Direction.DOWN, DOWN_COG);
    public static final Map<Axis, BevelCogWheelPart> SHAFTS = Map.of(Axis.X, X_SHAFT, Axis.Y, Y_SHAFT, Axis.Z, Z_SHAFT);

    protected final boolean cog;
    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;

    BevelCogWheelPart(boolean cog, VoxelShape shape, ResourceKey<LootTable> loot) {
        this.cog = cog;
        this.shape = shape;
        this.loot = loot;
    };

    @Override
    public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
        return (cog ? PetrolsPartsItems.BEVEL_COGWHEEL : AllBlocks.SHAFT).asStack();
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
        return new ItemRequirement(ItemUseType.CONSUME, (cog ? PetrolsPartsItems.BEVEL_COGWHEEL : AllBlocks.SHAFT).asStack());
    };

    public boolean isTopCog() {
        return this == SOUTH_COG || this == WHATS_UPCOG || this == EAST_COG;
    };
    
};
