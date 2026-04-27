package com.petrolpark.petrolsparts.content.kinetics.bevelGear;

import static com.petrolpark.petrolsparts.content.kinetics.bevelGear.BevelGearBlock.GEAR_SHAPE;
import static com.petrolpark.petrolsparts.content.kinetics.bevelGear.BevelGearBlock.LOOT;

import com.petrolpark.core.world.block.multiPart.MultiPartBlock;
import com.petrolpark.petrolsparts.content.kinetics.assemblage.AssemblagePart;
import com.simibubi.create.AllShapes;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum BevelGearPart implements MultiPartBlock.IPart {
    
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

    BevelGearPart(VoxelShape shape, ResourceKey<LootTable> loot) {
        this.shape = shape;
        this.loot = loot;
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
    
};
