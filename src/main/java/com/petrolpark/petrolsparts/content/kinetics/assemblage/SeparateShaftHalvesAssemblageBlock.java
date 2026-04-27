package com.petrolpark.petrolsparts.content.kinetics.assemblage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.petrolpark.compat.create.core.block.composite.MultiPartCompositeKineticBlock;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class SeparateShaftHalvesAssemblageBlock extends MultiPartCompositeKineticBlock<AssemblagePart> implements IBE<AssemblageBlockEntity>, ProperWaterloggedBlock, IAssemblageBlock {

    public SeparateShaftHalvesAssemblageBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(WATERLOGGED, false)
            .setValue(TOP_SHAFT_HALF, false)
            .setValue(BOTTOM_SHAFT_HALF, false)
            .setValue(TOP_COG, AssemblageCog.NONE)
            .setValue(MIDDLE_COG, AssemblageCog.NONE)
            .setValue(BOTTOM_COG, AssemblageCog.NONE)
        );
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        withBlockEntityDo(level, pos, AssemblageBlockEntity::invalidateParts);
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, AXIS, TOP_SHAFT_HALF, BOTTOM_SHAFT_HALF, TOP_COG, MIDDLE_COG, BOTTOM_COG);
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    };

    @Override
    public Collection<AssemblagePart> getParts(BlockState state) {
        final List<AssemblagePart> parts = new ArrayList<>(5);
        final Axis axis = state.getValue(AXIS);
        if (state.getValue(TOP_SHAFT_HALF)) parts.add(AssemblagePart.SHAFT_HALVES.get(Direction.get(AxisDirection.NEGATIVE, axis)));
        if (state.getValue(BOTTOM_SHAFT_HALF)) parts.add(AssemblagePart.SHAFT_HALVES.get(Direction.get(AxisDirection.POSITIVE, axis)));
        state.getValue(TOP_COG).addTopPart(axis, parts::add);
        state.getValue(MIDDLE_COG).addMiddlePart(axis, parts::add);
        state.getValue(BOTTOM_COG).addBottomPart(axis, parts::add);
        return parts;
    };

    @Override
    public BlockState withoutPart(BlockState state, AssemblagePart part) {
        state = part.remover.apply(state);
        if (state.getValue(TOP_COG).isNone() && state.getValue(MIDDLE_COG).isNone() && state.getValue(BOTTOM_COG).isNone() && !state.getValue(TOP_SHAFT_HALF) && !state.getValue(BOTTOM_SHAFT_HALF)) return Blocks.AIR.defaultBlockState(); //TODO water
        return state;
    };

    @Override
    public boolean hasTopShaft(BlockState state) {
        return state.getValue(TOP_SHAFT_HALF);
    };

    @Override
    public boolean hasBottomShaft(BlockState state) {
        return state.getValue(BOTTOM_SHAFT_HALF);
    };

    @Override
    public Class<AssemblageBlockEntity> getBlockEntityClass() {
        return AssemblageBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends AssemblageBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.SEPARATE_SHAFT_HALVES_ASSEMBLAGE.get();
    };
    
};
