package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.petrolpark.compat.create.core.block.composite.MultiPartCompositeKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class SingleShaftAssemblageBlock extends MultiPartCompositeKineticBlock<AssemblagePart> implements IBE<AssemblageBlockEntity>, ProperWaterloggedBlock, IAssemblageBlock {

    public SingleShaftAssemblageBlock(BlockBehaviour.Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, AXIS, TOP_COG, MIDDLE_COG, BOTTOM_COG);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public Collection<AssemblagePart> getParts(BlockState state) {
        final List<AssemblagePart> parts = new ArrayList<>(5);
        final Axis axis = state.getValue(AXIS);
        state.getValue(TOP_COG).addTopPart(axis, parts::add);
        state.getValue(MIDDLE_COG).addMiddlePart(axis, parts::add);
        state.getValue(BOTTOM_COG).addBottomPart(axis, parts::add);
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, AssemblagePart part) {
        return part.remover.apply(state);
    };

    @Override
    public boolean hasTopShaft(BlockState state) {
        return true;
    };

    @Override
    public boolean hasBottomShaft(BlockState state) {
        return true;
    };

    @Override
    public Class<AssemblageBlockEntity> getBlockEntityClass() {
        return AssemblageBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends AssemblageBlockEntity> getBlockEntityType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlockEntityType'");
    };
    
};
