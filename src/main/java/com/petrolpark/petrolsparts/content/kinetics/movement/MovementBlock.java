package com.petrolpark.petrolsparts.content.kinetics.movement;

import com.petrolpark.compat.create.core.block.composite.CompositeKineticBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MovementBlock extends CompositeKineticBlock implements IBE<MovementBlockEntity> {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MovementBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    };

    @Override
    public Class<MovementBlockEntity> getBlockEntityClass() {
        return MovementBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends MovementBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.MOVEMENT.get();
    };
    
};
