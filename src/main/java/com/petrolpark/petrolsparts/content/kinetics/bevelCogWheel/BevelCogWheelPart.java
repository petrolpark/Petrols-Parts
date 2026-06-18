package com.petrolpark.petrolsparts.content.kinetics.bevelCogWheel;

import static com.petrolpark.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelBlock.GEAR_SHAPE;
import static com.petrolpark.petrolsparts.content.kinetics.bevelCogWheel.BevelCogWheelBlock.LOOT;

import com.petrolpark.compat.create.core.block.CreateMultiPartBlock;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblagePart;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;

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

public enum BevelCogWheelPart implements CreateMultiPartBlock.ICreatePart {
    
    NORTH_GEAR(GEAR_SHAPE.get(Direction.NORTH), LOOT),
    SOUTH_GEAR(GEAR_SHAPE.get(Direction.SOUTH), LOOT),
    EAST_GEAR(GEAR_SHAPE.get(Direction.EAST), LOOT),
    WEST_GEAR(GEAR_SHAPE.get(Direction.WEST), LOOT),
    UP_GEAR(GEAR_SHAPE.get(Direction.UP), LOOT),
    DOWN_GEAR(GEAR_SHAPE.get(Direction.DOWN), LOOT),

    X_SHAFT(AllShapes.SIX_VOXEL_POLE.get(Axis.X), AssemblagePart.SHAFT_LOOT),
    Y_SHAFT(AllShapes.SIX_VOXEL_POLE.get(Axis.Y), AssemblagePart.SHAFT_LOOT),
    Z_SHAFT(AllShapes.SIX_VOXEL_POLE.get(Axis.Z), AssemblagePart.SHAFT_LOOT),
    ;

    protected final VoxelShape shape;
    protected final ResourceKey<LootTable> loot;

    BevelCogWheelPart(VoxelShape shape, ResourceKey<LootTable> loot) {
        this.shape = shape;
        this.loot = loot;
    };

    @Override
    public ItemStack cloneItemStack(BlockState state, LevelReader level, BlockPos pos, Player player) {
        // TODO Auto-generated method stub
        return null;
    };

    @Override
    public VoxelShape shape() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shape'");
    };

    @Override
    public ResourceKey<LootTable> loot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loot'");
    };

    @Override
    public ItemRequirement itemRequirement() {
        // TODO Auto-generated method stub
        return null;
    };
    
};
