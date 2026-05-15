package com.petrolpark.petrolsparts.content.kinetics.transmission;

import com.petrolpark.petrolsparts.core.block.CogType;
import com.petrolpark.petrolsparts.core.block.entity.IFaceAlignedCogWheelBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TransmissionBlockEntity extends KineticBlockEntity implements IFaceAlignedCogWheelBlockEntity {

    public TransmissionBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);  
    };

    @Override
    public CogType getCogType(Direction face) {
        final Direction facing = getBlockState().getValue(TransmissionBlock.FACING);
        if (face == facing) return CogType.small(getBlockState().getValue(TransmissionBlock.UPPER_COG));
        if (face == facing.getOpposite()) return CogType.small(getBlockState().getValue(TransmissionBlock.LOWER_COG));
        return CogType.NONE;
    };
    
};
